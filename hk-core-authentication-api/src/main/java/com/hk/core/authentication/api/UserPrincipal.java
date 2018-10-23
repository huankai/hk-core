package com.hk.core.authentication.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 当前登陆的用户
 *
 * @author: kevin
 * @date: 2017年9月28日上午9:45:55
 */
@Data
public class UserPrincipal implements Serializable {

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

    private String sexChinese;

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

    public UserPrincipal() {

    }

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
     * 是否为管理员
     *
     * @return true if is an admin.
     */
    @JsonIgnore
    public final boolean isAdministrator() {
        return protectUser;
    }

}
