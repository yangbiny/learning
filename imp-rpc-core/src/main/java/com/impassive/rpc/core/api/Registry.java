package com.impassive.rpc.core.api;

import com.impassive.rpc.common.URL;

public interface Registry {

  void register(URL<?> url);

}
