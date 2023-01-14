package com.impassive.pay.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeChatCallBackResult {

  private String id;

  @JsonProperty("create_time")
  private String createTime;

  @JsonProperty("event_type")
  private String eventType;

  @JsonProperty("resource_type")
  private String resourceType;

  /**
   * 加密的数据
   */
  private WeChatEncryptionData resource;
}