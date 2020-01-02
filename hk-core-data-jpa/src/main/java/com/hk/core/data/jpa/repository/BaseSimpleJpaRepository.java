package com.hk.core.data.jpa.repository;

import com.hk.commons.util.*;
import com.hk.core.data.commons.utils.OrderUtils;
import com.hk.core.data.jpa.convert.QueryByExamplePredicateBuilder;
import com.hk.core.data.jpa.repository.support.DefaultQueryHints;
import com.hk.core.data.jpa.repository.support.QueryHints;
import com.hk.core.data.jpa.type.TypeFactory;
import com.hk.core.jdbc.query.ConditionQueryModel;
import com.hk.core.page.QueryPage;
import com.hk.core.page.SimpleQueryPage;
import com.hk.core.query.Order;
import org.hibernate.jpa.TypedParameterValue;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.*;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.repository.query.QueryUtils.*;


/**
 * 重写 spring data jpa 实现，spring data jpa 默认会在所有 CRUD 都添加 事物，{@link SimpleJpaRepository} 类上的 {@link Transactional} 注解。
 * 在使用 MyCat 读取分离时，如果是添加了事物的查询(SELECT 语句) ，MyCat是不会不在读节点执行的。
 * 所以，这里重新　spring data jpa 中的的实现，将所有 查询请求不添加事务，这样就保证了 MyCat 中能正确的读写分离
 *
 * @author kevin
 * @date 2018-06-07 13:34
 */
public class BaseSimpleJpaRepository<T extends Persistable<ID>, ID extends Serializable> implements JpaRepositoryImplementation<T, ID>, BaseJpaRepository<T, ID> {

    private static final String ID_MUST_NOT_BE_NULL = "查询Id不能为空!";

    private final JpaEntityInformation<T, ?> entityInformation;

    private final EntityManager em;

    private final PersistenceProvider provider;

    @Nullable
    private CrudMethodMetadata metadata;

