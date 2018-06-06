package com.hk.core.query;

import java.util.List;

/**
 * @author kally
 * @date 2018年1月24日上午10:10:42
 */
abstract class AbstractQueryPage<T> implements QueryPage<T> {

    /**
     * 数据集
     */
    private List<T> data;

    /**
     * 总记录数
     */
    private long totalRow;

    @Override
    public List<T> getData() {
        return data;
    }

    @Override
    public long getTotalRow() {
        return totalRow;
    }

    /**
     * @param data the data to set
     */
    public void setData(List<T> data) {
        this.data = data;
    }

    /**
     * @param totalRow the totalRow to set
     */
    public void setTotalRow(long totalRow) {
        this.totalRow = totalRow;
    }

}
