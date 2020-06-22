package com.mqk.netty.Netty.codec2;

import com.mqk.netty.Netty.codec.StudentPOJO;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 *自定义的Handler需要继承Netty规定好的某个HandlerAdapter
 */
//public class NettyServerHandler extends ChannelInboundHandlerAdapter {
public class NettyServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage>{
	/**
	 * 可以读取客户端发送的消息
	 * @param ctx 上下文对象，含有pipeline,channel,地址
	 * @param message 客户端发送的数据
	 * @throws Exception
	 */
	@Override
	public void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage message) throws Exception {
		//根据dataType来显示不同的信息
		MyDataInfo.MyMessage.DataType dataType = message.getDataType();
		if(dataType==MyDataInfo.MyMessage.DataType.StudentType){
			MyDataInfo.Student student = message.getStudent();
			System.out.println("学生Id="+student.getId()+" 学生姓名name="+student.getName());
		}else if(dataType==MyDataInfo.MyMessage.DataType.WorkerType){
			MyDataInfo.Worker worker = message.getWorker();
			System.out.println("工人姓名name="+worker.getName()+" 工人年龄age="+worker.getAge());
		}else {
			System.out.println("传输的类型不正确");
		}
	}

	/**
	 * 读取数据完毕
	 * @param ctx
	 * @throws Exception
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.copiedBuffer("hello, client:your just a little brother",CharsetUtil.UTF_8));//数据写入到缓冲并刷新
	}
	/**
	 * 处理异常，一般是关闭通道
	 * @param ctx
	 * @param cause
	 * @throws Exception
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
}
