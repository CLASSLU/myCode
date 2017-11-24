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
		role.setRoleId(rs.getString("��ɫID"));
		role.setRoleName(rs.getString("��ɫ��"));
		role.setRoleDesc(rs.getString("��ɫ����"));
		role.setOrgId(rs.getString("������֯����"));
		role.setRoleStatus(rs.getString("״̬"));
		role.setVertime(dateToString(rs.getDate("����ʱ��")));
		role.setOrder(rs.getInt("����"));
		return role;
	}
	
	/**
	 * ����ת��Ϊ�ַ���
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
