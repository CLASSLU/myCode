package kd.idp.security;

import java.util.Map;

import kd.idp.cms.bean.priv.UserBean;
import kd.idp.common.config.WebConfigUtil;
import kd.idp.common.consts.TableConst;

import com.spring.dbservice.DBTemplate;

public class SecurityDao{

	/**
	 * 获得默认用户 
	 * @return
	 */
	public static UserBean getDefaultUser() {
		UserBean user = null;
		try {
			Map<String, Object> map = DBTemplate.getInstance().getResultMap("SELECT * FROM "
					+ TableConst.TABLE_MANAGE_USER + " WHERE 登录名='"
					+ WebConfigUtil.getDefaultUser() + "'");
			if(map != null){
				user = new UserBean();
				user.setUserId(String.valueOf(map.get("用户ID")));
				user.setUserName(String.valueOf(map.get("登录名")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
}
