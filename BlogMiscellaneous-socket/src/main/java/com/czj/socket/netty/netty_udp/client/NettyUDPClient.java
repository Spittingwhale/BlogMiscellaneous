package com.czj.socket.netty.netty_udp.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

//@Component
public class NettyUDPClient {
    public void run(int port) {

        EventLoopGroup group  = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST,true)//允许广播
                    .handler(new NettyUDPClientHandler());//设置消息处理器

            Channel ch = b.bind(0).sync().channel();
            //向网段内的所有机器广播UDP消息。
            ch.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("HELLO", CharsetUtil.UTF_8), new InetSocketAddress("127.0.0.1",port))).sync();
            if(!ch.closeFuture().await(15000)){
                System.out.println("查询超时！");
            }
        } catch (Exception e) {
            group.shutdownGracefully();
        }
    }
    public static void main(String [] args) {
        int port = 60000;
        new NettyUDPClient().run(port);
    }
}
