package com.impassive.pay.cmd;

import com.impassive.pay.core.Rmb;
import com.impassive.pay.entity.AliProductCode;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Getter
@Builder
@ToString
public class CreatePaySignCmd {

  // 自己的 支付ID
  private String paymentNo;

  // 商品 名称
  private String inventoryName;

  // 支付金额
  private Rmb amount;

  /**
   * Ali 支付的 code
   */
  private AliProductCode productCode;

  public boolean checkIsIllegal() {
    return StringUtils.isAnyEmpty(paymentNo, inventoryName)
        || amount == null || amount.asFen() < 0
        || productCode == null;
  }
}