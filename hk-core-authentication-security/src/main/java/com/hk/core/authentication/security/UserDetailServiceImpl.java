package com.hk.core.authentication.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 
 * @author huangkai
 *
 */
public abstract class UserDetailServiceImpl implements UserDetailsService {

	@Override
	public final UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return loadUserByLoginUsername(username);
	}

	protected abstract SecurityUserPrincipal loadUserByLoginUsername(String username);

}
