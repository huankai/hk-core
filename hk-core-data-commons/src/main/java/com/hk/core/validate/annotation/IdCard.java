package com.hk.core.validate.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.hk.core.validate.IdCardValidator;

/**
 * JSR-303 身份证验证
 *
 * @author: huangkai
 * @date 2018-01-26 16:22
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = IdCardValidator.class)
public @interface IdCard {
	
	/**
	 * 是否允许为空
	 * @return
	 */
	boolean nullable() default false;

	/**
	 * 错误描述信息
	 * @return
	 */
    String message() default "身份证格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
