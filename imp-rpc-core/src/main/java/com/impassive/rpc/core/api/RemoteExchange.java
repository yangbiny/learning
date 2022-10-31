package com.impassive.rpc.core.api;

import com.impassive.rpc.common.ImpUrl;

public interface RemoteExchange {

  /**
   * 打开网络服务器
   */
  void openServer(ImpUrl<?> impUrl);

}
