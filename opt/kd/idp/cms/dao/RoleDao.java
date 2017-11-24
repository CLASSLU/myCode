package kd.idp.cms.dao;

import java.util.ArrayList;
import java.util.List;

import kd.idp.cms.bean.priv.PrivAttrBean;
import kd.idp.cms.bean.priv.RoleBean;
import kd.idp.cms.bean.priv.UserBean;
import kd.idp.cms.mapper.priv.RoleRowMapper;
import kd.idp.common.CommonTools;
import kd.idp.common.config.WebConfigUtil;
import kd.idp.common.consts.TableConst;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spring.ServiceManager;
import com.spring.dbservice.DBTemplate;


@Transactional
public class RoleDao{

	
	/**
	 * ���� ��ɫ
	 * @param roles
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public boolean insertRoles(List<RoleBean> roles){
		try {
			String[] sqls = new String[roles.size()];
			for(int i=0;i<roles.size();i++){
				sqls[i] = getInsertRoleSql(roles.get(i));
			}
			int[] result = DBTemplate.getInstance().batchUpdateSql(sqls);
			System.out.println(result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * �޸Ľ�ɫ
	 * @param roles
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public boolean modifyRoles(List<RoleBean> roles){
		try {
			String[] sqls = new String[roles.size()];
			for(int i=0;i<roles.size();i++){
				if(roles.get(i).getRoleId() == null || "".equals(roles.get(i).getRoleId())){
					sqls[i] = getInsertRoleSql(roles.get(i));
				}else{
					sqls[i] = getUpdateRoleSql(roles.get(i));
				}
			}
			int[] result = DBTemplate.getInstance().batchUpdateSql(sqls);
			System.out.println(result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * ɾ����ɫ
	 * @param roles
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public boolean deleteRoles(List<RoleBean> roles){
		try {
			for(int i=0;i<roles.size();i++){
				deleteRole(roles.get(i));
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * ɾ����ɫ
	 * @param role
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public boolean deleteRole(RoleBean role){
		//ɾ����ɫȨ�޹���
		String sql = "DELETE FROM " + TableConst.TABLE_REL_ROLE_PRIV
				+ " WHERE ��ɫID='" + role.getRoleId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		//ɾ����ɫ���û�����
		sql = "DELETE FROM " + TableConst.TABLE_REL_USER_ROLE + " WHERE ��ɫID='"
				+ role.getRoleId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		//ɾ����ɫ����
		sql = "DELETE FROM " + TableConst.TABLE_MANAGE_ROLE + " WHERE ��ɫID='"
				+ role.getRoleId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		return true;
	}
	
	
	
	/**
	 * ���� ��ɫ �� �û����� ��Ϣ
	 * @param roleId
	 * @param userlist
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public boolean saveRoleRelUsers(String roleId,ArrayList<UserBean> userlist){
		//ɾ��ԭ�н�ɫ���û�����
		String delSql = "DELETE FROM " + TableConst.TABLE_REL_USER_ROLE
				+ " WHERE ��ɫID='" + roleId + "'";
		DBTemplate.getInstance().updateSql(delSql);
		//��ӽ�ɫ���û�����
		String insSql = "INSERT INTO " + TableConst.TABLE_REL_USER_ROLE
				+ " (�û�ID,��ɫID) VALUES(?,?)";
		final List<List<String>> datalist = new ArrayList<List<String>>();
		for (int i = 0; i < userlist.size(); i++) {
			List<String> row = new ArrayList<String>();
			row.add(userlist.get(i).getUserId());
			row.add(roleId);
			datalist.add(row);
		}
		DBTemplate.getInstance().batchPreparedUpdate(insSql, datalist);
		return true;
	}
	
	/**
	 * ͨ����ǰ��֯����������н�ɫ������������֯����
	 * @param orgId
	 * @return
	 */
	public List<RoleBean> getRolesFromCurrentOrg(String orgId){
		String sql = "SELECT * FROM "+TableConst.TABLE_MANAGE_ROLE+" WHERE ������֯���� = '"+orgId+"' ";
		try {
			return getRoleList(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<RoleBean>();
		}
	}
	
	
	/**
	 * ͨ���û� ��ù����Ľ�ɫ 
	 * @param userId
	 * @return
	 */
	public List<RoleBean> getRolesFromUser(String userId){
		String sql = "SELECT * FROM " + TableConst.TABLE_MANAGE_ROLE + " a,"
				+ TableConst.TABLE_REL_USER_ROLE
				+ " b WHERE a.��ɫID=b.��ɫID and b.�û�ID = '" + userId + "'";
		return getRoleList(sql);
	}
	
	/**
	 * ��� ��ǰ��֯���� �� ��ɫ��Ϣ(��ѡ��)
	 * �����û�
	 * @param orgId
	 * @param userId
	 * @return
	 */
	public List<RoleBean> getRolesFromOrgRelUser(String orgId,String userId){
		String subSql = "SELECT * FROM "+TableConst.TABLE_REL_USER_ROLE;
		if(userId != null && !"".equals(userId)){
			subSql += " WHERE �û�ID = '"+userId+"'";
		}
		String sql = "SELECT a.��ɫID,��ɫ��, �û�ID ��ɫ����,״̬,������֯����,����,����ʱ�� FROM "
				+ TableConst.TABLE_MANAGE_ROLE + " a left join ( " + subSql
				+ " ) b on a.��ɫID = b.��ɫID where a.������֯����='" + orgId + "'";
		try {
			return getRoleList(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<RoleBean>();
		}
	}
	
	
	/**
	 * ��� ��ǰ��֯���� �� ��ɫ��Ϣ (��ѡ��)
	 * ����Ȩ������
	 * @param orgId
	 * @param userId
	 * @return
	 */
	public List<RoleBean> getRolesFromOrgRelPrivAttr(String orgId,String attrId){
		String subSql = "SELECT * FROM "+TableConst.TABLE_REL_ROLE_PRIV;
		if(attrId != null && !"".equals(attrId)){
			subSql += " WHERE ����ID = '"+attrId+"'";
		}
		String sql = "SELECT a.��ɫID,��ɫ��, ����ID ��ɫ����,״̬,������֯����,����,����ʱ�� FROM "
				+ TableConst.TABLE_MANAGE_ROLE + " a left join ( " + subSql
				+ " ) b on a.��ɫID = b.��ɫID where a.������֯����='" + orgId + "'";
		try {
			return getRoleList(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<RoleBean>();
		}
	}
	
	/**
	 * ��ù���Ȩ�����ԵĽ�ɫ
	 * @param attrId
	 * @return
	 */
	public List<RoleBean> getRolesRelPrivAttr(String attrId){
		String sql = "SELECT a.��ɫID,��ɫ��, ����ID ��ɫ����,״̬,������֯����,����,����ʱ�� FROM "
				+ TableConst.TABLE_MANAGE_ROLE + " a, "
				+ TableConst.TABLE_REL_ROLE_PRIV
				+ " b WHERE a.��ɫID = b.��ɫID and ����ID='" + attrId + "' ";
		try {
			return getRoleList(sql);
		} catch (Exception e) {
			return new ArrayList<RoleBean>();
		}
	}
	
	
	/**
	 * ͨ����֯�������ӻ���
	 * ��ҳ ��ý�ɫ�б� ������������
	 * @param filtersql
	 * @return
	 */
	public List<RoleBean> getRolesFromOrg(String orgId,String filtersql,UserBean user){
		List<RoleBean> rolelist = new ArrayList<RoleBean>();
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("SELECT * FROM " + TableConst.TABLE_MANAGE_ROLE);
			ArrayList<String> filterSqls = new ArrayList<String>();
			if (filtersql != null && !"".equals(filtersql)) {
				filterSqls.add(filtersql);
			}
			if (orgId.equals(WebConfigUtil.getOrgPrivNode())) {
				// ��֯�������ڵ�
				StringBuffer sb = new StringBuffer();
				List<PrivAttrBean> attrs = ServiceManager.getPrivDao()
						.getUserPrivAttr(user.getUserId(),
								WebConfigUtil.getOrgPrivNode());
				if(attrs.size() > 0){
					sb.append(" ������֯���� IN ( SELECT ��֯����ID FROM "
							+ TableConst.TABLE_MANAGE_ORG
							+ " b start with ��֯������ in (");
					for (int i = 0; i < attrs.size(); i++) {
						sb.append("'");
						sb.append(attrs.get(i).getPrivAttrValue());
						sb.append("'");
						if (i < attrs.size() - 1) {
							sb.append(" , ");
						}
					}
					sb.append(") CONNECT BY �ϼ���֯����ID = PRIOR ��֯����ID )");
					filterSqls.add(sb.toString());
				}
			} else {
				filterSqls.add(" ������֯���� IN ( SELECT ��֯����ID FROM "
						+ TableConst.TABLE_MANAGE_ORG
						+ " b start with ��֯����ID = '" + orgId
						+ "' CONNECT BY �ϼ���֯����ID = PRIOR ��֯����ID )");
			}
			for (int j = 0; j < filterSqls.size(); j++) {
				if (j == 0) {
					sbSql.append(" WHERE ");
				}
				sbSql.append(filterSqls.get(j));
				if (j < filterSqls.size() - 1) {
					sbSql.append(" AND ");
				}
			}
			rolelist = getRoleList(sbSql.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rolelist;
	}
	
	/**
	 * ��ý�ɫ��Ϣ
	 * @param sql
	 * @return
	 */
	private List<RoleBean> getRoleList(String sql){
		return DBTemplate.getInstance().getResultRowMapperList(sql, new RoleRowMapper("yyyy-MM-dd"));
	}
	
	
	/**
	 * ��� �û��� ����SQL
	 * @param user
	 * @return
	 */
	private String getInsertRoleSql(RoleBean role) {
		return "INSERT INTO " + TableConst.TABLE_MANAGE_ROLE
				+ " (��ɫID,��ɫ��,��ɫ����,״̬,������֯����,����,����ʱ��) VALUES('"
				+ CommonTools.createId("ROLE") + "','" + role.getRoleName() + "','"
				+ role.getRoleDesc() + "',1,'" + role.getOrgId()
				+ "',1,sysdate)";
	}	
	
	/**
	 * ��� ��ɫ�� ����SQL
	 * @param user
	 * @return
	 */
	private String getUpdateRoleSql(RoleBean role) {
		return "UPDATE " + TableConst.TABLE_MANAGE_ROLE + "set ��ɫ��='"
				+ role.getRoleName() + "',��ɫ����='" + role.getRoleDesc()
				+ "',������֯����='" + role.getOrgId() + "' WHERE ��ɫID='"
				+ role.getRoleId() + "'";
	}

	
}
