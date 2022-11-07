package com.impassive.rpc.core.registry.factory;

import com.impassive.rpc.common.ImpUrl;
import com.impassive.rpc.core.api.Registry;
import com.impassive.rpc.core.registry.ZookeeperRegistry;

public class ZookeeperRegistryFactory extends AbstractRegistryFactory {

  @Override
  public Registry doBuildRegistry(ImpUrl url) {
    return new ZookeeperRegistry(url);
  }
}
