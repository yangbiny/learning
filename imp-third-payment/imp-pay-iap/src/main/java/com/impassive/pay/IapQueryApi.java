package com.impassive.pay;

import com.impassive.pay.entity.IapQueryTransactionResult;
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
import java.util.List;
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
    List<IapQueryTransactionResult> iapQueryTransactionResults = queryIapTransactionHistory(
        originTransactionId);

    return null;
  }


  private List<IapQueryTransactionResult> queryIapTransactionHistory(String originTransactionId) {
    if (StringUtils.isEmpty(originTransactionId)) {
      return null;
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
      return null;
    }

    List<IapQueryTransactionResult> iapQueryResults = new ArrayList<>();
    for (String signedTransaction : response.getSignedTransactions()) {
      IapQueryTransactionResult iapQueryResult = parsePayload(signedTransaction,
          IapQueryTransactionResult.class);
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
