package com.impassive.pay.entity.notify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.impassive.pay.entity.RenewInfo;
import com.impassive.pay.entity.TransactionInfo;
import lombok.Data;

/**
 * @author impassive
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotifyV2Data {

  private String appAppleId;
  private String bundleId;
  private String bundleVersion;
  private String environment;
  private String signedRenewalInfo;
  private RenewInfo renewalInfo;
  private String signedTransactionInfo;
  private TransactionInfo transactionInfo;

}
