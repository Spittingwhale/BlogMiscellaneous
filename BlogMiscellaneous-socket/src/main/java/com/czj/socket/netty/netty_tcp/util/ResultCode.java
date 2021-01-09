package com.czj.socket.netty.netty_tcp.util;

public interface ResultCode {
	
	/**
	 * 成功
	 */
	int SUCCESS = 0;
	
	/**
	 * 找不到命令
	 */
	int NO_INVOKER = 1;
	
	/**
	 * 参数异常
	 */
	int AGRUMENT_ERROR = 2;
	
	/**
	 * 未知异常
	 */
	int UNKOWN_EXCEPTION = 3;
	
	/**
	 * 用户名或密码不能为空
	 */
	int USERNAME_NULL = 4;
	
	/**
	 * 用户名已使用
	 */
	int USER_EXIST = 5;
	
	/**
	 * 用户不存在
	 */
	int USER_NO_EXIST = 6;
	
	/**
	 * 密码错误
	 */
	int PASSWARD_ERROR = 7;
	
	/**
	 * 您已登录
	 */
	int HAS_LOGIN = 8;
	
	/**
	 * 登录失败
	 */
	int LOGIN_FAIL = 9;
	
	/**
	 * 用户不在线
	 */
	int USER_NO_ONLINE = 10;
	
	/**
	 * 请先登录
	 */
	int LOGIN_PLEASE = 11;
}
