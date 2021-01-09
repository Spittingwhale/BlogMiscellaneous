package com.czj.socket.netty.netty_tcp.server;

import com.czj.socket.netty.netty_tcp.util.Request;
import com.czj.socket.netty.netty_tcp.util.Response;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@ChannelHandler.Sharable
@Service
public class NettyServerHandler extends SimpleChannelInboundHandler<Request> {
    private static final Logger log = LoggerFactory.getLogger(NettyServerHandler.class);


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //System.out.println("异常捕获了");
        cause.printStackTrace();
    }

    private static Channel channel = null;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("新客户端加入~~~~~~~~~");
        channel = ctx.channel();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Request request) throws Exception {

        log.info("收到客户端数据");
        log.info("Module: " + request.getModule());
        log.info("CMD: " + request.getCmd());
        if (request.getData() != null) {
            log.info("Data: " + new String(request.getData()));
        }
        Response response = new Response();
        response.setData("12345678901234567890123456789012345678901234567890".getBytes());
        response.setCmd((short) 0x21);
        response.setModule((short) 0x23);
        ctx.writeAndFlush(response);
    }
}
