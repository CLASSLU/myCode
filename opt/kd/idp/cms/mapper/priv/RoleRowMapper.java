package kd.idp.cms.mapper.priv;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kd.idp.cms.bean.priv.RoleBean;

import org.springframework.jdbc.core.RowMapper;

public class RoleRowMapper implements RowMapper<RoleBean>{

private SimpleDateFormat sdf = null;
	
	public RoleRowMapper(String pattern) {
		if(pattern != null && !"".equals(pattern)){
			sdf = new SimpleDateFormat(pattern);
		}else{
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		}
	}
	
	public RoleRowMapper() {
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	public RoleBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		RoleBean role = new RoleBean();
		role.setRoleId(rs.getString("角色ID"));
		role.setRoleName(rs.getString("角色名"));
		role.setRoleDesc(rs.getString("角色描述"));
		role.setOrgId(rs.getString("所属组织机构"));
		role.setRoleStatus(rs.getString("状态"));
		role.setVertime(dateToString(rs.getDate("创建时间")));
		role.setOrder(rs.getInt("排序"));
		return role;
	}
	
	/**
	 * 日期转换为字符串
	 * @param date
	 * @return
	 */
	private String dateToString(Date date){
		try {
			if(date != null){
				return sdf.format(date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
