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
	 * @param userId		用户ID
	 * @return				返回对应用户角色ID集
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
	 * @param roleId		角色ID
	 * @return				返回对应用户信息集
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
	 * 返回对应机构及下属机构下所有用户
	 * @param orgId		组织机构ID
	 * @param sqlCondition		用户过滤条件
	 * @return			返回对应机构及下属机构下所有用户
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
	 * 返回对应机构下所有用户
	 * @param orgId		组织机构ID
	 * @param sqlCondition		用户过滤条件
	 * @return			返回对应机构下所有用户
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
	 * 获取有效用户的组织机构ID
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
	 * 保存角色用户关系信息
	 * @param roleId		角色ID
	 * @param userIdArray		用户ID集
	 * @return				返回操作结果真假
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
	 * 返回用户对应角色信息集
	 * @param userId		用户ID
	 * @return				返回用户对应角色信息集
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
	 * 保存用户角色关系信息
	 * @param userId		用户ID
	 * @param roleIdArray		关联菜单ID集
	 * @return				返回操作结果真假
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
