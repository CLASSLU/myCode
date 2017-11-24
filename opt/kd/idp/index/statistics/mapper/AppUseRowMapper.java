package kd.idp.index.statistics.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kd.idp.index.statistics.bean.AppStatisticsBean;

import org.springframework.jdbc.core.RowMapper;

public class AppUseRowMapper implements RowMapper<AppStatisticsBean> {

	private SimpleDateFormat sdf = null;

	public AppUseRowMapper() {
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	public AppStatisticsBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		AppStatisticsBean appStatisticsBean = new AppStatisticsBean();
		appStatisticsBean.setDv(NullToString(rs.getString("����")));
		appStatisticsBean.setDate(dateToString(rs.getDate("����")));
		appStatisticsBean.setUserName(NullToString(rs.getString("��¼��")));
		appStatisticsBean.setUserId(NullToString(rs.getString("USERID")));
		appStatisticsBean.setDepartment(NullToString(rs.getString("����")));
		appStatisticsBean.setAppName(NullToString(rs.getString("ģ��")));
		appStatisticsBean.setAppType(NullToString(rs.getString("Ӧ�����")));
		appStatisticsBean.setCount(rs.getInt("Ӧ�ô���"));

		return appStatisticsBean;
	}

	/**
	 * ����ת��Ϊ�ַ���
	 * 
	 * @param date
	 * @return
	 */
	private String dateToString(Date date) {
		try {
			if (date != null) {
				return sdf.format(date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * ��null ת�� ''
	 * 
	 * @param value
	 * @return
	 */
	private String NullToString(String value) {
		if (value == null) {
			return "";
		} else {
			return value;
		}
	}
}
