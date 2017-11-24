package kd.idp.psidpapp.opt.ticket.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import kd.idp.psidpapp.opt.sys.flex.FlexUtil;
import kd.idp.psidpapp.opt.sys.menu.service.MenuService;
import kd.idp.psidpapp.opt.sys.serial.bean.PageNumberPattern;
import kd.idp.psidpapp.opt.sys.serial.service.SerialService;
import kd.idp.psidpapp.opt.sys.user.service.UserService;
import kd.idp.psidpapp.opt.sys.util.DateUtil;
import kd.idp.psidpapp.opt.ticket.constant.DataSourceName;
import kd.idp.psidpapp.opt.ticket.constant.FieldName;
import kd.idp.psidpapp.opt.ticket.constant.TableName;
import kd.idp.psidpapp.opt.ticket.dao.ItemDao;
import kd.idp.psidpapp.opt.ticket.dao.OrderDao;
import kd.idp.psidpapp.opt.ticket.dao.RecvTicketDao;
import kd.idp.psidpapp.opt.ticket.dao.TicketDao;

import com.sdjxd.pms.platform.tool.StringTool;
import com.spring.SpringBeanFactory;


public class TicketService {
	
	private static String HZDDK = "111";
	private static String TICKET_NP = "��Ʊ";
	private static String TICKET_SH = "���";
	private static String TICKET_XF = "�·�";
	private static String TICKET_ZX = "ִ��";
	private static String TICKET_JC = "���";
	private static String TICKET_GD = "�鵵";
	private static String TICKET_GD2 = "'�ϸ�','ȱ��'";
	private static String TICKET_SEARCH = "��ѯ";
	private static String TICKET_SEARCH2 = "'��Ʊ','���','�·�','ִ��','���','�ϸ�','ȱ��','����'";
	
	private static String[] TICKET_TYPE = new String[]{"����Ʊ","����Ʊ","�ۺ�Ʊ"};
	private static String[] TICKET_TYPE_NEW = new String[]{"�½�����Ʊ","�½�����Ʊ","�½��ۺ�Ʊ"};
	
	private TicketDao ticketDao;
	private ItemDao itemDao;
	private OrderDao orderDao;
	private RecvTicketDao recvTicketDao;
	
	public TicketDao getTicketDao() {
		return ticketDao;
	}

	public void setTicketDao(TicketDao ticketDao) {
		this.ticketDao = ticketDao;
	}

	public ItemDao getItemDao() {
		return itemDao;
	}

	public void setItemDao(ItemDao itemDao) {
		this.itemDao = itemDao;
	}

