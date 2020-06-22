package com.mqk.netty.Netty.webSocket;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

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
							//因为基于Http协议，使用http的编码和解码器
							pipeline.addLast(new HttpServerCodec());
							//是以块方式写的，添加ChunkedWriteHandler处理器
							pipeline.addLast(new ChunkedWriteHandler());
							/*
								1、http协议在传输过程中是分段的
								2、HttpObjectAggregator将多个段聚合起来
								3、这就是为什么当浏览器发送大量数据时，就会发出多次Http请求
							 */
							pipeline.addLast(new HttpObjectAggregator(8192));
							/*
								1、对于WebSocket，它的数据是以帧的形式传递的
								2、浏览器请求时 ws://localhost:7000/hello 要与WebSocketServerProtocolHandler中的Path对应
								3、WebSocketServerProtocolHandler核心功能是将http协议升级为ws协议，即保持长链接
							    4、通过一个状态码 101 将http协议升级为ws协议
							 */
							pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
							//自定义的handler,处理业务逻辑
							pipeline.addLast(new MyTextWebSocketFrameHandler());
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
