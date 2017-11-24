package com.spring;

import kd.idp.cms.dao.OrgDao;
import kd.idp.cms.dao.PortalInfoDao;
import kd.idp.cms.dao.PrivDao;
import kd.idp.cms.dao.RoleDao;
import kd.idp.cms.dao.SystemDao;
import kd.idp.cms.dao.UserDao;
import kd.idp.index.register.dao.RegisterDao;
import kd.idp.index.register.dao.UserRegisterDao;
import kd.idp.index.statistics.dao.StatisticsDao;


public class ServiceManager {

	public static Object getServiceBean(String beanName){
		Object serviceBean = null;
		try {
			serviceBean = SpringBeanFactory.getInstance().getBean(beanName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serviceBean;
	}
	

	/**
	 * �����֯���������� OrgDao
	 * @return
	 */
	public static OrgDao getOrgDao(){
		return (OrgDao)ServiceManager.getServiceBean("OrgDaoBean");
	}
	
	/**
	 * ��ý�ɫ������ RoleDao
	 * @return
	 */
	public static RoleDao getRoleDao(){
		return (RoleDao)ServiceManager.getServiceBean("RoleDaoBean");
	}
	
	/**
	 * ����û������� UserDao
	 * @return
	 */
	public static UserDao getUserDao(){
		return (UserDao)ServiceManager.getServiceBean("UserDaoBean");
	}
	
	/**
	 * ���Ȩ�޴����� PrivDao
	 * @return
	 */
	public static PrivDao getPrivDao(){
		return (PrivDao)ServiceManager.getServiceBean("PrivDaoBean");
	}
	
	/**
	 * ����Ż������� PortalInfoDao
	 * @return
	 */
	public static PortalInfoDao getPortalInfoDao(){
		return (PortalInfoDao)ServiceManager.getServiceBean("PortalInfoDaoBean");
	}
	
	/**
	 * ���ϵͳ������ SystemDao
	 * @return
	 */
	public static SystemDao getSystemDao(){
		return (SystemDao)ServiceManager.getServiceBean("SystemDaoBean");
	}
	
	/**
	 * �û�ע��ģ����֯������Ϣ RegisterDao
	 * @return
	 */
	public static UserRegisterDao getUserRegisterDaoBean(){
		return (UserRegisterDao)ServiceManager.getServiceBean("UserRegisterDaoBean");
	}
	
	/**
	 * �û�ע��ģ����֯������Ϣ RegisterDao
	 * @return
	 */
	public static RegisterDao getUserRegisterOrgDaoBean(){
		return (RegisterDao)ServiceManager.getServiceBean("UserRegisterOrgDaoBean");
	}
	
	/**
	 * �Ż�ͳ��ģ�� StatisticsDao
	 * @return
	 */
	public static StatisticsDao getStatisticsDao(){
		return (StatisticsDao)ServiceManager.getServiceBean("StatisticsDaoBean");
	}
	
}
