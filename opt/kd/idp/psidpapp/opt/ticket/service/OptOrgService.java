package kd.idp.psidpapp.opt.ticket.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import kd.idp.psidpapp.opt.sys.constant.DataSourceName;
import kd.idp.psidpapp.opt.sys.constant.TableName;
import kd.idp.psidpapp.opt.sys.flex.FlexUtil;
import kd.idp.psidpapp.opt.ticket.dao.OptOrgDao;

public class OptOrgService {
	
	private OptOrgDao optOrgDao;
	
	public OptOrgDao getOptOrgDao() {
		return optOrgDao;
	}

	public void setOptOrgDao(OptOrgDao optOrgDao) {
		this.optOrgDao = optOrgDao;
	}
	
	/**
	 * 获取操作单位
	 * @return				获取操作单位
	 * @throws Exception
	 */
	public List<Map<String, Object>> findOperOrgList() throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		List<Map<String, Object>> orgMapList = null;
		try {
			orgMapList = optOrgDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_ORG, "DATASTATUSID='1' AND CLASS IN ('发电厂','变电站') ORDER BY CLASS ASC");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return orgMapList;
	}
	
	/**
	 * 查询相似单位名称
	 * @param operOrgName	操作单位名称		
	 * @return				获取操作单位
	 * @throws Exception
	 */
	public List<Map<String, Object>> findOperOrgListByOrgName(String operOrgName) throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		List<Map<String, Object>> orgMapList = null;
		try {
			if (StringUtils.isNotEmpty(operOrgName)){
				orgMapList = optOrgDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_ORG, "DATASTATUSID='1' AND CLASS IN ('发电厂','变电站') AND NICKNAME LIKE '%" + operOrgName + "%' ORDER BY CLASS ASC");
			}else{
				orgMapList = optOrgDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_ORG, "DATASTATUSID='1' AND CLASS IN ('发电厂','变电站') ORDER BY CLASS ASC");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return orgMapList;
	}

}
