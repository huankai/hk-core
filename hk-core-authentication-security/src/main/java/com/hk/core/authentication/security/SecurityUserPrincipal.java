package com.hk.core.authentication.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hk.commons.util.ByteConstants;
import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.StringUtils;
import com.hk.core.authentication.api.UserPrincipal;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author kevin
 * @date 2017年12月21日下午5:45:54
 */
@SuppressWarnings("serial")
public class SecurityUserPrincipal extends UserPrincipal implements UserDetails, CredentialsContainer {

    public static final String ROLE_PREFIX = "ROLE_";

    /**
     *
     */
    @JsonIgnore
    private String password;

    @JsonIgnore
    private final Byte userStatus;

    public SecurityUserPrincipal(String userId, String orgId, String orgName, String deptId, String deptName, String account, boolean protectUser,
                                 String realName, Byte userType, String phone,
                                 String email, Byte sex, String iconPath, String password, Byte userStatus, Set<String> roles, Set<String> permissions) {
        super(userId, account, protectUser, realName, userType, phone, email, sex, iconPath, roles, permissions);
        setOrgId(orgId);
        setOrgName(orgName);
        setDeptId(deptId);
        setDeptName(deptName);
        this.userStatus = userStatus;
        this.password = password;
    }


    /**
     * 获取用户权限
     *
     * @return GrantedAuthorityList
     */
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        Set<String> roleSet = getRoles();
        if (CollectionUtils.isNotEmpty(roleSet)) {
            roleSet.forEach(role -> {
                if (!StringUtils.startsWith(role, ROLE_PREFIX)) {
                    role = ROLE_PREFIX + role;
                }
                authorityList.add(new SimpleGrantedAuthority(role));

            });
        }
        Set<String> permissionSet = getPermissions();
        if (CollectionUtils.isNotEmpty(permissionSet)) {
            permissionSet.forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission)));
        }
        return authorityList;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return getAccount();
    }

    /**
     * 用户不过期
     *
     * @return true
     */
    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账户是否锁定
     *
     * @return true if userStatus is One.
     */
    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return ByteConstants.TWO.equals(userStatus);
    }

    /**
     * 密码是否不过期
     *
     * @return true
     */
    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账户是否可用
     *
     * @return true if userStatus is One.
     */
    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return ByteConstants.TWO.equals(userStatus);
    }

    @Override
    public void eraseCredentials() {
        password = null;
    }
}
