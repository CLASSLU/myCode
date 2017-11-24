package kd.idp.common.utility;

import javax.servlet.http.HttpServletRequest;

import kd.idp.cms.bean.priv.UserBean;

import com.sdjxd.util.StringUtils;

public class RequestUtil {

	
	/**
	 * ���Ӧ��·��
	 * @param request
	 * @return
	 */
	public static String getBasePath(HttpServletRequest request){
		String path = request.getContextPath();
		return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	}
	/**
	 * ���Ӧ�÷���������
	 * @param request
	 * @return
	 */
	public static String getServerName(HttpServletRequest request){
		
		return request.getServerName();
	}
	/**
	 * ���request ����
	 * @param request
	 * @param key
	 * @return
	 */
	public static String getRequestParameter(HttpServletRequest request,String key){
		try {
			String param = request.getParameter(key);
			if("".equals(param)){
				return null;
			}else{
				return param;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * ���request url ����
	 * @param request
	 * @param key
	 * @return
	 */
	public static String getHeaderReferParameter(HttpServletRequest request,String pa){
		try {
			String refer=request.getHeader("referer");
			if(StringUtils.isBlank(refer))
				return null;
			String parameter=ProcessRequestUrl.URLRequest(refer).get(pa);
			return parameter;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * ��õ�ǰ��¼�û�
	 * @param request
	 * @return
	 */
	public static UserBean getCurrentUser(HttpServletRequest request){
		Object attr = request.getSession().getAttribute("guser");
		if(attr != null && attr instanceof UserBean ){
			return (UserBean)attr;
		}else{
			return null;
		}
	}
}
