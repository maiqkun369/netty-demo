package com.mqk.netty.Netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 *自定义的Handler需要继承Netty规定好的某个HandlerAdapter
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

	static final EventExecutorGroup group=new DefaultEventExecutorGroup(16);

	/**
	 * 可以读取客户端发送的消息
	 * @param ctx 上下文对象，含有pipeline,channel,地址
	 * @param msg 客户端发送的数据
	 * @throws Exception
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("EchoServerHandler的线程是---->"+Thread.currentThread().getName());
		//自定义普通任务
		//会添加到当前NIOEventLoop 的taskQueue中
		/*ctx.channel().eventLoop().execute(new Runnable() {
			@Override
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(10);
					ctx.writeAndFlush(Unpooled.copiedBuffer("hello, client:your just a little brother",CharsetUtil.UTF_8));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
*/
		//用户自定义定时任务-》该任务是提交到scheduleTaskQueue中
		/*ctx.channel().eventLoop().schedule(new Runnable() {
			@Override
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(10);
					ctx.writeAndFlush(Unpooled.copiedBuffer("hello, client:your just a little brother",CharsetUtil.UTF_8));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		},5,TimeUnit.SECONDS);*/
		System.out.println("Go on ...................................");
		//将任务提交到线程池
		group.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				//接收客户端消息
				ByteBuf buf = (ByteBuf) msg;
				byte[] bytes = new byte[buf.readableBytes()];
				buf.readBytes(bytes);
				try {
					String s = new String(bytes, "UTF-8");
					TimeUnit.SECONDS.sleep( 2);
					System.out.println("group.submit的call线程是=" + Thread.currentThread().getName());
					ctx.writeAndFlush(Unpooled.copiedBuffer("hello, client:your just a little brother", CharsetUtil.UTF_8));
					return null;
				} catch (UnsupportedEncodingException | InterruptedException e) {
					e.printStackTrace();
				}
				return null;
			}
		});
		/*
		System.out.println("服务器读取线程 "+Thread.currentThread().getName());
		System.out.println("Server ctx="+ctx);
		System.out.println("看看channel和pipeline的关系");
		Channel channel = ctx.channel();
		ChannelPipeline pipeline = ctx.pipeline();//本质是一个双向链表
		//msg转成Netty提供的ByteBuf
		ByteBuf buf=(ByteBuf) msg;
		System.out.println("客户端发送的消息是:"+buf.toString(CharsetUtil.UTF_8));
		System.out.println("客户端地址："+ctx.channel().remoteAddress());
		 */
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
