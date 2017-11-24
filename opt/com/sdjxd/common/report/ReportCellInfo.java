package com.sdjxd.common.report;

public class ReportCellInfo {
	private String sheetId = "";// ����
	private String value = "";// ��Ӧֵ
	private String style = "";// ��ʽ��
	private String dataType = "";// ��������
	private String format = "";// ��ʾ��ʽ
	private String clickEvent = "";// ��Ӧ�����¼�
	private String expandDirection = "";// ��չ����
	private int colspan = 1;// ����
	private int rowspan = 1;// ����
	private String parentCellSheetid = "";// ����Ԫ��ID
	private String colSheetId = "";// ��Ӧ��ID
	private String rowSheetId = "";// ��Ӧ��ID
	private String dybb = "";// ��Ӧ����
	private int isMainCell = 0;// �Ƿ�Ϊ����
	private int rowIndex = 0;// ������
	private int colIndex = 0;// ������
	private String topMain = "";// ������
	private String leftMain = "";// ������

	public String getSheetId() {
		return sheetId;
	}

	public void setSheetId(String sheetId) {
		this.sheetId = sheetId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getClickEvent() {
		return clickEvent;
	}

	public void setClickEvent(String clickEvent) {
		this.clickEvent = clickEvent;
	}

	public String getExpandDirection() {
		return expandDirection;
	}

	public void setExpandDirection(String expandDirection) {
		this.expandDirection = expandDirection;
	}

	public int getColspan() {
		return colspan;
	}

	public void setColspan(int colspan) {
		this.colspan = colspan;
	}

	public int getRowspan() {
		return rowspan;
	}

	public void setRowspan(int rowspan) {
		this.rowspan = rowspan;
	}

	public String getParentCellSheetid() {
		return parentCellSheetid;
	}

	public void setParentCellSheetid(String parentCellSheetid) {
		this.parentCellSheetid = parentCellSheetid;
	}

	public String getColSheetId() {
		return colSheetId;
	}

	public void setColSheetId(String colSheetId) {
		this.colSheetId = colSheetId;
	}

	public String getRowSheetId() {
		return rowSheetId;
	}

	public void setRowSheetId(String rowSheetId) {
		this.rowSheetId = rowSheetId;
	}

	public String getDybb() {
		return dybb;
	}

	public void setDybb(String dybb) {
		this.dybb = dybb;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public int getIsMainCell() {
		return isMainCell;
	}

	public void setIsMainCell(int isMainCell) {
		this.isMainCell = isMainCell;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public int getColIndex() {
		return colIndex;
	}

	public void setColIndex(int colIndex) {
		this.colIndex = colIndex;
	}

	public String getTopMain() {
		return topMain;
	}

	public void setTopMain(String topMain) {
		this.topMain = topMain;
	}

	public String getLeftMain() {
		return leftMain;
	}

	public void setLeftMain(String leftMain) {
		this.leftMain = leftMain;
	}

}
