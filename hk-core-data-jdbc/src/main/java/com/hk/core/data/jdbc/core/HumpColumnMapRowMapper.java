package com.hk.core.data.jdbc.core;

import com.hk.commons.util.LinkedHumpMap;
import org.springframework.jdbc.core.ColumnMapRowMapper;

import java.util.Map;

/**
 * @author: kevin
 * @date: 2018-10-08 09:27
 */
public class HumpColumnMapRowMapper extends ColumnMapRowMapper {

    @Override
    protected Map<String, Object> createColumnMap(int columnCount) {
        return new LinkedHumpMap<>(columnCount);
    }
}
