package com.sdjxd.common.report;

public class SqlInfo {
	private String select;// select 部分
	private String others;// 其它部分
	private String order;// 排序字段

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
