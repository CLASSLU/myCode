package com.sdjxd.common.flow;

import com.sdjxd.pms.platform.organize.User;

public class FolwOperation {
	private static FlowDao dao = new FlowDao();

	/**
	 * 查看是否有当前票面未进行会签的会签记录
	 * 
	 * @param sheetId
	 * @return
	 */
	public static String isHQ(String sheetId, String flowInstanceId, int nodeInstanceId) {
		return dao.isHQ(sheetId, flowInstanceId, nodeInstanceId);
	}

	/**
	 * 删除待办
	 * 
	 * @param instanceid
	 */
	public static void deleteWaitDo(String instanceid) {
		try {
			dao.deleteHqWaitDo(instanceid, User.getCurrentUser().getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
