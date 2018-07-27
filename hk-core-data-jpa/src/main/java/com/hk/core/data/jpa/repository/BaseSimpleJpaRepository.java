package com.hk.core.data.jpa.repository;

import com.hk.core.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;


/**
 * @author: kevin
 * @date 2018-06-07 13:34
 * @see SimpleJpaRepository
 */
public class BaseSimpleJpaRepository<T extends Persistable<ID>, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {


    /**
     * Creates a new {@link SimpleJpaRepository} to manage objects of the given domain type.
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

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.query.QueryByExampleExecutor#exists(org.springframework.data.domain.Example)
     */
    @Override
    public <S extends T> boolean exists(Example<S> example) {
        return !getQuery(new ExampleSpecification<>(example), example.getProbeType(), (Sort) null).getResultList()
                .isEmpty();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.query.QueryByExampleExecutor#findAll(org.springframework.data.domain.Example)
     */
    @Override
    public <S extends T> List<S> findAll(Example<S> example) {
        return getQuery(new ExampleSpecification<>(example), example.getProbeType(), (Sort) null).getResultList();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.query.QueryByExampleExecutor#findAll(org.springframework.data.domain.Example, org.springframework.data.domain.Sort)
     */
    @Override
    public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
        return getQuery(new ExampleSpecification<>(example), example.getProbeType(), sort).getResultList();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.query.QueryByExampleExecutor#findAll(org.springframework.data.domain.Example, org.springframework.data.domain.Pageable)
     */
    @Override
    public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
        ExampleSpecification<S> spec = new ExampleSpecification<>(example);
        Class<S> probeType = example.getProbeType();
        TypedQuery<S> query = getQuery(new ExampleSpecification<>(example), probeType, pageable);
        return pageable == null ? new PageImpl<>(query.getResultList()) : readPage(query, probeType, pageable, spec);
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
     * @author Christoph Strobl
     * @since 1.10
     */
    private static class ExampleSpecification<T> implements Specification<T> {

        private final Example<T> example;

        /**
         * Creates new {@link SimpleJpaRepository.ExampleSpecification}.
         *
         * @param example
         */
        public ExampleSpecification(Example<T> example) {
            Assert.notNull(example, "Example must not be null!");
            this.example = example;
        }

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return QueryByExamplePredicateBuilder.getPredicate(root, cb, example);
        }
    }
}
