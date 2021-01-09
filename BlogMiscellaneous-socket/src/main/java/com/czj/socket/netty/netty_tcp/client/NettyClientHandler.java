package com.czj.socket.netty.netty_tcp.client;

import com.czj.socket.netty.netty_tcp.ConstantValue;
import com.czj.socket.netty.netty_tcp.util.Request;
import com.czj.socket.netty.netty_tcp.util.Response;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * 服务端消息处理
 */
@ChannelHandler.Sharable
@Service
public class NettyClientHandler extends SimpleChannelInboundHandler<Response> {
    private static final Log log = LogFactory.getLog(NettyClientHandler.class);

    /**
     * 服务端接入
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("----------------- 连接StatusServer成功");
        NettyClient.channel = ctx.channel();
        log.info("=====>socket开始登陆");
        logInBytes(ctx.channel());
    }

    /**
     * 客户端断开
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("客户端连接断开");
    }

    /**
     * 异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.info("----------------- 连接服务断开");
        cause.printStackTrace();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.READER_IDLE)) {
                //长期没有收到服务端数据
            } else if (event.state().equals(IdleState.WRITER_IDLE)) {
                //长期未向服务器发送数据
            } else if (event.state().equals(IdleState.ALL_IDLE)) {
                //长时间 既未收到服务器发送数据，又未向服务器发送数据
            }
        }
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Response response) {
        try {
            log.info("收到服务端数据：");
            log.info("Module: " + response.getModule());
            log.info("Cmd: " + response.getCmd());
            if (response.getData() != null) {
                log.info("Data: " + new String(response.getData()));
            }

            Request request = new Request();
            request.setModule((short) 21);
            request.setCmd((short) 1);
            request.setData("收到数据".getBytes(StandardCharsets.UTF_8));
            ctx.writeAndFlush(request);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void logInBytes(Channel ctx) {
        log.info("=====>向服务端发送登录信息");
        ByteBuf logbuffer = Unpooled.buffer();
        logbuffer.writeByte(ConstantValue.HEADER_FLAG);
        logbuffer.writeByte(10);
        logbuffer.writeByte(0);
        String serviceId = "video";
        String password = "12345678";
        byte[] logStr = (serviceId + "#" + password).getBytes(StandardCharsets.UTF_8);
        byte[] logBytes = ConstantValue.encodeData(logStr, logStr.length);
        logbuffer.writeShort(logBytes.length);
        logbuffer.writeBytes(logBytes);
        ctx.writeAndFlush(logbuffer);
        log.info("=====>向服务端发送登录信息-----完成");
    }
}
