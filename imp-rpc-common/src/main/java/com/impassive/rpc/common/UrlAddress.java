package com.impassive.rpc.common;

import lombok.Data;

@Data
public class UrlAddress {

  private String address;

  private Integer port;

  public UrlAddress(String address, Integer port) {
    this.address = address;
    this.port = port;
  }
}
