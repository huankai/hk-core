package com.hk.core.authentication.security;

import com.google.common.collect.Lists;
import com.hk.commons.util.CollectionUtils;
import com.hk.core.authentication.api.UserPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
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
                                 String phone, String email, Integer sex, String iconPath, Integer userStatus) {
        super(userId, userName, nickName, userType, phone, email, sex, iconPath);
        this.passWord = passWord;
        this.userStatus = userStatus;
    }

    /**
     * 获取用户权限
     *
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authoritieList = Lists.newArrayList();
        Set<String> permissions = getPermissions();
        if (CollectionUtils.isNotEmpty(permissions)) {
            permissions.forEach(permission -> authoritieList.add(new SimpleGrantedAuthority(permission)));
        }
        return authoritieList;
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
