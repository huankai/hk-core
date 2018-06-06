package com.hk.core.data.commons.validate.annotation;

import com.hk.core.validate.IdCardValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

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
     *
     * @return
     */
    boolean nullable() default false;

    /**
     * 错误描述信息
     *
     * @return
     */
    String message() default "身份证格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
