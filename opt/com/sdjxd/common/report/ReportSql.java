package com.sdjxd.common.report;

import com.sdjxd.pms.platform.tool.Guid;

public class ReportSql {
	/**
	 * �õ���������Ϣ
	 * 
	 * @param sheetid
	 * @return
	 */
	public String getCol(String sheetid) {
		String sql = "SELECT T.*,ROWNUM AS COLINDEX FROM (SELECT *  FROM [S15].XMGG_REPORT_COL T WHERE DYBB ='"
				+ sheetid + "' ORDER BY SHOWORDER) T";
		return sql;
	}

	/**
	 * �õ����������Ϣ
	 * 
	 * @param sheetid
	 * @return
	 */
	public String getRow(String sheetid) {
		String sql = "SELECT T.*,ROWNUM AS ROWINDEX FROM (SELECT * FROM [S15].XMGG_REPORT_ROW T WHERE DYBB ='"
				+ sheetid + "' ORDER BY SHOWORDER) T";
		return sql;
	}

	/**
	 * �õ�����Ԫ����Ϣ
	 * 
	 * @param sheetid
	 * @return
	 */
	public String getCellValue(String sheetid) {
		String sql = "SELECT * FROM [S15].XMGG_REPORT_CVALUE WHERE DYBB ='" + sheetid
				+ "' ORDER BY SHOWORDER";
		return sql;
	}

	/**
	 * ��ѯ�Ƿ���ڵ�Ԫ��
	 * 
	 * @param cellSheetid
	 * @return
	 */
	public String isExistCell(String cellSheetid) {
		String sql = "SELECT SHEETID FROM [S15].XMGG_REPORT_CVALUE WHERE SHEETID = '"
				+ cellSheetid + "' ";
		return sql;
	}

	/**
	 * 
	 * @param rci
	 * @return
	 */
	public String updateCellInfo(ReportCellInfo rci) {
		StringBuffer sql = new StringBuffer(
				"UPDATE [S15].XMGG_REPORT_CVALUE SET SHEETID = '");
		sql.append(rci.getSheetId());
		sql.append("',DYL='");
		sql.append(rci.getColSheetId());
		sql.append("',DYH='");
		sql.append(rci.getRowSheetId());// ��ID
		sql.append("',DYBB='");
		sql.append(rci.getDybb());// ��Ӧ����
		sql.append("',ROWSPAN='");
		sql.append(rci.getRowspan());// ��Ӧ����
		sql.append("',COLSPAN='");
		sql.append(rci.getColspan());// ��Ӧ����
		sql.append("',YSB='");
		sql.append(rci.getStyle());// ��ʽ��
		sql.append("',SJLX='");
		sql.append(rci.getDataType());// ��������
		sql.append("',XSGS='");
		sql.append(rci.getFormat());// ��ʾ��ʽ
		sql.append("',DJSJ='");
		sql.append(rci.getClickEvent().replaceAll("'", "''"));// �����¼�
		sql.append("',DYZ='");
		sql.append(rci.getValue().replaceAll("'", "''"));// ��Ӧֵ
		sql.append("',ISMAINCELL='");
		sql.append(rci.getIsMainCell());// �Ƿ�����
		sql.append("',KZFX='");
		sql.append(rci.getExpandDirection());// ��չ����
		sql.append("',SZG='");
		sql.append(rci.getTopMain());// ������
		sql.append("',ZZG='");
		sql.append(rci.getLeftMain());// ������
		sql.append("' WHERE SHEETID='").append(rci.getSheetId()).append("'");
		return sql.toString();
	}

	/**
	 * ���úϲ���Ϣ
	 * 
	 * @param rci
	 * @return
	 */
	public String updateUnionCellInfo(ReportCellInfo rci) {
		StringBuffer sql = new StringBuffer(
				"UPDATE [S15].XMGG_REPORT_CVALUE SET SHEETID = '");
		sql.append(rci.getSheetId());
		sql.append("',DYL='");
		sql.append(rci.getColSheetId());
		sql.append("',DYH='");
		sql.append(rci.getRowSheetId());// ��ID
		sql.append("',DYBB='");
		sql.append(rci.getDybb());// ��Ӧ����
		sql.append("',ROWSPAN='");
		sql.append(rci.getRowspan());// ��Ӧ����
		sql.append("',COLSPAN='");
		sql.append(rci.getColspan());// ��Ӧ����
		sql.append("',ISMAINCELL='");
		sql.append(rci.getIsMainCell());// �Ƿ�����
		sql.append("' WHERE SHEETID='").append(rci.getSheetId()).append("'");
		return sql.toString();
	}

