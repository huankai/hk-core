package com.hk.core.data.commons.query;

import java.io.Serializable;
import java.util.List;

/**
 * @author: kevin
 * @date 2018年1月24日上午9:56:21
 */
public interface QueryPage<T> extends Serializable {

    /**
     * 返回当前页
     *
     * @return
     */
    int getPageIndex();

    /**
     * 返回当前页记录数
     *
     * @return
     */
    int getPageSize();

    /**
     * 返回当前页数据
     *
     * @return
     */
    List<T> getData();

    /**
     * 返回总记录
     *
     * @return
     */
    long getTotalRow();

}
