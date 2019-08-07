package com.hk.core.page;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果
 *
 * @author kevin
 * @date 2018年1月24日上午9:56:21
 */
public interface QueryPage<T> extends Serializable {

    /**
     * 返回当前页
     *
     * @return pageIndex
     */
    int getPageIndex();

    /**
     * 返回当前页记录数
     *
     * @return pageSize
     */
    int getPageSize();

    /**
     * 返回当前页数据
     *
     * @return data
     */
    List<T> getData();

    /**
     * 返回总记录
     *
     * @return totalRow
     */
    long getTotalRow();

    /**
     * 返回总页数
     *
     * @return 总页数
     */
    default long getTotalPage() {
        int pageSize = getPageSize();
        if (pageSize <= 0) {
            return 1;
        }
        long totalRow = getTotalRow();
        return totalRow == 0 ? 1 : totalRow / pageSize + (totalRow % pageSize > 0 ? 1 : 0);
    }

}
