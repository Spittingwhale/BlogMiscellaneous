package com.czj.socket.netty.netty_tcp.util;

import com.czj.socket.netty.netty_tcp.ConstantValue;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * 数据包格式
 * +——----——+——-----——+——----——+——----——+——----——+——----——+
 * |  包头	|  模块号      |  命令号    |  结果码    |  长度       |   数据     |
 * +——----——+——-----——+——----——+——----——+——----——+——----——+
 *  包头1字节
 *  模块号1字节
 *  命令号1字节
 *  结果码1字节
 *  长度2字节(数据部分占有字节数量)
 * </pre>
 */
public class ResponseEncoder extends MessageToByteEncoder<Response> {
    private static final Logger log = LoggerFactory.getLogger(ResponseEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Response response, ByteBuf buffer) throws Exception {
        //包头
        buffer.writeByte(ConstantValue.HEADER_FLAG);
        //module和cmd
        buffer.writeByte((byte) response.getModule());
        buffer.writeByte((byte) response.getCmd());
        //结果码
        buffer.writeByte((byte) response.getStateCode());
        //长度
        if (response.getModule() >= 10) {
            int lenth = response.getData() == null ? 0 : response.getData().length;
            if (lenth <= 0) {
                buffer.writeShort(lenth);
            } else {
                byte[] bs = ConstantValue.encodeData(response.getData(), lenth);
                lenth = bs.length;
                if (lenth == (byte) 0x7e) {
                    buffer.writeShort(lenth + 1);
                    buffer.writeBytes(bs);
                    buffer.writeByte(0);
                } else {
                    buffer.writeShort(lenth);
                    buffer.writeBytes(bs);
                }
            }
        }
    }
}
