package com.impassive.pay.entity.receipt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.impassive.pay.entity.common.ReceiptItem;
import com.impassive.pay.entity.common.PendingRenewInfo;
import com.impassive.pay.entity.common.Receipt;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceiptInfo {

  private String environment;

  @JsonProperty("is_retryable")
  private Boolean isRetryable;

  @JsonProperty("latest_receipt")
  private String latestReceipt;

  @JsonProperty("latest_receipt_info")
  private List<ReceiptItem> latestReceiptInfo;

  @JsonProperty("pending_renewal_info")
  private List<PendingRenewInfo> pendingRenewInfos;

  private Receipt receipt;

  private Integer status;

  public boolean isSandBox() {
    return status != null && status == 21007;
  }

  public boolean needTryAgain() {
    return status != null && (status == 21002 || status == 21005);
  }

}