	public OrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}
	
	public RecvTicketDao getRecvTicketDao() {
		return recvTicketDao;
	}

	public void setRecvTicketDao(RecvTicketDao recvTicketDao) {
		this.recvTicketDao = recvTicketDao;
	}

	/**
	 * @param parentId		����Ʊϵͳ�˵�ID
	 * @return				��ʼ������Ʊ����˵�
	 * @throws Exception
	 */
	public List<Map<String, Object>> initTopMenu(String parentId) throws Exception{
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
		List<Map<String, Object>> topMenuList = new ArrayList<Map<String,Object>>();
		try {
			MenuService meuService = (MenuService)SpringBeanFactory.getInstance().getBean("menuService");
			List<Map<String, Object>> tempTopMenuList = meuService.findMenuByParentIdByFlex(parentId);
			for (Map<String, Object> menu : tempTopMenuList){
				Map<String, Object> topMenuMap = new HashMap<String, Object>();
				String menuId = menu.get("ID").toString();
				String menuName = menu.get("NAME").toString();
				String menuNickName = menu.get("NICKNAME").toString();
				String icon = menu.get("ICON").toString();
				String url = menu.get("URL").toString();
				String type = menu.get("CLASS").toString();
				topMenuMap.put("ID", menuId);
				topMenuMap.put("NAME", menuName);
				topMenuMap.put("NICKNAME", menuNickName);
				topMenuMap.put("ICON", icon);
				topMenuMap.put("URL", url);
				topMenuMap.put("TYPE", type);
				long ticketNumber = 0;
				//1�������Զ������ӣ�class=2��
				if (TICKET_NP.equals(menuName)){
					List<Map<String, Object>> childMenuList = new ArrayList<Map<String,Object>>();
					for (int i=0; i<TICKET_TYPE.length; i++){
						Map<String, Object> childMenuMap = new HashMap<String, Object>();
						String ticketNPType = TICKET_TYPE[i];
						String ticketNPTypeNew = TICKET_TYPE_NEW[i];
						List<Map<String, Object>> ticketNPTypeList = ticketDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_TICKET, "DATASTATUSID='1' AND TICKETSTATUS='" + TICKET_NP + "' AND TICKETTYPE='" + ticketNPType + "'" + appendSqlCondition + " ORDER BY TICKETNUMBER DESC");
						ticketList2MenuList(ticketNPTypeList,icon);
						if (null != ticketNPTypeList && 0 < ticketNPTypeList.size()){
							ticketNumber += ticketNPTypeList.size();
							childMenuMap.put("ID", ticketNPType);
							childMenuMap.put("NAME", ticketNPType);
							childMenuMap.put("NICKNAME", ticketNPType + "(" + ticketNPTypeList.size() + ")");
							childMenuMap.put("ICON", icon);
							childMenuMap.put("TICKETTYPE", ticketNPType);
							childMenuMap.put("TICKETSTATUS", menuName);
							childMenuMap.put("CHILDS", ticketNPTypeList);
							//�����½���Ʊ����
							Map<String, Object> newMenuMap = new HashMap<String, Object>();
							newMenuMap.put("ID", ticketNPTypeNew);
							newMenuMap.put("NAME", ticketNPTypeNew);
							newMenuMap.put("NICKNAME", ticketNPTypeNew);
							newMenuMap.put("ICON", icon);
							newMenuMap.put("TICKETTYPE", ticketNPType);
							newMenuMap.put("TICKETSTATUS", menuName);
							ticketNPTypeList.add(0, newMenuMap);
						} else {
							childMenuMap.put("ID", ticketNPTypeNew);
							childMenuMap.put("NAME", ticketNPTypeNew);
							childMenuMap.put("NICKNAME", ticketNPTypeNew);
							childMenuMap.put("ICON", icon);
							childMenuMap.put("TICKETTYPE", ticketNPType);
							childMenuMap.put("TICKETSTATUS", menuName);
						}
						childMenuList.add(childMenuMap);
					}
					topMenuMap.put("NICKNAME", menuNickName + "(" + ticketNumber + ")");
					topMenuMap.put("CHILDS", childMenuList);
				}else if (TICKET_SH.equals(menuName) || TICKET_XF.equals(menuName) || TICKET_ZX.equals(menuName) || TICKET_JC.equals(menuName)){
					List<Map<String, Object>> ticketStatusList = ticketDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_TICKET, "DATASTATUSID='1' AND TICKETSTATUS='" + menuName + "'" + appendSqlCondition + " ORDER BY TICKETNUMBER DESC");
					ticketList2MenuList(ticketStatusList,icon);
					ticketNumber += ticketStatusList.size();
					topMenuMap.put("NICKNAME", menuNickName + "(" + ticketNumber + ")");
					topMenuMap.put("CHILDS", ticketStatusList);
				}else if (TICKET_GD.equals(menuName)){
					List<Map<String, Object>> ticketStatusList = ticketDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_TICKET, "DATASTATUSID='1' AND TICKETSTATUS IN(" + TICKET_GD2 + ")" + appendSqlCondition + " ORDER BY TICKETNUMBER DESC");
					ticketList2MenuList(ticketStatusList,icon);
					ticketNumber += ticketStatusList.size();
					topMenuMap.put("NICKNAME", menuNickName + "(" + ticketNumber + ")");
				}else if (TICKET_SEARCH.equals(menuName)){
					List<Map<String, Object>> ticketStatusList = ticketDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_TICKET, "DATASTATUSID='1' AND TICKETSTATUS IN(" + TICKET_SEARCH2 + ")" + appendSqlCondition + " ORDER BY TICKETNUMBER DESC");
					ticketList2MenuList(ticketStatusList,icon);
					ticketNumber += ticketStatusList.size();
					topMenuMap.put("NICKNAME", menuNickName + "(" + ticketNumber + ")");
				}
				//2�������������ӣ�class=1��
				else{
					
				}
				topMenuList.add(topMenuMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return topMenuList;
	}
	
	//������Ʊת��Ϊ�˵���Ŀ
	public void ticketList2MenuList(List<Map<String, Object>> ticketList, String icon)throws Exception{
		for (Map<String, Object> dataMap : ticketList){
			dataMap.put("ID", dataMap.get("ID"));
			dataMap.put("NAME",  dataMap.get("TICKETNUMBER"));
			dataMap.put("NICKNAME",  dataMap.get("OPERTASK"));
			dataMap.put("ICON",  icon);
		}
	}
	
	/**
	 * @param pagePatternId	����Ʊ���ģ��ID(1:����Ʊ,2:����Ʊ,3:�ۺ�Ʊ)
	 * @return				���ز���Ʊ���
	 * @throws Exception
	 */
