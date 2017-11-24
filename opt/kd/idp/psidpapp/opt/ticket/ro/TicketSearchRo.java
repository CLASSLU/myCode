package kd.idp.psidpapp.opt.ticket.ro;

import java.util.Map;


import kd.idp.psidpapp.opt.ticket.service.TicketSearchService;

import com.spring.SpringBeanFactory;

public class TicketSearchRo {

	
	/**
	 * ��ȡ����Ʊ����/�¡���
	 * @return	��ȡ����Ʊ����/�¡���
	 * @throws Exception
	 */
	public Map<String,Object> getTicketTreeDataMap(boolean isGd) throws Exception{
		TicketSearchService ticketSearchService = (TicketSearchService)SpringBeanFactory.getInstance().getBean("ticketSearchService");
		return ticketSearchService.getTicketTreeDataMap(isGd);
	}
	
	/**
	 * ��ȡ����Ʊͨ����ҳ����
	 * @param pageCondtionMap  ��ҳ����
	 * @return		��ȡ����Ʊͨ����ҳ����
	 * @throws Exception
	 */
	public Map<String,Object> getTickets2pageMapByPage(Map<String,Object> pageCondtionMap) throws Exception{
		TicketSearchService ticketSearchService = (TicketSearchService)SpringBeanFactory.getInstance().getBean("ticketSearchService");
		return ticketSearchService.getTickets2pageMapByPage(pageCondtionMap);
	}
}
