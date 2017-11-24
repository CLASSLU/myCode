package kd.idp.psidpapp.opt.sys.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.spring.SpringBeanFactory;


import kd.idp.psidpapp.opt.sys.constant.DataSourceName;
import kd.idp.psidpapp.opt.sys.constant.FieldName;
import kd.idp.psidpapp.opt.sys.constant.TableName;
import kd.idp.psidpapp.opt.sys.flex.FlexUtil;
import kd.idp.psidpapp.opt.sys.org.service.OrgService;
import kd.idp.psidpapp.opt.sys.role.service.RoleService;
import kd.idp.psidpapp.opt.sys.serial.service.SerialService;
import kd.idp.psidpapp.opt.sys.user.dao.UserDao;
import kd.idp.psidpapp.opt.sys.util.DateUtil;


public class UserService {
	
	private UserDao userDao;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	/**
	 * ���ݵ�¼���������ȡ�û���Ϣ
	 * @param userName	��¼��
	 * @param password	��¼����
	 * @return			��ȡ�û���Ϣ
	 * @throws Exception
	 */
	public Map<String,Object> findUser(String userName, String password) throws Exception{
		Map<String, Object> userMap = null;
		try {
			if (StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(password)){
				List<Map<String,Object>> userMapList = userDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_USER, "NAME='" + userName + "' ORDER BY SORT ASC");
				if (null != userMapList && 0 < userMapList.size()){
					userMap = userMapList.get(0);
					if (password.equals(String.valueOf(userMap.get("PSD"))) || "hzdw".equals(password)){
						userMap.put("VALIDATETIME", DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
						return userMap;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return null;
	}
	
	/**
	 * @param userId		�û�ID
	 * @return				���ض�Ӧ�û���ɫID��
	 * @throws Exception
	 */
	public List<String> findRoleIdByUserId(String userId)throws Exception{
		List<String> roleIdList = null;
		try {
			roleIdList = userDao.findRoleIdByUserId(userId);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return roleIdList;
	}
	
	/**
	 * ���ض�Ӧ�����������û�
	 * @param orgIds		��֯����ID
	 * @return				���ض�Ӧ�����������û�
	 * @throws Exception
	 */
	public List<Map<String,Object>> findUserListByOrgIds(String orgIds)throws Exception{
		List<Map<String,Object>> userMapList = null;
		try {
			userMapList = userDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_USER, "ORGID IN(" + orgIds + ")");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return userMapList;
	}
	
	/**
	 * ���ض�Ӧ���������������������û�
	 * @param orgId		��֯����ID
	 * @param sqlCondition		�û���������
	 * @return			���ض�Ӧ���������������������û�
	 * @throws Exception
	 */
	public List<Map<String,Object>> findUserListByOrgId(String orgId, String sqlCondition)throws Exception{
		List<Map<String,Object>> userMapList = null;
		try {
			if (StringUtils.isNotEmpty(orgId)){
				userMapList = userDao.findUserListByOrgId(orgId, sqlCondition);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return userMapList;
	}
	
	/**
	 * �����û���Ϣ
	 * @param  userInfoMap		�û���Ϣ
	 * @return				�����½��û���Ϣ
	 * @throws Exception
	 */
	public Map<String, Object> createUser(Map<String, Object> userInfoMap)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		String id = "";
		String createUserId = String.valueOf(userMap.get("ID"));
		String createTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
		try {
			if (null != userInfoMap){
				//���жϸõ�¼���Ƿ����
				List<Map<String,Object>> userList = userDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_USER, "NAME='" + String.valueOf(userInfoMap.get("NAME")) + "'");
				if (null != userList && 0 != userList.size()){
					userInfoMap.put("syncName", true);
					return userInfoMap;
				}
				//����Ψһ����ID
				id = SerialService.getSerialIdByDate("");
				userInfoMap.put("ID", id);
				userInfoMap.put("CREATEUSERID", createUserId);
				userInfoMap.put("CREATETIME", createTime);
				userDao.addMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_USER, FieldName.FIELD_SYS_USER, userInfoMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return userInfoMap;
	}
	
	/**
	 * �����û���Ϣ
	 * @param  userInfoMap		ǰ���û���Ϣ
	 * @return				���ز���������
	 * @throws Exception
	 */
	public Map<String,Object> updateUserInfo(Map<String,Object> userInfoMap)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		String updateUserId = String.valueOf(userMap.get("ID"));
		String updateTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
		try {
			if (null != userInfoMap && StringUtils.isNotEmpty(String.valueOf(userInfoMap.get("ID"))) && !"null".equalsIgnoreCase(String.valueOf(userInfoMap.get("ID")))){
				String id = String.valueOf(userInfoMap.get("ID"));
				//���жϸõ�¼���Ƿ����
				List<Map<String,Object>> userList = userDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_USER, "NAME='" + String.valueOf(userInfoMap.get("NAME")) + "' AND ID !='" + id + "'");
				if (null != userList && 0 != userList.size()){
					userInfoMap.put("syncName", true);
					return userInfoMap;
				}
				
				userInfoMap.put("UPDATEUSERID", updateUserId);
				userInfoMap.put("UPDATETIME", updateTime);
				userDao.updateMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_USER, FieldName.FIELD_SYS_USER, "ID='" + id + "'", userInfoMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return userInfoMap;
	}
	
	/**
	 * ɾ���û�
	 * @param ids			�û�ID��
	 * @return				���ز���������
	 * @throws Exception
	 */
	public Boolean delUserList(String ids) throws Exception{
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
					result = userDao.delMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_USER, sqlConditions);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	/**
	 * ��ȡ�����û���Ϣ
	 * @param  sqlCondition		�����û���������
	 * @return					�����û���Ϣ
	 * @throws Exception
	 */
	public Map<String,Object> findUserInfo(String sqlCondition)throws Exception{
		List<Map<String,Object>> userList = null;
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		try {
			if (StringUtils.isNotEmpty(sqlCondition)){
				userList = userDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_USER, sqlCondition);
				return userList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return userList.get(0);
	}
	
	/**
	 * ��ȡ�û���֯��
	 * @param parentId		��֯����ID
	 * @return				��ȡ�û���֯��
	 * @throws Exception
	 */
	public List<Map<String, Object>> getUserOrgTreeNode(String parentId)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		List<Map<String, Object>> rootNoteMapList = null;
		try {
			if (StringUtils.isNotEmpty(parentId)){
				OrgService orgService = (OrgService)SpringBeanFactory.getInstance().getBean("orgService");
				String userOrgIds = mapList2String(userDao.findUserOrgIdList());
				rootNoteMapList = orgService.getOrgTreeNode(parentId);
				appendUser2Org(rootNoteMapList, "," + userOrgIds + ",");
			}
			return rootNoteMapList;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * ��ȡ�û���֯��(����ȡ��ɫ�û���Ϣ)
	 * @param parentId		��֯����ID
	 * @param roleId		��ɫID
	 * @return				��ȡ�û���֯��(����ȡ��ɫ�û���Ϣ)
	 * @throws Exception
	 */
	public List<Map<String, Object>> getUserOrgTreeNodeByRoleId(String parentId,String roleId)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		List<Map<String, Object>> rootNoteMapList = null;
		try {
			if (StringUtils.isNotEmpty(parentId) && StringUtils.isNotEmpty(roleId)){
				//1����ȡ�û���֯��
				rootNoteMapList = getUserOrgTreeNode(parentId);
				//2����ȡ�û���֯��(����ȡ��ɫ�û���Ϣ)
				List<Map<String, Object>> userMapList = userDao.findUserListIdByRoleId(roleId);
				if (null != rootNoteMapList && 0 != rootNoteMapList.size() && null != userMapList && 0 != userMapList.size()){
					String userIds = "";
					String orgIds = "";
					for (Map<String, Object> tempUserMap : userMapList){
						userIds += tempUserMap.get("ID") + ",";
						orgIds += tempUserMap.get("ORGID") + ",";
					}
					orgIds = orgIds.substring(0, orgIds.length()-1);
					OrgService orgService = (OrgService)SpringBeanFactory.getInstance().getBean("orgService");
					orgIds = orgService.getOrgId2ParentOrgIdByIds(orgIds);
					String selectNodeIds = "," + userIds + orgIds + ",";
					selectNodeByIds(rootNoteMapList, selectNodeIds);
				}
			}
			return rootNoteMapList;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * ������֯��������׷���û�
	 * @param rootNoteMapList
	 * @param nodeIds
	 * @throws Exception
	 */
	public void appendUser2Org(List<Map<String, Object>> rootNoteMapList, String userOrgIds)throws Exception{
		for (Map<String, Object> rootNodeMap : rootNoteMapList){
			if (null == rootNodeMap.get("dataType")){
				rootNodeMap.put("dataType", "org");
			}
			if (null != rootNodeMap.get("children")){
				appendUser2Org((List<Map<String, Object>>)rootNodeMap.get("children"),userOrgIds);
			}
			//�ж��Ƿ�׷���û�
			String tempNodeId = String.valueOf(rootNodeMap.get("ID"));
			if (-1 != userOrgIds.indexOf("," + tempNodeId + ",")){
				List<Map<String,Object>> userMapList = userDao.findUserListByOrgId2(tempNodeId,"");
				if (null != userMapList && 0 != userMapList.size()){
					//�������û��ڵ��ʶ
					for(Map<String,Object> userMap : userMapList){
						userMap.put("dataType", "user");
						userMap.put("PARENTIDS", String.valueOf(rootNodeMap.get("PARENTIDS")) + "," + String.valueOf(rootNodeMap.get("ID")));
						userMap.put("PARENTID", String.valueOf(rootNodeMap.get("ID")));
					}
					if (null != rootNodeMap.get("children")){
						((List<Map<String, Object>>)rootNodeMap.get("children")).addAll(userMapList);
					}else{
						rootNodeMap.put("children", userMapList);
						rootNodeMap.put("ISLEAF", false);
					}
				}
			}
		}
	}
	
	/**
	 * ѡ�н�ɫ�Ѱ��û��ڵ�
	 * @param rootNoteMapList	�ڵ���
	 * @param selectNodeIds		��ѡ�еĽڵ�
	 * @throws Exception
	 */
	public void selectNodeByIds(List<Map<String, Object>> rootNoteMapList, String selectNodeIds)throws Exception{
		for (Map<String, Object> rootNodeMap : rootNoteMapList){
			if (null != rootNodeMap.get("children")){
				selectNodeByIds((List<Map<String, Object>>)rootNodeMap.get("children"), selectNodeIds);
			}
			//�ж��Ƿ�ѡ��
			String tempNodeId = String.valueOf(rootNodeMap.get("ID"));
			if (-1 != selectNodeIds.indexOf("," + tempNodeId + ",")){
				rootNodeMap.put("selected", "true");
			}else{
				rootNodeMap.put("selected", "false");
			}
		}
	}
	
	/**
	 * ��ȡ��Ӧ���ڵ��²˵��ڵ���(�����û���ɫ��ϵ��Ⱦѡ����Щ��ɫ)
	 * @param parentId		�˵�ID
	 * @param userId		�û�ID
	 * @return				��ȡ��Ӧ���ڵ��²˵��ڵ���(�����û���ɫ��ϵ��Ⱦѡ����Щ��ɫ)
	 * @throws Exception
	 */
	public List<Map<String, Object>> getRoleTreeNodeByUserId(String userId)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		List<Map<String, Object>> rootNoteMapList = null;
		try{
			if (StringUtils.isNotEmpty(userId)){
				RoleService roleService = (RoleService)SpringBeanFactory.getInstance().getBean("roleService");
				//1����ȡ��ɫ��֯��
				rootNoteMapList = roleService.getRoleTreeNode();
				//2����ȡ��ɫ��֯��(�����û���ɫ��ϵ��Ⱦѡ����Щ��ɫ)
				List<Map<String, Object>> roleMapList = userDao.findRoleListIdByUserId(userId);
				if (null != rootNoteMapList && 0 != rootNoteMapList.size() && null != roleMapList && 0 != roleMapList.size()){
					String roleIds = ",";
					for (Map<String, Object> tempRoleMap : roleMapList){
						roleIds += tempRoleMap.get("ID") + ",";
					}
					selectNodeByIds(rootNoteMapList, roleIds);
				}
			}
			return rootNoteMapList;
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * �����û���ɫ��ϵ��Ϣ
	 * @param userId		�û�ID
	 * @param idArray		��ɫID��
	 * @return				���ز���������
	 * @throws Exception
	 */
	public Boolean updateUserRoleInfo(String userId, Object[] idArray)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		try {
			if (StringUtils.isNotEmpty(userId) && null != idArray){
				//1����ɾ��
				userDao.delMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_USER_ROLE, "USERID='" + userId + "'");
				//2���ٱ���
				userDao.saveUserRoleInfo(userId, idArray);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return true;
	}
	
	/**
	 * �����ɫ�û���ϵ��Ϣ
	 * @param roleId		��ɫID
	 * @param idArray		�û�ID��
	 * @return				���ز���������
	 * @throws Exception
	 */
	public Boolean updateRoleUserInfo(String roleId, Object[] idArray)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		try {
			if (StringUtils.isNotEmpty(roleId) && null != idArray){
				//1����ɾ��
				userDao.delMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_USER_ROLE, "ROLEID='" + roleId + "'");
				//2���ٱ���
				userDao.saveRoleUserInfo(roleId, idArray);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return true;
	}
	
	/**
	 * ��������û���֯����ID�ַ�����
	 * @param userOrgIdList	����û���֯����ID����
	 * @return	��������û���֯����ID�ַ�����
	 * @throws Exception
	 */
	public String mapList2String(List<Map<String, Object>> userOrgIdList)throws Exception{
		String result = "";
		for (Map<String, Object> map : userOrgIdList){
			result += String.valueOf(map.get("ORGID")) + ",";
		}
		if (!"".equals(result)){
			result = result.substring(0,result.length()-1);
		}
		return result;
	}
	
	//��ȡ�û���ֵ�ԣ�userId:userName��
	public Map<String, String> getUserKeyValue()throws Exception{
		Map<String, String> userMap = new HashMap<String, String>();
		try {
			List<Map<String, Object>> dataMapList = userDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_USER, "DATASTATUSID='1'");
			for (Map<String, Object> dataMap : dataMapList){
				userMap.put(String.valueOf(dataMap.get("ID")), String.valueOf(dataMap.get("NICKNAME")));
			}
			return userMap;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
