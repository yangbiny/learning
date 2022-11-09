package com.impassive.rpc.common;

import com.impassive.rpc.common.constant.ImpConstant;
import com.impassive.rpc.utils.StringTools;
import java.util.Map;
import lombok.Getter;

@Getter
public class ImpUrl {

  private String protocol;
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
}
