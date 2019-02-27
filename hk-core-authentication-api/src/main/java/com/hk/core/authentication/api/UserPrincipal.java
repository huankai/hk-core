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
 * 当前登陆的用户
 *
 * @author kevin
 * @date 2017年9月28日上午9:45:55
 */
public class UserPrincipal implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 899893192177859358L;

    /**
     * 当前用户id
     */
    @Getter
    private final String userId;

    /**
     * 用户账号
     */
    @Getter
    private final String account;

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
    private final Byte userType;

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
     * appId
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
    private final boolean protectUser;

    /**
     * orgId
     */
    @Getter
    @Setter
    private String orgId;

    /**
     * orgName
     */
    @Getter
    @Setter
    private String orgName;

    /**
     * deptId
     */
    @Getter
    @Setter
    private String deptId;

    /**
     * deptNames
     */
    @Getter
    @Setter
    private String deptName;

    /**
     * 用户角色
     */
    @JsonIgnore
    private final Set<String> roles;

    /**
     * 用户权限
     */
    @JsonIgnore
    private final Set<String> permissions;

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

    public Set<String> getPermissions() {
        return Collections.unmodifiableSet(permissions);
    }

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
