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

	private static Log log = LogFactory.getLog(SpringBeanFactory.class);//������

	/**
	 * ˽�й��캯��
	 */
	private SpringBeanFactory() {
		super();
	}

	/**
	 * Ψһ����ʵ��
	 */
	private static SpringBeanFactory ctx = null;

	/**
	 * ��ȡʵ��
	 * 
	 * @return
	 */
	public static SpringBeanFactory getInstance() {
		if (log.isDebugEnabled()) {
			Date date = new Date();
			date.setTime(System.currentTimeMillis());
			log.debug("����SpringBeanFactory��ȡʵ������,ʱ��:" + date.toString());
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
	 * ��ʼ��bean���� bean�����ļ������� springBeanConfiguration.xml
	 * 
	 * @return
	 */
	public boolean init() {
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(ctx);//???
		try {
			long startTime = System.currentTimeMillis();
			/**
			 * ����spring����
			 */
			reader.loadBeanDefinitions(new ClassPathResource(
					"applicationContext.xml"));
			ctx.refresh();
			long endTime = System.currentTimeMillis();
			long duration = endTime - startTime;
			if (log.isDebugEnabled()) {
				log.debug("Spring��ʼ�����,����ʱ��:" + duration + "ms");
			}
		} catch (BeanDefinitionStoreException e) {
			e.printStackTrace();
			log.error("��ʼ��Spring bean����ʧ��");
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
			log.debug("Spring�����ر�,ʱ��:" + date.toString());
		}
	}
	
	
	public void refreshBean(String beanName){
		log.debug("ˢ�� Spring bean : " + beanName+"����");
		BeanDefinition beanDefinition = ctx.getBeanDefinition(beanName);
		ctx.registerBeanDefinition(beanName, beanDefinition);
	}

}
