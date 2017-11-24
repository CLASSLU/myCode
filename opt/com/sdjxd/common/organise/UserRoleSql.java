package com.sdjxd.common.organise;

public class UserRoleSql
{
  public String getUserRoleDeleteSql(String userID)
  {
    StringBuffer sql = new StringBuffer();

    sql.append("delete from [S].JXD7_XT_USERROLE where USERID='");
    sql.append(userID);
    sql.append("'");

    return sql.toString();
  }

  public String getRoleUserDeleteSql(String roleID)
  {
    StringBuffer sql = new StringBuffer();

    sql.append("delete from [S].JXD7_XT_USERROLE where ROLEID='");
    sql.append(roleID);
    sql.append("'");

    return sql.toString();
  }

  public String getDeleteSql(String userID, String roleID)
  {
    StringBuffer sql = new StringBuffer();

    sql.append("delete from [S].JXD7_XT_USERROLE where ROLEID='");
    sql.append(roleID);
    sql.append("' and USERID='");
    sql.append(userID);
    sql.append("'");

    return sql.toString();
  }

  public String[] getUserRoleAddSql(String userID, String[] roleID)
  {
    StringBuffer temp = new StringBuffer();
    String[] sql = new String[roleID.length];

    for (int i = 0; i < roleID.length; i++)
    {
      temp.append("insert into [S].JXD7_XT_USERROLE(USERID, ROLEID) values('");
      temp.append(userID);
      temp.append("', '");
      temp.append(roleID[i]);
      temp.append("')");
      sql[i] = temp.toString();
      temp.setLength(0);
    }

    return sql;
  }

  public String[] getRoleUserAddSql(String roleID, String[] userID)
  {
    StringBuffer temp = new StringBuffer();
    String[] sql = new String[userID.length];

    for (int i = 0; i < userID.length; i++)
    {
      temp.append("insert into [S].JXD7_XT_USERROLE(USERID, ROLEID) values('");
      temp.append(userID[i]);
      temp.append("', '");
      temp.append(roleID);
      temp.append("')");
      sql[i] = temp.toString();
      temp.setLength(0);
    }

    return sql;
  }

  public String getRoleSql(String userID)
  {
    StringBuffer sql = new StringBuffer();

    sql.append("select ROLEID from [S].JXD7_XT_USERROLE where USERID='");
    sql.append(userID);
    sql.append("'");

    return sql.toString();
  }

  public String getUserSql(String roleID)
  {
    StringBuffer sql = new StringBuffer();

    sql.append("select USERID from [S].JXD7_XT_USERROLE where ROLEID='");
    sql.append(roleID);
    sql.append("'");

    return sql.toString();
  }
  /**
   * 查询角色用户
   * @param userId
   * @return
   */
  public String getUserManageRoleUsers(String userId)
  {
	  StringBuffer sb = new StringBuffer();
	  sb.append("select sheetname as rolename, organisename, strcat(username)as username "+
						 " from (select s0.sheetname,s1.roleid,s1.userid,s2.username,s3.organiseid,s3.path as organisename,s0.showorder as rshoworder,s3.organiselevel as oshoworder "+
						          "from jxd7_xt_role s0,jxd7_xt_userrole s1,jxd7_xt_user s2,jxd7_xt_orgleval s3 "+
						         "where s0.sheetid = s1.roleid  and s2.userid = s1.userid and s3.organiseid = s2.deptid "+
						           "and s2.deptid in(select t2.organid from jxd7_xt_userrole t1, jxd7_xt_rolemorgan t2 "+
						                 "where t1.userid = '").append(userId).append(
						                   "' and t2.roleid = t1.roleid)"+
						         "order by s2.showorder asc)"+
						        "group by sheetname, organisename,rshoworder,oshoworder order by rshoworder,oshoworder asc");
	  return sb.toString();
  }
  public String getUserManageUserRoles(String userId)
  {
	  StringBuffer sb = new StringBuffer();
	  sb.append("select organisename,username,strcat(sheetname) as rolename,oshoworder,ushoworder,orglevel "+
						  "from (select s0.sheetname,s1.roleid,s1.userid,s2.username,s3.organiseid,s3.path as organisename,s2.showorder as ushoworder,s3.organiselevel as orglevel,s3.showorder as oshoworder "+
						          "from jxd7_xt_role s0,jxd7_xt_userrole s1,jxd7_xt_user s2,jxd7_xt_orgleval s3 "+
						         "where s0.sheetid = s1.roleid and s2.userid = s1.userid and s3.organiseid = s2.deptid and s3.organiseid in "+
						              "(select t2.organid from jxd7_xt_userrole t1, jxd7_xt_rolemorgan t2  "+
						             "  where t1.userid = '").append(userId).append("' and t2.roleid = t1.roleid)"+
						  		" order by  s0.showorder asc)"+
						" group by organisename,username,ushoworder,oshoworder,orglevel order by orglevel,oshoworder,ushoworder asc");
	  return sb.toString();
  }
}