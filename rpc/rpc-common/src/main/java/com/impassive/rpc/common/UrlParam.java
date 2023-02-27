package com.impassive.rpc.common;

import java.util.Map;
import lombok.Getter;

/**
 * @author impassive
 */
@Getter
public class UrlParam {

  private final Map<String, Object> params;

  public UrlParam(Map<String, Object> params) {
    this.params = params;
  }
}
