package com.spring.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

public class HashMapStrRowMapper implements RowMapper<Map<String, String>>{

	public Map<String, String> mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		Map<String, String> map = new HashMap<String, String>();
		int colunmCount = rs.getMetaData().getColumnCount();
		for(int i=0;i<colunmCount;i++){
			map.put(rs.getMetaData().getColumnName(i+1).toUpperCase(), rs.getString(i+1));
		}
		return map;
	}

}
