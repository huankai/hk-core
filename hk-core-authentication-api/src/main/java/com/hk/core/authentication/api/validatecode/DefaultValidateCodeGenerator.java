package com.hk.core.authentication.api.validatecode;

/**
 * @author kevin
 * @date 2018-07-27 13:48
 */
public class DefaultValidateCodeGenerator extends AbstractValidateCodeGenerator implements ValidateCodeGenerator<ValidateCode> {

    @Override
    public ValidateCode generate() {
        return new ValidateCode(randomStringGenerator(), getExpireSends());
    }
}
