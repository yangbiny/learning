package com.impassive.pay.entity;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum AliTradeStatus {
  /**
   * 交易创建，等待买家付款
   */
  WAIT_BUYER_PAY("WAIT_BUYER_PAY"),
  /**
   * 未付款交易超时关闭，或支付完成后全额退款
   */
  TRADE_CLOSED("TRADE_CLOSED"),
  /**
   * 交易支付成功
   * <p>
   * 通知触发条件是商户签约的产品支持退款功能的前提下，买家付款成功。
   * </p>
   */
  TRADE_SUCCESS("TRADE_SUCCESS"),

  /**
   * 交易结束，不可退款
   *
   * <p>
   * 通知触发条件是商户签约的产品不支持退款功能的前提下，买家付款成功； 或者，商户签约的产品支持退款功能的前提下，交易已经成功并且已经超过可退款期限。
   * </p>
   */
  TRADE_FINISHED("TRADE_FINISHED");

  private final String aliStatus;

  AliTradeStatus(String aliStatus) {
    this.aliStatus = aliStatus;
  }

  public static AliTradeStatus of(String status) {
    for (AliTradeStatus value : AliTradeStatus.values()) {
      if (StringUtils.equalsIgnoreCase(status, value.aliStatus)) {
        return value;
      }
    }
    return null;
  }

  public boolean paySuccess() {
    return this == TRADE_FINISHED || this == TRADE_SUCCESS;
  }


}