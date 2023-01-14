package com.impassive.pay.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeChatEncryptionData {

  @JsonProperty("original_type")
  private String originalType;

  private String algorithm;

  private String ciphertext;

  private String nonce;

  @JsonProperty("associated_data")
  private String associatedData;
}