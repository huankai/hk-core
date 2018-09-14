package com.hk.core.authentication.api.validatecode;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 验证码处理器
 *
 * @author: kevin
 * @date: 2018-07-27 13:37
 */
public interface ValidateCodeProcessor {

    /**
     * 生成验证码前缀
     */
    String VALIDATE_CODE_PREFIX = "VALIDATE_CODE_PREFIX_";

    /**
     * 创建验证码
     *
     * @param request request
     */
    void create(ServletWebRequest request) throws Exception;

    /**
     * 校验验证码
     *
     * @param request request
     * @throws ValidateCodeException 验证码验证异常
     */
    void validate(ServletWebRequest request) throws ValidateCodeException;
}
