package com.impassive.pay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.impassive.pay.entity.IapQueryTransactionInfo;
import com.impassive.pay.entity.IapSignHeader;
import com.impassive.pay.entity.OriginTransactionIdResponse;
import com.impassive.pay.entity.RenewInfo;
import com.impassive.pay.entity.TransactionInfo;
import com.impassive.pay.entity.notify.IapServiceNotifyV1;
import com.impassive.pay.entity.notify.IapServiceNotifyV2;
import com.impassive.pay.entity.notify.NotifyV2Data;
import com.impassive.pay.entity.receipt.ReceiptInfo;
import com.impassive.pay.entity.subscribe.IapLastTransactionData;
import com.impassive.pay.entity.subscribe.IapSubscribeQueryResponse;
import com.impassive.pay.entity.subscribe.OriginDataResponse;
import com.impassive.pay.tools.HttpExecuteResult;
import com.impassive.pay.tools.JsonTools;
import com.impassive.pay.tools.OkHttpExecutor;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Apple App Store 应用内购买 查询服务的 API入口，需要开通App Store Service 服务才可以使用
 *
 * @author impassive
 */
@Slf4j
@RequiredArgsConstructor
public class IapQueryClient {

  private static final String receiptUrl = "https://buy.itunes.apple.com/verifyReceipt";

  private static final String receiptSandBoxUrl = "https://sandbox.itunes.apple.com/verifyReceipt";

  private static final String payCheckUrl = "https://api.storekit.itunes.apple.com/inApps/v1/history/%s?sort=DESCENDING";

  private static final String payCheckSandBoxUrl = "https://api.storekit-sandbox.itunes.apple.com/inApps/v1/history/%s?sort=DESCENDING";

  private static final String subscribeCheckUrl = "https://api.storekit.itunes.apple.com/inApps/v1/subscriptions/%s";

  private static final String subscribeCheckSandBoxUrl = " https://api.storekit-sandbox.itunes.apple.com/inApps/v1/subscriptions/%s";

  private static final String orderCheckUrl = "https://api.storekit.itunes.apple.com/inApps/v1/lookup/%s";

  private final OkHttpExecutor okHttpExecutor;
  private final IapProperties iapProperties;


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

  @Nullable
  public IapServiceNotifyV2 serviceNotifyV2(String body) {
    if (StringUtils.isEmpty(body)) {
      throw new IllegalArgumentException("body 不能为空");
    }
    IapServiceNotifyV2 iapServiceNotifyV2 = parsePayload(body, IapServiceNotifyV2.class);
    if (iapServiceNotifyV2 == null || iapServiceNotifyV2.illegalNotify()) {
      log.warn("illegal notify : {}", body);
      return null;
    }
    NotifyV2Data data = iapServiceNotifyV2.getData();
    RenewInfo renewInfo = parsePayload(data.getSignedRenewalInfo(), RenewInfo.class);
    TransactionInfo transactionInfo = parsePayload(data.getSignedTransactionInfo(),
        TransactionInfo.class);
    if (renewInfo == null || transactionInfo == null) {
      log.error("parse data has failed : {}, {}", body, iapServiceNotifyV2);
      return null;
    }
    data.setRenewalInfo(renewInfo);
    data.setTransactionInfo(transactionInfo);
    return iapServiceNotifyV2;
  }

  @Nullable
  public ReceiptInfo queryTransactionInfoWithReceipt(String receipt) {
    if (StringUtils.isEmpty(receipt)) {
      log.warn("receipt 不能为空");
      return null;
    }
    return queryReceiptInfo(receipt);
  }

  public List<IapQueryTransactionInfo> queryIapTransactionHistory(String originTransactionId) {
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

    List<IapQueryTransactionInfo> iapQueryResults = new ArrayList<>();
    for (String signedTransaction : response.getSignedTransactions()) {
      IapQueryTransactionInfo iapQueryResult = parsePayload(signedTransaction,
          IapQueryTransactionInfo.class);
      iapQueryResults.add(iapQueryResult);
    }
    return iapQueryResults;
  }

