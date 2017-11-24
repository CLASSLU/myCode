package kd.idp.psidpapp.opt.ticket.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


import com.spring.SpringBeanFactory;

import kd.idp.psidpapp.opt.sys.flex.FlexUtil;
import kd.idp.psidpapp.opt.sys.serial.service.SerialService;
import kd.idp.psidpapp.opt.sys.util.DateUtil;
import kd.idp.psidpapp.opt.ticket.constant.DataSourceName;
import kd.idp.psidpapp.opt.ticket.constant.FieldName;
import kd.idp.psidpapp.opt.ticket.constant.TableName;
import kd.idp.psidpapp.opt.ticket.dao.OrderDao;

public class OrderService {
	
	private OrderDao orderDao;

	public OrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}
	
	/**
	 * 获取对应操作票的所有指令信息
	 * @param ticketId		操作票编号
	 * @return				返回一张操作票指令集
	 * @throws Exception
	 */
	public List<Map<String, Object>> findOrderList(String ticketId) throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		String appendSqlCondition = "";
		if (null != userMap.get("ROLEIDS") && StringUtils.isNotEmpty(String.valueOf(userMap.get("ROLEIDS")))){
			if (-1 == String.valueOf(userMap.get("ROLEIDS")).indexOf(",1,")){
				appendSqlCondition += " AND ID IN(SELECT ID FROM OPT.OPT_ORDER WHERE DATASTATUSID = '1' AND RECVUNITID='" + String.valueOf(userMap.get("ORGID")) + "') ";
			}
		}
		List<Map<String, Object>> orderMapList = null;
		try {
			if (StringUtils.isNotEmpty(ticketId)){
				orderMapList = orderDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_ORDER, "DATASTATUSID='1' AND TICKETID='" + ticketId + "'" + appendSqlCondition + " ORDER BY SORT ASC");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return orderMapList;
	}
	
	/**
	 * 创建操作令
	 * @param  orderMap		指令信息
	 * @return				返回新建操作令信息
	 * @throws Exception
	 */
	public Map<String, Object> createOrder(Map<String, Object> orderMap)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		String id = "";
		String name = "";
		long sort = 1;
		int dataStatusId = 1;
		String createUserId = String.valueOf(userMap.get("ID"));
		String createTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
		try {
			if (null != orderMap && null != orderMap.get("TICKETID") && StringUtils.isNotEmpty(String.valueOf(orderMap.get("TICKETID")))){
				//生成唯一主键ID
				id = SerialService.getSerialIdByDate("");
				//创建序号
				List<Map<String,Object>> orderMapList = findOrderList(String.valueOf(orderMap.get("TICKETID")));
				if (null == orderMapList || 0 == orderMapList.size()){
					sort = 1;
				} else {
					sort = Integer.parseInt(String.valueOf(orderMapList.get(orderMapList.size()-1).get("SORT"))) + 1;
				}
				name = "【" + String.valueOf(orderMap.get("ITEMSORT")) + "】令";
				//新增数据
				orderMap.put("ID", id);
				orderMap.put("NAME", name);
				orderMap.put("SORT", sort);
				orderMap.put("DATASTATUSID", dataStatusId);
				orderMap.put("CREATEUSERID", createUserId);
				orderMap.put("CREATETIME", createTime);
				orderDao.addMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_ORDER, FieldName.FIELD_OPT_OPT_ORDER, orderMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return orderMap;
	}
	
	/**
	 * 更新操作令
	 * @param  orderMap		前端操作令信息
	 * @return				更新操作令信息
	 * @throws Exception
	 */
	public Boolean updateOrder(Map<String,Object> orderMap)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		Boolean result = false;
		String updateUserId = String.valueOf(userMap.get("ID"));
		String updateTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
		try {
			if (null != orderMap && StringUtils.isNotEmpty(String.valueOf(orderMap.get("ID")))){
				String id = String.valueOf(orderMap.get("ID"));
				orderMap.put("UPDATEUSERID", updateUserId);
				orderMap.put("UPDATETIME", updateTime);
				result = orderDao.updateMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_ORDER, FieldName.FIELD_OPT_OPT_ORDER, "ID='" + id + "'", orderMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	/**
	 * 删除操作令
	 * @param ids			操作令ID集
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean delOrderList(String ids) throws Exception{
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
					result = orderDao.delMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_ORDER, sqlConditions);
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
				return orderDao.delMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_ORDER, "TICKETID='" + ticketId + "'");
			}else if ("审核".equals(destStatus) && -1 != "下发,执行,检查,合格,缺陷,作废".indexOf(srcStatus)){
				return orderDao.delMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_ORDER, "TICKETID='" + ticketId + "'");
			}else if ("下发".equals(destStatus) && -1 != "执行,检查,合格,缺陷,作废".indexOf(srcStatus)){
				return orderDao.delMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_ORDER, "TICKETID='" + ticketId + "'");
			}else if ("执行".equals(destStatus) && -1 != "检查,合格,缺陷,作废".indexOf(srcStatus)){
				return orderDao.delMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_ORDER, "TICKETID='" + ticketId + "'");
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
	 * 发令操作（发令、作废、跳过）
	 * @param ticketStatus		操作票状态
	 * @param orderMap			指令信息
	 * @param operOrderStatus	将操作的状态（发令、作废、跳过）
	 * @return					返回发令成功后指令信息
	 * @throws Exception
	 */
	public Map<String,Object> checkTicketInfoStatus2SendOrder(String ticketStatus, Map<String, Object> orderMap, String operOrderStatus) throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		Map<String, Object> rsultMap = null;
		String sendLTime = DateUtil.getCurrentDate("HH:mm");
		try {
			if (StringUtils.isNotEmpty(ticketStatus) && StringUtils.isNotEmpty(operOrderStatus) && null != orderMap){
				//1、判断票状态是否对应
				Map<String, Object> ticketMap = ((TicketService)SpringBeanFactory.getInstance().getBean("ticketService")).findTicket(String.valueOf(orderMap.get("TICKETID")));
				if (!ticketStatus.equals(ticketMap.get("TICKETSTATUS"))){
					return null;
				}
				//2、判断操作项目状态是否对应
				String[] itemSortArray = String.valueOf(orderMap.get("ITEMSORT")).split("，");
				for (String tempSort : itemSortArray){
					Map<String, Object> itemMap = ((ItemService)SpringBeanFactory.getInstance().getBean("itemService")).findItem(String.valueOf(orderMap.get("TICKETID")), tempSort);
					if (!String.valueOf(orderMap.get("ORDERSTATUS")).equals(itemMap.get("OPERLSTATUS"))){
						return null;
					}else{
						if ("发令".equals(operOrderStatus) && (null != itemMap.get("SENDLUSER") && StringUtils.isNotEmpty(String.valueOf(itemMap.get("SENDLUSER"))))){
							return null;
						}
					}
				}
				//3、更新操作项状态
				for (String tempSort : itemSortArray){
					Map<String, Object> itemMap = new HashMap<String, Object>();
					itemMap.put("TICKETID", orderMap.get("TICKETID"));
					itemMap.put("SORT", tempSort);
					if ("发令".equals(operOrderStatus)){
						itemMap.put("SENDLUSER", orderMap.get("SENDLUSER"));
						itemMap.put("SENDLUSERID", orderMap.get("SENDLUSERID"));
						itemMap.put("SENDLTIME", sendLTime);
						itemMap.put("JHRLUSERID", orderMap.get("JHRLUSERID"));
						itemMap.put("JHRLUSER", orderMap.get("JHRLUSER"));
						itemMap.put("OPERLSTATUS", "正在执行");
						orderMap.put("ORDERSTATUS","正在执行");
						orderMap.put("SENDLTIME",sendLTime);
					}else if ("作废".equals(operOrderStatus)){
						itemMap.put("REMARK", orderMap.get("ORDERCONTENT"));
						itemMap.put("OPERLSTATUS", operOrderStatus);
						itemMap.put("JHRLUSERID", orderMap.get("JHRLUSERID"));
						itemMap.put("JHRLUSER", orderMap.get("JHRLUSER"));
						orderMap.put("ORDERSTATUS",operOrderStatus);
						orderMap.put("SENDLTIME",sendLTime);
						orderMap.put("REMARK",orderMap.get("ORDERCONTENT"));
					}else if ("跳过".equals(operOrderStatus)){
						itemMap.put("REMARK", orderMap.get("ORDERCONTENT"));
						itemMap.put("OPERLSTATUS", operOrderStatus);
						itemMap.put("JHRLUSERID", orderMap.get("JHRLUSERID"));
						itemMap.put("JHRLUSER", orderMap.get("JHRLUSER"));
						orderMap.put("ORDERSTATUS", operOrderStatus);
						orderMap.put("SENDLTIME",sendLTime);
						orderMap.put("REMARK",orderMap.get("ORDERCONTENT"));
					}
					((ItemService)SpringBeanFactory.getInstance().getBean("itemService")).updateItemByTicketId2Sort(itemMap);
				}
				//4、保存操作令
				rsultMap = createOrder(orderMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rsultMap;
	}
	
	/**
	 * 执行令操作（受令、回令、收令、作废、跳过）
	 * @param ticketStatus		操作票状态
	 * @param orderMap			指令信息
	 * @param operOrderStatus	将操作的状态
	 * @return					返回发令成功后指令信息
	 * @throws Exception
	 */
	public boolean checkTicketInfoStatus2UpdateOrder(String ticketStatus, Map<String, Object> orderMap, String operOrderStatus) throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		String callTime = DateUtil.getCurrentDate("HH:mm");
		try {
			if (StringUtils.isNotEmpty(ticketStatus) && StringUtils.isNotEmpty(operOrderStatus) && null != orderMap){
				//1、判断票状态是否对应
				Map<String, Object> ticketMap = ((TicketService)SpringBeanFactory.getInstance().getBean("ticketService")).findTicket(String.valueOf(orderMap.get("TICKETID")));
				if (!ticketStatus.equals(ticketMap.get("TICKETSTATUS"))){
					return false;
				}
				//2、判断操作项目状态是否对应
				String[] itemSortArray = String.valueOf(orderMap.get("ITEMSORT")).split("，");
				for (String tempSort : itemSortArray){
					Map<String, Object> itemMap = ((ItemService)SpringBeanFactory.getInstance().getBean("itemService")).findItem(String.valueOf(orderMap.get("TICKETID")), tempSort);
					if (!String.valueOf(orderMap.get("ORDERSTATUS")).equals(itemMap.get("OPERLSTATUS"))){
						return false;
					}else{
						if ("受令".equals(operOrderStatus) && (null != itemMap.get("RECVLUSER") && StringUtils.isNotEmpty(String.valueOf(itemMap.get("RECVLUSER"))))){
							return false;
						}
						if ("回令".equals(operOrderStatus) && (null != itemMap.get("CALLLUSER") && StringUtils.isNotEmpty(String.valueOf(itemMap.get("CALLLUSER"))))){
							return false;
						}
						if ("收令".equals(operOrderStatus) && (null != itemMap.get("GETLUSER") && StringUtils.isNotEmpty(String.valueOf(itemMap.get("GETLUSER"))))){
							return false;
						}
					}
				}
				//3、更新操作项状态
				for (String tempSort : itemSortArray){
					Map<String, Object> itemMap = new HashMap<String, Object>();
					itemMap.put("TICKETID", orderMap.get("TICKETID"));
					itemMap.put("SORT", tempSort);
					if ("受令".equals(operOrderStatus)){
						itemMap.put("RECVLUSER", orderMap.get("RECVLUSER"));
						itemMap.put("RECVLUSERID", orderMap.get("RECVLUSERID"));
					}else if ("回令".equals(operOrderStatus)){
						itemMap.put("OVERLTIME", orderMap.get("OVERLTIME"));
						itemMap.put("CALLLUSER", orderMap.get("CALLLUSER"));
						itemMap.put("CALLLUSERID", orderMap.get("CALLLUSERID"));
						itemMap.put("OPERLSTATUS", "已经执行");
						orderMap.put("ORDERSTATUS", "已经执行");
					}else if ("收令".equals(operOrderStatus)){
						itemMap.put("GETLUSER", orderMap.get("GETLUSER"));
						itemMap.put("GETLUSERID", orderMap.get("GETLUSERID"));
						itemMap.put("CALLLTIME", callTime);
						orderMap.put("CALLLTIME", callTime);
					}else if ("作废".equals(operOrderStatus)){
						itemMap.put("REMARK", orderMap.get("ORDERCONTENT"));
						itemMap.put("OPERLSTATUS", operOrderStatus);
						orderMap.put("ORDERSTATUS",operOrderStatus);
						orderMap.put("REMARK",orderMap.get("ORDERCONTENT"));
					}else if ("跳过".equals(operOrderStatus)){
						itemMap.put("REMARK", orderMap.get("ORDERCONTENT"));
						itemMap.put("OPERLSTATUS", operOrderStatus);
						orderMap.put("ORDERSTATUS",operOrderStatus);
						orderMap.put("REMARK",orderMap.get("ORDERCONTENT"));
					}
					((ItemService)SpringBeanFactory.getInstance().getBean("itemService")).updateItemByTicketId2Sort(itemMap);
				}
				//4、保存操作令
				updateOrder(orderMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * 获取操作项对应指令Tab页ID
	 * @param ticketId	操作票ID
	 * @param sort		操作项序号
	 * @return	获取操作项对应指令Tab页ID
	 * @throws Exception
	 */
	public Map<String, Object> selectOrderTabIdPageByTicket2Sort(String ticketId, String sort)throws Exception{
		try {
			if (StringUtils.isNotEmpty(ticketId) && StringUtils.isNotEmpty(sort)){
				List<Map<String, Object>> orderMapList = orderDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_ORDER, "DATASTATUSID='1' AND TICKETID='" + ticketId + "' AND ('，' || ITEMSORT || '，' LIKE '%，" + sort + "，%')");
				if (null != orderMapList && 0 < orderMapList.size()){
					return orderMapList.get(0);
				}else{
					return null;
				}
			}else{
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
