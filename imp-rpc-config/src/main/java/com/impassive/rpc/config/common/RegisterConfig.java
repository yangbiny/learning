package com.impassive.rpc.config.common;

import com.impassive.rpc.common.ConfigurableData;
import com.impassive.rpc.exception.ExceptionCode;
import com.impassive.rpc.exception.ImpConfigException;
import com.impassive.rpc.utils.StringTools;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class RegisterConfig implements ConfigurableData {

  private String address;

  private Integer port;

  private String path = "/imp/rpc";

  @Override
  public void checkIllegal() {
    if (StringTools.isEmpty(address)) {
      throw new ImpConfigException(ExceptionCode.CONFIG_EXCEPTION,
          "Register address can not be empty");
    }
    if (port == null || port <= 0) {
      throw new ImpConfigException(ExceptionCode.CONFIG_EXCEPTION,
          "Register port can not be null");
    }
    if (StringTools.isEmpty(path)) {
      throw new ImpConfigException(ExceptionCode.CONFIG_EXCEPTION,
          "Register path can not be empty");
    }
    if (StringTools.notStartWith(path, "/") || StringTools.endWith(path, "/")) {
      throw new ImpConfigException(ExceptionCode.CONFIG_EXCEPTION,
          "Register path must start with '/' and must not end with '/'");
    }
  }
}
