package com.hk.core.authentication.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hk.commons.util.ByteConstants;
import com.hk.commons.util.CollectionUtils;
import com.hk.core.authentication.api.UserPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author: kevin
 * @date 2017年12月21日下午5:45:54
 */
@SuppressWarnings("serial")
public class SecurityUserPrincipal extends UserPrincipal implements UserDetails {

    /**
     *
     */
    @JsonIgnore
    private final String passWord;

    @JsonIgnore
    private Byte userStatus;

    public SecurityUserPrincipal(Boolean isProtect, String userId, String userName, String passWord, String nickName, Byte userType,
                                 String phone, String email, Byte sex, String iconPath, Byte userStatus) {
        super(userId, userName, isProtect, nickName, userType, phone, email, sex, iconPath);
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
        List<GrantedAuthority> authorityList = new ArrayList<>();
        Collection<String> permissions = getPermissionByAppId(getAppCode().getAppId());
        if (CollectionUtils.isNotEmpty(permissions)) {
            permissions.forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission)));
        }
        return authorityList;
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
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return ByteConstants.ONE.equals(userStatus);
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return ByteConstants.ONE.equals(userStatus);
    }

}
