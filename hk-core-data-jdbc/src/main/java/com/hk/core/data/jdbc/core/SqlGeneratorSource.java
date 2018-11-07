package com.hk.core.data.jdbc.core;

import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: kevin
 * @date: 2018-10-26 13:27
 * @see org.springframework.data.jdbc.core.SqlGeneratorSource
 */
@RequiredArgsConstructor
public class SqlGeneratorSource {

    private final Map<Class<?>, SqlGenerator> sqlGeneratorCache = new HashMap<>();

    private final RelationalMappingContext context;

    SqlGenerator getSqlGenerator(Class<?> domainType) {

        return sqlGeneratorCache.computeIfAbsent(domainType,
                t -> new SqlGenerator(context, context.getRequiredPersistentEntity(t), this));

    }
}
