package com.hk.core.elasticsearch.repository;

import com.hk.commons.util.*;
import com.hk.core.data.commons.utils.OrderUtils;
import com.hk.core.elasticsearch.query.Condition;
import com.hk.core.page.QueryPage;
import com.hk.core.page.SimpleQueryPage;
import com.hk.core.query.Order;
import com.hk.core.query.QueryModel;
import lombok.NoArgsConstructor;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.BeanWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchEntityInformation;
import org.springframework.data.elasticsearch.repository.support.SimpleElasticsearchRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 自定义扩展 Repository, 添加自定义查询方法
 *
 * @author huangkai
 * @date 2019/3/11 9:00
 */
@NoArgsConstructor
public class SimpleBaseElasticsearchRepository<T extends Persistable<String>> extends SimpleElasticsearchRepository<T, String>
        implements BaseElasticsearchRepository<T> {

    public SimpleBaseElasticsearchRepository(ElasticsearchEntityInformation<T, String> metadata,
                                             ElasticsearchOperations elasticsearchOperations) {
        super(metadata, elasticsearchOperations);
    }

    public SimpleBaseElasticsearchRepository(ElasticsearchOperations elasticsearchOperations) {
        super(elasticsearchOperations);
    }

    @Override
    public QueryPage<T> findByPage(QueryModel<T> queryModel) {
        Map<String, Object> map = BeanUtils.beanToMap(queryModel.getParam(), "class", "new");
        CriteriaQuery query = new CriteriaQuery(new Criteria(), PageRequest.of(queryModel.getStartRowIndex(),
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
        CriteriaQuery query = new CriteriaQuery(new Criteria(),
                PageRequest.of(pageIndex, pageSize, OrderUtils.toSort(orders)));
        Condition.addCriteria(query, conditions);
        Page<T> pageResult = elasticsearchOperations.queryForPage(query, getEntityClass());
        return new SimpleQueryPage<>(pageResult.getContent(), pageResult.getTotalElements(), pageIndex, pageSize);
    }

    @Override
    public long count(T t) {
        CriteriaQuery query = new CriteriaQuery(new Criteria());
        Map<String, Object> map = BeanUtils.beanToMap(t, "class", "new");
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
            DeleteQuery deleteQuery = new DeleteQuery();
            deleteQuery.setIndex(entityInformation.getIndexName());
            deleteQuery.setType(entityInformation.getType());
            deleteQuery.setQuery(QueryBuilders.idsQuery().addIds(CollectionUtils.toArray(ids)));
            elasticsearchOperations.delete(deleteQuery);
        }
    }

    @Override
    public List<T> findAll(List<Condition> conditions, Order... orders) {
        CriteriaQuery query = new CriteriaQuery(new Criteria());
        Condition.addCriteria(query, conditions);
        query.addSort(OrderUtils.toSort(orders));
        return elasticsearchOperations.queryForList(query, getEntityClass());
    }

    @Override
    public void bulkUpdate(Collection<T> entities) {
        if (CollectionUtils.isNotEmpty(entities)) {
            List<UpdateQuery> list = new ArrayList<>(entities.size());
            entities.forEach(item -> {
                String id = item.getId();
                if (StringUtils.isNotEmpty(id)) {
                    UpdateRequest request = new UpdateRequest();
                    Map<String, Object> updateMap = BeanUtils.beanToMap(item, "class", "new", "id");
                    if (CollectionUtils.isNotEmpty(updateMap)) {
                        request.doc(JsonUtils.serialize(updateMap), XContentType.JSON);
                        list.add((new UpdateQueryBuilder().withId(id).withClass(item.getClass())
                                .withUpdateRequest(request).build()));
                    }
                }
            });
            elasticsearchOperations.bulkUpdate(list);
        }
    }

//    @Override
//    public SearchResponse suggest(SuggestBuilder suggestBuilder) {
//        // TODO 未完成
//        return null;
//        return elasticsearchOperations.getClient()
//                .prepareSearch(entityInformation.getIndexName())
//                .suggest(suggestBuilder)
//                .get();
//    }

    @Override
    public QueryPage<T> queryForPage(SearchQuery searchQuery) {
        Page<T> page = elasticsearchOperations.queryForPage(searchQuery, getEntityClass(), new SearchResultMapper() {

            @Override
            public <E> AggregatedPage<E> mapResults(SearchResponse response, Class<E> clazz, Pageable pageable) {
                SearchHits hits = response.getHits();
                List<E> result = new ArrayList<>(hits.getHits().length);
                E data;
                BeanWrapper beanWrapper;
                for (SearchHit searchHit : hits) {
                    data = JsonUtils.deserialize(searchHit.getSourceAsString(), clazz);
                    Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
                    if (CollectionUtils.isNotEmpty(highlightFields)) {
                        beanWrapper = BeanWrapperUtils.createBeanWrapper(data);
                        for (Map.Entry<String, HighlightField> entry : highlightFields.entrySet()) {
                            Text[] fragments = entry.getValue().getFragments();
                            if (ArrayUtils.isNotEmpty(fragments)) {
                                beanWrapper.setPropertyValue(entry.getKey(), fragments[0].toString());
                            }
                        }
                    }
                    result.add(data);
                }
                return new AggregatedPageImpl<>(result, pageable, hits.getTotalHits(), response.getAggregations(),
                        response.getScrollId(), hits.getMaxScore());
            }

            @Override
            public <E> E mapSearchHit(SearchHit searchHit, Class<E> type) {
                return JsonUtils.deserialize(searchHit.getSourceAsString(), type);
            }
        });
        return new SimpleQueryPage<>(page.getContent(), page.getTotalElements(), page.getNumber(), page.getSize());
    }
}
