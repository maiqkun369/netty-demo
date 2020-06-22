package com.mqk.netty.Netty.groupChat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class GroupChatServer {
	private int port;  //监听端口
	public GroupChatServer(int port) {
		this.port = port;
	}
	//处理客户端请求
	public void run(){
		//创建两个线程组
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup,workerGroup)
					       .channel(NioServerSocketChannel.class)
					       .option(ChannelOption.SO_BACKLOG,128)
					       .childOption(ChannelOption.SO_KEEPALIVE,true)
					       .childHandler(new ChannelInitializer<SocketChannel>() {
						       @Override
						       protected void initChannel(SocketChannel socketChannel) throws Exception {
							       ChannelPipeline pipeline = socketChannel.pipeline();
							       //向pipeline中加入一个解码器
							       pipeline.addLast("decoder",new StringDecoder());
							       //向pipeline中加入一个编码器
							       pipeline.addLast("encoder",new StringEncoder());
							       //向pipeline中添加业务处理器
							       pipeline.addLast(new GroupCharServerHandler());
						       }
					       });
			System.out.println("netty 服务器启动。。。。。。。。");
			try {
				ChannelFuture cf = serverBootstrap.bind(port).sync();
				//监听关闭
				cf.channel().closeFuture().sync();

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}

	}

	public static void main(String[] args) {
		new GroupChatServer(7000).run();
	}
}
