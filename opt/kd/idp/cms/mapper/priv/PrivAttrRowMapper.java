package kd.idp.cms.mapper.priv;

import java.sql.ResultSet;
import java.sql.SQLException;

import kd.idp.cms.bean.priv.PrivAttrBean;

import org.springframework.jdbc.core.RowMapper;

public class PrivAttrRowMapper implements RowMapper<PrivAttrBean>{

	public PrivAttrBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		PrivAttrBean attr = new PrivAttrBean();
		attr.setPrivAttrId(rs.getString("����ID"));
		attr.setPrivId(rs.getString("Ȩ��ID"));
		attr.setPrivAttrType(rs.getString("��������"));
		attr.setPrivAttrName(rs.getString("������"));
		attr.setPrivAttrValue(rs.getString("����ֵ"));
		attr.setAttrOrder(rs.getInt("˳��"));
		return attr;
	}

}
