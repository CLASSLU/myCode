package com.sdjxd.function.db.sql;

public class SqlHelper
{
	public String getMethod(String id)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("select T.*FROM TB_HSE_HBGL_JCFF T WHERE SHEETID='").append(id).append("'");
		return sb.toString();
	}
	public String getArgs(String mId)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT T.* FROM TB_HSE_HBGL_JCFF_CS T WHERE T.FUNCTION_ID='")
			.append(mId).append("'");
		return sb.toString();
	}
}
