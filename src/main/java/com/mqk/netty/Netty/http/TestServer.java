package com.mqk.netty.Netty.http;

import com.mqk.netty.Netty.simple.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TestServer {
	public static void main(String[] args) {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);//只处理连接请求
		EventLoopGroup workGroup = new NioEventLoopGroup();//业务处理
		try{
			//服务器端启动对象
			ServerBootstrap bootStrap = new ServerBootstrap();
			//链式编程来进行设置
			//childHandler 对应 workGroup。 handler对应bossGroup
			bootStrap.group(bossGroup, workGroup)//设置两个线程组
					.channel(NioServerSocketChannel.class)//使用NioServerSocketChannel作为服务器的通道实现
					.childHandler(new TestServerInitializer());//给workGroup的EventLoop对应的管道设置处理器
			System.out.println("Server is ready.............");
			//绑定一个端口并且同步，生成一个ChannelFuture 启动服务器
			ChannelFuture cf = bootStrap.bind(6668).sync();
			//给cf注册监听器，监控关心的事件
			cf.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture channelFuture) throws Exception {
					if (cf.isSuccess()) {
						System.out.println("监听端口 6668成功");
					} else {
						System.out.println("失败");
					}
				}
			});

			//对关闭通道进行监听
			try {
				cf.channel().closeFuture().sync();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally{
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
}
