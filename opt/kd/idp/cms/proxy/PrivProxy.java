package kd.idp.cms.proxy;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kd.idp.cms.bean.priv.OrgBean;
import kd.idp.cms.bean.priv.PrivAttrBean;
import kd.idp.cms.bean.priv.PrivBean;
import kd.idp.cms.bean.priv.RoleBean;
import kd.idp.cms.bean.priv.UserBean;
import kd.idp.cms.client.UserPrivClient;
import kd.idp.cms.dao.OrgDao;
import kd.idp.cms.dao.PrivDao;
import kd.idp.cms.dao.RoleDao;
import kd.idp.cms.dao.UserDao;
import kd.idp.common.config.WebConfigUtil;
import kd.idp.common.flex.FlexConst;
import kd.idp.common.flex.FlexResult;
import kd.idp.common.flex.FlexSessionInvalidException;
import kd.idp.common.flex.FlexUtil;
import kd.idp.index.register.RegisterProxy;
import kd.idp.security.password.BeanMd5;

import com.spring.ServiceManager;

public class PrivProxy {
	
	private UserBean currentUser = null;
	
	public PrivProxy() {
		currentUser = FlexUtil.getFlexCurrentUser();
//		System.out.println("��ǰ��¼�û�: "+((currentUser != null)?currentUser.getUserDisplayName():"δ��¼"));
	}

	/**
	 * �ж��û��Ƿ����
	 * @param userName
	 * @return
	 */
	public boolean checkUserExist(String userName){
//		System.out.println("userName : "+userName);
		UserDao userdao = ServiceManager.getUserDao();
		return userdao.isUserExsit(userName);
	}
	
