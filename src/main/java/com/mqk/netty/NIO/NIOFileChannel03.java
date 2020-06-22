package com.mqk.netty.NIO;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel03 {
	@Test
	public void test(){
		FileInputStream inputStream=null;
		FileOutputStream fileOutputStream=null;
		try {
			inputStream = new FileInputStream("1.txt");
			FileChannel channel01 = inputStream.getChannel();

			fileOutputStream = new FileOutputStream("2.txt");
			FileChannel channel02 = fileOutputStream.getChannel();

			ByteBuffer buf = ByteBuffer.allocate(1024);

			while (true){
				try {
					buf.clear();
					int read = channel01.read(buf);
					if(read==-1){
						break;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					buf.flip();
					channel02.write(buf);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally {
			try {
				fileOutputStream.close();
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


	}

}
