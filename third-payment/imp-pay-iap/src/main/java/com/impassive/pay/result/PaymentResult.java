package com.impassive.pay.result;

public record PaymentResult(
    boolean paySuccess, /* 交易是否成功 */
    String transactionId, /* 交易的三方ID */
    Long transactionTime, /* 交易的时间 */
    String productId /* 交易的商品ID */

) {

  public static PaymentResult failed(String transactionId) {
    return new PaymentResult(
        false,
        transactionId,
        -1L,
        ""
    );
  }

  public static PaymentResult success(
      String transactionId,
      Long transactionTime,
      String productId
  ) {
    return new PaymentResult(true, transactionId, transactionTime, productId);
  }

}
