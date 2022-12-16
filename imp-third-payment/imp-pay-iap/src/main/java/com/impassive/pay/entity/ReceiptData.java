package com.impassive.pay.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * @author impassive
 */
public class ReceiptData {

  private String environment;

  private Receipt receipt;

  @JsonProperty("latest_receipt")
  private String latestReceipt;

  @JsonProperty("pending_renewal_info")
  private List<PendingRenewInfo> pendingRenewInfos;

  @JsonProperty("latest_receipt_info")
  private List<InApp> latestReceiptInfo;

  private Integer status;

}
