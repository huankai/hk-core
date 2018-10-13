package com.hk.core.data.jdbc;

import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author huangkai
 * @date 2018-10-13 0:52
 */
public class JpaPersistenEntityMetadata extends AbstracePersistenEntityMetadata {
    @Override
    protected String getTableName(PersistentEntity<?, ? extends PersistentProperty> persistentEntity) {
        return persistentEntity.getRequiredAnnotation(Table.class).name();
    }

    @Override
    protected String getColumnName(PersistentProperty<?> persistentProperty) {
        return persistentProperty.getRequiredAnnotation(Column.class).name();
    }

    @Override
    protected Iterable<? extends PersistentProperty> getPersistentProperties(PersistentEntity<?, ? extends PersistentProperty> persistentEntity) {
        return persistentEntity.getPersistentProperties(Column.class);
    }
}
