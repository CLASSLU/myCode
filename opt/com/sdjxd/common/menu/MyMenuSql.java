package com.sdjxd.common.menu;

import com.sdjxd.pms.platform.base.Constants;
import com.sdjxd.pms.platform.organize.User;
import com.sdjxd.pms.platform.tool.SqlHelper;

public class MyMenuSql {
	public String getFirstMenu(String filter) {
		SqlHelper sqlHelper = new SqlHelper();
		StringBuffer sql = new StringBuffer(
				"SELECT M.MENUID, M.MENUNAME, M.PARENTMENUID, M.OPENTYPEID, M.MENULEVEL,M.MENUTYPEID, M.FILLCONTIONTYPE, M.FILLCONTION, M.CONTION, M.ORDERNUM,M.WORKFLOWID,1 AS FUNCTIONID, M.URL, M.APPID, M.PATTERNID,M.ARGUMENT FROM [S].JXD7_XT_FUNCTIONMENU M INNER JOIN (SELECT DISTINCT R.MENUID FROM [S].JXD7_XT_ROLEMENU R INNER JOIN [S].JXD7_XT_USERROLE UR ON R.ROLEID = UR.ROLEID INNER JOIN [S].JXD7_XT_USER U ON UR.USERID = U.USERID WHERE U.USERID = '{USERID}') T ON M.MENUID = T.MENUID WHERE M.PARENTMENUID = '{PARENTMENUID}'");
		if (filter != null && filter.length() > 0) {
			sql.append(" and (").append(filter).append(")");
		}
		sql.append(" ORDER BY SHOWORDER");
		sqlHelper.setSql(sql.toString());

		sqlHelper.setValue("PARENTMENUID", Constants.rootOrganiseID);

		User user = User.getCurrentUser();

		if (user != null) {
			sqlHelper.setValue("USERID", user.getId());
		}
		return sqlHelper.getString();
	}
	public String getChildMenu(String parentMenuId) {
		SqlHelper sqlHelper = new SqlHelper();
		StringBuffer sql = new StringBuffer(
				"SELECT M.TREE_LEVEL,M.MENUID, M.MENUNAME, M.PARENTMENUID, M.OPENTYPEID, M.MENULEVEL,M.MENUTYPEID, M.FILLCONTIONTYPE, " +
				"	M.FILLCONTION, M.CONTION, M.ORDERNUM,M.WORKFLOWID,1 AS FUNCTIONID, M.URL, " +
				"M.APPID, M.PATTERNID,M.ARGUMENT FROM(SELECT LEVEL AS TREE_LEVEL,P.*FROM JXD7_XT_FUNCTIONMENU P connect by PRIOR P.MENUID=P.PARENTMENUID " +
				"and (P.Enabletime is null or P.Enabletime < to_char(sysdate, 'yyyy-MM-dd')) " +
				"and (P.DISABLETIME is null or P.DISABLETIME >= to_char(sysdate, 'yyyy-MM-dd')) START WITH"+
				"  P.MENUID='"+parentMenuId+"')M INNER JOIN" +
				" (SELECT DISTINCT R.MENUID FROM [S].JXD7_XT_ROLEMENU R INNER JOIN [S].JXD7_XT_USERROLE UR ON R.ROLEID =" +
				" UR.ROLEID INNER JOIN [S].JXD7_XT_USER U ON UR.USERID = U.USERID WHERE U.USERID =" +
				" '{USERID}') T ON M.MENUID = T.MENUID  ORDER BY SHOWORDER");
		sqlHelper.setSql(sql.toString());
		
		User user = User.getCurrentUser();
		if (user != null) {
			sqlHelper.setValue("USERID", user.getId());
		}
		else
			sqlHelper.setValue("USERID", "30000");
		return sqlHelper.getString();
	}
}
