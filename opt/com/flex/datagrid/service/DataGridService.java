package com.flex.datagrid.service;

import com.flex.datagrid.bean.PageGridBean;
import com.flex.datagrid.dao.DataGridDao;

public class DataGridService {

	private DataGridDao dao = new DataGridDao();
	
	public PageGridBean getPageBean(PageGridBean bean){
		return dao.getPageBean(bean);
	}
	
	
	public PageGridBean getPageDatas(PageGridBean bean){
		return dao.getPageDatas(bean);
	}
}
