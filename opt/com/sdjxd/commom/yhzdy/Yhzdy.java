package com.sdjxd.commom.yhzdy;

import java.sql.SQLException;

import com.sdjxd.pms.platform.data.DbOper;
import com.sdjxd.pms.platform.organize.User;
import com.sdjxd.pms.platform.tool.Guid;

public class Yhzdy {
	/**
	 * 
	 * @param cellid
	 *            ��Ӧ��Ԫ��ID
	 * @param patternid
	 *            ��Ӧ��Ʊ��ģ��ID
	 * @param method
	 *            �����û��Զ������ķ���
	 * @param userCellId
	 *            ��ҳ�û��Զ���Ԫ��ID
	 * @return
	 */
	public static boolean qiyong(int cellid, String patternid, String method,
			String userCellId) {
		String sheet = Guid.create();
		// Ϊ��ǰ�û������Զ���Ԫ��
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
		// ɾ���û�ԭ�е��Զ���Ԫ��
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
