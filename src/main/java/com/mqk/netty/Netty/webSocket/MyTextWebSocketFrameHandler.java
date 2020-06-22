package com.mqk.netty.Netty.webSocket;

import cn.hutool.core.date.DateUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

//TextWebSocketFrame表示一个文本帧
public class MyTextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>{

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame msg) throws Exception {
		System.out.println("服务器收到消息: "+msg.text());
		//回复浏览器
		channelHandlerContext.writeAndFlush(new TextWebSocketFrame("服务器时间 "+ DateUtil.format(LocalDateTime.now(),"yyyy-MM-dd HH:mm:ss ")+"\n"+msg.text()));
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		//id表示唯一的值，LongText是唯一的 ShortText不是唯一的
		System.out.println("handlerAdded 被调用了 "+ctx.channel().id().asLongText());
		System.out.println("handlerAdded 被调用了 "+ctx.channel().id().asShortText());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

		System.out.println("handlerRemoved被调用 "+ctx.channel().id().asLongText());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("异常发生 "+cause.getMessage());
		ctx.close();
	}
}
