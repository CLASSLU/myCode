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
import kd.idp.psidpapp.opt.ticket.dao.ItemDao;

import org.apache.commons.lang.StringUtils;


public class ItemService {
	
	private ItemDao itemDao;
	
	public ItemDao getItemDao() {
		return itemDao;
	}

	public void setItemDao(ItemDao itemDao) {
		this.itemDao = itemDao;
	}

	/**
	 * 获取对应操作票的所有操作项
	 * @param ticketId		操作票编号
	 * @return				返回一张操作票操作项集
	 * @throws Exception
	 */
	public List<Map<String, Object>> findItemList(String ticketId) throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		String appendSqlCondition = "";
		if (null != userMap.get("ROLEIDS") && StringUtils.isNotEmpty(String.valueOf(userMap.get("ROLEIDS")))){
			if (-1 == String.valueOf(userMap.get("ROLEIDS")).indexOf(",1,")){
				appendSqlCondition += " AND ID IN(SELECT ID FROM OPT.OPT_ITEM WHERE DATASTATUSID = '1') ";
			}
		}
		List<Map<String, Object>> itemMapList = null;
		try {
			if (StringUtils.isNotEmpty(ticketId)){
				itemMapList = itemDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_ITEM, "DATASTATUSID='1' AND TICKETID='" + ticketId + "'" + appendSqlCondition + " ORDER BY SORT ASC");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return itemMapList;
	}
	
	/**
	 * 获取对应操作票、操作序号的操作项信息
	 * @param ticketId		操作票ID
	 * @param itemSort		操作项序号
	 * @return				返回对应操作票、操作序号的操作项信息
	 * @throws Exception
	 */
	public Map<String, Object> findItem(String ticketId, String itemSort) throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		String appendSqlCondition = "";
		if (null != userMap.get("ROLEIDS") && StringUtils.isNotEmpty(String.valueOf(userMap.get("ROLEIDS")))){
			if (-1 == String.valueOf(userMap.get("ROLEIDS")).indexOf(",1,")){
				appendSqlCondition += " AND ID IN(SELECT ID FROM OPT.OPT_ITEM WHERE DATASTATUSID = '1' AND RECVUNITID='" + String.valueOf(userMap.get("ORGID")) + "') ";
			}
		}
		List<Map<String, Object>> itemMapList = null;
		try {
			if (StringUtils.isNotEmpty(ticketId)){
				itemMapList = itemDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_ITEM, "DATASTATUSID='1' AND TICKETID='" + ticketId + "' AND SORT='" + itemSort + "'" + appendSqlCondition + " ORDER BY SORT ASC");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return itemMapList.get(0);
	}
	
