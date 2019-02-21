package com.hk.core.authentication.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hk.commons.util.CollectionUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

/**
 * 当前登陆的用户
 *
 * @author kevin
 * @date 2017年9月28日上午9:45:55
 */
@Data
@NoArgsConstructor
public final class UserPrincipal implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 899893192177859358L;

    /**
     * 当前用户id
     */
    private String userId;

    /**
     * 用户账号
     */
    private String account;

    /**
     * 真实名称
     */
    private String realName;

    /**
     * 用户类型
     */
    private Byte userType;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户性别
     */
    private Byte sex;

    /**
     * 用户头像
     */
    private String iconPath;

    /**
     * appId
     */
    private ClientAppInfo appInfo;

    /**
     * 是否受保护的用户
     * protectUser
     */
    @JsonIgnore
    private boolean protectUser;

    /**
     * orgId
     */
    private String orgId;

    /**
     * orgName
     */
    private String orgName;

    /**
     * deptId
     */
    private String deptId;

    /**
     * deptNames
     */
    private String deptName;

    /**
     * 用户角色
     */
    @JsonIgnore
    private Set<String> roleSet;

    /**
     * 用户权限
     */
    @JsonIgnore
    private Set<String> permissionSet;

    public UserPrincipal(String userId, String account, boolean protectUser, String realName,
                         Byte userType, String phone, String email,
                         Byte sex, String iconPath) {
        this.userId = userId;
        this.account = account;
        this.protectUser = protectUser;
        this.realName = realName;
        this.userType = userType;
        this.phone = phone;
        this.email = email;
        this.sex = sex;
        this.iconPath = iconPath;
    }

    /**
     * 判断用户是否有权限
     *
     * @param permissionName 权限名
     */
    public boolean hasPermission(String permissionName) {
        return isAdministrator() || CollectionUtils.contains(permissionSet, permissionName);
    }

    /**
     * 判断用户是否有角色
     *
     * @param roleName 角色名
     */
    public boolean hasRole(String roleName) {
        return isAdministrator() || CollectionUtils.contains(roleSet, roleName);
    }

    /**
     * 是否为管理员
     *
     * @return true if is an admin.
     */
    @JsonIgnore
    public boolean isAdministrator() {
        return protectUser;
    }

}
