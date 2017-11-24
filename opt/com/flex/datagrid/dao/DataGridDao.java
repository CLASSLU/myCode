package com.flex.datagrid.dao;

import java.util.List;
import java.util.Map;

import com.flex.datagrid.bean.PageGridBean;
import com.spring.dbservice.DBTemplate;
import com.spring.mapper.HashMapStrRowMapper;

public class DataGridDao {

	public PageGridBean getPageBean(PageGridBean bean) {
		if(bean.getDataSql() != null){
			String conttSql = "SELECT COUNT(*) FROM (" + bean.getDataSql() + ")";
			int totalCount = DBTemplate.getInstance().getCount(conttSql);
			bean.setTotalCount(totalCount);
			int totalPage = 0;
			int pageSize = bean.getPageSize();
			totalPage = (totalCount + pageSize -1)/pageSize;  
			bean.setTotalPage(totalPage);
			bean = getPageDatas(bean);
		}
		return bean;
	}

	public PageGridBean getPageDatas(PageGridBean bean) {
		if(bean.getDataSql() != null){
			int offset = bean.getCurPage() * bean.getPageSize();
			String sql = "SELECT * FROM (" + bean.getDataSql() + ")  LIMIT "+  bean.getPageSize() +" OFFSET "+ offset +";";
			List<Map<String, String>> pagedatas = DBTemplate.getInstance().getResultRowMapperList(sql, new HashMapStrRowMapper());
			bean.setPagedatas(pagedatas);
		}
		return bean;
	}

}
