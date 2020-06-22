package com.mqk.netty.Netty.buf;

import cn.hutool.core.util.StrUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyByteBuf01 {
	public static void main(String[] args) {
		//创建对象，该对象包含一个数组arr，是一个byte[10]
		//netty 的buf中不用使filp()切换读写模式,底层维护了writerindex和readerindex
		ByteBuf buffer = Unpooled.buffer(10);
		for (int i = 0; i <10 ; i++) {
			buffer.writeByte(i);

		}
		for (int i = 0; i <buffer.capacity() ; i++) {
			System.out.println(buffer.readByte());
		}
	}
}
