package com.czj.socket.netty.netty_tcp.util;

import com.czj.socket.netty.netty_tcp.ConstantValue;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * <pre>
 * 数据包格式
 * +——----——+——-----——+——----——+——----——+——-----——+
 * |  包头	|  模块号      |  命令号    |   长度     |   数据       |
 * +——----——+——-----——+——----——+——----——+——-----——+
 * </pre>
 */
public class RequestEncoder extends MessageToByteEncoder<Request> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Request message, ByteBuf buffer) throws Exception {

		//包头
		buffer.writeByte(ConstantValue.HEADER_FLAG);
		//module
		buffer.writeByte(message.getModule());
		//cmd
		buffer.writeByte(message.getCmd());
		//长度
		int lenth = message.getData()==null? 0 : message.getData().length;
		if(lenth <= 0){
			buffer.writeShort(lenth);
		}else{
			buffer.writeShort(lenth);
			buffer.writeBytes(message.getData());
		}
	}
}
