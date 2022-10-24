package com.impassive.rpc.config.provider;

import com.impassive.rpc.common.URL;
import com.impassive.rpc.common.URLApplication;
import com.impassive.rpc.common.URLRegisterAddress;
import com.impassive.rpc.common.UrlProtocol;
import com.impassive.rpc.config.BaseConfig;
import com.impassive.rpc.config.common.ProtocolConfig;
import com.impassive.rpc.core.api.Protocol;
import com.impassive.rpc.exception.ExceptionCode;
import com.impassive.rpc.exception.ImpConfigException;
import com.impassive.rpc.extension.ExtensionLoader;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProviderConfig<T> extends BaseConfig {

  private final Protocol protocol = ExtensionLoader.buildExtensionLoader(Protocol.class)
      .buildDefaultExtension();

  private T invokeObject;

  private Class<T> classType;

  private ProtocolConfig protocolConfig;

  /**
   * 暴露服务的入口
   */
  public void export() {
    // 1. 检查参数
    checkIllegal();
    if (this.classType == null) {
      //noinspection unchecked
      this.classType = (Class<T>) invokeObject.getClass();
    }
    // 2. 构建URL
    URL<T> url = new URL<>(
        classType,
        invokeObject,
        new URLApplication(this.applicationConfig.getApplicationName()),
        new URLRegisterAddress(
            this.registerConfig.getAddress(),
            this.registerConfig.getPort(),
            this.registerConfig.getPath()
        ),
        new UrlProtocol(
            this.protocolConfig.getAddress(),
            this.protocolConfig.getPort()

        ));
    // 3. 暴露服务
    protocol.export(url);
  }

  @Override
  public void checkIllegal() {
    super.checkIllegal();
    if (this.protocolConfig == null) {
      throw new ImpConfigException(ExceptionCode.CONFIG_EXCEPTION, "Protocol Config 不能为空");
    }
    this.protocolConfig.checkIllegal();
    if (invokeObject == null) {
      throw new ImpConfigException(ExceptionCode.CONFIG_EXCEPTION,
          "Provider invoker 不能为空");
    }
  }
}
