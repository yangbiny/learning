package com.impassive.pay.entity.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * @author impassivey
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReceiptItem {

  private String appAccountToken;

  private String cancellationDate;

  private Long cancellationDateMs;

  private String cancellationDatePst;

  private String cancellationReason;

  private String expiresDate;

  private Long expiresDateMs;

  private String expiresDatePst;

  private Integer inAppOwnershipType;

  private Boolean isInIntroOfferPeriod;

  private Boolean isTrialPeriod;

  private Boolean isUpgraded;

  private String offerCodeRefName;

  private String originalPurchaseDate;

  private Long originalPurchaseDateMs;

  private Long originalPurchaseDatePst;

  private String originalTransactionId;

  private String promotionalOfferId;

  private String productId;

  private String purchaseDate;

  private Long purchaseDateMs;

  private String purchaseDatePst;

  private Integer quantity;

  private String subscriptionGroupIdentifier;

  private String transactionId;

  private String webOrderLineItemId;

}
