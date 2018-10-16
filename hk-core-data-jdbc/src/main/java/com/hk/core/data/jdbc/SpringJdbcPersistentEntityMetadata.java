package com.hk.core.data.jdbc;

import com.hk.commons.util.StringUtils;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author huangkai
 * @date 2018-10-13 11:00
 */
public class SpringJdbcPersistentEntityMetadata extends AbstractPersistentEntityMetadata {

    @Override
    protected String getTableName(PersistentEntity<?, ? extends PersistentProperty> persistentEntity) {
        Table table = persistentEntity.findAnnotation(Table.class);
        return null != table ? table.value() : StringUtils.substringAfterLast(persistentEntity.getName(), ".");
    }

    @Override
    protected String getColumnName(PersistentProperty<?> persistentProperty) {
        return persistentProperty.getRequiredAnnotation(Column.class).value();
    }

    @Override
    protected Iterable<? extends PersistentProperty> getPersistentProperties(PersistentEntity<?, ? extends PersistentProperty> persistentEntity) {
        return persistentEntity.getPersistentProperties(Column.class);
    }
}
