package com.flex.datagrid.bean;

import java.util.List;

public class PageGridBean {
	private int curPage  = 0; 					// 当前页码  
	private int totalPage = 0; 					// 总页数  
	private int totalCount = 0; 				// 总记录条数  
	private int pageSize = 20;					// 每页包含的记录数，默认10条  
	
	private String dataSql = null;				//需要显示数据的查询语句
	
	private List pagedatas = null; 				//过滤出来的数据

	public PageGridBean() {
		super();
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getDataSql() {
		return dataSql;
	}

	public void setDataSql(String dataSql) {
		this.dataSql = dataSql;
	}

	public List getPagedatas() {
		return pagedatas;
	}

	public void setPagedatas(List pagedatas) {
		this.pagedatas = pagedatas;
	}
}
