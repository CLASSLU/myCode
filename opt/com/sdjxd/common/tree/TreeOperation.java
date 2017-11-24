package com.sdjxd.common.tree;

import java.util.List;

import com.sdjxd.common.tree.model.DoubleTreeSelectedData;

public class TreeOperation {
	private static TreeDao dao = new TreeDao();

	/**
	 * 得到选中的结点列表
	 * 
	 * @param sql
	 *            要求第一列为被选中树的objectId
	 * @return
	 */
	public static List<String> getSelectNodeObjectIds(String sql) {
		return dao.getSelectNodeObjectIds(sql);
	}

	/**
	 * 保存选中的内容
	 * 
	 * @param dtsd
	 * @return
	 */
	public static boolean saveTowTreeSelectedNodes(DoubleTreeSelectedData dtsd) {
		return dao.saveTowTreeSelectedNodes(dtsd);
	}
	
	/**
	 * 根据组织机构ID，得到自二级节点到本级的名称连接信息
	 * 
	 * @param orgid
	 * @return
	 */
	public static String getOrganiseLevelName(String orgid) {
		return dao.getOrganiseLevelName(orgid);
	}
}
