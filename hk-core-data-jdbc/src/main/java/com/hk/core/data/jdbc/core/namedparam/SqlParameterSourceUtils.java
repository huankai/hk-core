package com.hk.core.data.jdbc.core.namedparam;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * @author huangkai
 * @date 2018-12-18 17:41
 */
public abstract class SqlParameterSourceUtils extends org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils {

    /**
     * @param candidates candidates
     * @return {@link SqlParameterSource}
     * @see org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils#createBatch(Collection)
     */
    @SuppressWarnings("unchecked")
	public static SqlParameterSource[] createBatch(Collection<?> candidates) {
        SqlParameterSource[] batch = new SqlParameterSource[candidates.size()];
        int i = 0;
        for (Object candidate : candidates) {
            batch[i] = (candidate instanceof Map ? new MapSqlParameterSource((Map<String, ?>) candidate) :
                    new OptionalBeanPropertySqlParameterSource(candidate));
            i++;
        }
        return batch;
    }

    /**
     * Optional 类型的参数 {@link #getValue(String)} 时获取具体的值
     */
    private static class OptionalBeanPropertySqlParameterSource extends BeanPropertySqlParameterSource {

        private OptionalBeanPropertySqlParameterSource(Object object) {
            super(object);
        }

        /**
         * 如果参数类型为 Optional，获取 Optional 的值。
         *
         * @param paramName paramName
         * @return 对象属性值
         */
        @Nullable
        @Override
        public Object getValue(String paramName) {
            Object value = super.getValue(paramName);
            if (value instanceof Optional) {
                value = ((Optional<?>) value).orElse(null);
            }
            return value;
        }
    }
}
