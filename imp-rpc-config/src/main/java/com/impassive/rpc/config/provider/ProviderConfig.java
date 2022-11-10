package com.impassive.rpc.config.provider;

import com.impassive.rpc.common.ImpUrl;
import com.impassive.rpc.config.BaseConfig;
import com.impassive.rpc.config.common.ProtocolConfig;
import com.impassive.rpc.core.api.Invoker;
import com.impassive.rpc.core.api.Protocol;
import com.impassive.rpc.core.invoker.ImpInvoker;
import com.impassive.rpc.exception.ExceptionCode;
import com.impassive.rpc.exception.ImpConfigException;
import com.impassive.rpc.extension.ExtensionLoader;
import java.util.HashMap;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProviderConfig<T> extends BaseConfig {

  private ImpUrl url = null;

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
      Class<?>[] interfaces = invokeObject.getClass().getInterfaces();
      if (interfaces.length == 0) {
        throw new ImpConfigException(ExceptionCode.CONFIG_EXCEPTION, "该对象未实现任何 接口");
      }
      //noinspection unchecked
      this.classType = (Class<T>) interfaces[0];
    }
    // 2. 构建URL
    ImpUrl url = new ImpUrl(
        protocolConfig.getName(),
        protocolConfig.getAddress(),
        protocolConfig.getPort(),
        this.classType,
        new HashMap<>()
    );
    Invoker<T> invoker = new ImpInvoker<>(url, classType);
    // 3. 暴露服务
    protocol.export(invoker);
  }

  public void destroy() {
    protocol.unExport();
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
