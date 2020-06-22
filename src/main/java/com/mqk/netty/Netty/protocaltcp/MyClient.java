package com.mqk.netty.Netty.protocaltcp;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class MyClient {
	public static void main(String[] args) throws Exception {
		//客户端需要一个事件循环组
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			//创建一个启动对象 客户端使用的不是ServerBootstrap 而是Bootstrap
			Bootstrap bootstrap = new Bootstrap();
			//设置相关参数
			bootstrap.group(group)//设置线程组
					.channel(NioSocketChannel.class) //设置客户端通道的实现类
					.handler(new MyClientInitializer());
			System.out.println("Client is ok.............");
			//启动客户端去连接服务器端 ChannelFuture ---->Netty的异步模型
			ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 7000).sync();
			//给关闭通道进行监听
			channelFuture.channel().closeFuture().sync();

		} finally {
			group.shutdownGracefully();
		}


	}
}
