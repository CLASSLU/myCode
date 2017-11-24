package kd.idp.psidpapp.opt.ticket.ro;

import java.util.List;
import java.util.Map;

import kd.idp.psidpapp.opt.ticket.service.ItemService;

import com.spring.SpringBeanFactory;

public class ItemRo {
	
	/**
	 * @param ticketId		操作票ID
	 * @return				返回一张操作票操作项集
	 * @throws Exception
	 */
	public List<Map<String, Object>> findItemList(String ticketId) throws Exception{
		ItemService itemService = (ItemService)SpringBeanFactory.getInstance().getBean("itemService");
		return itemService.findItemList(ticketId);
	}
	
	/**
	 * 创建操作项
	 * @param  ticketId		操作票ID
	 * @return				返回新建操作项信息
	 * @throws Exception
	 */
	public Map<String, Object> createItem(String ticketId)throws Exception{
		ItemService itemService = (ItemService)SpringBeanFactory.getInstance().getBean("itemService");
		return itemService.createItem(ticketId);
	}
	
	/**
	 * 默认新增时创建{addRows}条操作项
	 * @param  ticketId		操作票ID
	 * @param  addRows		默认创建条数
	 * @return				返回操作真假
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
	 * 插入操作项
	 * @param  ticketId		操作票ID
	 * @param  ticketId		操作票ID
	 * @return				返回插入操作项信息
	 * @throws Exception
	 */
	public Map<String, Object> insertItem(String ticketId, String suffIndex)throws Exception{
		ItemService itemService = (ItemService)SpringBeanFactory.getInstance().getBean("itemService");
		return itemService.insertItem(ticketId, suffIndex);
	}
	
	
	
	/**
	 * 更新操作项
	 * @param  itemMap		前端操作项信息
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean updateItem(Map<String,Object> itemMap)throws Exception{
		ItemService itemService = (ItemService)SpringBeanFactory.getInstance().getBean("itemService");
		return itemService.updateItem(itemMap);
	}
	
	/**
	 * 批量更新操作项
	 * @param  itemMapList		前端操作项信息集
	 * @return					返回操作结果真假
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
	 * 删除操作项
	 * @param ids			操作项ID
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean delItemList(String ids) throws Exception{
		ItemService itemService = (ItemService)SpringBeanFactory.getInstance().getBean("itemService");
		return itemService.delItemList(ids);
	}
	
	/**
	 * 校正操作项编号(删除后需调用)
	 * @param ticketId		操作票ID
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean checkItemListSort(String ticketId) throws Exception{
		ItemService itemService = (ItemService)SpringBeanFactory.getInstance().getBean("itemService");
		return itemService.checkItemListSort(ticketId);
	}

}
