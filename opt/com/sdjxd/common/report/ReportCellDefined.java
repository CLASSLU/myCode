package com.sdjxd.common.report;

public class ReportCellDefined {
	private static ReportDao dao = new ReportDao();

	/**
	 * �õ����������Ϣ
	 * 
	 * @param sheetid
	 * @return
	 */
	public static Object getCol(String sheetid) {
		return dao.getCol(sheetid);
	}

	/**
	 * �õ����������Ϣ 
	 * 
	 * @param sheetid
	 * @return
	 */
	public static Object getRow(String sheetid) {
		return dao.getRow(sheetid);
	}

	/**
	 * �õ�����ĵ�Ԫ����Ϣ
	 * 
	 * @param sheetid
	 * @return
	 */
	public static Object getCellValue(String sheetid) {
		return dao.getCellValue(sheetid);
	}

	/**
	 * �����
	 * 
	 * @param sheetid
	 * @return
	 */
	public static boolean addCol(String sheetid) {
		return dao.addCol(sheetid);
	}
	/**
	 * ɾ����
	 * @param sheetid
	 * @return
	 */
	public static boolean delCol(String sheetid) {
		return dao.delCol(sheetid);
	}

	/**
	 * �����
	 * 
	 * @param sheetid
	 * @return
	 */
	public static boolean addRow(String sheetid) {
		return dao.addRow(sheetid);
	}
	/**
	 * ɾ����
	 * @param sheetid
	 * @return
	 */
	public static boolean delRow(String sheetid) {
		return dao.delRow(sheetid);
	}
	/**
	 * ���浥Ԫ����Ϣ
	 * 
	 * @param cellInfo
	 * @return
	 */
	public static boolean saveCellInfo(ReportCellInfo cellInfo) {
		return dao.saveCellInfo(cellInfo);
	}

	/**
	 * �ϲ���Ԫ��
	 * 
	 * @param unionCells
	 * @return
	 */
	public static boolean unionCell(ReportCellInfo[] unionCells) {
		return dao.unionCell(unionCells);
	}
}