	/**
	 * 获取受票单位
	 * @param ticketId		操作票ID
	 * @return				获取受票单位
	 * @throws Exception
	 */
	public List<Map<String,Object>> getOperUnitMapList(String ticketId) throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		List<Map<String,Object>> mapList = null;
		try {
			if (StringUtils.isNotEmpty(ticketId)){
				mapList = itemDao.getOperUnitMapList(ticketId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return mapList;
	}
	/**
	 * [1、3区互通接口]获取受票单位
	 * @param ticketId		操作票ID
	 * @return				获取受票单位
	 * @throws Exception
	 */
	public List<Map<String,Object>> getOperUnitMapList2(String ticketId) throws Exception{
		List<Map<String,Object>> mapList = null;
		try {
			if (StringUtils.isNotEmpty(ticketId)){
				mapList = itemDao.getOperUnitMapList(ticketId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return mapList;
	}
	
	/**
	 * 创建操作项
	 * @param  ticketId		操作票ID
	 * @return				返回新建操作项信息
	 * @throws Exception
	 */
	public Map<String, Object> createItem(String ticketId)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		Map<String, Object> itemMap = new HashMap<String, Object>();
		String id = "";
		long sort = 1;
		int dataStatusId = 1;
		String createUserId = String.valueOf(userMap.get("ID"));
		String createTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
		try {
			if (null != ticketId && !"".equals(ticketId)){
				//生成唯一主键ID
				id = SerialService.getSerialIdByDate("");
				//创建序号
				List<Map<String,Object>> itemMapList = findItemList(ticketId);
				if (null == itemMapList || 0 == itemMapList.size()){
					sort = 1;
				} else {
					sort = Integer.parseInt(String.valueOf(itemMapList.get(itemMapList.size()-1).get("SORT"))) + 1;
				}
				//新增数据
				itemMap.put("ID", id);
				itemMap.put("TICKETID", ticketId);
				itemMap.put("SORT", sort);
				itemMap.put("DATASTATUSID", dataStatusId);
				itemMap.put("CREATEUSERID", createUserId);
				itemMap.put("CREATETIME", createTime);
				itemDao.addMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_ITEM, FieldName.FIELD_OPT_OPT_ITEM, itemMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return itemMap;
	}
	
	/**
	 * 插入操作项
	 * @param  ticketId		操作票ID
	 * @param  ticketId		操作票ID
	 * @return				返回插入操作项信息
	 * @throws Exception
	 */
	public Map<String, Object> insertItem(String ticketId, String suffIndex)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		Map<String, Object> itemMap = new HashMap<String, Object>();
		String id = "";
		long sort = Long.parseLong(suffIndex);
		int dataStatusId = 1;
		String createUserId = String.valueOf(userMap.get("ID"));
		String createTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
		try {
			if (null != ticketId && !"".equals(ticketId)){
				//生成唯一主键ID
				id = SerialService.getSerialIdByDate("");
				//更新>=suffIndex序号+1
				itemDao.updateSortAdd1(ticketId, sort);
				//新增数据
				itemMap.put("ID", id);
				itemMap.put("TICKETID", ticketId);
				itemMap.put("SORT", sort);
				itemMap.put("DATASTATUSID", dataStatusId);
				itemMap.put("CREATEUSERID", createUserId);
				itemMap.put("CREATETIME", createTime);
				itemDao.addMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_ITEM, FieldName.FIELD_OPT_OPT_ITEM, itemMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return itemMap;
	}
	
	
	

	/**
	 * 更新操作项信息
	 * @param itemMap		操作项更新信息	
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean updateItem(Map<String,Object> itemMap)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		Boolean result = false;
		String updateUserId = String.valueOf(userMap.get("ID"));
		String updateTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
		try {
			if (null != itemMap && null != itemMap.get("ID") && StringUtils.isNotEmpty(String.valueOf(itemMap.get("ID")))){
				String id = String.valueOf(itemMap.get("ID"));
				itemMap.put("UPDATEUSERID", updateUserId);
				itemMap.put("UPDATETIME", updateTime);
				result = itemDao.updateMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_ITEM, FieldName.FIELD_OPT_OPT_ITEM, "ID='" + id + "'", itemMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	/**
	 * 更新操作项信息(根据操作票ID和序号)
	 * @param itemMap	操作项更新信息
	 * @return
	 * @throws Exception
	 */
	public Boolean updateItemByTicketId2Sort(Map<String,Object> itemMap)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		Boolean result = false;
		String updateUserId = String.valueOf(userMap.get("ID"));
		String updateTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
		try {
			if (null != itemMap && null != itemMap.get("TICKETID") && StringUtils.isNotEmpty(String.valueOf(itemMap.get("TICKETID"))) && null != itemMap.get("SORT") && StringUtils.isNotEmpty(String.valueOf(itemMap.get("SORT")))){
				itemMap.put("UPDATEUSERID", updateUserId);
				itemMap.put("UPDATETIME", updateTime);
				result = itemDao.updateMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_ITEM, FieldName.FIELD_OPT_OPT_ITEM, "DATASTATUSID='1' AND TICKETID='" + itemMap.get("TICKETID") + "' AND SORT='" + itemMap.get("SORT") + "'", itemMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	/**
	 * 删除操作项
	 * @param ids			操作项ID集
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean delItemList(String ids) throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		Boolean result = false;
		try {
			if (StringUtils.isNotEmpty(ids)){
				String sqlConditions = "ID IN(";
				String []idArray = ids.split(",");
				for (String id : idArray){
					sqlConditions += "'" + id + "',";
				}
				if (!"ID IN(".equals(sqlConditions)){
					sqlConditions = sqlConditions.substring(0,sqlConditions.length()-1) + ")";
					result = itemDao.delMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_ITEM, sqlConditions);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	/**
	 * 校正操作项编号(删除后需调用)
	 * @param ticketId		操作票ID
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean checkItemListSort(String ticketId) throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		Boolean result = true;
		try {
			List<Map<String, Object>> itemMapList = findItemList(ticketId);
			if (null != itemMapList){
				for (int i=0; i<itemMapList.size(); i++){
					Map<String, Object> itemMap = itemMapList.get(i);
					itemMap.put("SORT", i+1);
					updateItem(itemMap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	
	/**
	 * 拟票、审核、下发、执行、检查（仅限于前置状态），清除将执行阶段信息（方便运维）
	 * @param ticketId	操作票ID
	 * @param srcStatus	源状态
	 * @param destStatus	目标状态
	 * @param userMap		当前用户信息
	 * @return	拟票、审核、下发、执行、检查（仅限于前置状态），清除将执行阶段信息（方便运维）
	 * @throws Exception
	 */
	public boolean ticketStatusBH2ClearTicketInfo(String ticketId, String srcStatus, String destStatus, Map<String, Object> userMap)throws Exception{
		try {
			Map<String, Object> itemMap = new HashMap<String, Object>();
			if ("拟票".equals(destStatus) && -1 != "审核,下发,执行,检查,合格,缺陷,作废".indexOf(srcStatus)){
				itemMap.put("SENDLTIME", "");
				itemMap.put("SENDLUSERID", "");
				itemMap.put("SENDLUSER", "");
				itemMap.put("RECVLUSERID", "");
				itemMap.put("RECVLUSER", "");
				itemMap.put("OVERLTIME", "");
				itemMap.put("CALLLTIME", "");
				itemMap.put("CALLLUSERID", "");
				itemMap.put("CALLLUSER", "");
				itemMap.put("GETLUSERID", "");
				itemMap.put("GETLUSER", "");
				itemMap.put("JHRLUSERID", "");
				itemMap.put("JHRLUSER", "");
				itemMap.put("OPERLSTATUS", "");
				itemMap.put("REMARK", "");
				itemMap.put("UPDATETIME", DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
				itemMap.put("UPDATEUSERID", userMap.get("ID"));
				return itemDao.updateMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_ITEM, FieldName.FIELD_OPT_OPT_ITEM, "TICKETID='" + ticketId + "'", itemMap);
			}else if ("审核".equals(destStatus) && -1 != "下发,执行,检查,合格,缺陷,作废".indexOf(srcStatus)){
				itemMap.put("SENDLTIME", "");
				itemMap.put("SENDLUSERID", "");
				itemMap.put("SENDLUSER", "");
				itemMap.put("RECVLUSERID", "");
				itemMap.put("RECVLUSER", "");
				itemMap.put("OVERLTIME", "");
				itemMap.put("CALLLTIME", "");
				itemMap.put("CALLLUSERID", "");
				itemMap.put("CALLLUSER", "");
				itemMap.put("GETLUSERID", "");
				itemMap.put("GETLUSER", "");
				itemMap.put("JHRLUSERID", "");
				itemMap.put("JHRLUSER", "");
				itemMap.put("OPERLSTATUS", "");
				itemMap.put("REMARK", "");
				itemMap.put("UPDATETIME", DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
				itemMap.put("UPDATEUSERID", userMap.get("ID"));
				return itemDao.updateMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_ITEM, FieldName.FIELD_OPT_OPT_ITEM, "TICKETID='" + ticketId + "'", itemMap);
			}else if ("下发".equals(destStatus) && -1 != "执行,检查,合格,缺陷,作废".indexOf(srcStatus)){
				itemMap.put("SENDLTIME", "");
				itemMap.put("SENDLUSERID", "");
				itemMap.put("SENDLUSER", "");
				itemMap.put("RECVLUSERID", "");
				itemMap.put("RECVLUSER", "");
				itemMap.put("OVERLTIME", "");
				itemMap.put("CALLLTIME", "");
				itemMap.put("CALLLUSERID", "");
				itemMap.put("CALLLUSER", "");
				itemMap.put("GETLUSERID", "");
				itemMap.put("GETLUSER", "");
				itemMap.put("JHRLUSERID", "");
				itemMap.put("JHRLUSER", "");
				itemMap.put("OPERLSTATUS", "");
				itemMap.put("REMARK", "");
				itemMap.put("UPDATETIME", DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
				itemMap.put("UPDATEUSERID", userMap.get("ID"));
				return itemDao.updateMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_ITEM, FieldName.FIELD_OPT_OPT_ITEM, "TICKETID='" + ticketId + "'", itemMap);
			}else if ("执行".equals(destStatus) && -1 != "检查,合格,缺陷,作废".indexOf(srcStatus)){
				itemMap.put("SENDLTIME", "");
				itemMap.put("SENDLUSERID", "");
				itemMap.put("SENDLUSER", "");
				itemMap.put("RECVLUSERID", "");
				itemMap.put("RECVLUSER", "");
				itemMap.put("OVERLTIME", "");
				itemMap.put("CALLLTIME", "");
				itemMap.put("CALLLUSERID", "");
				itemMap.put("CALLLUSER", "");
				itemMap.put("GETLUSERID", "");
				itemMap.put("GETLUSER", "");
				itemMap.put("JHRLUSERID", "");
				itemMap.put("JHRLUSER", "");
				itemMap.put("OPERLSTATUS", "");
				itemMap.put("REMARK", "");
				itemMap.put("UPDATETIME", DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
				itemMap.put("UPDATEUSERID", userMap.get("ID"));
				return itemDao.updateMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_ITEM, FieldName.FIELD_OPT_OPT_ITEM, "TICKETID='" + ticketId + "'", itemMap);
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
}
