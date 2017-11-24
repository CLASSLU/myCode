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
	 * ��ȡ��Ӧ����Ʊ������ָ����Ϣ
	 * @param ticketId		����ƱID
	 * @return				����һ�Ų���Ʊָ�
	 * @throws Exception
	 */
	public List<Map<String, Object>> findOrderList(String ticketId) throws Exception{
		OrderService orderService = (OrderService)SpringBeanFactory.getInstance().getBean("orderService");
		return orderService.findOrderList(ticketId);
	}
	
	/**
	 * ����������
	 * @param  orderMap		ָ����Ϣ
	 * @return				�����½���������Ϣ
	 * @throws Exception
	 */
	public Map<String, Object> createOrder(Map<String, Object> orderMap)throws Exception{
		OrderService orderService = (OrderService)SpringBeanFactory.getInstance().getBean("orderService");
		return orderService.createOrder(orderMap);
	}
	
	/**
	 * ���²�����
	 * @param  orderMap		ǰ�˲�������Ϣ
	 * @return				���²�������Ϣ
	 * @throws Exception
	 */
	public Boolean updateOrder(Map<String,Object> orderMap)throws Exception{
		OrderService orderService = (OrderService)SpringBeanFactory.getInstance().getBean("orderService");
		return orderService.updateOrder(orderMap);
	}
	
	/**
	 * ɾ��������
	 * @param ids			������ID��
	 * @return				���ز���������
	 * @throws Exception
	 */
	public Boolean delOrderList(String ids) throws Exception{
		OrderService orderService = (OrderService)SpringBeanFactory.getInstance().getBean("orderService");
		return orderService.delOrderList(ids);
	}
	
	/**
	 * �������
	 * @param ticketStatus		����Ʊ״̬
	 * @param orderMap			ָ����Ϣ
	 * @param operOrderStatus	��������״̬
	 * @return					���ط���ɹ���ָ����Ϣ
	 * @throws Exception
	 */
	public Map<String,Object> checkTicketInfoStatus2SendOrder(String ticketStatus, Map<String, Object> orderMap, String operOrderStatus) throws Exception{
		OrderService orderService = (OrderService)SpringBeanFactory.getInstance().getBean("orderService");
		return orderService.checkTicketInfoStatus2SendOrder(ticketStatus, orderMap, operOrderStatus);
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
		OrderService orderService = (OrderService)SpringBeanFactory.getInstance().getBean("orderService");
		return orderService.checkTicketInfoStatus2UpdateOrder(ticketStatus, orderMap, operOrderStatus);
	}
	
	/**
	 * ��ȡ�������Ӧָ��TabҳID
	 * @param ticketId	����ƱID
	 * @param sort		���������
	 * @return	��ȡ�������Ӧָ��TabҳID
	 * @throws Exception
	 */
	public Map<String, Object> selectOrderTabIdPageByTicket2Sort(String ticketId, String sort)throws Exception{
		OrderService orderService = (OrderService)SpringBeanFactory.getInstance().getBean("orderService");
		return orderService.selectOrderTabIdPageByTicket2Sort(ticketId, sort);
	}
	
}
