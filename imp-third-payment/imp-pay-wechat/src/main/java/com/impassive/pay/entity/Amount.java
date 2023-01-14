package com.impassive.pay.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Amount {

  /**
   * 订单总金额，单位分
   */
  private Integer total;

  /**
   * 用户支付金额，单位分
   */
  @JsonProperty("payer_total")
  private Integer payerTotal;

}