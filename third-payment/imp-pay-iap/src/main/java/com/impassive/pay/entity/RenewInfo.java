package com.impassive.pay.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author impassive
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RenewInfo {

  private String autoRenewProductId;

  private Integer autoRenewStatus;

  private String environment;

  private Integer expirationIntent;

  private Long gracePeriodExpiresDate;

  private Boolean isInBillingRetryPeriod;

  private String offerIdentifier;

  private Integer offerType;

  private String originalTransactionId;

  private String priceIncreaseStatus;

  private String productId;

  private Long recentSubscriptionStartDate;

  private Long signedDate;
}
