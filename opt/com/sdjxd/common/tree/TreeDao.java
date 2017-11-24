package com.sdjxd.common.tree;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.RowSet;

import com.sdjxd.common.tree.model.DoubleTreeSelectedData;
import com.sdjxd.pms.platform.data.DbOper;

public class TreeDao {
	TreeSql sqlHelper = new TreeSql();

	/**
	 * 得到选中的结点列表
	 * 
	 * @param sql
	 *            要求第一列为被选中树的objectId
	 * @return
	 */
	public List<String> getSelectNodeObjectIds(String sql) {
		List<String> result = new ArrayList<String>();
		RowSet rs;
		try {
			rs = DbOper.executeQuery(sql);
			while (rs.next()) {
				result.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;

	}

	public boolean saveTowTreeSelectedNodes(DoubleTreeSelectedData dtsd) {
		List<String> sqlList = sqlHelper.saveTowTreeSelectedNodes(dtsd);
		try {
			return DbOper.executeNonQuery(sqlList) == -1 ? false : true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据组织机构ID，得到自二级节点到本级的名称连接信息
	 * 
	 * @param orgid
	 * @return
	 */
	public String getOrganiseLevelName(String orgid)
	{
		String strLevelName = "";
		RowSet rs;
		String sql = "select substr(sys_connect_by_path(replace(t.organisename,'--','——'), '--'), 3) path from [S].jxd7_xt_organise t " +
			"where t.organiseid='" + orgid + "' start with t.preorganiseid = (select o.organiseid " +
			"from [S].jxd7_xt_organise o where o.preorganiseid is null) " +
			"connect by prior t.organiseid = t.preorganiseid";
		
		try {
			rs = DbOper.executeQuery(sql);
			while (rs.next()) {
				strLevelName = rs.getString("path");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		strLevelName = strLevelName.replace("--", "-");
		return strLevelName;
	}
}
