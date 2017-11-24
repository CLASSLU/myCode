package com.sdjxd.common.export;

public class Sql {
	public String getWordFileId(String dybdid) {
		StringBuffer sql = new StringBuffer(
				"select WORDPTID FROM [S15].XMGG_HELP_HELPINFO T WHERE T.DYBDID = '");
		sql.append(dybdid);
		sql.append("'");
		return sql.toString();
	}
}
