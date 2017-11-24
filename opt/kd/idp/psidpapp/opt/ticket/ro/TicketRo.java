package kd.idp.psidpapp.opt.ticket.ro;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


import kd.idp.psidpapp.opt.sys.flex.FlexUtil;
import kd.idp.psidpapp.opt.sys.user.service.UserService;
import kd.idp.psidpapp.opt.ticket.service.MessageService;
import kd.idp.psidpapp.opt.ticket.service.TicketService;

import com.spring.SpringBeanFactory;

public class TicketRo {
	
	/**
	 * @param parentId		操作票系统菜单ID
	 * @return				初始化操作票顶层菜单
	 * @throws Exception
	 */
	public List<Map<String, Object>> initTopMenu(String parentId) throws Exception{
		TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
		return ticketService.initTopMenu(parentId);
	}
	
	/**
	 * 创建操作票编号（根据不同操作类型）
	 * @param ticketType		操作票类型
	 * @return					返回创建操作票编号[TICKETNUMBER]（根据不同操作类型）
	 * @throws Exception
	 */
	public Map<String, Object> createPageNumber(String ticketType)throws Exception{
		TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
		return ticketService.createPageNumber(ticketType);
	}
	
	/**
	 * @param ticketType	操作票类型(调试票、逐项票、综合票)
	 * @return				返回新建操作票信息
	 * @throws Exception
	 */
	public Map<String,Object> createTicket(String ticketType)throws Exception{
		TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
		return ticketService.createTicket(ticketType);
	}
	
	/**
	 * 查询操作票信息
	 * @param ticketId		操作票编号
	 * @return				返回一张操作票信息
	 * @throws Exception
	 */
	public Map<String, Object> findTicket(String ticketId) throws Exception{
		TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
		return ticketService.findTicket(ticketId);
	}
	
	/**
	 * 更新操作票
	 * @param  ticketMap		前端操作票信息
	 * @return					更新操作票信息
	 * @throws Exception
	 */
	public Boolean updateTicket(Map<String,Object> ticketMap)throws Exception{
		TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
		return ticketService.updateTicket(ticketMap);
	}
	
	/*********************************************全局控制接口 开始***********************************************/
	/**
	 * 获取操作票所有信息
	 * @param ticketId		操作票ID
	 * @return				获取操作票所有信息
	 * @throws Exception
	 */
	public Map<String,Object> findTicketInfoMap(String ticketId) throws Exception{
		TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
		return ticketService.findTicketInfoMap(ticketId);
	}
	
	/**
	 * 保存所有操作票信息
	 * @param ticketInfoMap	操作票所有信息
	 * @return				返回操作结果信息
	 * @throws Exception
	 */
	public Boolean saveTicketInfo(Map<String,Object> ticketInfoMap)throws Exception{
		TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
		return ticketService.saveTicketInfo(ticketInfoMap);
	}
	

	/**
	 * 删除操作票所有信息
	 * @param ids			操作票ID集
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean delTicketInfoList(String ids) throws Exception{
		TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
		return ticketService.delTicketInfoList(ids);
	}
	
	/**
	 * 获取可修改票号
	 * @param ticketType	操作票编号模板ID(1:调试票,2:逐项票,3:综合票)
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getUpdateTicketNumberList(String ticketType) throws Exception{
		TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
		return ticketService.getUpdateTicketNumberList(ticketType);
	}
	
	/**
	 * 保存操作票操作开始时间
	 * @param ticketId		操作票ID
	 * @return 返回操作结果
	 * @throws Exception
	 */
	public Map<String, Object> saveTicketOperStartTime(String ticketId)throws Exception{
		TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
		return ticketService.saveTicketOperStartTime(ticketId);
	}
	
	/**
	 * 保存操作票操作结束时间
	 * @param ticketId		操作票ID
	 * @return 返回操作结果
	 * @throws Exception
	 */
	public Map<String, Object> saveTicketOperEntTime(String ticketId)throws Exception{
		TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
		return ticketService.saveTicketOperEntTime(ticketId);
	}
	
	/**
	 * 拟票、审核、下发、执行、检查（仅限于前置状态），清除将执行阶段信息（方便运维）
	 * @param ticketId	操作票ID
	 * @param srcStatus	源状态
	 * @param destStatus	目标状态
	 * @return	拟票、审核、下发、执行、检查（仅限于前置状态），清除将执行阶段信息（方便运维）
	 * @throws Exception
	 */
	public boolean ticketStatusBH22ClearTicketInfo(String ticketId, String srcStatus, String destStatus)throws Exception{
		TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
		return ticketService.ticketStatusBH22ClearTicketInfo(ticketId, srcStatus, destStatus);
	}
	
	/*********************************************全局控制接口 结束***********************************************/
	
	/**
	 * 操作票状态变更后发送提示消息
	 * @param subtopic	消息主题
	 * @param ticketId	操作票ID
	 * @param orgIds	接收消息用户组织机构Id
	 * @return	操作票状态变更后发送提示消息
	 * @throws Exception
	 */
	public Boolean ticketStatusAfterSendTip(String subtopic, String ticketId, String orgIds)throws Exception{
		TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
		return ticketService.ticketStatusAfterSendTip(subtopic, ticketId, orgIds);
	}
	
	/**
	 * 获取服务器时间
	 * @return 获取服务器时间
	 * @throws Exception
	 */
	public Date getCurrentDate()throws Exception{
		TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
		return ticketService.getCurrentDate();
	}
}
