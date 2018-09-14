package com.hk.core.page;

import lombok.Data;

import java.util.List;

/**
 * @author: kevin
 * @date: 2018年1月24日上午10:10:42
 */
@Data
@SuppressWarnings("serial")
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
