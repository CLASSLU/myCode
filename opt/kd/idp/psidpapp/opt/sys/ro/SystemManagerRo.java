package kd.idp.psidpapp.opt.sys.ro;

import java.util.List;
import java.util.Map;

import kd.idp.psidpapp.opt.sys.menu.service.MenuService;
import kd.idp.psidpapp.opt.sys.org.service.OrgService;
import kd.idp.psidpapp.opt.sys.role.service.RoleService;
import kd.idp.psidpapp.opt.sys.user.service.UserService;

import com.spring.SpringBeanFactory;

public class SystemManagerRo {
	
	
	/**
	 * [组织机构]获取应用组织机构配置键值对（ID:ORGMAP）
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getOrgConfigMap(String sqlCondition)throws Exception{
		OrgService orgService = (OrgService)SpringBeanFactory.getInstance().getBean("orgService");
		return orgService.getOrgConfigMap(sqlCondition);
	}

	/**
	 * [组织机构]获取对应父组织机构ID节点树
	 * @param parentId		组织机构ID
	 * @return				[组织机构]获取对应父组织机构ID节点树
	 * @throws Exception
	 */
	public List<Map<String, Object>> getOrgTreeNode(String parentId)throws Exception{
		OrgService orgService = (OrgService)SpringBeanFactory.getInstance().getBean("orgService");
		return orgService.getOrgTreeNode(parentId);
	}
	
	/**
	 * [组织机构]创建组织机构
	 * @param  orgMap		机构信息
	 * @return				返回新建组织机构
	 * @throws Exception
	 */
	public Map<String, Object> createOrg(Map<String, Object> orgMap)throws Exception{
		OrgService orgService = (OrgService)SpringBeanFactory.getInstance().getBean("orgService");
		return orgService.createOrg(orgMap);
	}
	
	/**
	 * [组织机构]更新组织机构
	 * @param  orgMap		前端组织机构信息
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean updateOrg(Map<String,Object> orgMap)throws Exception{
		OrgService orgService = (OrgService)SpringBeanFactory.getInstance().getBean("orgService");
		return orgService.updateOrg(orgMap);
	}
	
	/**
	 * [组织机构]删除组织机构
	 * @param ids			组织机构ID集
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean delOrgList(String ids) throws Exception{
		OrgService orgService = (OrgService)SpringBeanFactory.getInstance().getBean("orgService");
		return orgService.delOrgList(ids);
	}
	
	/**
	 * [用户]根据登录名和密码获取用户信息
	 * @param userName	登录名
	 * @param password	登录密码
	 * @return			获取用户信息
	 * @throws Exception
	 */
	public Map<String,Object> findUser(String userName, String password) throws Exception{
		UserService userService = (UserService)SpringBeanFactory.getInstance().getBean("userService");
		return userService.findUser(userName, password);
	}
	
	/**
	 * [用户]返回对应机构及下属机构下所有用户
	 * @param orgId		组织机构ID
	 * @param sqlCondition		用户过滤条件
	 * @return			返回对应机构及下属机构下所有用户
	 * @throws Exception
	 */
	public List<Map<String,Object>> findUserListByOrgId(String orgId, String sqlCondition)throws Exception{
		UserService userService = (UserService)SpringBeanFactory.getInstance().getBean("userService");
		return userService.findUserListByOrgId(orgId, sqlCondition);
	}
	
	/**
	 * [用户]创建用户信息
	 * @param  userInfoMap		用户信息
	 * @return				返回新建用户信息
	 * @throws Exception
	 */
	public Map<String, Object> createUser(Map<String, Object> userInfoMap)throws Exception{
		UserService userService = (UserService)SpringBeanFactory.getInstance().getBean("userService");
		return userService.createUser(userInfoMap);
	}
	
	/**
	 * [用户]更新用户信息
	 * @param  userInfoMap		前端用户信息
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Map<String,Object> updateUserInfo(Map<String,Object> userInfoMap)throws Exception{
		UserService userService = (UserService)SpringBeanFactory.getInstance().getBean("userService");
		return userService.updateUserInfo(userInfoMap);
	}
	
	/**
	 * [用户]删除用户
	 * @param ids			用户ID集
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean delUserList(String ids) throws Exception{
		UserService userService = (UserService)SpringBeanFactory.getInstance().getBean("userService");
		return userService.delUserList(ids);
	}
	
	/**
	 * [用户]获取单个用户信息
	 * @param  sqlCondition		单个用户过滤条件
	 * @return					返回用户信息
	 * @throws Exception
	 */
	public Map<String,Object> findUserInfo(String sqlCondition)throws Exception{
		UserService userService = (UserService)SpringBeanFactory.getInstance().getBean("userService");
		return userService.findUserInfo(sqlCondition);
	}
	
	/**
	 * [用户]保存角色用户关系信息
	 * @param roleId		角色ID
	 * @param idArray		用户ID集
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean updateRoleUserInfo(String roleId, Object[] idArray)throws Exception{
		UserService userService = (UserService)SpringBeanFactory.getInstance().getBean("userService");
		return userService.updateRoleUserInfo(roleId, idArray);
	}
	/**
	 * [用户]保存用户角色关系信息
	 * @param userId		用户ID
	 * @param idArray		角色ID集
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean updateUserRoleInfo(String userId, Object[] idArray)throws Exception{
		UserService userService = (UserService)SpringBeanFactory.getInstance().getBean("userService");
		return userService.updateUserRoleInfo(userId, idArray);
	}
	/**
	 * [用户]获取对应父节点下菜单节点树(根据用户角色关系渲染选中哪些角色)
	 * @param parentId		菜单ID
	 * @param userId		用户ID
	 * @return				获取对应父节点下菜单节点树(根据用户角色关系渲染选中哪些角色)
	 * @throws Exception
	 */
	public List<Map<String, Object>> getRoleTreeNodeByUserId(String userId)throws Exception{
		UserService userService = (UserService)SpringBeanFactory.getInstance().getBean("userService");
		return userService.getRoleTreeNodeByUserId(userId);
	}
	
