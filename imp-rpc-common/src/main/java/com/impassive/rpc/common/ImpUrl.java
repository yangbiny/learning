package com.impassive.rpc.common;

import com.impassive.rpc.common.constant.ImpConstant;
import com.impassive.rpc.exception.ExceptionCode;
import com.impassive.rpc.exception.ServiceException;
import com.impassive.rpc.utils.StringTools;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import lombok.Getter;

@Getter
public class ImpUrl {

  /**
   * 使用的协议信息
   */
  private String protocol;

  /**
   * 服务的 地址和 端口。端口表示的是当前应用的端口信息
   */
  private final UrlAddress urlAddress;

  private final UrlParam urlParam;

  public ImpUrl(
      String protocol,
      String address,
      Integer port,
      Class<?> classType,
      Map<String, Object> params
  ) {
    this.protocol = protocol;
    if (StringTools.isEmpty(this.protocol)) {
      this.protocol = ImpConstant.DEFAULT_PROTOCOL;
    }
    this.urlAddress = new PathUrlAddress(address, port, classType.getName());
    this.urlParam = new UrlParam(params);
  }

  public <T extends ConfigurableData> void addParams(T object) {
    ConfigKeyPath configKeyPath = object.keyPath();
    if (configKeyPath == null) {
      throw new ServiceException(ExceptionCode.COMMON_EXCEPTION, "config path can not be null");
    }
    BeanInfo beanInfo = getBeanInfo(object.getClass());
    try {
      for (MethodDescriptor methodDescriptor : beanInfo.getMethodDescriptors()) {
        Method method = methodDescriptor.getMethod();
        String name = method.getName();
        if (StringTools.notStartWith(name, "get")) {
          continue;
        }
        Object invoke = method.invoke(object);
        String format = String.format("%s_%s", configKeyPath.getPath(), name);
        urlParam.getParams().put(format, invoke);
      }
    } catch (Exception e) {
      throw new ServiceException(ExceptionCode.COMMON_EXCEPTION, e);
    }
  }

  private BeanInfo getBeanInfo(Class<?> classType) {
    try {
      return Introspector.getBeanInfo(classType);
    } catch (Exception e) {
      throw new ServiceException(ExceptionCode.SERVICE_EXPORTER_EXCEPTION, e);
    }
  }
}
