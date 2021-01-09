package com.czj.socket.netty.netty_tcp.client;

import com.czj.socket.netty.netty_tcp.util.RequestEncoder;
import com.czj.socket.netty.netty_tcp.util.ResponseDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * netty客户端
 */
@Service
public class NettyClient {
    private static final Logger log = LoggerFactory.getLogger(NettyClient.class);

    private int port = 9999;
    private String ip = "127.0.0.1";

    @Autowired
    public NettyClientHandler listenPaidServerHandler;
    public Bootstrap bootstrap;
    public EventLoopGroup group;
    public ChannelFuture future;
    public static Channel channel;
    public static Thread tcpConnectThread;

    @PreDestroy
    public void destroy() {
        try {
            if (tcpConnectThread != null) {
                tcpConnectThread.interrupt();
                Thread.sleep(1000);
            }
            log.info("关闭客户端");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void doconnect() {
        /*if (System.currentTimeMillis() - lastConnectTime < 2 * 60 * 1000) {
            return;
        }
        lastConnectTime = System.currentTimeMillis();
        if (channel != null && channel.isActive()) {
            return;
        }*/
        tcpConnectThread = new Thread(() -> {
            while (true) {

                if (channel == null || !channel.isActive()) {
                    try {
                        bootstrap = new Bootstrap();
                        //boss和worker
                        EventLoopGroup group = new NioEventLoopGroup();
                        //设置线程池
                        bootstrap.group(group);
                        //设置socket工厂、
                        bootstrap.channel(NioSocketChannel.class);

                        //设置管道工厂
                        bootstrap.handler(new ChannelInitializer<Channel>() {

                            @Override
                            protected void initChannel(Channel ch) throws Exception {
                                ch.pipeline().addLast("log", new LoggingHandler(LoggerFactory.class));
                                ch.pipeline().addLast("idle", new IdleStateHandler(120, 120, 200, TimeUnit.SECONDS));
                                ch.pipeline().addLast("bytesDecoder", new ResponseDecoder());
                                ch.pipeline().addLast("bytesEncoder", new RequestEncoder());
                                ch.pipeline().addLast(listenPaidServerHandler);
                            }
                        });
                        //设置参数，TCP参数
                        bootstrap.option(ChannelOption.SO_BACKLOG, 2048);//serverSocketchannel的设置，链接缓冲池的大小
                        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);//socketchannel的设置,维持链接的活跃，清除死链接
                        bootstrap.option(ChannelOption.TCP_NODELAY, true);//socketchannel的设置,关闭延迟发送

                        future = bootstrap.connect(ip, port);

                        log.info("ListenServer start ip:{} port:{} ", ip, port);
                        future.addListener(new ChannelFutureListener() {
                            @Override
                            public void operationComplete(ChannelFuture future) throws Exception {
                                if (future.isSuccess()) {
                                    channel = future.channel();
                                    log.info("=====>socket tcp链接建立成功");
                                    //发送登录信息
//                                    listenPaidServerHandler.logInBytes(channel);

                                } else {
                                    log.info("=====>socket tcp链接建立失败");
                                }
                            }
                        });
                        //等待服务端关闭
                        future.channel().closeFuture().sync();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        //释放资源
                        group.shutdownGracefully().syncUninterruptibly();
                    }
                }

            }
        });

        if (channel == null) {
            tcpConnectThread.start();
        }
    }
}
