package com.spring.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

public class HashMapRowMapper implements RowMapper<Map<String, Object>>{

	public Map<String, Object> mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		int colunmCount = rs.getMetaData().getColumnCount();
		for(int i=0;i<colunmCount;i++){
			map.put(rs.getMetaData().getColumnName(i+1).toUpperCase(), rs.getObject(i+1));
		}
		return map;
	}

}
