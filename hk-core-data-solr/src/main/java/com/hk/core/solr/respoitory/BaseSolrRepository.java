package com.hk.core.solr.respoitory;

import com.hk.core.page.QueryModel;
import com.hk.core.page.QueryPage;
import com.hk.core.query.Order;
import com.hk.core.solr.query.Condition;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.io.Serializable;
import java.util.List;

/**
 * @author: kevin
 * @date: 2018-07-04 13:01
 */
@NoRepositoryBean
public interface BaseSolrRepository<T extends Serializable, ID extends Serializable> extends SolrCrudRepository<T, ID> {

    QueryPage<T> findByPage(QueryModel<T> queryModel);

    QueryPage<T> findByPage(List<Condition> conditions, int pageIndex, int pageSize, Order... orders);

}
