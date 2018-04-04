package com.hk.core.query.jdbc;

import java.util.List;

/**
 * 查詢结果
 */
public class ListResult<T> {

	/**
	 * 查詢总记录数
	 */
	private long totalRowCount;

	/**
	 * 结果集
	 */
	private List<T> result;

	/**
	 * @param totalRowCount
	 * @param result
	 */
	public ListResult(long totalRowCount, List<T> result) {
		this.totalRowCount = totalRowCount;
		this.result = result;
	}

	/**
	 * @return the rowCount
	 */
	public long getTotalRowCount() {
		return totalRowCount;
	}

	/**
	 * @return the result
	 */
	public List<T> getResult() {
		return result;
	}

}
