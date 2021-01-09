package com.czj.socket;

import com.czj.socket.netty.netty_tcp.client.NettyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;

@Component
public class SockeDestroy  implements DisposableBean, ExitCodeGenerator {
    private static final Logger log = LoggerFactory.getLogger(SockeDestroy.class);
    @Override
    public void destroy() throws Exception {
        try {
            if (NettyClient.tcpConnectThread != null) {
                NettyClient.tcpConnectThread.interrupt();
                Thread.sleep(1000);
            }
            log.info("关闭客户端连接~~~");
            log.info("主程序退出~~~~~");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getExitCode() {
        log.info("主程序退出~~~~~exitCode");
        return 0;
    }
}
