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

/**
 * json 类型解析
 *
 * @author huangkai
 * @date 2019-03-12 23:01
 */
public class JsonTypeHandler<T extends Serializable> extends BaseTypeHandler<T> {

    private Class<T> clazz;

    public JsonTypeHandler(Class<T> clazz) {
        AssertUtils.notNull(clazz, "Type argument cannot be null");
        this.clazz = clazz;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JsonUtils.serialize(parameter));
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return JsonUtils.deserialize(rs.getString(columnName), this.clazz);
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return JsonUtils.deserialize(rs.getString(columnIndex), this.clazz);
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return JsonUtils.deserialize(cs.getString(columnIndex), this.clazz);
    }
}
