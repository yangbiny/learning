package com.impassive.rpc.remote.netty;

import com.impassive.rpc.common.URL;
import com.impassive.rpc.remote.api.RemoteService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;

public class NettyRemoteService implements RemoteService {

  @Override
  public void openService(URL<?> url) {
    ServerBootstrap serverBootstrap = new ServerBootstrap();
    NioEventLoopGroup parentGroup = new NioEventLoopGroup();
    NioEventLoopGroup childGroup = new NioEventLoopGroup();
    serverBootstrap.group(parentGroup, childGroup)
        .bind(url.getProtocol().port());
  }
}
