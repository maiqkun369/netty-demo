package com.mqk.netty.NIO.GroupChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GroupChatClient {
	private final String HOST="127.0.0.1";
	private final int POST=6667;
	private Selector selector;
	private SocketChannel socketChannel;
	private String username;

	public GroupChatClient() throws IOException {
		selector=Selector.open();
		socketChannel=socketChannel.open(new InetSocketAddress(HOST, POST));
		socketChannel.configureBlocking(false);
		socketChannel.register(selector, SelectionKey.OP_READ);
		username = socketChannel.getLocalAddress().toString().substring(1);
		System.out.println(username+" is ok !");
	}
	//向服务器发送消息
	public void sendInfo(String info){
		info=username+"说: "+info;
		try {
			socketChannel.write(ByteBuffer.wrap(info.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//去取从服务器端回复的消息
	public void readInfo(){
		try {
			int select = selector.select();
			if(select>0){
				Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
				while (iterator.hasNext()){
					SelectionKey key = iterator.next();
					if(key.isReadable()){
						//得到相关的通道
						SocketChannel channel = (SocketChannel)key.channel();
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						channel.read(buffer);
						String msg=new String(buffer.array());
						System.out.println(msg.trim());
					}
				}
				iterator.remove();//删除当前的selectionkey防止重复操作
			}else{
				//System.out.println("没有可用的通道......");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		GroupChatClient chatClient = new GroupChatClient();
		new Thread(()->{
			while (true){
				chatClient.readInfo();
				try {
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		//发送数据
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNextLine()){
			String s = scanner.nextLine();
			chatClient.sendInfo(s);
		}

	}
}
