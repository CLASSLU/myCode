package kd.idp.cms.mapper.priv;

import java.sql.ResultSet;
import java.sql.SQLException;

import kd.idp.cms.bean.priv.PrivAttrBean;

import org.springframework.jdbc.core.RowMapper;

public class PrivAttrRowMapper implements RowMapper<PrivAttrBean>{

	public PrivAttrBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		PrivAttrBean attr = new PrivAttrBean();
		attr.setPrivAttrId(rs.getString("属性ID"));
		attr.setPrivId(rs.getString("权限ID"));
		attr.setPrivAttrType(rs.getString("属性类型"));
		attr.setPrivAttrName(rs.getString("属性名"));
		attr.setPrivAttrValue(rs.getString("属性值"));
		attr.setAttrOrder(rs.getInt("顺序"));
		return attr;
	}

}
