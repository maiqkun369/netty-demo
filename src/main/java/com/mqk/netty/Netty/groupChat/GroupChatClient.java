package com.mqk.netty.Netty.groupChat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class GroupChatClient {
	private final String host;
	private final int port;

	public GroupChatClient(String host, int port) {
		this.host = host;
		this.port = port;
	}
	public void run(){
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group)
					 .channel(NioSocketChannel.class)
					 .handler(new ChannelInitializer<SocketChannel>() {
						 @Override
						 protected void initChannel(SocketChannel socketChannel) throws Exception {
							 ChannelPipeline pipeline = socketChannel.pipeline();
							 //加入相关的handler
							 //向pipeline中加入一个解码器
							 pipeline.addLast("decoder",new StringDecoder());
							 //向pipeline中加入一个编码器
							 pipeline.addLast("encoder",new StringEncoder());
							 //向pipeline中添加业务处理器
							 pipeline.addLast(new GroupCharClientHandler());
						 }
					 });
			try {
				ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
				Channel channel = channelFuture.channel();

				System.out.println("-------"+channel.localAddress()+"-------");

				Scanner scanner = new Scanner(System.in);
				while (scanner.hasNextLine()){
					String msg = scanner.nextLine();
					//通过channel发送到服务器端
					channel.writeAndFlush(msg+"\r\n");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} finally {
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) {
		new GroupChatClient("127.0.0.1",7000).run();
	}

}
