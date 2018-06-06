package com.hk.core.data.commons.query;

public enum AndOr {

	AND, OR;

	public String toSqlString() {
		return toString();
	}

}
