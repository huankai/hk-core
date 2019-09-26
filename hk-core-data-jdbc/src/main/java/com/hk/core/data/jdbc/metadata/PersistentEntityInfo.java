package com.hk.core.data.jdbc.metadata;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author kevin
 * @date 2018-10-12 13:55
 */
@Data
@Deprecated
public class PersistentEntityInfo {

    private Map<String, String> propertyColumns = new LinkedHashMap<>();

    private String tableName;

    private String idField;

    private String[] ignoreConditionFields;

}
