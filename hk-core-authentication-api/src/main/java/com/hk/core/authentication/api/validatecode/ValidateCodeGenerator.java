package com.hk.core.authentication.api.validatecode;

/**
 * 验证码生成器
 *
 * @author kevin
 * @date 2018-07-27 13:47
 */
public interface ValidateCodeGenerator<C extends ValidateCode> {

    C generate();
}
