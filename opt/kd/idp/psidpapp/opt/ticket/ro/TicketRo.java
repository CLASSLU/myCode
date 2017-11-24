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
	 * @param parentId		����Ʊϵͳ�˵�ID
	 * @return				��ʼ������Ʊ����˵�
	 * @throws Exception
	 */
	public List<Map<String, Object>> initTopMenu(String parentId) throws Exception{
		TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
		return ticketService.initTopMenu(parentId);
	}
	
	/**
	 * ��������Ʊ��ţ����ݲ�ͬ�������ͣ�
	 * @param ticketType		����Ʊ����
	 * @return					���ش�������Ʊ���[TICKETNUMBER]�����ݲ�ͬ�������ͣ�
	 * @throws Exception
	 */
	public Map<String, Object> createPageNumber(String ticketType)throws Exception{
		TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
		return ticketService.createPageNumber(ticketType);
	}
	
	/**
	 * @param ticketType	����Ʊ����(����Ʊ������Ʊ���ۺ�Ʊ)
	 * @return				�����½�����Ʊ��Ϣ
	 * @throws Exception
	 */
	public Map<String,Object> createTicket(String ticketType)throws Exception{
		TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
		return ticketService.createTicket(ticketType);
	}
	
	/**
	 * ��ѯ����Ʊ��Ϣ
	 * @param ticketId		����Ʊ���
	 * @return				����һ�Ų���Ʊ��Ϣ
	 * @throws Exception
	 */
	public Map<String, Object> findTicket(String ticketId) throws Exception{
		TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
		return ticketService.findTicket(ticketId);
	}
	
	/**
	 * ���²���Ʊ
	 * @param  ticketMap		ǰ�˲���Ʊ��Ϣ
	 * @return					���²���Ʊ��Ϣ
	 * @throws Exception
	 */
	public Boolean updateTicket(Map<String,Object> ticketMap)throws Exception{
		TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
		return ticketService.updateTicket(ticketMap);
	}
	
	/*********************************************ȫ�ֿ��ƽӿ� ��ʼ***********************************************/
	/**
	 * ��ȡ����Ʊ������Ϣ
	 * @param ticketId		����ƱID
	 * @return				��ȡ����Ʊ������Ϣ
	 * @throws Exception
	 */
	public Map<String,Object> findTicketInfoMap(String ticketId) throws Exception{
		TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
		return ticketService.findTicketInfoMap(ticketId);
	}
	
	/**
	 * �������в���Ʊ��Ϣ
	 * @param ticketInfoMap	����Ʊ������Ϣ
	 * @return				���ز��������Ϣ
	 * @throws Exception
	 */
	public Boolean saveTicketInfo(Map<String,Object> ticketInfoMap)throws Exception{
		TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
		return ticketService.saveTicketInfo(ticketInfoMap);
	}
	

	/**
	 * ɾ������Ʊ������Ϣ
	 * @param ids			����ƱID��
	 * @return				���ز���������
	 * @throws Exception
	 */
	public Boolean delTicketInfoList(String ids) throws Exception{
		TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
		return ticketService.delTicketInfoList(ids);
	}
	
	/**
	 * ��ȡ���޸�Ʊ��
	 * @param ticketType	����Ʊ���ģ��ID(1:����Ʊ,2:����Ʊ,3:�ۺ�Ʊ)
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getUpdateTicketNumberList(String ticketType) throws Exception{
		TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
		return ticketService.getUpdateTicketNumberList(ticketType);
	}
	
	/**
	 * �������Ʊ������ʼʱ��
	 * @param ticketId		����ƱID
	 * @return ���ز������
	 * @throws Exception
	 */
	public Map<String, Object> saveTicketOperStartTime(String ticketId)throws Exception{
		TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
		return ticketService.saveTicketOperStartTime(ticketId);
	}
	
	/**
	 * �������Ʊ��������ʱ��
	 * @param ticketId		����ƱID
	 * @return ���ز������
	 * @throws Exception
	 */
	public Map<String, Object> saveTicketOperEntTime(String ticketId)throws Exception{
		TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
		return ticketService.saveTicketOperEntTime(ticketId);
	}
	
	/**
	 * ��Ʊ����ˡ��·���ִ�С���飨������ǰ��״̬���������ִ�н׶���Ϣ��������ά��
	 * @param ticketId	����ƱID
	 * @param srcStatus	Դ״̬
	 * @param destStatus	Ŀ��״̬
	 * @return	��Ʊ����ˡ��·���ִ�С���飨������ǰ��״̬���������ִ�н׶���Ϣ��������ά��
	 * @throws Exception
	 */
	public boolean ticketStatusBH22ClearTicketInfo(String ticketId, String srcStatus, String destStatus)throws Exception{
		TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
		return ticketService.ticketStatusBH22ClearTicketInfo(ticketId, srcStatus, destStatus);
	}
	
	/*********************************************ȫ�ֿ��ƽӿ� ����***********************************************/
	
	/**
	 * ����Ʊ״̬���������ʾ��Ϣ
	 * @param subtopic	��Ϣ����
	 * @param ticketId	����ƱID
	 * @param orgIds	������Ϣ�û���֯����Id
	 * @return	����Ʊ״̬���������ʾ��Ϣ
	 * @throws Exception
	 */
	public Boolean ticketStatusAfterSendTip(String subtopic, String ticketId, String orgIds)throws Exception{
		TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
		return ticketService.ticketStatusAfterSendTip(subtopic, ticketId, orgIds);
	}
	
	/**
	 * ��ȡ������ʱ��
	 * @return ��ȡ������ʱ��
	 * @throws Exception
	 */
	public Date getCurrentDate()throws Exception{
		TicketService ticketService = (TicketService)SpringBeanFactory.getInstance().getBean("ticketService");
		return ticketService.getCurrentDate();
	}
}
