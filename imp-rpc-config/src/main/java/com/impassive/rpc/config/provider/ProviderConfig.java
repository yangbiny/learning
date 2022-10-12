package com.impassive.rpc.config.provider;

import com.impassive.rpc.config.BaseConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProviderConfig<T> extends BaseConfig {

  private T invokeObject;

  private Class<T> classType;

  /**
   * 暴露服务的入口
   */
  public void export() {
    checkIllegal();
  }

  @Override
  public void checkIllegal() {
    super.checkIllegal();
  }
}
