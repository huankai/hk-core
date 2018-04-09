package com.hk.core.query;

import com.google.common.collect.Lists;

import java.util.List;

public class QueryModel {

	/**
	 * 查询分页参数
	 */
	private int pageIndex;

	/**
	 * 查询分页参数
	 */
	private int pageSize = 10;

	/**
	 * 分页时，不需要再count，所以用该属性来存上一次count的数字
	 */
	private int totalRowCount;

	/**
	 * 查询排序
	 */
	private List<Order> orders = Lists.newArrayList();

	/**
	 * @return the pageIndex
	 */
	public int getPageIndex() {
		return pageIndex;
	}

	/**
	 * @param pageIndex
	 *            the pageIndex to set
	 */
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the orders
	 */
	public List<Order> getOrders() {
		return orders;
	}

	/**
	 * @param orders
	 *            the orders to set
	 */
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	/**
	 * 分页时，不需要再count，所以用该属性来存上一次count的数字
	 * 
	 * @return the totalRowCount
	 */
	public int getTotalRowCount() {
		return totalRowCount;
	}

	public int getStartRowIndex() {
		int startRow = pageIndex;
		if (pageIndex < 1) {
			startRow = 1;
		}
		return (startRow - 1) * pageSize;
	}

	/**
	 * Jpa 查询分页从 0 开始
	 * 
	 * @return
	 */
	public int getJpaStartRowIndex() {
		int startRow = pageIndex - 1;
		if (startRow < 0) {
			startRow = 0;
		}
		return startRow;
	}

	/**
	 * 分页时，不需要再count，所以用该属性来存上一次count的数字
	 * 
	 * @param totalRowCount
	 *            the totalRowCount to set
	 */
	public void setTotalRowCount(int totalRowCount) {
		this.totalRowCount = totalRowCount;
	}

	public List<org.springframework.data.domain.Sort.Order> getSortOrderList() {
		List<org.springframework.data.domain.Sort.Order> orderList = Lists.newArrayList();
		orders.forEach(item -> orderList.add(item.toSpringJpaOrder()));
		return orderList;
	}

}
