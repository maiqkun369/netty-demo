package com.mqk.netty.Netty.dubborpc.provider;

import com.mqk.netty.Netty.dubborpc.publicinterface.HelloService;

public class HelloServiceImpl implements HelloService {
	private static  int count=0;
	@Override
	public String hello(String msg) {
		System.out.println("收到客户端消息->"+msg);
		if(msg!=null){
			return "你好客户端，我已经收到消息 ["+msg+"] 第"+(++count)+"";
		}
		return  "你好客户端，我已经收到消息";
	}
}
