package com.impassive.pay.entity.subscribe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OriginDataResponse {

  private String subscriptionGroupIdentifier;

  private List<IapLastTransactionData> lastTransactions;

}