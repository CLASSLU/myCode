package com.sdjxd.common.flow;

import com.sdjxd.pms.platform.organize.User;

public class FolwOperation {
	private static FlowDao dao = new FlowDao();

	/**
	 * �鿴�Ƿ��е�ǰƱ��δ���л�ǩ�Ļ�ǩ��¼
	 * 
	 * @param sheetId
	 * @return
	 */
	public static String isHQ(String sheetId, String flowInstanceId, int nodeInstanceId) {
		return dao.isHQ(sheetId, flowInstanceId, nodeInstanceId);
	}

	/**
	 * ɾ������
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
