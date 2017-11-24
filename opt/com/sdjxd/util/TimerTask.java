package com.sdjxd.util;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.RowSet;

import com.sdjxd.pms.platform.base.Global;
import com.sdjxd.pms.platform.data.DbOper;
import com.sdjxd.pms.platform.tool.Guid;

/**
 * 定时任务
 *  
 * @author zhen
 * 
 */
public class TimerTask {
	/**
	 * 按定时任务设定生成定时任务
	 * 
	 * @param taskId
	 *            任务ID
	 * @param taskName
	 *            任务名称
	 */
	@SuppressWarnings("unchecked")
	public static void CalculateCurTimingTask(String taskId, String taskName) {
		/**
		 * 同步组织机构SQL
		 */
		String insertOrgSql = "insert into JXD7_XT_ORGANISE W1"
				+ "(ORGANISEID,"
				+ "ORGANISENAME,"
				+ "SHORTNAME,"
				+ "PREORGANISEID,"
				+ "SHOWORDER,"
				+ "ORGANISETYPE,"
				+ "DATASTATUSID,"
				+ "CREATEDATE,"
				+ "CREATEUSERID)"
				+ " select t.org_id as ORGANISEID,"
				+ "    T.U_NAME_FULL AS ORGANISENAME,"
				+ "    T.U_NAME_SHORT AS SHORTNAME,"
				+ "    T.ORG_ID_SUPERIOR AS PREORGANISEID,"
				+ "    T.U_ORDER AS SHOWORDER,"
				+ "    T.U_TYPE AS ORGANISETYPE,"
				+ "    T.U_VALIDATE AS DATASTATUSID,"
				+ "    T.U_SUBMITTIME AS CREATEDATE,"
				+ "''"
				+ "from "
				+ Global.getConfig("hsedbuser")
				+ ".auth_organization_tb T "
				+ " where to_char( T.ORG_ID) NOT IN (SELECT O.ORGANISEID FROM JXD7_XT_ORGANISE O) and t.u_validate = 1";
		/**
		 * 同步组织机构修改信息的SQL
		 */
		String orgInfoSql = "UPDATE JXD7_XT_ORGANISE O"
				+ " SET O.ORGANISENAME  = (SELECT T.U_NAME_FULL FROM "
				+ Global.getConfig("hsedbuser")
				+ ".auth_organization_tb T"
				+ " WHERE TO_CHAR(T.ORG_ID) = O.ORGANISEID),"
				+ " O.SHOWORDER = (SELECT nvl(T.U_ORDER,0) "
				+ "FROM "
				+ Global.getConfig("hsedbuser")
				+ ".auth_organization_tb T WHERE TO_CHAR(T.ORG_ID) = O.ORGANISEID),"
				+ " O.DATASTATUSID  = (SELECT T.U_VALIDATE FROM "
				+ Global.getConfig("hsedbuser")
				+ ".auth_organization_tb T WHERE TO_CHAR(T.ORG_ID) = O.ORGANISEID),"
				+ "O.PREORGANISEID = (SELECT T.ORG_ID_SUPERIOR FROM "
				+ Global.getConfig("hsedbuser")
				+ ".auth_organization_tb T WHERE TO_CHAR(T.ORG_ID) = O.ORGANISEID),"
				+ "O.U_NO = (SELECT T.U_NO FROM "
				+ Global.getConfig("hsedbuser")
				+ ".auth_organization_tb T WHERE TO_CHAR(T.ORG_ID) = O.ORGANISEID)"
				+ " WHERE O.DATASTATUSID = 1 and O.ORGANISEID IN (SELECT TO_CHAR(T.ORG_ID) FROM HSE_ZH.auth_organization_tb T)";
		List sqlList = new ArrayList();
		sqlList.add(insertOrgSql);
		sqlList.add(orgInfoSql);
		try {
			DbOper.executeNonQuery(sqlList);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void updateOrganiselevel(String organiseId) {

	}

	public static void main(String[] args) {
		CalculateCurTimingTask("", "");
	}
}
