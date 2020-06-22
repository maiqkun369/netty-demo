package com.mqk.netty.Netty.protocaltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class MyMessageDecoder extends ReplayingDecoder<Void> {
	@Override
	protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
		System.out.println("MyMessageDecoder的decode被调用");
		//需要将得到的二进制字节码转换成  MessageProtocal对象
		int length = byteBuf.readInt();
		byte[] content = new byte[length];
		byteBuf.readBytes(content);

		MessageProtocal messageProtocal = new MessageProtocal();
		messageProtocal.setLen(length);
		messageProtocal.setContent(content);

		list.add(messageProtocal);
	}
}
