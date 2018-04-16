package com.hk.core.authentication.security.encrypt;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hk.commons.util.encrypt.Encrypt;

/**
 * 
 * @author huangkai
 * @date 2017年12月19日下午1:12:14
 */
@Deprecated
public class SecurityPasswordEncrypt implements Encrypt, PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		return new BCryptPasswordEncoder().encode(rawPassword);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
	}

	@Override
	public String asSha512HashToBase64(Object source, Object salt, int hashIterations) {
		return null;
	}

	@Override
	public String asMD5ToString(Object source, Object salt, int hashIterations) {
		return null;
	}

}
