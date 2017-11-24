package com.spring;

import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SpringContextLoaderListener implements ServletContextListener {

	public static int num = 0;
	
	/**
	 * 守护进程timer
	 */
	Timer timer = new Timer();

	public void contextDestroyed(ServletContextEvent arg0) {
		timer.cancel();
	}

	public void contextInitialized(ServletContextEvent contextEvent) {
		/**
		 * 初始化Spring
		 */
		SpringBeanFactory ctx = SpringBeanFactory.getInstance();
		if (ctx == null) {
			System.out.println("初始化Spring Bean定义失败");
		}
	}

}
