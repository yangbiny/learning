package com.impassive.pay.entity.subscribe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IapSubscribeQueryResponse {

  private String bundleId;

  private String appAppleId;

  private String environment;

  private List<OriginDataResponse> data;

  public boolean isSandBox() {
    return !StringUtils.equalsIgnoreCase(environment, "Production");
  }
}