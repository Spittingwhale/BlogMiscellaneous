package com.czj.socket.netty.netty_tcp.util;

import com.czj.socket.netty.netty_tcp.ConstantValue;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RequestDecoder extends ByteToMessageDecoder {
    private static final Logger log = LoggerFactory.getLogger(RequestDecoder.class);

    /**
     * 数据包基本长度
     */
    public static int BASE_LENTH = 1 + 1;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
//        log.info("收到数据。。。");
        while (true) {
            if (buffer.readableBytes() >= BASE_LENTH) {
                //第一个可读数据包的起始位置
                int beginIndex;
                while (true) {
                    //包头开始游标点
                    beginIndex = buffer.readerIndex();
                    //标记初始读游标位置
                    buffer.markReaderIndex();
                    if (buffer.readByte() == ConstantValue.HEADER_FLAG) {
                        break;
                    }
                    //未读到包头标识略过一个字节
                    buffer.resetReaderIndex();
                    buffer.readByte();

                    //不满足
                    if (buffer.readableBytes() < BASE_LENTH) {
                        return;
                    }
                }
                //读取命令号
                short module = buffer.readByte();

                if (module == 0) {//0x7e00是连接保持命令，不做处理
                    Request message = new Request();
                    message.setModule(module);
                    //解析出消息对象，继续往下面的handler传递
                    out.add(message);

                } else {
                    short cmd = buffer.readByte();
                    log.debug("收到数据，模块号：" + module + ",命令号：" + cmd);
                    //读取数据长度
                    int lenth = buffer.readShort();
                    if (lenth < 0) {
                        //ctx.channel().close();
                    }

                    //数据包还没到齐
                    if (buffer.readableBytes() < lenth) {
                        buffer.readerIndex(beginIndex);
                        return;
                    }

                    //读数据部分
                    byte[] data = new byte[lenth];
                    buffer.readBytes(data);

                    Request message = new Request();
                    message.setModule(module);
                    message.setCmd(cmd);
                    message.setData(ConstantValue.decodeData(data, lenth));
                    //解析出消息对象，继续往下面的handler传递
                    out.add(message);
                }
            } else {
                break;
            }
        }

        //数据不完整，等待完整的数据包
        return;
    }
}