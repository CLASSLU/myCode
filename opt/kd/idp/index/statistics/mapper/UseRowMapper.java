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
		useStatisticsBean.setDv(NullToString(rs.getString("����")));
		useStatisticsBean.setStatisticsContent(NullToString(rs.getString("ͳ������")));
		useStatisticsBean.setCount(rs.getInt("ʹ�ô���"));
		useStatisticsBean.setComment(NullToString(rs.getString("��ע")));
		useStatisticsBean.setDate(dateToString(rs.getDate("����")));
		return useStatisticsBean;
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
