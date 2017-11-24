package kd.idp.cms.mapper.priv;

import java.sql.ResultSet;
import java.sql.SQLException;

import kd.idp.cms.bean.priv.PrivBean;

import org.springframework.jdbc.core.RowMapper;

public class PrivRowMapper implements RowMapper<PrivBean>{

	public PrivBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		PrivBean pb = new PrivBean();
		pb.setPrivGroup(rs.getString("权限组"));
		pb.setPrivId(rs.getString("权限ID"));
		pb.setPrivName(rs.getString("权限名"));
		pb.setPrivType(rs.getString("类型"));
		pb.setPrivParentId(rs.getString("权限父ID"));
		pb.setPrivImage(rs.getString("权限图片"));
		pb.setPrivLevel(rs.getInt("权限层次"));
		pb.setPrivOrder(rs.getInt("权限顺序"));
		return pb;
	}

}
