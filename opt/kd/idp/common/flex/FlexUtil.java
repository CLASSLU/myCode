package kd.idp.common.flex;

import kd.idp.cms.bean.priv.UserBean;
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
	public static UserBean getFlexCurrentUser(){
		FlexSession session = FlexContext.getFlexSession();
		if(session != null){
			return (UserBean)session.getAttribute("guser");
		}
		return null;
	}
	
	
	/**
	 * @param user
	 * @return
	 */
	public static boolean setFlexCurrentUser(UserBean user){
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
