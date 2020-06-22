package com.mqk.netty.Netty.protocaltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MyMessageEncoder extends MessageToByteEncoder<MessageProtocal> {
	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, MessageProtocal messageProtocal, ByteBuf byteBuf) throws Exception {
		System.out.println("MyMessageEncoder的encoded被调用");
		byteBuf.writeInt(messageProtocal.getLen());
		byteBuf.writeBytes(messageProtocal.getContent());

	}
}
