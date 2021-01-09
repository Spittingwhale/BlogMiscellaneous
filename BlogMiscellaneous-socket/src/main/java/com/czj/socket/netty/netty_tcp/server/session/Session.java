package com.czj.socket.netty.netty_tcp.server.session;

import io.netty.channel.Channel;

/**
 * 会话抽象接口
 */
public interface Session {
	
	/**
	 * 会话绑定对象
	 * @return
	 */
	Object getAttachment();

	Channel getChannel();
	
	/**
	 * 绑定对象
	 * @return
	 */
	void setAttachment(Object attachment);
	
	/**
	 * 移除绑定对象
	 * @return
	 */
	void removeAttachment();
	
	/**
	 * 向会话中写入消息
	 * @param message
	 */
	void write(Object message);
	
	/**
	 * 判断会话是否在连接中
	 * @return
	 */
	boolean isConnected();
	
	/**
	 * 关闭
	 * @return
	 */
	void close();

	String getBoxId();

	String getUserId();
}
