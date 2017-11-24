package kd.idp.psidpapp.opt.ticket.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.spring.dbservice.DBTemplate;

import kd.idp.psidpapp.opt.sys.dao.BaseDao;
import kd.idp.psidpapp.opt.ticket.constant.DataSourceName;
import kd.idp.psidpapp.opt.ticket.constant.TableName;

public class TicketSearchDao extends BaseDao {

	
	/**
	 * ��ȡ����Ʊ����/�¡���
	 * @param sqlCondition  ��������
	 * @return	��ȡ����Ʊ����/�¡���
	 * @throws Exception
	 */
	public Map<String,Object> getTicketTreeDataMap(String sqlCondition) throws Exception{
		Map<String, Object> rootNoteMap = new HashMap<String, Object>();
		List<Map<String, Object>> childMapList = new ArrayList<Map<String, Object>>();
		StringBuilder sb = new StringBuilder("");
		rootNoteMap.put("ID", "root");
		rootNoteMap.put("NAME", "��/��");
		rootNoteMap.put("PARENTID", null);
		rootNoteMap.put("TYPE", "root");
		rootNoteMap.put("children", childMapList);
		rootNoteMap.put("ISLEAF", false);
		try {
			sb.setLength(0);
			sb.append("SELECT DISTINCT SUBSTR(COALESCE(CASE WHEN(OPENTICKETTIME='') THEN NULL ELSE OPENTICKETTIME END,CREATETIME),0,4) OPENTICKETTIME FROM ").append(TableName.TABLE_OPT_OPT_TICKET).append(" WHERE DATASTATUSID='1'").append(sqlCondition);
			List<Map<String, Object>> yearMapList = DBTemplate.getInstance(DataSourceName.DATASOURCE_DM).getResultMapList(sb.toString());
			sb.setLength(0);
			sb.append("SELECT DISTINCT SUBSTR(COALESCE(CASE WHEN(OPENTICKETTIME='') THEN NULL ELSE OPENTICKETTIME END,CREATETIME),0,7) OPENTICKETTIME FROM ").append(TableName.TABLE_OPT_OPT_TICKET).append(" WHERE DATASTATUSID='1'").append(sqlCondition);
			List<Map<String, Object>> monthMapList = DBTemplate.getInstance(DataSourceName.DATASOURCE_DM).getResultMapList(sb.toString());
			for (Map<String, Object> yearMap : yearMapList){
				String year = String.valueOf(yearMap.get("OPENTICKETTIME"));
				Map<String, Object> yearNoteMap = new HashMap<String, Object>();
				List<Map<String, Object>> yearChildMapList = new ArrayList<Map<String, Object>>();
				yearNoteMap.put("ID", year);
				yearNoteMap.put("NAME", year + "��");
				yearNoteMap.put("PARENTID", "root");
				yearNoteMap.put("TYPE", "year");
				yearNoteMap.put("children", yearChildMapList);
				yearNoteMap.put("ISLEAF", false);
				for (Map<String, Object> monthMap : monthMapList){
					String month = String.valueOf(monthMap.get("OPENTICKETTIME"));
					if (-1 != month.indexOf(year)){
						Map<String, Object> monthNoteMap = new HashMap<String, Object>();
						String tempMonth = String.valueOf(Integer.parseInt(month.substring(5,7)));
						monthNoteMap.put("ID", month);
						monthNoteMap.put("NAME", tempMonth + "��");
						monthNoteMap.put("PARENTID", year);
						monthNoteMap.put("TYPE", "month");
						monthNoteMap.put("children", null);
						monthNoteMap.put("ISLEAF", true);
						yearChildMapList.add(monthNoteMap);
					}
				}
				childMapList.add(yearNoteMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return rootNoteMap;
	}
	
	/**
	 * ��ȡ����Ʊͨ����ҳ����
	 * @param pageSize  ÿҳ��Ŀ
	 * @param currentPage  ��ȡ��Nҳ
	 * @param sqlCondition  ��������
	 * @return		��ȡ����Ʊͨ����ҳ����
	 * @throws Exception
	 */
	public Map<String,Object> getTickets2pageMapByPage(int pageSize, int currentPage, String sqlCondition) throws Exception{
		Map<String, Object> pageDataMap = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder("");
		try {
			sb.setLength(0);
			sb.append("SELECT * FROM ").append(TableName.TABLE_OPT_OPT_TICKET).append(" WHERE DATASTATUSID='1'").append(sqlCondition).append(" ORDER BY TICKETNUMBER DESC");
			pageDataMap = findDataMapByPage(pageSize, currentPage, sb.toString(), DataSourceName.DATASOURCE_DM);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return pageDataMap;
	}
	
	
}
