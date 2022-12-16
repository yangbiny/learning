package com.impassive.pay.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author impassive
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PendingRenewInfo {

  @JsonProperty("is_in_billing_retry_period")
  private String isInBillingRetryPeriod;

  @JsonProperty("original_transaction_id")
  private String originalTransactionId;

  @JsonProperty("product_id")
  private String productId;

  @JsonProperty("auto_renew_status")
  private Integer autoRenewStatus;

  @JsonProperty("auto_renew_product_id")
  private String autoRenewProductId;

}
