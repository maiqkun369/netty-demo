package com.mqk.netty.NIO.GroupChat;

import cn.hutool.core.date.DateUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Date;
import java.util.Iterator;

public class GroupChatServer {
	private Selector selector;
	private ServerSocketChannel listenchannel;
	private static final int PORT=6667;

	public GroupChatServer() {
		//初始化工作
		try {
			selector=Selector.open();
			listenchannel=ServerSocketChannel.open();
			listenchannel.socket().bind(new InetSocketAddress(PORT));
			//设置非阻塞模式
			listenchannel.configureBlocking(false);
			listenchannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//监听
	public void listen(){
			try {
				while (true) {
					int count = selector.select();
					if(count>0){
						Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
						while (iterator.hasNext()){
							SelectionKey key = iterator.next();
							if(key.isAcceptable()){
								SocketChannel accept = listenchannel.accept();
								accept.configureBlocking(false);
								accept.register(selector,SelectionKey.OP_READ);
								//提示
								System.out.println(accept.getRemoteAddress()+"--->"+DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss")+" 上线");
							}
							if(key.isReadable()){
								//处理读事件
								readData(key);
							}
							//删除当前key防止重复处理
							iterator.remove();
						}
					}else {
						System.out.println("等待中.........");
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {


			}
	}
	//读取客户端消息
	private void readData(SelectionKey key){
		SocketChannel channel=null;
		try {
			channel = (SocketChannel)key.channel();
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			int count = channel.read(buffer);
			if(count>0){
				String msg=new String(buffer.array());
				System.out.println("来自客户端："+msg.trim());
				//向其他的客户端转发消息
				sendInfoToOtherClients(msg,channel);
			}

		}catch (IOException e){
			try {
				System.out.println(channel.getRemoteAddress()+" 离线了....");
				//取消注册
				key.cancel();
				//关闭通道
				channel.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	//转发消息给其他客户端
	private void sendInfoToOtherClients(String msg,SocketChannel self){
		System.out.println("服务器转发消息中......");
		//遍历，所有注册到selector上的SocketChannel并且排除self
		selector.keys().stream().forEach(key -> {
			//取出对应的SocketChannel
			Channel targetChannel = key.channel();
			//排除自己
			if(targetChannel instanceof  SocketChannel && targetChannel !=self){
				SocketChannel dest=(SocketChannel) targetChannel;
				ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
				try {
					dest.write(buffer);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
	}

	public static void main(String[] args) {
		new GroupChatServer().listen();

	}
}
