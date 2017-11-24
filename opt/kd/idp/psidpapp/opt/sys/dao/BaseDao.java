package kd.idp.psidpapp.opt.sys.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.spring.dbservice.DBTemplate;

public class BaseDao {
	
	
	/**
	 * ���ط�ҳ���󣨺���������ݣ�
	 * @param pageSize			ҳ��С
	 * @param currentPage		��ǰҳ
	 * @param sql				sql��ͼ
	 * @param dataSourceName	����ԴJDBC
	 * @return					���ط�ҳ���󣨺���������ݣ�
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
	 * ����
	 * @param dataSourceName	���ݿ�������
	 * @param tablName			���ʱ���·��
	 * @param propertyNames		�ɲ����ֶ���
	 * @param viewMap			ǰ���ύ����
	 * @return					����(���ز������)
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
			if (startLength == sb.toString().length()){	//�������ֶ�
				return false;
			}else{
				String addSql = sb.toString().substring(0, sb.toString().length()-1) + ") VALUES(";
				sb.setLength(0);
				sb.append(addSql);
				for (Object object : valueList){
					sb.append("'").append(object).append("',");
				}
				addSql = sb.toString().substring(0,sb.toString().length()-1) + ")";
				System.out.println("ƽ̨����SQL��䣺============[" + addSql + "]============");
				DBTemplate.getInstance(dataSourceName).executeSql(addSql);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * ɾ��
	 * @param dataSourceName	���ݿ�������
	 * @param tablName			���ʱ���·��
	 * @param sqlCondtions		��������SQL
	 * @return					ɾ��(���ز������)
	 * @throws Exception
	 */
	public boolean delMapListTable(String dataSourceName, String tablName, String sqlCondition) throws Exception{
		StringBuilder sb = new StringBuilder("");
		try {
			sb.setLength(0);
			sb.append("DELETE FROM ")
			  .append(tablName).append(" WHERE 1=1 AND ")
			  .append(sqlCondition);
			System.out.println("ƽ̨ɾ��SQL��䣺============[" + sb.toString() + "]============");
			DBTemplate.getInstance(dataSourceName).executeSql(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return true;
	}
	
	/**
	 * �޸�
	 * @param dataSourceName	���ݿ�������
	 * @param tablName			���ʱ���·��
	 * @param propertyNames		�ɸ����ֶ���
	 * @param sqlCondition		��������
	 * @param viewMap			ǰ�˸���ֵ
	 * @return					�޸�(���ز������)
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
			if (startLength == sb.toString().length()){	//�޸����ֶ�
				return false;
			}else{
				String updateSql = sb.toString().substring(0, sb.toString().length()-1);
				sb.setLength(0);
				sb.append(updateSql).append(" WHERE 1=1 AND ")
				  .append(sqlCondition);
				System.out.println("ƽ̨����SQL��䣺============[" + sb.toString() + "]============");
				DBTemplate.getInstance(dataSourceName).executeSql(sb.toString());
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * ��ѯ
	 * @param dataSourceName	���ݿ�������
	 * @param tablName			���ʱ���·��
	 * @param sqlCondtions		��������SQL
	 * @return					��ѯ(���ز��������)
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
			System.out.println("ƽ̨��ѯSQL��䣺============[" + sb.toString() + "]============");
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
			//ɾ��
			String delSqlCondition = "ID='20170816100514102635629'";
			System.out.println(dao.delMapListTable("DMDataSource", "OPT.OPT_TICKET", delSqlCondition));
			
			//����
			Map<String, Object> addMap = new HashMap<String, Object>();
			addMap.put("ID", "20170816100514102635629");
			addMap.put("TICKETNUMBER", "20170001");
			addMap.put("OPERTASK", "500kV����#5������");
			addMap.put("TICKETSTATUS", "��Ʊ");
			addMap.put("TICKETTYPE", "����Ʊ");
			addMap.put("DATASTATUSID", "1");
			addMap.put("CREATEUSERID", "admin");
			addMap.put("CREATETIME", "2017-08-16 14:25:34");
			System.out.println(dao.addMapTable("DMDataSource","OPT.OPT_TICKET", "ID,TICKETNUMBER,OPERTASK,FASHION,OPENTICKETTIME,DRAFTERID,DRAFTER,AUDITORID,AUDITOR,RECVTICKETERID,RECVTICKETER,RECVTIME,TICKETSTATUS,TICKETTYPE,TICKETOPERSTARTTIME,TICKETOPERENTTIME,EXT1,EXT2,EXT3,EXT4,EXT5,CLASS,SORT,DATASTATUSID,CREATEUSERID,CREATETIME,UPDATEUSERID,UPDATETIME,REMARK", addMap));
		
			//��ѯ
			String findSqlCondition = "ID='20170816100514102635629'";
			List<Map<String, Object>> findMapList = dao.findMapListTable("DMDataSource","OPT.OPT_TICKET",findSqlCondition);
			for (Map<String, Object> map : findMapList){
				System.out.println(map.get("TICKETNUMBER"));
				System.out.println(map.get("TICKETSTATUS"));
				System.out.println(map.get("TICKETTYPE"));
				System.out.println(map.get("CREATEUSERID"));
				System.out.println(map.get("CREATETIME"));
			}
			
			//�޸�
			Map<String, Object> updateMap = new HashMap<String, Object>();
			updateMap.put("ID", "20170816100514102635629");
			updateMap.put("OPERTASK", "500kV����#4������");
			updateMap.put("TICKETSTATUS", "��Ʊ");
			updateMap.put("DATASTATUSID", "1");
			System.out.println(dao.updateMapTable("DMDataSource","OPT.OPT_TICKET","ID,TICKETNUMBER,OPERTASK,FASHION,OPENTICKETTIME,DRAFTERID,DRAFTER,AUDITORID,AUDITOR,RECVTICKETERID,RECVTICKETER,RECVTIME,TICKETSTATUS,TICKETTYPE,TICKETOPERSTARTTIME,TICKETOPERENTTIME,EXT1,EXT2,EXT3,EXT4,EXT5,CLASS,SORT,DATASTATUSID,CREATEUSERID,CREATETIME,UPDATEUSERID,UPDATETIME,REMARK","ID='20170816100514102635629'", updateMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
