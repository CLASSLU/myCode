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
		timer.setTaskId(rs.getString("����ID"));
		timer.setTaskName(rs.getString("��������"));
		timer.setClassName(rs.getString("������"));
		timer.setMethodName(rs.getString("������"));
		timer.setTimerType(rs.getString("��ʱ����"));
		timer.setStatus(rs.getInt("״̬"));
		timer.setStartTime((rs.getDate("����ʱ��") == null)?new Date():rs.getDate("����ʱ��"));
		timer.setStopTime(rs.getDate("ֹͣʱ��"));
		timer.setPeriod(rs.getInt("����"));
		timer.setVertime(dateToString(rs.getDate("ʱ��")));
		timer.setUnits(rs.getString("��λ"));
		timer.setTaskDesc(rs.getString("��������"));
		return timer;
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
