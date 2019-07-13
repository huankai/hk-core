package com.hk.commons.validator;

import com.hk.commons.util.StringUtils;
import com.hk.commons.util.ValidateUtils;
import com.hk.commons.validator.constraints.Phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author kevin
 * @date 2019-4-24 15:48
 */
public class PhoneValidator implements ConstraintValidator<Phone, CharSequence> {

    private boolean notEmpty;

    @Override
    public void initialize(Phone constraintAnnotation) {
        this.notEmpty = constraintAnnotation.notEmpty();
    }


    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        return StringUtils.isEmpty(value) ? !notEmpty : ValidateUtils.isMobilePhone(value);
    }
}
