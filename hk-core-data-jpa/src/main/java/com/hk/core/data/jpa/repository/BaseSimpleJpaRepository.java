package com.hk.core.data.jpa.repository;

import com.hk.commons.util.AssertUtils;
import com.hk.commons.util.BeanUtils;
import com.hk.commons.util.ObjectUtils;
import com.hk.core.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;


/**
 * @author kevin
 * @date 2018-06-07 13:34
 * @see SimpleJpaRepository
 */
public class BaseSimpleJpaRepository<T extends Persistable<ID>, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseJpaRepository<T, ID> {

    /**
     * Creates a new {@link BaseSimpleJpaRepository} to manage objects of the given domain type.
     *
     * @param domainClass must not be {@literal null}.
     * @param em          must not be {@literal null}.
     */
    public BaseSimpleJpaRepository(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
    }

    public BaseSimpleJpaRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    @Override
    public <S extends T> boolean exists(Example<S> example) {
        return !getQuery(new ExampleSpecification<>(example), example.getProbeType(), Sort.unsorted()).getResultList()
                .isEmpty();
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example) {
        return getQuery(new ExampleSpecification<>(example), example.getProbeType(), Sort.unsorted()).getResultList();
    }

    @Override
    public <S extends T> long count(Example<S> example) {
        return executeCountQuery(getCountQuery(new ExampleSpecification<>(example), example.getProbeType()));
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

    /**
     * 更新不为空的属性
     *
     * @param t t
     * @return T
     */
    @Override
    public T updateByIdSelective(T t) {
        ID id = t.getId();
        AssertUtils.isTrueWithI18n(ObjectUtils.isNotEmpty(id), "update.id.notEmpty");
        T find = getOne(id);
        BeanUtils.copyNotNullProperties(t, find);
        return save(find);
    }


    @Override
    public boolean existsById(ID id) {
        return super.existsById(id);
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
