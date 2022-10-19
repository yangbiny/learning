package com.impassive.rpc.common;

import lombok.Getter;

@Getter
public class URL {

  /**
   * 这个 url 的 实力类型是什么
   */
  private final Class<?> classType;

  private final URLApplication application;

  private final URLRegisterAddress registerAddress;

  public URL(Class<?> classType, URLApplication application,
      URLRegisterAddress registerAddress) {
    this.classType = classType;
    this.application = application;
    this.registerAddress = registerAddress;
  }
}
