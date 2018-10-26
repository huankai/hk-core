package com.hk.core.data.jdbc;

import com.hk.core.data.jdbc.query.CompositeCondition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 删除参数
 *
 * @author: kevin
 * @date: 2018-09-19 14:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteArguments {

    /**
     * 要删除的表名
     */
    private String from;

    /**
     * 删除的条件
     * where conditions
     */
    private CompositeCondition conditions = new CompositeCondition();
}
