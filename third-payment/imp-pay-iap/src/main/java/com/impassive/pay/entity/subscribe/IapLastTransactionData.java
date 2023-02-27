package com.impassive.pay.entity.subscribe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.impassive.pay.entity.RenewInfo;
import com.impassive.pay.entity.TransactionInfo;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IapLastTransactionData {

  private String originalTransactionId;

  private Integer status;

  private String signedTransactionInfo;

  private TransactionInfo transactionInfo;

  private String signedRenewalInfo;

  private RenewInfo renewalInfo;


}