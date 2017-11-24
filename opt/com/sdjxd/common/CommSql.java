package com.sdjxd.common;

import org.apache.log4j.Logger;

import com.sdjxd.pms.platform.base.Global;
import com.sdjxd.pms.platform.data.DbOper;
import com.sdjxd.pms.platform.organize.User;
import com.sdjxd.pms.platform.tool.Guid;

public class CommSql {
	public static Logger log = Logger.getLogger(CommSql.class.getName());

	/**
	 * 根据表名查找Table基本属性
	 * 
	 * @param tableName
	 *            表名
	 * @return
	 */
	public String getTableByName(String tableName) {
		StringBuffer sql = new StringBuffer();
		sql
				.append("SELECT TABLEID,TABLENAME,TABLEMSG,MODULEID,TABLETYPE,TABLENOTES,DATATYPE,SHOWNAMECOL,PKCOL,PICPATH,ISVIEW");
		sql.append(" FROM [S].JXD7_XT_SYSTABLEINFO");
		sql.append(" WHERE TABLENAME='").append(tableName).append("'");
		return sql.toString();
	}

	/**
	 * 得到部门信息
	 * 
	 * @return
	 */
	public String getOrganise(String organiseId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM ");
		sql.append("[S].").append("JXD7_XT_ORGANISE");
		sql.append(" WHERE DATASTATUSID = 1 AND ORGANISEID='").append(
				organiseId).append("'");
		return sql.toString();
	}

