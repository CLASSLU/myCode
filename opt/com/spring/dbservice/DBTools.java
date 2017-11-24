package com.spring.dbservice;

import java.util.List;

import kd.idp.cms.bean.PagingGridBean;

import org.springframework.jdbc.core.RowMapper;


public class DBTools {

	/**
	 * 获得分页数据
	 * 
	 * @param <T>
	 * @param sql
	 * @param pageSize
	 * @param pageNum
	 * @param rowMapper
	 * @param type
	 *            分页标志 true 在内存中分页 false SQL分页
	 * @return
	 */
	public static <T> PagingGridBean<T> getPagingData(String sql, int pageSize,
			int pageNum, RowMapper<T> rowMapper, boolean type) {
		PagingGridBean<T> datalist = new PagingGridBean<T>();
		try {
			int startIndex = (pageNum - 1) * pageSize + 1;
			int endIndex = pageNum * pageSize;
			if (type) {
				List<T> rs = DBTemplate.getInstance()
						.getResultRowMapperList(sql, rowMapper);
				int totalCount = rs.size();
				if (totalCount > 0) {
					if (endIndex > totalCount) {
						endIndex = totalCount;
					}
					for (int i = startIndex - 1; i < endIndex; i++) {
						datalist.getGridData().add(rs.get(i));
					}
				}
			} else {
				datalist.setGridData(DBTemplate.getInstance()
						.getResultRowMapperList(
								"SELECT * FROM ( SELECT rownum as num , * FROM ( "
										+ sql + " ) ) WHERE num between "
										+ startIndex + " and " + endIndex,
								rowMapper));
			}
			int totalCount = getResultListSize(sql);
			//System.out.println(sql);
			
			datalist.setTotalData(totalCount);
			int totalPage = totalCount / pageSize;
			if (totalCount == 0 || totalCount % pageSize != 0) {
				totalPage++;
			}
			datalist.setTotlePage(totalPage);
			datalist.setPageNum(pageNum);
			datalist.setPageSize(pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datalist;
	}

	/**
	 * 获得结果集数量
	 * 
	 * @param sql
	 * @return
	 */
	public static int getResultListSize(String sql) {
		return DBTemplate.getInstance().getCount(
				"SELECT COUNT(*) FROM ( " + sql + " )");
	}
	
	
	/**
	 * 获得查询结果
	 * @param sql
	 * @param rowMapper
	 * @return
	 */
	public static <T> List<T> getDBQueryResultList(String sql,RowMapper<T> rowMapper){
		return DBTemplate.getInstance().getResultRowMapperList(sql,
				rowMapper);
	}

}
