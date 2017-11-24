package kd.idp.cms.mapper.system;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kd.idp.cms.bean.system.TimerTaskBean;

import org.springframework.jdbc.core.RowMapper;

public class TimerTaskRowMapper implements RowMapper<TimerTaskBean>{
	
	private SimpleDateFormat sdf = null;
	
	public TimerTaskRowMapper() {
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	public TimerTaskBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		TimerTaskBean timer = new TimerTaskBean();
		timer.setTaskId(rs.getString("任务ID"));
		timer.setTaskName(rs.getString("任务名称"));
		timer.setClassName(rs.getString("类名称"));
		timer.setMethodName(rs.getString("方法名"));
		timer.setTimerType(rs.getString("定时类型"));
		timer.setStatus(rs.getInt("状态"));
		timer.setStartTime((rs.getDate("启动时间") == null)?new Date():rs.getDate("启动时间"));
		timer.setStopTime(rs.getDate("停止时间"));
		timer.setPeriod(rs.getInt("周期"));
		timer.setVertime(dateToString(rs.getDate("时间")));
		timer.setUnits(rs.getString("单位"));
		timer.setTaskDesc(rs.getString("任务描述"));
		return timer;
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
