package com.impassive.rpc.core.registry.factory;

import com.impassive.rpc.common.ImpUrl;
import com.impassive.rpc.core.api.Registry;
import com.impassive.rpc.core.api.RegistryFactory;

public abstract class AbstractRegistryFactory implements RegistryFactory {

  protected abstract Registry doBuildRegistry(ImpUrl<?> url);

  @Override
  public Registry buildRegistry(ImpUrl<?> url) {
    return doBuildRegistry(url);
  }
}
