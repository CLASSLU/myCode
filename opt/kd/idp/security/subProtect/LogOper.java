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
 * 记录安全操作日志类
 * @author liming 2013-6-4
 *
 * (c)   Copyright 2013 PSIDP.KD Co,Ltd. All Rights Reserved
 * @version 1.0
 */
public class LogOper {


	/**
	 * 记录安全操作日志
	 * @param remoteIp 操作人ip
	 * @param operType 操作类型
	 * @param operResult 操作结果
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
		sql.append("INSERT INTO IDP_KH.AQFH_安全防护日志 ")
		   .append("(SHEETID,事件人,事件人ID,事件时间,事件动作,事件结果,IP) VALUES ('")
		   .append(sheetId).append("','").append(userName).append("','")
		   .append(userId).append("','").append(currentDate).append("','")
		   .append(operType).append("','").append(operResult).append("','")
		   .append(ip).append("')");
		
			DBTemplate.getInstance().updateSql(sql.toString());
		
	}
}


