package kd.idp.index.statistics.mapper;



import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kd.idp.index.statistics.bean.LoginStatisticsBean;

import org.springframework.jdbc.core.RowMapper;

public class LogInRowMapper implements RowMapper<LoginStatisticsBean>{
	private SimpleDateFormat sdf = null;
	public LogInRowMapper() {
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	public LoginStatisticsBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		LoginStatisticsBean loginStatisticsBean=new LoginStatisticsBean();
		loginStatisticsBean.setDv(NullToString(rs.getString("����")));
		loginStatisticsBean.setDate(dateToString(rs.getDate("����")));
		loginStatisticsBean.setUserName(NullToString(rs.getString("��¼��")));
		loginStatisticsBean.setUserId(NullToString(rs.getString("USERID")));
		loginStatisticsBean.setDepartment(NullToString(rs.getString("����")));
		loginStatisticsBean.setCount(rs.getInt("��¼����"));
		
		
		return loginStatisticsBean;
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
	
	/**
	 * ��null ת�� ''
	 * @param value
	 * @return
	 */
	private String NullToString(String value){
		if(value == null){
			return "";
		}else{
			return value;
		}
	}
}
