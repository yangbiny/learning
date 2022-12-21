package com.impassive.pay.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OriginTransactionIdResponse {

  private String bundleId;

  private String appAppleId;

  private String environment;

  private Boolean hasMore;

  private List<String> signedTransactions;

  public boolean isSandBox() {
    return !StringUtils.equalsIgnoreCase(environment, "Production");
  }
}