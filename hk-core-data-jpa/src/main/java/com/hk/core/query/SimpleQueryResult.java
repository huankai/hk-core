package com.hk.core.query;

import com.hk.core.query.jdbc.ListResult;

import java.util.List;

/**
 * @author kally
 * @date 2018年1月24日上午9:58:21
 */
public final class SimpleQueryResult<T> extends AbstractQueryResult<T> {

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
     * @param totalRowCount
     * @param data
     */
    public SimpleQueryResult(QueryModel query, long totalRowCount, List<T> data) {
        this.pageIndex = query.getPageIndex();
        this.pageSize = query.getPageSize();
        setTotalRowCount(totalRowCount);
        setData(data);
    }

    public SimpleQueryResult(QueryModel query, ListResult<T> result) {
        this.pageIndex = query.getPageIndex();
        this.pageSize = query.getPageSize();
        setTotalRowCount(result.getTotalRowCount());
        setData(result.getResult());
    }

    @Override
    public int getPageIndex() {
        return pageIndex;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @param pageIndex the pageIndex to set
     */
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
