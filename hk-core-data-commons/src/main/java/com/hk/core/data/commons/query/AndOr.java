package com.hk.core.data.commons.query;


/**
 * And Or
 *
 * @author kevin
 */
public enum AndOr {

    AND, OR;

    public String toSqlString() {
        return toString();
    }

}
