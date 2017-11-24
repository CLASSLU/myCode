package kd.idp.cms.bean;

import java.util.ArrayList;
import java.util.List;

public class PagingGridBean<T> {

	/**
	 * 表格数据
	 */
	private List<T> gridData = new ArrayList<T>();
	
	/**
	 * 每页显示记录数
	 */
	private int pageSize = 0;
	
	/**
	 * 当前页
	 */
	private int pageNum = 0;
	
	/**
	 * 总页数
	 */
	private int totlePage = 0;
	
	/**
	 * 总记录数
	 */
	private int totalData = 0;

	public List<T> getGridData() {
		return gridData;
	}

	public void setGridData(List<T> gridData) {
		this.gridData = gridData;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getTotlePage() {
		return totlePage;
	}

	public void setTotlePage(int totlePage) {
		this.totlePage = totlePage;
	}

	public int getTotalData() {
		return totalData;
	}

	public void setTotalData(int totalData) {
		this.totalData = totalData;
	}
	
}
