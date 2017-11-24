package com.spring;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SpringContextLoaderListener implements ServletContextListener {

	public static int num = 0;
	
	/**
	 * �ػ�����timer
	 */
	Timer timer = new Timer();

	public void contextDestroyed(ServletContextEvent arg0) {
		timer.cancel();
	}

	public void contextInitialized(ServletContextEvent contextEvent) {
		/**
		 * ��ʼ��Spring
		 */
		SpringBeanFactory ctx = SpringBeanFactory.getInstance();
		if (ctx == null) {
			System.out.println("��ʼ��Spring Bean����ʧ��");
		}
	}

}
