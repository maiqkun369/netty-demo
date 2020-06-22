package com.mqk.netty.NIO;


import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class NIOBasicBuffer {
	@Test
	public void test(){
		IntBuffer intBuffer = IntBuffer.allocate(5);
		//buffer存放数据
		for (int i = 0; i < intBuffer.capacity(); i++) {
			intBuffer.put(i*2);
		}
		//将buffer转换，读写切换  position 初始化
		intBuffer.flip();
		while (intBuffer.hasRemaining()){
			System.out.println(intBuffer.get());
		}
	}
}