	public String saveCellInfo(ReportCellInfo rci) {
		StringBuffer sql = new StringBuffer(
				"INSERT INTO [S15].XMGG_REPORT_CVALUE (SHEETID, DYL, DYH, DYBB,ROWSPAN, COLSPAN,YSB,SJLX,XSGS,DJSJ,DYZ,ISMAINCELL,KZFX,SZG,ZZG,DATASTATUSID) VALUES ('");
		sql.append(Guid.create());// ����
		sql.append("','");
		sql.append(rci.getColSheetId());// ��ID
		sql.append("','");
		sql.append(rci.getRowSheetId());// ��ID
		sql.append("','");
		sql.append(rci.getDybb());// ��Ӧ����
		sql.append("','");
		sql.append(rci.getRowspan());// ��Ӧ����
		sql.append("','");
		sql.append(rci.getColspan());// ��Ӧ����
		sql.append("','");
		sql.append(rci.getStyle());// ��ʽ��
		sql.append("','");
		sql.append(rci.getDataType());// ��������
		sql.append("','");
		sql.append(rci.getFormat());// ��ʾ��ʽ
		sql.append("','");
		sql.append(rci.getClickEvent().replaceAll("'", "''"));// �����¼�
		sql.append("','");
		sql.append(rci.getValue().replaceAll("'", "''"));// ��Ӧֵ
		sql.append("','");
		sql.append(rci.getIsMainCell());// �Ƿ�����
		sql.append("','");
		sql.append(rci.getExpandDirection());// ��չ����
		sql.append("','");
		sql.append(rci.getTopMain());// ������
		sql.append("','");
		sql.append(rci.getLeftMain());// ������
		sql.append("',1)");
		return sql.toString();
	}

	/**
	 * 
	 * @param sheetId
	 * @return
	 */
	public String addRow(String sheetId) {
		String sql = "INSERT INTO [S15].XMGG_REPORT_ROW (SHEETID, DATASTATUSID,DYBB,SHOWORDER) VALUES ('"
				+ Guid.create()
				+ "',1,'"
				+ sheetId
				+ "',(SELECT nvl(MAX( nvl(SHOWORDER,0)+1),0) as mshoworder FROM [S15].XMGG_REPORT_ROW))";
		return sql;
	}

	/**
	 * ɾ����
	 * 
	 * @param sheetId
	 * @return
	 */
	public String delRow(String sheetId) {
		String sql = "DELETE [S15].XMGG_REPORT_ROW R WHERE R.SHEETID = '" + sheetId
				+ "'";
		return sql;
	}

	/**
	 * ɾ����Ӧ�еĵ�Ԫ��Ԫ��
	 * 
	 * @param rowsheetId
	 * @return
	 */
	public String delCellInfoByRow(String rowsheetId) {
		String sql = "DELETE [S15].XMGG_REPORT_CVALUE C WHERE C.DYH = '" + rowsheetId
				+ "'";
		return sql;
	}

	/**
	 * 
	 * @param sheetId
	 * @return
	 */
	public String addCol(String sheetId) {
		String sql = "INSERT INTO [S15].XMGG_REPORT_COL (SHEETID, DATASTATUSID,DYBB,SHOWORDER) VALUES ('"
				+ Guid.create()
				+ "',1,'"
				+ sheetId
				+ "',(SELECT nvl(MAX( nvl(SHOWORDER,0)+1),0) as mshoworder FROM [S15].XMGG_REPORT_COL))";
		return sql;
	}

	/**
	 * ɾ����
	 * 
	 * @param sheetId
	 * @return
	 */
	public String delCol(String sheetId) {
		String sql = "DELETE [S15].XMGG_REPORT_COL C WHERE C.SHEETID = '" + sheetId
				+ "'";
		return sql;
	}

	/**
	 * ɾ���ж�Ӧ�ĵ�Ԫ��
	 * 
	 * @param colsheetId
	 * @return
	 */
	public String delCellInfoByCol(String colsheetId) {
		String sql = "DELETE [S15].XMGG_REPORT_CVALUE C WHERE C.DYL = '" + colsheetId
				+ "'";
		return sql;
	}

	/**
	 * ͨ�����ݼ����ͱ���ID�õ���Ӧ�����ݼ�
	 * 
	 * @param dataSetName
	 * @param reportId
	 * @return
	 */
	public String getDataSetByName(String dataSetName, String reportId) {
		String sql = "select * from  [S15].XMGG_REPORT_DATASET t where t.DYBB = '"
				+ reportId + "' and SHEETNAME = '" + dataSetName + "'";
		return sql;
	}
}
