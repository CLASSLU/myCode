package kd.idp.index.redis.bean;
	/*
	 * redis���ݿ��м�ֵ�Ե�ʵ����
	 */
public class Redis {
	private String redisKey;
	private String redisValue;
	private String redisType;
	public String getRedisKey() {
		return redisKey;
	}
	public void setRedisKey(String redisKey) {
		this.redisKey = redisKey;
	}
	public String getRedisValue() {
		return redisValue;
	}
	public void setRedisValue(String redisValue) {
		this.redisValue = redisValue;
	}
	public String getRedisType() {
		return redisType;
	}
	public void setRedisType(String redisType) {
		this.redisType = redisType;
	}
	
}
