package com.hk.core.data.jdbc.metadata;

import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentProperty;

/**
 * @author: sjq-278
 * @date: 2018-10-13 00:28
 */
public interface PersistentEntityMetadata {

    PersistentEntityInfo getPersistentEntityInfo(PersistentEntity<?, ? extends PersistentProperty> persistentEntity);
}
