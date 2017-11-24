package com.sdjxd.common.report;

public class ReportCellDefined {
	private static ReportDao dao = new ReportDao();

	/**
	 * 得到报表的列信息
	 * 
	 * @param sheetid
	 * @return
	 */
	public static Object getCol(String sheetid) {
		return dao.getCol(sheetid);
	}

	/**
	 * 得到报表的行信息 
	 * 
	 * @param sheetid
	 * @return
	 */
	public static Object getRow(String sheetid) {
		return dao.getRow(sheetid);
	}

	/**
	 * 得到报表的单元格信息
	 * 
	 * @param sheetid
	 * @return
	 */
	public static Object getCellValue(String sheetid) {
		return dao.getCellValue(sheetid);
	}

	/**
	 * 添加列
	 * 
	 * @param sheetid
	 * @return
	 */
	public static boolean addCol(String sheetid) {
		return dao.addCol(sheetid);
	}
	/**
	 * 删除列
	 * @param sheetid
	 * @return
	 */
	public static boolean delCol(String sheetid) {
		return dao.delCol(sheetid);
	}

	/**
	 * 添加行
	 * 
	 * @param sheetid
	 * @return
	 */
	public static boolean addRow(String sheetid) {
		return dao.addRow(sheetid);
	}
	/**
	 * 删除行
	 * @param sheetid
	 * @return
	 */
	public static boolean delRow(String sheetid) {
		return dao.delRow(sheetid);
	}
	/**
	 * 保存单元格信息
	 * 
	 * @param cellInfo
	 * @return
	 */
	public static boolean saveCellInfo(ReportCellInfo cellInfo) {
		return dao.saveCellInfo(cellInfo);
	}

	/**
	 * 合并单元格
	 * 
	 * @param unionCells
	 * @return
	 */
	public static boolean unionCell(ReportCellInfo[] unionCells) {
		return dao.unionCell(unionCells);
	}
}
