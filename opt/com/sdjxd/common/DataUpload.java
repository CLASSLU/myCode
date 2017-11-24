package com.sdjxd.common;

import java.util.ArrayList;
import java.util.List;

import javax.sql.RowSet;

import com.sdjxd.pms.platform.base.Global;
import com.sdjxd.pms.platform.data.DbOper;

public class DataUpload {

	public static String hsedbuser = Global.getConfig("hsedbuser");

	//技术报告上报
	public static boolean jsbg_upload(String[] sheetids) {
		List<String> list = new ArrayList<String>();
		try {
			for (String sheetid : sheetids) {
				String strSql = "SELECT CREATEORGID AS ORG_ID, "
						+ "TJR AS USER_ID, "
						+ "JSBGMC AS KEYPARTTECHNICREPORT_DES, "
						+ "TJSJ AS KEYPARTTECHNICREPORT_SUBTIME, "
						+ "(SELECT U_ERPCODE "
						+ "FROM JXD7_XT_ORGANISE "
						+ "WHERE ORGANISEID = "
						+ "(SELECT COMPID FROM JXD7_XT_USER WHERE USERID = A.TJR)) AS COMCODE, "
						+ "'0' AS U_STATE, " + "GJBWYHBW AS KEYPART_ALLNAME, "
						+ "ND AS U_YEAR "
						+ "FROM TB_HSE_ZZ_JSBG A WHERE SHEETID = '" + sheetid
						+ "' AND UPLOADFLAG != '1'";

				RowSet rs = DbOper.executeQuery(strSql);
				while (rs.next()) {
					strSql = "INSERT INTO [HSE].KEYPART_TECHNICREPORT_TB ("
							+ "KEYPARTTECHNICREPORT_ID, "
							+ "ORG_ID, "
							+ "USER_ID, "
							+ "KEYPARTTECHNICREPORT_DES, "
							+ "KEYPARTTECHNICREPORT_SUBTIME, "
							+ "COMCODE, "
							+ "U_STATE, "
							+ "KEYPART_ALLNAME, "
							+ "U_YEAR) VALUES ("
							+ "[HSE].SEQKEYPART_TECHNICREPORT_TB.NEXTVAL, '"
							+ rs.getString("ORG_ID") + "', '"
							+ rs.getString("USER_ID") + "', '"
							+ rs.getString("KEYPARTTECHNICREPORT_DES") + "', "
							+ "TO_DATE('"
							+ rs.getString("KEYPARTTECHNICREPORT_SUBTIME")
							+ "', 'yyyy-mm-dd hh24:mi:ss'), '"
							+ rs.getString("COMCODE") + "', '"
							+ rs.getString("U_STATE") + "', '"
							+ rs.getString("KEYPART_ALLNAME") + "', '"
							+ rs.getString("U_YEAR") + "')";

					list.add(strSql.replace("[HSE]", hsedbuser));

					strSql = "INSERT INTO [HSE].ZX_FILE_TB (FILE_BM, TABLE_NAME, PRIMARYKEY_VALUE, FILE_MC, CREATE_ID, CREATE_TIME, FILE_TYPE, FILE_DATE) "
							+ "SELECT [HSE].SEQZX_FILE_TB.NEXTVAL, "
							+ "'KEYPART_TECHNICREPORT_TB', "
							+ "[HSE].SEQKEYPART_TECHNICREPORT_TB.CURRVAL, "
							+ "FILENAME, "
							+ "CREATEUSERID, "
							+ "TO_DATE(CREATEDATE, 'yyyy-mm-dd hh24:mi:ss'), "
							+ "'技术报告附件', "
							+ "FILEDATA FROM JXD7_PM_FILE WHERE SHEETID = '"
							+ sheetid + "'";
					list.add(strSql.replace("[HSE]", hsedbuser));
					
					strSql = "INSERT INTO [HSE].BASE_TEXT_TB (TEXT_ID, TEXT_TABLENAME, TEXT_KEYID, TEXT_TEXT, U_REGISTER, U_REGISTIME, U_TYPE, TEXT_SUBID) " +
							"SELECT [HSE].SEQBASE_TEXT_TB.NEXTVAL, " +
							"'KEYPART_TECHNICREPORT_TB', " +
							"[HSE].SEQKEYPART_TECHNICREPORT_TB.CURRVAL, " +
							"NR, " +
							"BXR, " +
							"TO_DATE(CREATEDATE, 'yyyy-mm-dd hh24:mi:ss'), " +
							"SHEETGUID, '0' FROM TB_HSE_ZZ_BGWD WHERE BGID = '" + sheetid + "'";
					list.add(strSql.replace("[HSE]", hsedbuser));
				}
				list.add("UPDATE TB_HSE_ZZ_JSBG SET UPLOADFLAG = '1' WHERE SHEETID = '" + sheetid + "'");
			}
			DbOper.executeNonQuery(list);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		// DbOper.executeNonQuery(list);
		return true;
	}
}
