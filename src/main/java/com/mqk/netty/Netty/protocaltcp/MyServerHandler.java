package com.mqk.netty.Netty.protocaltcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocal> {
	private int count;
	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocal messageProtocal) throws Exception {
		int len = messageProtocal.getLen();
		byte[] content = messageProtocal.getContent();
		System.out.println("服务端接受到信息如下");
		System.out.println("长度="+len);
		System.out.println("内容"+new String(content,Charset.forName("utf-8")));
		System.out.println("服务器接收到消息包数量="+(++this.count));
		//回复消息
		String reply = UUID.randomUUID().toString();
		int length = reply.getBytes("utf-8").length;
		byte[] contentReply = reply.getBytes("utf-8");
		//构建一个协议包
		MessageProtocal messageProtocal1 = new MessageProtocal();
		messageProtocal1.setLen(length);
		messageProtocal1.setContent(contentReply);
		channelHandlerContext.writeAndFlush(messageProtocal1);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
