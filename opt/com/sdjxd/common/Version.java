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
		event.getServletContext().log("����ϵͳ�汾��");

		CommDao commdao = new CommDao();
		commdao.setVersion();

	}

	public void contextDestroyed(ServletContextEvent event) {
	}
}
