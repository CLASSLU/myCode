package kd.idp.security.subProtect;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kd.idp.cms.bean.priv.UserBean;
import kd.idp.security.password.BeanMd5;

/**
 * PassVerify.java
 *
 * @author liming 2013-6-3
 *
 * (c)   Copyright 2013 PSIDP.KD Co,Ltd. All Rights Reserved
 * @version 1.0
 */
public class PassVerify extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public PassVerify() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 *
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request,response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 *
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=gb2312");
		PrintWriter out = response.getWriter();
		String pwd = request.getParameter("pwd");
		if(pwd == null){
			out.print(false);
		}
		pwd = BeanMd5.MD5HexEncode(pwd);

		UserBean user = (UserBean)request.getSession().getAttribute("guser");
		String password = user.getUserPwd();
		if(pwd.equalsIgnoreCase(password)){
			out.print(true);
			LogOper.insertInfoToTable(request, OperType.SUBPASS_VERIFY, true);
		}
		else{
			out.print(false);
			LogOper.insertInfoToTable(request, OperType.SUBPASS_VERIFY, false);
		}

	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}


