package com.hk.core.authentication.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hk.commons.util.ByteConstants;
import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.StringUtils;
import com.hk.core.authentication.api.UserPrincipal;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 使用 spring security 实现的当前用户实体
 *
 * @author kevin
 * @date 2017年12月21日下午5:45:54
 */
@SuppressWarnings("serial")
@NoArgsConstructor
public class SecurityUserPrincipal extends UserPrincipal implements UserDetails, CredentialsContainer {

    /**
     * 默认的角色前缀
     */
    public static final String ROLE_PREFIX = "ROLE_";

    /**
     * 用户密码
     */
    @JsonIgnore
    private String password;

    /**
     * 用户状态
     */
    @JsonIgnore
    private Byte userStatus;

    public SecurityUserPrincipal(Long userId, Long orgId, String orgName, Long deptId, String deptName, String account,
                                 String realName, Byte userType, String phone,
                                 String email, Byte sex, String iconPath, String password, Byte userStatus, Set<String> roles, Set<String> permissions) {
        super(userId, account, realName, userType, phone, email, sex, iconPath, roles, permissions);
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
        return null != userStatus && ByteConstants.ONE == userStatus;
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
        return null != userStatus && ByteConstants.ONE == userStatus;
    }

    /**
     * 删除用户凭证信息，只有在登陆成功后才会调用
     *
     * @see org.springframework.security.authentication.ProviderManager#eraseCredentialsAfterAuthentication 是否删除凭证信息，默认为 true
     * @see org.springframework.security.authentication.ProviderManager#authenticate(Authentication) 第 218 行
     */
    @Override
    public void eraseCredentials() {
        password = null;
    }
}
