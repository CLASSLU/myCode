package kd.idp.psidpapp.opt.ticket.ro;

import java.util.List;
import java.util.Map;

import kd.idp.psidpapp.opt.ticket.service.OptOrgService;

import com.spring.SpringBeanFactory;

public class OptOrgRo {
	
	/**
	 * 获取操作单位
	 * @return				获取操作单位
	 * @throws Exception
	 */
	public List<Map<String, Object>> findOperOrgList() throws Exception{
		OptOrgService optOrgService = (OptOrgService)SpringBeanFactory.getInstance().getBean("optOrgService");
		return optOrgService.findOperOrgList();
	}
	
	/**
	 * 查询相似单位名称
	 * @param operOrgName	操作单位名称		
	 * @return				获取操作单位
	 * @throws Exception
	 */
	public List<Map<String, Object>> findOperOrgListByOrgName(String operOrgName) throws Exception{
		OptOrgService optOrgService = (OptOrgService)SpringBeanFactory.getInstance().getBean("optOrgService");
		return optOrgService.findOperOrgListByOrgName(operOrgName);
	}

}
