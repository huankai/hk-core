package com.hk.core.page;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: kevin
 * @date 2018-06-06 17:58
 */
@NoArgsConstructor
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
