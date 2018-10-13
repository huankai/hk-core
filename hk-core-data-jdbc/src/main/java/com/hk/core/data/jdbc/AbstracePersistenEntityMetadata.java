package com.hk.core.data.jdbc;

import org.springframework.data.domain.Auditable;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huangkai
 * @date 2018-10-13 11:03
 */
public abstract class AbstracePersistenEntityMetadata implements PersistenEntityMetadata {

    private static Map<String, PersistentEntityInfo> map = new ConcurrentHashMap<>();

    @Override
    public PersistentEntityInfo getPersistentEntityInfo(PersistentEntity<?, ? extends PersistentProperty> persistentEntity) {
        PersistentEntityInfo entityInfo = map.get(persistentEntity.getType().getName());
        if (null == entityInfo) {
            PersistentEntityInfo info = new PersistentEntityInfo();
            PersistentProperty<?> idProperty = persistentEntity.getRequiredIdProperty();
            info.setIdField(idProperty.getName());
            Iterable<? extends PersistentProperty> persistentProperties = getPersistentProperties(persistentEntity);
            Map<String, String> propertyColumns = new LinkedHashMap<>();
            propertyColumns.put(idProperty.getName(), idProperty.getName());
            persistentProperties.forEach(item -> propertyColumns.put(item.getName(),getColumnName(item)));
            info.setTableName(getTableName(persistentEntity));
            info.setPropertyColumns(propertyColumns);
            if (Auditable.class.isAssignableFrom(persistentEntity.getType())) {
                info.setIgnoreConditionFields(new String[]{"createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate"});
            }
            map.put(persistentEntity.getType().getName(), info);
        }
        return entityInfo;
    }


    protected abstract String getTableName(PersistentEntity<?, ? extends PersistentProperty> persistentEntity);

    protected abstract String getColumnName(PersistentProperty<?> persistentProperty);


    protected abstract Iterable<? extends PersistentProperty> getPersistentProperties(PersistentEntity<?, ? extends PersistentProperty> persistentEntity);
}
