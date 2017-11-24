package com.sdjxd.common.report;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.RowSet;

import com.sdjxd.pms.platform.data.DbOper;

public class ReportDao {
	ReportSql sqlHelper = new ReportSql();

	/**
	 * 得到报表列信息
	 * 
	 * @param sheetid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object getCol(String sheetid) {
		ArrayList dataSet = null;
		try {
			dataSet = DbOper.executeList(sqlHelper.getCol(sheetid));
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
		return dataSet;
	}

	/**
	 * 得到报表行信息
	 * 
	 * @param sheetid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object getRow(String sheetid) {
		ArrayList dataSet = null;
		try {
			dataSet = DbOper.executeList(sqlHelper.getRow(sheetid));
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
		return dataSet;
	}

	/**
	 * 得到报表单元格信息
	 * 
	 * @param sheetid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object getCellValue(String sheetid) {
		ArrayList dataSet = null;
		try {
			dataSet = DbOper.executeList(sqlHelper.getCellValue(sheetid));
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
		return dataSet;
	}

	/**
	 * 添加列
	 * 
	 * @param sheetId
	 *            列ID
	 * @return
	 */
	public boolean addCol(String sheetId) {
		try {

			return DbOper.executeNonQuery(sqlHelper.addCol(sheetId)) == -1 ? false
					: true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除列
	 * 
	 * @param sheetId
	 * @return
	 */
	public boolean delCol(String sheetId) {
		try {
			List<String> sqlList = new ArrayList<String>();
			sqlList.add(sqlHelper.delCol(sheetId));
			sqlList.add(sqlHelper.delCellInfoByCol(sheetId));
			return DbOper.executeNonQuery(sqlList) == -1 ? false : true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 添加行
	 * 
	 * @param sheetId
	 * @return
	 */
	public boolean addRow(String sheetId) {
		try {
			return DbOper.executeNonQuery(sqlHelper.addRow(sheetId)) == -1 ? false
					: true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除行
	 * 
	 * @param sheetId
	 * @return
	 */
	public boolean delRow(String sheetId) {
		try {
			List<String> sqlList = new ArrayList<String>();
			sqlList.add(sqlHelper.delRow(sheetId));
			sqlList.add(sqlHelper.delCellInfoByRow(sheetId));
			return DbOper.executeNonQuery(sqlList) == -1 ? false : true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 保存单元格信息
	 * 
	 * @param cellInfo
	 * @return
	 */
	public boolean saveCellInfo(ReportCellInfo cellInfo) {
		try {
			if (cellInfo.getSheetId() != null
					&& cellInfo.getSheetId().length() > 0) {
				return DbOper.executeNonQuery(sqlHelper
						.updateCellInfo(cellInfo)) == -1 ? false : true;
			} else {
				return DbOper.executeNonQuery(sqlHelper.saveCellInfo(cellInfo)) == -1 ? false
						: true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<ReportCellInfo> getReportCellInfoList(String sheetId) {
		List<ReportCellInfo> rciList = new ArrayList<ReportCellInfo>();
		RowSet rs = null;
		try {
			rs = DbOper.executeQuery(sqlHelper.getCellValue(sheetId));
			while (rs.next()) {
				ReportCellInfo rci = new ReportCellInfo();
				rs2ReportCellInfo(rs, rci);
				rciList.add(rci);
			}
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
		return rciList;
	}

	/**
	 * 绑定值
	 * 
	 * @param rs
	 * @param rci
	 */
	private void rs2ReportCellInfo(RowSet rs, ReportCellInfo rci) {
		try {
			rci.setSheetId(rs.getString("SHEETID"));// 主键
			rci.setValue(rs.getString("DYZ"));// 对应值
			rci.setDybb(rs.getString("DYBB"));// 对应报表
			rci.setColSheetId(rs.getString("DYL"));// 对应列
			rci.setRowSheetId(rs.getString("DYH"));// 对应行
			rci.setStyle(rs.getString("YSB"));// 样式表
			rci.setDataType(rs.getString("SJLX"));// 数据类型
			rci.setTopMain(rs.getString("SZG") == null ? "" : rs
					.getString("SZG"));// 上主格
			rci.setLeftMain(rs.getString("ZZG") == null ? "" : rs
					.getString("ZZG"));// 左主格
			rci.setClickEvent(rs.getString("DJSJ") == null ? "" : rs
					.getString("DJSJ"));// 左主格
			rci.setFormat(rs.getString("XSGS") == null ? "" : rs
					.getString("XSGS"));// 显示格式
			// 是否主格
			rci.setIsMainCell(Integer
					.valueOf(rs.getString("ISMAINCELL") == null
							|| rs.getString("ISMAINCELL").equals("0") ? 0 : 1));
			// 扩展方向,默认向下扩展
			rci.setExpandDirection((rs.getString("KZFX") == null || rs
					.getString("KZFX").equals("")) ? "1" : rs
							.getString("KZFX"));
			// 行数
			rci.setRowspan(Integer
					.valueOf(rs.getString("ROWSPAN") == null ? "1" : rs
							.getString("ROWSPAN")));
			// 列数
			rci.setColspan(Integer
					.valueOf(rs.getString("COLSPAN") == null ? "1" : rs
							.getString("COLSPAN")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过数据集名和报表ID得到相应的数据集
	 * 
	 * @param dataSetName
	 * @param reportId
	 * @return
	 */
	public ReportDataSet getDataSetByName(String dataSetName, String reportId) {
		RowSet rs = null;
		try {
			rs = DbOper.executeQuery(sqlHelper.getDataSetByName(dataSetName,
					reportId));
			if (rs.next()) {
				return ReportDataSet.instanceOfReportDataSet(rs
						.getString("SHEETID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			rs = null;
		}
		return null;
	}

	/**
	 * 合并单元格
	 * 
	 * @param unionCells
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean unionCell(ReportCellInfo[] unionCells) {
		List sqlList = new ArrayList();
		for (ReportCellInfo rci : unionCells) {
			if (rci.getSheetId() != null && rci.getSheetId().length() > 0) {
				sqlList.add(sqlHelper.updateUnionCellInfo(rci));
			} else {
				sqlList.add(sqlHelper.saveCellInfo(rci));
			}
		}
		try {
			return DbOper.executeNonQuery(sqlList) == -1 ? false : true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
