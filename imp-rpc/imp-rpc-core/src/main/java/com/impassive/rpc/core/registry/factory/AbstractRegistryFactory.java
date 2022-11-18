package com.impassive.rpc.core.registry.factory;

import com.impassive.rpc.common.ImpUrl;
import com.impassive.rpc.core.api.Registry;
import com.impassive.rpc.core.api.RegistryFactory;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractRegistryFactory implements RegistryFactory {

  private final static ConcurrentMap<String, Registry> REGISTRY_CONCURRENT_MAP = new ConcurrentHashMap<>();

  private final Lock lock = new ReentrantLock();

  protected abstract Registry doBuildRegistry(ImpUrl url);

  @Override
  public Registry buildRegistry(ImpUrl url) {
    String key = buildRegistryKey(url);
    Registry registry = REGISTRY_CONCURRENT_MAP.get(key);
    if (registry != null) {
      return registry;
    }
    try {
      lock.lock();
      registry = REGISTRY_CONCURRENT_MAP.get(key);
      if (registry != null) {
        return registry;
      }
      registry = doBuildRegistry(url);
      REGISTRY_CONCURRENT_MAP.put(key, registry);
    } finally {
      lock.unlock();
    }
    return registry;
  }

  private String buildRegistryKey(ImpUrl url) {
    return "zookeeper";
  }
}
