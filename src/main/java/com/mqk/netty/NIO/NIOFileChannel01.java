package com.mqk.netty.NIO;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel01 {
	@Test
	public void test(){
		String str="Hello,World";
		FileOutputStream fileOutputStream=null;
		try {
			 fileOutputStream = new FileOutputStream("file01.txt");

			FileChannel fileChannel = fileOutputStream.getChannel();

			ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

			byteBuffer.put(str.getBytes());
			//切换模式
			byteBuffer.flip();
			try {
				//write--->将缓冲区的数据写进通道中
				//read--->把通道中的数据读到缓冲区
				fileChannel.write(byteBuffer);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally {
			try {
				fileOutputStream.close();
				System.out.println("写入完毕!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
