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
	private static String TICKET_NP = "拟票";
	private static String TICKET_SH = "审核";
	private static String TICKET_XF = "下发";
	private static String TICKET_ZX = "执行";
	private static String TICKET_JC = "检查";
	private static String TICKET_GD = "归档";
	private static String TICKET_GD2 = "'合格','缺陷'";
	private static String TICKET_SEARCH = "查询";
	private static String TICKET_SEARCH2 = "'拟票','审核','下发','执行','检查','合格','缺陷','作废'";
	
	private static String[] TICKET_TYPE = new String[]{"调试票","逐项票","综合票"};
	private static String[] TICKET_TYPE_NEW = new String[]{"新建调试票","新建逐项票","新建综合票"};
	
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
	 * @param parentId		操作票系统菜单ID
	 * @return				初始化操作票顶层菜单
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
				//1、处理自定义链接（class=2）
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
							//插入新建拟票功能
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
				//2、处理正常链接（class=1）
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
	
	//将操作票转换为菜单栏目
	public void ticketList2MenuList(List<Map<String, Object>> ticketList, String icon)throws Exception{
		for (Map<String, Object> dataMap : ticketList){
			dataMap.put("ID", dataMap.get("ID"));
			dataMap.put("NAME",  dataMap.get("TICKETNUMBER"));
			dataMap.put("NICKNAME",  dataMap.get("OPERTASK"));
			dataMap.put("ICON",  icon);
		}
	}
	
	/**
	 * @param pagePatternId	操作票编号模板ID(1:调试票,2:逐项票,3:综合票)
	 * @return				返回操作票编号
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
	 * 创建操作票编号（根据不同操作类型）
	 * @param ticketType		操作票类型
	 * @return					返回创建操作票编号[TICKETNUMBER]（根据不同操作类型）
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
	 * @param ticketType	操作票类型(调试票、逐项票、综合票)
	 * @return				返回新建操作票信息
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
				//生成唯一主键ID
				ticketId = SerialService.getSerialIdByDate("");
				//创建票编号
				String pagePatternId = "";
				for (int i=0; i<TICKET_TYPE.length; i++){
					if (TICKET_TYPE[i].equals(ticketType)){
						pagePatternId = String.valueOf(i+1);
					}
				}
				//a、判断是否有闲置的票号未使用
				List<Map<String,Object>> updateTicketNumberList = getUpdateTicketNumberList(ticketType);
				if (null != updateTicketNumberList && 0 != updateTicketNumberList.size()){
					ticketNumber = String.valueOf(updateTicketNumberList.get(updateTicketNumberList.size()-1).get("TICKETNUMBER"));
				}else{
					ticketNumber = createPageNumberIsNotUse(ticketType, pagePatternId);
				}
				//新增数据
				ticketMap.put("ID", ticketId);
				ticketMap.put("TICKETNUMBER", ticketNumber);
				ticketMap.put("TICKETSTATUS", ticketStatus);
				ticketMap.put("TICKETTYPE", ticketType);
				ticketMap.put("DATASTATUSID", dataStatusId);
				ticketMap.put("CREATEUSERID", createUserId);
				ticketMap.put("CREATETIME", createTime);
				ticketDao.addMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_TICKET, FieldName.FIELD_OPT_OPT_TICKET, ticketMap);
				//二、判断，如果是综合票，则同时要创建操作项；
				if ("综合票".equals(ticketType)){
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
	 * 查询操作票信息
	 * @param ticketId		操作票编号
	 * @return				返回一张操作票信息
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
	 * 更新操作票
	 * @param  ticketMap		前端操作票信息
	 * @return					更新操作票信息
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
	
	
	
	/*********************************************全局控制接口 开始***********************************************/
	/**
	 * 获取操作票所有信息
	 * @param ticketId		操作票ID
	 * @return				获取操作票所有信息
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
	 * 保存所有操作票信息
	 * @param ticketInfoMap	操作票所有信息
	 * @return				返回操作结果信息
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
	 * 删除操作票所有信息
	 * @param ids			操作票ID集
	 * @return				返回操作结果真假
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
					//先删除操作项（外键）
					result = itemDao.delMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_ITEM, sqlConditions.replace("ID IN(", "TICKETID IN("));
					//再删除操作令（外键）
					if (result){
						result = orderDao.delMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_ORDER, sqlConditions.replace("ID IN(", "TICKETID IN("));
					}
					//然后删除受票信息（外键）
					if (result){
						result = recvTicketDao.delMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_RECVTICEKT, sqlConditions.replace("ID IN(", "TICKETID IN("));
					}
					//最后删除操作票 
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
	 * 获取可修改票号
	 * @param ticketType	操作票编号模板ID(1:调试票,2:逐项票,3:综合票)
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
			if ("调试票".equals(ticketType)){
				pagePatternId  = "1";
			}else if ("逐项票".equals(ticketType)){
				pagePatternId  = "2";
			}else if ("综合票".equals(ticketType)){
				pagePatternId  = "3";
			}
			if (StringUtils.isNotEmpty(pagePatternId)){
				SerialService serialService = (SerialService)SpringBeanFactory.getInstance().getBean("serialService");
				PageNumberPattern pageNumberPattern = serialService.getPageNumberPattern(pagePatternId);
				if (null != pageNumberPattern){
					//获取实例编号增长值
					String serialNumStr = serialService.getSerialNum(pagePatternId);
					if (null == serialNumStr){//编号模板无实例化，无需再查找
						return null;
					}
					long serialNum = Long.parseLong(serialNumStr);
					//获取现有票号
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
	 * 保存操作票操作开始时间
	 * @param ticketId		操作票ID
	 * @return 返回操作结果
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
			//1、更新操作项为可以执行
			ItemService itemService = (ItemService) SpringBeanFactory.getInstance().getBean("itemService");
			List<Map<String,Object>> dbItemList = itemService.findItemList(ticketId);
			List<Map<String,Object>> vItemList = new ArrayList<Map<String,Object>>();
			for (Map<String,Object> tempItem : dbItemList){
				Map<String,Object> vItemMap = new HashMap<String, Object>();
				vItemMap.put("ID", tempItem.get("ID"));
				vItemMap.put("OPERLSTATUS", "可以执行");
				vItemList.add(vItemMap);
				itemService.updateItem(vItemMap);
			}
			//2、更新操作票开始时间
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
	 * 保存操作票操作结束时间
	 * @param ticketId		操作票ID
	 * @return 返回操作结果
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
			dbMap.put("TICKETSTATUS", "检查");
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
	/*********************************************全局控制接口 结束***********************************************/
	
	/**
	 * 拟票、审核、下发、执行、检查（仅限于前置状态），清除将执行阶段信息（方便运维）
	 * @param ticketId	操作票ID
	 * @param srcStatus	源状态
	 * @param destStatus	目标状态
	 * @return	拟票、审核、下发、执行、检查（仅限于前置状态），清除将执行阶段信息（方便运维）
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
			Map<String, Object> ticketMap = new HashMap<String, Object>();
			if ("拟票".equals(destStatus) && -1 != "审核,下发,执行,检查,合格,缺陷,作废".indexOf(srcStatus)){
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
				ticketMap.put("TICKETSTATUS", "拟票");
				ticketMap.put("UPDATETIME", DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
				ticketMap.put("UPDATEUSERID", userMap.get("ID"));
				return ticketDao.updateMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_TICKET, FieldName.FIELD_OPT_OPT_TICKET, "ID='" + ticketId + "'", ticketMap);
			}else if ("审核".equals(destStatus) && -1 != "下发,执行,检查,合格,缺陷,作废".indexOf(srcStatus)){
				ticketMap.put("OPENTICKETTIME", "");
				ticketMap.put("AUDITORID", "");
				ticketMap.put("AUDITOR", "");
				ticketMap.put("RECVTICKETERID", "");
				ticketMap.put("RECVTICKETER", "");
				ticketMap.put("RECVTIME", "");
				ticketMap.put("TICKETOPERSTARTTIME", "");
				ticketMap.put("TICKETOPERENTTIME", "");
				ticketMap.put("TICKETSTATUS", "审核");
				ticketMap.put("UPDATETIME", DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
				ticketMap.put("UPDATEUSERID", userMap.get("ID"));
				return ticketDao.updateMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_TICKET, FieldName.FIELD_OPT_OPT_TICKET, "ID='" + ticketId + "'", ticketMap);
			}else if ("下发".equals(destStatus) && -1 != "执行,检查,合格,缺陷,作废".indexOf(srcStatus)){
				ticketMap.put("RECVTICKETERID", "");
				ticketMap.put("RECVTICKETER", "");
				ticketMap.put("RECVTIME", "");
				ticketMap.put("TICKETOPERSTARTTIME", "");
				ticketMap.put("TICKETOPERENTTIME", "");
				ticketMap.put("TICKETSTATUS", "下发");
				ticketMap.put("UPDATETIME", DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
				ticketMap.put("UPDATEUSERID", userMap.get("ID"));
				return ticketDao.updateMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_TICKET, FieldName.FIELD_OPT_OPT_TICKET, "ID='" + ticketId + "'", ticketMap);
			}else if ("执行".equals(destStatus) && -1 != "检查,合格,缺陷,作废".indexOf(srcStatus)){
				ticketMap.put("TICKETOPERSTARTTIME", "");
				ticketMap.put("TICKETOPERENTTIME", "");
				ticketMap.put("TICKETSTATUS", "执行");
				ticketMap.put("UPDATETIME", DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
				ticketMap.put("UPDATEUSERID", userMap.get("ID"));
				return ticketDao.updateMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_OPT_TICKET, FieldName.FIELD_OPT_OPT_TICKET, "ID='" + ticketId + "'", ticketMap);
			}else if ("检查".equals(destStatus) && -1 != "合格,缺陷,作废".indexOf(srcStatus)){
				ticketMap.put("TICKETSTATUS", "检查");
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
	 * 操作票状态变更后发送提示消息
	 * @param subtopic	消息主题
	 * @param ticketId	操作票ID
	 * @param orgIds	接收消息用户组织机构Id
	 * @return	操作票状态变更后发送提示消息
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
	 * 创建未使用的操作票类型票号
	 * @param ticketType		操作票类型
	 * @param ticketType		操作票类型
	 * @return					返回创建操作票编号且未使用[TICKETNUMBER]（根据不同操作类型）
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
	 * 获取服务器时间
	 * @return 获取服务器时间
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
	
	//测试接口
	public static void main(String[] args) {
		try {
			TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
//			List<Map<String, Object>> updateTicketNumberList = ticketService.getUpdateTicketNumberList("综合票");
//			for (Map<String, Object> updateTicketNumberMap : updateTicketNumberList){
//				System.out.println(updateTicketNumberMap.get("TICKETNUMBER"));
//			}
//			List<Map<String, Object>> topMenuList = ticketService.initTopMenu("0");
//			System.out.println(topMenuList.toString());
			//ticketService.createTicket("调试票");
			
			String teString = ticketService.createPageNumberIsNotUse("逐项票","2");
			System.out.println(teString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
