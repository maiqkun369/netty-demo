package com.mqk.netty.Netty.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 *自定义的Handler需要继承Netty规定好的某个HandlerAdapter
 */
//public class NettyServerHandler extends ChannelInboundHandlerAdapter {
public class NettyServerHandler extends SimpleChannelInboundHandler<StudentPOJO.Student>{
	/**
	 * 可以读取客户端发送的消息
	 * @param ctx 上下文对象，含有pipeline,channel,地址
	 * @param student 客户端发送的数据
	 * @throws Exception
	 */
	@Override
	public void channelRead0(ChannelHandlerContext ctx, StudentPOJO.Student student) throws Exception {
		//读取从客户端发送的StudentPOJO.Student
		System.out.println("客户端发送的数据 id="+student.getId()+" name="+student.getName());
	}

	/**
	 * 读取数据完毕
	 * @param ctx
	 * @throws Exception
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.copiedBuffer("hello, client:your just a little brother",CharsetUtil.UTF_8));//数据写入到缓冲并刷新
	}
	/**
	 * 处理异常，一般是关闭通道
	 * @param ctx
	 * @param cause
	 * @throws Exception
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
}
