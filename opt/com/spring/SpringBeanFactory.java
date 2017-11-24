package com.spring;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class SpringBeanFactory extends GenericApplicationContext {

	private static Log log = LogFactory.getLog(SpringBeanFactory.class);//？？？

	/**
	 * 私有构造函数
	 */
	private SpringBeanFactory() {
		super();
	}

	/**
	 * 唯一的类实例
	 */
	private static SpringBeanFactory ctx = null;

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static SpringBeanFactory getInstance() {
		if (log.isDebugEnabled()) {
			Date date = new Date();
			date.setTime(System.currentTimeMillis());
			log.debug("访问SpringBeanFactory获取实例对象,时间:" + date.toString());
		}
		if (ctx != null) {
			return ctx;
		} else {
			ctx = new SpringBeanFactory();
			if (ctx.init()) {
				return ctx;
			} else {
				ctx.close();
				ctx = null;
				return ctx;
			}
		}
	}

	/**
	 * 初始化bean定义 bean定义文件包括： springBeanConfiguration.xml
	 * 
	 * @return
	 */
	public boolean init() {
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(ctx);//???
		try {
			long startTime = System.currentTimeMillis();
			/**
			 * 加载spring设置
			 */
			reader.loadBeanDefinitions(new ClassPathResource(
					"applicationContext.xml"));
			ctx.refresh();
			long endTime = System.currentTimeMillis();
			long duration = endTime - startTime;
			if (log.isDebugEnabled()) {
				log.debug("Spring初始化完成,消耗时间:" + duration + "ms");
			}
		} catch (BeanDefinitionStoreException e) {
			e.printStackTrace();
			log.error("初始化Spring bean定义失败");
			return false;
		}
		return true;
	}

	@Override
	public void close() {
		super.close();
		if (log.isDebugEnabled()) {
			Date date = new Date();
			date.setTime(System.currentTimeMillis());
			log.debug("Spring工厂关闭,时间:" + date.toString());
		}
	}
	
	
	public void refreshBean(String beanName){
		log.debug("刷新 Spring bean : " + beanName+"对象");
		BeanDefinition beanDefinition = ctx.getBeanDefinition(beanName);
		ctx.registerBeanDefinition(beanName, beanDefinition);
	}

}
