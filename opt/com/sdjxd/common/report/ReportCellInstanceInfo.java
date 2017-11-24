package com.sdjxd.common.report;

import java.util.List;
import java.util.Map;

public class ReportCellInstanceInfo {
	private ReportCellCoordinate cellCoordinate;// ��Ԫ������
	private ReportCellCoordinate expandCellCoordinate;// ��Ԫ����չ����
	private String value = "";// ��Ӧֵ
	private String style = "";// ��ʽ��
	private String dataType = "";// ��������
	private String format = "";// ��ʾ��ʽ
	private String clickEvent = "";// ��Ӧ�����¼�
	private int colspan = 1;// ����
	private int rowspan = 1;// ����
	private String width = "";//���
	private String heigth = "";//�߶�
	private String parentCellSheetid = "";// ����Ԫ��ID
	private String display;// �Ƿ���ʾ
	private String expandDirection = "";// ��չ����
	private List<ReportCellInstanceInfo> rciiExpandList = null;// ��ǰԪ����չ����Ԫ��
	private Map<?, ?> dataColSet = null;//��Ӧ���ݿ���һ����¼�������ֶ�
	private boolean isHaveTopMainCell = false;
	private Map<?, ?> rowMap = null;//��ǰ����չ������������
	private Map<?, ?> colMap = null;//��ǰ����չ������������

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
