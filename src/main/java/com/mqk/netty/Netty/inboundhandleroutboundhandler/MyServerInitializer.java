package com.mqk.netty.Netty.inboundhandleroutboundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;


public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
	@Override
	protected void initChannel(SocketChannel socketChannel) throws Exception {
		ChannelPipeline pipeline = socketChannel.pipeline();
		//入站的handler进行解码 MyByteToLongDecoder
		//pipeline.addLast(new MyByteToLongDecoder());
		pipeline.addLast(new MyByteToLongDecoder2());
		//出站的handler进行编码
		pipeline.addLast(new MyLongToByteEncoder());

		pipeline.addLast(new MyServerHandler());

	}
}
