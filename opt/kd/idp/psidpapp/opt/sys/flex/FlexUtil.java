package kd.idp.psidpapp.opt.sys.flex;

import java.util.HashMap;
import java.util.Map;

import flex.messaging.FlexContext;
import flex.messaging.FlexSession;

public class FlexUtil {

	
	
	/**
	 * �Ƿ���֤����̨session
	 */
	public static boolean checkSession = true;
	
	/**
	 * ���session
	 * @return
	 */
	public static Map<String, Object> getFlexCurrentUser(){
		FlexSession session = FlexContext.getFlexSession();
		
		if(session != null){
			return (Map<String, Object>) session.getAttribute("guser");
		}
		return null;
	}
	
	/**
	 * ���session ����
	 * @return
	 */
	public static Map<String, Object> getFlexCurrentSessionConfig(){
		FlexSession session = FlexContext.getFlexSession();
		if(session != null){
			Map<String, Object> sessionConfig = new HashMap<String, Object>();
			sessionConfig.put("guser", (Map<String, Object>) session.getAttribute("guser"));
			sessionConfig.put("orgConfig", (Map<String, Object>) session.getAttribute("orgConfig"));
			sessionConfig.put("orderOrgConfig", (Map<String, Object>) session.getAttribute("orderOrgConfig"));
			return sessionConfig;
		}
		return null;
	}
	
	
	/**
	 * ���û���Ϣ�����FlexSession
	 * @param user	�û���Ϣ
	 * @return
	 */
	public static boolean setFlexCurrentUser(Map<String, Object> user){
		try {
			FlexSession session = FlexContext.getFlexSession();
			if(session != null){
				session.setAttribute("guser",user);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
