package com.impassive.pay.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 解密后的微信回调的信息
 *
 * @author impassivey
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeChatTradeInfo {

  @JsonProperty("appid")
  private String appId;

  @JsonProperty("mchid")
  private String mchId;

  /**
   * 我们自己的订单ID
   */
  @JsonProperty("out_trade_no")
  private String outTradeNo;

  /**
   * 微信支付订单号
   */
  @JsonProperty("transaction_id")
  private String transactionId;

  /**
   * 交易类型
   */
  @JsonProperty("trade_type")
  private String tradeType;

  @JsonProperty("trade_state")
  private String tradeState;

  @JsonProperty("trade_state_desc")
  private String tradeStateDesc;

  @JsonProperty("bank_type")
  private String bankType;

  private String attach;

  /**
   * 支付完成时间，遵循rfc3339标准格式，格式为YYYY-MM-DDTHH:mm:ss+TIMEZONE， YYYY-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，
   * HH:mm:ss表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC8小时，即北京时间）。
   * 例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日 13点29分35秒。
   */
  @JsonProperty("success_time")
  private String successTime;

  /**
   * 订单金额信息
   */
  private Amount amount;

  /**
   * 支付者的信息
   */
  private Payer payer;


  public String orderId() {
    return outTradeNo;
  }

}
