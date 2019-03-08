package com.hk.core.solr.respoitory;

import com.hk.core.query.QueryModel;
import com.hk.core.page.QueryPage;
import com.hk.core.query.Order;
import com.hk.core.solr.query.Condition;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.io.Serializable;
import java.util.List;

/**
 * @author kevin
 * @date 2018-07-04 13:01
 */
@NoRepositoryBean
public interface BaseSolrRepository<T extends Serializable, ID extends Serializable> extends SolrCrudRepository<T, ID> {

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
