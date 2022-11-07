package com.impassive.rpc.remote.netty;

import com.impassive.rpc.common.ImpUrl;
import com.impassive.rpc.remote.api.RemoteService;
import com.impassive.rpc.remote.api.RemoteUrl;
import com.impassive.rpc.remote.netty.code.NettyDecodeHandler;
import com.impassive.rpc.remote.netty.code.NettyEncodeHandler;
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


  public NettyRemoteService(RemoteUrl remoteUrl) {
    ServerBootstrap serverBootstrap = new ServerBootstrap();
    NioEventLoopGroup parentGroup = new NioEventLoopGroup();
    NioEventLoopGroup childGroup = new NioEventLoopGroup();
    ChannelFuture bind = serverBootstrap.group(parentGroup, childGroup)
        .channel(NioServerSocketChannel.class)
        .childHandler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel socketChannel) {
            // 编码器
            socketChannel.pipeline().addLast("encode", new NettyEncodeHandler());
            // 解码器
            socketChannel.pipeline().addLast("decode", new NettyDecodeHandler());
          }
        })
        .bind(remoteUrl.port());
    Channel channel = bind.channel();
    if (channel.isOpen()) {
      log.debug("service is open on port : {}", remoteUrl);
    }
  }

  @Override
  public void openService(ImpUrl impUrl) {

  }
}
