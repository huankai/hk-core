package com.hk.core.service.impl;

import com.hk.core.authentication.api.SecurityContext;
import com.hk.core.authentication.api.UserPrincipal;
import com.hk.core.query.JpaQueryModel;
import com.hk.core.query.QueryModel;
import com.hk.core.query.QueryPageable;
import com.hk.core.query.SimpleQueryResult;
import com.hk.core.repository.BaseRepository;
import com.hk.core.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * 基本Service CRUD操作
 * 
 * @author huangkai
 * @date 2017年9月27日下午2:16:24
 * @param <T>
 */
@Transactional(readOnly = true)
public abstract class BaseServiceImpl<T, PK extends Serializable> implements BaseService<T, PK> {

	/**
	 * 
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 返回 BaseRepository
	 * 
	 * @return
	 */
	protected abstract BaseRepository<T, PK> getBaseRepository();

	/**
	 * 获取登陆的用户信息
	 */
	@Autowired
	protected SecurityContext securityContext;

	/**
	 * 获取当前登陆的用户
	 * 
	 * @return
	 */
	protected final UserPrincipal getUserPrincipal() {
		return securityContext.getPrincipal();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hk.core.service.BaseService#saveOrUpdate(java.lang.Object)
	 */
	@Override
	@Transactional(readOnly = false)
	public <S extends T> S saveOrUpdate(S entity) {
		return getBaseRepository().save(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hk.core.service.BaseService#saveOrUpdate(java.lang.Iterable)
	 */
	@Override
	@Transactional(readOnly = false)
	public <S extends T> List<S> saveOrUpdate(Iterable<S> entities) {
		return getBaseRepository().save(entities);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hk.core.service.BaseService#saveAndFlush(java.lang.Object)
	 */
	@Override
	@Transactional(readOnly = false)
	public <S extends T> S saveAndFlush(S entity) {
		return getBaseRepository().saveAndFlush(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hk.core.service.BaseService#findOne(java.io.Serializable)
	 */
	@Override
	public final T findOne(PK id) {
		return getBaseRepository().findOne(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hk.core.service.BaseService#findOne(java.lang.Object)
	 */
	@Override
	public <S extends T> S findOne(S t) {
		return getBaseRepository().findOne(getExample(t));
	}

	@Override
	public <S extends T> List<S> findAll(S t) {
		return getBaseRepository().findAll(getExample(t));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hk.core.service.BaseService#getOne(java.io.Serializable)
	 */
	@Override
	public T getOne(PK id) {
		return getBaseRepository().getOne(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hk.core.service.BaseService#flush()
	 */
	@Override
	@Transactional
	public void flush() {
		getBaseRepository().flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hk.core.service.BaseService#exists(java.io.Serializable)
	 */
	@Override
	public final boolean exists(PK id) {
		return getBaseRepository().exists(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hk.core.service.BaseService#findAll()
	 */
	@Override
	public final List<T> findAll() {
		return getBaseRepository().findAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hk.core.service.BaseService#findAll(java.lang.Iterable)
	 */
	@Override
	public final List<T> findAll(Iterable<PK> ids) {
		return getBaseRepository().findAll(ids);
	}

	/**
	 * 
	 * @param t
	 * @return
	 */
	protected <S extends T> Example<S> getExample(S t) {
		return Example.of(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hk.core.service.BaseService#queryForPage(com.hk.core.query.QueryModel)
	 */
	@Override
	public QueryPageable<T> queryForPage(QueryModel query) {
		Page<T> page = getBaseRepository().findAll(
				new PageRequest(query.getJpaStartRowIndex(), query.getPageSize(), new Sort(query.getSortOrderList())));
		return new SimpleQueryResult<>(query, page.getTotalElements(), page.getContent());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hk.core.service.BaseService#queryForPage(com.hk.core.query.JpaQueryModel)
	 */
	@Override
	public QueryPageable<T> queryForPage(JpaQueryModel<T> query) {
		Page<T> page = getBaseRepository().findAll(getExample(query.getParams()),
				new PageRequest(query.getJpaStartRowIndex(), query.getPageSize(), new Sort(query.getSortOrderList())));
		return new SimpleQueryResult<>(query, page.getTotalElements(), page.getContent());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hk.core.service.BaseService#count()
	 */
	@Override
	public final long count() {
		return getBaseRepository().count();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hk.core.service.BaseService#count(java.lang.Object)
	 */
	@Override
	public long count(T t) {
		return getBaseRepository().count(getExample(t));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hk.core.service.BaseService#delete(java.io.Serializable)
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(PK id) {
		getBaseRepository().delete(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hk.core.service.BaseService#delete(java.lang.Object)
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(T entity) {
		getBaseRepository().delete(entity);
	}

	/*
	 * 
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(Iterable<? extends T> entities) {
		getBaseRepository().delete(entities);
	}

}
