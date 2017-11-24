package kd.idp.psidpapp.opt.sys.org.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


import kd.idp.psidpapp.opt.sys.constant.DataSourceName;
import kd.idp.psidpapp.opt.sys.constant.FieldName;
import kd.idp.psidpapp.opt.sys.constant.TableName;
import kd.idp.psidpapp.opt.sys.flex.FlexUtil;
import kd.idp.psidpapp.opt.sys.org.dao.OrgDao;
import kd.idp.psidpapp.opt.sys.serial.service.SerialService;
import kd.idp.psidpapp.opt.sys.util.DateUtil;

public class OrgService {
	
	private OrgDao orgDao;

	public OrgDao getOrgDao() {
		return orgDao;
	}

	public void setOrgDao(OrgDao orgDao) {
		this.orgDao = orgDao;
	}
	
	/**
	 * ��ȡӦ����֯�������ü�ֵ�ԣ�ID:ORGMAP��
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getOrgConfigMap(String sqlCondition)throws Exception{
		Map<String, Object> orgConfigMap = new HashMap<String, Object>();
		try {
			if (StringUtils.isNotEmpty(sqlCondition)){
				List<Map<String,Object>> orgMapList = orgDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_ORG, sqlCondition);
				for (Map<String,Object> orgMap : orgMapList){
					orgConfigMap.put(String.valueOf(orgMap.get("ID")), orgMap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return orgConfigMap;
	}
	
	/**
	 * ��ȡ��Ӧ����֯����ID�ڵ���
	 * @param parentId		��֯����ID
	 * @return				��ȡ��Ӧ����֯����ID�ڵ���
	 * @throws Exception
	 */
	public List<Map<String, Object>> getOrgTreeNode(String parentId)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		List<Map<String, Object>> rootNoteMapList = null;
		try{
			if (StringUtils.isNotEmpty(parentId)){
				rootNoteMapList = orgDao.getOrgTreeNode(parentId);
			}
			return rootNoteMapList;
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * ������֯����
	 * @param  orgMap		������Ϣ
	 * @return				�����½���֯����
	 * @throws Exception
	 */
	public Map<String, Object> createOrg(Map<String, Object> orgMap)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		String id = "";
		String createUserId = String.valueOf(userMap.get("ID"));
		String createTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
		try {
			if (null != orgMap){
				//����Ψһ����ID
				id = SerialService.getSerialIdByDate("");
				orgMap.put("ID", id);
				orgMap.put("CREATEUSERID", createUserId);
				orgMap.put("CREATETIME", createTime);
				//���糧���⴦��1����д��ص�λ��ORDERUNITID = ID��2����д��
				if ("���糧".equals(String.valueOf(orgMap.get("CLASS"))) && "null".equalsIgnoreCase(String.valueOf(orgMap.get("ORDERUNITID"))) || !StringUtils.isNotEmpty(String.valueOf(orgMap.get("ORDERUNITID")))){
					orgMap.put("ORDERUNITID", id);
				}
				orgDao.addMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_ORG, FieldName.FIELD_SYS_ORG, orgMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return orgMap;
	}
	
	/**
	 * ������֯����
	 * @param  orgMap		ǰ����֯������Ϣ
	 * @return				���ز���������
	 * @throws Exception
	 */
	public Boolean updateOrg(Map<String,Object> orgMap)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		Boolean result = false;
		String updateUserId = String.valueOf(userMap.get("ID"));
		String updateTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
		try {
			if (null != orgMap && StringUtils.isNotEmpty(String.valueOf(orgMap.get("ID"))) && !"null".equalsIgnoreCase(String.valueOf(orgMap.get("ID")))){
				String id = String.valueOf(orgMap.get("ID"));
				orgMap.put("UPDATEUSERID", updateUserId);
				orgMap.put("UPDATETIME", updateTime);
				result = orgDao.updateMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_ORG, FieldName.FIELD_SYS_ORG, "ID='" + id + "'", orgMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	/**
	 * ɾ����֯����
	 * @param ids			��֯����ID��
	 * @return				���ز���������
	 * @throws Exception
	 */
	public Boolean delOrgList(String ids) throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		Boolean result = false;
		try {
			if (StringUtils.isNotEmpty(ids)){
				String sqlConditions = "ID IN(";
				String []idArray = ids.split(",");
				for (String id : idArray){
					sqlConditions += "'" + id + "',";
				}
				if (!"ID IN(".equals(sqlConditions)){
					sqlConditions = sqlConditions.substring(0,sqlConditions.length()-1) + ")";
					result = orgDao.delMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_ORG, sqlConditions);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	/**
	 * ��ȡ��֯����ID��������ID�ַ�����
	 * @param orgIds
	 * @return
	 * @throws Exception
	 */
	public String getOrgId2ParentOrgIdByIds(String orgIds)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		try {
			if (StringUtils.isNotEmpty(orgIds)){
				String sqlConditions = "ID IN(";
				String []idArray = orgIds.split(",");
				for (String id : idArray){
					sqlConditions += "'" + id + "',";
				}
				if (!"ID IN(".equals(sqlConditions)){
					sqlConditions = sqlConditions.substring(0,sqlConditions.length()-1) + ")";
					List<Map<String, Object>> orgMapList = orgDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_ORG, sqlConditions);
					String parentOrgIds = mapList2String(orgMapList);
					orgIds += "," + parentOrgIds;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return orgIds;
		
	}
	
	 /* ��������û���֯����ID�ַ�����
	 * @param userOrgIdList	����û���֯����ID����
	 * @return	��������û���֯����ID�ַ�����
	 * @throws Exception
	 */
	public String mapList2String(List<Map<String, Object>> userOrgIdList)throws Exception{
		String result = "";
		for (Map<String, Object> map : userOrgIdList){
			result += String.valueOf(map.get("PARENTIDS")) + ",";
		}
		if (!"".equals(result)){
			result = result.substring(0,result.length()-1);
		}
		return result;
	}
	
	//��ȡ��֯������ֵ�ԣ�orgId:orgName��
	public Map<String, String> getOrgKeyValue()throws Exception{
		Map<String, String> orgMap = new HashMap<String, String>();
		try {
			List<Map<String, Object>> dataMapList = orgDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_ORG, "DATASTATUSID='1'");
			for (Map<String, Object> dataMap : dataMapList){
				orgMap.put(String.valueOf(dataMap.get("ID")), String.valueOf(dataMap.get("NICKNAME")));
			}
			return orgMap;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	//��ȡ������ϵ��ֵ�ԣ�operOrgId:recvOrgId��
	public Map<String, String> getOrgKeyValue2()throws Exception{
		Map<String, String> orgMap = new HashMap<String, String>();
		try {
			List<Map<String, Object>> dataMapList = orgDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_ORG, "DATASTATUSID='1'");
			for (Map<String, Object> dataMap : dataMapList){
				orgMap.put(String.valueOf(dataMap.get("ID")), String.valueOf(dataMap.get("ORDERUNITID")));
			}
			return orgMap;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
}
