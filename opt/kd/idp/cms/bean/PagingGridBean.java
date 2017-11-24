package kd.idp.cms.bean;

import java.util.ArrayList;
import java.util.List;

public class PagingGridBean<T> {

	/**
	 * �������
	 */
	private List<T> gridData = new ArrayList<T>();
	
	/**
	 * ÿҳ��ʾ��¼��
	 */
	private int pageSize = 0;
	
	/**
	 * ��ǰҳ
	 */
	private int pageNum = 0;
	
	/**
	 * ��ҳ��
	 */
	private int totlePage = 0;
	
	/**
	 * �ܼ�¼��
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
