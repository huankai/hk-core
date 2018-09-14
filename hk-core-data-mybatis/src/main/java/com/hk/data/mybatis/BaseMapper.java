package com.hk.data.mybatis;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hk.core.page.QueryPage;
import com.hk.core.page.SimpleQueryPage;
import com.hk.core.query.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: kevin
 * @date: 2018-07-04 11:35
 */
public interface BaseMapper<T> {

    /**
     * 不分页查询
     *
     * @param t      查询条件
     * @param orders 排序
     * @return result
     */
    List<T> findAll(@Param("param") T t, @Param("orders") List<Order> orders);

    /**
     * 分页查询
     *
     * @param t         查询条件
     * @param orders    查询排序
     * @param pageIndex 开始页
     * @param pageSize  每页显示记录数
     * @return result
     */
    default QueryPage<T> findByPage(T t, List<Order> orders, int pageIndex, int pageSize) {
        Page<T> page = PageHelper.startPage(pageIndex, pageSize).doSelectPage(() -> findAll(t, orders));
        return new SimpleQueryPage<>(page.getResult(), page.getTotal(), pageIndex, pageSize);
    }
}
