package com.sdjxd.common.report;

public class ReportRowInstance {
	private int rowIndex;// �к�
	private String rowStyle;// ����ʽ
	private int rowHight;// �и߶�
	private int headRow;//�Ƿ��ͷ��

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public String getRowStyle() {
		return rowStyle;
	}

	public void setRowStyle(String rowStyle) {
		this.rowStyle = rowStyle;
	}

	public int getRowHight() {
		return rowHight;
	}

	public void setRowHight(int rowHight) {
		this.rowHight = rowHight;
	}

	public int getHeadRow() {
		return headRow;
	}

	public void setHeadRow(int headRow) {
		this.headRow = headRow;
	}
}
