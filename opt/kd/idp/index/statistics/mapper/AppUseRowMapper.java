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
		appStatisticsBean.setDv(NullToString(rs.getString("地区")));
		appStatisticsBean.setDate(dateToString(rs.getDate("日期")));
		appStatisticsBean.setUserName(NullToString(rs.getString("登录人")));
		appStatisticsBean.setUserId(NullToString(rs.getString("USERID")));
		appStatisticsBean.setDepartment(NullToString(rs.getString("处室")));
		appStatisticsBean.setAppName(NullToString(rs.getString("模块")));
		appStatisticsBean.setAppType(NullToString(rs.getString("应用类别")));
		appStatisticsBean.setCount(rs.getInt("应用次数"));

		return appStatisticsBean;
	}

	/**
	 * 日期转换为字符串
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
	 * 将null 转成 ''
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
