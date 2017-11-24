package kd.idp.index.redis.bean;
/*
 * 电力与电量，以及发电曲线的实体类
 */
public class Num {
	private String hour;
	private String minute;
	private String producepower;
	private String line;
	private String redisType;
	public String getRedisType() {
		return redisType;
	}
	public void setRedisType(String redisType) {
		this.redisType = redisType;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public String getMinute() {
		return minute;
	}
	public void setMinute(String minute) {
		this.minute = minute;
	}
	public String getProducepower() {
		return producepower;
	}
	public void setProducepower(String producepower) {
		this.producepower = producepower;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	
}
