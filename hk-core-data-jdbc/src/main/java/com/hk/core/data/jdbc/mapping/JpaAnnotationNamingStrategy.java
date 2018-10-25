package com.hk.core.data.jdbc.mapping;

import com.hk.commons.util.StringUtils;
import org.springframework.data.jdbc.repository.config.JdbcConfiguration;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.Optional;

/**
 * 只有使用Jpa注解表名与字段映射，需要注入此bean
 *
 * @author: sjq-278
 * @date: 2018-10-25 12:37
 * @see JdbcConfiguration#jdbcMappingContext(Optional)
 */
public class JpaAnnotationNamingStrategy implements NamingStrategy {

    @Override
    public String getTableName(Class<?> type) {
        String tableName = null;
        Entity entity = type.getAnnotation(Entity.class);
        if (null != entity) {
            tableName = entity.name();
        }
        if (StringUtils.isEmpty(tableName)) {
            Table table = type.getAnnotation(Table.class);
            if (null != table) {
                return table.name();
            }
        }
        if (StringUtils.isEmpty(tableName)) {
            tableName = type.getSimpleName().toLowerCase();
        }
        return tableName;
    }

    @Override
    public String getColumnName(RelationalPersistentProperty property) {
        Field field = property.getField();
        Column column = field.getAnnotation(Column.class);
        return null == column ? field.getName().toLowerCase() : column.name();
    }
}
