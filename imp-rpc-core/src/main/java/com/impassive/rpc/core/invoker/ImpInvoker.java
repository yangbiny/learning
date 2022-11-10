package com.impassive.rpc.core.invoker;

import com.impassive.rpc.common.ImpUrl;
import com.impassive.rpc.core.api.Invoker;

/**
 * @author impassive
 */
public class ImpInvoker<T> implements Invoker<T> {

  private final ImpUrl url;

  private final Class<T> classType;

  public ImpInvoker(ImpUrl url, Class<T> classType) {
    this.url = url;
    this.classType = classType;
  }

  @Override
  public Class<T> getInterface() {
    return classType;
  }
}
