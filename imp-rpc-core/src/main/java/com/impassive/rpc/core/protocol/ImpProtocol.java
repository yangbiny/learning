package com.impassive.rpc.core.protocol;

import com.impassive.rpc.common.URL;
import com.impassive.rpc.core.api.Protocol;
import com.impassive.rpc.core.api.Registry;
import com.impassive.rpc.core.api.RemoteExchange;
import com.impassive.rpc.exception.ExceptionCode;
import com.impassive.rpc.exception.ServiceExportException;
import com.impassive.rpc.extension.ExtensionLoader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImpProtocol implements Protocol {

  private static final Map<Class<?>, URL<?>> exportedUrlMap = new ConcurrentHashMap<>();

  @Override
  public void export(URL<?> url) {
    // 1. 检查 服务是否已经暴露
    checkIsExported(url);
    // 2. 打开 端口
    RemoteExchange remoteExchange = ExtensionLoader
        .buildExtensionLoader(RemoteExchange.class)
        .buildDefaultExtension();
    remoteExchange.openServer(url);
    // 3. 写入注册中心
    Registry registry = ExtensionLoader
        .buildExtensionLoader(Registry.class)
        .buildDefaultExtension();
    registry.register(url);

    exportedUrlMap.put(url.getClassType(), url);
  }

  @Override
  public <T> T refer(URL<T> refer) {
    return null;
  }

  private void checkIsExported(URL<?> url) {
    Class<?> classType = url.getClassType();
    URL<?> exportedUrl = exportedUrlMap.get(classType);
    if (exportedUrl != null) {
      throw new ServiceExportException(ExceptionCode.SERVICE_EXPORTER_EXCEPTION,
          String.format("不允许重复暴露服务 : %s", classType.getName()));
    }
  }
}
