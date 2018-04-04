package com.hk.core.authentication.shiro.listeners;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志记录监听器
 * 
 * @author huangkai
 * @date 2017年10月22日下午2:13:27
 */
public class LogSessionListener extends SessionListenerAdapter {

	private static Logger logger = LoggerFactory.getLogger(LogSessionListener.class);

	@Override
	public void onStart(Session session) {
		log("会话创建：{}", session.getId());
	}

	@Override
	public void onStop(Session session) {
		log("会话停止：{}", session.getId());
	}

	@Override
	public void onExpiration(Session session) {
		log("会话过期：{}", session.getId());
	}

	private void log(String message, Object... args) {
		if (logger.isDebugEnabled()) {
			logger.debug(message, args);
		}
	}

}
