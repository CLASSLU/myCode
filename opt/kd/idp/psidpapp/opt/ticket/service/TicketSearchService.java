package kd.idp.psidpapp.opt.ticket.service;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import kd.idp.psidpapp.opt.sys.flex.FlexUtil;
import kd.idp.psidpapp.opt.ticket.dao.TicketSearchDao;

public class TicketSearchService {
	
	private TicketSearchDao ticketSearchDao;

	public TicketSearchDao getTicketSearchDao() {
		return ticketSearchDao;
	}

	public void setTicketSearchDao(TicketSearchDao ticketSearchDao) {
		this.ticketSearchDao = ticketSearchDao;
	}



	/**
	 * ��ȡ����Ʊ����/�¡���
	 * @return	��ȡ����Ʊ����/�¡���
	 * @throws Exception
	 */
	public Map<String,Object> getTicketTreeDataMap(boolean isGd) throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		String appendSqlCondition = "";
		if (null != userMap.get("ROLEIDS") && StringUtils.isNotEmpty(String.valueOf(userMap.get("ROLEIDS")))){
			if (-1 == String.valueOf(userMap.get("ROLEIDS")).indexOf(",1,")){
				appendSqlCondition += " AND ID IN(SELECT DISTINCT TICKETID FROM OPT.OPT_RECVTICEKT WHERE DATASTATUSID = '1' AND RECVUNITID='" + String.valueOf(userMap.get("ORGID")) + "') ";
			}
		}
		if (isGd){
			appendSqlCondition += " AND TICKETSTATUS IN('ȱ��','�ϸ�') ";
		}
		Map<String, Object> treeDataMap = null;
		try {
			treeDataMap = ticketSearchDao.getTicketTreeDataMap(appendSqlCondition);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return treeDataMap;
	}
	
	/**
	 * ��ȡ����Ʊͨ����ҳ����
	 * @param pageCondtionMap  ��ҳ����
	 * @return		��ȡ����Ʊͨ����ҳ����
	 * @throws Exception
	 */
	public Map<String,Object> getTickets2pageMapByPage(Map<String,Object> pageCondtionMap) throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		String appendSqlCondition = "";
		if (null != userMap.get("ROLEIDS") && StringUtils.isNotEmpty(String.valueOf(userMap.get("ROLEIDS")))){
			if (-1 == String.valueOf(userMap.get("ROLEIDS")).indexOf(",1,")){
				appendSqlCondition += " AND ID IN(SELECT DISTINCT TICKETID FROM OPT.OPT_RECVTICEKT WHERE DATASTATUSID = '1' AND RECVUNITID='" + String.valueOf(userMap.get("ORGID")) + "') ";
			}
		}
		Map<String, Object> pageMap = null;
		try {
			if (null != pageCondtionMap){
				if (null != pageCondtionMap.get("isGd") && Boolean.valueOf(String.valueOf(pageCondtionMap.get("isGd")))){
					appendSqlCondition += " AND TICKETSTATUS IN('ȱ��','�ϸ�') ";
				}
				int pageSize = Integer.parseInt(String.valueOf(pageCondtionMap.get("pageSize")));
				int currentPage = Integer.parseInt(String.valueOf(pageCondtionMap.get("currentPage")));
				String sqlCondition = String.valueOf((null == pageCondtionMap.get("sqlCondition")) ? "" : pageCondtionMap.get("sqlCondition"));
				if (StringUtils.isNotEmpty(sqlCondition)){
					appendSqlCondition += sqlCondition;
				}
				pageMap = ticketSearchDao.getTickets2pageMapByPage(pageSize, currentPage, appendSqlCondition);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return pageMap;
	}

}
