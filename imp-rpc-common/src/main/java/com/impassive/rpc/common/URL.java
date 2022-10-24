package com.impassive.rpc.common;

import lombok.Getter;

@Getter
public class URL<T> {

  /**
   * 这个 url 的 实力类型是什么
   */
  private final Class<T> classType;

  private final T invokeObject;

  private final URLApplication application;

  private final URLRegisterAddress registerAddress;

  private final UrlProtocol protocol;

  public URL(
      Class<T> classType,
      T invokeObject,
      URLApplication application,
      URLRegisterAddress registerAddress,
      UrlProtocol protocol) {
    this.classType = classType;
    this.invokeObject = invokeObject;
    this.application = application;
    this.registerAddress = registerAddress;
    this.protocol = protocol;
  }
}
