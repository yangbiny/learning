package com.impassive.pay.cmd;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class WeChatSignReqCmd {

  private String timestamp;

  /**
   * 微信传入的随机字符串
   */
  private String nonce;

  /**
   * 微信传入的签名
   */
  private String signature;

  /**
   * 证书序列号
   */
  private String serial;

  public boolean checkIsIllegal() {
    return StringUtils.isAnyEmpty(timestamp, nonce, serial, serial);
  }
}