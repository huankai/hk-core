package com.hk.core.data.jdbc;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: sjq-278
 * @date: 2018-10-12 13:55
 */
@Data
public class PersistentEntityInfo {


    private Map<String, String> propertyColumns = new LinkedHashMap<>();

    private String tableName;

    private String idField;

    private String[] ignoreConditionFields;

}
