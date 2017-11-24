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
	 * ��ȡ��Ӧ����Ʊ������ָ����Ϣ
	 * @param ticketId		����Ʊ���
	 * @return				����һ�Ų���Ʊָ�
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
	 * ����������
	 * @param  orderMap		ָ����Ϣ
	 * @return				�����½���������Ϣ
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
				//����Ψһ����ID
				id = SerialService.getSerialIdByDate("");
				//�������
				List<Map<String,Object>> orderMapList = findOrderList(String.valueOf(orderMap.get("TICKETID")));
				if (null == orderMapList || 0 == orderMapList.size()){
					sort = 1;
				} else {
					sort = Integer.parseInt(String.valueOf(orderMapList.get(orderMapList.size()-1).get("SORT"))) + 1;
				}
				name = "��" + String.valueOf(orderMap.get("ITEMSORT")) + "����";
				//��������
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
	 * ���²�����
	 * @param  orderMap		ǰ�˲�������Ϣ
	 * @return				���²�������Ϣ
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
	 * ɾ��������
	 * @param ids			������ID��
	 * @return				���ز���������
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
	 * ��Ʊ����ˡ��·���ִ�С���飨������ǰ��״̬���������ִ�н׶���Ϣ��������ά��
	 * @param ticketId		����ƱID
	 * @param srcStatus		Դ״̬
	 * @param destStatus	Ŀ��״̬
	 * @param userMap		��ǰ�û���Ϣ
	 * @return	��Ʊ����ˡ��·���ִ�С���飨������ǰ��״̬���������ִ�н׶���Ϣ��������ά��
	 * @throws Exception
	 */
	public boolean ticketStatusBH2ClearTicketInfo(String ticketId, String srcStatus, String destStatus, Map<String, Object> userMap)throws Exception{
		try {
			if ("��Ʊ".equals(destStatus) && -1 != "���,�·�,ִ��,���,�ϸ�,ȱ��,����".indexOf(srcStatus)){
				return orderDao.delMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_ORDER, "TICKETID='" + ticketId + "'");
			}else if ("���".equals(destStatus) && -1 != "�·�,ִ��,���,�ϸ�,ȱ��,����".indexOf(srcStatus)){
				return orderDao.delMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_ORDER, "TICKETID='" + ticketId + "'");
			}else if ("�·�".equals(destStatus) && -1 != "ִ��,���,�ϸ�,ȱ��,����".indexOf(srcStatus)){
				return orderDao.delMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_ORDER, "TICKETID='" + ticketId + "'");
			}else if ("ִ��".equals(destStatus) && -1 != "���,�ϸ�,ȱ��,����".indexOf(srcStatus)){
				return orderDao.delMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_ORDER, "TICKETID='" + ticketId + "'");
			}else if ("���".equals(destStatus) && -1 != "�ϸ�,ȱ��,����".indexOf(srcStatus)){
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
	 * ���������������ϡ�������
	 * @param ticketStatus		����Ʊ״̬
	 * @param orderMap			ָ����Ϣ
	 * @param operOrderStatus	��������״̬��������ϡ�������
	 * @return					���ط���ɹ���ָ����Ϣ
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
				//1���ж�Ʊ״̬�Ƿ��Ӧ
				Map<String, Object> ticketMap = ((TicketService)SpringBeanFactory.getInstance().getBean("ticketService")).findTicket(String.valueOf(orderMap.get("TICKETID")));
				if (!ticketStatus.equals(ticketMap.get("TICKETSTATUS"))){
					return null;
				}
				//2���жϲ�����Ŀ״̬�Ƿ��Ӧ
				String[] itemSortArray = String.valueOf(orderMap.get("ITEMSORT")).split("��");
				for (String tempSort : itemSortArray){
					Map<String, Object> itemMap = ((ItemService)SpringBeanFactory.getInstance().getBean("itemService")).findItem(String.valueOf(orderMap.get("TICKETID")), tempSort);
					if (!String.valueOf(orderMap.get("ORDERSTATUS")).equals(itemMap.get("OPERLSTATUS"))){
						return null;
					}else{
						if ("����".equals(operOrderStatus) && (null != itemMap.get("SENDLUSER") && StringUtils.isNotEmpty(String.valueOf(itemMap.get("SENDLUSER"))))){
							return null;
						}
					}
				}
				//3�����²�����״̬
				for (String tempSort : itemSortArray){
					Map<String, Object> itemMap = new HashMap<String, Object>();
					itemMap.put("TICKETID", orderMap.get("TICKETID"));
					itemMap.put("SORT", tempSort);
					if ("����".equals(operOrderStatus)){
						itemMap.put("SENDLUSER", orderMap.get("SENDLUSER"));
						itemMap.put("SENDLUSERID", orderMap.get("SENDLUSERID"));
						itemMap.put("SENDLTIME", sendLTime);
						itemMap.put("JHRLUSERID", orderMap.get("JHRLUSERID"));
						itemMap.put("JHRLUSER", orderMap.get("JHRLUSER"));
						itemMap.put("OPERLSTATUS", "����ִ��");
						orderMap.put("ORDERSTATUS","����ִ��");
						orderMap.put("SENDLTIME",sendLTime);
					}else if ("����".equals(operOrderStatus)){
						itemMap.put("REMARK", orderMap.get("ORDERCONTENT"));
						itemMap.put("OPERLSTATUS", operOrderStatus);
						itemMap.put("JHRLUSERID", orderMap.get("JHRLUSERID"));
						itemMap.put("JHRLUSER", orderMap.get("JHRLUSER"));
						orderMap.put("ORDERSTATUS",operOrderStatus);
						orderMap.put("SENDLTIME",sendLTime);
						orderMap.put("REMARK",orderMap.get("ORDERCONTENT"));
					}else if ("����".equals(operOrderStatus)){
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
				//4�����������
				rsultMap = createOrder(orderMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rsultMap;
	}
	
	/**
	 * ִ���������������������ϡ�������
	 * @param ticketStatus		����Ʊ״̬
	 * @param orderMap			ָ����Ϣ
	 * @param operOrderStatus	��������״̬
	 * @return					���ط���ɹ���ָ����Ϣ
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
				//1���ж�Ʊ״̬�Ƿ��Ӧ
				Map<String, Object> ticketMap = ((TicketService)SpringBeanFactory.getInstance().getBean("ticketService")).findTicket(String.valueOf(orderMap.get("TICKETID")));
				if (!ticketStatus.equals(ticketMap.get("TICKETSTATUS"))){
					return false;
				}
				//2���жϲ�����Ŀ״̬�Ƿ��Ӧ
				String[] itemSortArray = String.valueOf(orderMap.get("ITEMSORT")).split("��");
				for (String tempSort : itemSortArray){
					Map<String, Object> itemMap = ((ItemService)SpringBeanFactory.getInstance().getBean("itemService")).findItem(String.valueOf(orderMap.get("TICKETID")), tempSort);
					if (!String.valueOf(orderMap.get("ORDERSTATUS")).equals(itemMap.get("OPERLSTATUS"))){
						return false;
					}else{
						if ("����".equals(operOrderStatus) && (null != itemMap.get("RECVLUSER") && StringUtils.isNotEmpty(String.valueOf(itemMap.get("RECVLUSER"))))){
							return false;
						}
						if ("����".equals(operOrderStatus) && (null != itemMap.get("CALLLUSER") && StringUtils.isNotEmpty(String.valueOf(itemMap.get("CALLLUSER"))))){
							return false;
						}
						if ("����".equals(operOrderStatus) && (null != itemMap.get("GETLUSER") && StringUtils.isNotEmpty(String.valueOf(itemMap.get("GETLUSER"))))){
							return false;
						}
					}
				}
				//3�����²�����״̬
				for (String tempSort : itemSortArray){
					Map<String, Object> itemMap = new HashMap<String, Object>();
					itemMap.put("TICKETID", orderMap.get("TICKETID"));
					itemMap.put("SORT", tempSort);
					if ("����".equals(operOrderStatus)){
						itemMap.put("RECVLUSER", orderMap.get("RECVLUSER"));
						itemMap.put("RECVLUSERID", orderMap.get("RECVLUSERID"));
					}else if ("����".equals(operOrderStatus)){
						itemMap.put("OVERLTIME", orderMap.get("OVERLTIME"));
						itemMap.put("CALLLUSER", orderMap.get("CALLLUSER"));
						itemMap.put("CALLLUSERID", orderMap.get("CALLLUSERID"));
						itemMap.put("OPERLSTATUS", "�Ѿ�ִ��");
						orderMap.put("ORDERSTATUS", "�Ѿ�ִ��");
					}else if ("����".equals(operOrderStatus)){
						itemMap.put("GETLUSER", orderMap.get("GETLUSER"));
						itemMap.put("GETLUSERID", orderMap.get("GETLUSERID"));
						itemMap.put("CALLLTIME", callTime);
						orderMap.put("CALLLTIME", callTime);
					}else if ("����".equals(operOrderStatus)){
						itemMap.put("REMARK", orderMap.get("ORDERCONTENT"));
						itemMap.put("OPERLSTATUS", operOrderStatus);
						orderMap.put("ORDERSTATUS",operOrderStatus);
						orderMap.put("REMARK",orderMap.get("ORDERCONTENT"));
					}else if ("����".equals(operOrderStatus)){
						itemMap.put("REMARK", orderMap.get("ORDERCONTENT"));
						itemMap.put("OPERLSTATUS", operOrderStatus);
						orderMap.put("ORDERSTATUS",operOrderStatus);
						orderMap.put("REMARK",orderMap.get("ORDERCONTENT"));
					}
					((ItemService)SpringBeanFactory.getInstance().getBean("itemService")).updateItemByTicketId2Sort(itemMap);
				}
				//4�����������
				updateOrder(orderMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * ��ȡ�������Ӧָ��TabҳID
	 * @param ticketId	����ƱID
	 * @param sort		���������
	 * @return	��ȡ�������Ӧָ��TabҳID
	 * @throws Exception
	 */
	public Map<String, Object> selectOrderTabIdPageByTicket2Sort(String ticketId, String sort)throws Exception{
		try {
			if (StringUtils.isNotEmpty(ticketId) && StringUtils.isNotEmpty(sort)){
				List<Map<String, Object>> orderMapList = orderDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_ORDER, "DATASTATUSID='1' AND TICKETID='" + ticketId + "' AND ('��' || ITEMSORT || '��' LIKE '%��" + sort + "��%')");
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
