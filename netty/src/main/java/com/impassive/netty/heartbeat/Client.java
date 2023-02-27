package com.impassive.netty.heartbeat;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author impassive
 */
public class Client {

  public static void main(String[] args) {

    try {

      NioEventLoopGroup group = new NioEventLoopGroup();
      Bootstrap bootstrap = new Bootstrap();

      bootstrap.group(group)
          .channel(NioSocketChannel.class)
          .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
              ChannelPipeline pipeline = ch.pipeline();
              pipeline.addLast(new IdleStateHandler(10, 0, 0));
              pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, -4, 0));
              pipeline.addLast(new HeartbeatClientHandler());
            }
          });

      Channel channel = bootstrap.remoteAddress("127.0.0.1", 12345)
          .connect()
          .sync()
          .channel();

      for (int i = 0; i < 10; i++) {
        String content = "client msg : %s".formatted(i);
        ByteBuf buffer = channel.alloc().buffer();
        buffer.writeInt(5 + content.getBytes().length);
        buffer.writeByte(3);
        buffer.writeBytes(content.getBytes());

        channel.writeAndFlush(buffer);

        Thread.sleep(20000);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
