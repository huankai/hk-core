package com.hk.core.data.jdbc;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: kevin
 * @date: 2018-09-19 10:27
 */
public abstract class JdbcDaoSupport {

    @Autowired
    protected JdbcSession jdbcSession;
}
