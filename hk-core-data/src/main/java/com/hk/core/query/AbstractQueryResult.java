package com.hk.core.query;

import java.util.List;

/**
 * @author kally
 * @date 2018年1月24日上午10:10:42
 */
abstract class AbstractQueryResult<T> implements QueryPageable<T> {

	/**
	 * 数据集
	 */
	private List<T> data;

	/**
	 * 总记录数
	 */
	private long totalRowCount;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hk.core.query.QueryPageable#getData()
	 */
	@Override
	public List<T> getData() {
		return data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hk.core.query.QueryPageable#getTotalRowCount()
	 */
	@Override
	public long getTotalRowCount() {
		return totalRowCount;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<T> data) {
		this.data = data;
	}

	/**
	 * @param totalRowCount
	 *            the totalRowCount to set
	 */
	public void setTotalRowCount(long totalRowCount) {
		this.totalRowCount = totalRowCount;
	}

}
