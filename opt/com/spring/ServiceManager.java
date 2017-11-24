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
	 * 获得组织机构处理类 OrgDao
	 * @return
	 */
	public static OrgDao getOrgDao(){
		return (OrgDao)ServiceManager.getServiceBean("OrgDaoBean");
	}
	
	/**
	 * 获得角色处理类 RoleDao
	 * @return
	 */
	public static RoleDao getRoleDao(){
		return (RoleDao)ServiceManager.getServiceBean("RoleDaoBean");
	}
	
	/**
	 * 获得用户处理类 UserDao
	 * @return
	 */
	public static UserDao getUserDao(){
		return (UserDao)ServiceManager.getServiceBean("UserDaoBean");
	}
	
	/**
	 * 获得权限处理类 PrivDao
	 * @return
	 */
	public static PrivDao getPrivDao(){
		return (PrivDao)ServiceManager.getServiceBean("PrivDaoBean");
	}
	
	/**
	 * 获得门户处理类 PortalInfoDao
	 * @return
	 */
	public static PortalInfoDao getPortalInfoDao(){
		return (PortalInfoDao)ServiceManager.getServiceBean("PortalInfoDaoBean");
	}
	
	/**
	 * 获得系统处理类 SystemDao
	 * @return
	 */
	public static SystemDao getSystemDao(){
		return (SystemDao)ServiceManager.getServiceBean("SystemDaoBean");
	}
	
	/**
	 * 用户注册模块组织机构信息 RegisterDao
	 * @return
	 */
	public static UserRegisterDao getUserRegisterDaoBean(){
		return (UserRegisterDao)ServiceManager.getServiceBean("UserRegisterDaoBean");
	}
	
	/**
	 * 用户注册模块组织机构信息 RegisterDao
	 * @return
	 */
	public static RegisterDao getUserRegisterOrgDaoBean(){
		return (RegisterDao)ServiceManager.getServiceBean("UserRegisterOrgDaoBean");
	}
	
	/**
	 * 门户统计模块 StatisticsDao
	 * @return
	 */
	public static StatisticsDao getStatisticsDao(){
		return (StatisticsDao)ServiceManager.getServiceBean("StatisticsDaoBean");
	}
	
}
