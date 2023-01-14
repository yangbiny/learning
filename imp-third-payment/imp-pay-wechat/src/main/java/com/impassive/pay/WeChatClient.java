package com.impassive.pay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.impassive.pay.cmd.CreatePaySignCmd;
import com.impassive.pay.entity.NonceInfo;
import com.impassive.pay.result.Sign;
import com.impassive.pay.tools.HttpExecuteResult;
import com.impassive.pay.tools.JsonTools;
import com.impassive.pay.tools.OkHttpExecutor;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;
import java.util.Random;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

/**
 * @author impassive
 */
@Slf4j
@RequiredArgsConstructor
public class WeChatClient {

  private static final String WEI_CHAT_PAY_API = "https://api.mch.weixin.qq.com/v3/pay/transactions/app";


  private final Random random = new Random();

  private final WeChatPayProperties weChatPayProperties;

  private final OkHttpExecutor okHttpExecutor;

  public Sign applyPaymentSign(CreatePaySignCmd createPaySignCmd) {
    String timestamp = buildTimestamp();
    String nonceStr = buildNonceStr();
    NonceInfo nonceInfo = new NonceInfo(timestamp, nonceStr);
    WeiChatPayForm weiChatPayForm = new WeiChatPayForm(
        weChatPayProperties.getAppId(),
        weChatPayProperties.getPayNotifyUrl(),
        createPaySignCmd.getPaymentNo(),
        createPaySignCmd.getDescribe(),
        createPaySignCmd.getAmount().asFen(),
        weChatPayProperties.getPartnerId()
    );
    WeiChatPrepayId weiChatPrepayId = sendToWeiChatForPerPayId(weiChatPayForm, nonceInfo);
    if (weiChatPrepayId == null || StringUtils.isEmpty(weiChatPrepayId.prepayId)) {
      log.error("prepay result is null : {}", createPaySignCmd);
      throw new RuntimeException("prepay is null ");
    }
    String sign = sign(timestamp, nonceStr, weiChatPrepayId.prepayId);
    return new Sign(
        sign,
        weiChatPrepayId.prepayId,
        timestamp,
        nonceStr
    );
  }


