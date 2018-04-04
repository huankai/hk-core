/**
 * 
 */
package com.hk.core.query.jdbc;

import java.util.List;

import com.google.common.collect.Lists;
import com.hk.commons.util.CollectionUtils;
import com.hk.commons.util.StringUtils;
import com.hk.core.query.AndOr;

/**
 * @author huangkai
 * @date 2017年12月21日上午8:51:51
 */
public class CompositeCondition implements Condition {

	private AndOr andOr;

	private List<Condition> conditions = Lists.newArrayList();

	/**
	 *
	 */
	public CompositeCondition() {
		this.andOr = AndOr.AND;
	}

	/**
	 * @param andOr
	 * @param conditions
	 */
	public CompositeCondition(AndOr andOr, List<Condition> conditions) {
		this.andOr = andOr;
		this.conditions = conditions;
	}

	/**
	 * @return the andOr
	 */
	public AndOr getAndOr() {
		return andOr;
	}

	/**
	 * @param andOr
	 *            the andOr to set
	 */
	public void setAndOr(AndOr andOr) {
		this.andOr = andOr;
	}

	/**
	 * @return the conditions
	 */
	public List<Condition> getConditions() {
		return conditions;
	}

	/**
	 * @param conditions
	 *            the conditions to set
	 */
	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}

	/**
	 * 
	 * @param condition
	 */
	public void addCondition(Condition condition) {
		conditions.add(condition);
	}

	/**
	 * 
	 * @param conditions
	 */
	public void addConditions(Condition... conditions) {
		CollectionUtils.addAll(this.conditions, conditions);
	}

	/**
	 * 
	 * @param condition
	 */
	public void removeCondition(Condition condition) {
		conditions.remove(condition);
	}

	/**
	 * 
	 * @param index
	 */
	public void removeCondition(int index) {
		conditions.remove(index);
	}

	/**
	 * 
	 */
	public void clearConditions() {
		conditions.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hk.core.query.jdbc.Condition#toSqlString(java.util.List)
	 */
	@Override
	public String toSqlString(List<Object> parameters) {
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for (Condition c : conditions) {
			if (null != c) {
				String sql = c.toSqlString(parameters);
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
		return sb.length() > 0 ? sb.toString() : null;
	}

}
