package kd.idp.psidpapp.opt.ticket.ro;

import java.util.List;
import java.util.Map;

import kd.idp.psidpapp.opt.ticket.service.RecvTicketService;


import com.spring.SpringBeanFactory;

public class RecvTicketRo {
	
	/**
	 * ��ȡ��Ӧ����Ʊ��������Ʊ��Ϣ
	 * @param ticketId		����ƱID
	 * @return				����һ�Ų���Ʊ��Ʊ��
	 * @throws Exception
	 */
	public List<Map<String, Object>> findRecvTicketList(String ticketId) throws Exception{
		RecvTicketService recvTicketService = (RecvTicketService)SpringBeanFactory.getInstance().getBean("recvTicketService");
		return recvTicketService.findRecvTicketList(ticketId);
	}
	
	/**
	 * ������Ʊ��Ϣ
	 * @param ticketId		����ƱID
	 * @return				���ز���������
	 * @throws Exception
	 */
	public Boolean createRecvTicketList(String ticketId)throws Exception{
		RecvTicketService recvTicketService = (RecvTicketService)SpringBeanFactory.getInstance().getBean("recvTicketService");
		return recvTicketService.createRecvTicketList(ticketId);
	}
	
	/**
	 * ������Ʊ��Ϣ
	 * @param  recvTicketMap		ǰ����Ʊ��Ϣ
	 * @return						���ز������
	 * @throws Exception
	 */
	public Boolean updateRecvTicket(Map<String,Object> recvTicketMap)throws Exception{
		RecvTicketService recvTicketService = (RecvTicketService)SpringBeanFactory.getInstance().getBean("recvTicketService");
		return recvTicketService.updateRecvTicket(recvTicketMap);
	}
	
	/**
	 * ɾ����Ʊ��Ϣ
	 * @param ticketId		����ƱID
	 * @return				���ز���������
	 * @throws Exception
	 */
	public Boolean delRecvTicketList(String ticketId) throws Exception{
		RecvTicketService recvTicketService = (RecvTicketService)SpringBeanFactory.getInstance().getBean("recvTicketService");
		return recvTicketService.delRecvTicketList(ticketId);
	}
}
