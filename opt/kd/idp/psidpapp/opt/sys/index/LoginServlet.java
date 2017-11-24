package kd.idp.psidpapp.opt.sys.index;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.spring.SpringBeanFactory;

import kd.idp.psidpapp.opt.sys.constant.EnumConstant;
import kd.idp.psidpapp.opt.sys.flex.FlexUtil;
import kd.idp.psidpapp.opt.sys.org.service.OrgService;
import kd.idp.psidpapp.opt.sys.user.service.UserService;


public class LoginServlet extends HttpServlet {

	/**
	 * 序列化
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
			String userName = request.getParameter("USERNAME");
			String passWord = request.getParameter("PASSWORD");
			Map<String,Object> userMap = ((UserService)SpringBeanFactory.getInstance().getBean("userService")).findUser(userName, passWord);
			if (null != userMap){
				if ("0".equals(String.valueOf(userMap.get("DATASTATUSID")))){
					response.getWriter().print(EnumConstant.AUTH_ERROR_USER_STATUS);
					return;
				}
				if ("2".equals(String.valueOf(userMap.get("DATASTATUSID")))){
					response.getWriter().print(EnumConstant.AUTH_ERROR_USER_OR_ORG);
					return;
				}
				if (null == userMap.get("ORGID") || "null".equals(userMap.get("ORGID")) || "".equals(userMap.get("ORGID"))){
					response.getWriter().print(EnumConstant.AUTH_ERROR_ORG_NULL);
					return;
				}
				//1、获取用户角色信息
				String roleIds = "";
				List<String> roleIdList = ((UserService)SpringBeanFactory.getInstance().getBean("userService")).findRoleIdByUserId(String.valueOf(userMap.get("ID")));
				for (int i=0; i<roleIdList.size(); i++){
					roleIds += "," + roleIdList.get(i);
				}
				if (StringUtils.isNotEmpty(roleIds)){
					roleIds += ",";
				}
				userMap.put("ROLEIDS", roleIds);
				request.getSession().setAttribute("guser", userMap);
				//2、获取组织机构配置信息
				Map<String, Object> orgConfigMap = ((OrgService)SpringBeanFactory.getInstance().getBean("orgService")).getOrgConfigMap("1=1");
				request.getSession().setAttribute("orgConfig", orgConfigMap);
				//3、获取监控单位组织机构配置信息
				Map<String, Object> orderOrgConfigMap = ((OrgService)SpringBeanFactory.getInstance().getBean("orgService")).getOrgConfigMap("DATASTATUSID='1' AND CLASS='调控中心部门' AND NAME LIKE '%监控%' ORDER BY ID ASC");
				request.getSession().setAttribute("orderOrgConfig", orderOrgConfigMap);
				
//				FlexUtil.setFlexCurrentUser(userMap);
				response.getWriter().print(EnumConstant.AUTH_SUCCESS_FLAG);
			}else{
				response.getWriter().print(EnumConstant.AUTH_ERROR_NAME_OR_PASSWORD);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void init() throws ServletException {
	}
	
	

}
