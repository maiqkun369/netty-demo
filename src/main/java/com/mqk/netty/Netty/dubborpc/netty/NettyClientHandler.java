package com.mqk.netty.Netty.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable<Object> {
	private ChannelHandlerContext context;//上下文
	private String result;//返回结果
	private String param;//客户端调用方法时，传入的参数

	/**
	 * 被代理对象调用，发送数据给服务器，->wait->等待被(channelRead)唤醒->返回结果
	 * @return
	 * @throws Exception
	 */
	@Override
	public synchronized Object call() throws Exception {
		System.out.println("call1被调用");
		this.context.writeAndFlush(this.param);
		// 进行wait
		wait();//等待channelRead获取到服务器的结果后唤醒
		System.out.println("call2被调用");
		return this.result;//服务方返回的结果
	}

	/**
	 * 与服务器建立连接后调用
	 * @param ctx
	 * @throws Exception
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelActive被调用");
		this.context=ctx;
	}

	/**
	 * 收到数据后调用
	 * @param ctx
	 * @param msg
	 * @throws Exception
	 */
	@Override
	//加synchronized以确保call方法不被其他线程唤醒，保证其有返回值
	public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("channelRead被调用");
		this.result=msg.toString();
		notify();//唤醒等待的线程
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}


	void setParam(String param) {
		System.out.println("setParam");
		this.param = param;
	}
}
