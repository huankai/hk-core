package com.hk.core.data.jdbc;

/**
 * @author: sjq-278
 * @date: 2018-10-13 00:28
 */
public interface PersistenEntityMetadata {

    PersistentEntityInfo getPersistentEntityInfo(Class<?> clazz);
}
