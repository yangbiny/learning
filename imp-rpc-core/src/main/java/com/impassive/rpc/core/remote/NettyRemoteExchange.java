package com.impassive.rpc.core.remote;

import com.impassive.rpc.common.URL;
import com.impassive.rpc.core.api.RemoteExchange;
import com.impassive.rpc.extension.ExtensionLoader;
import com.impassive.rpc.remote.api.RemoteService;

public class NettyRemoteExchange implements RemoteExchange {

  private final RemoteService remoteService = ExtensionLoader.buildExtensionLoader(
      RemoteService.class).buildDefaultExtension();

  @Override
  public void openServer(URL<?> url) {
    remoteService.openService(url);
  }
}
