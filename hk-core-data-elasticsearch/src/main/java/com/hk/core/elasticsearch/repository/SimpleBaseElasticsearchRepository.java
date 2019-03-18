package com.hk.core.elasticsearch.repository;

import com.hk.commons.util.BeanUtils;
import com.hk.commons.util.CollectionUtils;
import com.hk.core.data.commons.utils.OrderUtils;
import com.hk.core.elasticsearch.query.Condition;
import com.hk.core.page.QueryPage;
import com.hk.core.page.SimpleQueryPage;
import com.hk.core.query.Order;
import com.hk.core.query.QueryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformation;
import org.springframework.data.elasticsearch.repository.support.SimpleElasticsearchRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 自定义扩展 Repository, 添加自定义查询方法
 *
 * @author huangkai
 * @date 2019/3/11 9:00
 */
public class SimpleBaseElasticsearchRepository<T extends Serializable>
        extends SimpleElasticsearchRepository<T> implements BaseElasticsearchRepository<T> {

    public SimpleBaseElasticsearchRepository() {
    }

    public SimpleBaseElasticsearchRepository(ElasticsearchEntityInformation<T, String> metadata,
                                             ElasticsearchOperations elasticsearchOperations) {
        super(metadata, elasticsearchOperations);
    }

    public SimpleBaseElasticsearchRepository(ElasticsearchOperations elasticsearchOperations) {
        super(elasticsearchOperations);
    }

    @Override
    public QueryPage<T> findByPage(QueryModel<T> queryModel) {
        Map<String, Object> map = BeanUtils.beanToMap(queryModel.getParam(), "class");
        CriteriaQuery query = new CriteriaQuery(new Criteria(), PageRequest.of(queryModel.getPageIndex(),
                queryModel.getPageSize(), OrderUtils.toSort(queryModel.getOrders())));
        if (CollectionUtils.isNotEmpty(map)) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                query.addCriteria(Criteria.where(entry.getKey()).is(entry.getValue()));
            }
        }
        Page<T> pageResult = elasticsearchOperations.queryForPage(query, getEntityClass());
        return new SimpleQueryPage<>(queryModel, pageResult.getContent(), pageResult.getTotalElements());
    }

    @Override
    public QueryPage<T> findByPage(List<Condition> conditions, int pageIndex, int pageSize, Order... orders) {
        CriteriaQuery query = new CriteriaQuery(new Criteria(), PageRequest.of(pageIndex,
                pageSize, OrderUtils.toSort(orders)));
        Condition.addCriteria(query, conditions);
        Page<T> pageResult = elasticsearchOperations.queryForPage(query, getEntityClass());
        return new SimpleQueryPage<>(pageResult.getContent(), pageResult.getTotalElements(), pageIndex, pageSize);
    }

    @Override
    public long count(T t) {
        CriteriaQuery query = new CriteriaQuery(new Criteria());
        Map<String, Object> map = BeanUtils.beanToMap(t, "class");
        if (CollectionUtils.isNotEmpty(map)) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                query.addCriteria(Criteria.where(entry.getKey()).is(entry.getValue()));
            }
        }
        return elasticsearchOperations.count(query);
    }

    @Override
    public void deleteByIds(Iterable<String> ids) {
        if (CollectionUtils.isNotEmpty(ids)) {
            ids.forEach(this::deleteById);
        }
    }
}
