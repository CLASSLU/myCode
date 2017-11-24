package com.sdjxd.common.report;

import java.util.List;
import java.util.Map;

public class ReportCellInstanceInfo {
	private ReportCellCoordinate cellCoordinate;// 单元格坐标
	private ReportCellCoordinate expandCellCoordinate;// 单元格扩展坐标
	private String value = "";// 对应值
	private String style = "";// 样式表
	private String dataType = "";// 数据类型
	private String format = "";// 显示格式
	private String clickEvent = "";// 对应单击事件
	private int colspan = 1;// 列数
	private int rowspan = 1;// 行数
	private String width = "";//宽度
	private String heigth = "";//高度
	private String parentCellSheetid = "";// 父单元格ID
	private String display;// 是否显示
	private String expandDirection = "";// 扩展方向
	private List<ReportCellInstanceInfo> rciiExpandList = null;// 当前元件扩展出的元件
	private Map<?, ?> dataColSet = null;//对应数据库中一条记录的所有字段
	private boolean isHaveTopMainCell = false;
	private Map<?, ?> rowMap = null;//当前格扩展的所有行数据
	private Map<?, ?> colMap = null;//当前格扩展的所有列数据

	public ReportCellCoordinate getCellCoordinate() {
		return cellCoordinate;
	}

	public void setCellCoordinate(ReportCellCoordinate cellCoordinate) {
		this.cellCoordinate = cellCoordinate;
	}

	public ReportCellCoordinate getExpandCellCoordinate() {
		return expandCellCoordinate;
	}

	public void setExpandCellCoordinate(
			ReportCellCoordinate expandCellCoordinate) {
		this.expandCellCoordinate = expandCellCoordinate;
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

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getClickEvent() {
		return clickEvent;
	}

	public void setClickEvent(String clickEvent) {
		this.clickEvent = clickEvent;
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

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public List<ReportCellInstanceInfo> getRciiExpandList() {
		return rciiExpandList;
	}

	public void setRciiExpandList(List<ReportCellInstanceInfo> rciiExpandList) {
		this.rciiExpandList = rciiExpandList;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeigth() {
		return heigth;
	}

	public void setHeigth(String heigth) {
		this.heigth = heigth;
	}

	public Map<?, ?> getDataColSet() {
		return dataColSet;
	}

	public void setDataColSet(Map<?, ?> dataColSet) {
		this.dataColSet = dataColSet;
	}

	public String getExpandDirection() {
		return expandDirection;
	}

	public void setExpandDirection(String expandDirection) {
		this.expandDirection = expandDirection;
	}

	public boolean isHaveTopMainCell() {
		return isHaveTopMainCell;
	}

	public void setHaveTopMainCell(boolean isHaveTopMainCell) {
		this.isHaveTopMainCell = isHaveTopMainCell;
	}

	public Map<?, ?> getRowMap() {
		return rowMap;
	}

	public void setRowMap(Map<?, ?> rowMap) {
		this.rowMap = rowMap;
	}

	public Map<?, ?> getColMap() {
		return colMap;
	}

	public void setColMap(Map<?, ?> colMap) {
		this.colMap = colMap;
	}
}
