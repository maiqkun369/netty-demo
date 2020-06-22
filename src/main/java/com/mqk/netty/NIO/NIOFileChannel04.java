package com.mqk.netty.NIO;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class NIOFileChannel04 {
	@Test
	public void test(){
		FileInputStream fileinputStream =null;
		FileOutputStream fileOutputStream=null;
		FileChannel srcChannel=null;
		FileChannel destChannel=null;

		try {
			fileinputStream= new FileInputStream("u=3095646859,358987937&fm=26&gp=0.jpg");
			fileOutputStream = new FileOutputStream("dog.jpg");

			srcChannel = fileinputStream.getChannel();
			destChannel = fileOutputStream.getChannel();

			destChannel.transferFrom(srcChannel,0,srcChannel.size());

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				srcChannel.close();
				destChannel.close();
				fileinputStream.close();
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
