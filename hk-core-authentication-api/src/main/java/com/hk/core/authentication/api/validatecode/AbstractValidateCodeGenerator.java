package com.hk.core.authentication.api.validatecode;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author kevin
 * @date 2019-8-8 14:41
 */
public abstract class AbstractValidateCodeGenerator {

    /**
     * 生成验证码默认长度
     */
    private static final int DEFAULT_CODE_LENGTH = 6;

    /**
     * 生成验证码默认过期时间：单位: 秒
     */
    private static final int DEFAULT_EXPIRE_SENDS = 60;

    @Getter
    @Setter
    private int codeLength;

    @Getter
    @Setter
    private int expireSends;

    public AbstractValidateCodeGenerator() {
        this(DEFAULT_CODE_LENGTH, DEFAULT_EXPIRE_SENDS);
    }

    public AbstractValidateCodeGenerator(int codeLength, int expireSends) {
        this.codeLength = codeLength >= 0 ? codeLength : DEFAULT_CODE_LENGTH;
        this.expireSends = expireSends >= 0 ? expireSends : DEFAULT_EXPIRE_SENDS;
    }

    protected String randomStringGenerator() {
        return RandomStringUtils.randomNumeric(codeLength);
    }


}
