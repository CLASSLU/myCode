package com.sdjxd.commom.yhzdy;

import java.sql.SQLException;

import com.sdjxd.pms.platform.data.DbOper;
import com.sdjxd.pms.platform.organize.User;
import com.sdjxd.pms.platform.tool.Guid;

public class Yhzdy {
	/**
	 * 
	 * @param cellid
	 *            对应的元件ID
	 * @param patternid
	 *            对应的票面模板ID
	 * @param method
	 *            生成用户自定义对象的方法
	 * @param userCellId
	 *            首页用户自定义元件ID
	 * @return
	 */
	public static boolean qiyong(int cellid, String patternid, String method,
			String userCellId) {
		String sheet = Guid.create();
		// 为当前用户插入自定义元件
		String sqlinsert = "INSERT INTO [S15].XMGG_ZDY_SYZDY (KJBH,BDBM,SHEETID,ZDYXX,SYYJBM,CREATEUSERID)VALUES('"
				+ cellid
				+ "','"
				+ patternid
				+ "','"
				+ sheet
				+ "','"
				+ method
				+ "','"
				+ userCellId
				+ "','"
				+ User.getCurrentUser().getId()
				+ "')";
		// 删除用户原有的自定义元件
		String sqldel = "DELETE FROM [S15].XMGG_ZDY_SYZDY WHERE SYYJBM='"
				+ userCellId + "' AND CREATEUSERID = '"
				+ User.getCurrentUser().getId() + "'";
		String[] sqlArray = new String[2];
		sqlArray[0] = sqldel;
		sqlArray[1] = sqlinsert;
		try {
			return DbOper.executeNonQuery(sqlArray) == -1 ? false : true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
