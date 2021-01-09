package com.czj.socket.netty.netty_tcp.server;

import com.czj.socket.netty.netty_tcp.util.RequestDecoder;
import com.czj.socket.netty.netty_tcp.util.ResponseEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NettyServer {

    private static final Logger log = LoggerFactory.getLogger(NettyServer.class);

    int port = 9999;

    @Autowired
    private NettyServerHandler nettyServerHandler;

    /**
     * 启动
     */
    public void start() {

        // 服务类
        ServerBootstrap b = new ServerBootstrap();

        // 创建boss和worker
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        //业务线程池
        EventLoopGroup businessGroup = new NioEventLoopGroup(5);

        try {
            // 设置循环线程组事例
            b.group(bossGroup, workerGroup);

            // 设置channel工厂
            b.channel(NioServerSocketChannel.class);

            b.option(ChannelOption.SO_REUSEADDR, true);
            b.option(ChannelOption.SO_BACKLOG, 8192);// 链接缓冲池队列大小
            b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);//建立连接的超时时间
            b.option(ChannelOption.SO_SNDBUF, 16 * 1024);
            b.option(ChannelOption.SO_RCVBUF, 16 * 1024);
            b.childOption(ChannelOption.SO_KEEPALIVE, true);
            b.childOption(ChannelOption.TCP_NODELAY, true);
            // 设置管道
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
//                    ch.pipeline().addLast("log", new LoggingHandler(LoggerFactory.class));
                    ch.pipeline().addLast("bytesDecoder", new RequestDecoder());
                    ch.pipeline().addLast("bytesEncoder", new ResponseEncoder());
                    ch.pipeline().addLast(businessGroup, nettyServerHandler);

                }
            });
            // 可以绑定端口
            b.bind(port).sync();
            log.info("NettyServer start ip: 127.0.0.1 port:{} ", port);
        } catch (Exception e) {
            log.error("", e);
            e.printStackTrace();
        }
    }
}
