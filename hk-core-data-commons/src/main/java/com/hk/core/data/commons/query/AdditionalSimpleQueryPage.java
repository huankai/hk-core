package com.hk.core.data.commons.query;

import com.hk.core.data.commons.ListResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


/**
 * @author: kevin
 * @date 2018-07-04 11:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AdditionalSimpleQueryPage<T> extends SimpleQueryPage<T> {

    /**
     * 其它扩展参数
     */
    private Object additional;

    public AdditionalSimpleQueryPage(QueryModel<?> query, ListResult<T> result, Object additional) {
        this(query, result.getResult(), result.getTotalRowCount(), additional);
    }

    public AdditionalSimpleQueryPage(QueryModel<?> query, List<T> data, long totalRow, Object additional) {
        this(data, totalRow, query.getPageIndex(), query.getPageSize(), additional);
    }

    public AdditionalSimpleQueryPage(List<T> data, long totalRow, int pageIndex, int pageSize, Object additional) {
        super(data, totalRow, pageIndex, pageSize);
        this.additional = additional;
    }

    public AdditionalSimpleQueryPage(QueryPage<T> page, Object additional) {
        super(page.getData(), page.getTotalRow(), page.getPageIndex(), page.getPageSize());
        this.additional = additional;
    }
}
