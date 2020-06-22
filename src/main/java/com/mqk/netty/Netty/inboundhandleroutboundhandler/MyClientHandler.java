package com.mqk.netty.Netty.inboundhandleroutboundhandler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, Long aLong) throws Exception {
		System.out.println("服务器ip="+channelHandlerContext.channel().remoteAddress());
		System.out.println("收到来自服务器的消息"+aLong);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("MyClientHandler 发送数据");
		/*管道的MessageToByteEncoder会判断当前的msg
		 是不是应该处理的类型如果是就处理，不是就跳过Encode
		 因此编写Encode哟注意传入的数据类型和处理的数据类型要一致
		 */
		ctx.writeAndFlush(123456L);
	}
}
