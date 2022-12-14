package com.impassive.payment.core;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentSign {

  // 支付ID
  public final String paymentNo;
  // 签名信息
  public final String sign;
  // 三方订单ID
  public final String thirdOrderId;

  // 下面是 微信 特有的信息。用于 验证签名
  public final String timestamp;

  public final String nonceStr;

}