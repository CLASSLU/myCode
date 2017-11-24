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
	 * [��֯����]��ȡӦ����֯�������ü�ֵ�ԣ�ID:ORGMAP��
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getOrgConfigMap(String sqlCondition)throws Exception{
		OrgService orgService = (OrgService)SpringBeanFactory.getInstance().getBean("orgService");
		return orgService.getOrgConfigMap(sqlCondition);
	}

	/**
	 * [��֯����]��ȡ��Ӧ����֯����ID�ڵ���
	 * @param parentId		��֯����ID
	 * @return				[��֯����]��ȡ��Ӧ����֯����ID�ڵ���
	 * @throws Exception
	 */
	public List<Map<String, Object>> getOrgTreeNode(String parentId)throws Exception{
		OrgService orgService = (OrgService)SpringBeanFactory.getInstance().getBean("orgService");
		return orgService.getOrgTreeNode(parentId);
	}
	
	/**
	 * [��֯����]������֯����
	 * @param  orgMap		������Ϣ
	 * @return				�����½���֯����
	 * @throws Exception
	 */
	public Map<String, Object> createOrg(Map<String, Object> orgMap)throws Exception{
		OrgService orgService = (OrgService)SpringBeanFactory.getInstance().getBean("orgService");
		return orgService.createOrg(orgMap);
	}
	
	/**
	 * [��֯����]������֯����
	 * @param  orgMap		ǰ����֯������Ϣ
	 * @return				���ز���������
	 * @throws Exception
	 */
	public Boolean updateOrg(Map<String,Object> orgMap)throws Exception{
		OrgService orgService = (OrgService)SpringBeanFactory.getInstance().getBean("orgService");
		return orgService.updateOrg(orgMap);
	}
	
	/**
	 * [��֯����]ɾ����֯����
	 * @param ids			��֯����ID��
	 * @return				���ز���������
	 * @throws Exception
	 */
	public Boolean delOrgList(String ids) throws Exception{
		OrgService orgService = (OrgService)SpringBeanFactory.getInstance().getBean("orgService");
		return orgService.delOrgList(ids);
	}
	
	/**
	 * [�û�]���ݵ�¼���������ȡ�û���Ϣ
	 * @param userName	��¼��
	 * @param password	��¼����
	 * @return			��ȡ�û���Ϣ
	 * @throws Exception
	 */
	public Map<String,Object> findUser(String userName, String password) throws Exception{
		UserService userService = (UserService)SpringBeanFactory.getInstance().getBean("userService");
		return userService.findUser(userName, password);
	}
	
	/**
	 * [�û�]���ض�Ӧ���������������������û�
	 * @param orgId		��֯����ID
	 * @param sqlCondition		�û���������
	 * @return			���ض�Ӧ���������������������û�
	 * @throws Exception
	 */
	public List<Map<String,Object>> findUserListByOrgId(String orgId, String sqlCondition)throws Exception{
		UserService userService = (UserService)SpringBeanFactory.getInstance().getBean("userService");
		return userService.findUserListByOrgId(orgId, sqlCondition);
	}
	
	/**
	 * [�û�]�����û���Ϣ
	 * @param  userInfoMap		�û���Ϣ
	 * @return				�����½��û���Ϣ
	 * @throws Exception
	 */
	public Map<String, Object> createUser(Map<String, Object> userInfoMap)throws Exception{
		UserService userService = (UserService)SpringBeanFactory.getInstance().getBean("userService");
		return userService.createUser(userInfoMap);
	}
	
	/**
	 * [�û�]�����û���Ϣ
	 * @param  userInfoMap		ǰ���û���Ϣ
	 * @return				���ز���������
	 * @throws Exception
	 */
	public Map<String,Object> updateUserInfo(Map<String,Object> userInfoMap)throws Exception{
		UserService userService = (UserService)SpringBeanFactory.getInstance().getBean("userService");
		return userService.updateUserInfo(userInfoMap);
	}
	
	/**
	 * [�û�]ɾ���û�
	 * @param ids			�û�ID��
	 * @return				���ز���������
	 * @throws Exception
	 */
	public Boolean delUserList(String ids) throws Exception{
		UserService userService = (UserService)SpringBeanFactory.getInstance().getBean("userService");
		return userService.delUserList(ids);
	}
	
	/**
	 * [�û�]��ȡ�����û���Ϣ
	 * @param  sqlCondition		�����û���������
	 * @return					�����û���Ϣ
	 * @throws Exception
	 */
	public Map<String,Object> findUserInfo(String sqlCondition)throws Exception{
		UserService userService = (UserService)SpringBeanFactory.getInstance().getBean("userService");
		return userService.findUserInfo(sqlCondition);
	}
	
	/**
	 * [�û�]�����ɫ�û���ϵ��Ϣ
	 * @param roleId		��ɫID
	 * @param idArray		�û�ID��
	 * @return				���ز���������
	 * @throws Exception
	 */
	public Boolean updateRoleUserInfo(String roleId, Object[] idArray)throws Exception{
		UserService userService = (UserService)SpringBeanFactory.getInstance().getBean("userService");
		return userService.updateRoleUserInfo(roleId, idArray);
	}
	/**
	 * [�û�]�����û���ɫ��ϵ��Ϣ
	 * @param userId		�û�ID
	 * @param idArray		��ɫID��
	 * @return				���ز���������
	 * @throws Exception
	 */
	public Boolean updateUserRoleInfo(String userId, Object[] idArray)throws Exception{
		UserService userService = (UserService)SpringBeanFactory.getInstance().getBean("userService");
		return userService.updateUserRoleInfo(userId, idArray);
	}
	/**
	 * [�û�]��ȡ��Ӧ���ڵ��²˵��ڵ���(�����û���ɫ��ϵ��Ⱦѡ����Щ��ɫ)
	 * @param parentId		�˵�ID
	 * @param userId		�û�ID
	 * @return				��ȡ��Ӧ���ڵ��²˵��ڵ���(�����û���ɫ��ϵ��Ⱦѡ����Щ��ɫ)
	 * @throws Exception
	 */
	public List<Map<String, Object>> getRoleTreeNodeByUserId(String userId)throws Exception{
		UserService userService = (UserService)SpringBeanFactory.getInstance().getBean("userService");
		return userService.getRoleTreeNodeByUserId(userId);
	}
	
	/**
	 * [�˵�]��ȡ��Ӧ���ڵ��²˵��ڵ���
	 * @param parentId		�˵�ID
	 * @return				��ȡ��Ӧ���ڵ��²˵��ڵ���
	 * @throws Exception
	 */
	public List<Map<String, Object>> getMenuTreeNode(String parentId)throws Exception{
		MenuService menuService = (MenuService)SpringBeanFactory.getInstance().getBean("menuService");
		return menuService.getMenuTreeNode(parentId);
	}
	/**
	 * [�˵�]��ȡ��Ӧ���ڵ��²˵��ڵ���(���ݽ�ɫ�˵���ϵ��Ⱦ�Ƿ�ѡ��)
	 * @param parentId		�˵�ID
	 * @param roleId		��ɫID
	 * @return				��ȡ��Ӧ���ڵ��²˵��ڵ���(���ݽ�ɫ�˵���ϵ��Ⱦ�Ƿ�ѡ��)
	 * @throws Exception
	 */
	public List<Map<String, Object>> getMenuTreeNodeByRoleId(String parentId, String roleId)throws Exception{
		MenuService menuService = (MenuService)SpringBeanFactory.getInstance().getBean("menuService");
		return menuService.getMenuTreeNodeByRoleId(parentId, roleId);
	}
	/**
	 * [�˵�]�����˵�
	 * @param  menuMap		�˵���Ϣ
	 * @return				�����½��˵�
	 * @throws Exception
	 */
	public Map<String, Object> createMenu(Map<String, Object> menuMap)throws Exception{
		MenuService menuService = (MenuService)SpringBeanFactory.getInstance().getBean("menuService");
		return menuService.createMenu(menuMap);
	}
	
	/**
	 * [�˵�]���²˵�
	 * @param  menuMap		ǰ�˲˵���Ϣ
	 * @return				���ز���������
	 * @throws Exception
	 */
	public Boolean updateMenu(Map<String,Object> menuMap)throws Exception{
		MenuService menuService = (MenuService)SpringBeanFactory.getInstance().getBean("menuService");
		return menuService.updateMenu(menuMap);
	}
	
	/**
	 * [�˵�]ɾ���˵�
	 * @param ids			�˵�ID��
	 * @return				���ز���������
	 * @throws Exception
	 */
	public Boolean delMenuList(String ids) throws Exception{
		MenuService menuService = (MenuService)SpringBeanFactory.getInstance().getBean("menuService");
		return menuService.delMenuList(ids);
	}
	
	/**
	 * [�˵�]�����ɫ�˵���ϵ��Ϣ
	 * @param roleId		��ɫID
	 * @param idArray		�˵�ID��
	 * @return				���ز���������
	 * @throws Exception
	 */
	public Boolean updateRoleMenuInfo(String roleId, Object[] idArray)throws Exception{
		MenuService menuService = (MenuService)SpringBeanFactory.getInstance().getBean("menuService");
		return menuService.updateRoleMenuInfo(roleId, idArray);
	}
	
	/**
	 * [��ɫ]��ȡ��ɫ��
	 * @return				��ȡ��ɫ�ڵ���
	 * @throws Exception
	 */
	public List<Map<String, Object>> getRoleTreeNode()throws Exception{
		RoleService roleService = (RoleService)SpringBeanFactory.getInstance().getBean("roleService");
		return roleService.getRoleTreeNode();
	}
	
	/**
	 * [��ɫ]���ӽ�ɫ
	 * @param  roleMap		��ɫ��Ϣ
	 * @return				�����½���ɫ��Ϣ
	 * @throws Exception
	 */
	public Map<String, Object> createRole(Map<String, Object> roleMap)throws Exception{
		RoleService roleService = (RoleService)SpringBeanFactory.getInstance().getBean("roleService");
		return roleService.createRole(roleMap);
	}
	
	/**
	 * [��ɫ]���½�ɫ��Ϣ
	 * @param  roleMap		ǰ�˽�ɫ��Ϣ
	 * @return				���½�ɫ��Ϣ
	 * @throws Exception
	 */
	public Boolean updateRole(Map<String,Object> roleMap)throws Exception{
		RoleService roleService = (RoleService)SpringBeanFactory.getInstance().getBean("roleService");
		return roleService.updateRole(roleMap);
	}
	
	/**
	 * [��ɫ]ɾ����ɫ��Ϣ
	 * @param ids			��ɫID��
	 * @return				���ز���������
	 * @throws Exception
	 */
	public Boolean delRoleList(String ids) throws Exception{
		RoleService roleService = (RoleService)SpringBeanFactory.getInstance().getBean("roleService");
		return roleService.delRoleList(ids);
	}
	
	/**
	 * [�û���֯����]��ȡ�û���֯��
	 * @param parentId		��֯����ID
	 * @return				��ȡ�û���֯��
	 * @throws Exception
	 */
	public List<Map<String, Object>> getUserOrgTreeNode(String parentId)throws Exception{
		UserService userService = (UserService)SpringBeanFactory.getInstance().getBean("userService");
		return userService.getUserOrgTreeNode(parentId);
	}
	
	/**
	 * [�û���֯����]��ȡ�û���֯��(����ȡ��ɫ�û���Ϣ)
	 * @param parentId		��֯����ID
	 * @param roleId		��ɫID
	 * @return				��ȡ�û���֯��(����ȡ��ɫ�û���Ϣ)
	 * @throws Exception
	 */
	public List<Map<String, Object>> getUserOrgTreeNodeByRoleId(String parentId, String roleId)throws Exception{
		UserService userService = (UserService)SpringBeanFactory.getInstance().getBean("userService");
		return userService.getUserOrgTreeNodeByRoleId(parentId, roleId);
	}
}
