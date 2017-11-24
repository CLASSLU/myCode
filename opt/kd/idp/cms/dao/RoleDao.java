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
	 * 新增 角色
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
	 * 修改角色
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
	 * 删除角色
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
	 * 删除角色
	 * @param role
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public boolean deleteRole(RoleBean role){
		//删除角色权限关联
		String sql = "DELETE FROM " + TableConst.TABLE_REL_ROLE_PRIV
				+ " WHERE 角色ID='" + role.getRoleId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		//删除角色与用户关联
		sql = "DELETE FROM " + TableConst.TABLE_REL_USER_ROLE + " WHERE 角色ID='"
				+ role.getRoleId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		//删除角色本身
		sql = "DELETE FROM " + TableConst.TABLE_MANAGE_ROLE + " WHERE 角色ID='"
				+ role.getRoleId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		return true;
	}
	
	
	
	/**
	 * 保存 角色 的 用户关联 信息
	 * @param roleId
	 * @param userlist
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public boolean saveRoleRelUsers(String roleId,ArrayList<UserBean> userlist){
		//删除原有角色与用户关联
		String delSql = "DELETE FROM " + TableConst.TABLE_REL_USER_ROLE
				+ " WHERE 角色ID='" + roleId + "'";
		DBTemplate.getInstance().updateSql(delSql);
		//添加角色与用户关联
		String insSql = "INSERT INTO " + TableConst.TABLE_REL_USER_ROLE
				+ " (用户ID,角色ID) VALUES(?,?)";
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
	 * 通过当前组织机构获得所有角色，不包括子组织机构
	 * @param orgId
	 * @return
	 */
	public List<RoleBean> getRolesFromCurrentOrg(String orgId){
		String sql = "SELECT * FROM "+TableConst.TABLE_MANAGE_ROLE+" WHERE 所属组织机构 = '"+orgId+"' ";
		try {
			return getRoleList(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<RoleBean>();
		}
	}
	
	
	/**
	 * 通过用户 获得关联的角色 
	 * @param userId
	 * @return
	 */
	public List<RoleBean> getRolesFromUser(String userId){
		String sql = "SELECT * FROM " + TableConst.TABLE_MANAGE_ROLE + " a,"
				+ TableConst.TABLE_REL_USER_ROLE
				+ " b WHERE a.角色ID=b.角色ID and b.用户ID = '" + userId + "'";
		return getRoleList(sql);
	}
	
	/**
	 * 获得 当前组织机构 的 角色信息(勾选树)
	 * 关联用户
	 * @param orgId
	 * @param userId
	 * @return
	 */
	public List<RoleBean> getRolesFromOrgRelUser(String orgId,String userId){
		String subSql = "SELECT * FROM "+TableConst.TABLE_REL_USER_ROLE;
		if(userId != null && !"".equals(userId)){
			subSql += " WHERE 用户ID = '"+userId+"'";
		}
		String sql = "SELECT a.角色ID,角色名, 用户ID 角色描述,状态,所属组织机构,排序,创建时间 FROM "
				+ TableConst.TABLE_MANAGE_ROLE + " a left join ( " + subSql
				+ " ) b on a.角色ID = b.角色ID where a.所属组织机构='" + orgId + "'";
		try {
			return getRoleList(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<RoleBean>();
		}
	}
	
	
	/**
	 * 获得 当前组织机构 的 角色信息 (勾选树)
	 * 关联权限属性
	 * @param orgId
	 * @param userId
	 * @return
	 */
	public List<RoleBean> getRolesFromOrgRelPrivAttr(String orgId,String attrId){
		String subSql = "SELECT * FROM "+TableConst.TABLE_REL_ROLE_PRIV;
		if(attrId != null && !"".equals(attrId)){
			subSql += " WHERE 属性ID = '"+attrId+"'";
		}
		String sql = "SELECT a.角色ID,角色名, 属性ID 角色描述,状态,所属组织机构,排序,创建时间 FROM "
				+ TableConst.TABLE_MANAGE_ROLE + " a left join ( " + subSql
				+ " ) b on a.角色ID = b.角色ID where a.所属组织机构='" + orgId + "'";
		try {
			return getRoleList(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<RoleBean>();
		}
	}
	
	/**
	 * 获得关联权限属性的角色
	 * @param attrId
	 * @return
	 */
	public List<RoleBean> getRolesRelPrivAttr(String attrId){
		String sql = "SELECT a.角色ID,角色名, 属性ID 角色描述,状态,所属组织机构,排序,创建时间 FROM "
				+ TableConst.TABLE_MANAGE_ROLE + " a, "
				+ TableConst.TABLE_REL_ROLE_PRIV
				+ " b WHERE a.角色ID = b.角色ID and 属性ID='" + attrId + "' ";
		try {
			return getRoleList(sql);
		} catch (Exception e) {
			return new ArrayList<RoleBean>();
		}
	}
	
	
	/**
	 * 通过组织机构及子机构
	 * 分页 获得角色列表 包含条件过滤
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
				// 组织机构根节点
				StringBuffer sb = new StringBuffer();
				List<PrivAttrBean> attrs = ServiceManager.getPrivDao()
						.getUserPrivAttr(user.getUserId(),
								WebConfigUtil.getOrgPrivNode());
				if(attrs.size() > 0){
					sb.append(" 所属组织机构 IN ( SELECT 组织机构ID FROM "
							+ TableConst.TABLE_MANAGE_ORG
							+ " b start with 组织机构名 in (");
					for (int i = 0; i < attrs.size(); i++) {
						sb.append("'");
						sb.append(attrs.get(i).getPrivAttrValue());
						sb.append("'");
						if (i < attrs.size() - 1) {
							sb.append(" , ");
						}
					}
					sb.append(") CONNECT BY 上级组织机构ID = PRIOR 组织机构ID )");
					filterSqls.add(sb.toString());
				}
			} else {
				filterSqls.add(" 所属组织机构 IN ( SELECT 组织机构ID FROM "
						+ TableConst.TABLE_MANAGE_ORG
						+ " b start with 组织机构ID = '" + orgId
						+ "' CONNECT BY 上级组织机构ID = PRIOR 组织机构ID )");
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
	 * 获得角色信息
	 * @param sql
	 * @return
	 */
	private List<RoleBean> getRoleList(String sql){
		return DBTemplate.getInstance().getResultRowMapperList(sql, new RoleRowMapper("yyyy-MM-dd"));
	}
	
	
	/**
	 * 获得 用户表 插入SQL
	 * @param user
	 * @return
	 */
	private String getInsertRoleSql(RoleBean role) {
		return "INSERT INTO " + TableConst.TABLE_MANAGE_ROLE
				+ " (角色ID,角色名,角色描述,状态,所属组织机构,排序,创建时间) VALUES('"
				+ CommonTools.createId("ROLE") + "','" + role.getRoleName() + "','"
				+ role.getRoleDesc() + "',1,'" + role.getOrgId()
				+ "',1,sysdate)";
	}	
	
	/**
	 * 获得 角色表 更新SQL
	 * @param user
	 * @return
	 */
	private String getUpdateRoleSql(RoleBean role) {
		return "UPDATE " + TableConst.TABLE_MANAGE_ROLE + "set 角色名='"
				+ role.getRoleName() + "',角色描述='" + role.getRoleDesc()
				+ "',所属组织机构='" + role.getOrgId() + "' WHERE 角色ID='"
				+ role.getRoleId() + "'";
	}

	
}
