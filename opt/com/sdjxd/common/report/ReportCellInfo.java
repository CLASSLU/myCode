package com.sdjxd.common.report;

public class ReportCellInfo {
	private String sheetId = "";// 主键
	private String value = "";// 对应值
	private String style = "";// 样式表
	private String dataType = "";// 数据类型
	private String format = "";// 显示格式
	private String clickEvent = "";// 对应单击事件
	private String expandDirection = "";// 扩展方向
	private int colspan = 1;// 列数
	private int rowspan = 1;// 行数
	private String parentCellSheetid = "";// 父单元格ID
	private String colSheetId = "";// 对应列ID
	private String rowSheetId = "";// 对应行ID
	private String dybb = "";// 对应报表
	private int isMainCell = 0;// 是否为主格
	private int rowIndex = 0;// 行索引
	private int colIndex = 0;// 列索引
	private String topMain = "";// 上主格
	private String leftMain = "";// 左主格

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