//	public String createTicketNumber(String pagePatternId)throws Exception{
//		String ticketNumber = "";
//		try {
//			if (null != pagePatternId && !"".equals(pagePatternId)){
//				SerialService serialService = (SerialService)SpringBeanFactory.getInstance().getBean("serialService");
//				ticketNumber = serialService.createPageNumber(pagePatternId);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		}
//		return ticketNumber;
//	}
	
	/**
	 * ��������Ʊ��ţ����ݲ�ͬ�������ͣ�
	 * @param ticketType		����Ʊ����
	 * @return					���ش�������Ʊ���[TICKETNUMBER]�����ݲ�ͬ�������ͣ�
	 * @throws Exception
	 */
	public Map<String, Object> createPageNumber(String ticketType)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		Map<String, Object> pageNumberMap = new HashMap<String, Object>();
		try {
			if (StringUtils.isNotEmpty(ticketType)){
				String pagePatternId = "";
				for (int i=0; i<TICKET_TYPE.length; i++){
					if (TICKET_TYPE[i].equals(ticketType)){
						pagePatternId = String.valueOf(i+1);
					}
				}
				String pageNumber = createPageNumberIsNotUse(ticketType, pagePatternId);
				pageNumberMap.put("TICKETNUMBER", pageNumber);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return pageNumberMap;
	}
	
	/**
	 * @param ticketType	����Ʊ����(����Ʊ������Ʊ���ۺ�Ʊ)
	 * @return				�����½�����Ʊ��Ϣ
	 * @throws Exception
	 */
	public Map<String, Object> createTicket(String ticketType)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		Map<String, Object> ticketMap = new HashMap<String, Object>();
		String ticketId = "";
		String ticketNumber = "";
		String ticketStatus = TICKET_NP;
		int dataStatusId = 1;
		String createUserId = String.valueOf(userMap.get("ID"));
		String createTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
		try {
			if (null != ticketType && !"".equals(ticketType)){
				//����Ψһ����ID
				ticketId = SerialService.getSerialIdByDate("");
				//����Ʊ���
				String pagePatternId = "";
				for (int i=0; i<TICKET_TYPE.length; i++){
					if (TICKET_TYPE[i].equals(ticketType)){
						pagePatternId = String.valueOf(i+1);
					}
				}
				//a���ж��Ƿ������õ�Ʊ��δʹ��
				List<Map<String,Object>> updateTicketNumberList = getUpdateTicketNumberList(ticketType);
				if (null != updateTicketNumberList && 0 != updateTicketNumberList.size()){
					ticketNumber = String.valueOf(updateTicketNumberList.get(updateTicketNumberList.size()-1).get("TICKETNUMBER"));
				}else{
					ticketNumber = createPageNumberIsNotUse(ticketType, pagePatternId);
				}
				//��������
				ticketMap.put("ID", ticketId);
				ticketMap.put("TICKETNUMBER", ticketNumber);
				ticketMap.put("TICKETSTATUS", ticketStatus);
				ticketMap.put("TICKETTYPE", ticketType);
				ticketMap.put("DATASTATUSID", dataStatusId);
				ticketMap.put("CREATEUSERID", createUserId);
				ticketMap.put("CREATETIME", createTime);
				ticketDao.addMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_TICKET, FieldName.FIELD_OPT_OPT_TICKET, ticketMap);
				//�����жϣ�������ۺ�Ʊ����ͬʱҪ���������
				if ("�ۺ�Ʊ".equals(ticketType)){
					ItemService itemService = (ItemService)SpringBeanFactory.getInstance().getBean("itemService");		
					itemService.createItem(ticketId);
				}
				
//				OrderService orderService = (OrderService)SpringBeanFactory.getInstance().getBean("orderService");		
//				RecvTicketService recvTicketService = (RecvTicketService)SpringBeanFactory.getInstance().getBean("recvTicketService");
//				orderService.createOrder(userMap));
//				recvTicketService.createRecvTicketList(ticketId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return ticketMap;
	}
	
	/**
	 * ��ѯ����Ʊ��Ϣ
	 * @param ticketId		����Ʊ���
	 * @return				����һ�Ų���Ʊ��Ϣ
	 * @throws Exception
	 */
	public Map<String, Object> findTicket(String ticketId) throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		Map<String, Object> ticketMap = null;
		try {
			if (StringUtils.isNotEmpty(ticketId)){
				List<Map<String,Object>> ticketMapList = ticketDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_TICKET, "DATASTATUSID='1' AND ID='" + ticketId + "'");
				if (null != ticketMapList && 0 != ticketMapList.size()){
					ticketMap = ticketMapList.get(0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return ticketMap;
	}
	
	/**
	 * ���²���Ʊ
	 * @param  ticketMap		ǰ�˲���Ʊ��Ϣ
	 * @return					���²���Ʊ��Ϣ
	 * @throws Exception
	 */
	public Boolean updateTicket(Map<String,Object> ticketMap)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		Boolean result = false;
		String updateUserId = String.valueOf(userMap.get("ID"));
		String updateTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
		try {
			if (null != ticketMap && null != ticketMap.get("ID") && StringUtils.isNotEmpty(String.valueOf(ticketMap.get("ID")))){
				String id = String.valueOf(ticketMap.get("ID"));
				ticketMap.put("UPDATEUSERID", updateUserId);
				ticketMap.put("UPDATETIME", updateTime);
				result = ticketDao.updateMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_TICKET, FieldName.FIELD_OPT_OPT_TICKET, "ID='" + id + "'", ticketMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	
	
	/*********************************************ȫ�ֿ��ƽӿ� ��ʼ***********************************************/
	/**
	 * ��ȡ����Ʊ������Ϣ
	 * @param ticketId		����ƱID
	 * @return				��ȡ����Ʊ������Ϣ
	 * @throws Exception
	 */
	public Map<String,Object> findTicketInfoMap(String ticketId) throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		Map<String, Object> ticketInfoMap = new HashMap<String, Object>();
		try {
			if (StringUtils.isNotEmpty(ticketId)){
				ticketInfoMap.put("ticketMap", findTicket(ticketId));
				ticketInfoMap.put("itemMapList", ((ItemService)SpringBeanFactory.getInstance().getBean("itemService")).findItemList(ticketId));
				ticketInfoMap.put("orderMapList", ((OrderService)SpringBeanFactory.getInstance().getBean("orderService")).findOrderList(ticketId));
				ticketInfoMap.put("recvTicketMapList", ((RecvTicketService)SpringBeanFactory.getInstance().getBean("recvTicketService")).findRecvTicketList(ticketId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ticketInfoMap;
	}
	
	/**
	 * �������в���Ʊ��Ϣ
	 * @param ticketInfoMap	����Ʊ������Ϣ
	 * @return				���ز��������Ϣ
	 * @throws Exception
	 */
	public Boolean saveTicketInfo(Map<String,Object> ticketInfoMap)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		try {
			if (null != ticketInfoMap){
				if (null != ticketInfoMap.get("ticketMap")){
					Map<String, Object> ticketMap = (Map<String, Object>)ticketInfoMap.get("ticketMap");
					updateTicket(ticketMap);
				}
				if (null != ticketInfoMap.get("itemMapList")){
					List<Map<String, Object>> itemMapList = (List<Map<String, Object>>)ticketInfoMap.get("itemMapList");
					for (Map<String, Object> itemMap : itemMapList){
						((ItemService)SpringBeanFactory.getInstance().getBean("itemService")).updateItem(itemMap);
					}
				}
				if (null != ticketInfoMap.get("orderMapList")){
					List<Map<String, Object>> orderMapList = (List<Map<String, Object>>)ticketInfoMap.get("orderMapList");
					for (Map<String, Object> orderMap : orderMapList){
						((OrderService)SpringBeanFactory.getInstance().getBean("orderService")).updateOrder(orderMap);
					}
				}
				if (null != ticketInfoMap.get("recvTicketMapList")){
					List<Map<String, Object>> recvTicketMapList = (List<Map<String, Object>>)ticketInfoMap.get("recvTicketMapList");
					for (Map<String, Object> recvTicketMap : recvTicketMapList){
						((RecvTicketService)SpringBeanFactory.getInstance().getBean("recvTicketService")).updateRecvTicket(recvTicketMap);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * ɾ������Ʊ������Ϣ
	 * @param ids			����ƱID��
	 * @return				���ز���������
	 * @throws Exception
	 */
	public Boolean delTicketInfoList(String ids) throws Exception{
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
					//��ɾ������������
					result = itemDao.delMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_ITEM, sqlConditions.replace("ID IN(", "TICKETID IN("));
					//��ɾ������������
					if (result){
						result = orderDao.delMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_ORDER, sqlConditions.replace("ID IN(", "TICKETID IN("));
					}
					//Ȼ��ɾ����Ʊ��Ϣ�������
					if (result){
						result = recvTicketDao.delMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_RECVTICEKT, sqlConditions.replace("ID IN(", "TICKETID IN("));
					}
					//���ɾ������Ʊ 
					if (result){
						result = ticketDao.delMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_TICKET, sqlConditions);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	/**
	 * ��ȡ���޸�Ʊ��
	 * @param ticketType	����Ʊ���ģ��ID(1:����Ʊ,2:����Ʊ,3:�ۺ�Ʊ)
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getUpdateTicketNumberList(String ticketType) throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		List<Map<String,Object>> updateTicketNumberList = new ArrayList<Map<String,Object>>();
		String year = String.valueOf(DateUtil.getCurrYear());
		String pagePatternId = "";
		try {
			if ("����Ʊ".equals(ticketType)){
				pagePatternId  = "1";
			}else if ("����Ʊ".equals(ticketType)){
				pagePatternId  = "2";
			}else if ("�ۺ�Ʊ".equals(ticketType)){
				pagePatternId  = "3";
			}
			if (StringUtils.isNotEmpty(pagePatternId)){
				SerialService serialService = (SerialService)SpringBeanFactory.getInstance().getBean("serialService");
				PageNumberPattern pageNumberPattern = serialService.getPageNumberPattern(pagePatternId);
				if (null != pageNumberPattern){
					//��ȡʵ���������ֵ
					String serialNumStr = serialService.getSerialNum(pagePatternId);
					if (null == serialNumStr){//���ģ����ʵ�����������ٲ���
						return null;
					}
					long serialNum = Long.parseLong(serialNumStr);
					//��ȡ����Ʊ��
					List<Long> ticketNumberList = new ArrayList<Long>();
					List<Map<String,Object>> ticketMapList = ticketDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_TICKET, "DATASTATUSID='1' AND TICKETTYPE='" + ticketType + "' AND TICKETNUMBER LIKE '" + year + "%'");
					for (Map<String,Object> ticketMap : ticketMapList){
						String ticketNumberStr = String.valueOf(ticketMap.get("TICKETNUMBER"));
						ticketNumberStr =  ticketNumberStr.substring(ticketNumberStr.length()-pageNumberPattern.getSerialLength());
						long ticketNumber = Long.parseLong(ticketNumberStr);
						ticketNumberList.add(ticketNumber);
					}
					for (long i=serialNum; i>0; i--){
						boolean flag = false;
						for (long temptiketNumber : ticketNumberList){
							if (temptiketNumber == i){
								flag = true;
								break;
							}
						}
						if (!flag){
							Map<String,Object> ticketNumberMap = new HashMap<String,Object>();
							ticketNumberMap.put("TICKETNUMBER", serialService.createPageNumberBySerialValue(pagePatternId,StringTool.leftPad(String.valueOf(i), pageNumberPattern.getSerialLength(),'0')));
							updateTicketNumberList.add(ticketNumberMap);
						}
					}	
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return updateTicketNumberList;
	}
	
	/**
	 * �������Ʊ������ʼʱ��
	 * @param ticketId		����ƱID
	 * @return ���ز������
	 * @throws Exception
	 */
	public Map<String, Object> saveTicketOperStartTime(String ticketId)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		String updateUserId = String.valueOf(userMap.get("ID"));
		String updateTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			//1�����²�����Ϊ����ִ��
			ItemService itemService = (ItemService) SpringBeanFactory.getInstance().getBean("itemService");
			List<Map<String,Object>> dbItemList = itemService.findItemList(ticketId);
			List<Map<String,Object>> vItemList = new ArrayList<Map<String,Object>>();
			for (Map<String,Object> tempItem : dbItemList){
				Map<String,Object> vItemMap = new HashMap<String, Object>();
				vItemMap.put("ID", tempItem.get("ID"));
				vItemMap.put("OPERLSTATUS", "����ִ��");
				vItemList.add(vItemMap);
				itemService.updateItem(vItemMap);
			}
			//2�����²���Ʊ��ʼʱ��
			Map<String, Object> dbMap = new HashMap<String, Object>();
			dbMap.put("TICKETOPERSTARTTIME", updateTime);
			dbMap.put("UPDATEUSERID", updateUserId);
			dbMap.put("UPDATETIME", updateTime);
			boolean result = ticketDao.updateMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_TICKET, FieldName.FIELD_OPT_OPT_TICKET, "ID='" + ticketId + "'", dbMap);
			if (result){
				resultMap.put("TICKETOPERSTARTTIME", updateTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * �������Ʊ��������ʱ��
	 * @param ticketId		����ƱID
	 * @return ���ز������
	 * @throws Exception
	 */
	public Map<String, Object> saveTicketOperEntTime(String ticketId)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		String updateUserId = String.valueOf(userMap.get("ID"));
		String updateTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Map<String, Object> dbMap = new HashMap<String, Object>();
			dbMap.put("TICKETSTATUS", "���");
			dbMap.put("TICKETOPERENTTIME", updateTime);
			dbMap.put("UPDATEUSERID", updateUserId);
			dbMap.put("UPDATETIME", updateTime);
			boolean result = ticketDao.updateMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_TICKET, FieldName.FIELD_OPT_OPT_TICKET, "ID='" + ticketId + "'", dbMap);
			if (result){
				resultMap.put("TICKETOPERENTTIME", updateTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	/*********************************************ȫ�ֿ��ƽӿ� ����***********************************************/
	
	/**
	 * ��Ʊ����ˡ��·���ִ�С���飨������ǰ��״̬���������ִ�н׶���Ϣ��������ά��
	 * @param ticketId	����ƱID
	 * @param srcStatus	Դ״̬
	 * @param destStatus	Ŀ��״̬
	 * @return	��Ʊ����ˡ��·���ִ�С���飨������ǰ��״̬���������ִ�н׶���Ϣ��������ά��
	 * @throws Exception
	 */
	public boolean ticketStatusBH22ClearTicketInfo(String ticketId, String srcStatus, String destStatus)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		try {
			if (StringUtils.isNotEmpty(ticketId) && StringUtils.isNotEmpty(srcStatus) && StringUtils.isNotEmpty(destStatus)){
				ItemService itemService = (ItemService)SpringBeanFactory.getInstance().getBean("itemService");
				OrderService orderService = (OrderService)SpringBeanFactory.getInstance().getBean("orderService");
				RecvTicketService recvTicketService = (RecvTicketService)SpringBeanFactory.getInstance().getBean("recvTicketService");
				ticketStatusBH2ClearTicketInfo(ticketId, srcStatus, destStatus, userMap);
				itemService.ticketStatusBH2ClearTicketInfo(ticketId, srcStatus, destStatus, userMap);
				orderService.ticketStatusBH2ClearTicketInfo(ticketId, srcStatus, destStatus, userMap);
				recvTicketService.ticketStatusBH2ClearTicketInfo(ticketId, srcStatus, destStatus, userMap);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
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
			Map<String, Object> ticketMap = new HashMap<String, Object>();
			if ("��Ʊ".equals(destStatus) && -1 != "���,�·�,ִ��,���,�ϸ�,ȱ��,����".indexOf(srcStatus)){
				ticketMap.put("OPENTICKETTIME", "");
				ticketMap.put("DRAFTERID", "");
				ticketMap.put("DRAFTER", "");
				ticketMap.put("AUDITORID", "");
				ticketMap.put("AUDITOR", "");
				ticketMap.put("RECVTICKETERID", "");
				ticketMap.put("RECVTICKETER", "");
				ticketMap.put("RECVTIME", "");
				ticketMap.put("TICKETOPERSTARTTIME", "");
				ticketMap.put("TICKETOPERENTTIME", "");
				ticketMap.put("TICKETSTATUS", "��Ʊ");
				ticketMap.put("UPDATETIME", DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
				ticketMap.put("UPDATEUSERID", userMap.get("ID"));
				return ticketDao.updateMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_TICKET, FieldName.FIELD_OPT_OPT_TICKET, "ID='" + ticketId + "'", ticketMap);
			}else if ("���".equals(destStatus) && -1 != "�·�,ִ��,���,�ϸ�,ȱ��,����".indexOf(srcStatus)){
				ticketMap.put("OPENTICKETTIME", "");
				ticketMap.put("AUDITORID", "");
				ticketMap.put("AUDITOR", "");
				ticketMap.put("RECVTICKETERID", "");
				ticketMap.put("RECVTICKETER", "");
				ticketMap.put("RECVTIME", "");
				ticketMap.put("TICKETOPERSTARTTIME", "");
				ticketMap.put("TICKETOPERENTTIME", "");
				ticketMap.put("TICKETSTATUS", "���");
				ticketMap.put("UPDATETIME", DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
				ticketMap.put("UPDATEUSERID", userMap.get("ID"));
				return ticketDao.updateMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_TICKET, FieldName.FIELD_OPT_OPT_TICKET, "ID='" + ticketId + "'", ticketMap);
			}else if ("�·�".equals(destStatus) && -1 != "ִ��,���,�ϸ�,ȱ��,����".indexOf(srcStatus)){
				ticketMap.put("RECVTICKETERID", "");
				ticketMap.put("RECVTICKETER", "");
				ticketMap.put("RECVTIME", "");
				ticketMap.put("TICKETOPERSTARTTIME", "");
				ticketMap.put("TICKETOPERENTTIME", "");
				ticketMap.put("TICKETSTATUS", "�·�");
				ticketMap.put("UPDATETIME", DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
				ticketMap.put("UPDATEUSERID", userMap.get("ID"));
				return ticketDao.updateMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_TICKET, FieldName.FIELD_OPT_OPT_TICKET, "ID='" + ticketId + "'", ticketMap);
			}else if ("ִ��".equals(destStatus) && -1 != "���,�ϸ�,ȱ��,����".indexOf(srcStatus)){
				ticketMap.put("TICKETOPERSTARTTIME", "");
				ticketMap.put("TICKETOPERENTTIME", "");
				ticketMap.put("TICKETSTATUS", "ִ��");
				ticketMap.put("UPDATETIME", DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
				ticketMap.put("UPDATEUSERID", userMap.get("ID"));
				return ticketDao.updateMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_TICKET, FieldName.FIELD_OPT_OPT_TICKET, "ID='" + ticketId + "'", ticketMap);
			}else if ("���".equals(destStatus) && -1 != "�ϸ�,ȱ��,����".indexOf(srcStatus)){
				ticketMap.put("TICKETSTATUS", "���");
				ticketMap.put("UPDATETIME", DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
				ticketMap.put("UPDATEUSERID", userMap.get("ID"));
				return ticketDao.updateMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_TICKET, FieldName.FIELD_OPT_OPT_TICKET, "ID='" + ticketId + "'", ticketMap);
			}else{
				return false;
			}	
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * ����Ʊ״̬���������ʾ��Ϣ
	 * @param subtopic	��Ϣ����
	 * @param ticketId	����ƱID
	 * @param orgIds	������Ϣ�û���֯����Id
	 * @return	����Ʊ״̬���������ʾ��Ϣ
	 * @throws Exception
	 */
	public Boolean ticketStatusAfterSendTip(String subtopic, String ticketId, String orgIds)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		String userIds = "";
		try {
			if (StringUtils.isNotEmpty(subtopic) && StringUtils.isNotEmpty(ticketId)){
				UserService userService = (UserService)SpringBeanFactory.getInstance().getBean("userService");
				if (StringUtils.isNotEmpty(orgIds)){
					orgIds += ",'" + HZDDK + "'";
				}else{
					orgIds += "'" + HZDDK + "'";
				}
				List<Map<String,Object>> userMapList = userService.findUserListByOrgIds(orgIds);
				for (Map<String,Object> userMap2 : userMapList){
					if (!String.valueOf(userMap.get("ID")).equals(String.valueOf(userMap2.get("ID")))){
						userIds += "," + String.valueOf(userMap2.get("ID")) + "," ;
					}
				}
				Map<String,Object> ticketInfoMap = findTicketInfoMap(ticketId);
				MessageService.pushMessages(subtopic, userIds, (Map<String, Object>)ticketInfoMap.get("ticketMap"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return true;
	}
	
	/**
	 * ����δʹ�õĲ���Ʊ����Ʊ��
	 * @param ticketType		����Ʊ����
	 * @param ticketType		����Ʊ����
	 * @return					���ش�������Ʊ�����δʹ��[TICKETNUMBER]�����ݲ�ͬ�������ͣ�
	 * @throws Exception
	 */
	public String createPageNumberIsNotUse(String ticketType, String pagePatternId)throws Exception{
		SerialService serialService = (SerialService)SpringBeanFactory.getInstance().getBean("serialService");
		String pageNumber = serialService.createPageNumber(pagePatternId);
		List<Map<String, Object>> ticketTypeList = ticketDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_TICKET, "DATASTATUSID='1' AND TICKETTYPE='" + ticketType + "' AND TICKETNUMBER='" + pageNumber + "'");
		if (null != ticketTypeList && 0 != ticketTypeList.size()){
			return createPageNumberIsNotUse(ticketType, pagePatternId);
		}else{
			return pageNumber;
		}
	}
	
	
	/**
	 * ��ȡ������ʱ��
	 * @return ��ȡ������ʱ��
	 * @throws Exception
	 */
	public Date getCurrentDate()throws Exception{
		Calendar calendar = Calendar.getInstance();
		try {
			return calendar.getTime();
		} catch (Exception e) {
			e.printStackTrace();
			return calendar.getTime();
		}
	}
	
	//���Խӿ�
	public static void main(String[] args) {
		try {
			TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
//			List<Map<String, Object>> updateTicketNumberList = ticketService.getUpdateTicketNumberList("�ۺ�Ʊ");
//			for (Map<String, Object> updateTicketNumberMap : updateTicketNumberList){
//				System.out.println(updateTicketNumberMap.get("TICKETNUMBER"));
//			}
//			List<Map<String, Object>> topMenuList = ticketService.initTopMenu("0");
//			System.out.println(topMenuList.toString());
			//ticketService.createTicket("����Ʊ");
			
			String teString = ticketService.createPageNumberIsNotUse("����Ʊ","2");
			System.out.println(teString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
