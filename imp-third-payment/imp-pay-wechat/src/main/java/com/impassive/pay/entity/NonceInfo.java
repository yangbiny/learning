package com.impassive.pay.entity;

import lombok.Data;

@Data
public class NonceInfo {

  private String timeStamp;

  private String nonceStr;

  public NonceInfo(String timeStamp, String nonceStr) {
    this.timeStamp = timeStamp;
    this.nonceStr = nonceStr;
  }
}