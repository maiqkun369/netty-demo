package com.mqk.netty.Netty.dubborpc.netty;


import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyServer {
	public static void startServer(String hostName,int port){
		startServer0(hostName,port);
	}
	/**
	 * Server初始化函数
	 * @param hostName
	 * @param port
	 */
	private static void startServer0(String hostName,int port){
		//创建boosGroup和workGroup两个线程组 子线程数默认为CPU核数*2
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);//只处理连接请求
		EventLoopGroup workGroup = new NioEventLoopGroup();//业务处理
		try {
			//服务器端启动对象
			io.netty.bootstrap.ServerBootstrap bootStrap = new io.netty.bootstrap.ServerBootstrap();
			//链式编程来进行设置
			bootStrap.group(bossGroup,workGroup)//设置两个线程组
					.channel(NioServerSocketChannel.class)//使用NioServerSocketChannel作为服务器的通道实现
					.option(ChannelOption.SO_BACKLOG,128)//设置线程队列等待连接的个数
					.childOption(ChannelOption.SO_KEEPALIVE,true)// 设置保持活动链接状态
					.childHandler(new ChannelInitializer<SocketChannel>() {//创建一个通道测试对象
						//给pipeline 设置处理器
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							//加入到线程池中而不是默认队列
							ChannelPipeline pipeline = socketChannel.pipeline();
							pipeline.addLast(new StringDecoder());
							pipeline.addLast(new StringEncoder());
							pipeline.addLast(new NettyServerHandler());
						}
					});//给workGroup的EventLoop对应的管道设置处理器
			System.out.println("Server is ready.............");
			//绑定一个端口并且同步，生成一个ChannelFuture 启动服务器
			ChannelFuture cf = bootStrap.bind(hostName,port).sync();
			//给cf注册监听器，监控关心的事件
			cf.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture channelFuture) throws Exception {
					if(cf.isSuccess()){
						System.out.println("监听IP: "+hostName+"端口: "+port+"成功");
					}else{
						System.out.println("失败");
					}
				}
			});
			//对关闭通道进行监听
			cf.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
}
