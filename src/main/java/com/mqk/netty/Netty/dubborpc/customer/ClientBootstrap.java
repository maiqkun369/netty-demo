package com.mqk.netty.Netty.dubborpc.customer;

import com.mqk.netty.Netty.dubborpc.netty.NettyClient;
import com.mqk.netty.Netty.dubborpc.publicinterface.HelloService;

import java.util.concurrent.TimeUnit;

public class ClientBootstrap {
	//定义协议头 HelloService#hello#
	public static final String providerName="HelloService#hello#";
	public static void main(String[] args) {
		//创建消费者
		NettyClient nettyClient = new NettyClient();
		//创建代理对象
		HelloService service =(HelloService)nettyClient.getBean(HelloService.class, providerName);
		for (;;){
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//通过代理对象调用服务提供者的方法（服务）
			String res = service.hello("给爷爬~~~~~~~");
			System.out.println("调用的结果 res="+res);
		}
	}

}
