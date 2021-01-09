package com.czj.socket.websocket.server;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;


public class WebSocketServerDemo extends WebSocketServer {

    int count = 10000;
    public static Map<Integer, WebSocket> socketMap = new HashMap<>();

    public WebSocketServerDemo() throws UnknownHostException {
    }

    public WebSocketServerDemo(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
        System.out.println("websocket Server start at port:" + port);
    }


    /**
     * 触发连接事件
     */
    @Override
    public void onOpen(WebSocket conn, ClientHandshake clientHandshake) {
        count++;
        conn.setAttachment(count);
        socketMap.put(count, conn);
        System.out.println("new connection ===  " + conn.getRemoteSocketAddress().getAddress().getHostAddress() + "   \n\r count ===" + count);
        conn.send("连接成功-------  id:" + conn);
    }

    /**
     * 连接断开时触发关闭事件
     */
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Integer attachment = conn.getAttachment();
        if (socketMap.containsKey(attachment)) {
            socketMap.remove(attachment);
        }
    }

    /**
     * 客户端发送消息到服务器时触发事件
     */
    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("you have a new message: " + message);

        if (socketMap.containsKey(conn.getAttachment())) {

        }
        //向客户端发送消息
        conn.send("服务端收到消息回执~~~~");
    }

    /**
     * 触发异常事件
     */
    @Override
    public void onError(WebSocket conn, Exception e) {
        //e.printStackTrace();
        if (conn != null) {
            //some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    @Override
    public void onStart() {
        System.out.println("on Start");
    }

    //向指定客户端发送消息
    public static void sendToClient(Integer integer, String message) {
        System.out.println("给web客户端 ： " + integer + "发送消息");
        socketMap.forEach((s, webSocket) -> {
            if (s == integer) {
                webSocket.send(message);
            }
        });
    }

    /**
     * 启动服务端
     *
     * @param args
     * @throws UnknownHostException
     */
    public static void main(String[] args) throws UnknownHostException {
        new WebSocketServerDemo(8887).start();
    }
}
