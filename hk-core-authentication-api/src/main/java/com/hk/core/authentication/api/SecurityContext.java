package com.hk.core.authentication.api;

import com.hk.core.web.Webs;

import java.util.Optional;

/**
 * 此接口可获取当前登陆用户信息
 *
 * @author kevin
 * @date 2017年10月23日下午12:50:55
 */
public interface SecurityContext {

    /**
     * 获取当前登陆的用户
     *
     * @return {@link UserPrincipal}
     */
    UserPrincipal getPrincipal();

    /**
     * 当前用户是否有认证
     *
     * @return 认证返回true, 否则返回false
     */
    boolean isAuthenticated();

    /**
     * 从 session 中获取属性
     *
     * @param key   session Key
     * @param clazz 值类型
     * @param <T>   泛型类型
     * @return session Value
     */
    default <T> Optional<T> getSessionAttribute(String key, Class<T> clazz) throws ClassCastException {
        return Optional.ofNullable(Webs.getAttributeFromSession(key, clazz));
    }

    /**
     * 设置 session 属性值，如果 session 不存在，不创建session
     *
     * @param key   session Key
     * @param value session Value
     */
    default void setSessionAttribute(String key, Object value) {
        setSessionAttribute(key, value, false);
    }

    /**
     * 设置session属性值
     *
     * @param key    session Key
     * @param value  session Value
     * @param create 如果session　不存在，是否创建session
     */
    default void setSessionAttribute(String key, Object value, boolean create) {
        Webs.setAttributeFromSession(key, value, create);
    }

    /**
     * 从 session中删除值
     *
     * @param key session Key
     */
    default void removeSessionAttribute(String key) {
        Webs.removeAttributeFromSession(key);
    }

}
