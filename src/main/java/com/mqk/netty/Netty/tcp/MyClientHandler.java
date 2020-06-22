package com.mqk.netty.Netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

public class MyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
	private int count;
	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
		byte[] buffer = new byte[byteBuf.readableBytes()];
		byteBuf.readBytes(buffer);
		String msg = new String(buffer, Charset.forName("utf-8"));
		System.out.println("客户端接收到消息:"+msg);
		System.out.println("客户端接收到的消息量="+(++this.count));
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for (int i = 0; i <10 ; i++) {
			ByteBuf buf = Unpooled.copiedBuffer("Hello,Server" + i, Charset.forName("utf-8"));
			ctx.writeAndFlush(buf);
		}
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
