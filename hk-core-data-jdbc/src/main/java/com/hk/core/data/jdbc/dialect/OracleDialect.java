package com.hk.core.data.jdbc.dialect;

import com.hk.commons.util.StringUtils;

/**
 * @author: kevin
 * @date: 2018-09-19 10:06
 */
public class OracleDialect implements Dialect {

    @Override
    public String getLimitSql(String sql, int offset, int rows) {
        return getLimitString(sql, offset, Integer.toString(offset), Integer.toString(rows));
    }

    /**
     * 将sql变成分页sql语句,提供将offset及limit使用占位符号(placeholder)替换.
     * <pre>
     * 如mysql
     * dialect.getLimitString("select * from user", 12, ":offset",0,":limit") 将返回
     * select * from user limit :offset,:limit
     * </pre>
     *
     * @param sql               实际SQL语句
     * @param offset            分页开始纪录条数
     * @param offsetPlaceholder 分页开始纪录条数－占位符号
     * @param limitPlaceholder  分页纪录条数占位符号
     * @return 包含占位符的分页sql
     */
    private String getLimitString(String sql, int offset, String offsetPlaceholder, String limitPlaceholder) {
        sql = sql.trim();
        boolean isForUpdate = false;
        if (StringUtils.endsWithIgnoreCase(sql.toLowerCase(), " for update")) {
            sql = sql.substring(0, sql.length() - 11);
            isForUpdate = true;
        }
        StringBuilder pagingSelect = new StringBuilder(sql.length() + 100);

        if (offset > 0) {
            pagingSelect.append("SELECT * FROM ( SELECT row_.*, rownum rownum_ from ( ");
        } else {
            pagingSelect.append("SELECT * FROM ( ");
        }
        pagingSelect.append(sql);
        if (offset > 0) {
            String endString = offsetPlaceholder + "+" + limitPlaceholder;
            pagingSelect.append(" ) row_ WHERE rownum <= ").append(endString).append(") WHERE rownum_ > ").append(offsetPlaceholder);
        } else {
            pagingSelect.append(" ) WHERE rownum <= ").append(limitPlaceholder);
        }

        if (isForUpdate) {
            pagingSelect.append(" FOR UPDATE");
        }

        return pagingSelect.toString();
    }
}
