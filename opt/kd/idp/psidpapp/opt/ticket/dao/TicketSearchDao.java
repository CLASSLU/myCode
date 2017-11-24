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
	 * 获取操作票【年/月】树
	 * @param sqlCondition  过滤条件
	 * @return	获取操作票【年/月】树
	 * @throws Exception
	 */
	public Map<String,Object> getTicketTreeDataMap(String sqlCondition) throws Exception{
		Map<String, Object> rootNoteMap = new HashMap<String, Object>();
		List<Map<String, Object>> childMapList = new ArrayList<Map<String, Object>>();
		StringBuilder sb = new StringBuilder("");
		rootNoteMap.put("ID", "root");
		rootNoteMap.put("NAME", "年/月");
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
				yearNoteMap.put("NAME", year + "年");
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
						monthNoteMap.put("NAME", tempMonth + "月");
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
	 * 获取操作票通过分页条件
	 * @param pageSize  每页条目
	 * @param currentPage  获取第N页
	 * @param sqlCondition  过滤条件
	 * @return		获取操作票通过分页条件
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
