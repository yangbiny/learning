package com.impassive.rpc.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author impassive
 */
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class PathUrlAddress extends UrlAddress {

  private final String path;

  public PathUrlAddress(String address, Integer port, String path) {
    super(address, port);
    this.path = path;
  }
}
