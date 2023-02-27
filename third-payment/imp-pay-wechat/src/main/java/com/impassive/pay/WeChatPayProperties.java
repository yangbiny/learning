package com.impassive.pay;

import lombok.Data;

@Data
public class WeChatPayProperties {

  /**
   * 通知地址
   */
  private String payNotifyUrl;

  /**
   * 平台证书
   */
  private String certPerm;

  /**
   * 商户API私钥
   */
  private String privateKey;

  private String partnerId;

  private String apiSerNo;

  private String wePaySerNo;

  private String apiV3Key;

  private String appId;
}