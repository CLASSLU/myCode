package com.sdjxd.common.report;

import com.sdjxd.pms.platform.tool.Guid;

public class ReportSql {
	/**
	 * 得到报表列信息
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
	 * 得到报表的行信息
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
	 * 得到报表单元格信息
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
	 * 查询是否存在单元格
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
		sql.append(rci.getRowSheetId());// 行ID
		sql.append("',DYBB='");
		sql.append(rci.getDybb());// 对应报表
		sql.append("',ROWSPAN='");
		sql.append(rci.getRowspan());// 对应行数
		sql.append("',COLSPAN='");
		sql.append(rci.getColspan());// 对应列数
		sql.append("',YSB='");
		sql.append(rci.getStyle());// 样式表
		sql.append("',SJLX='");
		sql.append(rci.getDataType());// 数据类型
		sql.append("',XSGS='");
		sql.append(rci.getFormat());// 显示格式
		sql.append("',DJSJ='");
		sql.append(rci.getClickEvent().replaceAll("'", "''"));// 单击事件
		sql.append("',DYZ='");
		sql.append(rci.getValue().replaceAll("'", "''"));// 对应值
		sql.append("',ISMAINCELL='");
		sql.append(rci.getIsMainCell());// 是否主格
		sql.append("',KZFX='");
		sql.append(rci.getExpandDirection());// 扩展方向
		sql.append("',SZG='");
		sql.append(rci.getTopMain());// 上主格
		sql.append("',ZZG='");
		sql.append(rci.getLeftMain());// 左主格
		sql.append("' WHERE SHEETID='").append(rci.getSheetId()).append("'");
		return sql.toString();
	}

	/**
	 * 设置合并信息
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
		sql.append(rci.getRowSheetId());// 行ID
		sql.append("',DYBB='");
		sql.append(rci.getDybb());// 对应报表
		sql.append("',ROWSPAN='");
		sql.append(rci.getRowspan());// 对应行数
		sql.append("',COLSPAN='");
		sql.append(rci.getColspan());// 对应列数
		sql.append("',ISMAINCELL='");
		sql.append(rci.getIsMainCell());// 是否主格
		sql.append("' WHERE SHEETID='").append(rci.getSheetId()).append("'");
		return sql.toString();
	}

	public String saveCellInfo(ReportCellInfo rci) {
		StringBuffer sql = new StringBuffer(
				"INSERT INTO [S15].XMGG_REPORT_CVALUE (SHEETID, DYL, DYH, DYBB,ROWSPAN, COLSPAN,YSB,SJLX,XSGS,DJSJ,DYZ,ISMAINCELL,KZFX,SZG,ZZG,DATASTATUSID) VALUES ('");
		sql.append(Guid.create());// 主键
		sql.append("','");
		sql.append(rci.getColSheetId());// 列ID
		sql.append("','");
		sql.append(rci.getRowSheetId());// 行ID
		sql.append("','");
		sql.append(rci.getDybb());// 对应报表
		sql.append("','");
		sql.append(rci.getRowspan());// 对应行数
		sql.append("','");
		sql.append(rci.getColspan());// 对应列数
		sql.append("','");
		sql.append(rci.getStyle());// 样式表
		sql.append("','");
		sql.append(rci.getDataType());// 数据类型
		sql.append("','");
		sql.append(rci.getFormat());// 显示格式
		sql.append("','");
		sql.append(rci.getClickEvent().replaceAll("'", "''"));// 单击事件
		sql.append("','");
		sql.append(rci.getValue().replaceAll("'", "''"));// 对应值
		sql.append("','");
		sql.append(rci.getIsMainCell());// 是否主格
		sql.append("','");
		sql.append(rci.getExpandDirection());// 扩展方向
		sql.append("','");
		sql.append(rci.getTopMain());// 上主格
		sql.append("','");
		sql.append(rci.getLeftMain());// 左主格
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
	 * 删除行
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
	 * 删除对应行的单元格元件
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
	 * 删除列
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
	 * 删除列对应的单元格
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
	 * 通过数据集名和报表ID得到相应的数据集
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
