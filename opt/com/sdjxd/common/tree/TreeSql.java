package com.sdjxd.common.tree;

import java.util.ArrayList;
import java.util.List;

import com.sdjxd.common.tree.model.DoubleTreeSelectedData;
import com.sdjxd.pms.platform.data.DbOper;
import com.sdjxd.pms.platform.tool.Guid;

public class TreeSql {
	public List<String> saveTowTreeSelectedNodes(DoubleTreeSelectedData dtsd) {
		List<String> sqlList = new ArrayList<String>();
		// 先清除数据
		StringBuffer delSql = new StringBuffer("delete ");
		delSql.append(DbOper.getSchema());
		delSql.append(dtsd.getTableName());
		delSql.append(" t where t.").append(dtsd.getFromCloumnName());
		delSql.append(" = '").append(dtsd.getFromTreeSelNodeObjectId()).append(
				"'");
		sqlList.add(delSql.toString());
		// 再添加数据
		for (String nObjectId : dtsd.getToTreeSelNodeObjectId()) {
			StringBuffer sqlBegin = new StringBuffer("insert into ").append(
					DbOper.getSchema()).append(dtsd.getTableName()).append(
					" (SHEETID,DATASTATUSID,").append(dtsd.getFromCloumnName())
					.append(",").append(dtsd.getToColumnName()).append(
							") values ('");
			sqlBegin.append(Guid.create());
			sqlBegin.append("',1,'").append(dtsd.getFromTreeSelNodeObjectId())
					.append("','");

			StringBuffer sql = new StringBuffer();
			sql.append(sqlBegin).append(nObjectId).append("')");
			sqlList.add(sql.toString());
		}

		return sqlList;
	}
}
