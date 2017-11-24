package com.sdjxd.common.organise;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sdjxd.pms.platform.data.DbOper;
import com.sdjxd.pms.platform.organize.User;

public class UserRole {
	public static Logger log = Logger.getLogger(UserRole.class.getName());
	
	private static UserRoleSql sqlHelper = new UserRoleSql();
	
	/**
	 * 删除角色中的人员信息
	 * 
	 * @param propertyName
	 * @return
	 */
	public static boolean deleteRoleUser(String[] userRole) {
		if (userRole.length < 1)
	    {
	    	return false;
	    }

	    String roleID = userRole[0];
	    String[] sql = new String[userRole.length-1];

	    for (int i = 1; i < userRole.length; i++)
	    {
	    	sql[(i-1)] = sqlHelper.getDeleteSql(userRole[i], roleID);
	    }

	    try
	    {
	      DbOper.executeNonQuery(sql);
	    }
	    catch (SQLException e)
	    {
	      e.printStackTrace();
	      return false;
	    }
	    
		return true;
	}
	/**
	 * 查询角色用户和用户角色
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList[] getUserManageRolesAndUsers(String userId) throws SQLException
	{
		ArrayList[] arr = new ArrayList[2];
		if(userId == null || userId.equals(""))
			userId = User.getCurrentUser().getId();
		arr[0] = DbOper.executeList(sqlHelper.getUserManageRoleUsers(userId));
		arr[1] = DbOper.executeList(sqlHelper.getUserManageUserRoles(userId));
		return arr;
	}
}
