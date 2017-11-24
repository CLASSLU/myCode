package kd.idp.psidpapp.opt.ticket.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kd.idp.psidpapp.opt.sys.flex.FlexUtil;
import kd.idp.psidpapp.opt.sys.serial.service.SerialService;
import kd.idp.psidpapp.opt.sys.util.DateUtil;
import kd.idp.psidpapp.opt.ticket.constant.DataSourceName;
import kd.idp.psidpapp.opt.ticket.constant.FieldName;
import kd.idp.psidpapp.opt.ticket.constant.TableName;
import kd.idp.psidpapp.opt.ticket.dao.RecvTicketDao;

import org.apache.commons.lang.StringUtils;

import com.spring.SpringBeanFactory;

public class RecvTicketService {

	private RecvTicketDao recvTicketDao;
	
	public RecvTicketDao getRecvTicketDao() {
		return recvTicketDao;
	}

	public void setRecvTicketDao(RecvTicketDao recvTicketDao) {
		this.recvTicketDao = recvTicketDao;
	}

	/**
	 * 获取对应操作票的所有受票信息
	 * @param ticketId		操作票ID
	 * @return				返回一张操作票受票集
	 * @throws Exception
	 */
	public List<Map<String, Object>> findRecvTicketList(String ticketId) throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		String appendSqlCondition = "";
		if (null != userMap.get("ROLEIDS") && StringUtils.isNotEmpty(String.valueOf(userMap.get("ROLEIDS")))){
			if (-1 == String.valueOf(userMap.get("ROLEIDS")).indexOf(",1,")){
				appendSqlCondition += " AND ID IN(SELECT ID FROM OPT.OPT_RECVTICEKT WHERE DATASTATUSID = '1' AND RECVUNITID='" + String.valueOf(userMap.get("ORGID")) + "') ";
			}
		}
		List<Map<String, Object>> recvTicketMapList = null;
		try {
			if (StringUtils.isNotEmpty(ticketId)){
				recvTicketMapList = recvTicketDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_RECVTICEKT, "DATASTATUSID='1' AND TICKETID='" + ticketId + "'" + appendSqlCondition + " ORDER BY SORT ASC");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return recvTicketMapList;
	}
	
	
	/**
	 * 审票完毕后创建受票信息
	 * @param ticketId		操作票ID
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean createRecvTicketList(String ticketId)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		String recvTicketStatus = "未签收";
		int dataStatusId = 1;
		String createUserId = String.valueOf(userMap.get("ID"));
		String createTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
		try {
			if (StringUtils.isNotEmpty(ticketId)){
				//1、先删除受票信息集
				delRecvTicketList(ticketId);
				//2、再插入受票信息集
				ItemService itemService = (ItemService)SpringBeanFactory.getInstance().getBean("itemService");
				List<Map<String, Object>> itemMapList = itemService.getOperUnitMapList(ticketId);
				for (int i=0; i<itemMapList.size(); i++){
					Map<String,Object> recvTicketMap = new HashMap<String, Object>();
					Map<String, Object> itemMap = itemMapList.get(i);
					//生成唯一主键ID
					String id = SerialService.getSerialIdByDate("");
					String operunitId = String.valueOf(itemMap.get("OPERUNITID"));
					String operUnit = String.valueOf(itemMap.get("OPERUNIT"));
					String recvUnitId = String.valueOf(itemMap.get("RECVUNITID"));
					int sort = i + 1;
					recvTicketMap.put("ID", id);
					recvTicketMap.put("OPERUNITID", operunitId);
					recvTicketMap.put("OPERUNIT", operUnit);
					recvTicketMap.put("RECVUNITID", recvUnitId);
					recvTicketMap.put("RECVTICKETSTATUS", recvTicketStatus);
					recvTicketMap.put("TICKETID", ticketId);
					recvTicketMap.put("SORT", sort);
					recvTicketMap.put("DATASTATUSID", dataStatusId);
					recvTicketMap.put("CREATEUSERID", createUserId);
					recvTicketMap.put("CREATETIME", createTime);
					recvTicketDao.addMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_RECVTICEKT, FieldName.FIELD_OPT_OPT_RECVTICEKT, recvTicketMap);
				}
				//3、更新操作开票时间
				Map<String, Object> ticketMap = new HashMap<String, Object>();
				ticketMap.put("ID", ticketId);
				ticketMap.put("OPENTICKETTIME", createTime);
				((TicketService)SpringBeanFactory.getInstance().getBean("ticketService")).updateTicket(ticketMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return true;
	}
	/**
	 * [1、3区互通接口]审票完毕后创建受票信息
	 * @param ticketId		操作票ID
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean createRecvTicketList2(String ticketId)throws Exception{
		String recvTicketStatus = "未签收";
		int dataStatusId = 1;
		String createUserId = "admin";
		String createTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
		try {
			if (StringUtils.isNotEmpty(ticketId)){
				//1、先删除受票信息集
				delRecvTicketList2(ticketId);
				//2、再插入受票信息集
				ItemService itemService = (ItemService)SpringBeanFactory.getInstance().getBean("itemService");
				List<Map<String, Object>> itemMapList = itemService.getOperUnitMapList2(ticketId);
				for (int i=0; i<itemMapList.size(); i++){
					Map<String,Object> recvTicketMap = new HashMap<String, Object>();
					Map<String, Object> itemMap = itemMapList.get(i);
					//生成唯一主键ID
					String id = SerialService.getSerialIdByDate("");
					String operunitId = String.valueOf(itemMap.get("OPERUNITID"));
					String operUnit = String.valueOf(itemMap.get("OPERUNIT"));
					String recvUnitId = String.valueOf(itemMap.get("RECVUNITID"));
					int sort = i + 1;
					recvTicketMap.put("ID", id);
					recvTicketMap.put("OPERUNITID", operunitId);
					recvTicketMap.put("OPERUNIT", operUnit);
					recvTicketMap.put("RECVUNITID", recvUnitId);
					recvTicketMap.put("RECVTICKETSTATUS", recvTicketStatus);
					recvTicketMap.put("TICKETID", ticketId);
					recvTicketMap.put("SORT", sort);
					recvTicketMap.put("DATASTATUSID", dataStatusId);
					recvTicketMap.put("CREATEUSERID", createUserId);
					recvTicketMap.put("CREATETIME", createTime);
					recvTicketDao.addMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_RECVTICEKT, FieldName.FIELD_OPT_OPT_RECVTICEKT, recvTicketMap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return true;
	}
	
	/**
	 * 拟票、审核、下发、执行、检查（仅限于前置状态），清除将执行阶段信息（方便运维）
	 * @param ticketId		操作票ID
	 * @param srcStatus		源状态
	 * @param destStatus	目标状态
	 * @param userMap		当前用户信息
	 * @return	拟票、审核、下发、执行、检查（仅限于前置状态），清除将执行阶段信息（方便运维）
	 * @throws Exception
	 */
	public boolean ticketStatusBH2ClearTicketInfo(String ticketId, String srcStatus, String destStatus, Map<String, Object> userMap)throws Exception{
		try {
			if ("拟票".equals(destStatus) && -1 != "审核,下发,执行,检查,合格,缺陷,作废".indexOf(srcStatus)){
				return delRecvTicketList(ticketId);
			}else if ("审核".equals(destStatus) && -1 != "下发,执行,检查,合格,缺陷,作废".indexOf(srcStatus)){
				return delRecvTicketList(ticketId);
			}else if ("下发".equals(destStatus) && -1 != "执行,检查,合格,缺陷,作废".indexOf(srcStatus)){
				return createRecvTicketList(ticketId);
			}else if ("执行".equals(destStatus) && -1 != "检查,合格,缺陷,作废".indexOf(srcStatus)){
				Map<String, Object> recvTicketMap = new HashMap<String, Object>();
				recvTicketMap.put("RECVTICKETSTATUS", "已签收");
				return recvTicketDao.updateMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_RECVTICEKT, FieldName.FIELD_OPT_OPT_RECVTICEKT, "TICKETID='" + ticketId + "'", recvTicketMap);
			}else if ("检查".equals(destStatus) && -1 != "合格,缺陷,作废".indexOf(srcStatus)){
				return true;
			}else{
				return false;
			}	
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 更新受票信息
	 * @param  recvTicketMap		前端受票信息
	 * @return						返回操作真假
	 * @throws Exception
	 */
	public Boolean updateRecvTicket(Map<String,Object> recvTicketMap)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		Boolean result = false;
		String updateUserId = String.valueOf(userMap.get("ID"));
		String updateTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
		try {
			if (null != recvTicketMap && null != recvTicketMap.get("ID") && StringUtils.isNotEmpty(String.valueOf(recvTicketMap.get("ID")))){
				String id = String.valueOf(recvTicketMap.get("ID"));
				recvTicketMap.put("UPDATEUSERID", updateUserId);
				recvTicketMap.put("UPDATETIME", updateTime);
				result = recvTicketDao.updateMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_RECVTICEKT, FieldName.FIELD_OPT_OPT_RECVTICEKT, "ID='" + id + "'", recvTicketMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	/**
	 * 删除受票信息
	 * @param ticketIds		操作票ID
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean delRecvTicketList(String ticketIds) throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		Boolean result = false;
		try {
			if (StringUtils.isNotEmpty(ticketIds)){
				String sqlConditions = "TICKETID IN(";
				String []ticketIdArray = ticketIds.split(",");
				for (String tempTicketId : ticketIdArray){
					sqlConditions += "'" + tempTicketId + "',";
				}
				if (!"TICKETID IN(".equals(sqlConditions)){
					sqlConditions = sqlConditions.substring(0,sqlConditions.length()-1) + ")";
					result = recvTicketDao.delMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_RECVTICEKT, sqlConditions);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	/**
	 * [1、3区互通接口]删除受票信息
	 * @param ticketIds		操作票ID
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean delRecvTicketList2(String ticketIds) throws Exception{
		Boolean result = false;
		try {
			if (StringUtils.isNotEmpty(ticketIds)){
				String sqlConditions = "TICKETID IN(";
				String []ticketIdArray = ticketIds.split(",");
				for (String tempTicketId : ticketIdArray){
					sqlConditions += "'" + tempTicketId + "',";
				}
				if (!"TICKETID IN(".equals(sqlConditions)){
					sqlConditions = sqlConditions.substring(0,sqlConditions.length()-1) + ")";
					result = recvTicketDao.delMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_RECVTICEKT, sqlConditions);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
}
