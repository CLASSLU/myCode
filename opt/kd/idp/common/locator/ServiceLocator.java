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
 * 前后台 交互 控制器
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
			//调用 类名
			String className = request.getParameter("className"); 
			//调用 方法名
			String funcName = request.getParameter("funcName");
			//参数列表 json
			String params = request.getParameter("params");
			
			System.out.println("前台生成: "+params);
			
			//创建 调用
			ReflectService rs = new ReflectService(className,funcName);
			//解析json
			ParserJSONTools jt = new ParserJSONTools(params);
			rs.setParamValues(jt.parserJSON(),request,response);
			//调用方法 返回结果
			Object result = rs.invokeMethod();
			//将结果 组织成json
			String jsonstr = CreateJSONTools.toJSONString(result);
			
			System.out.println("后台生成: "+jsonstr);
			
			out.print("["+jsonstr+"]");
		}
		else{
			//非法调用
			out.print("非法调用：ServiceLocator");
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
