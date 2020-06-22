package com.mqk.netty.NIO;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
	public static void main(String[] args) throws IOException {

		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

		//得到一个selector对象
		Selector selector = Selector.open();
		//绑定一个端口
		serverSocketChannel.socket().bind(new InetSocketAddress(6666));
		//设置为非阻塞
		serverSocketChannel.configureBlocking(false);
		//注册到Selector上绑定事件 op_accept
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

		while (true){
			//开始监听
			if(selector.select(1000)==0){
				//没有事件发生
				System.out.println("服务器等待了1s，无连接");
				continue;
			}
			//发生事件,通过selectionKeys可获得相应的通道
			Set<SelectionKey> selectionKeys = selector.selectedKeys();

			Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

			while (keyIterator.hasNext()){
				//获得selectionKey
				SelectionKey key = keyIterator.next();
				//根据key，对应的通道发生的事件做相应的处理
				if(key.isAcceptable()){
					//给该客户端生成一个 SocketChannel
					SocketChannel socketChannel = serverSocketChannel.accept();
					System.out.println("客户端连接成功 生成了一个socketChannel"+socketChannel.hashCode());
					socketChannel.configureBlocking(false);
					//socketChannel注册到selector上,关注事件为OP_READ并关联一个buffer
					socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
					System.out.println("客户端连接后，注册的selectionKey的数量="+selector.keys().size());
				}
				if(key.isReadable()){
					//通过key反向获取到对应的channel
					SocketChannel channel = (SocketChannel)key.channel();
					//获取到该channel关联的buffer
					ByteBuffer buffer =(ByteBuffer) key.attachment();
					channel.read(buffer);
					System.out.println("来自客户端 "+new Date()+"\n"+new String(buffer.array()));
				}
				//手动从集合中移除当前的selectionkey防止重复操作
				keyIterator.remove();
			}


		}

	}
}
