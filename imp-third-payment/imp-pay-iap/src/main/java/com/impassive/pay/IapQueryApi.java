package com.impassive.pay;

import com.impassive.pay.entity.IapQueryTransactionDecoder;
import com.impassive.pay.entity.notify.IapServiceNotifyV1;
import com.impassive.pay.entity.IapSignHeader;
import com.impassive.pay.entity.OriginTransactionIdResponse;
import com.impassive.pay.result.PaymentResult;
import com.impassive.pay.tools.HttpExecuteResult;
import com.impassive.pay.tools.JsonTools;
import com.impassive.pay.tools.OkHttpExecutor;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * Apple App Store 应用内购买 查询服务的 API入口，需要开通App Store Service 服务才可以使用
 *
 * @author impassive
 */
@Slf4j
@RequiredArgsConstructor
public class IapQueryApi {

  private static final String payCheckUrl = "https://api.storekit.itunes.apple.com/inApps/v1/history/%s?sort=DESCENDING";
  private static final String payCheckSandBoxUrl = "https://api.storekit-sandbox.itunes.apple.com/inApps/v1/history/%s?sort=DESCENDING";
  private final OkHttpExecutor okHttpExecutor;
  private final IapProperties iapProperties;

  public PaymentResult checkPaymentResult(String originTransactionId, String transactionId) {
    if (StringUtils.isAnyEmpty(originTransactionId, transactionId)) {
      return null;
    }
    List<IapQueryTransactionDecoder> iapQueryTransactionRespons =
        queryIapTransactionHistory(originTransactionId);

    Optional<IapQueryTransactionDecoder> first = iapQueryTransactionRespons.stream()
        .filter(item -> StringUtils.equals(transactionId, item.getTransactionId()))
        .findFirst();

    if (first.isEmpty()) {
      return PaymentResult.failed(transactionId);
    }
    IapQueryTransactionDecoder iapQueryTransactionDecoder = first.get();
    return PaymentResult.success(
        iapQueryTransactionDecoder.getTransactionId(),
        iapQueryTransactionDecoder.getExpiresDate(),
        iapQueryTransactionDecoder.getProductId()
    );
  }

  /**
   * 解析 IAP 第一个版本的通知
   *
   * @param body 通知的全部信息
   * @return 解析结果。如果 验证失败 或者 通知为空，则会返回null
   */
  @Nullable
  public IapServiceNotifyV1 serviceNotifyV1(String body) {
    IapServiceNotifyV1 iapServiceNotifyV1 = JsonTools.fromJson(body, IapServiceNotifyV1.class);
    return iapServiceNotifyV1 != null &&
        StringUtils.equals(iapServiceNotifyV1.getPassword(), iapProperties.getPassword())
        ?
        iapServiceNotifyV1 : null;
  }


  private List<IapQueryTransactionDecoder> queryIapTransactionHistory(String originTransactionId) {
    if (StringUtils.isEmpty(originTransactionId)) {
      return Collections.emptyList();
    }
    // apple建议先直接调用线上，如果是沙箱环境再调用沙箱环境
    // 1. 发送请求到apple
    OriginTransactionIdResponse response = null;
    int index = 3;
    while (index >= 0 && response == null) {
      response = queryApplePayHistory(originTransactionId, false);
      // 2. 验证返回值是否是沙箱环境
      if (response != null && response.isSandBox()) {
        // 2.1 是沙箱环境重新请求沙箱环境
        response = queryApplePayHistory(originTransactionId, true);
      }
      index--;
    }

    if (response == null) {
      return Collections.emptyList();
    }

    List<IapQueryTransactionDecoder> iapQueryResults = new ArrayList<>();
    for (String signedTransaction : response.getSignedTransactions()) {
      IapQueryTransactionDecoder iapQueryResult = parsePayload(signedTransaction,
          IapQueryTransactionDecoder.class);
      iapQueryResults.add(iapQueryResult);
    }
    return iapQueryResults;
  }


  @Nullable
  private OriginTransactionIdResponse queryApplePayHistory(String originTransaction,
      Boolean isSandBox) {
    String usedUrl = payCheckUrl;
    if (isSandBox) {
      usedUrl = payCheckSandBoxUrl;
    }
    HttpExecuteResult httpExecuteResult = okHttpExecutor.executeWithGet(
        String.format(usedUrl, originTransaction));
    if (httpExecuteResult.failed()) {
      return null;
    }
    return JsonTools.fromJson(httpExecuteResult.body(), OriginTransactionIdResponse.class);
  }

  @Nullable
  private <T> T parsePayload(String jwsContent, Class<T> classType) {
    if (StringUtils.isEmpty(jwsContent)) {
      return null;
    }
    String[] split = jwsContent.split("\\.");
    // 载荷信息
    String payload = new String(Base64.getUrlDecoder().decode(split[1]));
    if (!verify(jwsContent)) {
      log.error("验证失败 ： {}", jwsContent);
      return null;
    }
    log.info("payload : {}", payload);
    return JsonTools.fromJson(payload, classType);
  }

  private boolean verify(String jwsContent) {
    String[] split = jwsContent.split("\\.");
    byte[] decode = Base64.getUrlDecoder().decode(split[0]);
    String headerStr = new String(decode);
    IapSignHeader iapSignHeader = JsonTools.fromJson(headerStr, IapSignHeader.class);
    if (iapSignHeader == null) {
      log.error("parse has  error : {}", jwsContent);
      return false;
    }
    try {
      // 验证证书
      List<String> x5c = iapSignHeader.getX5c();
      String last = x5c.get(x5c.size() - 1);
      if (!StringUtils.equals(last, iapProperties.getG3Root())) {
        log.error("证书不一致 : {}", jwsContent);
        return true;
      }
      for (int i = 1; i < x5c.size(); i++) {
        X509Certificate before = build(x5c.get(i - 1));
        X509Certificate after = build(x5c.get(i));
        boolean verify = verify(before, after);
        if (!verify) {
          log.error("证书验证失败 : {}", jwsContent);
          return true;
        }
      }

      // 验证签名
      Signature signer = Signature.getInstance("SHA256withECDSAinP1363Format");
      String certificateContent = iapSignHeader.getX5c().get(0);
      X509Certificate certificate = build(certificateContent);
      signer.initVerify(certificate);
      byte[] signContent = (split[0] + "." + split[1]).getBytes(StandardCharsets.UTF_8);
      signer.update(signContent);
      boolean verify = signer.verify(Base64.getUrlDecoder().decode(split[2]));
      if (!verify) {
        log.error("签名验证失败 ：{}", jwsContent);
        throw new RuntimeException("签名验证失败");
      }
      return true;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private boolean verify(X509Certificate before, X509Certificate after) {
    String issuerX500Principal = before.getIssuerX500Principal().getName();
    String name = after.getSubjectX500Principal().getName();
    if (!StringUtils.equals(issuerX500Principal, name)) {
      return false;
    }
    try {
      before.verify(after.getPublicKey());
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  private X509Certificate build(String certContent) throws CertificateException {
    String format = String.format("-----BEGIN CERTIFICATE-----\n"
        + "%s"
        + "-----END CERTIFICATE-----", certContent);
    CertificateFactory x509 = CertificateFactory.getInstance("x509");
    ByteArrayInputStream inStream = new ByteArrayInputStream(
        format.getBytes(StandardCharsets.UTF_8));
    return (X509Certificate) x509.generateCertificate(inStream);
  }

}
