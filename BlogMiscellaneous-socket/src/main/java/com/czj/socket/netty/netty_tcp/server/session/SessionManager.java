package com.czj.socket.netty.netty_tcp.server.session;

import com.czj.socket.netty.netty_tcp.util.Response;
import com.google.protobuf.GeneratedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 会话管理者
 */
public class SessionManager {
    private static final Logger log = LoggerFactory.getLogger(SessionManager.class);

    private static String BoxKeyPre = "BOX:TCPTIME:";
    private static String BoxShutKeyPre = "BOX:SHUTTIME:";
    private static String UserKeyPre = "USER:TCPTIME:";
    private static String UserNotify = "USER:NOTIFYTIME:";
    public static HashMap<String, Long> boxRelunchTime;

    /**
     * 在线会话
     */
    private static final Map<String, Session> onlineSessions = new ConcurrentHashMap<>();

    /**
     * 用户盒子关系
     */
    private static final Map<String, String> userBox = new ConcurrentHashMap<>();

    /**
     * 用户TCP心跳包最近时间
     */
    private static final Map<String, Long> boxTime_ = new ConcurrentHashMap<>();

    /**
     * 用户TCP心跳包最近时间
     */
    private static final Map<String, Long> userTime_ = new ConcurrentHashMap<>();

    /**
     * 在线box会话
     */
    private static final ConcurrentHashMap<String, Session> onlineBoxSessions = new ConcurrentHashMap<>();

    public static List<String> getUsers() {
        List<String> uid = new ArrayList<>();
        for (String k : userBox.keySet()) {
            uid.add(k);
        }
        return uid;
    }

    public static List<String> getPadUsers() {
        List<String> uid = new ArrayList<>();
        for (String k : onlineSessions.keySet()) {
            uid.add(k);
        }
        return uid;
    }

    //获取盒子上次重启时间，防止连续重启
    public static boolean getBoxRelunchTime(String boxId) {
        if (boxRelunchTime == null) {
            return true;
        }
        Iterator<Map.Entry<String, Long>> iterator = boxRelunchTime.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Long> next = iterator.next();
            if (boxId != null && boxId.equals(next.getKey())) {
                Long lastTime = next.getValue();
                if (System.currentTimeMillis() - lastTime < 20 * 60 * 1000) {
                    return false;
                }
            }
        }