  @Nullable
  public IapSubscribeQueryResponse queryIapSubscribe(String originTransactionId) {
    if (StringUtils.isEmpty(originTransactionId)) {
      log.error("origin transaction 不能为空");
      return null;
    }
    // apple建议先直接调用线上，如果是沙箱环境再调用沙箱环境
    // 1. 发送请求到apple
    IapSubscribeQueryResponse response = null;
    int index = 3;
    while (index >= 0 && response == null) {
      response = queryAppleSubscribeStatus(originTransactionId, false);
      // 2. 验证返回值是否是沙箱环境
      if (response != null && response.isSandBox()) {
        // 2.1 是沙箱环境重新请求沙箱环境
        response = queryAppleSubscribeStatus(originTransactionId, true);
      }
      index--;
    }

    if (response == null) {
      return null;
    }

    List<OriginDataResponse> dataList = response.getData();
    for (OriginDataResponse data : dataList) {
      for (IapLastTransactionData lastTransactionData : data.getLastTransactions()) {
        TransactionInfo iapQueryTransactionResult = parsePayload(
            lastTransactionData.getSignedTransactionInfo(),
            TransactionInfo.class
        );
        RenewInfo iapQueryRenewResult = parsePayload(
            lastTransactionData.getSignedRenewalInfo(),
            RenewInfo.class
        );
        lastTransactionData.setTransactionInfo(iapQueryTransactionResult);
        lastTransactionData.setRenewalInfo(iapQueryRenewResult);
      }
    }
    return response;
  }

  public List<IapQueryTransactionInfo> queryWithOrderId(String orderId) {
    String url = String.format(orderCheckUrl, orderId);
    HttpExecuteResult httpExecuteResult = executeGet(url);
    if (httpExecuteResult.failed()) {
      return Collections.emptyList();
    }

    String body = httpExecuteResult.body();
    OrderResponse orderResponse = JsonTools.fromJson(body, OrderResponse.class);
    if (orderResponse == null || orderResponse.checkIsFailed()) {
      return Collections.emptyList();
    }

    List<IapQueryTransactionInfo> results = new ArrayList<>();
    for (String signedTransaction : orderResponse.signedTransactions) {
      IapQueryTransactionInfo iapQueryTransactionResult = parsePayload(
          signedTransaction,
          IapQueryTransactionInfo.class
      );
      results.add(iapQueryTransactionResult);
    }
    return results;
  }

  private HttpExecuteResult executeGet(String url) {
    Headers headers = new Headers.Builder()
        .add("Authorization", String.format("Bearer %s", generateSign()))
        .build();
    return okHttpExecutor.executeWithGet(url, headers);
  }


  @Nullable
  private ReceiptInfo queryReceiptInfo(String receiptData) {
    if (StringUtils.isEmpty(receiptData)) {
      return null;
    }
    // apple建议先直接调用线上，如果是沙箱环境再调用沙箱环境
    // 1. 发送请求到apple
    ReceiptInfo receiptInfo = null;
    int index = 3;
    while (index >= 0 && (receiptInfo == null || receiptInfo.needTryAgain())) {
      receiptInfo = sendReceiptToApple(receiptData, false);
      // 2. 验证返回值是否是沙箱环境
      if (receiptInfo != null && receiptInfo.isSandBox()) {
        // 2.1 是沙箱环境重新请求沙箱环境
        receiptInfo = sendReceiptToApple(receiptData, true);
      }
      index--;
    }
    return receiptInfo == null ? null : receiptInfo.needTryAgain() ? null : receiptInfo;
  }

