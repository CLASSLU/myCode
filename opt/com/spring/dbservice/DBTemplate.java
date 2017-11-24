package com.spring.dbservice;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.event.BaseEvent;
import com.event.IEventCallBack;
import com.spring.mapper.HashMapRowMapper;

public class DBTemplate extends DataSourceTemplate {

	private String dataSourceName = null;

	private JdbcTemplate jdbcTemplate = null;

	/**
	 * 私有构造函数
	 */
	private DBTemplate() {
		System.out.println("初始化 DBTemplate " + this);
		dataSourceName = defaultDataSourceName;
		jdbcTemplate = super.getJdbcTemplate(dataSourceName);
		bindEvent();
	}

	/**
	 * 私有构造函数
	 */
	private DBTemplate(String _dataSourceName) {
		System.out.println("初始化 DBTemplate " + this);
		if (_dataSourceName == null) {
			dataSourceName = defaultDataSourceName;
		} else {
			dataSourceName = _dataSourceName;
		}
		jdbcTemplate = super.getJdbcTemplate(dataSourceName);
		bindEvent();//事件绑定
	}

	/**
	 * 唯一的类实例
	 */
	private static Map<String, DBTemplate> dbMap = new HashMap<String, DBTemplate>();

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static DBTemplate getInstance() {
		DBTemplate db = dbMap.get(defaultDataSourceName);
		if (db == null) {
			db = new DBTemplate();
			dbMap.put(defaultDataSourceName, db);
		}
		return db;
	}

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static DBTemplate getInstance(String dataSource) {
		DBTemplate db = dbMap.get(dataSource);
		if (db == null) {
			db = new DBTemplate(dataSource);
			dbMap.put(dataSource, db);
		}
		return db;
	}

	/**
	 * 事件绑定
	 */
	public void bindEvent() {
		this.addEventListener(
				CanneoGetConnectionEvent.CANNOT_GET_CONNECTION_EVENT,
				new IEventCallBack() {

					public void callBack(BaseEvent event) {
						((CanneoGetConnectionEvent) event).getDb()
								.resetDBTemplate();
						throw new RuntimeException(
								((CanneoGetConnectionEvent) event).getE());
					}

				}, true);
	}

	/**
	 * 数据库重新连接
	 */
	public void resetDBTemplate() {
		dbMap.remove(dataSourceName);
		super.resetDataSource(dataSourceName);
		DBTemplate.getInstance(dataSourceName);
	}

	/*
	 * 
	 * DBSource相关
	 * 
	 */

	public Connection getDBConnection() throws SQLException {
		return jdbcTemplate.getDataSource().getConnection();
	}

	/*
	 * 查询操作
	 */

