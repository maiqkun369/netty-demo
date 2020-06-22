package com.mqk.netty.Netty.protocaltcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocal> {
	private int count;
	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocal messageProtocal) throws Exception {
		int len = messageProtocal.getLen();
		byte[] content = messageProtocal.getContent();
		System.out.println("客户端接受到消息如下:");
		System.out.println("len="+len);
		System.out.println("content="+new String(content,Charset.forName("utf-8")));
		System.out.println("客户端接受消息数量="+(++this.count));
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for (int i = 0; i <10 ; i++) {
			String msg="当头炮，屏风马";
			byte[] content = msg.getBytes(Charset.forName("utf-8"));
			int length = msg.getBytes(Charset.forName("utf-8")).length;

			//创建协议包
			MessageProtocal messageProtocal = new MessageProtocal();
			messageProtocal.setLen(length);
			messageProtocal.setContent(content);
			ctx.writeAndFlush(messageProtocal);
		}
	}
}