  /**
   * 返回给客户端的签名
   *
   * @param timestamp 时间戳
   * @param nonceStr 随机字符串
   * @param prepayId 预支付交易会话ID
   * @return 签名信息
   */
  private String sign(String timestamp, String nonceStr, String prepayId) {
    // 1. 打开配置的证书的信息（证书内容）
    try {
      String message = weChatPayProperties.getAppId() + "\n"
          + timestamp + "\n"
          + nonceStr + "\n"
          + prepayId + "\n";
      Signature signer = Signature.getInstance("SHA256withRSA");
      PrivateKey privateKey = PemUtil.loadPrivateKey(
          new ByteArrayInputStream(weChatPayProperties.getPrivateKey().getBytes(
              StandardCharsets.UTF_8)));
      signer.initSign(privateKey);
      signer.update(message.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(signer.sign());
    } catch (Exception e) {
      log.error("sign has error : {},{},{}", timestamp, nonceStr, prepayId);
      log.error("exception : ", e);
      throw new RuntimeException("sign has error : ", e);
    }
  }

  /**
   * 发送请求到微信，以生成微信的perPayId
   *
   * @param weiChatPayForm 请求信息
   * @return perPayId 的信息
   */
  @Nullable
  private WeiChatPrepayId sendToWeiChatForPerPayId(
      WeiChatPayForm weiChatPayForm,
      NonceInfo nonceInfo
  ) {
    String requestBody = JsonTools.toJson(weiChatPayForm);
    String string = executeWeiChatRequest(WEI_CHAT_PAY_API, "POST", requestBody, nonceInfo);
    if (string == null) {
      return null;
    }
    if (log.isDebugEnabled()) {
      log.debug("receive wei chat info : {}, form = {}", string, weiChatPayForm);
    }
    return JsonTools.fromJson(string, WeiChatPrepayId.class);
  }

  /**
   * 执行微信的请求。会进行签名和验签
   * <p>如果签名、验签失败或者执行请求失败均会返回null</p>
   *
   * @param urlStr 需要执行的完整的url
   * @param method 执行URL的方法：POST或者GET
   * @param requestBodyStr 执行请求的请求体：如果为get，直接传null，post不允许为Null
   * @param nonceInfo 随机字符串的信息
   * @return 返回值信息：原样返回，未处理
   */
  private String executeWeiChatRequest(
      String urlStr,
      String method,
      @Nullable String requestBodyStr,
      NonceInfo nonceInfo
  ) {
    HttpUrl url = HttpUrl.parse(urlStr);
    if (url == null) {
      return null;
    }
    String timestamp = nonceInfo.getTimeStamp();
    String nonceStr = nonceInfo.getNonceStr();
    String sign = signForAccessWeiChat(url, method, timestamp, nonceStr, requestBodyStr);
    String authorization = "mchid=\"" + weChatPayProperties.getPartnerId() + "\","
        + "nonce_str=\"" + nonceStr + "\","
        + "timestamp=\"" + timestamp + "\","
        + "serial_no=\"" + weChatPayProperties.getApiSerNo() + "\","
        + "signature=\"" + sign + "\"";

    // 构建请求
    Headers headers = new Headers.Builder()
        .add("Authorization", "WECHATPAY2-SHA256-RSA2048 " + authorization)
        .add("Accept", "application/json")
        .build();

    HttpExecuteResult result = okHttpExecutor.execute(urlStr, method, requestBodyStr, headers);

    if (result.failed()) {
      log.error("request body is failed : {},url = {},method = {},requestBody = {}",
          result,
          urlStr,
          method,
          requestBodyStr
      );
      return null;
    }
    return result.body();
  }


  /**
   * 访问微信时需要的签名信息
   *
   * @param url 访问的url
   * @param method 访问的方法：post一类
   * @param timestamp 时间戳
   * @param nonceStr 随机字符串
   * @param body 信息实体
   * @return 签名信息
   */
  private String signForAccessWeiChat(
      HttpUrl url,
      String method,
      String timestamp,
      String nonceStr,
      String body
  ) {
    // 1. 打开配置的证书的信息（证书内容）
    try {
      if (StringUtils.isEmpty(body)) {
        body = "";
      }
      Signature signer = Signature.getInstance("SHA256withRSA");
      PrivateKey privateKey = PemUtil.loadPrivateKey(
          new ByteArrayInputStream(weChatPayProperties.getPrivateKey().getBytes(
              StandardCharsets.UTF_8)));
      signer.initSign(privateKey);
      signer.update(buildMessage(url, method, timestamp, nonceStr, body));
      return Base64.getEncoder().encodeToString(signer.sign());
    } catch (Exception e) {
      log.error("sign has error : {},{},{},{},{}", url, method, timestamp, nonceStr, body);
      log.error("exception : ", e);
      throw new RuntimeException("sign has error : ", e);
    }
  }

  private byte[] buildMessage(
      HttpUrl url,
      String method,
      String timestamp,
      String nonceStr,
      String body
  ) {
    String canonicalUrl = url.encodedPath();
    if (url.encodedQuery() != null) {
      canonicalUrl += "?" + url.encodedQuery();
    }
    String message = method + "\n"
        + canonicalUrl + "\n"
        + timestamp + "\n"
        + nonceStr + "\n"
        + body + "\n";
    return message.getBytes(StandardCharsets.UTF_8);
  }

  /**
   * 构建一个时间戳字符串
   *
   * @return 时间戳字符串
   */
  private String buildTimestamp() {
    return System.currentTimeMillis() / 1000 + "";
  }

  /**
   * 构建一个随机字符串
   *
   * @return 随机字符串
   */
  private String buildNonceStr() {
    long val = random.nextLong();
    String res = DigestUtils.md5Hex(val + "yzx").toUpperCase();
    if (32 < res.length()) {
      return res.substring(0, 32);
    } else {
      return res;
    }
  }

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  static class WeiChatPrepayId {

    @JsonProperty("prepay_id")
    private String prepayId;

  }

  /**
   * 微信预支付请求信息
   */
  @Getter
  @ToString
  private static class WeiChatPayForm {

    @JsonProperty("mchid")
    private final String mchId;

    @JsonProperty("appid")
    private final String appId;

    @JsonProperty("notify_url")
    private final String notifyUrl;

    @JsonProperty("out_trade_no")
    private String outTradeNo;

    private final String description;

    private final Amount amount;

    public WeiChatPayForm(
        String appId,
        String notifyUrl,
        String paymentNo,
        String inventoryName,
        Integer totalPay,
        String mchId
    ) {
      this.appId = appId;
      this.notifyUrl = notifyUrl;
      this.outTradeNo = paymentNo;
      this.description = inventoryName;
      this.mchId = mchId;
      this.amount = new Amount(totalPay);
    }

    @Getter
    static class Amount {

      private final String currency = "CNY";

      private final Integer total;

      public Amount(Integer total) {
        this.total = total;
      }
    }
  }


}
