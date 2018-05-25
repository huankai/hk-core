package com.hk.core.query.jdbc;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 查詢结果
 */
@AllArgsConstructor
public class ListResult<T> {

    /**
     * 查詢总记录数
     */
    @Getter
    private long totalRowCount;

    /**
     * 结果集
     */
    @Getter
    private List<T> result;

}
