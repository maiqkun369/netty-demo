package com.mqk.netty.Netty.codec2;

import com.mqk.netty.Netty.codec.StudentPOJO;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Random;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
	/**
	 * 当通道就绪时就会触发该方法
	 * @param ctx
	 * @throws Exception
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//随机的发送Student或者Worker对象
		int random = new Random().nextInt(3);
		MyDataInfo.MyMessage myMessage=null;
		if(random==0){//发送Student对象
			myMessage = MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.StudentType)
					.setStudent(MyDataInfo.Student.newBuilder().setId(5).setName("Curry").build()).build();
		}else {//发送Worker对象
			myMessage = MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.WorkerType)
					.setWorker(MyDataInfo.Worker.newBuilder().setAge(18).setName("Kobe Bryant").build()).build();
		}
		ctx.writeAndFlush(myMessage);

	}

	/**
	 * 当通道有读取事件时，会触发
	 * @param ctx
	 * @param msg
	 * @throws Exception
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf=(ByteBuf)msg;
		System.out.println("服务器回复的消息:"+buf.toString(CharsetUtil.UTF_8));
		System.out.println("服务器的地址："+ctx.channel().remoteAddress());
	}

	/**
	 *处理异常
	 * @param ctx
	 * @param cause
	 * @throws Exception
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
