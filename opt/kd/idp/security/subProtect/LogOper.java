package kd.idp.security.subProtect;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kd.idp.cms.bean.priv.UserBean;

import com.sdjxd.pms.platform.data.DbOper;
import com.sdjxd.pms.platform.tool.DateTool;
import com.sdjxd.pms.platform.tool.Guid;
import com.spring.dbservice.DBTemplate;

/**
 * LogOper.java
 * ��¼��ȫ������־��
 * @author liming 2013-6-4
 *
 * (c)   Copyright 2013 PSIDP.KD Co,Ltd. All Rights Reserved
 * @version 1.0
 */
public class LogOper {


	/**
	 * ��¼��ȫ������־
	 * @param remoteIp ������ip
	 * @param operType ��������
	 * @param operResult �������
	 * @return void
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { RuntimeException.class })
	public static void insertInfoToTable(HttpServletRequest request, String operType, boolean operResult){

		UserBean user = (UserBean) request.getSession().getAttribute("guser");
		String userName = "";
		String userId = "";
		if(user != null){
			userName = user.getUserName();
			userId = user.getUserId();
		}
		else{
			userName = request.getParameter("username");
		}
		String currentDate = DateTool.getCurrentDate("yyyy-MM-dd HH:mm:ss");
		String ip = request.getRemoteAddr();
		StringBuffer sql = new StringBuffer();
		String sheetId = Guid.create();
		sql.append("INSERT INTO IDP_KH.AQFH_��ȫ������־ ")
		   .append("(SHEETID,�¼���,�¼���ID,�¼�ʱ��,�¼�����,�¼����,IP) VALUES ('")
		   .append(sheetId).append("','").append(userName).append("','")
		   .append(userId).append("','").append(currentDate).append("','")
		   .append(operType).append("','").append(operResult).append("','")
		   .append(ip).append("')");
		
			DBTemplate.getInstance().updateSql(sql.toString());
		
	}
}


