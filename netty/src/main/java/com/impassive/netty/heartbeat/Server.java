package com.impassive.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author impassive
 */
public class Server {

  public static void main(String[] args) {
    NioEventLoopGroup group = new NioEventLoopGroup(1);
    NioEventLoopGroup worker = new NioEventLoopGroup(4);

    try {

      ServerBootstrap bootstrap = new ServerBootstrap();
      bootstrap.group(group, worker)
          .channel(NioServerSocketChannel.class)
          .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
              ChannelPipeline pipeline = ch.pipeline();
              pipeline.addLast(new IdleStateHandler(0, 10, 0));
              pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, -4, 0));
              pipeline.addLast(new HeartbeatServiceHandler());
            }
          });

      Channel channel = bootstrap.bind(12345).sync().channel();
      channel.closeFuture().sync();


    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
