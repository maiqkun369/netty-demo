package com.mqk.netty.Netty.groupChat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GroupCharServerHandler extends SimpleChannelInboundHandler<String> {
	//定义一个channel组，管理所有的channel
	//GlobalEventExecutor.INSTANCE全局事件执行器，是一个单例
	private static ChannelGroup channelGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 表示建立连接，一旦建立，第一个被执行
	 * 将当前channel加入到channelGroup
	 * @param ctx
	 * @throws Exception
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		super.handlerAdded(ctx);
		Channel channel = ctx.channel();
		//将该客户加入聊天的信息推送给别的客户端writeAndFlush会将channelGroup中的channel遍历并且发送消息
		channelGroup.writeAndFlush("[客户端]"+simpleDateFormat.format(new Date())+"->"+channel.remoteAddress()+" 加入聊天\n");
		channelGroup.add(channel);
	}

	/**
	 * 表示channel处于活动状态,提示 xx上线，可在此处利用Map<User,channel>实现登录
	 * @param ctx
	 * @throws Exception
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		System.out.println(simpleDateFormat.format(new Date())+"->"+ctx.channel().remoteAddress()+" 上线~~~~~~~~~~~");
	}

	/**
	 * 表示channel处于不活动状态，提示 xx离线
	 * @param ctx
	 * @throws Exception
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		System.out.println(simpleDateFormat.format(new Date())+"->"+ctx.channel().remoteAddress()+" 离线~~~~~~~~~~~");
	}

	/**
	 * 断开连接 会触发handlerRemoved, 将XX客户信息推送给其他在线的客户端
	 * @param ctx
	 * @throws Exception
	 */
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		super.handlerRemoved(ctx);
		Channel channel = ctx.channel();
		channelGroup.writeAndFlush("[客户端]"+simpleDateFormat.format(new Date())+"->"+channel.remoteAddress()+" 离开了\n");
		System.out.println("channelGroup size"+channelGroup.size());
	}

	/**
	 * 异常处理
	 * @param ctx
	 * @param cause
	 * @throws Exception
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		ctx.close();
	}

	/**
	 * 读取数据
	 * @param ctx
	 * @param s
	 * @throws Exception
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
		Channel channel = ctx.channel();
		//遍历channelGroup，排除掉自己
		channelGroup.stream().forEach(ch -> {
			if(channel!=ch){//不是当前的channel 转发消息
				ch.writeAndFlush("[客户] "+simpleDateFormat.format(new Date())+"->"+channel.remoteAddress()+" 发送了消息:"+s+"\n");
			}else{
				ch.writeAndFlush("[自己] "+simpleDateFormat.format(new Date())+" 发送了消息:"+s+"\n");
			}
		});

	}
}