	/**
	 * 将部门名附加到用户上
	 * 
	 * @param userIds
	 * @return
	 */
	public String getUserOrgainse(String userIds) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT O.ORGANISENAME,O.SHOWORDER as OSHOWORDER,U.* FROM ");
		sql.append("[S].").append("JXD7_XT_ORGANISE O,").append(
				"[S].").append("JXD7_XT_USER U");
		sql.append(" WHERE U.DEPTID = O.ORGANISEID  AND (U.USERID IN (")
				.append(userIds).append(")) ORDER BY O.SHOWORDER,U.SHOWORDER");
		return sql.toString();
	}

	/**
	 * 得到用户信息
	 * 
	 * @param userIds
	 * @return
	 */
	public String getUserInfo(String roleIds) {
		User user = User.getCurrentUser();
		StringBuffer sql = new StringBuffer();
		/*sql
				.append("SELECT U.*,L.ORGANIZELEVAL,T.SHOWORDER as OSHOWORDER FROM ");
		sql.append(DbOper.getSchema()).append("JXD7_XT_USER U ,");
		sql.append(DbOper.getSchema()).append("JXD7_XT_ORGLEVAL L ,");
		sql.append(DbOper.getSchema()).append("JXD7_XT_ORGANISE T, ");
		sql.append(DbOper.getSchema()).append("JXD7_XT_USERROLE V ");
		sql.append(" WHERE u.DEPTID =T.ORGANISEID and L.ORGANISEID = T.ORGANISEID and U.USERID=V.USERID ")
				.append("AND V.ROLEID IN (")
				.append(roleIds).append(")");
				
				sql.append(" and (T.ORGANISEID in (select o.organiseid from jxd7_xt_organise o where o.preorganiseid in (select organiseid from jxd7_xt_organise start with organiseid='")
				.append(user.getDeptId())
				.append(
						"' connect by prior preorganiseid=organiseid) and (o.organisetype = 5 or o.organisetype=6)) or T.ORGANISEID in ( select organiseid from jxd7_xt_organise start with organiseid='")
				.append(user.getDeptId())
				.append("' connect by prior preorganiseid=organiseid)or T.ORGANISEID in (select o.organiseid from jxd7_xt_organise o ")
				.append("start with organiseid = (select organiseid from (SELECT organiseid FROM JXD7_XT_ORGANISE where organiseid='")
				.append(user.getDeptId())
				.append("' or organiseid = (SELECT preorganiseid FROM JXD7_XT_ORGANISE where organiseid='")
				.append(user.getDeptId())
				.append("' and (organisetype = 5 or organisetype = 6)) order by organiselevel) a where rownum=1) connect by prior organiseid = preorganiseid))")
				.append(Global.getConfig("list.developDeptFilter"))
				
				.append(
				" ORDER BY L.ORGANIZELEVAL,OSHOWORDER,u.SHOWORDER");//排序字段，先按层次,组织机构排序字段,用户排序字段*/
		sql.append("select m1.USERID,m1.deptid,m1.SHOWORDER, m1.USERNAME, tt.organisename,tt.organiseid,tt.organiselevel,tt.SHOWORDER as OSHOWORDER ")
			.append("from (select t0.* ")
			.append("          from jxd7_xt_organise t0, ")
			.append("               (select * ")
 			.append("                 from jxd7_xt_organise t1 ")
			.append("                 where (select organiselevel ")
			.append("                          from jxd7_xt_organise ")
			.append("                         where organiseid = '")
			.append(user.getDeptId()).append("') like ")
			.append("                       t1.organiselevel || '%') t2 ")
			.append("         where (t0.preorganiseid = t2.preorganiseid or ")
			.append("               t0.preorganiseid is null) ")
 			.append("          and (t0.organisetype = 5 or t0.organisetype = 6) ")
			.append("        union all ")
 			.append("       select t1.* ")
			.append("          from jxd7_xt_organise t1, jxd7_xt_organise seff ")
			.append("         where seff.organiseid = '")
			.append(user.getDeptId()).append("' ")
			.append("           and (seff.organiselevel like t1.organiselevel || '%' or ")
			.append("               t1.organiselevel like seff.organiselevel || '%' or ")
 			.append("              (t1.organiselevel like substr(seff.organiselevel,0,length(seff.organiselevel)-2) || '%' and ")
 			.append("              seff.organisetype in (5, 6))) ")
			.append("        ) tt, ")
 			.append("      jxd7_xt_user m1, ")
			.append("       jxd7_xt_userrole m2 ")
			.append(" where m1.username not like '%admin%' and m1.USERID = m2.userid ")
			.append("   and m2.roleid in (")
			.append(roleIds).append(") ")
			.append("   and m1.DEPTID = tt.organiseid  ORDER BY tt.organiselevel,OSHOWORDER,m1.SHOWORDER");
		return sql.toString();
	}

	/**
	 * 得到流程参与者部门(本级、上级、下级，同级不看)
	 * 
	 * @param userIds
	 * @return
	 */
	public String getFlowActorDeptsUpAndDown(String roleIds) {
		User user = User.getCurrentUser();
		StringBuffer sql = new StringBuffer();
		/*sql.append("SELECT T.ORGANISEID, T.ORGANISENAME,T.PREORGANISEID,T.ORGANISELEVEL  FROM JXD7_XT_ORGANISE T,JXD7_XT_ORGLEVAL L ")
			.append("WHERE T.ORGANISEID = L.ORGANISEID AND  T.ORGANISEID IN ");
		sql.append("(select distinct t1.organiseid from jxd7_xt_organise t1,(select t.organiselevel from jxd7_xt_organise t where t.organiseid in(")
			.append("SELECT DISTINCT U.DEPTID FROM JXD7_XT_USER U,jxd7_xt_userrole V WHERE U.userid=V.userid and V.roleid in (")
			.append(roleIds)
			.append("))) t2 where t2.organiselevel like t1.organiselevel||'%')");
		sql.append(" and exists (select s0.organiseid from jxd7_xt_organise s0,(select * from jxd7_xt_organise s1 where ") 
			.append(" (select organiselevel from jxd7_xt_organise ") 
			.append(" where organiseid='")
			.append(user.getDeptId())
			.append("') like s1.organiselevel||'%'") 
			.append(" ) s2") 
			.append(" where (s0.preorganiseid = s2.preorganiseid or s0.preorganiseid is null)") 
			.append("  and (s0.organisetype = 5 or s0.organisetype = 6)") 
			.append(" union ") 
			.append(" select s1.* from jxd7_xt_organise s1, jxd7_xt_organise seff where  seff.organiseid='")
			.append(user.getDeptId())
			.append("'") 
			.append(" and (seff.organiselevel like  t1.organiselevel||'%'") 
			.append(" or s1.organiselevel like seff.organiselevel||'%'") 
			.append(" or (s1.preorganiseid=seff.preorganiseid and seff.organisetype in (5, 6)))") 
				.append(Global.getConfig("list.developDeptFilter")).append(" ORDER BY L.ORGANIZELEVAL, T.SHOWORDER");*/
		sql.append("select distinct tt.organiselevel, tt.organiseid, tt.organisename,tt.PREORGANISEID,tt.SHOWORDER ")
			.append("from (")
			.append("select aa.* from jxd7_xt_organise aa,")
			.append("(select t0.* ")
			.append("from jxd7_xt_organise t0, ")
			.append("(select * ")
			.append("from jxd7_xt_organise t1 ")
			.append("where (select organiselevel ")
			.append("from jxd7_xt_organise ")
			.append("where organiseid = '")
			.append(user.getDeptId()).append("') like ")
			.append("t1.organiselevel || '%') t2 ")
			.append("where (t0.preorganiseid = t2.preorganiseid or ")
			.append("t0.preorganiseid is null) ")
			.append("and (t0.organisetype = 5 or t0.organisetype = 6)) bb ")
	 		.append("where aa.organiseid=bb.organiseid or (aa.organiselevel like bb.organiselevel||'%' and (aa.organisetype = 5 or aa.organisetype = 6))")
//			.append("from jxd7_xt_organise t0, ")
//			.append("(select * ")
//			.append("from jxd7_xt_organise t1 ")
//			.append("where (select organiselevel ")
//			.append("from jxd7_xt_organise ")
//			.append("where organiseid = '")
//			.append(user.getDeptId()).append("') like ")
//			.append("t1.organiselevel || '%') t2 ")
//			.append("where (t0.preorganiseid = t2.preorganiseid or ")
//			.append("t0.preorganiseid is null) ")
//			.append("and (t0.organisetype = 5 or t0.organisetype = 6) ")
			.append("union all ")
			.append("select t1.* ")
			.append("from jxd7_xt_organise t1, jxd7_xt_organise seff ")
			.append("where seff.organiseid = '")
			.append(user.getDeptId()).append("' ")
			.append("and (seff.organiselevel like t1.organiselevel || '%' ")
			.append("or t1.organiselevel like (SELECT distinct a2.organiselevel ")
			.append("FROM JXD7_XT_ORGANISE a1, JXD7_XT_ORGANISE a2 ")
			.append("where a1.organiseid = '")
			.append(user.getDeptId()).append("' ")
			.append("and ((a2.organiseid = a1.organiseid and ")
			.append("(a1.organisetype != 5 and a1.organisetype != 6)) or ")
			.append("((a1.organisetype = 5 or a1.organisetype = 6) and ")
			.append("a2.organiseid = ")
			.append("(select organiseid ")
			.append("from (select o.organiseid ")
			.append("from jxd7_xt_organise o ")
			.append("where (o.organisetype != 5 and o.organisetype != 6) ")
			.append("start with o.ORGANISEID = '")
			.append(user.getDeptId()).append("' ")
			.append("connect by prior o.preorganiseid = o.organiseid ")
			.append("order by o.organiselevel desc) ")
			.append("where rownum = 1)))) || '%'  or  ")
			.append("(t1.preorganiseid=seff.preorganiseid and  ")
			.append("t1.organisetype in (5, 6))) ")
//			.append("select t1.* ")
//			.append("from jxd7_xt_organise t1, jxd7_xt_organise seff ")
//			.append("where seff.organiseid = '")
//			.append(user.getDeptId()).append("' ")
//			.append("and (seff.organiselevel like t1.organiselevel || '%' or ")
//			.append("t1.organiselevel like seff.organiselevel || '%' or ")
//			.append("(t1.organiselevel like substr(seff.organiselevel,0,length(seff.organiselevel)-2) || '%' and ")
//			.append("t1.organisetype in (5, 6))) ")
			.append(") tt, ")
			.append("(select m0.organiselevel,m0.organisename ")
			.append("from jxd7_xt_organise m0, jxd7_xt_user m1, jxd7_xt_userrole m2 ")
			.append("where m0.organiseid = m1.DEPTID ")
			.append("and m1.USERID = m2.userid ")
			.append("and m2.roleid in (")
			.append(roleIds).append(")) ss ")
			.append("where ss.organisename<>'管理员' and ss.organiselevel like tt.organiselevel || '%' ")
			.append("order by tt.organiselevel,tt.SHOWORDER");
		
		return sql.toString();
	}
	
	/**
	 * 得到流程参与者部门(本级及上级)
	 * 
	 * @param userIds
	 * @return
	 */
	public String getFlowActorDepts(String roleIds) {
		User user = User.getCurrentUser();
		StringBuffer sql = new StringBuffer();
		
		sql.append("select distinct tt.organiselevel, tt.organiseid, tt.organisename,tt.PREORGANISEID,tt.SHOWORDER ")
		.append("from (")
		.append("select aa.* from jxd7_xt_organise aa,")
		.append("(select t0.* ")
		.append("from jxd7_xt_organise t0, ")
		.append("(select * ")
		.append("from jxd7_xt_organise t1 ")
		.append("where (select organiselevel ")
		.append("from jxd7_xt_organise ")
		.append("where organiseid = '")
		.append(user.getDeptId()).append("') like ")
		.append("t1.organiselevel || '%') t2 ")
		.append("where (t0.preorganiseid = t2.preorganiseid or ")
		.append("t0.preorganiseid is null) ")
		.append("and (t0.organisetype = 5 or t0.organisetype = 6)) bb")
 		.append(" where aa.organiseid=bb.organiseid or (aa.organiselevel like bb.organiselevel||'%' and (aa.organisetype = 5 or aa.organisetype = 6))")
//		select t0.* ")
//		.append("from jxd7_xt_organise t0, ")
//		.append("(select * ")
//		.append("from jxd7_xt_organise t1 ")
//		.append("where (select organiselevel ")
//		.append("from jxd7_xt_organise ")
//		.append("where organiseid = '")
//		.append(user.getDeptId()).append("') like ")
//		.append("t1.organiselevel || '%') t2 ")
//		.append("where (t0.preorganiseid = t2.preorganiseid or ")
//		.append("t0.preorganiseid is null) ")
//		.append("and (t0.organisetype = 5 or t0.organisetype = 6) ")
		.append("union all ")
		.append("select t1.* ")
		.append("from jxd7_xt_organise t1, jxd7_xt_organise seff ")
		.append("where seff.organiseid = '")
		.append(user.getDeptId()).append("' ")
		.append("and (seff.organiselevel like t1.organiselevel || '%')")
//		//.append("t1.organiselevel like seff.organiselevel || '%' or ")
		//.append("(t1.organiselevel like substr(seff.organiselevel,0,length(seff.organiselevel)-2) || '%' and ")
		//.append("seff.organisetype in (5, 6))) ")
		.append(") tt, ")
		.append("(select m0.organiselevel,m0.organisename ")
		.append("from jxd7_xt_organise m0, jxd7_xt_user m1, jxd7_xt_userrole m2 ")
		.append("where m0.organiseid = m1.DEPTID ")
		.append("and m1.USERID = m2.userid ")
		.append("and m2.roleid in (")
		.append(roleIds).append(")) ss ")
		.append("where ss.organisename<>'管理员' and ss.organiselevel like tt.organiselevel || '%' ")
		.append("order by tt.organiselevel,tt.SHOWORDER");

		return sql.toString();
	}
	/**
	 * 得到流程参与者部门(本级及上级)(根据隐患等级)
	 * 
	 * @param userIds
	 * @return
	 */
	public String getFlowActorDeptYhdj(String roleIds) {
		User user = User.getCurrentUser();
		StringBuffer sql = new StringBuffer();
		
		sql.append("select distinct tt.organiselevel, tt.organiseid, tt.organisename,tt.PREORGANISEID,tt.SHOWORDER ")
		.append("from (")
		.append("select aa.* from jxd7_xt_organise aa,")
		.append("(select t0.* ")
		.append("from jxd7_xt_organise t0, ")
		.append("(select * ")
		.append("from jxd7_xt_organise t1 ")
		.append("where (select organiselevel ")
		.append("from jxd7_xt_organise ")
		.append("where organiseid = '")
		.append(user.getDeptId()).append("') like ")
		.append("t1.organiselevel || '%') t2 ")
		.append("where (t0.preorganiseid = t2.preorganiseid or ")
		.append("t0.preorganiseid is null) ")
		.append("and (t0.organisetype = 5 or t0.organisetype = 6)) bb")
 		.append(" where aa.organiseid=bb.organiseid or (aa.organiselevel like bb.organiselevel||'%' and (aa.organisetype = 5 or aa.organisetype = 6))")
		.append("union all ")
		.append("select t1.* ")
		.append("from jxd7_xt_organise t1, jxd7_xt_organise seff ")
		.append("where seff.organiseid = '")
		.append(user.getDeptId()).append("' ")
		.append("and t1.deplevel_id <3 and  (seff.organiselevel like t1.organiselevel || '%')")
		.append(") tt, ")
		.append("(select m0.organiselevel,m0.organisename ")
		.append("from jxd7_xt_organise m0, jxd7_xt_user m1, jxd7_xt_userrole m2 ")
		.append("where m0.organiseid = m1.DEPTID ")
		.append("and m1.USERID = m2.userid ")
		.append("and m2.roleid in (")
		.append(roleIds).append(")) ss ")
		.append("where  ss.organisename<>'管理员' and ss.organiselevel like tt.organiselevel || '%' ")
		.append("order by tt.organiselevel,tt.SHOWORDER");

		return sql.toString();
	}

	
	/**
	 * 得到流程参与者部门(本级及本级以下)
	 * 
	 * @param userIds
	 * @return
	 */
	public String getFlowActorDeptsDown(String roleIds) {
		User user = User.getCurrentUser();
		StringBuffer sql = new StringBuffer();
		
		sql.append("select distinct tt.organiselevel, tt.organiseid, tt.organisename,tt.PREORGANISEID,tt.SHOWORDER ")
		.append("from ( ")
		.append("select t1.* ")
		.append("from jxd7_xt_organise t1 ")
		.append("where T1.DATASTATUSID=1 and  t1.organisename<>'管理员' and t1.organiselevel like (SELECT distinct a2.organiselevel ")
			.append("FROM JXD7_XT_ORGANISE a1, JXD7_XT_ORGANISE a2 ")
			.append("where a1.organiseid = '")
			.append(user.getDeptId()).append("' ")
			.append("and ((a2.organiseid = a1.organiseid and ")
			.append("(a1.organisetype != 5 and a1.organisetype != 6)) or ")
			.append("((a1.organisetype = 5 or a1.organisetype = 6) and ")
			.append("a2.organiseid = ")
			.append("(select organiseid ")
			.append("from (select o.organiseid ")
			.append("from jxd7_xt_organise o ")
			.append("where (o.organisetype != 5 and o.organisetype != 6) ")
			.append("start with o.ORGANISEID = '")
			.append(user.getDeptId()).append("' ")
			.append("connect by prior o.preorganiseid = o.organiseid ")
			.append("order by o.organiselevel desc) ")
			.append("where rownum = 1)))) || '%'")
		.append(") tt, ")
		.append("(select m0.organiselevel,m0.organisename ")
		.append("from jxd7_xt_organise m0, jxd7_xt_user m1, jxd7_xt_userrole m2 ")
		.append("where m0.organiseid = m1.DEPTID ")
		.append("and m1.USERID = m2.userid ")
		.append("and m2.roleid in (")
		.append(roleIds).append(")) ss ")
		.append("where ss.organisename<>'管理员' and ss.organiselevel like tt.organiselevel || '%' ")
		.append("order by tt.organiselevel,tt.SHOWORDER");
		
		return sql.toString();
	}
	
	/**
	 * 得到流程参与者部门(无过滤)
	 * 
	 * @param userIds
	 * @return
	 */
	public String getFlowActorDeptsNoFilter(String roleIds) {
		StringBuffer sql = new StringBuffer();
		
		sql
				.append(
						"select distinct tt.organiselevel,tt.organiseid,tt.organisename,tt.PREORGANISEID,tt.SHOWORDER ")
				.append("from jxd7_xt_organise tt, ")
				.append("(select distinct m0.organiselevel, m0.organisename ")
				.append(
						"from jxd7_xt_organise m0, jxd7_xt_user m1, jxd7_xt_userrole m2 ")
				.append(
						"where m0.organiseid <> '5A154DE6-E1F9-4FBA-9D7C-48A11EF7F1C6' ")
				.append("and m0.organiseid = m1.DEPTID ").append(
						"and m1.USERID = m2.userid ").append(
						"and m2.roleid in (").append(roleIds).append(")) ss ")
				.append("where ss.organiselevel like tt.organiselevel || '%' ")
				.append("order by tt.organiselevel, tt.SHOWORDER");
		
		return sql.toString();
	}

	/**
	 * 删除HSE系统中的通知信息
	 * 
	 * @param sheetid
	 * @return
	 */
	public String deleteHseNotice(String sheetid) {
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE ").append(Global.getConfig("hsedbuser"));
		sql.append(".OA_MAIL_TB t where t.mailcontent like'%").append(sheetid)
				.append("%' and mailintoid = '");
		sql.append(User.getCurrentUser().getId()).append("'");
		return sql.toString();
	}

	public String getUntreadCount(String flowInstanceId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(*) as count FROM ");
		sql.append("[S15].").append("XMGG_V_UNTREAD U");
		sql.append(" WHERE U.FLOWINSTANCEID = '").append(flowInstanceId)
				.append("'");
		return sql.toString();
	}

	/**
	 * 
	 * @param flowobjectid
	 * @return
	 */
	public String getFlowObjectInfo(String[] flowobjectid) {
		String flowobjectids = "";
		for (int i = 0; i < flowobjectid.length; i++) {
			if (flowobjectids.length() != 0) {
				flowobjectids += ",";
			}
			flowobjectids += "'" + flowobjectid[i] + "'";
		}

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT FLOWINSTANCEID, NODEINSTANCEID");
		sql.append(" FROM ").append("[S].").append(
				"JXD7_WF_NODEINSTANCE ");
		sql.append(" WHERE FLOWINSTANCEID IN(").append(flowobjectids).append(
				") AND RUNSTATUS = 0");

		return sql.toString();
	}

	public String getSaveHQJLSql(String sheetId, String userId, String operid) {
		StringBuffer sql = new StringBuffer("insert into ")
				.append("[S15].")
				.append(
						"XMGG_FLOW_HQJL (SHEETID,DATASTATUSID,HQRID,DYBDID,FSRID,CREATEDATE,SHOWORDER,WDOPERID) values ('");
		sql.append(Guid.create());
		sql.append("',1,'").append(userId).append("','");
		sql.append(sheetId).append("','");
		sql.append(User.getCurrentUser().getId());
		sql.append("',to_char(sysdate,'yyyy-MM-dd HH24:mi:ss'),(SELECT nvl(MAX(SHOWORDER),0)+1 FROM XMGG_FLOW_HQJL)");
		sql.append(",'").append(operid).append("')");
		
		return sql.toString();
	}

	/**
	 * 得到主版本号
	 * 
	 * @return
	 */
	public String getMainVersionSql() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT PVALUE FROM ");
		sql.append("[S].").append("JXD7_DM_SYSTEMPARAMETER");
		sql.append(" WHERE PID= 7");
		return sql.toString();
	}

	/**
	 * 得到次版本号
	 * 
	 * @return
	 */
	public String getMinorVersionSql() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT PVALUE FROM ");
		sql.append("[S].").append("JXD7_DM_SYSTEMPARAMETER");
		sql.append(" WHERE PID= 8");
		return sql.toString();
	}

	public String setVersionSql(String mainVersion, String minorVersion) {
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE ");
		sql.append("[S].").append(
				"JXD7_DM_SYSTEMPARAMETER SET PVALUE='").append(mainVersion);
		sql.append("' WHERE PID= 7;UPDATE ");
		sql.append("[S].").append(
				"JXD7_DM_SYSTEMPARAMETER SET PVALUE='").append(minorVersion);
		sql.append("' WHERE PID= 8");
		return sql.toString();
	}

	public String setMainVersionSql(String mainVersion) {
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE ");
		sql.append("[S].").append(
				"JXD7_DM_SYSTEMPARAMETER SET PVALUE='").append(mainVersion);
		sql.append("' WHERE PID= 7 ");
		return sql.toString();
	}

	public String setMinorVersionSql(String minorVersion) {
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE ");
		sql.append("[S].").append(
				"JXD7_DM_SYSTEMPARAMETER SET PVALUE='").append(minorVersion);
		sql.append("' WHERE PID= 8 ");
		return sql.toString();
	}

	public String setOldVersionSql(String oldVersion, String Version) {
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO ");
		sql.append("[S].").append(
				"XMGG_VERSION(SHEETID,YBBH,BBH,GXSJ) VALUES(NEWID(),'").append(
				oldVersion).append("','").append(Version);
		sql.append("',to_char(sysdate,'yyyy-MM-dd HH24:MI:SS'))");
		return sql.toString();
	}

	public String getUserByDeptId(String deptId) {
		String sql = "SELECT USERCODE,USERNAME FROM [S].JXD7_XT_USER WHERE DEPTID = '"
				+ deptId + "' AND DATASTATUSID = 1  ORDER BY SHOWORDER";
		return sql;
	}

	/**
	 * 得到数据库服务器当前系统时间
	 * 
	 * @return
	 */
	String getDBSysDate() {
		return "select to_char(sysdate,'yyyy-MM-dd HH24:mi:ss') from dual";
	}

	public String saveUserOpenPage(String userId, String menuName, String url) {
		StringBuffer sql = new StringBuffer("insert into ")
				.append("[S15].")
				.append(
						"XMGG_USEROPENPAGE (SHEETID,DATASTATUSID,YHID,MENUNAME,URL,CREATEDATE) values ('");
		sql.append(Guid.create());
		sql.append("',1,'").append(userId).append("','");
		sql.append(menuName).append("','");
		sql.append(url.replaceAll("'", "''"));
		sql.append("',to_char(sysdate,'yyyy-MM-dd HH24:mi:ss'))");
		return sql.toString();
	}
	
	/**
	 * 下一个子行政区域的层次,层次码
	 * @param regionId 当前行政区域ID
	 * @return
	 */
	public String getNextRegionNeedInfo(String regionId)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT REGIONLEVEL,REGIONLEVELCODE,(SELECT MAX(REGIONLEVELCODE) FROM XMGG_REGION S WHERE S.PREREGIONID=T.SHEETID)AS MAX_CHILDREN_LEVELCODE FROM XMGG_REGION T WHERE SHEETID='")
			.append(regionId).append('\'');
		return sb.toString();
	}
	
	/**
	 * 删除行政区域及下级行政区域
	 * @param levelCode 层次码
	 * @return
	 */
	public String deleteRegionCascade(String levelCode){
		StringBuffer sb = new StringBuffer();
		sb.append("DELETE FROM [S15].XMGG_REGION S WHERE S.REGIONLEVELCODE LIKE '")
			.append(levelCode).append("%'");
		return sb.toString();
	}
}
