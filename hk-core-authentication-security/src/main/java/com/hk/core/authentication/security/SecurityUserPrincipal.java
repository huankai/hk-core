package com.hk.core.authentication.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.google.common.collect.Lists;
import com.hk.commons.util.CollectionUtils;
import com.hk.core.authentication.api.UserPrincipal;

/**
 * 
 * @author huangkai
 * @date 2017年12月21日下午5:45:54
 */
@SuppressWarnings("serial")
public class SecurityUserPrincipal extends UserPrincipal implements UserDetails {

	/**
	 * 
	 */
	private String passWord;
	
	private Integer userStatus;

	public SecurityUserPrincipal() {
	}

	public SecurityUserPrincipal(String userId, String userName, String passWord, String nickName, Integer userType,
			String phone, String email, Integer sex,String iconPath,Integer userStatus) {
		super(userId, userName, nickName, userType, phone, email, sex,iconPath);
		this.passWord = passWord;
		this.userStatus = userStatus;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<String> roleNames = getRoles();
		if (CollectionUtils.isNotEmpty(roleNames)) {
			List<GrantedAuthority> authorities = Lists.newArrayListWithExpectedSize(roleNames.size());
			roleNames.forEach(roleName -> authorities.add(new SimpleGrantedAuthority(roleName)));
			return authorities;
		}
		return Collections.emptyList();
	}

	@Override
	public String getPassword() {
		return passWord;
	}

	@Override
	public String getUsername() {
		return getNickName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return null != userStatus && userStatus == 1;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
