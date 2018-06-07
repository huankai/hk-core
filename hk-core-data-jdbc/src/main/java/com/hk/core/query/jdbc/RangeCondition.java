package com.hk.core.query.jdbc;

import com.hk.core.data.commons.query.Operator;

import java.util.List;

/**
 * 
 * @author huangkai
 * @date 2017年12月20日下午3:23:10
 * @param <T>
 */
public class RangeCondition<T> implements Condition {

	private String field;

	private T start;

	private T end;

	private boolean includeStart;

	private boolean includeEnd;

	/**
	 * 
	 */
	public RangeCondition() {
	}

	/**
	 * @param field
	 * @param start
	 * @param end
	 * @param includeStart
	 * @param includeEnd
	 */
	public RangeCondition(String field, T start, T end, boolean includeStart, boolean includeEnd) {
		this.field = field;
		this.start = start;
		this.end = end;
		this.includeStart = includeStart;
		this.includeEnd = includeEnd;
	}

	@Override
	public String toSqlString(List<Object> parameters) {
		CompositeCondition compos = new CompositeCondition();
		if (null != start) {
			compos.addCondition(new SimpleCondition(field, includeStart ? Operator.GTE : Operator.GT, start));
		}
		if (null != end) {
			compos.addCondition(new SimpleCondition(field, includeEnd ? Operator.LTE : Operator.LT, end));
		}
		return compos.toSqlString(parameters);
	}

	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}

	/**
	 * @return the start
	 */
	public T getStart() {
		return start;
	}

	/**
	 * @return the end
	 */
	public T getEnd() {
		return end;
	}

	/**
	 * @return the includeStart
	 */
	public boolean isIncludeStart() {
		return includeStart;
	}

	/**
	 * @return the includeEnd
	 */
	public boolean isIncludeEnd() {
		return includeEnd;
	}

	/**
	 * @param field the field to set
	 */
	public void setField(String field) {
		this.field = field;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(T start) {
		this.start = start;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(T end) {
		this.end = end;
	}

	/**
	 * @param includeStart the includeStart to set
	 */
	public void setIncludeStart(boolean includeStart) {
		this.includeStart = includeStart;
	}

	/**
	 * @param includeEnd the includeEnd to set
	 */
	public void setIncludeEnd(boolean includeEnd) {
		this.includeEnd = includeEnd;
	}
	
	

}
