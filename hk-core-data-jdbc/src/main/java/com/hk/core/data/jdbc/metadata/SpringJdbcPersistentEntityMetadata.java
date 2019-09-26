package com.hk.core.data.jdbc.metadata;

import com.hk.commons.util.StringUtils;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 使用 spring data 注解
 *
 * @author huangkai
 * @date 2018-10-13 11:00
 */
@Deprecated
public class SpringJdbcPersistentEntityMetadata extends AbstractPersistentEntityMetadata {

    @Override
    protected String getTableName(PersistentEntity<?, ? extends PersistentProperty<?>> persistentEntity) {
        Table table = persistentEntity.findAnnotation(Table.class);
        return null != table ? table.value() : StringUtils.substringAfterLast(persistentEntity.getName(), ".").toLowerCase();
    }

    @Override
    protected String getColumnName(PersistentProperty<?> persistentProperty) {
        return persistentProperty.getRequiredAnnotation(Column.class).value();
    }

    @Override
    protected Iterable<? extends PersistentProperty<?>> getPersistentProperties(PersistentEntity<?, ? extends PersistentProperty<?>> persistentEntity) {
        return persistentEntity.getPersistentProperties(Column.class);
    }
}
