package com.impassive.rpc.core.api;

import com.impassive.rpc.common.URL;

public interface RemoteExchange {

  /**
   * 打开网络服务器
   */
  void openServer(URL<?> url);

}