	/**
	 * �û���¼
	 * @param userName
	 * @param pwd
	 * @return
	 */
	public FlexResult userLogin(String userName,String pwd){
		FlexResult result = new FlexResult();
		try {
			UserDao userdao = ServiceManager.getUserDao();
			UserBean user = userdao.getUserFromName(userName);
			if(user != null && user.getUserPwd().equals(BeanMd5.MD5HexEncode(pwd))){
				FlexUtil.setFlexCurrentUser(user);
				result.setStatus(FlexConst.RESULT_OK);
				result.setMessage("�û���¼�ɹ�!");
				result.setResultObject(user);
			}else{
				result.setStatus(FlexConst.RESULT_FAIL);
				result.setMessage("�û��������������,�����µ�¼!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}
	
	/**
	 * ע���û�
	 * @param user
	 * @return
	 */
	public boolean regUser(UserBean user){
		try {
			UserDao userdao = ServiceManager.getUserDao();
			boolean flag = userdao.addNewUser(user,false);
//			System.out.println(flag);
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * �����û�
	 * @param opType
	 * @param user
	 * @return
	 */
	public FlexResult operateUser(String opType,UserBean user){
		FlexResult result = new FlexResult();
		try {
			if(user != null){
				UserDao userdao = ServiceManager.getUserDao();
				if("mod".equals(opType)){
					//userdao.modifyUser(user);
				}else if("del".equals(opType)){
					userdao.deleteUser(user);
				}else if("add".equals(opType)){
					userdao.addNewUser(user,true);
				}
				result.setStatus(FlexConst.RESULT_OK);
				result.setMessage("�����ɹ�!");
			}else{
				result.setStatus(FlexConst.RESULT_FAIL);
				result.setMessage("�����û�Ϊ��!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}
	
	/**
	 * ����ɾ���û�
	 * @param users
	 * @return
	 */
	public FlexResult deleteUsers(List<UserBean> users){
		FlexResult result = new FlexResult();
		try {
			UserDao userdao = ServiceManager.getUserDao();
			result.setStatus(FlexConst.RESULT_OK);
			result.setMessage("ɾ���ɹ�!��ɾ��"+userdao.deleteUsers(users)+"���û���Ϣ");
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * ��ҳ+�������� ͨ����֯��������û�
	 * @param orgId
	 * @param filtersql
	 * @return
	 * @throws FlexSessionInvalidException 
	 */
	public List<UserBean> getUsersFromOrg(String orgId,String filtersql) throws FlexSessionInvalidException{ 
		if(FlexUtil.checkSession){
			if(currentUser == null){
				throw new FlexSessionInvalidException();
			}
		}
		List<UserBean> userList = new ArrayList<UserBean>();
		try {
			UserDao userdao = ServiceManager.getUserDao();
//			long t1 = System.currentTimeMillis();
			userList = userdao.getUsersFromOrg(orgId, filtersql,currentUser);
//			System.out.println("---1---" + (System.currentTimeMillis() - t1));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList;
	}
	
	/**
	 * δ������֯���� �û�
	 * @param filtersql
	 * @return
	 */
	public List<UserBean> getUsersWithoutOrg(String filtersql){
		UserDao userdao = ServiceManager.getUserDao();
		return userdao.getUsersWithoutOrg(filtersql);
	}
	
	/**
	 * δͨ����� �û�
	 * @param filtersql
	 * @return
	 */
	public List<UserBean> getUsersWithoutAudit(String filtersql){
		UserDao userdao = ServiceManager.getUserDao();
		return userdao.getUsersWithoutAudit(filtersql);
	}
	
	
	/**
	 * ��ý�ɫ�������û�
	 * @param userId
	 * @return
	 */
	public List<UserBean> getUsersFromRole(String roleId){
		try {
			UserDao userdao = ServiceManager.getUserDao();
			return userdao.getUsersFromRole(roleId);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<UserBean>();
		}
	}
	
	
	/**
	 * �û��� ��ѡ
	 * @param nodeId
	 * @param roleId
	 * @return
	 */
	public String getUserTreeNode(String nodeId,String roleId){
		StringBuffer sb = new StringBuffer();
		try {
			sb.append(getOrgTreeNodes(nodeId,roleId,""));
			UserDao userdao = ServiceManager.getUserDao();
			List<UserBean> userlist = userdao.getUsersFromOrgRelRole(nodeId, roleId);
			sb.append(getUserCheckTreeNode(nodeId, userlist));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	
	/**
	 * ���� �û� �� ��ɫ���� ��Ϣ
	 * @param userId
	 * @param rolelist
	 * @return
	 */
	public FlexResult saveUserRelRoles(String userId,ArrayList<RoleBean> rolelist){
		FlexResult result = new FlexResult();
		try {
			UserDao userdao = ServiceManager.getUserDao();
			userdao.saveUserRelRoles(userId, rolelist);
			result.setStatus(FlexConst.RESULT_OK);
			result.setMessage("�����û��Ľ�ɫ�����ɹ�!");
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 * ��ҳ+�������� ͨ����֯������ý�ɫ
	 * @param orgId
	 * @param filtersql
	 * @return
	 * @throws FlexSessionInvalidException 
	 */
	public List<RoleBean> getRolesFromOrg(String orgId,String filtersql) throws FlexSessionInvalidException{ 
		if(FlexUtil.checkSession){
			if(currentUser == null){
				throw new FlexSessionInvalidException();
			}
		}
		List<RoleBean> roleGrid = new ArrayList<RoleBean>();
		try {
			RoleDao roledao = ServiceManager.getRoleDao();
//			long t1 = System.currentTimeMillis();
			roleGrid = roledao.getRolesFromOrg(orgId, filtersql,currentUser);
//			System.out.println("---1---" + (System.currentTimeMillis() - t1));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return roleGrid;
	}
	
	

	/**
	 * ��ɫ�� ��ѡ
	 * @param nodeId
	 * @param userId
	 * @return
	 */
	public String getRoleTreeNode(String nodeId,String userId){
		StringBuffer sb = new StringBuffer();
		try {
			sb.append(getOrgTreeNodes(nodeId,"",userId));
			RoleDao roledao = ServiceManager.getRoleDao();
			List<RoleBean> rolelist = roledao.getRolesFromOrgRelUser(nodeId, userId);
			sb.append(getRoleCheckTreeNode(nodeId, rolelist));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	/**
	 * ����û������Ľ�ɫ
	 * @param userId
	 * @return
	 */
	public List<RoleBean> getRolesFromUser(String userId){
		try {
			RoleDao roledao = ServiceManager.getRoleDao();
			return roledao.getRolesFromUser(userId);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<RoleBean>();
		}
		
	}
	
	
	/**
	 * ������ɫ
	 * @param opType
	 * @param roles
	 * @return
	 */
	public boolean operateRoles(String opType,List<RoleBean> roles){
		try {
			if(roles != null){
				RoleDao roledao = ServiceManager.getRoleDao();
				if("add".equals(opType)){
					roledao.insertRoles(roles);
				}else if("mod".equals(opType)){
					roledao.modifyRoles(roles);
				}else if("del".equals(opType)){
					roledao.deleteRoles(roles);
				}
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * ���� ��ɫ �� �û����� ��Ϣ
	 * @param roleId
	 * @param userlist
	 * @return
	 */
	public FlexResult saveRoleRelUsers(String roleId,ArrayList<UserBean> userlist){
		FlexResult result = new FlexResult();
		try {
			RoleDao roledao = ServiceManager.getRoleDao();
			roledao.saveRoleRelUsers(roleId, userlist);
			result.setStatus(FlexConst.RESULT_OK);
			result.setMessage("�����ɫ���û������ɹ�!");
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage(e.getMessage());
			result.setStatus(FlexConst.RESULT_FAIL);
		}
		return result;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	/**
	 * �����֯������ (---------------------------------------------���Ż�---------------)
	 * �첽
	 * @param nodeId
	 * @param roleId
	 * @param userId
	 * @return
	 * @throws FlexSessionInvalidException 
	 */
	public String getOrgTreeNodes(String nodeId,String roleId,String userId) throws FlexSessionInvalidException{
		List<String> privOrgIDList = null;
		if(FlexUtil.checkSession){
			if(currentUser == null){
				throw new FlexSessionInvalidException();
			}
			if(nodeId != null && nodeId.equals(WebConfigUtil.getOrgPrivNode())){
				// ���ڵ�
				privOrgIDList = new ArrayList<String>();
				List<PrivAttrBean> orgattrs = ServiceManager.getPrivDao().getUserPrivAttr(currentUser.getUserId(), WebConfigUtil.getOrgPrivNode());
				for(int a = 0;a<orgattrs.size();a++){
					privOrgIDList.add(orgattrs.get(a).getPrivAttrValue());
				}
			}
		}
		StringBuffer sb = new StringBuffer();
		try {
			OrgDao orgdao = ServiceManager.getOrgDao();
			UserDao userdao = ServiceManager.getUserDao();
			RoleDao roledao = ServiceManager.getRoleDao();
			if(nodeId == null || "".equals(nodeId)){
				// ���ڵ�
				nodeId = WebConfigUtil.getOrgTreeRootId();
			}
			List<OrgBean> selectedOrgList = null; 
			if(roleId != null && !"".equals(roleId)){
				//�û���
				selectedOrgList = orgdao.getChildrenOrgRelUser(nodeId, roleId);
			}else if(userId != null && !"".equals(userId)){
				//��ɫ��
				selectedOrgList = orgdao.getChildrenOrgRelRole(nodeId, userId);
			}
			List<OrgBean> orglist = orgdao.getChildrenOrgInfo(nodeId,true,privOrgIDList);
			if(orglist != null && orglist.size()>0){
				for(int i=0;i<orglist.size();i++){
					OrgBean o = orglist.get(i);
					sb.append("<node nodeId='" + o.getOrgId() + "' label='"
							+ o.getOrgName() + "' parentId='" + o.getForgId()
							+ "' " + "order='" + o.getOrder() + "' sortName='"
							+ o.getSortName() + "' " + "trueName='"
							+ o.getTrueName() + "' type='" + o.getType()
							+ "' location='" + o.getLocation() + "' hasequ='"
							+ o.getHasEqu() + "'");
					if(orgdao.getChildrenOrgInfo(o.getOrgId(),true,null).size() > 0){
						sb.append(" isBranch='true' ");
					}else{
						//��ɫ
						if(userId != null && !"".equals(userId)){
							if(roledao.getRolesFromCurrentOrg(o.getOrgId()).size() > 0){
								sb.append(" isBranch='true' ");
							}else{
								sb.append(" isBranch='false' ");
							}
						}
						//�û�
						else if(roleId != null && !"".equals(roleId)){
							if(userdao.getUsersFromCurrentOrg(o.getOrgId()).size() > 0){
								sb.append(" isBranch='true' ");
							}else{
								sb.append(" isBranch='false' ");
							}
						}
						else{
							sb.append(" isBranch='false' ");
						}
					}
					//��ѡ״̬
					if(selectedOrgList!=null && selectedOrgList.size()>0){
						boolean selected = false;
						for(int j=0;j<selectedOrgList.size();j++){
							if(selectedOrgList.get(j).getOrgId().equals(o.getOrgId())){
								sb.append("selected='1' ");
								selected = true;
								break;
							}
						}
						if(!selected){
							sb.append("selected='0' ");
						}
					}
					sb.append(" />");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(sb.toString());
		return sb.toString();
	}
	
	/**
	 * �޸�,����,ɾ����֯����,���� ��֯����ID 
	 * @param org
	 * @return
	 */
	public String operateOrg(String type,OrgBean org){
		String status=null;
		try {
			
			 
			if(org == null){
				return null;
			}
			OrgDao orgdao = ServiceManager.getOrgDao();
			if("add".equals(type)){
				status=orgdao.addNewOrg(org);
				
				
				//return status;
			}else if("del".equals(type)){
				status=orgdao.deleteOrg(org);
				//return status;
			}else{
				status=orgdao.modOrg(org);
				//return status;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		//������֯��������json�ļ� ���û�ע��ҳ��ʹ��
		RegisterProxy RegisterProxy=new RegisterProxy();
		RegisterProxy.generateOrgJson();
		return status;
	}
	
	/**
	 * �����֯������Ϣ
	 * @param orgId
	 * @return
	 */
	public OrgBean getOrgInfo(String orgId){
		List<OrgBean> orgList = ServiceManager.getOrgDao().getOrgInfo(orgId);
		if(orgList.size() > 0){
			return orgList.get(0);
		}else{
			return new OrgBean();
		}
	}
	/**
	 * �����֯������ϸ��Ϣ
	 * @param orgId
	 * @param orgType
	 * @return
	 */
	
	public OrgBean getOrgDetailInfo(String orgId,String orgType){
		OrgDao dao = ServiceManager.getOrgDao();
		List<OrgBean> orgList = dao.getOrgList(dao.getOrgSqlByType(orgId, orgType));
		if(orgList.size() > 0){
			return orgList.get(0);
		}else{
			return new OrgBean();
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * ���Ȩ���� (---------------------------------------------���Ż�---------------)
	 * �첽
	 * @param nodeId
	 * @param roleId
	 * @param userId
	 * @return
	 * @throws FlexSessionInvalidException 
	 */
	public String getPrivTreeNodes(String nodeId,String roleId,String userId) throws FlexSessionInvalidException{
		List<String> privOrgNameList = null;
		PrivDao privdao = ServiceManager.getPrivDao();
		if(FlexUtil.checkSession){
			if(currentUser == null){
				throw new FlexSessionInvalidException();
			}
			privOrgNameList = privdao.getOrgNamePrivList(nodeId, currentUser);
		}
		StringBuffer sb = new StringBuffer();
		try {
			if(nodeId == null || "".equals(nodeId)){
				// ���ڵ�
				nodeId = WebConfigUtil.getPrivTreeRootId();
			}
			List<PrivBean> selectedPrivList = null; 
			if(roleId != null && !"".equals(roleId)){
				//������ɫȨ����
				selectedPrivList = privdao.getChildrenPrivRelRole(nodeId, roleId,true);
			}else if(userId != null && !"".equals(userId)){
				//�����û�Ȩ����
				selectedPrivList = privdao.getChildrenPrivRelUser(nodeId, userId,true);
			}
			List<PrivBean> privlist = privdao.getChildrenPrivInfo(nodeId,true,privOrgNameList);
//			if(nodeId.equals(WebConfigUtil.getPrivTreeRootId()) && "admin".equals(currentUser.getUserName())){
//				// ���ڵ� �����ǳ�������Ա��½ admin
//				privlist.add(privdao.getPrivInfo("org_root"));
//			}
			if(privlist != null && privlist.size()>0){
				for(int i=0;i<privlist.size();i++){
					PrivBean p = privlist.get(i);
					sb.append("<node nodeId='" + p.getPrivId() + "' label='"
							+ p.getPrivName() + "' parentId='"
							+ p.getPrivParentId() + "' " + "order='"
							+ p.getPrivOrder() + "' type='" + p.getPrivType()
							+ "' level ='" + p.getPrivLevel() + "' group='"
							+ p.getPrivGroup() + "'");
					if (privdao.getChildrenPrivInfo(p.getPrivId(), true, null)
							.size() > 0) {
						sb.append(" isBranch='true' ");
					} else {
						sb.append(" isBranch='false' ");
					}
					//��ѡ״̬
					if(selectedPrivList!=null && selectedPrivList.size()>0){
						boolean selected = false;
						for(int j=0;j<selectedPrivList.size();j++){
							if(selectedPrivList.get(j).getPrivId().equals(p.getPrivId())){
								sb.append("selected='1' ");
								selected = true;
								break;
							}
						}
						if(!selected){
							sb.append("selected='0' ");
						}
					}
					sb.append(" />");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	/**
	 * ͨ�� ��ID,���Ȩ���б� 
	 * @param nodeId
	 * @return
	 */
	public List<PrivBean> getPrivsFromParent(String nodeId){
		try {
			PrivDao privdao = ServiceManager.getPrivDao();
			return privdao.getChildrenPrivInfo(nodeId,true,null);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<PrivBean>();
		}
	}
	
	/**
	 * ���Ȩ��������Ϣ
	 * @param privId
	 * @return
	 */
	public List<PrivAttrBean> getPrivAttrs(String privId){
		try {
			PrivDao privdao = ServiceManager.getPrivDao();
			return privdao.getAttrsFromPriv(privId);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<PrivAttrBean>();
		}
		
	}
	
	/**
	 * ����Ȩ��
	 * @param type
	 * @param priv
	 * @return
	 */
	public String operatePriv(String type,PrivBean priv){
		if(priv == null){
			return null;
		}
		try {
			PrivDao privdao = ServiceManager.getPrivDao();
			if("add".equals(type)){
				return privdao.addNewPriv(priv);
			}else if("del".equals(type)){
				return privdao.deletePriv(priv);
			}else{
				return privdao.modPriv(priv);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * ����Ȩ������
	 * @param type
	 * @param attr
	 * @return
	 */
	public String operatePrivAttr(String type,PrivAttrBean attr){
		if(attr == null){
			return null;
		}
		PrivDao privdao = ServiceManager.getPrivDao();
		if("add".equals(type)){
			return privdao.addNewPrivAttr(attr);
		}else if("del".equals(type)){
			return privdao.deletePrivAttr(attr);
		}else{
			return privdao.modPrivAttr(attr);
		}
	}
	
	/**
	 * ����ɾ�� Ȩ������
	 * @param attrName
	 * @return
	 */
	public int batchDeletePrivAttr(List<String> attrIds){
		PrivDao privdao = ServiceManager.getPrivDao();
		try {
			return privdao.deletePrivAttr(attrIds);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	
	/**
	 * ����Ȩ�������б�
	 * @param privId
	 * @param attrlist
	 * @return
	 */
	public int savePrivAttrs(String privId,List<PrivAttrBean> attrlist){
		if(privId != null){
			PrivDao privdao = ServiceManager.getPrivDao();
			return privdao.savePrivAttrs(privId, attrlist);
		}
		return 0;
	}
	
	
	/**
	 * ����û�Ȩ��
	 * @param nodeId
	 * @param attr
	 * @return
	 * @throws FlexSessionInvalidException 
	 */
	public String getUserPrivTreeNode(String nodeId,PrivAttrBean attr) throws FlexSessionInvalidException{
		return getGrantPrivTreeNode("user", nodeId, attr);
	}
	
	/**
	 * ��ý�ɫȨ��
	 * @param nodeId
	 * @param attr
	 * @return
	 * @throws FlexSessionInvalidException 
	 */
	public String getRolePrivTreeNode(String nodeId,PrivAttrBean attr) throws FlexSessionInvalidException{
		return getGrantPrivTreeNode("role", nodeId, attr);
	}
	
	/**
	 * �����Ȩ��֯����
	 * @param type
	 * @param nodeId
	 * @param attr
	 * @return
	 * @throws FlexSessionInvalidException 
	 */
	private String getGrantPrivTreeNode(String type,String nodeId,PrivAttrBean attr) throws FlexSessionInvalidException{
		List<String> privOrgIDList = null;
		if(FlexUtil.checkSession){
			if(currentUser == null){
				throw new FlexSessionInvalidException();
			}
			if(nodeId != null && nodeId.equals(WebConfigUtil.getOrgPrivNode())){
				// ���ڵ�
				privOrgIDList = new ArrayList<String>();
				List<PrivAttrBean> orgattrs = ServiceManager.getPrivDao().getUserPrivAttr(currentUser.getUserId(), WebConfigUtil.getOrgPrivNode());
				for(int a = 0;a<orgattrs.size();a++){
					privOrgIDList.add(orgattrs.get(a).getPrivAttrValue());
				}
			}
		}
		StringBuffer sb = new StringBuffer();
		try {
			if(nodeId == null || "".equals(nodeId)){
				// ���ڵ�
				nodeId = WebConfigUtil.getOrgTreeRootId();
			}
			PrivDao privdao = ServiceManager.getPrivDao();
			OrgDao orgdao = ServiceManager.getOrgDao();
			UserDao userdao = ServiceManager.getUserDao();
			RoleDao roledao = ServiceManager.getRoleDao();
			List<OrgBean> selectedOrgList = null;
			if("user".equals(type)){
				selectedOrgList = privdao.getChildrenOrgWithUser(nodeId, attr.getPrivAttrId());
			}else{
				selectedOrgList = privdao.getChildrenOrgWithRole(nodeId, attr.getPrivAttrId());
			}
			List<OrgBean> orglist = orgdao.getChildrenOrgInfo(nodeId,true,privOrgIDList);
			if(orglist != null && orglist.size()>0){
				for(int i=0;i<orglist.size();i++){
					OrgBean o = orglist.get(i);
					sb.append("<node nodeId='" + o.getOrgId() + "' label='"
							+ o.getOrgName() + "' parentId='" + o.getForgId()
							+ "' " + "order='" + o.getOrder() + "' sortName='"
							+ o.getSortName() + "' " + "trueName='"
							+ o.getTrueName() + "' type='" + o.getType()
							+ "' location='" + o.getLocation() + "'");
					if(orgdao.getChildrenOrgInfo(o.getOrgId(),true,null).size() > 0){
						sb.append(" isBranch='true' ");
					}else{
						//��ɫ
						if("role".equals(type)){
							if(roledao.getRolesFromCurrentOrg(o.getOrgId()).size() > 0){
								sb.append(" isBranch='true' ");
							}else{
								sb.append(" isBranch='false' ");
							}
						}
						//�û�
						else if("user".equals(type)){
							if(userdao.getUsersFromCurrentOrg(o.getOrgId()).size() > 0){
								sb.append(" isBranch='true' ");
							}else{
								sb.append(" isBranch='false' ");
							}
						}
						else{
							sb.append(" isBranch='false' ");
						}
					}
					//��ѡ״̬
					if(selectedOrgList!=null && selectedOrgList.size()>0){
						boolean selected = false;
						for(int j=0;j<selectedOrgList.size();j++){
							if(selectedOrgList.get(j).getOrgId().equals(o.getOrgId())){
								sb.append("selected='1' ");
								selected = true;
								break;
							}
						}
						if(!selected){
							sb.append("selected='0' ");
						}
					}
					sb.append(" />");
				}
			}
			if("user".equals(type)){
				List<UserBean> userlist = userdao.getUsersFromOrgRelPrivAttr(nodeId, attr.getPrivAttrId());
				sb.append(getUserCheckTreeNode(nodeId, userlist));
			}else{
				List<RoleBean> rolelist = roledao.getRolesFromOrgRelPrivAttr(nodeId, attr.getPrivAttrId());
				sb.append(getRoleCheckTreeNode(nodeId, rolelist));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	

	/**
	 * ��ù���Ȩ�����ԵĽ�ɫ
	 * @param attrId
	 * @return
	 */
	public List<RoleBean> getRoleRelPrivAttr(String attrId){
		RoleDao roledao = ServiceManager.getRoleDao();
		return roledao.getRolesRelPrivAttr(attrId);
	}
	
	/**
	 * �����ɫ��Ȩ�޹�����ϵ
	 * @param attrList
	 * @param roleList
	 * @return
	 */
	public FlexResult saveRoleRelPrivAttr(String type,List<PrivAttrBean> attrList,List<RoleBean> roleList){
		FlexResult result = new FlexResult();
		try {
			PrivDao privdao = ServiceManager.getPrivDao();
			if("priv".equals(type)){
				privdao.saveRoleRelPrivAttr(attrList.get(0), roleList);
			}else{
				privdao.saveRoleRelPrivAttr(attrList, roleList.get(0));
			}
			result.setStatus(FlexConst.RESULT_OK);
			result.setMessage("Ϊ��ɫ��Ȩ�ɹ�!");
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}
	
	/**
	 * ��ù���Ȩ�����Ե��û�
	 * @param attrId
	 * @return
	 */
	public List<UserBean> getUserRelPrivAttr(String attrId){
		UserDao userdao = ServiceManager.getUserDao();
		return userdao.getUsersRelPrivAttr(attrId);
	}
	
	/**
	 * �����û���Ȩ�޹�����ϵ
	 * @param attrList
	 * @param userList
	 * @return
	 */
	public FlexResult saveUserRelPrivAttr(String type,List<PrivAttrBean> attrList,List<UserBean> userList){
		FlexResult result = new FlexResult();
		try {
			PrivDao privdao = ServiceManager.getPrivDao();
			if("priv".equals(type)){
				privdao.saveUserRelPrivAttr(attrList.get(0), userList);
			}
			else{
				privdao.saveUserRelPrivAttr(attrList, userList.get(0));
			}
			result.setStatus(FlexConst.RESULT_OK);
			result.setMessage("Ϊ�û���Ȩ�ɹ�!");
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * ���Ȩ�޵����Լ��û��Ĺ���״̬
	 * @param privId
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getPrivAttrRelUser(String privId,String userId){
		try {
			PrivDao privdao = ServiceManager.getPrivDao();
			List<PrivAttrBean> allAttrs = privdao.getAttrsFromPriv(privId);
			List<PrivAttrBean> selectedAttrs = privdao.getUserPrivAttr(userId, privId);
			return getSelectedPrivAttr(allAttrs, selectedAttrs);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Map<String,Object>>();
		}
	}
	
	
	/**
	 * ���Ȩ�޵����Լ���ɫ�Ĺ���״̬
	 * @param privId
	 * @param roleId
	 * @return
	 */
	public List<Map<String, Object>> getPrivAttrRelRole(String privId,String roleId){
		try {
			PrivDao privdao = ServiceManager.getPrivDao();
			List<PrivAttrBean> allAttrs = privdao.getAttrsFromPriv(privId);
			List<PrivAttrBean> selectedAttrs = privdao.getRolePrivAttr(roleId, privId);
			return getSelectedPrivAttr(allAttrs, selectedAttrs);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Map<String,Object>>();
		}
	}
	

	/**
	 * ����Ȩ������
	 * @param sourcePrivId
	 * @param targetPrivId
	 * @return
	 */
	public FlexResult pastePrivAttrs(String sourcePrivId,String targetPrivId){
		FlexResult result = new FlexResult();
		try {
			PrivDao privdao = ServiceManager.getPrivDao();
			result.setStatus(FlexConst.RESULT_OK);
			result.setMessage("ִ�гɹ�,����"+privdao.pastePrivAttrs(sourcePrivId,targetPrivId)+" ������!");
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}
	
	/**
	 * ���Ȩ�޵����Լ�����״̬
	 * @param allAttrs
	 * @param selectedAttrs
	 * @return
	 */
	private List<Map<String, Object>> getSelectedPrivAttr(List<PrivAttrBean> allAttrs,List<PrivAttrBean> selectedAttrs){
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		for(int i=0;i<allAttrs.size();i++){
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("priv", allAttrs.get(i));
			item.put("selected", false);
			for(int j=0;j<selectedAttrs.size();j++){
				if(allAttrs.get(i).getPrivAttrId().equals(selectedAttrs.get(j).getPrivAttrId())){
					item.put("selected", true);
					break;
				}
			}
			result.add(item);
		}
		return result;
	}
	
	
	
	
	/**
	 * ����û���ѡ ���ڵ�
	 * @param nodeId
	 * @param userlist
	 * @return
	 */
	private String getUserCheckTreeNode(String nodeId,List<UserBean> userlist){
		StringBuffer sb = new StringBuffer();
		try {
			if(userlist != null && userlist.size()>0){
				for(int i=0;i<userlist.size();i++){
					UserBean user = userlist.get(i);
					sb.append("<node nodeId='" + user.getUserId() + "' label='"
							+ user.getUserDisplayName() + "' parentId='" + nodeId
							+ "' " + "order='" + user.getUserOrder() + "' isBranch='false' " +
							" nodetype='user' ");
					if(user.getBak() == null || "".equals(user.getBak()) ){
						sb.append("selected='0'");
					}else{
						sb.append("selected='1'");
					}
					sb.append(" />");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	/**
	 * ��ý�ɫ��ѡ ���ڵ�
	 * @param nodeId
	 * @param rolelist
	 * @return
	 */
	private String getRoleCheckTreeNode(String nodeId,List<RoleBean> rolelist){
		StringBuffer sb = new StringBuffer();
		try {
			if(rolelist != null && rolelist.size()>0){
				for(int i=0;i<rolelist.size();i++){
					RoleBean role = rolelist.get(i);
					sb.append("<node nodeId='" + role.getRoleId() + "' label='"
							+ role.getRoleName() + "' parentId='" + nodeId
							+ "' " + "order='" + role.getOrder() + "' isBranch='false' " +
							" nodetype='role' ");
					if(role.getRoleDesc() == null || "".equals(role.getRoleDesc()) ){
						sb.append("selected='0'");
					}else{
						sb.append("selected='1'");
					}
					sb.append(" />");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	 /**
	  * aviyy	2014-01-26	������������Ȩ�޷���	New��Edit��Delete��Publish
	  * ���Ӧ�������ԣ���ť��
	  * 
	  * @param nodeId
	  * @param rolelist
	  * @return
	  */
	 public List<Map<String, String>> getOperateStatus(String privId){
	    List<Map<String, String>> operate = new ArrayList<Map<String, String>>();
	    UserPrivClient userpriv = new UserPrivClient(currentUser.getUserId());
	    List<PrivAttrBean> attrs = userpriv.getPrivAttrs(privId);
	    for (int i = 0; i < attrs.size(); i++) {
	    if ("����".equals(attrs.get(i).getPrivAttrType())) {
	     Map<String, String> map = new HashMap<String, String>();
	     map.put("label", attrs.get(i).getPrivAttrName());
//	     map.put("menu", attrs.get(i).getPrivAttrValue());
	     operate.add(map);
	    }
	   }
	    return operate;
	 }
	
	
}
