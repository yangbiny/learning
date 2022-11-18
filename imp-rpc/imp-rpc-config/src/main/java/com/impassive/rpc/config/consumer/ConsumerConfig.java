package com.impassive.rpc.config.consumer;

import com.impassive.rpc.common.ConfigKeyPath;
import com.impassive.rpc.config.BaseConfig;
import com.impassive.rpc.exception.ExceptionCode;
import com.impassive.rpc.exception.ImpConfigException;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ConsumerConfig<T> extends BaseConfig {

  private Class<T> classType;

  @Override
  public ConfigKeyPath keyPath() {
    return ConfigKeyPath.consumer;
  }

  @Override
  public void checkIllegal() {
    super.checkIllegal();

    if (this.classType == null) {
      throw new ImpConfigException(ExceptionCode.CONFIG_EXCEPTION,
          "Consumer class type can not be null");
    }
  }
}
