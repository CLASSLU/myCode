package kd.idp.psidpapp.opt.ticket.ro;

import java.util.List;
import java.util.Map;

import kd.idp.psidpapp.opt.ticket.service.RecvTicketService;


import com.spring.SpringBeanFactory;

public class RecvTicketRo {
	
	/**
	 * 获取对应操作票的所有受票信息
	 * @param ticketId		操作票ID
	 * @return				返回一张操作票受票集
	 * @throws Exception
	 */
	public List<Map<String, Object>> findRecvTicketList(String ticketId) throws Exception{
		RecvTicketService recvTicketService = (RecvTicketService)SpringBeanFactory.getInstance().getBean("recvTicketService");
		return recvTicketService.findRecvTicketList(ticketId);
	}
	
	/**
	 * 创建受票信息
	 * @param ticketId		操作票ID
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean createRecvTicketList(String ticketId)throws Exception{
		RecvTicketService recvTicketService = (RecvTicketService)SpringBeanFactory.getInstance().getBean("recvTicketService");
		return recvTicketService.createRecvTicketList(ticketId);
	}
	
	/**
	 * 更新受票信息
	 * @param  recvTicketMap		前端受票信息
	 * @return						返回操作真假
	 * @throws Exception
	 */
	public Boolean updateRecvTicket(Map<String,Object> recvTicketMap)throws Exception{
		RecvTicketService recvTicketService = (RecvTicketService)SpringBeanFactory.getInstance().getBean("recvTicketService");
		return recvTicketService.updateRecvTicket(recvTicketMap);
	}
	
	/**
	 * 删除受票信息
	 * @param ticketId		操作票ID
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean delRecvTicketList(String ticketId) throws Exception{
		RecvTicketService recvTicketService = (RecvTicketService)SpringBeanFactory.getInstance().getBean("recvTicketService");
		return recvTicketService.delRecvTicketList(ticketId);
	}
}
