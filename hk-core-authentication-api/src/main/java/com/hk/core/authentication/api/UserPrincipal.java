package com.hk.core.authentication.api;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * 当前登陆的用户
 *
 * @author: kevin
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
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户匿名
     */
    private String nickName;

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
    private String appId;

    /**
     * isProtect
     */
    private final boolean isProtect;

    /**
     * 用户角色
     * <pre>
     *     key :appId
     *     value:角色列表
     * </pre>
     */
    private Map<String, Collection<String>> appRoleSet;

    /**
     * 用户权限
     * * <pre>
     *     key :appId
     *     value:权限列表
     * </pre>
     */
    private Map<String, Collection<String>> appPermissionSet;

    public UserPrincipal(String userId, String userName, boolean isProtect, String nickName,
                         Byte userType, String phone, String email,
                         Byte sex, String iconPath) {
        this.userId = userId;
        this.userName = userName;
        this.isProtect = isProtect;
        this.nickName = nickName;
        this.userType = userType;
        this.phone = phone;
        this.email = email;
        this.sex = sex;
        this.iconPath = iconPath;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Byte getUserType() {
        return userType;
    }

    public void setUserType(Byte userType) {
        this.userType = userType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Map<String, Collection<String>> getAppRoleSet() {
        return appRoleSet;
    }

    public void setAppRoleSet(Map<String, Collection<String>> appRoleSet) {
        this.appRoleSet = appRoleSet;
    }

    public Map<String, Collection<String>> getAppPermissionSet() {
        return appPermissionSet;
    }

    public void setAppPermissionSet(Map<String, Collection<String>> appPermissionSet) {
        this.appPermissionSet = appPermissionSet;
    }

    public String getUserTypeChinease() {
        return null;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }


    public boolean isProtect() {
        return isProtect;
    }

    public Collection<String> getRoleByAppId(String appId) {
        return filterByAppId(appRoleSet, appId);
    }

    private Collection<String> filterByAppId(Map<String, Collection<String>> map, String appId) {
        Collection<String> result = null;
        if (map != null) {
            result = map.get(appId);
        }
        if (result == null) {
            result = Collections.emptyList();
        }
        return Collections.unmodifiableCollection(result);
    }

    public Collection<String> getPermissionByAppId(String appId) {
        return filterByAppId(appPermissionSet, appId);
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * 是否为管理员
     *
     * @return
     */
    public final boolean isAdministrator() {
        return isProtect;
    }

}
