package kd.idp.security;

import java.util.Map;

import kd.idp.cms.bean.priv.UserBean;
import kd.idp.common.config.WebConfigUtil;
import kd.idp.common.consts.TableConst;

import com.spring.dbservice.DBTemplate;

public class SecurityDao{

	/**
	 * ���Ĭ���û� 
	 * @return
	 */
	public static UserBean getDefaultUser() {
		UserBean user = null;
		try {
			Map<String, Object> map = DBTemplate.getInstance().getResultMap("SELECT * FROM "
					+ TableConst.TABLE_MANAGE_USER + " WHERE ��¼��='"
					+ WebConfigUtil.getDefaultUser() + "'");
			if(map != null){
				user = new UserBean();
				user.setUserId(String.valueOf(map.get("�û�ID")));
				user.setUserName(String.valueOf(map.get("��¼��")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
}
