package com.impassive.rpc.config.common;

import com.impassive.rpc.common.ConfigurableData;
import com.impassive.rpc.exception.ExceptionCode;
import com.impassive.rpc.exception.ImpConfigException;
import com.impassive.rpc.utils.StringTools;
import lombok.Data;

@Data
public class ApplicationConfig implements ConfigurableData {

  private String applicationName;

  @Override
  public void checkIllegal() {
    if (StringTools.isEmpty(applicationName)) {
      throw new ImpConfigException(ExceptionCode.CONFIG_EXCEPTION,
          "Application name can not be empty");
    }
  }
}
