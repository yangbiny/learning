package com.impassive.netty.heartbeat;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author impassive
 */
public class HeartbeatClientHandler extends AbstractHeartbeatHandler {

  @Override
  protected void handlerReadIdle(ChannelHandlerContext ctx) {
    super.handlerReadIdle(ctx);
    sendPingMsg(ctx);
  }

  @Override
  protected void handlerAllIdle(ChannelHandlerContext ctx) {
    super.handlerAllIdle(ctx);
    sendPingMsg(ctx);
  }

  @Override
  protected void handlerData(ChannelHandlerContext ctx, ByteBuf msg) {
    byte[] data = new byte[msg.readableBytes() - 5];
    msg.skipBytes(5);
    msg.readBytes(data);
    String content = new String(data);
    System.out.println("Client get content: " + content);
  }
}