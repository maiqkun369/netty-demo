package com.mqk.netty.NIO;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class ScatteringAndGathering {
	@Test
	public void test() throws IOException {
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);


		//绑定端口到socket并启动
		serverSocketChannel.socket().bind(inetSocketAddress);

		//buffer数组
		ByteBuffer[] byteBuffers = new ByteBuffer[2];
		byteBuffers[0]=ByteBuffer.allocate(5);
		byteBuffers[1]=ByteBuffer.allocate(3);
		//等待客户端连接
		SocketChannel acceptChannel = serverSocketChannel.accept();
		int messageLongth=8;
		//读取
		while (true){
			int byteRead=0;
			while (byteRead<messageLongth){
				long read = acceptChannel.read(byteBuffers);//自动分散
				byteRead+=read;//累计读取的字节数
				System.out.println("byteRead="+byteRead);
				Arrays.asList(byteBuffers).stream().map(buffer->"position="+buffer.position()+
						",limit="+buffer.limit()).forEach(System.out::println);
			}
			//将所有的buffer进行翻转
			Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.flip());
			//显示到客户端
			long byteWrite=0;
			while (byteWrite<messageLongth){
				long write = acceptChannel.write(byteBuffers);//自动聚集
				byteWrite+=write;
			}
			Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.clear());
			System.out.println("byteRead="+byteRead+",byteWrite="+byteWrite+",messagelength="+messageLongth);
		}
	}
}
