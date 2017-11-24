package kd.idp.psidpapp.opt.sys.flex;

import java.util.HashMap;
import java.util.Map;

import flex.messaging.FlexContext;
import flex.messaging.FlexSession;

public class FlexUtil {

	
	
	/**
	 * 是否验证控制台session
	 */
	public static boolean checkSession = true;
	
	/**
	 * 获得session
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
	 * 获得session 配置
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
	 * 将用户信息存放与FlexSession
	 * @param user	用户信息
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
