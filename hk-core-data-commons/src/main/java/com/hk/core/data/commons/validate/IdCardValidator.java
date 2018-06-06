package com.hk.core.data.commons.validate;

import com.hk.commons.util.StringUtils;
import com.hk.commons.util.ValidateUtils;
import com.hk.core.data.commons.validate.annotation.IdCard;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 身份证验证类
 * @author kally
 * @date 2018年3月6日下午4:30:52
 */
public class IdCardValidator implements ConstraintValidator<IdCard, CharSequence> {
	
	private boolean nullable;

	@Override
	public void initialize(IdCard constraintAnnotation) {
		this.nullable = constraintAnnotation.nullable();
	}

	@Override
	public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
		return StringUtils.isEmpty(value) ? nullable : ValidateUtils.isIDCard(value);
	}
	
}
