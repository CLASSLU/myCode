package com.sdjxd.common.flow;

import com.sdjxd.pms.platform.organize.User;

public class FlowSql {
	/**
	 * �õ�ָ������ǰ�˻�û�л�ǩ�Ļ�ǩ��Ϣ
	 * 
	 * @param sheetId
	 * @return
	 */
	public String isHQ(String sheetId) {
		StringBuffer sql = new StringBuffer(
				"SELECT * FROM [S15].XMGG_FLOW_HQJL T WHERE T.DYBDID = '");
		sql.append(sheetId);
		sql.append("' and T.HQRID = '");
		sql.append(User.getCurrentUser().getId());
		sql.append("' AND T.ZT IS NULL");
		return sql.toString();
	}

	public String deleteHqWaitDo(String operid, String userId) {
		StringBuffer sql = new StringBuffer(
				"delete [S].jxd7_xt_waitdo t where t.operid = '").append(operid)
				.append("' and t.opername like '%��ǩ%' and t.receiver = '")
				.append(userId).append("'");

		return sql.toString();
	}

	public String isNeedSH(String flowInstanceId, int nodeInstanceId)
	{
		StringBuffer sql = new StringBuffer(
		"select waitdoid from [S].jxd7_xt_waitdo t where t.operid='");
		
		sql.append(flowInstanceId).append(nodeInstanceId);
		sql.append("' and t.opername not like '%��ǩ%' and t.receiver = '");
		sql.append(User.getCurrentUser().getId());
		sql.append("'");
		
		return sql.toString();
	}
}
