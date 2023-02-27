package com.impassive.rpc.core.api;

import com.impassive.rpc.common.ImpUrl;

public interface RegistryFactory {

  Registry buildRegistry(ImpUrl url);

}
