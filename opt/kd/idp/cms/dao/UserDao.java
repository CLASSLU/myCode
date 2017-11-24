package kd.idp.cms.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kd.idp.cms.bean.priv.PrivAttrBean;
import kd.idp.cms.bean.priv.RoleBean;
import kd.idp.cms.bean.priv.UserBean;
import kd.idp.cms.mapper.priv.UserRowMapper;
import kd.idp.common.CommonTools;
import kd.idp.common.config.WebConfigUtil;
import kd.idp.common.consts.TableConst;
import kd.idp.security.password.BeanMd5;

import com.spring.ServiceManager;
import com.spring.dbservice.DBTemplate;


@Transactional
public class UserDao{

	
	/**
	 * 用户 是否存在
	 * @param userName
	 * @return
	 * true 存在
	 * false 不存在
	 */
	public boolean isUserExsit(String userName){
		try {
			if(DBTemplate.getInstance().getResultMap("SELECT 用户ID FROM "+TableConst.TABLE_MANAGE_USER+" WHERE 登录名='"+userName+"'") == null){
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * 通过用户ID 获得用户信息
	 * @param userId
	 * @return
	 */
	public UserBean getUserFromID(String userId){
//		String sql = "SELECT * FROM "+TableConst.VIEW_MANAGE_USER +" where 用户ID='"+userId+"'";
		String sql = "SELECT * FROM "+TableConst.TABLE_MANAGE_USER +" where 用户ID='"+userId+"'";
		try {
			List<UserBean> userList = getUserList(sql);
			if(userList != null && userList.size()>0){
				return userList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 通过用户名 获得用户信息
	 * @param userName
	 * @return
	 */
	public UserBean getUserFromName(String userName){
		String sql = "SELECT * FROM "+TableConst.VIEW_MANAGE_USER + " where 登录名='"+userName+"'";
		try {
			List<UserBean> userList = getUserList(sql);
			if(userList != null && userList.size()>0){
				return userList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 获得角色关联的 用户
	 * @param userId
	 * @return
	 */
	public List<UserBean> getUsersFromRole(String roleId){
		String sql = "SELECT * FROM " + TableConst.VIEW_MANAGE_USER + " a,"
				+ TableConst.TABLE_REL_USER_ROLE
				+ " b WHERE a.用户ID=b.用户ID and b.角色ID = '" + roleId + "'";
		return getUserList(sql);
	}
	
	
	/**
	 * 保存 用户 的 角色关联 信息
	 * @param userId
	 * @param rolelist
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public boolean saveUserRelRoles(String userId,ArrayList<RoleBean> rolelist){
		//删除原有用户与角色关联
		String delSql = "DELETE FROM " + TableConst.TABLE_REL_USER_ROLE
				+ " WHERE 用户ID='" + userId + "'";
		DBTemplate.getInstance().updateSql(delSql);
		//添加用户与角色关联
		String insSql = "INSERT INTO " + TableConst.TABLE_REL_USER_ROLE
				+ " (用户ID,角色ID) VALUES(?,?)";
		final List<List<String>> datalist = new ArrayList<List<String>>();
		for (int i = 0; i < rolelist.size(); i++) {
			List<String> row = new ArrayList<String>();
			row.add(userId);
			row.add(rolelist.get(i).getRoleId());
			datalist.add(row);
		}
		DBTemplate.getInstance().batchPreparedUpdate(insSql, datalist);
		return true;
	}
	
	/**
	 *  通过当前组织机构获得所有用户，不包括子组织机构
	 * @param orgId
	 * @return
	 */
	public List<UserBean> getUsersFromCurrentOrg(String orgId){
		String sql = "SELECT * FROM "+TableConst.VIEW_MANAGE_USER+" WHERE 组织机构ID = '"+orgId+"'";
		try {
			return getUserList(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<UserBean>();
		}
	}
	
	/**
	 * 分页 获得用户列表 包含条件过滤
	 * @param orgId
	 * @param filtersql
	 * @return
	 */
	public List<UserBean> getUsersFromOrg(String orgId, String filtersql,UserBean user) {
		List<UserBean> userlist = new ArrayList<UserBean>();
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("SELECT * FROM " + TableConst.VIEW_MANAGE_USER
					+ " WHERE 是否审核 > 0");
			if (filtersql != null && !"".equals(filtersql)) {
				sbSql.append(" AND " + filtersql);
			}
			if (orgId.equals(WebConfigUtil.getOrgPrivNode())) {
				// 组织机构根节点
				List<PrivAttrBean> attrs = ServiceManager.getPrivDao()
						.getUserPrivAttr(user.getUserId(),
								WebConfigUtil.getOrgPrivNode());
				if(attrs.size() > 0){
					sbSql.append(" AND 组织机构ID IN ( SELECT 组织机构ID FROM "
							+ TableConst.TABLE_MANAGE_ORG
							+ " b start with 组织机构名 in (");
					for (int i = 0; i < attrs.size(); i++) {
						sbSql.append("'");
						sbSql.append(attrs.get(i).getPrivAttrValue());
						sbSql.append("'");
						if (i < attrs.size() - 1) {
							sbSql.append(" , ");
						}
					}
					sbSql.append(") CONNECT BY 上级组织机构ID = PRIOR 组织机构ID )");
				}
				
			} else {
				sbSql.append(" AND 组织机构ID IN ( SELECT 组织机构ID FROM "
						+ TableConst.TABLE_MANAGE_ORG
						+ " b start with 组织机构ID = '" + orgId
						+ "' CONNECT BY 上级组织机构ID = PRIOR 组织机构ID )");
			}
			userlist = getUserList(sbSql.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userlist;
	}
	
	
	/**
	 * 未分配组织机构 用户
	 * @param filtersql
	 * @return
	 */
	public List<UserBean> getUsersWithoutOrg(String filtersql){
		List<UserBean> userlist = new ArrayList<UserBean>();
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("SELECT * FROM " + TableConst.VIEW_MANAGE_USER
					+ " WHERE ( 组织机构ID = '" + WebConfigUtil.UNINITIALIZE
					+ "' or 组织机构ID is NULL ) ");
			if (filtersql != null && !"".equals(filtersql)) {
				sbSql.append(" AND " + filtersql);
			}
			userlist = getUserList(sbSql.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userlist;
	}
	
	/**
	 * 未通过审核 用户
	 * @param filtersql
	 * @return
	 */
	public List<UserBean> getUsersWithoutAudit(String filtersql){
		List<UserBean> userlist = new ArrayList<UserBean>();
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("SELECT * FROM " + TableConst.VIEW_MANAGE_USER
					+ " WHERE 是否审核 = 0 ");
			if (filtersql != null && !"".equals(filtersql)) {
				sbSql.append(" AND " + filtersql);
			}
			userlist = getUserList(sbSql.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userlist;
	}
	
	/**
	 * 获得 当前组织机构 的 用户信息 (勾选树)
	 * 关联角色
	 * @param orgId
	 * @param roleId
	 * @return
	 */
	public List<UserBean> getUsersFromOrgRelRole(String orgId,String roleId){
		String subSql = "SELECT * FROM "+TableConst.TABLE_REL_USER_ROLE;
		if(roleId != null && !"".equals(roleId)){
			subSql += " WHERE 角色ID = '"+roleId+"'";
		}
		String sql = "SELECT a.用户ID,登录名,显示名,密码,组织机构ID,组织机构名称,单位ID,单位名称,是否冻结,顺序,是否审核,创建时间,所属单位类型," +
				"出生日期,性别,工作时间,学历,毕业学校,毕业时间,政治面貌,入党时间,党员信息,技术职称,职称评级时间," +
				"办公电话,家庭电话,手机号,邮箱,岗位,职级,照片,个人简历,个人荣誉,个人状态, b.角色ID 备注  FROM "
				+ TableConst.VIEW_MANAGE_USER
				+ " a left join ( "
				+ subSql
				+ " ) b on a.用户ID = b.用户ID where a.组织机构ID='" + orgId + "'";
		try {
			return getUserList(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<UserBean>();
		}
	}
	
	/**
	 * 获得 当前组织机构 的 用户信息 (勾选树)
	 * 关联权限属性
	 * @param orgId
	 * @param roleId
	 * @return
	 */
	public List<UserBean> getUsersFromOrgRelPrivAttr(String orgId,String attrId){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT 用户ID FROM ");
		sb.append(TableConst.TABLE_REL_USER_PRIV);
		sb.append(" WHERE 属性ID = '");
		sb.append(attrId);
		sb.append("' UNION SELECT 用户ID FROM ");
		sb.append(TableConst.TABLE_REL_ROLE_PRIV);
		sb.append(" a, ");
		sb.append(TableConst.TABLE_REL_USER_ROLE);
		sb.append(" b WHERE a.角色ID=b.角色ID AND 属性ID = '");
		sb.append(attrId);
		sb.append("' ");
		String sql = "SELECT a.用户ID,登录名,显示名,密码,组织机构ID,组织机构名称,单位名称,单位ID,是否冻结,顺序,是否审核,创建时间,"
				+ "出生日期,性别,工作时间,学历,毕业学校,毕业时间,政治面貌,入党时间,党员信息,技术职称,职称评级时间,"
				+ "办公电话,家庭电话,手机号,邮箱,岗位,职级,照片,个人简历,个人荣誉,个人状态, c.用户ID 备注  FROM "
				+ TableConst.VIEW_MANAGE_USER + " a left join ( "
				+ sb.toString() + " ) c on a.用户ID = c.用户ID where a.组织机构ID='"
				+ orgId + "'";
		try {
			return getUserList(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<UserBean>();
		}
	}
	
	/**
	 * 获得关联权限属性的用户
	 * @param attrId
	 * @return
	 */
	public List<UserBean> getUsersRelPrivAttr(String attrId){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT 用户ID,属性ID FROM ");
		sb.append(TableConst.TABLE_REL_USER_PRIV);
		sb.append(" WHERE 属性ID = '");
		sb.append(attrId);
		sb.append("' UNION SELECT 用户ID,属性ID FROM ");
		sb.append(TableConst.TABLE_REL_ROLE_PRIV);
		sb.append(" a, ");
		sb.append(TableConst.TABLE_REL_USER_ROLE);
		sb.append(" b WHERE a.角色ID=b.角色ID AND 属性ID = '");
		sb.append(attrId);
		sb.append("' ");
		String sql = "SELECT a.用户ID,登录名,显示名,密码,组织机构ID,组织机构名称,单位名称,单位ID,是否冻结,顺序,是否审核,创建时间,所属单位类型,"
				+ "出生日期,性别,工作时间,学历,毕业学校,毕业时间,政治面貌,入党时间,党员信息,技术职称,职称评级时间,"
				+ "办公电话,家庭电话,手机号,邮箱,岗位,职级,照片,个人简历,个人荣誉,个人状态, c.用户ID 备注  FROM "
				+ TableConst.VIEW_MANAGE_USER + " a, ( "
				+ sb.toString()
				+ " ) c WHERE a.用户ID = c.用户ID and 属性ID='" + attrId + "' ";
		try {
			return getUserList(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<UserBean>();
		}
	}
	
	
	/**
	 * 获得用户信息
	 * @param sql
	 * @return
	 */
	private List<UserBean> getUserList(String sql){
		return DBTemplate.getInstance().getResultRowMapperList(sql, new UserRowMapper());
	}
	

	/**
	 * 新增用户
	 * @param user
	 * @param isAudit true 已审核 false 未审核
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public boolean addNewUser(UserBean user, boolean isAudit){
		String userId = CommonTools.createId("USER");
		user.setUserId(userId);
		DBTemplate.getInstance().updateSql(getInsertUserSql(user, isAudit));
		//DBTemplate.getInstance().updateSql(getInsertUserAttrSql(user));
		return true;
	}
	
	/**
	 * 修改用户
	 * @param user
	 * @return
	 *//*
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public boolean modifyUser(UserBean user){
		try {
			if(DBTemplate.getInstance().updateSql(getUpdateUserSql(user)) > 0){
				if(DBTemplate.getInstance().updateSql(getUpdateUserAttrSql(user)) == 0){
					DBTemplate.getInstance().updateSql(getInsertUserAttrSql(user));
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}*/
	
	/**
	 * 删除用户
	 * @param user
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public boolean deleteUser(UserBean user){
		//删除用户权限
		String sql = "DELETE FROM " + TableConst.TABLE_REL_USER_PRIV
				+ " WHERE 用户ID='" + user.getUserId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		//删除用户与角色关联
		sql = "DELETE FROM " + TableConst.TABLE_REL_USER_ROLE + " WHERE 用户ID='"
				+ user.getUserId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		//删除用户属性
		sql = "DELETE FROM " + TableConst.TABLE_MANAGE_USER_ATTR
				+ " WHERE 用户ID='" + user.getUserId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		//删除用户
		sql = "DELETE FROM " + TableConst.TABLE_MANAGE_USER + " WHERE 用户ID='"
				+ user.getUserId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		return true;
	}
	
	/**
	 * 批量删除用户
	 * @param user
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public int deleteUsers(List<UserBean> users){
		List<List<String>> idList = new ArrayList<List<String>>();
		for (int i = 0; i < users.size(); i++) {
			List<String> userid = new ArrayList<String>();
			userid.add(users.get(i).getUserId());
			idList.add(userid);
		}
		//删除用户权限
		String sql = "DELETE FROM " + TableConst.TABLE_REL_USER_PRIV
				+ " WHERE 用户ID=?";
		DBTemplate.getInstance().batchPreparedUpdate(sql, idList);
		//删除用户与角色关联
		sql = "DELETE FROM " + TableConst.TABLE_REL_USER_ROLE + " WHERE 用户ID=?";
		DBTemplate.getInstance().batchPreparedUpdate(sql, idList);
		//删除用户属性
		sql = "DELETE FROM " + TableConst.TABLE_MANAGE_USER_ATTR
				+ " WHERE 用户ID=?";
		DBTemplate.getInstance().batchPreparedUpdate(sql, idList);
		//删除用户
		sql = "DELETE FROM " + TableConst.TABLE_MANAGE_USER + " WHERE 用户ID=?";
		DBTemplate.getInstance().batchPreparedUpdate(sql, idList);
		return idList.size();
	}
	
	
	/**
	 * 获得 用户表 插入SQL
	 * @param user
	 * @param isAudit true 已审核 false 未审核
	 * @return
	 */
	protected String getInsertUserSql(UserBean user,boolean isAudit) {
		return "INSERT INTO " + TableConst.TABLE_MANAGE_USER
				+ " (用户ID,登录名,显示名,密码,单位ID,单位名称,组织机构ID,组织机构名称,是否冻结,顺序,是否审核,创建时间,所属单位类型)" + " VALUES('"
				+ user.getUserId() + "','" + user.getUserName() + "','"
				+ user.getUserDisplayName() + "','" + BeanMd5.MD5HexEncode(user.getUserPwd()) + "','" + user.getUserCompanyId() + "','" + user.getUserCompanyName() + "','"
				+ user.getUserOrgId() + "','" + user.getUserOrgName() + "',1,999,"+((isAudit)?"1":"0")+",sysdate,'"+user.getCompanyType()+"')";
	}

/*	*//**
	 * 获得 用户属性表 插入SQL
	 * @param user
	 * @return
	 *//*
	private String getInsertUserAttrSql(UserBean user) {
		UserAttrBean attr = user.getAttr();
		return "INSERT INTO "
				+ TableConst.TABLE_MANAGE_USER_ATTR
				+ " (用户ID,出生日期,性别,工作时间,学历,毕业学校,毕业时间,"
				+ "政治面貌,入党时间,党员信息,技术职称,职称评级时间,办公电话,家庭电话,手机号,邮箱,岗位,职级,照片,个人简历,个人荣誉,个人状态,备注) "
				+ "VALUES('" + user.getUserId() + "',to_date('"
				+ attr.getBirthDate() + "','YYYY-MM-DD'),"
				+ attr.getUserGender() + ",to_date('" + attr.getEmployedDate()
				+ "','YYYY-MM-DD')," + "'" + attr.getEducation() + "','"
				+ attr.getSchool() + "',to_date('" + attr.getGraduationDate()
				+ "','YYYY-MM-DD'),'" + attr.getPoliticsStatus() + "',"
				+ "to_date('" + attr.getJoinPartyDate() + "','YYYY-MM-DD'),'"
				+ attr.getPartyMemberInfo() + "','" + attr.getTechnicalTitle()
				+ "',to_date('" + attr.getTechTitleDate() + "',"
				+ "'YYYY-MM-DD'),'" + attr.getOfficeTel() + "','"
				+ attr.getHomeTel() + "','" + attr.getPhoneNumber() + "','"
				+ attr.getEmail() + "','" + attr.getPosition() + "'," + "'"
				+ attr.getPositionLevel() + "','" + attr.getPhoto() + "','"
				+ attr.getResume() + "','" + attr.getHonor() + "','"
				+ attr.getPersonalStatus() + "','" + attr.getBak() + "')";
	}*/

	/**
	 * 获得 用户表 更新SQL
	 * @param user
	 * @return
	 */
	protected String getUpdateUserSql(UserBean user) {
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE ");
		sb.append(TableConst.TABLE_MANAGE_USER);
		sb.append(" set 登录名='");
		sb.append(user.getUserName());
		sb.append("', 显示名='");
		sb.append(user.getUserDisplayName());
		sb.append("',");
		if (user.getUserPwd() != null && !"".equals(user.getUserPwd())) {
			sb.append("密码='");
			sb.append(BeanMd5.MD5HexEncode(user.getUserPwd()));
			sb.append("',");
		}
		sb.append("单位ID='");
		sb.append(user.getUserCompanyId());
		sb.append("', 单位名称='");
		sb.append(user.getUserCompanyName());
		sb.append("', 组织机构ID='");
		sb.append(user.getUserOrgId());
		sb.append("', 组织机构名称='");
		sb.append(user.getUserOrgName());
//		sb.append("', 是否审核=");
//		sb.append(user.getAudit());
		sb.append("' WHERE 用户ID='");
		sb.append(user.getUserId());
		sb.append("'");
		return sb.toString();
	}

	/**
	 * 获得 用户属性表 更新SQL
	 * @param user
	 * @return
	 *//*
	protected String getUpdateUserAttrSql(UserBean user) {
		UserAttrBean attr = user.getAttr();
		return "UPDATE " + TableConst.TABLE_MANAGE_USER_ATTR
				+ " SET 出生日期=to_date('" + attr.getBirthDate()
				+ "','YYYY-MM-DD'),性别=" + attr.getUserGender()
				+ ",工作时间=to_date('" + attr.getEmployedDate()
				+ "','YYYY-MM-DD'),学历='" + attr.getEducation() + "',毕业学校='"
				+ attr.getSchool() + "',毕业时间=to_date('"
				+ attr.getGraduationDate() + "','YYYY-MM-DD')" + ","
				+ "政治面貌='" + attr.getPoliticsStatus() + "',入党时间=to_date('"
				+ attr.getJoinPartyDate() + "','YYYY-MM-DD'),党员信息='"
				+ attr.getPartyMemberInfo() + "',技术职称='"
				+ attr.getTechnicalTitle() + "',职称评级时间=to_date('"
				+ attr.getTechTitleDate() + "','YYYY-MM-DD'),办公电话='"
				+ attr.getOfficeTel() + "',家庭电话='" + attr.getHomeTel()
				+ "',手机号='" + attr.getPhoneNumber() + "'," + "邮箱='"
				+ attr.getEmail() + "',岗位='" + attr.getPosition() + "',职级='"
				+ attr.getPositionLevel() + "',照片='" + attr.getPhoto()
				+ "',个人简历='" + attr.getResume() + "',个人荣誉='" + attr.getHonor()
				+ "',个人状态='" + attr.getPersonalStatus() + "',备注='"
				+ attr.getBak() + "' WHERE 用户ID='" + user.getUserId() + "'";
	}
	*/
}
