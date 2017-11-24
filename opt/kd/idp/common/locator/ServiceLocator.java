package kd.idp.common.locator;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kd.idp.common.locator.reflect.CreateJSONTools;
import kd.idp.common.locator.reflect.ParserJSONTools;
import kd.idp.common.locator.reflect.ReflectService;

/**
 * 
 * ǰ��̨ ���� ������
 * 
 * @author caoyu
 *
 */
public class ServiceLocator extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4206208138056924401L;

	/**
	 * Constructor of the object.
	 */
	public ServiceLocator() {
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

		doPost(request, response);
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

		response.setContentType("text/html;charset=gbk");
		PrintWriter out = response.getWriter();
		
		String checkcode = request.getParameter("checkcode");
		
		if("locator".equals(checkcode)){
			//���� ����
			String className = request.getParameter("className"); 
			//���� ������
			String funcName = request.getParameter("funcName");
			//�����б� json
			String params = request.getParameter("params");
			
			System.out.println("ǰ̨����: "+params);
			
			//���� ����
			ReflectService rs = new ReflectService(className,funcName);
			//����json
			ParserJSONTools jt = new ParserJSONTools(params);
			rs.setParamValues(jt.parserJSON(),request,response);
			//���÷��� ���ؽ��
			Object result = rs.invokeMethod();
			//����� ��֯��json
			String jsonstr = CreateJSONTools.toJSONString(result);
			
			System.out.println("��̨����: "+jsonstr);
			
			out.print("["+jsonstr+"]");
		}
		else{
			//�Ƿ�����
			out.print("�Ƿ����ã�ServiceLocator");
		}
		out.flush();
		out.close();
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
