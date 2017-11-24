package kd.idp.index.redis.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kd.idp.index.redis.bean.Redis;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.spring.SpringBeanFactory;

public class RedisDao {
		public  List<Redis> getRedisMessage(){
			JedisPool jedisPool = (JedisPool)SpringBeanFactory.getInstance().getBean("jedisPool");
			Jedis client = jedisPool.getResource(); 
			Set<String> ktv=new HashSet<String>();
			ktv=client.keys("*");
			List<Redis> redisNum=new ArrayList<Redis>();
			for(String str:ktv){
				Redis rs=new Redis();
				rs.setRedisKey(str);
				rs.setRedisValue(client.get(str));
				rs.setRedisType(client.type(str));
				redisNum.add(rs);
			}
			return redisNum;
		}
		public void updateMessage(String newKey,String newValue){
			JedisPool jedisPool = (JedisPool)SpringBeanFactory.getInstance().getBean("jedisPool");
			Jedis client = jedisPool.getResource(); 
			try{
				client.del(newKey);
				client.set(newKey,newValue);
			    client.save();
			    System.out.println(client.get(newKey).toString());
			}catch(Exception e){  
			    e.printStackTrace();  
			}finally{  
			    jedisPool.returnResource(client);//±ÿ–Î–¥  
			}  
		}
		public List<Redis> backSelect(String inputKey){
			JedisPool jedisPool = (JedisPool)SpringBeanFactory.getInstance().getBean("jedisPool");
			Jedis client = jedisPool.getResource(); 
			List<Redis> selectNum=new ArrayList<Redis>();
			Set<String> ktv=new HashSet<String>();
			ktv=client.keys("*");
			for(String str:ktv){
				if(str.contains(inputKey)){
					Redis rs=new Redis();
					rs.setRedisKey(str);
					rs.setRedisValue(client.get(str));
					rs.setRedisType(client.type(str));
					selectNum.add(rs);
				}
			}
			return selectNum;
		}
public static void main(String [] args){
	List<Redis> redisNum1=new ArrayList<Redis>();
	RedisDao rd=new  RedisDao();
	redisNum1=rd.getRedisMessage();
	System.out.println(redisNum1.size());
}
}
