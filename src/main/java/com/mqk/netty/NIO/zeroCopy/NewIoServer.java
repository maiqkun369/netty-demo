package com.mqk.netty.NIO.zeroCopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NewIoServer {
	public static void main(String[] args) throws IOException {
		InetSocketAddress inetSocketAddress = new InetSocketAddress(7001);
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		ServerSocket serverSocket = serverSocketChannel.socket();
		serverSocket.bind(inetSocketAddress);

		ByteBuffer buffer = ByteBuffer.allocate(1024);
		while (true){
			SocketChannel socketChannel = serverSocketChannel.accept();
			int count=0;
			while (count!=-1){
				try {
					count=socketChannel.read(buffer);
				} catch (IOException e) {
					//e.printStackTrace();
					break;
				}
				buffer.rewind();//position=0 mark作废 重复读


			}
		}
	}
}
