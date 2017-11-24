package com.sdjxd.common.file;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sdjxd.pms.platform.data.DbOper;

/**
 * �ϴ�����Ԫ������
 * @author Key
 */
public class FileUploadCell 
{
	/**
	 * ��ո����ͱ�ʵ��֮��Ĺ���
	 * @param fileIds
	 * @throws SQLException
	 */
	public static void removeRelationWithFormInstance(String[] fileIds) throws SQLException
	{
		List sqlList = new ArrayList();
		for(String id:fileIds)
		{
			sqlList.add("UPDATE [S].JXD7_PM_FILE SET SHEETID=REPLACE(SHEETID,'-','=') WHERE ID='"+id+"'");
		}
		if(sqlList.size()>0)
			DbOper.executeNonQuery(sqlList);
	}
}
