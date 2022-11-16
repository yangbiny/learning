package com.impassive.rpc.core.protocol;

import com.impassive.rpc.common.ImpUrl;
import com.impassive.rpc.core.api.Invoker;
import com.impassive.rpc.core.api.Protocol;
import com.impassive.rpc.exception.ExceptionCode;
import com.impassive.rpc.exception.ServiceException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ImpProtocol implements Protocol {

  private static final Map<Class<?>, Invoker<?>> INVOKER_MAP = new ConcurrentHashMap<>();


  @Override
  public void export(Invoker<?> invoker) {
    Class<?> anInterface = invoker.getInterface();
    Invoker<?> exist = INVOKER_MAP.get(anInterface);
    if (exist != null) {
      throw new ServiceException(ExceptionCode.SERVICE_EXPORTER_EXCEPTION, "该服务已经注册");
    }



    INVOKER_MAP.put(anInterface, invoker);
  }

  @Override
  public <T> T refer(Class<?> classType, ImpUrl refer) {
    return null;
  }

  @Override
  public void unExport() {

  }
}