  @Nullable
  private ReceiptInfo sendReceiptToApple(String receiptData, Boolean isSandBox) {
    String usedUrl = receiptUrl;
    if (isSandBox) {
      usedUrl = receiptSandBoxUrl;
    }
    IapRequestBody iapRequestBody = new IapRequestBody(receiptData, iapProperties.getPassword());

    HttpExecuteResult httpExecuteResult = okHttpExecutor.executeWithPost(usedUrl,
        JsonTools.toJson(iapRequestBody));
    if (httpExecuteResult == null) {
      return null;
    }
    String string = httpExecuteResult.body();
    if (log.isDebugEnabled()) {
      log.debug("received info : {}", string);
    }
    return JsonTools.fromJson(string, ReceiptInfo.class);
  }


  @Nullable
  private OriginTransactionIdResponse queryApplePayHistory(String originTransaction,
      Boolean isSandBox) {
    String usedUrl = payCheckUrl;
    if (isSandBox) {
      usedUrl = payCheckSandBoxUrl;
    }
    HttpExecuteResult httpExecuteResult = executeGet(String.format(usedUrl, originTransaction));
    if (httpExecuteResult.failed()) {
      return null;
    }
    return JsonTools.fromJson(httpExecuteResult.body(), OriginTransactionIdResponse.class);
  }

  @Nullable
  private IapSubscribeQueryResponse queryAppleSubscribeStatus(String originTransaction,
      Boolean isSandBox) {
    String usedUrl = subscribeCheckUrl;
    if (isSandBox) {
      usedUrl = subscribeCheckSandBoxUrl;
    }
    HttpExecuteResult httpExecuteResult = executeGet(String.format(usedUrl, originTransaction));
    if (httpExecuteResult == null || StringUtils.isEmpty(httpExecuteResult.body())) {
      return null;
    }
    return JsonTools.fromJson(httpExecuteResult.body(), IapSubscribeQueryResponse.class);
  }

  @Nullable
  private <T> T parsePayload(String jwsContent, Class<T> classType) {
    if (StringUtils.isEmpty(jwsContent)) {
      return null;
    }
    if (!verify(jwsContent)) {
      log.error("验证失败 ： {}", jwsContent);
      return null;
    }
    String[] split = jwsContent.split("\\.");
    // 载荷信息
    String payload = new String(Base64.getUrlDecoder().decode(split[1]));
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

  private String generateSign() {
    long epochSecond = Instant.now().getEpochSecond();
    // TODO
    String sign = String.format("""
        {
          "iss": "%s",  "iat": %s,
          "exp": %s,
          "aud": "appstoreconnect-v1",
          "bid": "%s"
          
        }
        """, iapProperties.getIssId(), epochSecond, epochSecond + 5 * 60, iapProperties.getBid());
    return Jwts.builder()
        .setHeaderParam("alg", "ES256")
        .setHeaderParam("kid", iapProperties.getKeyId())
        .setHeaderParam("typ", "JWT")
        .signWith(SignatureAlgorithm.ES256, generatePrivateKey())
        .setPayload(sign)
        .compact();
  }

  private PrivateKey generatePrivateKey() {
    try {
      Security.addProvider(new BouncyCastleProvider());
      KeyFactory keyFactory = KeyFactory.getInstance("ECDH", "BC");
      byte[] devicePriKeyBytes = Base64.getDecoder().decode(iapProperties.getPrivateKey());
      PKCS8EncodedKeySpec devicePriKeySpec = new PKCS8EncodedKeySpec(devicePriKeyBytes);
      return keyFactory.generatePrivate(devicePriKeySpec);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Getter
  @JsonInclude(Include.NON_NULL)
  private static class IapRequestBody {

    @JsonProperty("receipt-data")
    private final String receiptData;

    private final String password;

    /**
     * 为true的时候，只会包含最后一次的续订的信息，不会包含已经发生过的续订的信息
     */
    @Setter
    @JsonProperty("exclude-old-transactions")
    private Boolean excludeOldTransactions;

    public IapRequestBody(String receiptData, String password) {
      this.receiptData = receiptData;
      this.password = password;
      this.excludeOldTransactions = true;
    }

  }

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  private static class OrderResponse {

    private Integer status;

    private List<String> signedTransactions;

    public boolean checkIsFailed() {
      return status == 1;
    }
  }
}
