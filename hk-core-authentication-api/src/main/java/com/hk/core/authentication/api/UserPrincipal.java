package com.hk.core.authentication.api;

import java.io.Serializable;
import java.util.Set;

/**
 * 当前登陆的用户
 * 
 * @author huangkai
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
	private Integer userType;

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
	private Integer sex;
	
	/**
	 * 用户头像
	 */
	private String iconPath;

	/**
	 * 用户权限
	 */
	private Set<String> permissions;
	
	public UserPrincipal() {
		
	}

	public UserPrincipal(String userId, String userName, String nickName, Integer userType, String phone, String email,
			Integer sex,String iconPath) {
		this.userId = userId;
		this.userName = userName;
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

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
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

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Set<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
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

	/**
	 * 是否为管理员
	 * 
	 * @return
	 */
	public boolean isAdministrator() {
		return null != userType && userType.intValue() == 0;
	}

}
