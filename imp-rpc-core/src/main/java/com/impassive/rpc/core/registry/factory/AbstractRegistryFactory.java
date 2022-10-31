package com.impassive.rpc.core.registry.factory;

import com.impassive.rpc.common.ImpUrl;
import com.impassive.rpc.core.api.Registry;
import com.impassive.rpc.core.api.RegistryFactory;

public abstract class AbstractRegistryFactory implements RegistryFactory {

  @Override
  public Registry buildRegistry(ImpUrl<?> url) {
    return null;
  }
}
