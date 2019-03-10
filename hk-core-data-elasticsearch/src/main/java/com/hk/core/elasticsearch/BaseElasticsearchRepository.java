package com.hk.core.elasticsearch;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.hk.core.page.QueryPage;
import com.hk.core.query.Order;
import com.hk.core.query.QueryModel;
import com.hk.core.elasticsearch.query.Condition;

public interface BaseElasticsearchRepository<T extends Serializable, ID extends Serializable>
		extends ElasticsearchRepository<T, ID> {
	
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

}
