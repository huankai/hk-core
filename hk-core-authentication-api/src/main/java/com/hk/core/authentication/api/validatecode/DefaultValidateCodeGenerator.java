package com.hk.core.authentication.api.validatecode;

import com.hk.commons.util.AssertUtils;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author: kevin
 * @date: 2018-07-27 13:48
 */
public class DefaultValidateCodeGenerator implements ValidateCodeGenerator<ValidateCode> {

    /**
     * 生成长度
     */
    private byte codeLength;

    /**
     * 过期时间
     */
    private int expireIn;

    public DefaultValidateCodeGenerator(byte codeLength, int expireIn) {
        AssertUtils.isTrue(codeLength > 0, "生成的验证码长度必须大于0");
        this.codeLength = codeLength;
        this.expireIn = expireIn;
    }

    @Override
    public ValidateCode generate() {
        String code = RandomStringUtils.randomNumeric(codeLength);
        return new ValidateCode(code, expireIn);
    }
}