	/**
	 * [菜单]获取对应父节点下菜单节点树
	 * @param parentId		菜单ID
	 * @return				获取对应父节点下菜单节点树
	 * @throws Exception
	 */
	public List<Map<String, Object>> getMenuTreeNode(String parentId)throws Exception{
		MenuService menuService = (MenuService)SpringBeanFactory.getInstance().getBean("menuService");
		return menuService.getMenuTreeNode(parentId);
	}
	/**
	 * [菜单]获取对应父节点下菜单节点树(根据角色菜单关系渲染是否选中)
	 * @param parentId		菜单ID
	 * @param roleId		角色ID
	 * @return				获取对应父节点下菜单节点树(根据角色菜单关系渲染是否选中)
	 * @throws Exception
	 */
	public List<Map<String, Object>> getMenuTreeNodeByRoleId(String parentId, String roleId)throws Exception{
		MenuService menuService = (MenuService)SpringBeanFactory.getInstance().getBean("menuService");
		return menuService.getMenuTreeNodeByRoleId(parentId, roleId);
	}
	/**
	 * [菜单]创建菜单
	 * @param  menuMap		菜单信息
	 * @return				返回新建菜单
	 * @throws Exception
	 */
	public Map<String, Object> createMenu(Map<String, Object> menuMap)throws Exception{
		MenuService menuService = (MenuService)SpringBeanFactory.getInstance().getBean("menuService");
		return menuService.createMenu(menuMap);
	}
	
	/**
	 * [菜单]更新菜单
	 * @param  menuMap		前端菜单信息
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean updateMenu(Map<String,Object> menuMap)throws Exception{
		MenuService menuService = (MenuService)SpringBeanFactory.getInstance().getBean("menuService");
		return menuService.updateMenu(menuMap);
	}
	
	/**
	 * [菜单]删除菜单
	 * @param ids			菜单ID集
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean delMenuList(String ids) throws Exception{
		MenuService menuService = (MenuService)SpringBeanFactory.getInstance().getBean("menuService");
		return menuService.delMenuList(ids);
	}
	
	/**
	 * [菜单]保存角色菜单关系信息
	 * @param roleId		角色ID
	 * @param idArray		菜单ID集
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean updateRoleMenuInfo(String roleId, Object[] idArray)throws Exception{
		MenuService menuService = (MenuService)SpringBeanFactory.getInstance().getBean("menuService");
		return menuService.updateRoleMenuInfo(roleId, idArray);
	}
	
	/**
	 * [角色]获取角色树
	 * @return				获取角色节点树
	 * @throws Exception
	 */
	public List<Map<String, Object>> getRoleTreeNode()throws Exception{
		RoleService roleService = (RoleService)SpringBeanFactory.getInstance().getBean("roleService");
		return roleService.getRoleTreeNode();
	}
	
	/**
	 * [角色]增加角色
	 * @param  roleMap		角色信息
	 * @return				返回新建角色信息
	 * @throws Exception
	 */
	public Map<String, Object> createRole(Map<String, Object> roleMap)throws Exception{
		RoleService roleService = (RoleService)SpringBeanFactory.getInstance().getBean("roleService");
		return roleService.createRole(roleMap);
	}
	
	/**
	 * [角色]更新角色信息
	 * @param  roleMap		前端角色信息
	 * @return				更新角色信息
	 * @throws Exception
	 */
	public Boolean updateRole(Map<String,Object> roleMap)throws Exception{
		RoleService roleService = (RoleService)SpringBeanFactory.getInstance().getBean("roleService");
		return roleService.updateRole(roleMap);
	}
	
	/**
	 * [角色]删除角色信息
	 * @param ids			角色ID集
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean delRoleList(String ids) throws Exception{
		RoleService roleService = (RoleService)SpringBeanFactory.getInstance().getBean("roleService");
		return roleService.delRoleList(ids);
	}
	
	/**
	 * [用户组织机构]获取用户组织树
	 * @param parentId		组织机构ID
	 * @return				获取用户组织树
	 * @throws Exception
	 */
	public List<Map<String, Object>> getUserOrgTreeNode(String parentId)throws Exception{
		UserService userService = (UserService)SpringBeanFactory.getInstance().getBean("userService");
		return userService.getUserOrgTreeNode(parentId);
	}
	
	/**
	 * [用户组织机构]获取用户组织树(并获取角色用户信息)
	 * @param parentId		组织机构ID
	 * @param roleId		角色ID
	 * @return				获取用户组织树(并获取角色用户信息)
	 * @throws Exception
	 */
	public List<Map<String, Object>> getUserOrgTreeNodeByRoleId(String parentId, String roleId)throws Exception{
		UserService userService = (UserService)SpringBeanFactory.getInstance().getBean("userService");
		return userService.getUserOrgTreeNodeByRoleId(parentId, roleId);
	}
}
