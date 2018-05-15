package com.hk.core.authentication.security;

import com.google.common.collect.Lists;
import com.hk.commons.util.ByteConstants;
import com.hk.commons.util.CollectionUtils;
import com.hk.core.authentication.api.PermissionContants;
import com.hk.core.authentication.api.UserPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @author huangkai
 * @date 2017年12月21日下午5:45:54
 */
@SuppressWarnings("serial")
public class SecurityUserPrincipal extends UserPrincipal implements UserDetails, PermissionContants {

    /**
     *
     */
    private String passWord;

    private Byte userStatus;

    private final boolean isProtect;

    public SecurityUserPrincipal(Boolean isProtect, String userId, String userName, String passWord, String nickName, Byte userType,
                                 String phone, String email, Byte sex, String iconPath, Byte userStatus) {
        super(userId, userName, nickName, userType, phone, email, sex, iconPath);
        this.isProtect = isProtect;
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
        if (isProtect) {
            authoritieList.add(new SimpleGrantedAuthority(PROTECT_ADMIN_PERMISSION));
            return authoritieList;
        }
        Collection<String> permissions = getPermissionByAppId(getAppId());
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
        return isProtect && ByteConstants.ONE.equals(userStatus);
    }

}
