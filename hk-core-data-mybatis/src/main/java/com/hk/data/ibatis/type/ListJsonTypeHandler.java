package com.hk.data.ibatis.type;

import com.hk.commons.util.AssertUtils;
import com.hk.commons.util.JsonUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author huangkai
 * @date 2019-03-12 23:13
 */
public class ListJsonTypeHandler<T> extends BaseTypeHandler<List<T>> {

    private Class<T> clazz;

    public ListJsonTypeHandler(Class<T> clazz) {
        AssertUtils.notNull(clazz, "Type argument cannot be null");
        this.clazz = clazz;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<T> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JsonUtils.serialize(parameter));
    }

    @Override
    public List<T> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return JsonUtils.deserializeList(rs.getString(columnName), clazz);
    }

    @Override
    public List<T> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return JsonUtils.deserializeList(rs.getString(columnIndex), clazz);
    }

    @Override
    public List<T> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return JsonUtils.deserializeList(cs.getString(columnIndex), clazz);
    }
}
