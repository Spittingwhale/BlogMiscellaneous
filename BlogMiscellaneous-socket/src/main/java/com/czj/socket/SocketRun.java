package com.czj.socket;

import com.czj.socket.netty.netty_tcp.client.NettyClient;
import com.czj.socket.netty.netty_tcp.server.NettyServer;
import com.czj.socket.netty.netty_udp.client.NettyUDPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SocketRun implements ApplicationRunner {
    @Autowired
    private NettyClient nettyClient;
    @Autowired
    private NettyServer nettyServer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        nettyServer.start();
//        Thread.sleep(1000);
//        nettyClient.doconnect();
    }
}
