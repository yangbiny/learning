package com.impassive.rpc.remote.netty;

import com.impassive.rpc.common.ImpUrl;
import com.impassive.rpc.remote.api.RemoteService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyRemoteService implements RemoteService {

  @Override
  public void openService(ImpUrl<?> impUrl) {
    ServerBootstrap serverBootstrap = new ServerBootstrap();
    NioEventLoopGroup parentGroup = new NioEventLoopGroup();
    NioEventLoopGroup childGroup = new NioEventLoopGroup();
    ChannelFuture bind = serverBootstrap.group(parentGroup, childGroup)
        .channel(NioServerSocketChannel.class)
        .childHandler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel socketChannel) {

          }
        })
        .bind(impUrl.getProtocol().port());
    Channel channel = bind.channel();
    if (channel.isOpen()) {
      log.debug("service is open on port : {}", impUrl.getProtocol().port());
    }
  }
}
