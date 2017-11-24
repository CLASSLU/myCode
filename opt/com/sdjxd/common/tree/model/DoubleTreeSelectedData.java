package com.sdjxd.common.tree.model;

public class DoubleTreeSelectedData {

	private String fromTreeSelNodeObjectId;
	private String[] toTreeSelNodeObjectId;
	private String tableName;
	private String fromCloumnName;
	private String toColumnName;

	public String getFromTreeSelNodeObjectId() {
		return fromTreeSelNodeObjectId;
	}

	public void setFromTreeSelNodeObjectId(String fromTreeSelNodeObjectId) {
		this.fromTreeSelNodeObjectId = fromTreeSelNodeObjectId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getFromCloumnName() {
		return fromCloumnName;
	}

	public void setFromCloumnName(String fromCloumnName) {
		this.fromCloumnName = fromCloumnName;
	}

	public String getToColumnName() {
		return toColumnName;
	}

	public void setToColumnName(String toColumnName) {
		this.toColumnName = toColumnName;
	}

	public String[] getToTreeSelNodeObjectId() {
		return toTreeSelNodeObjectId;
	}

	public void setToTreeSelNodeObjectId(String[] toTreeSelNodeObjectId) {
		this.toTreeSelNodeObjectId = toTreeSelNodeObjectId;
	}

}