    /**
     * Creates a new {@link BaseSimpleJpaRepository} to manage objects of the given domain type.
     *
     * @param domainClass must not be {@literal null}.
     * @param em          must not be {@literal null}.
     */
    public BaseSimpleJpaRepository(Class<T> domainClass, EntityManager em) {
        this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em);
    }

    public BaseSimpleJpaRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        Assert.notNull(entityInformation, "JpaEntityInformation must not be null!");
        Assert.notNull(entityManager, "EntityManager must not be null!");

        this.entityInformation = entityInformation;
        this.em = entityManager;
        this.provider = PersistenceProvider.fromEntityManager(entityManager);
    }

    @Override
    public <S extends T> boolean exists(Example<S> example) {
        return !getQuery(new ExampleSpecification<>(example), example.getProbeType(), Sort.unsorted()).getResultList()
                .isEmpty();
    }

    @Override
    public List<T> findAll() {
        return getQuery(null, Sort.unsorted()).getResultList();
    }

    @Override
    public List<T> findAll(Sort sort) {
        return getQuery(null, sort).getResultList();
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        if (isUnpaged(pageable)) {
            return new PageImpl<>(findAll());
        }
        return findAll((Specification<T>) null, pageable);
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        Assert.notNull(ids, "The given Iterable of Id's must not be null!");

        if (!ids.iterator().hasNext()) {
            return Collections.emptyList();
        }
        if (entityInformation.hasCompositeId()) {

            List<T> results = new ArrayList<>();

            for (ID id : ids) {
                findById(id).ifPresent(results::add);
            }

            return results;
        }

        ByIdsSpecification<T> specification = new ByIdsSpecification<>(entityInformation);
        TypedQuery<T> query = getQuery(specification, Sort.unsorted());

        return query.setParameter(specification.parameter, ids).getResultList();
    }

    @Override
    public long count() {
        return em.createQuery(getCountQueryString(), Long.class).getSingleResult();
    }

    private String getCountQueryString() {
        String countQuery = String.format(COUNT_QUERY_STRING, provider.getCountQueryPlaceholder(), "%s");
        return getQueryString(countQuery, entityInformation.getEntityName());
    }

    @Override
    @Transactional
    public void deleteById(ID id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        delete(findById(id).orElseThrow(() -> new EmptyResultDataAccessException(
                String.format("No %s entity with id %s exists!", entityInformation.getJavaType(), id), 1)));
    }

    @Override
    @Transactional
    public void delete(T entity) {
        Assert.notNull(entity, "The entity must not be null!");
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }

    @Override
    @Transactional
    public void deleteAll(Iterable<? extends T> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");
        for (T entity : entities) {
            delete(entity);
        }
    }

    @Override
    @Transactional
    public void deleteAll() {
        for (T element : findAll()) {
            delete(element);
        }
    }

    @Override
    @Transactional
    public <S extends T> S save(S entity) {
        if (entityInformation.isNew(entity)) {
            em.persist(entity);
            return entity;
        } else {
            return em.merge(entity);
        }
    }

    @Override
    @Transactional
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");
        List<S> result = new ArrayList<>();
        for (S entity : entities) {
            result.add(save(entity));
        }
        return result;
    }

    @Override
    public Optional<T> findById(ID id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        Class<T> domainType = getDomainClass();

        if (metadata == null) {
            return Optional.ofNullable(em.find(domainType, id));
        }

        LockModeType type = metadata.getLockModeType();

        Map<String, Object> hints = getQueryHints().withFetchGraphs(em).asMap();

        return Optional.ofNullable(type == null ? em.find(domainType, id, hints) : em.find(domainType, id, type, hints));
    }

    @Override
    @Transactional
    public void flush() {
        em.flush();
    }

    @Override
    @Transactional
    public <S extends T> S saveAndFlush(S entity) {
        S result = save(entity);
        flush();

        return result;
    }

    @Override
    @Transactional
    public void deleteInBatch(Iterable<T> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");
        if (!entities.iterator().hasNext()) {
            return;
        }
        applyAndBind(getQueryString(DELETE_ALL_QUERY_STRING, entityInformation.getEntityName()), entities, em)
                .executeUpdate();
    }

    @Override
    @Transactional
    public void deleteAllInBatch() {
        em.createQuery(getDeleteAllQueryString()).executeUpdate();
    }

    private String getDeleteAllQueryString() {
        return getQueryString(DELETE_ALL_QUERY_STRING, entityInformation.getEntityName());
    }

    @Override
    public T getOne(ID id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        return em.getReference(getDomainClass(), id);
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example) {
        return getQuery(new ExampleSpecification<>(example), example.getProbeType(), Sort.unsorted()).getResultList();
    }

    @Override
    public <S extends T> long count(Example<S> example) {
        return executeCountQuery(getCountQuery(new ExampleSpecification<>(example), example.getProbeType()));
    }

    protected Class<T> getDomainClass() {
        return entityInformation.getJavaType();
    }

    protected <S extends T> TypedQuery<S> getQuery(@Nullable Specification<S> spec, Class<S> domainClass,
                                                   Pageable pageable) {

        Sort sort = pageable.isPaged() ? pageable.getSort() : Sort.unsorted();
        return getQuery(spec, domainClass, sort);
    }

    protected <S extends T> TypedQuery<Long> getCountQuery(@Nullable Specification<S> spec, Class<S> domainClass) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);

        Root<S> root = applySpecificationToCriteria(spec, domainClass, query);

        if (query.isDistinct()) {
            query.select(builder.countDistinct(root));
        } else {
            query.select(builder.count(root));
        }

        // Remove all Orders the Specifications might have applied
        query.orderBy(Collections.emptyList());

        return em.createQuery(query);
    }

    private <S, U extends T> Root<U> applySpecificationToCriteria(@Nullable Specification<U> spec, Class<U> domainClass,
                                                                  CriteriaQuery<S> query) {

        Assert.notNull(domainClass, "Domain class must not be null!");
        Assert.notNull(query, "CriteriaQuery must not be null!");

        Root<U> root = query.from(domainClass);

        if (spec == null) {
            return root;
        }

        CriteriaBuilder builder = em.getCriteriaBuilder();
        Predicate predicate = spec.toPredicate(root, query, builder);

        if (predicate != null) {
            query.where(predicate);
        }

        return root;
    }

    /**
     * Creates a {@link TypedQuery} for the given {@link Specification} and {@link Sort}.
     *
     * @param spec can be {@literal null}.
     * @param sort must not be {@literal null}.
     * @return
     */
    protected TypedQuery<T> getQuery(@Nullable Specification<T> spec, Sort sort) {
        return getQuery(spec, getDomainClass(), sort);
    }

    /**
     * Creates a {@link TypedQuery} for the given {@link Specification} and {@link Sort}.
     *
     * @param spec        can be {@literal null}.
     * @param domainClass must not be {@literal null}.
     * @param sort        must not be {@literal null}.
     * @return
     */
    private <S extends T> TypedQuery<S> getQuery(@Nullable Specification<S> spec, Class<S> domainClass, Sort sort) {

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<S> query = builder.createQuery(domainClass);

        Root<S> root = applySpecificationToCriteria(spec, domainClass, query);
        query.select(root);

        if (sort.isSorted()) {
            query.orderBy(toOrders(sort, root, builder));
        }

        return applyRepositoryMethodMetadata(em.createQuery(query));
    }

    private <S> TypedQuery<S> applyRepositoryMethodMetadata(TypedQuery<S> query) {

        if (metadata == null) {
            return query;
        }

        LockModeType type = metadata.getLockModeType();
        TypedQuery<S> toReturn = type == null ? query : query.setLockMode(type);

        applyQueryHints(toReturn);

        return toReturn;
    }

    private void applyQueryHints(Query query) {

        for (Map.Entry<String, Object> hint : getQueryHints().withFetchGraphs(em)) {
            query.setHint(hint.getKey(), hint.getValue());
        }
    }

    private QueryHints getQueryHints() {
        return metadata == null ? QueryHints.NoHints.INSTANCE : DefaultQueryHints.of(entityInformation, metadata);
    }

    /**
     * Executes a count query and transparently sums up all values returned.
     *
     * @param query must not be {@literal null}.
     * @return
     */
    private static long executeCountQuery(TypedQuery<Long> query) {
        Assert.notNull(query, "TypedQuery must not be null!");
        List<Long> totals = query.getResultList();
        long total = 0L;
        for (Long element : totals) {
            total += element == null ? 0 : element;
        }
        return total;
    }


    @Override
    public <S extends T> Optional<S> findOne(Example<S> example) {
        try {
            return Optional.of(
                    getQuery(new ExampleSpecification<>(example), example.getProbeType(), Sort.unsorted()).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
        return getQuery(new ExampleSpecification<>(example), example.getProbeType(), sort).getResultList();
    }

    @Override
    public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
        ExampleSpecification<S> spec = new ExampleSpecification<>(example);
        Class<S> probeType = example.getProbeType();
        TypedQuery<S> query = getQuery(new ExampleSpecification<>(example), probeType, pageable);
        return readPage(query, probeType, pageable, spec);
    }

    private <S extends T> Page<S> readPage(TypedQuery<S> query, final Class<S> domainClass, Pageable pageable,
                                           @Nullable Specification<S> spec) {

        if (pageable.isPaged()) {
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }

        return PageableExecutionUtils.getPage(query.getResultList(), pageable,
                () -> executeCountQuery(getCountQuery(spec, domainClass)));
    }

    /**
     * 更新不为空的属性
     *
     * @param t t
     * @return T
     */
    @Override
    @Transactional
    public T updateByIdSelective(T t) {
        ID id = t.getId();
        AssertUtils.isTrueWithI18n(ObjectUtils.isNotEmpty(id), "update.id.notEmpty");
        T find = getOne(id);
        BeanUtils.copyNotNullProperties(t, find);
        return save(find);
    }

    @Override
    public QueryPage<T> queryForPage(ConditionQueryModel queryModel) {
        String entityName = entityInformation.getEntityName();
        List<Object> parameters = new ArrayList<>();
        String whereString = queryModel.getParam().toSqlString(parameters);
        StringBuilder hql = new StringBuilder("FROM ");
        StringBuilder sqlCount = new StringBuilder("SELECT COUNT(")
                .append(entityInformation.getRequiredIdAttribute().getName())
                .append(") FROM ")
                .append(entityName);
        hql.append(entityName);
        if (StringUtils.isNotEmpty(whereString)) {
            String where = HqlUtils.replaceHqlParameter(whereString);
            hql.append(" WHERE ").append(where);
            sqlCount.append(" WHERE ").append(where);
        }
        List<Order> orders = queryModel.getOrders();
        Set<String> fieldNames = FieldUtils.getAllFieldsList(entityInformation.getJavaType())
                .stream()
                .map(Field::getName)
                .collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(orders)) {
            Set<Order> orderList = orders.stream()
                    .filter(item -> fieldNames.contains(item.getField()))
                    .collect(Collectors.toSet());
            if (CollectionUtils.isNotEmpty(orderList)) {
                hql.append(OrderUtils.toOrderSql(orderList));
            }
        }
        TypedQuery<T> hqlQuery = em.createQuery(hql.toString(), entityInformation.getJavaType())
                .setFirstResult(queryModel.getStartRowIndex() * queryModel.getPageSize()) // 查询记录数
                .setMaxResults(queryModel.getPageSize());
        TypedQuery<Long> countQuery = em.createQuery(sqlCount.toString(), Long.class); // 分页查询
        TypedParameterValue parameterValue;
        for (int index = 0, size = parameters.size(); index < size; index++) {
            var value = parameters.get(index);
            parameterValue = new TypedParameterValue(TypeFactory.getType(value), value);
            hqlQuery.setParameter(index, parameterValue);
            countQuery.setParameter(index, parameterValue);
        }
        var count = countQuery.getSingleResult();
        List<T> result = (count == 0) ? new ArrayList<>() : hqlQuery.getResultList();//当查询记录为 0 时，不再需要查询数据集
        return new SimpleQueryPage<>(result, count, queryModel.getStartRowIndex(), queryModel.getPageSize());
    }


    @Override
    public boolean existsById(ID id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        if (entityInformation.getIdAttribute() == null) {
            return findById(id).isPresent();
        }
        String placeholder = provider.getCountQueryPlaceholder();
        String entityName = entityInformation.getEntityName();
        Iterable<String> idAttributeNames = entityInformation.getIdAttributeNames();
        String existsQuery = QueryUtils.getExistsQueryString(entityName, placeholder, idAttributeNames);
        TypedQuery<Long> query = em.createQuery(existsQuery, Long.class);
        if (!entityInformation.hasCompositeId()) {
            query.setParameter(idAttributeNames.iterator().next(), id);
            return query.getSingleResult() == 1L;
        }
        for (String idAttributeName : idAttributeNames) {
            Object idAttributeValue = entityInformation.getCompositeIdAttributeValue(id, idAttributeName);
            boolean complexIdParameterValueDiscovered = idAttributeValue != null
                    && !query.getParameter(idAttributeName).getParameterType().isAssignableFrom(idAttributeValue.getClass());
            if (complexIdParameterValueDiscovered) {
                // fall-back to findById(id) which does the proper mapping for the parameter.
                return findById(id).isPresent();
            }
            query.setParameter(idAttributeName, idAttributeValue);
        }
        return query.getSingleResult() == 1L;
    }

    @Override
    public void setRepositoryMethodMetadata(CrudMethodMetadata crudMethodMetadata) {
        this.metadata = crudMethodMetadata;
    }

    @Override
    public Optional<T> findOne(Specification<T> spec) {
        try {
            return Optional.of(getQuery(spec, Sort.unsorted()).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<T> findAll(Specification<T> spec) {
        return getQuery(spec, Sort.unsorted()).getResultList();
    }

    protected TypedQuery<T> getQuery(@Nullable Specification<T> spec, Pageable pageable) {

        Sort sort = pageable.isPaged() ? pageable.getSort() : Sort.unsorted();
        return getQuery(spec, getDomainClass(), sort);
    }

    @Override
    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        TypedQuery<T> query = getQuery(spec, pageable);
        return isUnpaged(pageable) ? new PageImpl<T>(query.getResultList())
                : readPage(query, getDomainClass(), pageable, spec);
    }

    private static boolean isUnpaged(Pageable pageable) {
        return pageable.isUnpaged();
    }

    @Override
    public List<T> findAll(Specification<T> spec, Sort sort) {
        return getQuery(spec, sort).getResultList();
    }

    @Override
    public long count(Specification<T> spec) {
        return executeCountQuery(getCountQuery(spec, getDomainClass()));
    }


    private static final class ByIdsSpecification<T> implements Specification<T> {

        private static final long serialVersionUID = 1L;

        private final JpaEntityInformation<T, ?> entityInformation;

        @Nullable
        ParameterExpression<Iterable> parameter;

        ByIdsSpecification(JpaEntityInformation<T, ?> entityInformation) {
            this.entityInformation = entityInformation;
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.data.jpa.domain.Specification#toPredicate(javax.persistence.criteria.Root, javax.persistence.criteria.CriteriaQuery, javax.persistence.criteria.CriteriaBuilder)
         */
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

            Path<?> path = root.get(entityInformation.getIdAttribute());
            parameter = cb.parameter(Iterable.class);
            return path.in(parameter);
        }
    }

    /**
     * {@link Specification} that gives access to the {@link Predicate} instance representing the values contained in the
     * {@link Example}.
     *
     * @param <T>
     * @author kevin
     * @since 1.10
     */
    @SuppressWarnings("serial")
    private static class ExampleSpecification<T> implements Specification<T> {

        private final Example<T> example;

        /**
         * Creates new {@link SimpleJpaRepository.ExampleSpecification}.
         *
         * @param example example
         */
        ExampleSpecification(Example<T> example) {
            Assert.notNull(example, "Example must not be null!");
            this.example = example;
        }

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return QueryByExamplePredicateBuilder.getPredicate(root, cb, example);
        }
    }
}
