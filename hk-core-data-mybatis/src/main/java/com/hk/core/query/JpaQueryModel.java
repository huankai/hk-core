package com.hk.core.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 使用JPA查询
 *
 * @author huangkai
 * @date 2017年12月23日下午3:35:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class JpaQueryModel<T> extends QueryModel {

    private T params;
}