	/**
	 * 获得一条查询结果的 map 对象
	 * 
	 * @param sql
	 * @return
	 */
	public Map<String, Object> getResultMap(String sql) {
		try {
			return jdbcTemplate.queryForMap(sql);
		} catch (CannotGetJdbcConnectionException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (DataAccessResourceFailureException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (EmptyResultDataAccessException e) {
			return new HashMap<String, Object>();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new HashMap<String, Object>();
	}

	/**
	 * 获得一条查询结果的 bean 对象
	 * 
	 * @param <T>
	 * @param sql
	 * @param rowMapper
	 * @return
	 */
	public <T> T getResultRowMapper(String sql, RowMapper<T> rowMapper) {
		List<T> list = new ArrayList<T>();
		try {
			list = jdbcTemplate.query(sql, rowMapper);
		} catch (CannotGetJdbcConnectionException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (DataAccessResourceFailureException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 将查询结果 封装为 map 集合
	 * 
	 * @param sql
	 * @return
	 */
	public List<Map<String, Object>> getResultMapList(String sql) {
		try {
			return jdbcTemplate.query(sql, new HashMapRowMapper());
		} catch (CannotGetJdbcConnectionException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (DataAccessResourceFailureException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new ArrayList<Map<String, Object>>();
	}

	/**
	 * @param <T>
	 * @param sql
	 * @param keyColumn
	 * @param T
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getResultList(String sql, final String keyColumn,
			Class<T> T) {
		try {
			return jdbcTemplate.query(sql, new RowMapper<T>() {

				public T mapRow(ResultSet rs, int rowNum) throws SQLException {
					return (T) rs.getObject(keyColumn);
				}
			});
		} catch (CannotGetJdbcConnectionException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (DataAccessResourceFailureException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new ArrayList<T>();
	}

	/**
	 * 将查询结果 封装为自定义 bean 集合
	 * 
	 * @param <T>
	 * @param sql
	 * @param rowMapper
	 * @return
	 */
	public <T> List<T> getResultRowMapperList(String sql, RowMapper<T> rowMapper) {
		try {
			return jdbcTemplate.query(sql, rowMapper);
		} catch (CannotGetJdbcConnectionException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (DataAccessResourceFailureException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new ArrayList<T>();
	}

	public <T> Map<String, T> getResultMap(String sql, final String keyColumn,
			final RowMapper<T> rowMapper) {
		final Map<String, T> map = new HashMap<String, T>();
		try {
			jdbcTemplate.query(sql, new RowMapper<Map<String, T>>() {

				public Map<String, T> mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					map.put(String.valueOf(rs.getObject(keyColumn)), rowMapper
							.mapRow(rs, rowNum));
					return null;
				}

			});
		} catch (CannotGetJdbcConnectionException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (DataAccessResourceFailureException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return map;
	}

	/**
	 * 获得 result 方法
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public ResultSet getResultSet(String sql) throws SQLException {
		Connection conn = null;
		Statement st = null;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			st = conn.createStatement();
			return st.executeQuery(sql);
		} catch (CannotGetJdbcConnectionException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (DataAccessResourceFailureException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (st != null) {
				st.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return null;
	}

	/**
	 * @param sql
	 * @return
	 */
	public int getCount(String sql) throws RuntimeException {
		try {
			return jdbcTemplate.queryForInt(sql);
		} catch (CannotGetJdbcConnectionException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (DataAccessResourceFailureException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return -1;
	}

	/**
	 * @param sql
	 * @param args
	 * @return
	 */
	public int queryForInt(String sql, Object... args) {
		try {
			return jdbcTemplate.queryForInt(sql, args);
		} catch (CannotGetJdbcConnectionException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (DataAccessResourceFailureException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return -1;
	}

	/**
	 * @param <T>
	 * @param sql
	 * @param args
	 * @param T
	 * @return
	 */
	public <T> List<T> query(String sql, Object[] args, RowMapper<T> T) {
		try {
			return jdbcTemplate.query(sql, args, T);
		} catch (CannotGetJdbcConnectionException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (DataAccessResourceFailureException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new ArrayList<T>();
	}

	/**
	 * @param <T>
	 * @param sql
	 * @param T
	 * @return
	 */
	public <T> List<T> query(String sql, RowMapper<T> T) {
		try {
			return jdbcTemplate.query(sql, T);
		} catch (CannotGetJdbcConnectionException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (DataAccessResourceFailureException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new ArrayList<T>();
	}

	/**
	 * @param sql
	 * @return
	 */
	public SqlRowSet queryForRowSet(String sql) {
		try {
			return jdbcTemplate.queryForRowSet(sql);
		} catch (CannotGetJdbcConnectionException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (DataAccessResourceFailureException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	/*
	 * 执行操作
	 */

	/**
	 * 执行SQL
	 * 
	 * @param sql
	 * @return
	 * @throws RuntimeException
	 */
	public void executeSql(String sql) throws RuntimeException {
		try {
			jdbcTemplate.execute(sql);
		} catch (CannotGetJdbcConnectionException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (DataAccessResourceFailureException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 批量执行sqls 带事务
	 * 
	 * @param sqls
	 * @return
	 * @throws RuntimeException
	 */
	public int batchExecuteWithTransaction(final String[] sqls)
			throws RuntimeException {
		TransactionStatus txStatus = TransactionUtils
				.getTransaction(dataSourceName);
		PlatformTransactionManager txManager = TransactionUtils
				.getTransactionManager(dataSourceName);
		try {
			jdbcTemplate.batchUpdate(sqls);
			txManager.commit(txStatus);
		} catch (Exception e) {
			txManager.rollback(txStatus);
			throw new RuntimeException(e);
		}
		return sqls.length;
	}
	/**
	 * 批量执行sqls 带事务
	 * 
	 * @param sqls
	 * @return
	 * @throws RuntimeException
	 */
	public int batchExecuteWithTransaction(List<String> list)
			throws RuntimeException {
		String []sqls = new String[list.size()];
		sqls = list.toArray(sqls);
		return batchExecuteWithTransaction(sqls);
	}
	/**
	 * 更新SQL
	 * 
	 * @param sql
	 * @return
	 * @throws RuntimeException
	 */
	public int updateSql(String sql) throws RuntimeException {
		try {
			return jdbcTemplate.update(sql);
		} catch (CannotGetJdbcConnectionException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (DataAccessResourceFailureException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return -1;
	}

	/**
	 * 批量更新 SQL
	 * 
	 * @param sqls
	 * @return
	 * @throws RuntimeException
	 */
	public int[] batchUpdateSql(String[] sqls) throws RuntimeException {
		try {
			return jdbcTemplate.batchUpdate(sqls);
		} catch (CannotGetJdbcConnectionException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (DataAccessResourceFailureException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new int[0];
	}
	
	public int[] batchUpdateSql(List<String> list) throws RuntimeException {
		String [] sqls = new String[list.size()];
		return batchUpdateSql(list.toArray(sqls));
	}

	public int preUpdateSql(String sql, Object[] params)
			throws RuntimeException {
		try {
			return jdbcTemplate.update(sql, params);
		} catch (CannotGetJdbcConnectionException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (DataAccessResourceFailureException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return -1;
	}
	
	public int[] batchUpdate(String sql, List<Object[]> batchArgs){
		try {
			return jdbcTemplate.batchUpdate(sql, batchArgs);
		} catch (CannotGetJdbcConnectionException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (DataAccessResourceFailureException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new int[0];
	}

	/**
	 * 批量更新 SQL 附有事务功能
	 * 
	 * @param sqls
	 * @return
	 * @throws RuntimeException
	 */
	public int batchUpdateWithTransaction(final String[] sqls)
			throws RuntimeException {
		try {
			TransactionUtils.transactionExecute(dataSourceName,
					new TransactionCallback<Object>() {

						public Object doInTransaction(TransactionStatus arg0) {
							jdbcTemplate.batchUpdate(sqls);
							return null;
						}

					});
			return sqls.length;
		} catch (CannotGetJdbcConnectionException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (DataAccessResourceFailureException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return -1;
	}

	/**
	 * 带参数更新SQL
	 * 
	 * @param sql
	 * @param param
	 * @return
	 */
	public int updateSqlWithParam(String sql, Object[] param)
			throws RuntimeException {
		try {
			return jdbcTemplate.update(sql, param);
		} catch (CannotGetJdbcConnectionException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (DataAccessResourceFailureException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return -1;
	}

	/**
	 * 批量更新
	 * 
	 * @param <T>
	 * @param sql
	 * @param dataList
	 * @return
	 * @throws RuntimeException
	 */
	public <T> int[] batchPreparedUpdate(String sql,
			final List<List<T>> dataList) throws RuntimeException {
		try {
			return jdbcTemplate.batchUpdate(sql,
					new BatchPreparedStatementSetter() {

						public int getBatchSize() {
							return dataList.size();
						}

						public void setValues(PreparedStatement ps, int index)
								throws SQLException {
							List<T> row = dataList.get(index);
							if (row != null && row.size() > 0) {
								for (int i = 0; i < row.size(); i++) {
									T value = row.get(i);
									if (value instanceof String) {
										ps.setString(i + 1, String
												.valueOf(value));
									} else if (value instanceof Integer) {
										ps.setInt(i + 1,
												Integer.parseInt(String
														.valueOf(value)));
									} else if (value instanceof BigDecimal) {
										ps.setInt(i + 1,
												Integer.parseInt(String
														.valueOf(value)));
									} else if (value instanceof Timestamp) {
										ps
												.setTimestamp(
														i + 1,
														Timestamp
																.valueOf(String
																		.valueOf(value)));
									} else if (value == null) {
										ps.setNull(i + 1, ps
												.getParameterMetaData()
												.getParameterType(i));
									}
								}
							}
						}

					});
		} catch (CannotGetJdbcConnectionException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (DataAccessResourceFailureException e) {
			dispatchEvent(new CanneoGetConnectionEvent(this, e));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new int[0];
	}

	@Override
	protected JdbcTemplate getJdbcTemplate(String dataSourceName) {
		return null;
	}

	@Override
	protected void resetDataSource(String dataSourceName) {

	}

}
