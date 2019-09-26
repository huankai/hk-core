package com.hk.core.data.jdbc.metadata;

import com.hk.commons.util.AuditField;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.data.domain.Auditable;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author huangkai
 * @date 2018-10-13 11:03
 */
@Deprecated
public abstract class AbstractPersistentEntityMetadata implements PersistentEntityMetadata, AuditField {

    private Cache cache = new ConcurrentMapCache("PERSISTENT_ENTITY_METADATA", false);

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    @Override
    public PersistentEntityInfo getPersistentEntityInfo(PersistentEntity<?, ? extends PersistentProperty<?>> persistentEntity) {
        PersistentEntityInfo entityInfo = cache.get(persistentEntity.getType().getName(), PersistentEntityInfo.class);
        if (null == entityInfo) {
            entityInfo = new PersistentEntityInfo();
            PersistentProperty<?> idProperty = persistentEntity.getRequiredIdProperty();
            entityInfo.setIdField(idProperty.getName());
            Iterable<? extends PersistentProperty<?>> persistentProperties = getPersistentProperties(persistentEntity);
            Map<String, String> propertyColumns = new LinkedHashMap<>();
            propertyColumns.put(idProperty.getName(), idProperty.getName());
            persistentProperties.forEach(item -> propertyColumns.put(item.getName(), getColumnName(item)));
            entityInfo.setTableName(getTableName(persistentEntity));
            entityInfo.setPropertyColumns(propertyColumns);
            if (Auditable.class.isAssignableFrom(persistentEntity.getType())) {
                entityInfo.setIgnoreConditionFields(new String[]{CREATED_BY, CREATED_DATE, LAST_MODIFIED_BY, LAST_MODIFIED_DATE});
            }
            cache.put(persistentEntity.getType().getName(), entityInfo);
        }
        return entityInfo;
    }

    protected abstract String getTableName(PersistentEntity<?, ? extends PersistentProperty<?>> persistentEntity);

    protected abstract String getColumnName(PersistentProperty<?> persistentProperty);

    protected abstract Iterable<? extends PersistentProperty<?>> getPersistentProperties(PersistentEntity<?, ? extends PersistentProperty<?>> persistentEntity);
}
