package com.hk.core.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Order
 *
 * @author kevin
 * @date 2018年2月5日下午12:49:44
 */
@Data
@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
public final class Order implements Serializable {

    /**
     * 排序字段
     */
    private String field;

    /**
     * ASC | DESC
     */
    private boolean desc;

    /**
     * ASC
     *
     * @param field field
     * @return Order asc
     */
    public static Order asc(String field) {
        return new Order(field, false);
    }

    /**
     * DESC
     *
     * @param field field
     * @return Order desc
     */
    public static Order desc(String field) {
        return new Order(field, true);
    }

    @Override
    public String toString() {
        return String.format("%s %s", field, (desc ? "DESC" : "ASC"));
    }
}
