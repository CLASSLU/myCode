package com.spring.dbservice;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.spring.ServiceManager;
import com.spring.SpringBeanFactory;

public abstract class TransactionUtils {

	private static TransactionDefinition txDefinition;

	private static Map<String, PlatformTransactionManager> txMap = new HashMap<String, PlatformTransactionManager>();

	/**
	 * 获得事务管理器
	 * 
	 * @param dataSourceName
	 * @return
	 */
	public static PlatformTransactionManager getTransactionManager(
			String dataSourceName) {
		try {
			DataSource dataSource = (DataSource) ServiceManager
					.getServiceBean(dataSourceName);
			PlatformTransactionManager tm = txMap.get(dataSourceName);
			if (tm == null) {
				tm = new DataSourceTransactionManager(dataSource);
				txMap.put(dataSourceName, tm);
			}
			return tm;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param dataSourceName
	 */
	private static void resetTransactionManager(String dataSourceName) {
		txMap.remove(dataSourceName);
		SpringBeanFactory.getInstance().refreshBean(dataSourceName);
		getTransactionManager(dataSourceName);
	}

	/**
	 * 获得默认事务管理器
	 * 
	 * @param dataSourceName
	 * @return
	 */
	public static PlatformTransactionManager getDefaultTransactionManager() {
		return getTransactionManager(DataSourceTemplate.defaultDataSourceName);
	}

	/**
	 * 默认事务执行模板
	 * 
	 * @param action
	 */
	public static <T> T transactionExecute(TransactionCallback<T> action) {
		try {
			return getTransactionTemplate(
					DataSourceTemplate.defaultDataSourceName,
					TransactionDefinition.PROPAGATION_REQUIRED,
					TransactionDefinition.ISOLATION_DEFAULT).execute(action);
		} catch (CannotCreateTransactionException e) {
			resetTransactionManager(DataSourceTemplate.defaultDataSourceName);
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 事务执行模板
	 * 
	 * @param dataSourceName
	 * @param action
	 */
	public static <T> T transactionExecute(String dataSourceName,
			TransactionCallback<T> action) {
		try {
			return getTransactionTemplate(dataSourceName,
					TransactionDefinition.PROPAGATION_REQUIRED,
					TransactionDefinition.ISOLATION_DEFAULT).execute(action);
		} catch (CannotCreateTransactionException e) {
			resetTransactionManager(dataSourceName);
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * 获得事务状态
	 * 
	 * @param dataSourceName
	 * @return
	 */
	public static TransactionStatus getTransaction(String dataSourceName) {
		return getTransactionManager(dataSourceName).getTransaction(
				getDefaultTransactionDefinition());
	}

	/**
	 * 获得默认事务状态
	 * 
	 * @return
	 */
	public static TransactionStatus getDefaultTransaction() {
		return getDefaultTransactionManager().getTransaction(
				getDefaultTransactionDefinition());
	}

	/**
	 * 获得事务模板
	 * 
	 * @param dataSourceName
	 * @param propagationBehavior
	 * @param isolationLevel
	 * @return
	 */
	private static TransactionTemplate getTransactionTemplate(
			String dataSourceName, int propagationBehavior, int isolationLevel) {
		TransactionTemplate transactionTemplate = new TransactionTemplate(
				getTransactionManager(dataSourceName));
		transactionTemplate.setPropagationBehavior(propagationBehavior);
		transactionTemplate.setIsolationLevel(isolationLevel);
		return transactionTemplate;
	}

	/**
	 * 获得默认事务定义
	 * 
	 * @return
	 */
	private static TransactionDefinition getDefaultTransactionDefinition() {
		if (txDefinition == null) {
			txDefinition = new DefaultTransactionDefinition(
					TransactionDefinition.PROPAGATION_REQUIRED);
		}
		return txDefinition;
	}

}
