package com.hk.core.data.jpa.query;

import com.hk.core.data.commons.query.QueryModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Persistable;

/**
 * @author: huangkai
 * @date 2018-06-07 16:10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class JpaQueryModel<T extends Persistable> extends QueryModel {

    private T param;
}
