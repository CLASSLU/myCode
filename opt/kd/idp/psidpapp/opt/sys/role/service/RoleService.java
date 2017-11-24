package kd.idp.psidpapp.opt.sys.role.service;

import java.util.List;
import java.util.Map;

import kd.idp.psidpapp.opt.sys.constant.DataSourceName;
import kd.idp.psidpapp.opt.sys.constant.FieldName;
import kd.idp.psidpapp.opt.sys.constant.TableName;
import kd.idp.psidpapp.opt.sys.flex.FlexUtil;
import kd.idp.psidpapp.opt.sys.role.dao.RoleDao;
import kd.idp.psidpapp.opt.sys.serial.service.SerialService;
import kd.idp.psidpapp.opt.sys.util.DateUtil;

import org.apache.commons.lang.StringUtils;

public class RoleService {

	private RoleDao roleDao;

	public RoleDao getRoleDao() {
		return roleDao;
	}

	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}
	
	/**
	 * 获取一条角色信息
	 * @param roleId		角色ID
	 * @return				返回一条角色信息
	 * @throws Exception
	 */
	public List<Map<String, Object>> findRoleList(String roleId) throws Exception{
		List<Map<String, Object>> roleMapList = null;
		try {
			if (StringUtils.isNotEmpty(roleId)){
				roleMapList = roleDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_ROLE, "DATASTATUSID='1' AND ID='" + roleId + "' ORDER BY SORT ASC");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return roleMapList;
	}
	
	/**
	 * 获取角色树
	 * @return				获取角色节点树
	 * @throws Exception
	 */
	public List<Map<String, Object>> getRoleTreeNode()throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		List<Map<String, Object>> rootNoteMapList = null;
		try{
			rootNoteMapList = roleDao.getRoleTreeNode();
			return rootNoteMapList;
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 增加角色
	 * @param  roleMap		角色信息
	 * @return				返回新建角色信息
	 * @throws Exception
	 */
	public Map<String, Object> createRole(Map<String, Object> roleMap)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		String id = "";
		int dataStatusId = 1;
		String createUserId = String.valueOf(userMap.get("ID"));
		String createTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
		try {
			if (null != roleMap){
				//生成唯一主键ID
				id = SerialService.getSerialIdByDate("");
				//新增数据
				roleMap.put("ID", id);
				roleMap.put("DATASTATUSID", dataStatusId);
				roleMap.put("CREATEUSERID", createUserId);
				roleMap.put("CREATETIME", createTime);
				roleDao.addMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_ROLE, FieldName.FIELD_SYS_ROLE, roleMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return roleMap;
	}
	
	/**
	 * 更新角色信息
	 * @param  roleMap		前端角色信息
	 * @return				更新角色信息
	 * @throws Exception
	 */
	public Boolean updateRole(Map<String,Object> roleMap)throws Exception{
		Boolean result = false;
		String updateUserId = "admin";
		String updateTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
		try {
			if (null != roleMap && null != roleMap.get("ID") && StringUtils.isNotEmpty(String.valueOf(roleMap.get("ID")))){
				String id = String.valueOf(roleMap.get("ID"));
				roleMap.put("UPDATEUSERID", updateUserId);
				roleMap.put("UPDATETIME", updateTime);
				result = roleDao.updateMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_ROLE, FieldName.FIELD_SYS_ROLE, "ID='" + id + "'", roleMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	/**
	 * 删除角色信息
	 * @param ids			角色ID集
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean delRoleList(String ids) throws Exception{
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
					result = roleDao.delMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_ROLE, sqlConditions);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
}
