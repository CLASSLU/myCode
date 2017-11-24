package kd.idp.index.redis.service;

import java.util.ArrayList;
import java.util.List;

import kd.idp.index.redis.bean.Redis;
import kd.idp.index.redis.dao.RedisDao;

public class PowerManageService {
	/**
	 * 将redis数据库中的键值对以及数据类型返回
	 * @return
	 */
	public List<Redis> ktvBack(){
		List<Redis> redisList=new ArrayList<Redis>();
		RedisDao rd=new RedisDao();
		redisList=rd.getRedisMessage();
		return redisList;
		
	}
	/**
	 * 将修改过的数据存入redis数据库中
	 * @param key
	 * @param value
	 */
	public void ktvUpdate(String key,String value){
		RedisDao rd=new RedisDao();
		rd.updateMessage(key,value);
	}
	/**
	 * 模糊查询
	 * @param inputKey
	 * @return
	 */
	public List<Redis> selectMessage(String inputKey){
		System.out.println(inputKey.toString());
		List<Redis>  lr=new ArrayList<Redis>();
		RedisDao sm=new RedisDao();
		lr=sm.backSelect(inputKey);
		return lr;
	}
}
