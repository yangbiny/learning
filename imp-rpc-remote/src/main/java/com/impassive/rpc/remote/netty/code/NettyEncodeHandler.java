package com.impassive.rpc.remote.netty.code;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author impassive
 */
public class NettyEncodeHandler extends MessageToByteEncoder<Object> {

  @Override
  protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf)
      throws Exception {

  }
}
