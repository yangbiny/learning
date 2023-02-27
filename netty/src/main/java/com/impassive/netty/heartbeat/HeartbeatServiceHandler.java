package com.impassive.netty.heartbeat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author impassive
 */
public class HeartbeatServiceHandler extends AbstractHeartbeatHandler {



  @Override
  protected void handlerReadIdle(ChannelHandlerContext ctx) {
    super.handlerReadIdle(ctx);
    System.err.println(
        "---client " + ctx.channel().remoteAddress().toString() + " reader timeout, close it---");
    ctx.close();
  }

  @Override
  protected void handlerData(ChannelHandlerContext ctx, ByteBuf msg) {
    byte[] data = new byte[msg.readableBytes() - 5];
    ByteBuf responseBuf = Unpooled.copiedBuffer(msg);
    msg.skipBytes(5);
    msg.readBytes(data);
    String content = new String(data);
    System.out.println("Service get content: " + content);
    ctx.write(responseBuf);
  }
}
