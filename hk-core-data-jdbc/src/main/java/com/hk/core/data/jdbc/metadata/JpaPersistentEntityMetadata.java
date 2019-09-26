package com.hk.core.data.jdbc.metadata;

import com.hk.commons.util.StringUtils;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 使用 JPA 注解
 *
 * @author huangkai
 * @date 2018-10-13 0:52
 */
@Deprecated
public class JpaPersistentEntityMetadata extends AbstractPersistentEntityMetadata {

    @Override
    protected String getTableName(PersistentEntity<?, ? extends PersistentProperty<?>> persistentEntity) {
        Table table = persistentEntity.findAnnotation(Table.class);
        if (table != null) {
            return table.name();
        }
        Entity entity = persistentEntity.findAnnotation(Entity.class);
        if (null != entity) {
            return entity.name();
        }
        return StringUtils.substringAfterLast(persistentEntity.getName(), ".");
    }

    @Override
    protected String getColumnName(PersistentProperty<?> persistentProperty) {
        return persistentProperty.getRequiredAnnotation(Column.class).name();
    }

    @Override
    protected Iterable<? extends PersistentProperty<?>> getPersistentProperties(PersistentEntity<?, ? extends PersistentProperty<?>> persistentEntity) {
        return persistentEntity.getPersistentProperties(Column.class);
    }

}
