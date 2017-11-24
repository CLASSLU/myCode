package kd.idp.index.redis.service;

import java.util.ArrayList;
import java.util.List;

import kd.idp.index.redis.bean.Redis;
import kd.idp.index.redis.dao.RedisDao;

public class PowerManageService {
	/**
	 * ��redis���ݿ��еļ�ֵ���Լ��������ͷ���
	 * @return
	 */
	public List<Redis> ktvBack(){
		List<Redis> redisList=new ArrayList<Redis>();
		RedisDao rd=new RedisDao();
		redisList=rd.getRedisMessage();
		return redisList;
		
	}
	/**
	 * ���޸Ĺ������ݴ���redis���ݿ���
	 * @param key
	 * @param value
	 */
	public void ktvUpdate(String key,String value){
		RedisDao rd=new RedisDao();
		rd.updateMessage(key,value);
	}
	/**
	 * ģ����ѯ
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
