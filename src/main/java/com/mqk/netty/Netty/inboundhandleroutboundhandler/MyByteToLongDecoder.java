package com.mqk.netty.Netty.inboundhandleroutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {
	/**
	 *
	 * @param channelHandlerContext
	 * @param byteBuf 入站的 buf
	 * @param list 将解码后的数据传给下一个Handler处理
	 * @throws Exception
	 */
	@Override
	protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
		System.out.println("MyByteToLongDecoder 被调用");
		//由于long  8个字节
		if(byteBuf.readableBytes()>=8){
			list.add(byteBuf.readLong());
		}
	}
}
