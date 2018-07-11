package com.hk.core.query.jdbc;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hk.commons.util.ArrayUtils;
import com.hk.core.data.commons.query.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

/**
 * 查询参数
 * 
 * @author: kevin
 * @date 2017年12月22日下午12:30:52
 */
public final class SelectArguments {

	/**
	 * 分页参数
	 */
	@Getter
	@Setter
	private int pageIndex;

	/**
	 * 分页参数
	 */
	@Getter
	@Setter
	private int pageSize;

	/**
	 * 去重查询
	 */
	@Getter
	@Setter
	private boolean distinct;

	/**
	 * 查询字段
	 */
	@Getter
	@Setter
	private Set<String> fieldSet;

	/**
	 * 查询表名
	 */
	@Getter
	@Setter
	private String from;

	@Getter
	private CompositeCondition conditions = new CompositeCondition();

	/**
	 * Group by
	 */
	@Getter
	@Setter
	private Set<String> groupBySet;

	/**
	 * 查询排序
	 */
	@Getter
	@Setter
	private List<Order> orders = Lists.newArrayList();

	public int getStartRowIndex() {
		if (pageIndex <= 0) {
			pageIndex = 1;
		}
		return (pageIndex - 1) * pageSize;
	}
	
	public void setFields(String... fields) {
		if(ArrayUtils.isNotEmpty(fields)) {
			this.fieldSet = Sets.newHashSet(fields);
		}
	}

	/**
	 * @param groupBys
	 *            the groupBys to set
	 */
	public void setGroupBys(String... groupBys) {
		if (ArrayUtils.isNotEmpty(groupBys)) {
			this.groupBySet = Sets.newHashSet(groupBys);
		}
	}

}
