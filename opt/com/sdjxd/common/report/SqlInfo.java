package com.sdjxd.common.report;

public class SqlInfo {
	private String select;// select ����
	private String others;// ��������
	private String order;// �����ֶ�

	public String putSelectField(String field) {
		if (select.length() == 0) {
			select = field;
		} else {
			select += "," + field;
		}
		return select;
	}

	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}
