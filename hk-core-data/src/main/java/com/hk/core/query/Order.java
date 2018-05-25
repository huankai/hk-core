package com.hk.core.query;

import com.hk.core.query.jpa.PathUtils;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.jpa.criteria.OrderImpl;
import org.springframework.data.domain.Sort.Direction;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.io.Serializable;

/**
 * Order
 *
 * @author kally
 * @date 2018年2月5日下午12:49:44
 */
public final class Order implements Serializable {

    /**
     * 排序字段
     */
    @Getter
    @Setter
    private String field;

    /**
     * ASC | DESC
     */
    @Getter
    @Setter
    private boolean desc;

    public Order() {
    }

    public Order(String field, boolean desc) {
        this.field = field;
        this.desc = desc;
    }

    /**
     * ASC
     *
     * @param field
     * @return
     */
    public static Order asc(String field) {
        return new Order(field, false);
    }

    /**
     * DESC
     *
     * @param field
     * @return
     */
    public static Order desc(String field) {
        return new Order(field, true);
    }

    @Override
    public String toString() {
        return toSqlString();
    }

    public String toSqlString() {
        return String.format(" %s %s", getField(), desc ? Direction.DESC.name() : Direction.ASC.name());
    }

    /**
     * JPA 排序
     *
     * @param root
     * @return
     */
    @SuppressWarnings("rawtypes")
    public javax.persistence.criteria.Order toJpaOrder(Root<?> root) {
        Path expression = PathUtils.getPath(root, field);
        return new OrderImpl(expression, !desc);
    }

    /**
     * Spring JPA
     *
     * @return
     */
    public org.springframework.data.domain.Sort.Order toSpringJpaOrder() {
        return new org.springframework.data.domain.Sort.Order(desc ? Direction.DESC : Direction.ASC, field);
    }
}
