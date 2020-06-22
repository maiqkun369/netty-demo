package com.mqk.netty.NIO;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel02 {
	@Test
	public void test(){
		FileInputStream inputStream=null;
		try {
			File file = new File("1.txt");
			inputStream=new FileInputStream(file);
			FileChannel inputchannel = inputStream.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate((int) file.length());
			try {
				inputchannel.read(buffer);
				System.out.println(new String(buffer.array()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
