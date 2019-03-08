package com.hk.core.authentication.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.CollectionUtils;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 当前登陆的用户，所有获取当前登陆用户信息必须继承此类
 *
 * @author kevin
 * @date 2017年9月28日上午9:45:55
 */
@NoArgsConstructor
public class UserPrincipal implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 899893192177859358L;

    /**
     * 当前用户id
     */
    @Getter
    private String userId;

    /**
     * 用户账号
     */
    @Getter
    private String account;

    /**
     * 真实名称
     */
    @Getter
    @Setter
    private String realName;

    /**
     * 用户类型
     */
    @Getter
    private Byte userType;

    /**
     * 用户手机号
     */
    @Getter
    @Setter
    private String phone;

    /**
     * 用户邮箱
     */
    @Getter
    @Setter
    private String email;

    /**
     * 用户性别
     */
    @Getter
    @Setter
    private Byte sex;

    /**
     * 用户头像
     */
    @Getter
    @Setter
    private String iconPath;

    /**
     * 获取 app 信息，对于 单点登陆，或 oauth2 登陆时有效
     */
    @Getter
    @Setter
    private ClientAppInfo appInfo;

    /**
     * 是否受保护的用户
     * protectUser
     */
    @Getter
    @JsonIgnore
    private boolean protectUser;

    /**
     * 用户所在机构id
     */
    @Getter
    @Setter
    private String orgId;

    /**
     * 用户所在机构名称
     */
    @Getter
    @Setter
    private String orgName;

    /**
     * 用户所在部门Id
     */
    @Getter
    @Setter
    private String deptId;

    /**
     * 用户所在部门名称
     */
    @Getter
    @Setter
    private String deptName;

    /**
     * 用户角色
     */
    @JsonIgnore
    private Set<String> roles;

    /**
     * 用户权限
     */
    @JsonIgnore
    private Set<String> permissions;

    public UserPrincipal(String userId, String account, Byte userType) {
        this.protectUser = false;
        this.userId = userId;
        this.account = account;
        this.userType = userType;
        this.roles = new HashSet<>();
        this.permissions = new HashSet<>();
    }

    public UserPrincipal(String userId, String account, boolean protectUser, String realName,
                         Byte userType, String phone, String email,
                         Byte sex, String iconPath, Set<String> roles, Set<String> permissions) {
        this.userId = userId;
        this.account = account;
        this.protectUser = protectUser;
        this.realName = realName;
        this.userType = userType;
        this.phone = phone;
        this.email = email;
        this.sex = sex;
        this.iconPath = iconPath;
        this.roles = (roles == null) ? new HashSet<>() : roles;
        this.permissions = (permissions == null) ? new HashSet<>() : permissions;
    }

    /**
     * 获取用户所有权限
     */
    public Set<String> getPermissions() {
        return Collections.unmodifiableSet(permissions);
    }

    /**
     * 获取用户所有角色
     */
    public Set<String> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    /**
     * 判断用户是否有权限
     *
     * @param permissionName 权限名
     */
    public final boolean hasPermission(String permissionName) {
        return isAdministrator() || CollectionUtils.contains(permissions, permissionName);
    }

    /**
     * 判断用户是否有角色
     *
     * @param roleName 角色名
     */
    public final boolean hasRole(String roleName) {
        return isAdministrator() || CollectionUtils.contains(permissions, roleName);
    }

    /**
     * 是否为管理员
     *
     * @return true if is an admin.
     */
    @JsonIgnore
    public final boolean isAdministrator() {
        return protectUser;
    }

}
