package com.hk.core.query.jdbc;

import com.hk.commons.util.AssertUtils;
import org.springframework.data.domain.Persistable;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: kevin
 * @date 2018-05-31 09:17
 */
public class Update<T extends Persistable<?>> {

    private boolean updateNullField;

    private Map<String, Object> paramMap;

    private T persistable;

    public Update(T persistable) {
        this(persistable, false);
    }

    public Update(T persistable, boolean updateNullField) {
        AssertUtils.notNull(persistable, "permistable must not be null");
        this.persistable = persistable;
        this.updateNullField = updateNullField;
        this.paramMap = new LinkedHashMap<>();
    }

    public void setPersistable(T persistable) {
        this.persistable = persistable;
    }

    public void setUpdateNullField(boolean updateNullField) {
        this.updateNullField = updateNullField;
    }

    public String toSqlString() {
//        BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(persistable);
//        Class<? extends Persistable> clazz = persistable.getClass();
//        List<Field> columnFieldList = FieldUtils.getFieldsListWithAnnotation(clazz, Column.class);
//        List<String> updateFieldList = Lists.newArrayList();
//        columnFieldList.forEach(field -> {
//            String fieldName = field.getName();
//            Column column = field.getAnnotation(Column.class);
//            Object value = wrapper.getPropertyValue(fieldName);
//            if (null != value || updateNullField) {
//                paramMap.put(fieldName, value);
//                updateFieldList.add(String.format("%s = :%s", column.name(), fieldName));
//            }
//        });
//        if (paramMap.isEmpty()) {
//            return null;
//        }
//        List<Field> idFieldList = FieldUtils.getFieldsListWithAnnotation(persistable.getClass(), Id.class);
//        Field idField = idFieldList.get(0);
//        paramMap.put(idField.getName(), wrapper.getPropertyValue(idField.getName()));
//        return String.format("UPDATE %s SET %s WHERE %s = :%s",
//                clazz.getAnnotation(Table.class).name(),
//                updateFieldList.stream().collect(Collectors.joining(",")),
//                idField.getName()
//                , idField.getName());
        return null;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    @Override
    public String toString() {
        return toSqlString();
    }
}
