package com.mqk.netty.Netty.inboundhandleroutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;
//不需要判断数据是否足够读取，内部自动进行处理
public class MyByteToLongDecoder2 extends ReplayingDecoder<Void> {
	@Override
	protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
		System.out.println("MyByteToLongDecoder2被调用");
		list.add(byteBuf.readLong());
	}
}
