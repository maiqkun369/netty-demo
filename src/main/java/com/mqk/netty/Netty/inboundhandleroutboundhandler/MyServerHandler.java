package com.mqk.netty.Netty.inboundhandleroutboundhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyServerHandler extends SimpleChannelInboundHandler<Long> {
	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, Long aLong) throws Exception {
		System.out.println("从客户端"+channelHandlerContext.channel().remoteAddress()+"读取到--->"+aLong);
		//给客户端回送
		channelHandlerContext.writeAndFlush(9527l);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
