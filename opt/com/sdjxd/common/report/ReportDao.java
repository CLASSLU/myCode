package com.sdjxd.common.report;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.RowSet;

import com.sdjxd.pms.platform.data.DbOper;

public class ReportDao {
	ReportSql sqlHelper = new ReportSql();

	/**
	 * �õ���������Ϣ
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
	 * �õ���������Ϣ
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
	 * �õ�����Ԫ����Ϣ
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
	 * �����
	 * 
	 * @param sheetId
	 *            ��ID
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
	 * ɾ����
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
	 * �����
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
	 * ɾ����
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
	 * ���浥Ԫ����Ϣ
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
	 * ��ֵ
	 * 
	 * @param rs
	 * @param rci
	 */
	private void rs2ReportCellInfo(RowSet rs, ReportCellInfo rci) {
		try {
			rci.setSheetId(rs.getString("SHEETID"));// ����
			rci.setValue(rs.getString("DYZ"));// ��Ӧֵ
			rci.setDybb(rs.getString("DYBB"));// ��Ӧ����
			rci.setColSheetId(rs.getString("DYL"));// ��Ӧ��
			rci.setRowSheetId(rs.getString("DYH"));// ��Ӧ��
			rci.setStyle(rs.getString("YSB"));// ��ʽ��
			rci.setDataType(rs.getString("SJLX"));// ��������
			rci.setTopMain(rs.getString("SZG") == null ? "" : rs
					.getString("SZG"));// ������
			rci.setLeftMain(rs.getString("ZZG") == null ? "" : rs
					.getString("ZZG"));// ������
			rci.setClickEvent(rs.getString("DJSJ") == null ? "" : rs
					.getString("DJSJ"));// ������
			rci.setFormat(rs.getString("XSGS") == null ? "" : rs
					.getString("XSGS"));// ��ʾ��ʽ
			// �Ƿ�����
			rci.setIsMainCell(Integer
					.valueOf(rs.getString("ISMAINCELL") == null
							|| rs.getString("ISMAINCELL").equals("0") ? 0 : 1));
			// ��չ����,Ĭ��������չ
			rci.setExpandDirection((rs.getString("KZFX") == null || rs
					.getString("KZFX").equals("")) ? "1" : rs
							.getString("KZFX"));
			// ����
			rci.setRowspan(Integer
					.valueOf(rs.getString("ROWSPAN") == null ? "1" : rs
							.getString("ROWSPAN")));
			// ����
			rci.setColspan(Integer
					.valueOf(rs.getString("COLSPAN") == null ? "1" : rs
							.getString("COLSPAN")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ͨ�����ݼ����ͱ���ID�õ���Ӧ�����ݼ�
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
	 * �ϲ���Ԫ��
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
