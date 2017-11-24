package kd.idp.psidpapp.opt.ticket.ro;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import kd.idp.psidpapp.opt.sys.flex.FlexUtil;
import kd.idp.psidpapp.opt.sys.util.DateUtil;
import kd.idp.psidpapp.opt.ticket.constant.DataSourceName;
import kd.idp.psidpapp.opt.ticket.constant.TableName;
import kd.idp.psidpapp.opt.ticket.service.ItemService;
import kd.idp.psidpapp.opt.ticket.service.OrderService;
import kd.idp.psidpapp.opt.ticket.service.TicketService;

import com.spring.SpringBeanFactory;

public class OrderRo {
	
	/**
	 * 获取对应操作票的所有指令信息
	 * @param ticketId		操作票ID
	 * @return				返回一张操作票指令集
	 * @throws Exception
	 */
	public List<Map<String, Object>> findOrderList(String ticketId) throws Exception{
		OrderService orderService = (OrderService)SpringBeanFactory.getInstance().getBean("orderService");
		return orderService.findOrderList(ticketId);
	}
	
	/**
	 * 创建操作令
	 * @param  orderMap		指令信息
	 * @return				返回新建操作令信息
	 * @throws Exception
	 */
	public Map<String, Object> createOrder(Map<String, Object> orderMap)throws Exception{
		OrderService orderService = (OrderService)SpringBeanFactory.getInstance().getBean("orderService");
		return orderService.createOrder(orderMap);
	}
	
	/**
	 * 更新操作令
	 * @param  orderMap		前端操作令信息
	 * @return				更新操作令信息
	 * @throws Exception
	 */
	public Boolean updateOrder(Map<String,Object> orderMap)throws Exception{
		OrderService orderService = (OrderService)SpringBeanFactory.getInstance().getBean("orderService");
		return orderService.updateOrder(orderMap);
	}
	
	/**
	 * 删除操作令
	 * @param ids			操作令ID集
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean delOrderList(String ids) throws Exception{
		OrderService orderService = (OrderService)SpringBeanFactory.getInstance().getBean("orderService");
		return orderService.delOrderList(ids);
	}
	
	/**
	 * 发令操作
	 * @param ticketStatus		操作票状态
	 * @param orderMap			指令信息
	 * @param operOrderStatus	将操作的状态
	 * @return					返回发令成功后指令信息
	 * @throws Exception
	 */
	public Map<String,Object> checkTicketInfoStatus2SendOrder(String ticketStatus, Map<String, Object> orderMap, String operOrderStatus) throws Exception{
		OrderService orderService = (OrderService)SpringBeanFactory.getInstance().getBean("orderService");
		return orderService.checkTicketInfoStatus2SendOrder(ticketStatus, orderMap, operOrderStatus);
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
		OrderService orderService = (OrderService)SpringBeanFactory.getInstance().getBean("orderService");
		return orderService.checkTicketInfoStatus2UpdateOrder(ticketStatus, orderMap, operOrderStatus);
	}
	
	/**
	 * 获取操作项对应指令Tab页ID
	 * @param ticketId	操作票ID
	 * @param sort		操作项序号
	 * @return	获取操作项对应指令Tab页ID
	 * @throws Exception
	 */
	public Map<String, Object> selectOrderTabIdPageByTicket2Sort(String ticketId, String sort)throws Exception{
		OrderService orderService = (OrderService)SpringBeanFactory.getInstance().getBean("orderService");
		return orderService.selectOrderTabIdPageByTicket2Sort(ticketId, sort);
	}
	
}
