package kd.idp.cms.mapper.system;

import java.sql.ResultSet;
import java.sql.SQLException;

import kd.idp.cms.bean.system.DictionaryBean;

import org.springframework.jdbc.core.RowMapper;

public class DictionaryRowMapper implements RowMapper<DictionaryBean>{

	public DictionaryBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		DictionaryBean dic = new DictionaryBean();
		dic.setType(rs.getString("分类"));
		dic.setName(rs.getString("名称"));
		dic.setCode(rs.getString("标识"));
		dic.setOrder(rs.getInt("顺序"));
		return dic;
	}

}
