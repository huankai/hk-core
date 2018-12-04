package com.hk.core.solr.respoitory;

import com.hk.commons.util.BeanUtils;
import com.hk.commons.util.CollectionUtils;
import com.hk.core.data.commons.utils.OrderUtils;
import com.hk.core.page.QueryModel;
import com.hk.core.page.QueryPage;
import com.hk.core.page.SimpleQueryPage;
import com.hk.core.query.Order;
import com.hk.core.solr.query.Condition;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.data.solr.repository.query.SolrEntityInformation;
import org.springframework.data.solr.repository.support.SimpleSolrRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author: huangkai
 * @date: 2018-12-03 14:13
 * @see SimpleSolrRepository
 */
public class BaseSimpleSolrRepository<T extends Serializable, ID extends Serializable> extends SimpleSolrRepository<T, ID> implements BaseSolrRepository<T, ID> {

    private final String solrCollectionName;

    public BaseSimpleSolrRepository(SolrOperations solrOperations, SolrEntityInformation<T, ?> metadata) {
        super(solrOperations, metadata);
        solrCollectionName = metadata.getCollectionName();
    }

    @Override
    public QueryPage<T> findByPage(QueryModel<T> queryModel) {
        SimpleQuery query = new SimpleQuery().setPageRequest(new SolrPageRequest(queryModel.getPageIndex(), queryModel.getPageSize()));
        Map<String, Object> map = BeanUtils.beanToMap(queryModel.getParam(), "class");
        if (CollectionUtils.isNotEmpty(map)) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (Objects.nonNull(entry.getValue())) {
                    query.addCriteria(new Criteria(entry.getKey()).is(entry.getValue()));
                }
            }
        }
        fillQueryCondition(query);
        query.addSort(OrderUtils.toSort(queryModel.getOrders()));
        ScoredPage<T> page = getSolrOperations().queryForPage(solrCollectionName, query, getEntityClass());
        return new SimpleQueryPage<>(queryModel, page.getContent(), page.getTotalElements());
    }

    @Override
    public QueryPage<T> findByPage(List<Condition> conditions, int pageIndex, int pageSize, Order... orders) {
        SimpleQuery query = new SimpleQuery().setPageRequest(new SolrPageRequest(pageIndex, pageSize));
        if (CollectionUtils.isNotEmpty(conditions)) {
            for (Condition condition : conditions) {
                Criteria criteria = condition.toSolrCriteria();
                if (null != criteria) {
                    query.addCriteria(criteria);
                }
            }
        }
        fillQueryCondition(query);
        query.addSort(OrderUtils.toSort(orders));
        ScoredPage<T> page = getSolrOperations().queryForPage(solrCollectionName, query, getEntityClass());
        return new SimpleQueryPage<>(page.getContent(), page.getTotalElements(), pageIndex, pageSize);
    }

    private void fillQueryCondition(Query query) {
        if (query.getCriteria() == null) {
            query.addCriteria(AnyCriteria.any());
        }
    }
}
