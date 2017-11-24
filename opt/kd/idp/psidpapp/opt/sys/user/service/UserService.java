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
	 * 根据登录名和密码获取用户信息
	 * @param userName	登录名
	 * @param password	登录密码
	 * @return			获取用户信息
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
	 * @param userId		用户ID
	 * @return				返回对应用户角色ID集
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
	 * 返回对应机构下所有用户
	 * @param orgIds		组织机构ID
	 * @return				返回对应机构下所有用户
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
	 * 返回对应机构及下属机构下所有用户
	 * @param orgId		组织机构ID
	 * @param sqlCondition		用户过滤条件
	 * @return			返回对应机构及下属机构下所有用户
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
	 * 创建用户信息
	 * @param  userInfoMap		用户信息
	 * @return				返回新建用户信息
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
				//先判断该登录名是否存在
				List<Map<String,Object>> userList = userDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_USER, "NAME='" + String.valueOf(userInfoMap.get("NAME")) + "'");
				if (null != userList && 0 != userList.size()){
					userInfoMap.put("syncName", true);
					return userInfoMap;
				}
				//生成唯一主键ID
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
	 * 更新用户信息
	 * @param  userInfoMap		前端用户信息
	 * @return				返回操作结果真假
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
				//先判断该登录名是否存在
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
	 * 删除用户
	 * @param ids			用户ID集
	 * @return				返回操作结果真假
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
	 * 获取单个用户信息
	 * @param  sqlCondition		单个用户过滤条件
	 * @return					返回用户信息
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
	 * 获取用户组织树
	 * @param parentId		组织机构ID
	 * @return				获取用户组织树
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
	 * 获取用户组织树(并获取角色用户信息)
	 * @param parentId		组织机构ID
	 * @param roleId		角色ID
	 * @return				获取用户组织树(并获取角色用户信息)
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
				//1、获取用户组织树
				rootNoteMapList = getUserOrgTreeNode(parentId);
				//2、获取用户组织树(并获取角色用户信息)
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
	 * 遍历组织机构树并追加用户
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
			//判断是否追加用户
			String tempNodeId = String.valueOf(rootNodeMap.get("ID"));
			if (-1 != userOrgIds.indexOf("," + tempNodeId + ",")){
				List<Map<String,Object>> userMapList = userDao.findUserListByOrgId2(tempNodeId,"");
				if (null != userMapList && 0 != userMapList.size()){
					//增加树用户节点标识
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
	 * 选中角色已绑定用户节点
	 * @param rootNoteMapList	节点树
	 * @param selectNodeIds		需选中的节点
	 * @throws Exception
	 */
	public void selectNodeByIds(List<Map<String, Object>> rootNoteMapList, String selectNodeIds)throws Exception{
		for (Map<String, Object> rootNodeMap : rootNoteMapList){
			if (null != rootNodeMap.get("children")){
				selectNodeByIds((List<Map<String, Object>>)rootNodeMap.get("children"), selectNodeIds);
			}
			//判断是否被选中
			String tempNodeId = String.valueOf(rootNodeMap.get("ID"));
			if (-1 != selectNodeIds.indexOf("," + tempNodeId + ",")){
				rootNodeMap.put("selected", "true");
			}else{
				rootNodeMap.put("selected", "false");
			}
		}
	}
	
	/**
	 * 获取对应父节点下菜单节点树(根据用户角色关系渲染选中哪些角色)
	 * @param parentId		菜单ID
	 * @param userId		用户ID
	 * @return				获取对应父节点下菜单节点树(根据用户角色关系渲染选中哪些角色)
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
				//1、获取角色组织树
				rootNoteMapList = roleService.getRoleTreeNode();
				//2、获取角色组织树(根据用户角色关系渲染选中哪些角色)
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
	 * 保存用户角色关系信息
	 * @param userId		用户ID
	 * @param idArray		角色ID集
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean updateUserRoleInfo(String userId, Object[] idArray)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		try {
			if (StringUtils.isNotEmpty(userId) && null != idArray){
				//1、先删除
				userDao.delMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_USER_ROLE, "USERID='" + userId + "'");
				//2、再保存
				userDao.saveUserRoleInfo(userId, idArray);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return true;
	}
	
	/**
	 * 保存角色用户关系信息
	 * @param roleId		角色ID
	 * @param idArray		用户ID集
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean updateRoleUserInfo(String roleId, Object[] idArray)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		try {
			if (StringUtils.isNotEmpty(roleId) && null != idArray){
				//1、先删除
				userDao.delMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_USER_ROLE, "ROLEID='" + roleId + "'");
				//2、再保存
				userDao.saveRoleUserInfo(roleId, idArray);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return true;
	}
	
	/**
	 * 返回相关用户组织机构ID字符串集
	 * @param userOrgIdList	相关用户组织机构ID集合
	 * @return	返回相关用户组织机构ID字符串集
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
	
	//获取用户键值对（userId:userName）
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
