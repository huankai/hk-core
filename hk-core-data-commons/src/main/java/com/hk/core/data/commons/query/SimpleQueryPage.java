package com.hk.core.data.commons.query;


import com.hk.core.data.commons.ListResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author: kevin
 * @date 2018年1月24日上午9:58:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleQueryPage<T> extends AbstractQueryPage<T> {

    /**
     * 当前页号
     */
    private int pageIndex;

    /**
     * 每页显示记录数
     */
    private int pageSize;

    /**
     * @param query
     * @param result
     */
    public SimpleQueryPage(QueryModel<?> query, ListResult<T> result) {
        this(query, result.getResult(), result.getTotalRowCount());
    }

    /**
     * @param query
     * @param totalRow
     * @param data
     */
    public SimpleQueryPage(QueryModel<?> query, List<T> data, long totalRow) {
        this(data, totalRow, query.getStartRowIndex(), query.getPageSize());
    }

    /**
     * @param data
     * @param totalRow
     * @param pageIndex
     * @param pageSize
     */
    public SimpleQueryPage(List<T> data, long totalRow, int pageIndex, int pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        setData(data);
        setTotalRow(totalRow);
    }


}
