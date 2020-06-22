package com.mqk.netty.Netty.dubborpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyClient {
	//创建线程池
	private static ExecutorService executor= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	private static NettyClientHandler client;
	private static int count;
	//使用代理模式获取一个代理对象
	public Object getBean(final Class<?> serviceClass,final String providerName){
		return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
				new Class<?>[]{serviceClass},(proxy, method, args) -> {
					System.out.println("(proxy, method, args)进入第"+(++count)+"次");
					//以下部分客户端每调用一次hello，就会执行
					if(client == null){
						initClient();
					}
					//设置要发给服务器的信息
					//providerName->协议头   args[0]->客户端调用api helll(??)传入的参数
					client.setParam(providerName+args[0]);
					return executor.submit(client).get();
				});
	}
	//初始化工作
	private static void initClient(){
		client=new NettyClientHandler();
		//创建EventLoopGroup
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			//创建一个启动对象 客户端使用的不是ServerBootstrap 而是Bootstrap
			Bootstrap bootstrap = new Bootstrap();
			//设置相关参数
			bootstrap.group(group)//设置线程组
					 .channel(NioSocketChannel.class) //设置客户端通道的实现类
					 .option(ChannelOption.TCP_NODELAY,true)
					 .handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							ChannelPipeline pipeline = socketChannel.pipeline();
							pipeline.addLast(new StringDecoder());
							pipeline.addLast(new StringEncoder());
							pipeline.addLast(client);//加入自定义的处理器
						}
					});
			System.out.println("Client is ok.............");
			//启动客户端去连接服务器端 ChannelFuture ---->Netty的异步模型
			bootstrap.connect("127.0.0.1", 7000).sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
