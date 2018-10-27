package com.hk.core.data.jdbc.annotations;

import java.lang.annotation.*;

/**
 * 执行 update 时不需要更新的字段
 *
 * @author huangkai
 * @date 2018-10-27 13:25
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NonUpdate {


}
