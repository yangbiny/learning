package com.impassive.pay.core;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * @author impassive
 */
@Getter
public class Rmb {

  /**
   * 钱的数量。单位 为 分
   */
  private int value;

  private Rmb(int value) {
    this.value = value;
  }

  public static Rmb ofFen(@NotNull Integer val) {
    if (val < 0) {
      throw new IllegalArgumentException("price can not be negative " + val);
    }
    return new Rmb(val);
  }

  public int asFen() {
    return value;
  }
}
