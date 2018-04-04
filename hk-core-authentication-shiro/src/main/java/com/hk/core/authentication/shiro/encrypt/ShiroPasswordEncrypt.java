package com.hk.core.authentication.shiro.encrypt;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.Sha512Hash;

import com.hk.commons.util.encrypt.Encrypt;

/**
 * 加密工具类
 *
 * @author huangkai
 * @date 2017年10月24日上午10:07:13
 */
public class ShiroPasswordEncrypt implements Encrypt {


	@Override
	public String asSha512HashToBase64(Object source, Object salt, int hashIterations) {
		return new Sha512Hash(source, salt, hashIterations).toBase64();
	}

	@Override
	public String asMD5ToString(Object source, Object salt, int hashIterations) {
		return new Md5Hash(source, salt).toString();
	}

}
