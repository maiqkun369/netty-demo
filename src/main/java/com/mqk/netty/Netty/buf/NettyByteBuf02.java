package com.mqk.netty.Netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;
import java.util.ArrayList;

public class NettyByteBuf02 {
	public static void main(String[] args) {


		ByteBuf buf = Unpooled.copiedBuffer("Hello,world!", Charset.forName("utf-8"));
		if(buf.hasArray()){
			byte[] content = buf.array();
			System.out.println(new String(content, Charset.forName("utf-8")));
			System.out.println("byteBuf="+buf);

			System.out.println(buf.arrayOffset());
			System.out.println(buf.readerIndex());
			System.out.println(buf.writerIndex());
		}
		System.out.println(buf.getCharSequence(5, 6, Charset.forName("utf-8")));
	}
}