        return true;
    }

    //设置盒子重启时间
    public static void setBoxRelunchTime(String boxId, Long relunchTime) {
        if (boxRelunchTime == null) {
            boxRelunchTime = new HashMap<>();
        }
        boxRelunchTime.put(boxId, relunchTime);
    }

    /**
     * 加入
     *
     * @param userId
     * @return
     */
    public static boolean putSession(String userId, Session session) {
        if (!onlineSessions.containsKey(userId)) {
            boolean success = onlineSessions.putIfAbsent(userId, session) == null ? true : false;
            return success;
        }
        return false;
    }

    /**
     * 加入
     *
     * @param userId
     * @return
     */
    public static boolean putSession(String userId, String boxId) {
        userBox.put(userId, boxId);
        return true;
    }

    /**
     * 清除盒子用户
     *
     * @param boxId 盒子id
     * @return
     */
    public static void clearUsers(String boxId) {
        Iterator<Map.Entry<String, String>> iterator = userBox.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String value = entry.getValue();
            if (boxId.equals(value)) {
                iterator.remove();
            }
        }
        return;
    }

    /**
     * 加入box
     *
     * @param boxId
     * @return
     */
    public static boolean putBoxSession(String boxId, Session session) {
        if (!isOnlineBox(boxId)) {
            onlineBoxSessions.put(boxId, session);
            return true;
        }
        return false;
    }

    /**
     * 移除
     *
     * @param userId
     */
    public static Session removeSession(String userId) {
        return onlineSessions.remove(userId);
    }

    /**
     * 移除Box
     *
     * @param boxId
     */
    public static Session removeBoxSession(String boxId) {
        return onlineBoxSessions.remove(boxId);
    }

    public static void sendMessageForUser(String userId, short module, short cmd, String message) {
        if (userId != null) {
            Session session = onlineSessions.get(userId);
            if (session != null && session.isConnected()) {
                log.debug("查找到用户session");
                Response response = null;
                try {
                    log.error("找到用户：" + userId + "：推送消息------>模块号：" + module + ",命令号：" + cmd + ",内容：" + message);
                    response = new Response(module, cmd, message.getBytes("utf-8"));
                } catch (UnsupportedEncodingException e) {
                    log.error("", e);
                    e.printStackTrace();
                }
                session.write(response);
            } else {
                if (session != null) {
                    log.error("连接是否不可用：session.isConnected():" + session.isConnected() + "，--->" + session.getUserId());
                } else {
                    log.error("没有查找到用户session");
                    log.error("onlineSessions信息为：");
                    for (Map.Entry<String, Session> entry : onlineSessions.entrySet()) {
                        log.debug(entry.getKey() + "--------->" + entry.getValue());
                    }
                }
            }
        }
    }

    /**
     * 发送消息[字符串]
     *
     * @param
     * @param userId
     * @param message
     */
    public static void sendMessage(String userId, short module, short cmd, String message) {
        log.error("查找用户：" + userId + " 推送消息...");
        String boxId = userBox.get(userId);
        log.error("找到对应的盒子ID:" + boxId);
        if (boxId != null) {
            Session session = onlineBoxSessions.get(boxId);
            if (session != null && session.isConnected()) {
                log.debug("查找到用户session");
                Response response = null;
                try {
                    log.error("找到用户：" + userId + "：推送消息------>模块号：" + module + ",命令号：" + cmd + ",内容：" + message);
                    response = new Response(module, cmd, message.getBytes("utf-8"));
                } catch (UnsupportedEncodingException e) {
                    log.error("", e);
                    e.printStackTrace();
                }
                session.write(response);
            } else {
                if (session != null) {
                    log.error("连接是否不可用：session.isConnected():" + session.isConnected() + "，--->" + session.getBoxId());
                } else {
                    log.error("没有查找到盒子session");
                    log.error("onlineSessions信息为：");
                    for (Map.Entry<String, Session> entry : onlineBoxSessions.entrySet()) {
                        log.debug(entry.getKey() + "--------->" + entry.getValue());
                    }
                }
            }
        }
    }

    public static void clearBoxSession(String boxId) {
        log.error("长时间没收到TCP心跳，清除盒子session");
        Session session = onlineBoxSessions.get(boxId);
        if (session != null)
            session.getChannel().close();
        onlineBoxSessions.remove(boxId);
    }

    public static void clearUserSession(String userId) {
        log.error("长时间没收到TCP心跳，清除用户session");
        Session session = onlineSessions.get(userId);
        if (session != null)
            session.getChannel().close();
        onlineSessions.remove(userId);
    }

    /**
     * 发送消息[字符串]
     *
     * @param
     * @param boxId
     * @param message
     */
    public static void sendBoxMessage(String boxId, short module, short cmd, String message) {
        log.debug("向盒子推送消息：" + boxId);
        Session session = onlineBoxSessions.get(boxId);
        if (session != null && session.isConnected()) {
            log.error("向盒子：" + boxId + " 推送消息------>模块号：" + module + ",命令号：" + cmd + ",内容：" + message);
            Response response = null;
            try {
                response = new Response(module, cmd, message.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                log.error("", e);
                e.printStackTrace();
            }
            session.write(response);
        } else {
            if (session != null) {
                log.error("连接是否不可用：session.isConnected():" + session.isConnected() + "，--->" + session.getBoxId());
            } else {
                log.error("没有查找到盒子session");
                log.error("onlineBoxSessions信息为：");
                for (Map.Entry<String, Session> entry : onlineBoxSessions.entrySet()) {
                    log.debug(entry.getKey() + "--------->" + entry.getValue());
                }
            }
        }
    }

    /**
     * 发送消息[自定义协议]
     *
     * @param <T>
     * @param userId
     * @param message
     */
    /*public static <T extends Serializer> void sendMessage(String userId, short module, short cmd, T message) {
        log.error("查找用户：" + userId + " 推送消息...");
        String boxId = userBox.get(userId);
        log.error("找到对应的盒子ID:" + boxId);
        Session session = onlineBoxSessions.get(boxId);
        if (session != null && session.isConnected()) {
            Response response = new Response(module, cmd, message.getBytes());
            session.write(response);
        }
    }*/

    /**
     * 发送消息[protoBuf协议]
     *
     * @param <T>
     * @param userId
     * @param message
     */
    public static <T extends GeneratedMessage> void sendMessage(String userId, short module, short cmd, T message) {
        log.error("查找用户：" + userId + " 推送消息...");
        String boxId = userBox.get(userId);
        log.error("找到对应的盒子ID:" + boxId);
        Session session = onlineBoxSessions.get(boxId);
        if (session != null && session.isConnected()) {
            Response response = new Response(module, cmd, message.toByteArray());
            session.write(response);
        }
    }

    /**
     * 是否在线
     *
     * @param userId
     * @return
     */
    public static boolean isOnlineUser(String userId) {
        return onlineSessions.containsKey(userId);
    }

    /**
     * Box是否在线
     *
     * @param boxId
     * @return
     */
    public static boolean isOnlineBox(String boxId) {
        Session session = onlineBoxSessions.get(boxId);
        if (session != null && session.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取所有在线用户
     * @return
     */
	/*public static Set<String> getOnlineUsers() {
		return Collections.unmodifiableSet(onlineSessions.keySet());
	}*/

    /**
     * 获取所有在线Box
     *
     * @return
     */
    public static Set<String> getOnlineBoxs() {
        return Collections.unmodifiableSet(onlineBoxSessions.keySet());
    }

}
