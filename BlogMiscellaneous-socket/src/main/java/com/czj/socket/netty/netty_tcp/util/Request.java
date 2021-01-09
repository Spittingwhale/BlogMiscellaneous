package com.czj.socket.netty.netty_tcp.util;
/**
 * 消息对象
 */
public class Request {
	
	/**
	 * 模块号 不能使用0x7e
	 */
	private short module;
	
	/**
	 * 命令号 不能使用0x7e
	 */
	private short cmd;
	
	/**
	 * 数据
	 */
	private byte[] data;
	
	public static Request valueOf(short module, short cmd, byte[] data){
		Request request = new Request();
		request.setModule(module);
		request.setCmd(cmd);
		request.setData(data);
		return request;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public short getModule() {
		return module;
	}

	public void setModule(short module) {
		this.module = module;
	}

	public short getCmd() {
		return cmd;
	}

	public void setCmd(short cmd) {
		this.cmd = cmd;
	}
}
