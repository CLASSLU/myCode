package kd.idp.cms.bean.system;

import java.util.Date;

public class TimerTaskBean {

	/**
	 * ����ID
	 */
	private String taskId = null;
	
	/**
	 * ��������
	 */
	private String taskName = null;
	
	/**
	 * ������
	 */
	private String className = null;
	
	/**
	 * ������
	 */
	private String methodName = null;
	
	/**
	 * ��ʱ����
	 */
	private String timerType = null;
	
	/**
	 * ʱ��
	 */
	private String vertime = null;
	
	/**
	 * ����ʱ��
	 */
	private Date startTime = null;
	
	/**
	 * ����ʱ��
	 */
	private Date stopTime = null;
	
	/**
	 * ����
	 */
	private int period = 0;
	
	/**
	 * ��λ
	 */
	private String units = null;
	
	/**
	 * ״̬
	 */
	private int status = 0;
	
	/**
	 * ��������
	 */
	private String taskDesc = null;

	

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskDesc() {
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getTimerType() {
		return timerType;
	}

	public void setTimerType(String timerType) {
		this.timerType = timerType;
	}

	public String getVertime() {
		return vertime;
	}

	public void setVertime(String vertime) {
		this.vertime = vertime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStopTime() {
		return stopTime;
	}

	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}
	
}
