package com.sdjxd.common.flow;

import java.sql.SQLException;

import javax.sql.RowSet;

import org.apache.log4j.Logger;

import com.sdjxd.pms.platform.data.DbOper;

public class FlowDao {
	private Logger log = Logger.getLogger(FlowDao.class.getName());

	private FlowSql sqlHelper = new FlowSql();

	/**
	 * �鿴�Ƿ��е�ǰƱ��δ���л�ǩ�Ļ�ǩ��¼
	 * 
	 * @param sheetId
	 * @param nodeInstanceId 
	 * @param flowInstanceId 
	 * @return
	 */
	public String isHQ(String sheetId, String flowInstanceId, int nodeInstanceId) {
		String strSheetID = "";
		
		RowSet rs = null;
		try {
			rs = DbOper.executeQuery(sqlHelper.isHQ(sheetId));
			if (rs.next()) {
				strSheetID = rs.getString("SHEETID");
			}
			
			//�ж��Ƿ���Ҫ��ˡ�
			rs = null;
			rs = DbOper.executeQuery(sqlHelper.isNeedSH(flowInstanceId, nodeInstanceId));
			if (rs.next()) {
				strSheetID = strSheetID + ",1";
			}
			else
			{
				strSheetID = strSheetID + ",0";
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return "";
		}
		
		return strSheetID;
	}

	/**
	 * ɾ����ǩ����
	 * @param operid
	 * @param userId
	 * @return
	 */
	public boolean deleteHqWaitDo(String operid, String userId) {
		try {
			return DbOper.executeNonQuery(sqlHelper.deleteHqWaitDo(operid,
					userId)) == -1 ? false : true;
		} catch (SQLException e) {
			e.printStackTrace();
			log.debug(e.getMessage());
			return false;
		}
	}
}
