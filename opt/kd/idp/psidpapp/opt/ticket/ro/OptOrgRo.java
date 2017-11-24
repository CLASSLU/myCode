package kd.idp.psidpapp.opt.ticket.ro;

import java.util.List;
import java.util.Map;

import kd.idp.psidpapp.opt.ticket.service.OptOrgService;

import com.spring.SpringBeanFactory;

public class OptOrgRo {
	
	/**
	 * ��ȡ������λ
	 * @return				��ȡ������λ
	 * @throws Exception
	 */
	public List<Map<String, Object>> findOperOrgList() throws Exception{
		OptOrgService optOrgService = (OptOrgService)SpringBeanFactory.getInstance().getBean("optOrgService");
		return optOrgService.findOperOrgList();
	}
	
	/**
	 * ��ѯ���Ƶ�λ����
	 * @param operOrgName	������λ����		
	 * @return				��ȡ������λ
	 * @throws Exception
	 */
	public List<Map<String, Object>> findOperOrgListByOrgName(String operOrgName) throws Exception{
		OptOrgService optOrgService = (OptOrgService)SpringBeanFactory.getInstance().getBean("optOrgService");
		return optOrgService.findOperOrgListByOrgName(operOrgName);
	}

}
