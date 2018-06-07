package com.hk.core.repository.impl;

/**
 * 基本Dao类
 * 
 * @author huangkai
 *
 */
public abstract class BaseDaoImpl {

//	/**
//	 * EntityManager
//	 */
//	@PersistenceContext
//	private EntityManager entityManager;
//
//	/**
//	 * JDBC Session
//	 */
//	@Autowired
//	private JdbcSession jdbcSession;
//
//	protected EntityManager getEntityManager() {
//		return entityManager;
//	}
//
//	/**
//	 * 查询集合，返回Map
//	 *
//	 * @param arguments
//	 *            查询参数
//	 * @param retriveRowCount
//	 *            是否进行 Count查询 ，如果为false,返回的长度就是集合的长度，如果为true，会使用count查询一次
//	 * @return
//	 */
//	public ListResult<Map<String, Object>> queryForList(SelectArguments arguments, boolean retriveRowCount) {
//		return jdbcSession.queryForList(arguments, retriveRowCount);
//	}
//
//	/**
//	 * 查询集合，返回对象
//	 *
//	 * @param arguments
//	 *            查询参数
//	 * @param retriveRowCount
//	 *            是否进行 Count查询 ，如果为false,返回的长度就是集合的长度，如果为true，会使用count查询一次
//	 * @param clazz
//	 *            返回的对象类型
//	 * @return
//	 */
//	public <T> ListResult<T> queryForList(SelectArguments arguments, boolean retriveRowCount, Class<T> clazz) {
//		return jdbcSession.queryForList(arguments, retriveRowCount, clazz);
//	}
//
//	/**
//	 * 查询唯一
//	 *
//	 * @param arguments
//	 *            查询参数
//	 * @param clazz
//	 *            返回的对象类型
//	 * @return
//	 */
//	public final <T> T queryForObject(SelectArguments arguments, Class<T> clazz) {
//		return jdbcSession.queryForObject(arguments, clazz);
//	}

}
