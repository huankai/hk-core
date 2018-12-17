package com.hk.core.data.jdbc.core;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kevin
 * @date 2018-10-26 13:41
 * @see org.springframework.data.jdbc.core.MapEntityRowMapper
 */
class MapEntityRowMapper<T> implements RowMapper<Map.Entry<Object, T>> {

    private final RowMapper<T> delegate;
    private final String keyColumn;

    /**
     * @param delegate rowmapper used as a delegate for obtaining the map values.
     * @param keyColumn the name of the key column.
     */
    MapEntityRowMapper(RowMapper<T> delegate, String keyColumn) {

        this.delegate = delegate;
        this.keyColumn = keyColumn;
    }

    @NonNull
    @Override
    public Map.Entry<Object, T> mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new HashMap.SimpleEntry<>(rs.getObject(keyColumn), delegate.mapRow(rs, rowNum));
    }
}
