package com.impassive.netty.heartbeat;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author impassive
 */
public abstract class AbstractHeartbeatHandler extends SimpleChannelInboundHandler<ByteBuf> {

  public static final byte PING_MSG = 1;
  public static final byte PONG_MSG = 2;
  public static final byte CUSTOM_MSG = 3;


  @Override
  protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
    if (msg.getByte(4) == PING_MSG) {
      System.out.printf("get ping msg from %s%n", ctx.channel().remoteAddress());
      sendPongMsg(ctx);
    } else if (msg.getByte(4) == PONG_MSG) {
      System.out.printf("get pong msg from %s%n", ctx.channel().remoteAddress());
    } else {
      handlerData(ctx, msg);
    }
  }


  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

    if (evt instanceof IdleStateEvent event) {
      switch (event.state()) {
        case READER_IDLE -> handlerReadIdle(ctx);
        case WRITER_IDLE -> handlerWriteIdle(ctx);
        case ALL_IDLE -> handlerAllIdle(ctx);
      }
    }
  }

  protected void handlerAllIdle(ChannelHandlerContext ctx) {
    System.out.println("---- All Idle ----");
  }

  protected void handlerWriteIdle(ChannelHandlerContext ctx) {
    System.out.println("---- Writer Idle ----");
  }

  protected void handlerReadIdle(ChannelHandlerContext ctx) {
    System.out.println("---- Read Idle ----");

  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    System.out.println(ctx);
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    System.out.println(ctx);
  }

  protected abstract void handlerData(ChannelHandlerContext ctx, ByteBuf msg);

  private void sendPongMsg(ChannelHandlerContext ctx) {
    ByteBuf buffer = ctx.alloc().buffer(5);
    buffer.writeInt(5);
    buffer.writeByte(PONG_MSG);
    ctx.writeAndFlush(buffer);
  }

  protected void sendPingMsg(ChannelHandlerContext ctx) {
    ByteBuf buffer = ctx.alloc().buffer(5);
    buffer.writeInt(5);
    buffer.writeByte(PING_MSG);
    ctx.writeAndFlush(buffer);
  }


}
