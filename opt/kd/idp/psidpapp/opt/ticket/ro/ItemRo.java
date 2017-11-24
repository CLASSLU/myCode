package kd.idp.psidpapp.opt.ticket.ro;

import java.util.List;
import java.util.Map;

import kd.idp.psidpapp.opt.ticket.service.ItemService;

import com.spring.SpringBeanFactory;

public class ItemRo {
	
	/**
	 * @param ticketId		����ƱID
	 * @return				����һ�Ų���Ʊ�����
	 * @throws Exception
	 */
	public List<Map<String, Object>> findItemList(String ticketId) throws Exception{
		ItemService itemService = (ItemService)SpringBeanFactory.getInstance().getBean("itemService");
		return itemService.findItemList(ticketId);
	}
	
	/**
	 * ����������
	 * @param  ticketId		����ƱID
	 * @return				�����½���������Ϣ
	 * @throws Exception
	 */
	public Map<String, Object> createItem(String ticketId)throws Exception{
		ItemService itemService = (ItemService)SpringBeanFactory.getInstance().getBean("itemService");
		return itemService.createItem(ticketId);
	}
	
	/**
	 * Ĭ������ʱ����{addRows}��������
	 * @param  ticketId		����ƱID
	 * @param  addRows		Ĭ�ϴ�������
	 * @return				���ز������
	 * @throws Exception
	 */
	public boolean createItem(String ticketId,int addRows)throws Exception{
		ItemService itemService = (ItemService)SpringBeanFactory.getInstance().getBean("itemService");
		for (int i=0; i<addRows; i++){
			itemService.createItem(ticketId);
		}
		return true;
	}
	
	/**
	 * ���������
	 * @param  ticketId		����ƱID
	 * @param  ticketId		����ƱID
	 * @return				���ز����������Ϣ
	 * @throws Exception
	 */
	public Map<String, Object> insertItem(String ticketId, String suffIndex)throws Exception{
		ItemService itemService = (ItemService)SpringBeanFactory.getInstance().getBean("itemService");
		return itemService.insertItem(ticketId, suffIndex);
	}
	
	
	
	/**
	 * ���²�����
	 * @param  itemMap		ǰ�˲�������Ϣ
	 * @return				���ز���������
	 * @throws Exception
	 */
	public Boolean updateItem(Map<String,Object> itemMap)throws Exception{
		ItemService itemService = (ItemService)SpringBeanFactory.getInstance().getBean("itemService");
		return itemService.updateItem(itemMap);
	}
	
	/**
	 * �������²�����
	 * @param  itemMapList		ǰ�˲�������Ϣ��
	 * @return					���ز���������
	 * @throws Exception
	 */
	public Boolean updateItemList(List<Map<String,Object>> itemMapList)throws Exception{
		ItemService itemService = (ItemService)SpringBeanFactory.getInstance().getBean("itemService");
		for (Map<String,Object> itemMap : itemMapList){
			itemService.updateItem(itemMap);
		}
		return true;
	}
	
	/**
	 * ɾ��������
	 * @param ids			������ID
	 * @return				���ز���������
	 * @throws Exception
	 */
	public Boolean delItemList(String ids) throws Exception{
		ItemService itemService = (ItemService)SpringBeanFactory.getInstance().getBean("itemService");
		return itemService.delItemList(ids);
	}
	
	/**
	 * У����������(ɾ���������)
	 * @param ticketId		����ƱID
	 * @return				���ز���������
	 * @throws Exception
	 */
	public Boolean checkItemListSort(String ticketId) throws Exception{
		ItemService itemService = (ItemService)SpringBeanFactory.getInstance().getBean("itemService");
		return itemService.checkItemListSort(ticketId);
	}

}
