package kd.idp.psidpapp.opt.ticket.dao;

import java.util.List;
import java.util.Map;

import com.spring.dbservice.DBTemplate;

import kd.idp.psidpapp.opt.sys.dao.BaseDao;
import kd.idp.psidpapp.opt.ticket.constant.DataSourceName;
import kd.idp.psidpapp.opt.ticket.constant.TableName;

public class ItemDao extends BaseDao{

	/**
	 * @param ticketId		操作票ID
	 * @param suffIndex		新增插入位置SORT
	 * @return
	 * @throws Exception
	 */
	public boolean updateSortAdd1(String ticketId, long suffIndex) throws Exception{
		StringBuilder sb = new StringBuilder("");
		try {
			sb.setLength(0);
			sb.append("UPDATE ").append(TableName.TABLE_OPT_OPT_ITEM).append(" SET SORT=(SORT+1) ")
			  .append("WHERE DATASTATUSID='1' AND TICKETID='").append(ticketId)
			  .append("' AND SORT>='").append(suffIndex).append("'");
			DBTemplate.getInstance(DataSourceName.DATASOURCE_DM).executeSql(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return true;
	}
	
	/**
	 * 获取受票单位
	 * @param ticketId		操作票ID
	 * @return				获取受票单位
	 * @throws Exception
	 */
	public List<Map<String,Object>> getOperUnitMapList(String ticketId) throws Exception{
		List<Map<String,Object>> mapList = null;
		StringBuilder sb = new StringBuilder("");
		try {
			sb.setLength(0);
			sb.append("SELECT OPERUNITID, MAX(OPERUNIT) AS OPERUNIT, MAX(RECVUNITID) AS RECVUNITID FROM (SELECT OPERUNITID,OPERUNIT,RECVUNITID FROM ").append(TableName.TABLE_OPT_OPT_ITEM)
			  .append(" WHERE DATASTATUSID='1' AND TICKETID='").append(ticketId).append("' ORDER BY SORT ASC) GROUP BY OPERUNITID");
			mapList = DBTemplate.getInstance(DataSourceName.DATASOURCE_DM).getResultMapList(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return mapList;
	}
}
