package com.sdjxd.common.text;

import javax.sql.RowSet;

import com.sdjxd.pms.platform.data.DbOper;

public class Text {
	public static String getTextByValue(String tablename, String idfield, String namefield, String value){
		String result = "";
		try{
			String strSql = "SELECT T." + namefield + " FROM [S]." + tablename + " T WHERE T." + idfield + " IN ('" + value.replace(",", "','") + "') ORDER BY T.SHOWORDER";
			RowSet rs = DbOper.executeQuery(strSql);
			while(rs.next()){
				result += rs.getString(namefield) + ",";
			}
			if(result.length() > 0){
				result = result.substring(0, result.length() - 1);
			}
		}catch(Exception err){
			err.printStackTrace();
		}
		return result;
	}
}
