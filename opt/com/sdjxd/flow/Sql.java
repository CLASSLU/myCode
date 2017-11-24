package com.sdjxd.flow;

import java.util.List;

import com.sdjxd.pms.platform.organize.User;
import com.sdjxd.pms.platform.tool.Guid;
import com.sdjxd.pms.platform.tool.StringTool;
import com.sdjxd.pms.platform.workflow.model.FlowOperatorBean;
import com.sdjxd.pms.platform.workflow.service.FlowOperator;

public class Sql {
	/**
	 * 
	 * @param formInstanceId
	 * @param f
	 * @param t
	 * @return
	 */
	public String saveStartLog(String formInstanceId, User f, FlowOperator t,
			boolean isUntreadNode) {

		StringBuffer sql = new StringBuffer(
				"insert into [S15].XMGG_FOLW_SHJL (SHEETID,DATASTATUSID,FSR,SPRID,JSRID,JSR,FBDID,ZT,SHYJ,CLSJ,CREATEDATE,SHOWORDER) VALUES ('");
		sql.append(Guid.create()).append("',1,'");
		sql.append(f.getName()).append("','").append(f.getId()).append("','");
		sql.append(t.getOperatorId()).append("','").append(t.getOperatorName())
				.append("','");
		sql.append(formInstanceId).append("','");
		if (isUntreadNode) {
			sql.append("退回提交");
		} else {
			sql.append("提交");
		}
		sql.append("','");
		if (isUntreadNode) {
			sql.append("退回提交审批");
		} else {
			sql.append("提交审批");
		}
		sql
				.append("',TO_CHAR(SYSDATE,'yyyy-mm-dd hh24:mi:ss'),TO_CHAR(SYSDATE,'yyyy-mm-dd hh24:mi:ss'),(SELECT MAX(SHOWORDER)+1 FROM [S15].XMGG_FOLW_SHJL))");
		return sql.toString();
	}
	
	/**
	 * 
	 * @param formInstanceId
	 * @param f
	 * @param t
	 * @return
	 */
	public String saveSendDownDept(String formInstanceId, FlowOperator t) {

		StringBuffer sql = new StringBuffer();
		
		sql.append("update TB_HSE_CHK_CHK set NFGS_FLOW_REDEPTID=");
		sql.append("(select s.deptid from jxd7_xt_user s where s.userid='");
		sql.append(t.getOperatorId());
		sql.append("')");
		sql.append(" where sheetid='");
		sql.append(formInstanceId).append("'");
		
		return sql.toString();
	}

	/**
	 * 得到当前结点是否退回过
	 * 
	 * @param flowInstanceId
	 * @param nodeId
	 * @return
	 */
	public String isUntreadNode(String flowInstanceId, String nodeId) {
		StringBuffer sql = new StringBuffer(
				"select 1 from jxd7_wf_untread u where u.flowinstanceid = '");
		sql
				.append(flowInstanceId)
				.append(
						"' and u.nodeinstanceid in (select t.nodeinstanceid from jxd7_wf_nodeinstance t where t.flownodeid = '")
				.append(nodeId).append("')");

		return sql.toString();
	}

	public String getActorsList(String flowid, String flownodeid) {
		StringBuffer sql = new StringBuffer(
				"SELECT ACTORDATA FROM JXD7_WF_ACTOR A WHERE A.FLOWID = '");
		sql.append(flowid).append("' AND A.FLOWNODEID =  '").append(flownodeid)
				.append("'");
		return sql.toString();
	}

	public String updateActors(String flowid, String flownodeid,
			String actordata) {
		//替换掉/r/n,替换单引号“‘”为两个单引号。
		actordata = actordata.replaceAll("'", "''");
		actordata = actordata.replaceAll("/r/n", " ");
		
		StringBuffer sql = new StringBuffer(
				"update JXD7_WF_ACTOR A set a.ACTORDATA = '").append(actordata)
				.append("'  WHERE A.FLOWID = '");
		sql.append(flowid).append("' AND A.FLOWNODEID =  '").append(flownodeid)
				.append("'");
		
		//String strSql = StringTool.replaceKeyWord(sql.toString());
		return sql.toString();
	}

	/**
	 * 是否为会签流程
	 * @param flowid
	 * @return
	 */
	public String getIsSignFlow(String flowid) {
		StringBuffer sql = new StringBuffer(
				"SELECT 1 FROM [S15].XMGG_FLOW_ISSIGN A WHERE A.FLOWID = '");
		sql.append(flowid).append("' AND A.FLAG =  '1'");
		return sql.toString();
	}
}
