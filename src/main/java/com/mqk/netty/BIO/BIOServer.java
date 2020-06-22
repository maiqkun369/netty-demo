package com.mqk.netty.BIO;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {
	@Test
	public void test() throws IOException {
		ExecutorService threadPool = Executors.newCachedThreadPool();
		ServerSocket serverSocket = new ServerSocket(6666);
		Console.log("服务器启动了");
		while (true){
			final Socket socket = serverSocket.accept();
			Console.log("连接到一个客户端");
			threadPool.execute(()->{
				//与客户端通信
				handler(socket);
			});
		}
	}
	public void handler(Socket socket){
		byte[] bytes=new byte[1024];
		try {
			System.out.println("线程信息："+Thread.currentThread().getId()+"  线程名字为： "+Thread.currentThread().getName()+"当前时间为："+new Date());
			InputStream inputStream = socket.getInputStream();
			int len=0;
			while ((len=inputStream.read(bytes))!=-1){
				System.out.println("线程信息："+Thread.currentThread().getId()+"  线程名字为： "+Thread.currentThread().getName()+"当前时间为："+new Date());
				Console.error(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss") +"\n"+new String(bytes,0,len));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			Console.log("关闭连接");
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


	}
}
