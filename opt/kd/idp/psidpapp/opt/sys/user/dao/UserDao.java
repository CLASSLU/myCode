package kd.idp.psidpapp.opt.sys.user.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.spring.dbservice.DBTemplate;

import kd.idp.psidpapp.opt.sys.constant.DataSourceName;
import kd.idp.psidpapp.opt.sys.constant.TableName;
import kd.idp.psidpapp.opt.sys.dao.BaseDao;

public class UserDao extends BaseDao{
	
	/**
	 * @param userId		�û�ID
	 * @return				���ض�Ӧ�û���ɫID��
	 * @throws Exception
	 */
	public List<String> findRoleIdByUserId(String userId)throws Exception{
		List<String> roleIdList = new ArrayList<String>();
		StringBuilder sb = new StringBuilder("");
		try {
			sb.setLength(0);
			sb.append("SELECT * FROM ").append(TableName.TABLE_SYS_USER_ROLE)
			  .append(" WHERE USERID='").append(userId).append("'");
			SqlRowSet rs = DBTemplate.getInstance(DataSourceName.DATASOURCE_DM).queryForRowSet(sb.toString());
			while (rs.next()){
				roleIdList.add(rs.getString("ROLEID"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return roleIdList;
	}
	
	/**
	 * @param roleId		��ɫID
	 * @return				���ض�Ӧ�û���Ϣ��
	 * @throws Exception
	 */
	public List<Map<String, Object>> findUserListIdByRoleId(String roleId)throws Exception{
		List<Map<String, Object>> userMapList = null;
		StringBuilder sb = new StringBuilder("");
		try {
			sb.setLength(0);
			sb.append("SELECT * FROM ").append(TableName.TABLE_SYS_USER).append(" WHERE ID IN(").append("SELECT USERID FROM ").append(TableName.TABLE_SYS_USER_ROLE)
			  .append(" WHERE ROLEID='").append(roleId).append("')");
			userMapList = DBTemplate.getInstance(DataSourceName.DATASOURCE_DM).getResultMapList(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return userMapList;
	}
	
	/**
	 * ���ض�Ӧ���������������������û�
	 * @param orgId		��֯����ID
	 * @param sqlCondition		�û���������
	 * @return			���ض�Ӧ���������������������û�
	 * @throws Exception
	 */
	public List<Map<String,Object>> findUserListByOrgId(String orgId, String sqlCondition)throws Exception{
		List<Map<String,Object>> userMapList = null;
		StringBuilder sb = new StringBuilder("");
		try {
			sb.setLength(0);
			String sql = sb.append("SELECT * FROM ").append(TableName.TABLE_SYS_USER).append(" WHERE ORGID IN").append("(SELECT ID FROM(SELECT * FROM ").append(TableName.TABLE_SYS_ORG).append(" WHERE DATASTATUSID='1' ORDER BY SORT ASC) ")
			  .append("START WITH PARENTID='").append(orgId).append("' CONNECT BY PRIOR ID=PARENTID) OR (ORGID='" + orgId + "')").toString();
			if (StringUtils.isNotEmpty(sqlCondition)){
				sb.setLength(0);
				sb.append("SELECT * FROM (").append(sql).append(") WHERE 1=1 ").append(sqlCondition);
			}
			userMapList = DBTemplate.getInstance(DataSourceName.DATASOURCE_DM).getResultMapList(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return userMapList;
	}
	
	/**
	 * ���ض�Ӧ�����������û�
	 * @param orgId		��֯����ID
	 * @param sqlCondition		�û���������
	 * @return			���ض�Ӧ�����������û�
	 * @throws Exception
	 */
	public List<Map<String,Object>> findUserListByOrgId2(String orgId, String sqlCondition)throws Exception{
		List<Map<String,Object>> userMapList = null;
		StringBuilder sb = new StringBuilder("");
		try {
			sb.setLength(0);
			String sql = sb.append("SELECT * FROM ").append(TableName.TABLE_SYS_USER).append(" WHERE ORGID IN('").append(orgId).append("')").toString();
			if (StringUtils.isNotEmpty(sqlCondition)){
				sb.setLength(0);
				sb.append("SELECT * FROM (").append(sql).append(") WHERE 1=1 ").append(sqlCondition);
			}
			userMapList = DBTemplate.getInstance(DataSourceName.DATASOURCE_DM).getResultMapList(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return userMapList;
	}
	
	/**
	 * ��ȡ��Ч�û�����֯����ID
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> findUserOrgIdList()throws Exception{
		List<Map<String,Object>> userOrgIdList = null;
		StringBuilder sb = new StringBuilder("");
		try {
			sb.setLength(0);
			sb.append("SELECT DISTINCT ORGID FROM ").append(TableName.TABLE_SYS_USER).append(" WHERE DATASTATUSID='1'");
			userOrgIdList = DBTemplate.getInstance(DataSourceName.DATASOURCE_DM).getResultMapList(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return userOrgIdList;
	}
	
	/**
	 * �����ɫ�û���ϵ��Ϣ
	 * @param roleId		��ɫID
	 * @param userIdArray		�û�ID��
	 * @return				���ز���������
	 * @throws Exception
	 */
	public Boolean saveRoleUserInfo(String roleId, Object[] userIdArray)throws Exception{
		StringBuilder sb = new StringBuilder("");
		try {
			List<String> sqlList = new ArrayList<String>();
			for (int i=0; i<userIdArray.length; i++){
				String userId = (String)userIdArray[i];
				sb.setLength(0);
				sb.append("INSERT INTO ").append(TableName.TABLE_SYS_USER_ROLE).append("(USERID,ROLEID) VALUES('")
				  .append(userId).append("','")
				  .append(roleId).append("')");
				sqlList.add(sb.toString());
			}
			if (0 != sqlList.size()){
				DBTemplate.getInstance(DataSourceName.DATASOURCE_DM).batchExecuteWithTransaction(sqlList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return true;
	}
	
	/**
	 * �����û���Ӧ��ɫ��Ϣ��
	 * @param userId		�û�ID
	 * @return				�����û���Ӧ��ɫ��Ϣ��
	 * @throws Exception
	 */
	public List<Map<String, Object>> findRoleListIdByUserId(String userId)throws Exception{
		List<Map<String, Object>> roleMapList = null;
		StringBuilder sb = new StringBuilder("");
		try {
			sb.setLength(0);
			sb.append("SELECT * FROM ").append(TableName.TABLE_SYS_ROLE).append(" WHERE ID IN(").append("SELECT ROLEID FROM ").append(TableName.TABLE_SYS_USER_ROLE)
			  .append(" WHERE USERID='").append(userId).append("')");
			roleMapList = DBTemplate.getInstance(DataSourceName.DATASOURCE_DM).getResultMapList(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return roleMapList;
	}
	
	/**
	 * �����û���ɫ��ϵ��Ϣ
	 * @param userId		�û�ID
	 * @param roleIdArray		�����˵�ID��
	 * @return				���ز���������
	 * @throws Exception
	 */
	public Boolean saveUserRoleInfo(String userId, Object[] roleIdArray)throws Exception{
		StringBuilder sb = new StringBuilder("");
		try {
			List<String> sqlList = new ArrayList<String>();
			for (int i=0; i<roleIdArray.length; i++){
				String roleId = (String)roleIdArray[i];
				sb.setLength(0);
				sb.append("INSERT INTO ").append(TableName.TABLE_SYS_USER_ROLE).append("(USERID,ROLEID) VALUES('")
				  .append(userId).append("','")
				  .append(roleId).append("')");
				sqlList.add(sb.toString());
			}
			if (0 != sqlList.size()){
				DBTemplate.getInstance(DataSourceName.DATASOURCE_DM).batchExecuteWithTransaction(sqlList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return true;
	}
}
