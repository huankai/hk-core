package com.hk.core.data.commons.query;

import lombok.Data;

import java.util.List;

/**
 * @author: kevin
 * @date 2018年1月24日上午10:10:42
 */
@Data
abstract class AbstractQueryPage<T> implements QueryPage<T> {

    /**
     * 数据集
     */
    private List<T> data;

    /**
     * 总记录数
     */
    private long totalRow;


}
