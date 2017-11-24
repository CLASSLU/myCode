package kd.idp.psidpapp.opt.sys.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.spring.dbservice.DBTemplate;

public class BaseDao {
	
	
	/**
	 * 返回分页对象（含结果集数据）
	 * @param pageSize			页大小
	 * @param currentPage		当前页
	 * @param sql				sql视图
	 * @param dataSourceName	数据源JDBC
	 * @return					返回分页对象（含结果集数据）
	 * @throws Exception
	 */
	public Map<String,Object> findDataMapByPage(int pageSize, int currentPage, String sql, String dataSourceName) throws Exception{
		Map<String, Object> pageDataMap = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder("");
		try {
			sb.setLength(0);
			long totalCount = DBTemplate.getInstance(dataSourceName).getResultMapList(sql).size();
			long totalPage = (totalCount / pageSize);
			if (0 != totalCount && totalCount < pageSize){
				totalPage = 1;
			}else if (0 != totalCount % pageSize){
				totalPage++;
			}
			long startIndex = pageSize * (currentPage - 1);
			long endIndex = startIndex + pageSize;
			sql = "SELECT * FROM (SELECT ROWNUM as ROWSN, A.* FROM (" + sql + ") A) WHERE "
			    + startIndex + " < ROWSN AND ROWSN <= " + endIndex;
			List<Map<String, Object>> dataList = DBTemplate.getInstance(dataSourceName).getResultMapList(sql);
			pageDataMap.put("TOTALCOUNT", totalCount);
			pageDataMap.put("TOTALPAGE", totalPage);
			pageDataMap.put("DATALIST", dataList);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return pageDataMap;
	}
	
	/**
	 * 新增
	 * @param dataSourceName	数据库链接名
	 * @param tablName			访问表名路径
	 * @param propertyNames		可插入字段名
	 * @param viewMap			前端提交数据
	 * @return					新增(返回操作真假)
	 * @throws Exception
	 */
	public boolean addMapTable(String dataSourceName, String tablName, String propertyNames, Map<String, Object> viewMap)throws Exception{
		StringBuilder sb = new StringBuilder("");
		String fieldNameArray[] = propertyNames.split(",");
		sb.setLength(0);
		sb.append("INSERT INTO ").append(tablName).append("(");
		long startLength = sb.toString().length();
		try {
			List<Object> valueList = new ArrayList<Object>();
			for (String filedName:fieldNameArray){
				if (null != viewMap.get(filedName)){
					sb.append(filedName).append(",");
					valueList.add(viewMap.get(filedName));
				}
			}
			if (startLength == sb.toString().length()){	//无新增字段
				return false;
			}else{
				String addSql = sb.toString().substring(0, sb.toString().length()-1) + ") VALUES(";
				sb.setLength(0);
				sb.append(addSql);
				for (Object object : valueList){
					sb.append("'").append(object).append("',");
				}
				addSql = sb.toString().substring(0,sb.toString().length()-1) + ")";
				System.out.println("平台新增SQL语句：============[" + addSql + "]============");
				DBTemplate.getInstance(dataSourceName).executeSql(addSql);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 删除
	 * @param dataSourceName	数据库链接名
	 * @param tablName			访问表名路径
	 * @param sqlCondtions		过滤条件SQL
	 * @return					删除(返回操作真假)
	 * @throws Exception
	 */
	public boolean delMapListTable(String dataSourceName, String tablName, String sqlCondition) throws Exception{
		StringBuilder sb = new StringBuilder("");
		try {
			sb.setLength(0);
			sb.append("DELETE FROM ")
			  .append(tablName).append(" WHERE 1=1 AND ")
			  .append(sqlCondition);
			System.out.println("平台删除SQL语句：============[" + sb.toString() + "]============");
			DBTemplate.getInstance(dataSourceName).executeSql(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return true;
	}
	
	/**
	 * 修改
	 * @param dataSourceName	数据库链接名
	 * @param tablName			访问表名路径
	 * @param propertyNames		可更新字段名
	 * @param sqlCondition		更新条件
	 * @param viewMap			前端更新值
	 * @return					修改(返回操作真假)
	 * @throws Exception
	 */
	public boolean updateMapTable(String dataSourceName, String tablName, String propertyNames, String sqlCondition, Map<String, Object> viewMap)throws Exception{
		StringBuilder sb = new StringBuilder("");
		String fieldNameArray[] = propertyNames.split(",");
		sb.setLength(0);
		sb.append("UPDATE ").append(tablName).append(" SET ");
		long startLength = sb.toString().length();
		try {
			for (String filedName:fieldNameArray){
				if (null != viewMap.get(filedName)){
					sb.append(filedName).append("='").append(viewMap.get(filedName)).append("',");
				}
			}
			if (startLength == sb.toString().length()){	//无更新字段
				return false;
			}else{
				String updateSql = sb.toString().substring(0, sb.toString().length()-1);
				sb.setLength(0);
				sb.append(updateSql).append(" WHERE 1=1 AND ")
				  .append(sqlCondition);
				System.out.println("平台更新SQL语句：============[" + sb.toString() + "]============");
				DBTemplate.getInstance(dataSourceName).executeSql(sb.toString());
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 查询
	 * @param dataSourceName	数据库链接名
	 * @param tablName			访问表名路径
	 * @param sqlCondtions		过滤条件SQL
	 * @return					查询(返回操作结果集)
	 * @throws Exception
	 */
	public List<Map<String,Object>> findMapListTable(String dataSourceName, String tablName, String sqlCondition) throws Exception{
		List<Map<String,Object>> mapList = null;
		StringBuilder sb = new StringBuilder("");
		try {
			sb.setLength(0);
			sb.append("SELECT * FROM ")
			  .append(tablName).append(" WHERE 1=1 AND ")
			  .append(sqlCondition);
			System.out.println("平台查询SQL语句：============[" + sb.toString() + "]============");
			mapList = DBTemplate.getInstance(dataSourceName).getResultMapList(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return mapList;
	}
	

	public static void main(String[] args) {
		BaseDao dao = new BaseDao();
		try {
			//删除
			String delSqlCondition = "ID='20170816100514102635629'";
			System.out.println(dao.delMapListTable("DMDataSource", "OPT.OPT_TICKET", delSqlCondition));
			
			//新增
			Map<String, Object> addMap = new HashMap<String, Object>();
			addMap.put("ID", "20170816100514102635629");
			addMap.put("TICKETNUMBER", "20170001");
			addMap.put("OPERTASK", "500kV汉川#5机检修");
			addMap.put("TICKETSTATUS", "拟票");
			addMap.put("TICKETTYPE", "调试票");
			addMap.put("DATASTATUSID", "1");
			addMap.put("CREATEUSERID", "admin");
			addMap.put("CREATETIME", "2017-08-16 14:25:34");
			System.out.println(dao.addMapTable("DMDataSource","OPT.OPT_TICKET", "ID,TICKETNUMBER,OPERTASK,FASHION,OPENTICKETTIME,DRAFTERID,DRAFTER,AUDITORID,AUDITOR,RECVTICKETERID,RECVTICKETER,RECVTIME,TICKETSTATUS,TICKETTYPE,TICKETOPERSTARTTIME,TICKETOPERENTTIME,EXT1,EXT2,EXT3,EXT4,EXT5,CLASS,SORT,DATASTATUSID,CREATEUSERID,CREATETIME,UPDATEUSERID,UPDATETIME,REMARK", addMap));
		
			//查询
			String findSqlCondition = "ID='20170816100514102635629'";
			List<Map<String, Object>> findMapList = dao.findMapListTable("DMDataSource","OPT.OPT_TICKET",findSqlCondition);
			for (Map<String, Object> map : findMapList){
				System.out.println(map.get("TICKETNUMBER"));
				System.out.println(map.get("TICKETSTATUS"));
				System.out.println(map.get("TICKETTYPE"));
				System.out.println(map.get("CREATEUSERID"));
				System.out.println(map.get("CREATETIME"));
			}
			
			//修改
			Map<String, Object> updateMap = new HashMap<String, Object>();
			updateMap.put("ID", "20170816100514102635629");
			updateMap.put("OPERTASK", "500kV汉川#4机检修");
			updateMap.put("TICKETSTATUS", "拟票");
			updateMap.put("DATASTATUSID", "1");
			System.out.println(dao.updateMapTable("DMDataSource","OPT.OPT_TICKET","ID,TICKETNUMBER,OPERTASK,FASHION,OPENTICKETTIME,DRAFTERID,DRAFTER,AUDITORID,AUDITOR,RECVTICKETERID,RECVTICKETER,RECVTIME,TICKETSTATUS,TICKETTYPE,TICKETOPERSTARTTIME,TICKETOPERENTTIME,EXT1,EXT2,EXT3,EXT4,EXT5,CLASS,SORT,DATASTATUSID,CREATEUSERID,CREATETIME,UPDATEUSERID,UPDATETIME,REMARK","ID='20170816100514102635629'", updateMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
