package com.sdjxd.common.web.servlte;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.RowSet;

import com.sdjxd.pms.platform.data.DbOper;

public class UserSelServlet extends HttpServlet {
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		// 接收从客户端提交的loginName参数
		String deptId = request.getParameter("deptId");

		String reValue = "";
		String sql = "SELECT USERCODE,USERNAME FROM [S].JXD7_XT_USER WHERE DEPTID = '"
				+ deptId + "' AND DATASTATUSID = 1  ORDER BY SHOWORDER";

		System.out.println(sql);
		RowSet rs = null;
		try {
			rs = DbOper.executeQuery(sql);

			while (rs.next()) {
				if (reValue.length() != 0) {
					reValue += ",";
				}
				reValue += "['" + rs.getString("USERCODE") + "','"
						+ rs.getString("USERNAME") + "']";
			}
		} catch (SQLException exp) {
			exp.printStackTrace();
		}

		if (reValue.length() != 0) {
			reValue = "[" + reValue + "]";
		}
		// 将处理结果返回客户端
		out.print(reValue);
		out.flush();
		out.close();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#destroy()
	 */
	@Override
	public void destroy() {
		super.destroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() throws ServletException {
		super.init();
	}

	private static final long serialVersionUID = 1L;
}
