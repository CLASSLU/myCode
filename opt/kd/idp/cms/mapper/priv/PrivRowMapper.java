package kd.idp.cms.mapper.priv;

import java.sql.ResultSet;
import java.sql.SQLException;

import kd.idp.cms.bean.priv.PrivBean;

import org.springframework.jdbc.core.RowMapper;

public class PrivRowMapper implements RowMapper<PrivBean>{

	public PrivBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		PrivBean pb = new PrivBean();
		pb.setPrivGroup(rs.getString("Ȩ����"));
		pb.setPrivId(rs.getString("Ȩ��ID"));
		pb.setPrivName(rs.getString("Ȩ����"));
		pb.setPrivType(rs.getString("����"));
		pb.setPrivParentId(rs.getString("Ȩ�޸�ID"));
		pb.setPrivImage(rs.getString("Ȩ��ͼƬ"));
		pb.setPrivLevel(rs.getInt("Ȩ�޲��"));
		pb.setPrivOrder(rs.getInt("Ȩ��˳��"));
		return pb;
	}

}
