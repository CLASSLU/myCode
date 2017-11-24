package kd.idp.index.statistics.mapper;



import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kd.idp.index.statistics.bean.UseStatisticsBean;

import org.springframework.jdbc.core.RowMapper;

public class UseRowMapper implements RowMapper<UseStatisticsBean>{
	private SimpleDateFormat sdf = null;
	public UseRowMapper() {
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	public UseStatisticsBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		UseStatisticsBean useStatisticsBean=new UseStatisticsBean();
		useStatisticsBean.setDv(NullToString(rs.getString("地区")));
		useStatisticsBean.setStatisticsContent(NullToString(rs.getString("统计内容")));
		useStatisticsBean.setCount(rs.getInt("使用次数"));
		useStatisticsBean.setComment(NullToString(rs.getString("备注")));
		useStatisticsBean.setDate(dateToString(rs.getDate("日期")));
		return useStatisticsBean;
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
