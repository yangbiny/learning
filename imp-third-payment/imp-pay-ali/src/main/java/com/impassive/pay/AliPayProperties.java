package com.impassive.pay;

import java.time.Duration;
import lombok.Data;

/**
 * @author impassive
 */
@Data
public class AliPayProperties {

  private String aliAppId;
  private String aliPrivateKey;
  private String aliPublicKey;
  private String encryptKey;
  private String payNotifyUrl;
  private String subNotifyUrl;
  private String sellerId;

  /**
   * 订单超时时间
   */
  private Duration orderTimeOutExpress = Duration.ofMinutes(5);

}
