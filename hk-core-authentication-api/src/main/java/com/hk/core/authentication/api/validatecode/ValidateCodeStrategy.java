package com.hk.core.authentication.api.validatecode;

import org.springframework.web.context.request.RequestAttributes;

/**
 * 验证码存储器
 *
 * @author kevin
 * @date 2018-07-27 14:06
 */
public interface ValidateCodeStrategy {

    /**
     * 验证码存储
     *
     * @param request request
     * @param name    name
     * @param value   value
     */
    void save(RequestAttributes request, String name, Object value);

    /**
     * 获取验证码
     *
     * @param request request
     * @param name    name
     * @return value
     */
    Object get(RequestAttributes request, String name);

    /**
     * 删除验证码
     *
     * @param request request
     * @param name    name
     */
    void remove(RequestAttributes request, String name);
}
