package com.sdjxd.common;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.RowSet;

import org.apache.log4j.Logger;

import com.sdjxd.common.bean.DateCheckerBean;
import com.sdjxd.common.model.User;
import com.sdjxd.pms.platform.base.Global;
import com.sdjxd.pms.platform.data.DbOper;
import com.sdjxd.pms.platform.form.service.Form;
import com.sdjxd.pms.platform.table.service.Table;
import com.sdjxd.pms.platform.workflow.service.FlowInstance;

public class CommDao {
	private static Logger log = Logger.getLogger(CommDao.class);
	private CommSql sqlHelper = new CommSql();

	public String checkJSON(String sql) {
		StringBuffer json = new StringBuffer("[");
		RowSet rs = null;
		try {
			rs = DbOper.executeQuery(sql);
			while (rs.next()) {
				json.append("{id:'").append(rs.getString("SHEETID")).append(
						"',name:'");
				json.append(rs.getString("SHEETNAME")).append("'},");
			}
			json.replace(json.length() - 1, json.length(), "]");
			if (json.length() == 1) {
				return "[]";
			}
			return json.toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return "";
		}
	}

	public boolean deleteHseNotice(String sheetid) {
		String sql = sqlHelper.deleteHseNotice(sheetid);
		try {
			return DbOper.executeNonQuery(sql) == -1 ? false : true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean hasUntreadCount(String flowInstanceId) {
		String sql = sqlHelper.getUntreadCount(flowInstanceId);
		RowSet rs = null;
		int count = 0;
		try {
			rs = DbOper.executeQuery(sql);
			if (rs.next()) {
				count = rs.getInt("COUNT");
			}
			if (count == 0) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<Map<String, String>> getFlowObjectInfo(String[] flowobjectid) {
		List<Map<String, String>> flowObjectInfo = new ArrayList<Map<String, String>>();

		CommSql commsql = new CommSql();
		String sql = commsql.getFlowObjectInfo(flowobjectid);

		try {
			RowSet rs = DbOper.executeQuery(sql);
			while (rs.next()) {
				Map<String, String> tempMap = new HashMap<String, String>();
				tempMap.put("flowinstanceid", rs.getString("FLOWINSTANCEID"));
				tempMap.put("nodeinstanceid", rs.getString("NODEINSTANCEID"));
				flowObjectInfo.add(tempMap);
				tempMap = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return flowObjectInfo;
	}

	public boolean saveHQJL(String sheetId, String userIds, String operid) {
		List<String> sqlList = new ArrayList<String>();
		for (String userId : userIds.split(",")) {
			sqlList.add(sqlHelper.getSaveHQJLSql(sheetId, userId, operid));
		}
		try {
			return DbOper.executeNonQuery(sqlList) == -1 ? false : true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 得到主版本号
	 * 
	 * @return
	 */
	public String getMainVersion() {
		String sql = sqlHelper.getMainVersionSql();
		RowSet rs = null;
		String mainVersion = "";
		try {
			rs = DbOper.executeQuery(sql);
			if (rs.next()) {
				mainVersion = rs.getString("PVALUE");
			}
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return mainVersion;
	}

	/**
	 * 得到次版本号
	 * 
	 * @return
	 */
	public String getMinorVersion() {
		String sql = sqlHelper.getMinorVersionSql();
		RowSet rs = null;
		String minorVersion = "";
		try {
			rs = DbOper.executeQuery(sql);
			if (rs.next()) {
				minorVersion = rs.getString("PVALUE");
			}
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return minorVersion;
	}

	/**
	 * 
	 * @return
	 */
	public User[] getUserByDeptId(String deptId) {
		List<User> userList = new ArrayList<User>();
		RowSet rs = null;
		try {
			rs = DbOper.executeQuery(sqlHelper.getUserByDeptId(deptId));
			if (rs.next()) {
				User user = new User();
				user.setUserid(rs.getString("PVALUE"));
				user.setUsername(rs.getString("PVALUE"));
				userList.add(user);
			}
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return (User[]) userList.toArray();
	}

	/**
	 * 执行查询返回信息
	 * 
	 * @param sql
	 * @return
	 */
	public Object ececuteQuery(String sql) {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		RowSet rs = null;
		try {
			rs = DbOper.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			Map<String, String> tempMap = new HashMap<String, String>();
			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					tempMap.put(rsmd.getColumnName(i), rs.getString(i));
				}
				result.add(tempMap);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}


	public static String sheetExists(String tablename, String sheetid) {
		try {
			String strSql = "SELECT SHEETID FROM " + tablename
					+ " WHERE SHEETGUID = '" + sheetid + "'";
			RowSet rs = DbOper.executeQuery(strSql);
			if (rs.next())
				return rs.getString("SHEETID");
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return "";
	}
	
	public static boolean sheetDel(String[] delArray, String[] tableArray){
		try{
			List<String> list = new ArrayList<String>();
			for(int i = 0; i < delArray.length; i++){
				String table = tableArray[i];
				String sheetid = delArray[i];
				String strSql = "DELETE FROM " + table + " WHERE SHEETID = '" + sheetid + "' AND UPLOADFLAG IS NULL";
				list.add(strSql);
			}
			
			DbOper.executeNonQuery(list);
			return true;
		}catch (Exception e) {
			log.info(e.getMessage());
		}
		return false;
	}
	/**
	 * 得到数据库服务器当前系统时间
	 * 
	 * @return
	 */
	String getDBSysDate() {
		try {
			RowSet rs = DbOper.executeQuery(sqlHelper.getDBSysDate());
			if (rs.next()) {
				return rs.getString(1);
			}
			return "";
		} catch (SQLException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 根据patternid和sheetid删除一条流程，主要用于视图
	 */
	public static boolean delete(String patternid, String sheetid){
		try{
			com.sdjxd.pms.platform.form.service.Form.deleteInstanceAndFlow(patternid, sheetid);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public boolean saveUserOpenPage(String userId,String menuName,String url) throws SQLException{
		return DbOper.executeNonQuery(sqlHelper.saveUserOpenPage(userId, menuName, url))==-1?false:true;
	}
	
	public static String getServerTime(String format){
		String date = null;
		try{
			date = new SimpleDateFormat(format).format(new Date());
		}catch(Exception e){
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			log.error(e.getMessage());
		}
		return date;
	}
	
	public static String getServerTime(){
		return getServerTime("yyyy-MM-dd HH:mm:ss");
	}
	/**
	 * 设置系统版本号
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void setVersion() {
		// String mainVersion = VersionProperties.getString("mainVersion");
		// String minorVersion = VersionProperties.getString("minorVersion");

		java.util.Properties dbProps = new java.util.Properties();

		dbProps = Global.loadProperties("version.properties");

		String mainVersion = dbProps.getProperty("mainVersion");
		String minorVersion = dbProps.getProperty("minorVersion");

		String oldMainVersion = getMainVersion();
		String oldMinorVersion = getMinorVersion();
		if (!mainVersion.equals(oldMainVersion) || !minorVersion.equals(oldMinorVersion))
		{
			List sqlList = new ArrayList();
			String sql = sqlHelper.setMainVersionSql(mainVersion);
			sqlList.add(sql);
			sql = sqlHelper.setMinorVersionSql(minorVersion);
			sqlList.add(sql);

			String oldVersion = oldMainVersion + "." + oldMinorVersion;
			String version = mainVersion + "." + minorVersion;
			sql = sqlHelper.setOldVersionSql(oldVersion,version);
			sqlList.add(sql);
			
			try {
				DbOper.executeNonQuery(sqlList);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
	}
	
	/**
	 * 判断票是否驳回控制是否显示审核历史
	 * @param patternid
	 * @return 1，是驳回，2，不是驳回，3，不知道是不是
	 */
	public static int showShList(String sheetid, String patternid){
		try{
			String sheetstatusid = "", tablename = "";
			
			String strSql = "SELECT STATUSID FROM [S].JXD7_WF_FLOWNODE T1, [S].JXD7_WF_FORM T2 WHERE T2.FORMPATTERNID = '" + patternid + "' AND T2.FLOWID = T1.FLOWID";
			RowSet rs = DbOper.executeQuery(strSql);
			if(rs.next()){
				sheetstatusid = rs.getString("STATUSID");
			}else{
				return 3;
			}
			
			strSql = "SELECT TABLENAME FROM [S].JXD7_PM_PATTERN WHERE PATTERNID = '" + patternid + "'";
			rs = DbOper.executeQuery(strSql);
			if(rs.next()){
				tablename = rs.getString("TABLENAME");
			}else{
				return 3;
			}
			
			strSql = "SELECT SHEETSTATUSID FROM " + tablename + " WHERE SHEETID = '" + sheetid + "'";
			rs = DbOper.executeQuery(strSql);
			if(!rs.next())
				return 2;
			else if(!rs.getString("SHEETSTATUSID").equals(sheetstatusid))
				return 3;
			
			strSql = "SELECT 1 FROM XMGG_FLOW_SHHQ T1, " + tablename + " T2 WHERE T1.DYBDID = '" + sheetid + "' AND T1.DYBDID = T2.SHEETID AND T2.SHEETSTATUSID = '" + sheetstatusid + "'";
			rs = DbOper.executeQuery(strSql);
			if(rs.next()){
				return 1;
			}else{
				return 2;
			}
		}catch(Exception err){
			err.printStackTrace();
			return 3;
		}
	}
	
	//修复组织机构showorder
	public static boolean resetOrgShoworder(){
		try{
			int i = 0;
			List<String> list = new ArrayList<String>();
			
			String strSql = "SELECT ORGANISEID, ORGANISELEVEL FROM JXD7_XT_ORGANISE ORDER BY LENGTH(ORGANISELEVEL), SHOWORDER";
			RowSet rs = DbOper.executeQuery(strSql);
			while(rs.next()){
				strSql = "UPDATE JXD7_XT_ORGANISE SET SHOWORDER = " + i++ + " WHERE ORGANISEID = '" + rs.getString("ORGANISEID") + "'";
				list.add(strSql);
			}
			
			i = DbOper.executeNonQuery(list);
			if(i == -1)
				return false;
			return true;
		}catch(Exception err){
			err.printStackTrace();
			return false;
		}
	}
	
	public static HashMap getSubTableRecordsCount(Object[]mainRecords,HashMap[] subTableInfo) throws SQLException
	{
		boolean deleteOK = true;
		HashMap map = new HashMap();
		for(Object value:mainRecords)
		{
			int count = 0;
			for(HashMap tinfo:subTableInfo)
			{
				String table = (String)tinfo.get("tableId");
				String field =  (String)tinfo.get("field");
				String sql = "select count(1) from "+table+" where "+field+"='"+value+"'";
				ResultSet rs = DbOper.executeQuery(sql);
				if(rs.next())
					count += rs.getInt(1);
			}
			if(count>0)
			{
				deleteOK = false;
			}
			map.put(value, count);
		}
		map.put("deleteOK",deleteOK);
		return map;
	}
	/**
	 * 删除主表信息及相关子表信息
	 * @param table
	 * @param values
	 * @param isFlow
	 * @param subTables
	 * @return
	 */
	public static boolean deleteCascade(String table, Object[]values, boolean isFlow, HashMap[] subTables)
	{
		boolean r = true;
		try
		{
			for(Object value:values)
			{
				//删除子表
				if(subTables != null && subTables.length>0)
				{
					for(HashMap subTable:subTables)
					{
						Object b = subTable.get("isFlow");
						b = b==null?false:b;
						r &= deleteSubTableData((String)value,(String)subTable.get("tableId"),(String)subTable.get("field"),
								(Boolean)b);
					}
				}
				//删除主表
				if(isFlow && r)
				{
					r &= FlowInstance.deleteByForm((String)value);
				}
				if(r)
					Table.deleteData(table,(String)value);
				else
				{
					//指定主表是流程,但删除失败时
					try{
						//尝试判断主表是不是流程表
						ResultSet rs = DbOper.executeQuery("select FLOWOBJECTID FROM "+table+" where SHEETID='"+value+"'");
						if(rs.next() && rs.getString(1)==null)
							throw(new SQLException("FLOWOBJECTID为空,以表单形式删除记录."));
						break;//不是流程表,结束循环，删除失败
					}catch(SQLException e){
						//不是流程表,或流程实例ID为空,删除表单实例
						log.info(e.getMessage());
						if(e.getMessage().indexOf("FLOWOBJECTID")>=0)
							r = Table.deleteData(table,(String)value);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			r =false;
		}
		return r;
	}
	private static boolean deleteSubTableData(String value, String subTable, String subTableField, boolean isFlow) throws SQLException
	{
		ResultSet rs = null;
		boolean r = true;
		List<String> list = new ArrayList<String>();
		if(isFlow)
		{
			rs = DbOper.executeQuery("select flowobjectid from "+subTable+" where "+subTableField+"='"+value+"'");
			while(rs.next())
			{
				list.add(rs.getString(1));
			}
			if(list.size()>0)
			{
				r &= FlowInstance.delete(list.toArray(new String[0]));
			}
		}
		if(r)
			DbOper.executeNonQuery("delete from "+subTable+" where "+subTableField+"='"+value+"'");
		rs = null;
		list = null;
		return r;
	}
	
	/**
	 * 判断日期重复
	 * @return true|false|null
	 */
	public static Object isDuplicated(DateCheckerBean bean)
	{
		if(bean == null || !bean.checkData())
			return null;
		ResultSet rs = null;
		try{
			if(bean.getTableName() == null)
			{
				Form form = Form.getPattern(bean.getPatternId());
				bean.setTableName(form.getIndexTable());
				if(bean.getPk() != null)
					bean.setPkField(form.getIndexTablePkCol());
			}
			 rs = DbOper.executeQuery(bean.getCheckSql());
			 if(rs.next())
				 return true;
			 return false;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			rs = null;
		}
	}
	
	/**
	 * 返回下一个下级行政区域的层次和层次编码
	 * 
	 * @author Key 2011-11-29
	 * @param regionId 当前行政区域ID
	 * @return [层次,层次码]
	 */
	public static String[] getNextRegionInfo(String regionId)
	{
		String[] info = null;
		try{
			ResultSet rs = DbOper.executeQuery(new CommSql().getNextRegionNeedInfo(regionId));
			if(rs.next())
			{
				String maxChildrenLevelCode = rs.getString("MAX_CHILDREN_LEVELCODE");
				String  levelCode = rs.getString("REGIONLEVELCODE");
				int level = rs.getInt("REGIONLEVEL");
				info = new String[2];
				info[0] = String.valueOf(level+1);
				if(maxChildrenLevelCode != null)
				{
					String s = maxChildrenLevelCode.substring(0,maxChildrenLevelCode.length()-3);
					String tail = maxChildrenLevelCode.substring(maxChildrenLevelCode.length()-3);
					int lv = Integer.parseInt(tail)+1;
					if(lv<10)
						s = s+"00"+lv;
					else if(lv<100)
						s = s +"0" + lv;
					else
						s += lv;
					info[1] = s;
				}
				else
				{
					info[1] = levelCode +"001";
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return info;
	}
	
	/**
	 * 级联删除行政区域,及下级行政区域
	 * @param regLVCode 层次码
	 */
	public static boolean deleteRegionCascade(String[] regLVCode)
	{
		List<String> sqlList = new ArrayList<String>();
		CommSql sqlMaker = new CommSql();
		for(String lvc:regLVCode)
			sqlList.add(sqlMaker.deleteRegionCascade(lvc));
		if(sqlList.size()>0)
		{
			try{
				DbOper.executeNonQuery(sqlList);
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
}
