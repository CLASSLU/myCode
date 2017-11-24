package com.sdjxd.common.waitDo;

import com.sdjxd.pms.platform.data.DbOper;
import javax.sql.RowSet;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

public class WaitDoDeal {
	public static Logger log = Logger.getLogger(WaitDoDeal.class.getName());

	/**
	 * 得到配置文件指定的属性
	 * 
	 * @param propertyName
	 * @return
	 */
	public static String getWatDoSheetName(String operid, String waitDoId, int waitDoType) {
		String sheetName = "";
		String strSql = "";
		String strTableName = "";
		RowSet rs = null;
		
		switch (waitDoType)
		{
		case 1:
			strSql = "select tablename from [S].jxd7_pm_pattern where patternid='" +
					waitDoId + "'";
			break;
			
		case 2:
			strSql = "select tablename from [S].jxd7_pm_pattern where patternid in (" +
				"select b.formpatternid from [S].jxd7_wf_form b where b.flowid in (select flowid " +
				"from [S].jxd7_wf_instance s where s.flowinstanceid='" +
				waitDoId + "'))";
			break;
			
		case 3:
			strSql = "select tablename from [S].jxd7_pm_pattern where patternid in (" +
				"select b.formpatternid from [S].jxd7_wf_form b where b.flowid ='" +
				waitDoId + "')";
			break;

		default:
			return "";
		}
		
		try {
			rs = DbOper.executeQuery(strSql);
			if (rs.next()) {
				strTableName = rs.getString("tablename");
			}
			
			if (strTableName == "" || strTableName == null)
			{
				return "获取表单对应表名称失败";
			}
			
			switch (waitDoType)
			{
			case 1:
				strSql = "select SHEETNAME from [S]." + strTableName + " where sheetid = '" +
						operid + "'";
				break;
				
			case 2:
				strSql = "select SHEETNAME from [S]." + strTableName + " where flowobjectid = '" +
					waitDoId + "'";
				break;
				
			case 3:
				strSql = "select SHEETNAME from [S]." + strTableName + " where flowobjectid = '" +
					waitDoId + "'";
				break;

			default:
				return "";
			}
			
			rs = DbOper.executeQuery(strSql);
			if (rs.next()) {
				sheetName = rs.getString("SHEETNAME");
			}
			
			if (sheetName == "" || sheetName == null)
			{
				sheetName = "未取得表单名称";
			}
			rs = null;
		} catch (Exception e) {
			log.error(e.getMessage());
			return "获表名称失败";
		}
		
		return sheetName;
	}
	
	/**
	 * 得到流程参数以及所属模块
	 * 
	 * @param propertyName
	 * @return
	 */
	public static String getFlowParamInfo(String flowid) {
		String strRValue = "";
		String strSql = "";
		RowSet rs = null;
		
		strSql = "select OPERURL from [S].jxd7_xt_waitdo t where t.operid='"+ flowid + "'";
		
		try {
			rs = DbOper.executeQuery(strSql);
			if (rs.next()) {
				strRValue = rs.getString("OPERURL");
			}
			rs = null;
		} catch (Exception e) {
			log.error(e.getMessage());
			return "NULL";
		}
/*		strSql = "select varpath from jxd7_wf_formvar t where flowid ='" + flowid + "' and t.varid in " +
				"(select s.formvarid from jxd7_wf_param s where s.flowid='" + flowid + "' and s.paramname='NAME') ";
		
		try {
			rs = DbOper.executeQuery(strSql);
			if (rs.next()) {
				String strVarPath = rs.getString("varpath");
				JSONArray arrTemp = JSONArray.fromObject(strVarPath);
				strVarPath = arrTemp.getString(0);
				strRValue = strRValue + strVarPath + ",";
			}
			rs = null;
			
			strSql = "select modulename from jxd7_dm_module a where a.moduleid in " +
					"(select moduleid from jxd7_wf_flow r where r.flowid='" +
					flowid + "')";
			
			rs = DbOper.executeQuery(strSql);
			if (rs.next()) {
				strRValue = strRValue + rs.getString("modulename") + ",";
			}

			rs = null;
		} catch (Exception e) {
			log.error(e.getMessage());
			return "NULL";
		}
		
		if (strRValue.length() > 0)
			strRValue = strRValue.substring(0, strRValue.length()-1);
		*/
		return strRValue;
	}
	
	/**
	 * 得到流程参数以及所属模块
	 * 
	 * @param propertyName
	 * @return
	 */
	public static String getWatDoStatus(String operid) {
		String statusName = "";
		String strSql = "";
		RowSet rs = null;
		
		String flowinstanceid = operid.substring(0, 36);
		String nodeinstanceid = operid.substring(36, operid.length());
		strSql = "SELECT T.STATUSNAME, U.REASON FROM JXD7_WF_NODEINSTANCE T LEFT JOIN [S].JXD7_WF_UNTREAD U ON T.FLOWINSTANCEID = U.FLOWINSTANCEID " +
				"AND T.NODEINSTANCEID = U.NODEINSTANCEID where T.FLOWINSTANCEID='" + flowinstanceid +
				"' AND T.NODEINSTANCEID='" + nodeinstanceid + "'";
		
		try {
			rs = DbOper.executeQuery(strSql);
			if (rs.next()) {
				String strReason = rs.getString("REASON");
				statusName = rs.getString("STATUSNAME");
				if (strReason == null || strReason == "")
				{
				}
				else
				{
					statusName = "驳回修改";
				}
			}
			rs = null;
		} catch (Exception e) {
			log.error(e.getMessage());
			return "";
		}
		
		if (statusName == null)
			statusName = "";
		
		return statusName;
	}
	
	/**
	 * 得到人员名称
	 * 
	 * @param userid
	 * @return
	 */
	public static String getSendUserName(String userid)
	{
		String userName = "";
		String strSql = "";
		RowSet rs = null;
		
		strSql = "select USERNAME from [S].jxd7_xt_user t where t.userid='" + userid + "'";
		
		try {
			rs = DbOper.executeQuery(strSql);
			if (rs.next()) {
				userName = rs.getString("USERNAME");
			}
			rs = null;
		} catch (Exception e) {
			log.error(e.getMessage());
			return "";
		}
		
		return userName;
	}
}
