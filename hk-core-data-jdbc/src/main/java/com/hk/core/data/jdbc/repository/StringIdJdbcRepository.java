package com.hk.core.data.jdbc.repository;

import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author: sjq-278
 * @date: 2018-10-10 10:16
 */
@NoRepositoryBean
public interface StringIdJdbcRepository<T extends Persistable<String>> extends JdbcRepository<T, String> {
}
