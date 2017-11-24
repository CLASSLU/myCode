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
	 * �û� �Ƿ����
	 * @param userName
	 * @return
	 * true ����
	 * false ������
	 */
	public boolean isUserExsit(String userName){
		try {
			if(DBTemplate.getInstance().getResultMap("SELECT �û�ID FROM "+TableConst.TABLE_MANAGE_USER+" WHERE ��¼��='"+userName+"'") == null){
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * ͨ���û�ID ����û���Ϣ
	 * @param userId
	 * @return
	 */
	public UserBean getUserFromID(String userId){
//		String sql = "SELECT * FROM "+TableConst.VIEW_MANAGE_USER +" where �û�ID='"+userId+"'";
		String sql = "SELECT * FROM "+TableConst.TABLE_MANAGE_USER +" where �û�ID='"+userId+"'";
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
	 * ͨ���û��� ����û���Ϣ
	 * @param userName
	 * @return
	 */
	public UserBean getUserFromName(String userName){
		String sql = "SELECT * FROM "+TableConst.VIEW_MANAGE_USER + " where ��¼��='"+userName+"'";
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
	 * ��ý�ɫ������ �û�
	 * @param userId
	 * @return
	 */
	public List<UserBean> getUsersFromRole(String roleId){
		String sql = "SELECT * FROM " + TableConst.VIEW_MANAGE_USER + " a,"
				+ TableConst.TABLE_REL_USER_ROLE
				+ " b WHERE a.�û�ID=b.�û�ID and b.��ɫID = '" + roleId + "'";
		return getUserList(sql);
	}
	
	
	/**
	 * ���� �û� �� ��ɫ���� ��Ϣ
	 * @param userId
	 * @param rolelist
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public boolean saveUserRelRoles(String userId,ArrayList<RoleBean> rolelist){
		//ɾ��ԭ���û����ɫ����
		String delSql = "DELETE FROM " + TableConst.TABLE_REL_USER_ROLE
				+ " WHERE �û�ID='" + userId + "'";
		DBTemplate.getInstance().updateSql(delSql);
		//����û����ɫ����
		String insSql = "INSERT INTO " + TableConst.TABLE_REL_USER_ROLE
				+ " (�û�ID,��ɫID) VALUES(?,?)";
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
	 *  ͨ����ǰ��֯������������û�������������֯����
	 * @param orgId
	 * @return
	 */
	public List<UserBean> getUsersFromCurrentOrg(String orgId){
		String sql = "SELECT * FROM "+TableConst.VIEW_MANAGE_USER+" WHERE ��֯����ID = '"+orgId+"'";
		try {
			return getUserList(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<UserBean>();
		}
	}
	
	/**
	 * ��ҳ ����û��б� ������������
	 * @param orgId
	 * @param filtersql
	 * @return
	 */
	public List<UserBean> getUsersFromOrg(String orgId, String filtersql,UserBean user) {
		List<UserBean> userlist = new ArrayList<UserBean>();
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("SELECT * FROM " + TableConst.VIEW_MANAGE_USER
					+ " WHERE �Ƿ���� > 0");
			if (filtersql != null && !"".equals(filtersql)) {
				sbSql.append(" AND " + filtersql);
			}
			if (orgId.equals(WebConfigUtil.getOrgPrivNode())) {
				// ��֯�������ڵ�
				List<PrivAttrBean> attrs = ServiceManager.getPrivDao()
						.getUserPrivAttr(user.getUserId(),
								WebConfigUtil.getOrgPrivNode());
				if(attrs.size() > 0){
					sbSql.append(" AND ��֯����ID IN ( SELECT ��֯����ID FROM "
							+ TableConst.TABLE_MANAGE_ORG
							+ " b start with ��֯������ in (");
					for (int i = 0; i < attrs.size(); i++) {
						sbSql.append("'");
						sbSql.append(attrs.get(i).getPrivAttrValue());
						sbSql.append("'");
						if (i < attrs.size() - 1) {
							sbSql.append(" , ");
						}
					}
					sbSql.append(") CONNECT BY �ϼ���֯����ID = PRIOR ��֯����ID )");
				}
				
			} else {
				sbSql.append(" AND ��֯����ID IN ( SELECT ��֯����ID FROM "
						+ TableConst.TABLE_MANAGE_ORG
						+ " b start with ��֯����ID = '" + orgId
						+ "' CONNECT BY �ϼ���֯����ID = PRIOR ��֯����ID )");
			}
			userlist = getUserList(sbSql.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userlist;
	}
	
	
	/**
	 * δ������֯���� �û�
	 * @param filtersql
	 * @return
	 */
	public List<UserBean> getUsersWithoutOrg(String filtersql){
		List<UserBean> userlist = new ArrayList<UserBean>();
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("SELECT * FROM " + TableConst.VIEW_MANAGE_USER
					+ " WHERE ( ��֯����ID = '" + WebConfigUtil.UNINITIALIZE
					+ "' or ��֯����ID is NULL ) ");
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
	 * δͨ����� �û�
	 * @param filtersql
	 * @return
	 */
	public List<UserBean> getUsersWithoutAudit(String filtersql){
		List<UserBean> userlist = new ArrayList<UserBean>();
		try {
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("SELECT * FROM " + TableConst.VIEW_MANAGE_USER
					+ " WHERE �Ƿ���� = 0 ");
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
	 * ��� ��ǰ��֯���� �� �û���Ϣ (��ѡ��)
	 * ������ɫ
	 * @param orgId
	 * @param roleId
	 * @return
	 */
	public List<UserBean> getUsersFromOrgRelRole(String orgId,String roleId){
		String subSql = "SELECT * FROM "+TableConst.TABLE_REL_USER_ROLE;
		if(roleId != null && !"".equals(roleId)){
			subSql += " WHERE ��ɫID = '"+roleId+"'";
		}
		String sql = "SELECT a.�û�ID,��¼��,��ʾ��,����,��֯����ID,��֯��������,��λID,��λ����,�Ƿ񶳽�,˳��,�Ƿ����,����ʱ��,������λ����," +
				"��������,�Ա�,����ʱ��,ѧ��,��ҵѧУ,��ҵʱ��,������ò,�뵳ʱ��,��Ա��Ϣ,����ְ��,ְ������ʱ��," +
				"�칫�绰,��ͥ�绰,�ֻ���,����,��λ,ְ��,��Ƭ,���˼���,��������,����״̬, b.��ɫID ��ע  FROM "
				+ TableConst.VIEW_MANAGE_USER
				+ " a left join ( "
				+ subSql
				+ " ) b on a.�û�ID = b.�û�ID where a.��֯����ID='" + orgId + "'";
		try {
			return getUserList(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<UserBean>();
		}
	}
	
	/**
	 * ��� ��ǰ��֯���� �� �û���Ϣ (��ѡ��)
	 * ����Ȩ������
	 * @param orgId
	 * @param roleId
	 * @return
	 */
	public List<UserBean> getUsersFromOrgRelPrivAttr(String orgId,String attrId){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT �û�ID FROM ");
		sb.append(TableConst.TABLE_REL_USER_PRIV);
		sb.append(" WHERE ����ID = '");
		sb.append(attrId);
		sb.append("' UNION SELECT �û�ID FROM ");
		sb.append(TableConst.TABLE_REL_ROLE_PRIV);
		sb.append(" a, ");
		sb.append(TableConst.TABLE_REL_USER_ROLE);
		sb.append(" b WHERE a.��ɫID=b.��ɫID AND ����ID = '");
		sb.append(attrId);
		sb.append("' ");
		String sql = "SELECT a.�û�ID,��¼��,��ʾ��,����,��֯����ID,��֯��������,��λ����,��λID,�Ƿ񶳽�,˳��,�Ƿ����,����ʱ��,"
				+ "��������,�Ա�,����ʱ��,ѧ��,��ҵѧУ,��ҵʱ��,������ò,�뵳ʱ��,��Ա��Ϣ,����ְ��,ְ������ʱ��,"
				+ "�칫�绰,��ͥ�绰,�ֻ���,����,��λ,ְ��,��Ƭ,���˼���,��������,����״̬, c.�û�ID ��ע  FROM "
				+ TableConst.VIEW_MANAGE_USER + " a left join ( "
				+ sb.toString() + " ) c on a.�û�ID = c.�û�ID where a.��֯����ID='"
				+ orgId + "'";
		try {
			return getUserList(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<UserBean>();
		}
	}
	
	/**
	 * ��ù���Ȩ�����Ե��û�
	 * @param attrId
	 * @return
	 */
	public List<UserBean> getUsersRelPrivAttr(String attrId){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT �û�ID,����ID FROM ");
		sb.append(TableConst.TABLE_REL_USER_PRIV);
		sb.append(" WHERE ����ID = '");
		sb.append(attrId);
		sb.append("' UNION SELECT �û�ID,����ID FROM ");
		sb.append(TableConst.TABLE_REL_ROLE_PRIV);
		sb.append(" a, ");
		sb.append(TableConst.TABLE_REL_USER_ROLE);
		sb.append(" b WHERE a.��ɫID=b.��ɫID AND ����ID = '");
		sb.append(attrId);
		sb.append("' ");
		String sql = "SELECT a.�û�ID,��¼��,��ʾ��,����,��֯����ID,��֯��������,��λ����,��λID,�Ƿ񶳽�,˳��,�Ƿ����,����ʱ��,������λ����,"
				+ "��������,�Ա�,����ʱ��,ѧ��,��ҵѧУ,��ҵʱ��,������ò,�뵳ʱ��,��Ա��Ϣ,����ְ��,ְ������ʱ��,"
				+ "�칫�绰,��ͥ�绰,�ֻ���,����,��λ,ְ��,��Ƭ,���˼���,��������,����״̬, c.�û�ID ��ע  FROM "
				+ TableConst.VIEW_MANAGE_USER + " a, ( "
				+ sb.toString()
				+ " ) c WHERE a.�û�ID = c.�û�ID and ����ID='" + attrId + "' ";
		try {
			return getUserList(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<UserBean>();
		}
	}
	
	
	/**
	 * ����û���Ϣ
	 * @param sql
	 * @return
	 */
	private List<UserBean> getUserList(String sql){
		return DBTemplate.getInstance().getResultRowMapperList(sql, new UserRowMapper());
	}
	

	/**
	 * �����û�
	 * @param user
	 * @param isAudit true ����� false δ���
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
	 * �޸��û�
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
	 * ɾ���û�
	 * @param user
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public boolean deleteUser(UserBean user){
		//ɾ���û�Ȩ��
		String sql = "DELETE FROM " + TableConst.TABLE_REL_USER_PRIV
				+ " WHERE �û�ID='" + user.getUserId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		//ɾ���û����ɫ����
		sql = "DELETE FROM " + TableConst.TABLE_REL_USER_ROLE + " WHERE �û�ID='"
				+ user.getUserId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		//ɾ���û�����
		sql = "DELETE FROM " + TableConst.TABLE_MANAGE_USER_ATTR
				+ " WHERE �û�ID='" + user.getUserId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		//ɾ���û�
		sql = "DELETE FROM " + TableConst.TABLE_MANAGE_USER + " WHERE �û�ID='"
				+ user.getUserId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		return true;
	}
	
	/**
	 * ����ɾ���û�
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
		//ɾ���û�Ȩ��
		String sql = "DELETE FROM " + TableConst.TABLE_REL_USER_PRIV
				+ " WHERE �û�ID=?";
		DBTemplate.getInstance().batchPreparedUpdate(sql, idList);
		//ɾ���û����ɫ����
		sql = "DELETE FROM " + TableConst.TABLE_REL_USER_ROLE + " WHERE �û�ID=?";
		DBTemplate.getInstance().batchPreparedUpdate(sql, idList);
		//ɾ���û�����
		sql = "DELETE FROM " + TableConst.TABLE_MANAGE_USER_ATTR
				+ " WHERE �û�ID=?";
		DBTemplate.getInstance().batchPreparedUpdate(sql, idList);
		//ɾ���û�
		sql = "DELETE FROM " + TableConst.TABLE_MANAGE_USER + " WHERE �û�ID=?";
		DBTemplate.getInstance().batchPreparedUpdate(sql, idList);
		return idList.size();
	}
	
	
	/**
	 * ��� �û��� ����SQL
	 * @param user
	 * @param isAudit true ����� false δ���
	 * @return
	 */
	protected String getInsertUserSql(UserBean user,boolean isAudit) {
		return "INSERT INTO " + TableConst.TABLE_MANAGE_USER
				+ " (�û�ID,��¼��,��ʾ��,����,��λID,��λ����,��֯����ID,��֯��������,�Ƿ񶳽�,˳��,�Ƿ����,����ʱ��,������λ����)" + " VALUES('"
				+ user.getUserId() + "','" + user.getUserName() + "','"
				+ user.getUserDisplayName() + "','" + BeanMd5.MD5HexEncode(user.getUserPwd()) + "','" + user.getUserCompanyId() + "','" + user.getUserCompanyName() + "','"
				+ user.getUserOrgId() + "','" + user.getUserOrgName() + "',1,999,"+((isAudit)?"1":"0")+",sysdate,'"+user.getCompanyType()+"')";
	}

/*	*//**
	 * ��� �û����Ա� ����SQL
	 * @param user
	 * @return
	 *//*
	private String getInsertUserAttrSql(UserBean user) {
		UserAttrBean attr = user.getAttr();
		return "INSERT INTO "
				+ TableConst.TABLE_MANAGE_USER_ATTR
				+ " (�û�ID,��������,�Ա�,����ʱ��,ѧ��,��ҵѧУ,��ҵʱ��,"
				+ "������ò,�뵳ʱ��,��Ա��Ϣ,����ְ��,ְ������ʱ��,�칫�绰,��ͥ�绰,�ֻ���,����,��λ,ְ��,��Ƭ,���˼���,��������,����״̬,��ע) "
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
	 * ��� �û��� ����SQL
	 * @param user
	 * @return
	 */
	protected String getUpdateUserSql(UserBean user) {
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE ");
		sb.append(TableConst.TABLE_MANAGE_USER);
		sb.append(" set ��¼��='");
		sb.append(user.getUserName());
		sb.append("', ��ʾ��='");
		sb.append(user.getUserDisplayName());
		sb.append("',");
		if (user.getUserPwd() != null && !"".equals(user.getUserPwd())) {
			sb.append("����='");
			sb.append(BeanMd5.MD5HexEncode(user.getUserPwd()));
			sb.append("',");
		}
		sb.append("��λID='");
		sb.append(user.getUserCompanyId());
		sb.append("', ��λ����='");
		sb.append(user.getUserCompanyName());
		sb.append("', ��֯����ID='");
		sb.append(user.getUserOrgId());
		sb.append("', ��֯��������='");
		sb.append(user.getUserOrgName());
//		sb.append("', �Ƿ����=");
//		sb.append(user.getAudit());
		sb.append("' WHERE �û�ID='");
		sb.append(user.getUserId());
		sb.append("'");
		return sb.toString();
	}

	/**
	 * ��� �û����Ա� ����SQL
	 * @param user
	 * @return
	 *//*
	protected String getUpdateUserAttrSql(UserBean user) {
		UserAttrBean attr = user.getAttr();
		return "UPDATE " + TableConst.TABLE_MANAGE_USER_ATTR
				+ " SET ��������=to_date('" + attr.getBirthDate()
				+ "','YYYY-MM-DD'),�Ա�=" + attr.getUserGender()
				+ ",����ʱ��=to_date('" + attr.getEmployedDate()
				+ "','YYYY-MM-DD'),ѧ��='" + attr.getEducation() + "',��ҵѧУ='"
				+ attr.getSchool() + "',��ҵʱ��=to_date('"
				+ attr.getGraduationDate() + "','YYYY-MM-DD')" + ","
				+ "������ò='" + attr.getPoliticsStatus() + "',�뵳ʱ��=to_date('"
				+ attr.getJoinPartyDate() + "','YYYY-MM-DD'),��Ա��Ϣ='"
				+ attr.getPartyMemberInfo() + "',����ְ��='"
				+ attr.getTechnicalTitle() + "',ְ������ʱ��=to_date('"
				+ attr.getTechTitleDate() + "','YYYY-MM-DD'),�칫�绰='"
				+ attr.getOfficeTel() + "',��ͥ�绰='" + attr.getHomeTel()
				+ "',�ֻ���='" + attr.getPhoneNumber() + "'," + "����='"
				+ attr.getEmail() + "',��λ='" + attr.getPosition() + "',ְ��='"
				+ attr.getPositionLevel() + "',��Ƭ='" + attr.getPhoto()
				+ "',���˼���='" + attr.getResume() + "',��������='" + attr.getHonor()
				+ "',����״̬='" + attr.getPersonalStatus() + "',��ע='"
				+ attr.getBak() + "' WHERE �û�ID='" + user.getUserId() + "'";
	}
	*/
}
