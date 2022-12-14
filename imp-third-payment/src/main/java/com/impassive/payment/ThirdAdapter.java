package com.impassive.payment;

import com.impassive.payment.core.PaymentSign;

/**
 * @author impassive
 */
public interface ThirdAdapter {

  /**
   * 申请交易的签名信息。
   *
   * @return 交易的签名信息
   */
  PaymentSign applyTradeSign();

}
