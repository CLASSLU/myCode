package com.sdjxd.common.export;

import java.sql.SQLException;

import javax.sql.RowSet;

import com.sdjxd.pms.platform.data.DbOper;

public class Dao {
	Sql SqlHelper = new Sql();

	public String getWordFileId(String dybdid) {

		try {
			RowSet rs = DbOper.executeQuery(SqlHelper.getWordFileId(dybdid));
			if (rs.next()) {
				return rs.getString("WORDPTID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
