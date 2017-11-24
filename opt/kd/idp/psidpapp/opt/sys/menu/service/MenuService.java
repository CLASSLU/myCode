package kd.idp.psidpapp.opt.sys.menu.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


import kd.idp.psidpapp.opt.sys.constant.DataSourceName;
import kd.idp.psidpapp.opt.sys.constant.FieldName;
import kd.idp.psidpapp.opt.sys.constant.TableName;
import kd.idp.psidpapp.opt.sys.flex.FlexUtil;
import kd.idp.psidpapp.opt.sys.menu.dao.MenuDao;
import kd.idp.psidpapp.opt.sys.serial.service.SerialService;
import kd.idp.psidpapp.opt.sys.util.DateUtil;

public class MenuService {
	
	
	private MenuDao menuDao;
	
	public MenuDao getMenuDao() {
		return menuDao;
	}

	public void setMenuDao(MenuDao menuDao) {
		this.menuDao = menuDao;
	}


	
	/**
	 * 获取父节点下的所有一级孩子节点集
	 * @param parentId		父节点ID
	 * @return				父节点下的一级孩子节点集
	 * @throws Exception
	 */
	public List<Map<String, Object>> findMenuByParentId(String parentId) throws Exception{
		List<Map<String, Object>> menuMapList = null;
		try {
			if (StringUtils.isNotEmpty(parentId)){
				menuMapList = menuDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_MENU, "DATASTATUSID='1' AND PARENTID='" + parentId + "' ORDER BY SORT ASC");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return menuMapList;
	}
	
	/**
	 * 获取父节点下的有权限的一级孩子节点集
	 * @param parentId		父节点ID
	 * @return				父节点下的一级孩子节点集
	 * @throws Exception
	 */
	public List<Map<String, Object>> findMenuByParentIdByFlex(String parentId) throws Exception{
		List<Map<String, Object>> menuMapList = null;
		try {
			if (StringUtils.isNotEmpty(parentId)){
				Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
				if (null != userMap){
					String roleIds = String.valueOf(userMap.get("ROLEIDS"));
					if (StringUtils.isNotEmpty(roleIds) && !"null".equals(roleIds)){
						String []roleIdArray = roleIds.substring(1,roleIds.length()-1).split(",");
						roleIds = "";
						for (String roleId : roleIdArray){
							roleIds += "'" + roleId + "',";
						}
						roleIds = roleIds.substring(0,roleIds.length()-1);
						String sqlCondition = "DATASTATUSID='1' AND PARENTID='" + parentId + "' AND ID IN(SELECT MENUID FROM OPT.SYS_ROLE_MENU WHERE ROLEID IN(" + roleIds + ")) ORDER BY SORT ASC";
						menuMapList = menuDao.findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_MENU, sqlCondition);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return menuMapList;
	}
	
	/**
	 * 获取对应父节点下菜单节点树
	 * @param parentId		菜单ID
	 * @return				获取对应父节点下菜单节点树
	 * @throws Exception
	 */
	public List<Map<String, Object>> getMenuTreeNode(String parentId)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		List<Map<String, Object>> rootNoteMapList = null;
		try{
			if (StringUtils.isNotEmpty(parentId)){
				rootNoteMapList = menuDao.getMenuTreeNode(parentId);
			}
			return rootNoteMapList;
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 获取对应父节点下菜单节点树(根据角色菜单关系渲染是否选中)
	 * @param parentId		菜单ID
	 * @param roleId		角色ID
	 * @return				获取对应父节点下菜单节点树(根据角色菜单关系渲染是否选中)
	 * @throws Exception
	 */
	public List<Map<String, Object>> getMenuTreeNodeByRoleId(String parentId, String roleId)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		List<Map<String, Object>> rootNoteMapList = null;
		try{
			if (StringUtils.isNotEmpty(parentId) && StringUtils.isNotEmpty(roleId)){
				//1、获取角色菜单组织树
				rootNoteMapList = getMenuTreeNode(parentId);
				//2、获取角色菜单组织树(根据角色菜单关系渲染是否选中)
				List<Map<String, Object>> menuMapList = menuDao.findMenuListIdByRoleId(roleId);
				if (null != rootNoteMapList && 0 != rootNoteMapList.size() && null != menuMapList && 0 != menuMapList.size()){
					String menuIds = ",";
					for (Map<String, Object> tempMenuMap : menuMapList){
						menuIds += tempMenuMap.get("ID") + ",";
					}
					selectNodeByIds(rootNoteMapList, menuIds);
				}
			}
			return rootNoteMapList;
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 选中角色已绑定菜单节点
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
	 * 创建菜单
	 * @param  menuMap		菜单信息
	 * @return				返回新建菜单
	 * @throws Exception
	 */
	public Map<String, Object> createMenu(Map<String, Object> menuMap)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		String id = "";
		String createUserId = String.valueOf(userMap.get("ID"));
		String createTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
		try {
			if (null != menuMap){
				//生成唯一主键ID
				id = SerialService.getSerialIdByDate("");
				menuMap.put("ID", id);
				menuMap.put("CREATEUSERID", createUserId);
				menuMap.put("CREATETIME", createTime);
				menuDao.addMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_MENU, FieldName.FIELD_SYS_MENU, menuMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return menuMap;
	}
	
	/**
	 * 更新菜单
	 * @param  menuMap		前端菜单信息
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean updateMenu(Map<String,Object> menuMap)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		Boolean result = false;
		String updateUserId = String.valueOf(userMap.get("ID"));
		String updateTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
		try {
			if (null != menuMap && StringUtils.isNotEmpty(String.valueOf(menuMap.get("ID"))) && !"null".equalsIgnoreCase(String.valueOf(menuMap.get("ID")))){
				String id = String.valueOf(menuMap.get("ID"));
				menuMap.put("UPDATEUSERID", updateUserId);
				menuMap.put("UPDATETIME", updateTime);
				result = menuDao.updateMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_MENU, FieldName.FIELD_SYS_MENU, "ID='" + id + "'", menuMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	/**
	 * 删除菜单
	 * @param ids			菜单ID集
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean delMenuList(String ids) throws Exception{
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
					result = menuDao.delMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_MENU, sqlConditions);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	/**
	 * 保存角色菜单关系信息
	 * @param roleId		角色ID
	 * @param idArray		菜单ID集
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean updateRoleMenuInfo(String roleId, Object[] idArray)throws Exception{
		Map<String, Object> userMap = FlexUtil.getFlexCurrentUser();
		if (null == userMap){
			throw new Exception("seesion invalid");
		}
		try {
			if (StringUtils.isNotEmpty(roleId) && null != idArray){
				//1、先删除
				menuDao.delMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_ROLE_MENU, "ROLEID='" + roleId + "'");
				//2、再保存
				menuDao.saveRoleMenuInfo(roleId, idArray);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return true;
	}
}
