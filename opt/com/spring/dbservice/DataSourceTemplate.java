package com.spring.dbservice;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import com.spring.SpringBeanFactory;
import com.event.EventDispatcher;

public abstract class DataSourceTemplate extends EventDispatcher{

	public final static String defaultDataSourceName = "DMDataSource";//配置文件DataBaseConfiguration.xml里的数据库名

	/**
	 * @param dataSourceName
	 * @return
	 */
	protected JdbcTemplate getJdbcTemplate(String dataSourceName) {
		try {
			System.out.println("----------------初始化数据库连接------------------");
			DataSource dataSource = (DataSource) SpringBeanFactory
					.getInstance().getBean(dataSourceName);
			return new JdbcTemplate(dataSource);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param dataSourceName
	 */
	protected void resetDataSource(String dataSourceName){
		SpringBeanFactory.getInstance().refreshBean(dataSourceName);
	}

}
