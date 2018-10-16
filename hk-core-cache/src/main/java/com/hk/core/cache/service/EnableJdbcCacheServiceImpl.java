package com.hk.core.cache.service;

import com.hk.core.data.jdbc.SelectArguments;
import com.hk.core.data.jdbc.query.CompositeCondition;
import com.hk.core.page.ListResult;
import com.hk.core.page.QueryModel;
import com.hk.core.page.QueryPage;
import com.hk.core.query.Order;
import com.hk.core.service.jdbc.impl.JdbcServiceImpl;
import org.springframework.aop.framework.AopContext;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service implementation Enable Cache.
 *
 * @author: kevin
 * @date: 2018-05-16 09:58
 * @see com.hk.core.cache.spring.FixUseSupperClassAnnotationParser
 * @see com.hk.core.cache.spring.FixUseSupperClassCacheOperationSource
 */
public abstract class EnableJdbcCacheServiceImpl<T extends Persistable<PK>, PK extends Serializable> extends JdbcServiceImpl<T, PK> {

    @Override
    public ListResult<T> findAll(CompositeCondition condition, Order... orders) {
        return super.findAll(condition, orders);
    }

    @Override
    public Iterable<T> findByIds(PK... pks) {
        return super.findByIds(pks);
    }

    @Override
    public ListResult<T> findAll(T t, Order... orders) {
        return super.findAll(t, orders);
    }

    @Override
    public QueryPage<T> queryForPage(QueryModel<T> query) {
        return super.queryForPage(query);
    }

    @Override
    public QueryPage<T> queryForPage(SelectArguments selectArguments) {
        return super.queryForPage(selectArguments);
    }

    @Override
    public long count(T t) {
        return super.count(t);
    }

    @Override
    public ListResult<T> findAll(CompositeCondition condition, Set<String> groupBys, Order... orders) {
        return super.findAll(condition, groupBys, orders);
    }

    @Override
    public void deleteById(PK pk) {
        super.deleteById(pk);
    }

    @Override
    public void delete(T entity) {
        super.delete(entity);
    }

    @Override
    public void delete(Iterable<T> entities) {
        super.delete(entities);
    }

    @Override
    public T insert(T t) {
        return super.insert(t);
    }

    @Override
    public Iterable<T> batchInsert(Iterable<T> entities) {
        return super.batchInsert(entities);
    }

    @Override
    public Optional<T> findById(PK pk) {
        return super.findById(pk);
    }

    @Override
    public Iterable<T> findAll(Order... orders) {
        return super.findAll(orders);
    }

    @Override
    public Iterable<T> findByIds(Iterable<PK> pks) {
        return super.findByIds(pks);
    }

    @Override
    public boolean existsById(PK pk) {
        return super.existsById(pk);
    }

    @Override
    public boolean exists(T t) {
        return super.exists(t);
    }

    @Override
    public long count() {
        return super.count();
    }

    @Override
    public T updateById(T t) {
        return super.updateById(t);
    }

    @Override
    public T insertOrUpdate(T t) {
        return super.insertOrUpdate(t);
    }

    @Override
    public List<T> insertOrUpdate(Iterable<T> entities) {
        return super.insertOrUpdate(entities);
    }

    @Override
    public void deleteByIds(Iterable<PK> pks) {
        super.deleteByIds(pks);
    }

    @Override
    public List<T> batchUpdate(Iterable<T> entities) {
        return super.batchUpdate(entities);
    }


//    @Override
//    @Cacheable(key = "'id'+#root.args[0]")
//    public Optional<T> findById(PK pk) {
//        return super.findById(pk);
//    }
//
//    @Override
//    @Cacheable(key = "'count'")
//    public long count() {
//        return super.count();
//    }

    //    /**
//     * <p>
//     * If the key(ID) exists in the cache,it is obtained directly from the cache.<br/>
//     * Otherwise,query from the database and put the result in the cache.
//     * <p>
//     *
//     * @param id id
//     * @return
//     */
//    @Override
//    public Optional<T> findOne(PK id) {
//        return super.findOne(id);
//    }
//
//    @Override
//    @Cacheable(key = "'id'+#root.args[0]")
//    public T getOne(PK pk) {
//        return super.getOne(pk);
//    }

    //    }
//
//    /**
//     * 删除缓存
//     *
//     * @param entity
//     * @return
//     */
//    @Override
//    @CachePut(key = "'id'+#root.args[0].id")
//    public T updateByIdSelective(T entity) {
//        return super.updateByIdSelective(entity);
//    }
//
//    @Override
//    @Caching(put = @CachePut(key = "'id'+#root.args[0].id", condition = "#root.args[0].id != null"))
//    public T insertOrUpdate(T t) {
//        return super.insertOrUpdate(t);
//    }
//
//    @Override
////    @Cacheable(key = "'id'+#result.id")
//    public T insert(T t) {
//        return super.insert(t);
//    }
//
//    @Override
//    @CacheEvict(allEntries = true)
//    public Collection<T> insertOrUpdate(Collection<T> entities) {
//        return super.insertOrUpdate(entities);
//    }
//
//    @Override
//    @CachePut(key = "'id'+#root.args[0].id")
//    public T updateById(T t) {
//        return super.updateById(t);
//    }
//
//    /**
//     * <p>
//     * Delete data from the primary key and delete the data from the cache.
//     * </p>
//     *
//     * @param id id
//     */
//    @Override
//    @Caching(
//            evict = {
//                    @CacheEvict(key = "'id'+#id"),
//                    @CacheEvict(key = "'count'")
//            }
//    )
//    public void deleteById(PK id) {
//        super.deleteById(id);
//    }
//
//    @Override
//    @CacheEvict(allEntries = true)
//    public void deleteByIds(Collection<PK> ids) {
//        super.deleteByIds(ids);
//    }
//
//    /**
//     * <p>
//     * delete entity and Delete Cache.<br/>
//     * When id != null,delete the single record of the specified ID and delete the cached count record.<br/>
//     * Otherwise,It is impossible to determine how many records are deleted and empty the entity and count in the cache.
//     * <p>
//     *
//     * @param entity entity
//     */
//    @Override
//    @Caching(
//            evict = {
//                    @CacheEvict(key = "'id'+#root.args[0].id", condition = "#root.args[0].id != null"),
//                    @CacheEvict(allEntries = true, condition = "#root.args[0].id == null"),
//                    @CacheEvict(key = "'count'")
//            }
//    )
//    public void delete(T entity) {
//        super.delete(entity);
//    }
//
//    /**
//     * <p>
//     * delete List and Clear Cache.
//     * </p>
//     *
//     * @param entities entities
//     */
//    @Override
//    @CacheEvict(allEntries = true)
//    public void delete(Collection<T> entities) {
//        super.delete(entities);
//    }
//
//    /**
//     * <p>
//     * Get the current proxy object.
//     * </p>
//     * <p>
//     * must be set @EnableAspectJAutoProxy(exposeProxy = true) to true.
//     * </p>
//     *
//     * @return
//     */
    @SuppressWarnings("unchecked")
    protected final EnableJdbcCacheServiceImpl<T, PK> getCurrentProxy() {
        return (EnableJdbcCacheServiceImpl<T, PK>) AopContext.currentProxy();
    }

}
