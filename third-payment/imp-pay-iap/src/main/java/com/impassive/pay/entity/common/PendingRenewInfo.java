package com.impassive.pay.entity.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * @author impassive
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PendingRenewInfo {

  private String autoRenewProductId;

  private Integer autoRenewStatus;

  private String expirationIntent;

  private String gracePeriodExpiresDate;

  private String gracePeriodExpiresDateMs;

  private String gracePeriodExpiresDatePst;

  private String isInBillingRetryPeriod;

  private String offerCodeRefName;

  private String originalTransactionId;

  private String priceConsentStatus;

  private String productId;

  private String promotionalOfferId;

  private String priceIncreaseStatus;


  public boolean isEnable() {
    return autoRenewStatus == 1;
  }

}
