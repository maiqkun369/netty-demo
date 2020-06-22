package com.mqk.netty.Netty.dubborpc.provider;

import com.mqk.netty.Netty.dubborpc.netty.NettyServer;


public class ServerBootstrap {
	public static void main(String[] args) {
		NettyServer.startServer("127.0.0.1",7000);
	}
}
