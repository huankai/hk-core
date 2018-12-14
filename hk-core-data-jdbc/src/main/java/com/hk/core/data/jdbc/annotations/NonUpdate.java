package com.hk.core.data.jdbc.annotations;

import java.lang.annotation.*;

/**
 * 执行 update 时不需要更新的字段,spring data jdbc 并没有在执行更新语句 的时候，并没有指定哪些字段不需要更新
 * 
 * @see com.hk.core.data.jdbc.core.SqlGenerator
 *
 * @author huangkai
 * @date 2018-10-27 13:25
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NonUpdate {


}
