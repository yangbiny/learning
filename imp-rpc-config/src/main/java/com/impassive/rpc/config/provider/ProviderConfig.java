package com.impassive.rpc.config.provider;

import com.impassive.rpc.config.BaseConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProviderConfig<T> extends BaseConfig {

  private T invokeObject;

  private Class<T> classType;

  @Override
  public boolean legal() {
    return this.applicationConfig != null && this.applicationConfig.legal()
        && this.registerConfig != null && this.registerConfig.legal();
  }
}
