package com.impassive.pay.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IapQueryTransactionResult {

  private String bundleId;

  private String environment;

  private Long expiresDate;

  private String inAppOwnershipType;

  private Boolean isUpgraded;

  private String offerIdentifier;

  private Integer offerType;

  private Long originalPurchaseDate;

  private String originalTransactionId;

  private String productId;

  private Long purchaseDate;

  private Integer quantity;

  private Long revocationDate;

  private Integer revocationReason;

  private Long signedDate;

  private String subscriptionGroupIdentifier;

  private String transactionId;

  private String type;

  private String webOrderLineItemId;
}