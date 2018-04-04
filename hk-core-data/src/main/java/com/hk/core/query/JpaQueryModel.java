package com.hk.core.query;

/**
 * 使用JPA查询
 *
 * @author huangkai
 * @date 2017年12月23日下午3:35:28
 */
public class JpaQueryModel<T> extends QueryModel {

    private T params;

    /**
     * @return the params
     */
    public T getParams() {
        return params;
    }

    /**
     * @param params the params to set
     */
    public void setParams(T params) {
        this.params = params;
    }

}
