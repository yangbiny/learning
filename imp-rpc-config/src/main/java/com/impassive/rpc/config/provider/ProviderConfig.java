package com.impassive.rpc.config.provider;

import com.impassive.rpc.common.Url;
import com.impassive.rpc.config.BaseConfig;
import com.impassive.rpc.core.api.Protocol;
import com.impassive.rpc.exception.ExceptionCode;
import com.impassive.rpc.exception.ImpConfigException;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProviderConfig<T> extends BaseConfig {

  private final Protocol protocol = null;

  private T invokeObject;

  private Class<T> classType;

  /**
   * 暴露服务的入口
   */
  public void export() {
    checkIllegal();
    if (this.classType == null) {
      //noinspection unchecked
      this.classType = (Class<T>) invokeObject.getClass();
    }
    Url url = new Url();
    protocol.export(url);
  }

  @Override
  public void checkIllegal() {
    super.checkIllegal();
    if (invokeObject == null) {
      throw new ImpConfigException(ExceptionCode.CONFIG_EXCEPTION,
          "Provider invoker can not be null");
    }
  }
}
