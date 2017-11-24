package com.sdjxd.common.tree;

import java.util.List;

import com.sdjxd.common.tree.model.DoubleTreeSelectedData;

public class TreeOperation {
	private static TreeDao dao = new TreeDao();

	/**
	 * �õ�ѡ�еĽ���б�
	 * 
	 * @param sql
	 *            Ҫ���һ��Ϊ��ѡ������objectId
	 * @return
	 */
	public static List<String> getSelectNodeObjectIds(String sql) {
		return dao.getSelectNodeObjectIds(sql);
	}

	/**
	 * ����ѡ�е�����
	 * 
	 * @param dtsd
	 * @return
	 */
	public static boolean saveTowTreeSelectedNodes(DoubleTreeSelectedData dtsd) {
		return dao.saveTowTreeSelectedNodes(dtsd);
	}
	
	/**
	 * ������֯����ID���õ��Զ����ڵ㵽����������������Ϣ
	 * 
	 * @param orgid
	 * @return
	 */
	public static String getOrganiseLevelName(String orgid) {
		return dao.getOrganiseLevelName(orgid);
	}
}
