package com.hk.core.elasticsearch.repository;

import com.hk.core.elasticsearch.query.Condition;
import com.hk.core.page.QueryPage;
import com.hk.core.query.Order;
import com.hk.core.query.QueryModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface BaseElasticsearchRepository<T extends Serializable>
        extends ElasticsearchRepository<T, String> {

    /**
     * 分页查询
     *
     * @param queryModel queryModel
     * @return {@link QueryPage}
     */
    QueryPage<T> findByPage(QueryModel<T> queryModel);

    /**
     * 分页查询
     *
     * @param conditions 查询条件
     * @param pageIndex  分页参数
     * @param pageSize   分页参数
     * @param orders     排序字段
     * @return {@link QueryPage}
     */
    QueryPage<T> findByPage(List<Condition> conditions, int pageIndex, int pageSize, Order... orders);

    /**
     * 根据条件查询记录数
     *
     * @param t t
     * @return count
     */
    long count(T t);

    /**
     * 根据id删除
     *
     * @param ids ids
     */
    void deleteByIds(Iterable<String> ids);

    /**
     * 条件查询
     *
     * @param conditions conditions
     * @param orders     orders
     * @return 查询结果集
     */
    List<T> findAll(List<Condition> conditions, Order... orders);

}
