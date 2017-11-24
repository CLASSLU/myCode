package kd.idp.psidpapp.opt.ticket.ro;

import java.util.Map;


import kd.idp.psidpapp.opt.ticket.service.TicketSearchService;

import com.spring.SpringBeanFactory;

public class TicketSearchRo {

	
	/**
	 * 获取操作票【年/月】树
	 * @return	获取操作票【年/月】树
	 * @throws Exception
	 */
	public Map<String,Object> getTicketTreeDataMap(boolean isGd) throws Exception{
		TicketSearchService ticketSearchService = (TicketSearchService)SpringBeanFactory.getInstance().getBean("ticketSearchService");
		return ticketSearchService.getTicketTreeDataMap(isGd);
	}
	
	/**
	 * 获取操作票通过分页条件
	 * @param pageCondtionMap  分页条件
	 * @return		获取操作票通过分页条件
	 * @throws Exception
	 */
	public Map<String,Object> getTickets2pageMapByPage(Map<String,Object> pageCondtionMap) throws Exception{
		TicketSearchService ticketSearchService = (TicketSearchService)SpringBeanFactory.getInstance().getBean("ticketSearchService");
		return ticketSearchService.getTickets2pageMapByPage(pageCondtionMap);
	}
}
