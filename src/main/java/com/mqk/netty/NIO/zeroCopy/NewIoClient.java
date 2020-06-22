package com.mqk.netty.NIO.zeroCopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIoClient {
	public static void main(String[] args) throws IOException {
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.connect(new InetSocketAddress("localhost", 7001));
		String file="dog.jpg";
		FileChannel fileChannel = new FileInputStream(file).getChannel();
		//准备发送
		long startTime = System.currentTimeMillis();
		//windows系统中一次调用transferTo(底层使用了零拷贝)只能发送8M，因此需要分段传输
		/*long count=0;
		for (int i = 0; i <=(fileChannel.size()/(8*1024))+1 ; i++) {
			count = fileChannel.transferTo(i * 1024, (i + 1) * 1024, socketChannel);
			count+=count;
			System.out.println(count);
		}*/
		long count = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
		System.out.println("发送的总的字节数 ="+count+" 耗时："+(System.currentTimeMillis()-startTime));
		fileChannel.close();
	}
}

