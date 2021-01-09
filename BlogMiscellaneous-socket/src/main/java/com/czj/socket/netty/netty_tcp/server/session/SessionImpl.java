package com.czj.socket.netty.netty_tcp.server.session;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 会话封装类
 */
public class SessionImpl implements Session {


	private static final Logger log = LoggerFactory.getLogger(SessionImpl.class);
	/**
	 * 绑定对象key
	 */
	public static AttributeKey<Object> ATTACHMENT_KEY  = AttributeKey.valueOf("ATTACHMENT_KEY");
	
	/**
	 * 实际会话对象
	 */
	private Channel channel;

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public SessionImpl(Channel channel) {
		this.channel = channel;
	}

	@Override
	public Object getAttachment() {
		return channel.attr(ATTACHMENT_KEY).get();
	}

	@Override
	public void setAttachment(Object attachment) {
		channel.attr(ATTACHMENT_KEY).set(attachment);
	}
	
	@Override
	public void removeAttachment() {
		channel.attr(ATTACHMENT_KEY).remove();
	}

	@Override
	public void write(Object message) {
		//System.out.println("写出了信息："+message.toString());
		try {
			channel.writeAndFlush(message);
			//channel.flush();
		}catch (Exception e){
			log.error("",e);
			e.printStackTrace();
		}
	}

	@Override
	public boolean isConnected() {
		return channel.isActive();
	}

	@Override
	public void close() {
		channel.close();
	}

	@Override
	public String getBoxId() {
		Object object = getAttachment();
		if(object == null) return null;
		if(object instanceof  List) {
			List<String> us = (List<String>) object;
			if (us.size() > 0) {
				for (String u : us) {
					if (u.startsWith("box_")) {
						return u;
					}
				}
			}
		}
		return null;
	}

	@Override
	public String getUserId() {
		Object object = getAttachment();
		if(object != null && object instanceof  String){
			String id = (String)object;
			return id;
		}
		return null;
	}

	public static void main(String[] args) {
		List<String> s = new ArrayList<>();
		String a = "123";
		String b = "456";
		s.add(a);
		s.add(b);
		System.out.println(s instanceof List);
	}

}
