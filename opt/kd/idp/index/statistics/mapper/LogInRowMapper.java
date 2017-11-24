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
		loginStatisticsBean.setDv(NullToString(rs.getString("地区")));
		loginStatisticsBean.setDate(dateToString(rs.getDate("日期")));
		loginStatisticsBean.setUserName(NullToString(rs.getString("登录人")));
		loginStatisticsBean.setUserId(NullToString(rs.getString("USERID")));
		loginStatisticsBean.setDepartment(NullToString(rs.getString("处室")));
		loginStatisticsBean.setCount(rs.getInt("登录次数"));
		
		
		return loginStatisticsBean;
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
	
	/**
	 * 将null 转成 ''
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
