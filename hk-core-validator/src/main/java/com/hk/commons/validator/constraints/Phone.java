package com.hk.commons.validator.constraints;

import com.hk.commons.validator.PhoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 手机号验证
 *
 * @author kevin
 * @date 2019-4-24 15:48
 */
@Documented
@Target({METHOD, FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = PhoneValidator.class)
public @interface Phone {

    /**
     * 是否不能为空
     */
    boolean notEmpty() default false;

    /**
     * 错误提示消息
     */
    String message() default "{com.hk.commons.validator.constraints.Phone.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
