package com.impassive.pay.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.annotation.Nullable;
import lombok.Data;

/**
 * @author impassive
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceiptData {

  private String environment;
  @Nullable
  private Receipt receipt;
  @JsonProperty("latest_receipt")
  private String latestReceipt;
  @JsonProperty("pending_renewal_info")
  private List<PendingRenewInfo> pendingRenewInfos;
  @JsonProperty("latest_receipt_info")
  private List<InApp> latestReceiptInfo;
  private Integer status;

}
