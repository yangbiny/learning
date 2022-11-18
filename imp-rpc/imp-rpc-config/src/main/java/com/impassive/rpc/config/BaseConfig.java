package com.impassive.rpc.config;

import com.impassive.rpc.common.ConfigurableData;
import com.impassive.rpc.config.common.ApplicationConfig;
import com.impassive.rpc.config.common.RegisterConfig;
import com.impassive.rpc.exception.ExceptionCode;
import com.impassive.rpc.exception.ImpConfigException;
import lombok.Data;

@Data
public abstract class BaseConfig implements ConfigurableData {

  protected ApplicationConfig applicationConfig;

  protected RegisterConfig registerConfig;

  @Override
  public void checkIllegal() {
    if (this.applicationConfig == null) {
      throw new ImpConfigException(ExceptionCode.CONFIG_EXCEPTION, "applicationConfig 不能为空");
    }
    this.applicationConfig.checkIllegal();

    if (this.registerConfig == null) {
      throw new ImpConfigException(ExceptionCode.CONFIG_EXCEPTION, "registerConfig 不能为空");
    }
    this.registerConfig.checkIllegal();
  }
}
