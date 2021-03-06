package com.hk.core.authentication.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hk.commons.util.ByteConstants;
import com.hk.commons.util.CollectionUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * 当前登陆的用户，所有获取当前登陆用户信息必须继承此类
 *
 * @author kevin
 * @date 2017年9月28日上午9:45:55
 */
@Getter
@Setter
@NoArgsConstructor
public class UserPrincipal implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 当前用户id
     */

    private Long userId;

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
     * 获取 app 信息，对于 单点登陆，或 oauth2 登陆时有效
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ClientAppInfo appInfo;

    /**
     * 用户所在机构id
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long orgId;

    /**
     * 用户所在机构名称
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String orgName;

    /**
     * 用户所在部门Id
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long deptId;

    /**
     * 用户所在部门名称
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String deptName;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, String> thirdOpenId;

    /**
     * 用户角色
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<String> roles;

    /**
     * 用户权限
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<String> permissions;

    public UserPrincipal(Long userId, String account, Byte userType) {
        this.userId = userId;
        this.account = account;
        this.userType = userType;
    }

    public UserPrincipal(Long userId, String account, String realName,
                         Byte userType, String phone, String email,
                         Byte sex, String iconPath, Set<String> roles, Set<String> permissions) {
        this.userId = userId;
        this.account = account;
        this.realName = realName;
        this.userType = userType;
        this.phone = phone;
        this.email = email;
        this.sex = sex;
        this.iconPath = iconPath;
        this.roles = roles;
        this.permissions = permissions;

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
        return userType == ByteConstants.ONE;
    }

}
