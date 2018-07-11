package com.hk.core.query.jdbc;

import com.google.common.collect.Lists;
import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.StringUtils;
import com.hk.core.data.commons.query.AndOr;
import lombok.Getter;

import java.util.List;

/**
 * @author: kevin
 * @date 2017年12月22日下午1:13:35
 */
public class LogicalCondition implements Condition {

	@Getter
	private AndOr andOr;

	@Getter
	private List<SimpleCondition> conditions = Lists.newArrayList();

	/**
	 * @param conditions
	 */
	public LogicalCondition(List<SimpleCondition> conditions) {
		this(AndOr.AND, conditions);
	}

	/**
	 * @param conditions
	 */
	public LogicalCondition(SimpleCondition... conditions) {
		this.andOr = AndOr.AND;
		CollectionUtils.addAllNotNull(this.conditions, conditions);
	}

	/**
	 * @param andOr
	 * @param conditions
	 */
	public LogicalCondition(AndOr andOr, List<SimpleCondition> conditions) {
		this.andOr = andOr;
		this.conditions = conditions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hk.core.query.jdbc.Condition#toSqlString(java.util.List)
	 */
	@Override
	public String toSqlString(List<Object> parameters) {
		if (CollectionUtils.isEmpty(conditions)) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for (Condition condition : conditions) {
			if (null != condition) {
				String sql = condition.toSqlString(parameters);
				if (StringUtils.isNotEmpty(sql)) {
					if (index++ > 0) {
						sb.append(" ");
						sb.append(andOr.toSqlString());
						sb.append(" ");
					}
					sb.append(sql);
				}
			}
		}
		if (sb.indexOf(andOr.toSqlString()) != -1) {
			sb.insert(0, "(").append(")");
		}
		return sb.toString();
	}

}
