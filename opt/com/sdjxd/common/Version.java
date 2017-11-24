package com.sdjxd.common;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

public class Version extends HttpServlet implements ServletContextListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1661766562820056460L;

	public Version() {
	}

	public void contextInitialized(ServletContextEvent event) {
		event.getServletContext().log("更新系统版本号");

		CommDao commdao = new CommDao();
		commdao.setVersion();

	}

	public void contextDestroyed(ServletContextEvent event) {
	}
}
