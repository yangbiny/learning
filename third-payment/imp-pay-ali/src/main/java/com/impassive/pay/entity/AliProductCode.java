package com.impassive.pay.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AliProductCode {
  QUICK_MSECURITY_PAY(1, "无线快捷支付产品", "QUICK_MSECURITY_PAY"),
  CYCLE_PAY_AUTH(2, "周期扣款产品", "CYCLE_PAY_AUTH");

  private final Integer code;
  private final String desc;
  private final String value;

}