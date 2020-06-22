package com.mqk.netty.Netty.inboundhandleroutboundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		ChannelPipeline pipeline = socketChannel.pipeline();
		//加入出站的handler对数据进行编码
		pipeline.addLast(new MyLongToByteEncoder());
		//入站的解码器
		//pipeline.addLast(new MyByteToLongDecoder());
		pipeline.addLast(new MyByteToLongDecoder2());

		pipeline.addLast(new MyClientHandler());
	}
}
