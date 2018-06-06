package com.hk.core.query;

import com.hk.core.ListResult;

import java.util.List;

/**
 * @author kally
 * @date 2018年1月24日上午9:58:21
 */
public final class SimpleQueryPage<T> extends AbstractQueryPage<T> {

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
     * @param totalRow
     * @param data
     */
    public SimpleQueryPage(QueryModel query, long totalRow, List<T> data) {
        this.pageIndex = query.getPageIndex();
        this.pageSize = query.getPageSize();
        setTotalRow(totalRow);
        setData(data);
    }

    public SimpleQueryPage(QueryModel query, ListResult<T> result) {
        this.pageIndex = query.getPageIndex();
        this.pageSize = query.getPageSize();
        setTotalRow(result.getTotalRowCount());
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
