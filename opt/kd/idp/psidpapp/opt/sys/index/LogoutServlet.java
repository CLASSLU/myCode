package kd.idp.psidpapp.opt.sys.index;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import flex.messaging.FlexContext;
import flex.messaging.FlexSession;

import kd.idp.psidpapp.opt.sys.flex.FlexUtil;

public class LogoutServlet extends HttpServlet{
	/**
	 * –Ú¡–ªØ
	 */
	private static final long serialVersionUID = 1L;
	
	public void destroy() {
		super.destroy();
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		this.doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		try {
//			String userName = request.getParameter("USERNAME");
//			String passWord = request.getParameter("PASSWORD");
			FlexSession session = FlexContext.getFlexSession();
			if(session != null){
				session.removeAttribute("guser");
			}
			request.getSession().removeAttribute("guser");
			response.getWriter().print("TRUE");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void init() throws ServletException {
	}
	
}
