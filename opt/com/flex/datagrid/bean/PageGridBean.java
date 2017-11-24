package com.flex.datagrid.bean;

import java.util.List;

public class PageGridBean {
	private int curPage  = 0; 					// ��ǰҳ��  
	private int totalPage = 0; 					// ��ҳ��  
	private int totalCount = 0; 				// �ܼ�¼����  
	private int pageSize = 20;					// ÿҳ�����ļ�¼����Ĭ��10��  
	
	private String dataSql = null;				//��Ҫ��ʾ���ݵĲ�ѯ���
	
	private List pagedatas = null; 				//���˳���������

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
