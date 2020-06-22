package com.mqk.netty.Netty.heartBeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import java.util.concurrent.TimeUnit;

public class MyServer {
	public static void main(String[] args) {
		//创建两个线程组
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup,workerGroup)
					.channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG,128)
					.childOption(ChannelOption.SO_KEEPALIVE,true)
					.handler(new LoggingHandler(LogLevel.INFO))//bossGroup中添加一个日志处理器
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							ChannelPipeline pipeline = socketChannel.pipeline();
							//netty提供的处理器，IdleStateHandler-->处理空闲状态的处理器
							//当IdleStateHandler触发后，就会传递给管道的下一个handler的userEventTrigger去处理读空闲，写空闲，读写空闲
							pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
							pipeline.addLast(new MyServerHandle());
						}
					});
			ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
