package com.mqk.netty.Netty.dubborpc.netty;

import com.mqk.netty.Netty.dubborpc.provider.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		//获取客户端发送的消息并调用服务
		System.out.println("msg="+msg);
		//客户端在调用服务器API时需要定制一个协议，例如发的消息必须以HelloService#hello#开头
		if(msg.toString().startsWith("HelloService#hello#")){
			String res = new HelloServiceImpl().hello(msg.toString().substring(msg.toString().lastIndexOf("#") + 1));
			ctx.writeAndFlush(res);
		}

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
}
