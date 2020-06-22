package com.mqk.netty.Netty.protocaltcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		ChannelPipeline pipeline = socketChannel.pipeline();
		pipeline.addLast(new MyMessageEncoder());
		pipeline.addLast(new MyMessageDecoder());
		pipeline.addLast(new MyClientHandler());

	}
}