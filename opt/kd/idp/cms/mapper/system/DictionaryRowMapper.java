package kd.idp.cms.mapper.system;

import java.sql.ResultSet;
import java.sql.SQLException;

import kd.idp.cms.bean.system.DictionaryBean;

import org.springframework.jdbc.core.RowMapper;

public class DictionaryRowMapper implements RowMapper<DictionaryBean>{

	public DictionaryBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		DictionaryBean dic = new DictionaryBean();
		dic.setType(rs.getString("����"));
		dic.setName(rs.getString("����"));
		dic.setCode(rs.getString("��ʶ"));
		dic.setOrder(rs.getInt("˳��"));
		return dic;
	}

}
