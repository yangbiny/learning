package com.impassive.rpc.core.api;

import com.impassive.rpc.common.ImpUrl;

public interface Registry {

  void register(ImpUrl<?> impUrl);

  void unRegister(ImpUrl<?> impUrl);

}
