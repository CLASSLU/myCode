package com.spring.dbservice;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.spring.SpringBeanFactory;

public class DataSourceUtil {

	private final static String defaultDataSourceName = "DMDataSource";
	
	private static Map<Integer, JdbcTemplate> templateMap = new HashMap<Integer, JdbcTemplate>();
	
	public static JdbcTemplate getJdbcTemplate(){
		try {
			DataSource dataSource = (DataSource)SpringBeanFactory.getInstance().getBean(defaultDataSourceName);
			JdbcTemplate jt = templateMap.get(dataSource.hashCode());
			if(jt == null){
				jt = new JdbcTemplate(dataSource);
				templateMap.put(dataSource.hashCode(), jt);
			}
			return jt;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static JdbcTemplate getJdbcTemplate(DataSource dataSource){
		try {
			if(dataSource == null){
				dataSource = (DataSource)SpringBeanFactory.getInstance().getBean(defaultDataSourceName);
			}
			JdbcTemplate jt = templateMap.get(dataSource.hashCode());
			if(jt == null){
				jt = new JdbcTemplate(dataSource);
				templateMap.put(dataSource.hashCode(), jt);
			}
			return jt;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static JdbcTemplate getJdbcTemplate(String dataSourceName){
		try {
			if(dataSourceName == null){
				dataSourceName = defaultDataSourceName;
			}
			DataSource dataSource = (DataSource)SpringBeanFactory.getInstance().getBean(dataSourceName);
			JdbcTemplate jt = templateMap.get(dataSource.hashCode());
			if(jt == null){
				jt = new JdbcTemplate(dataSource);
				templateMap.put(dataSource.hashCode(), jt);
			}
			return jt;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
