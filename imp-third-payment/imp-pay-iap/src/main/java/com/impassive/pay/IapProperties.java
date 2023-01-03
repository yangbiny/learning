package com.impassive.pay;

import lombok.Data;
import lombok.Getter;

@Data
public class IapProperties {

  private String g3Root;

  private String password;

  private String issId;

  private String keyId;

  private String privateKey;
}
