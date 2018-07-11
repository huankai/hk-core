package com.hk.core.exception;

/**
 * 
 * @author: kevin
 * @date 2017年8月30日下午5:30:09
 */
@SuppressWarnings("serial")
public class ServiceException extends RuntimeException {

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(String message, Throwable t) {
		super(message, t);
	}

}
