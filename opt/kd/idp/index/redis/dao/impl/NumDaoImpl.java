package kd.idp.index.redis.dao.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import kd.idp.index.redis.bean.Num;
import kd.idp.index.redis.dao.NumDao;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.spring.SpringBeanFactory;

public class NumDaoImpl implements NumDao{
     public void save(Num num){
		JedisPool jedisPool = (JedisPool)SpringBeanFactory.getInstance().getBean("jedisPool");
		Jedis client = jedisPool.getResource(); 
		try{  
			client.set("hour",num.getHour());
		    client.set("minute",num.getMinute());
		    client.set("power",num.getProducepower());
		    client.save();
		}catch(Exception e){  
		    e.printStackTrace();  
		}finally{  
		    jedisPool.returnResource(client);//±ÿ–Î–¥  
		}  
		
     }
	public Map<String, Object> find() {
		JedisPool jedisPool = (JedisPool)SpringBeanFactory.getInstance().getBean("jedisPool");
		Jedis client = jedisPool.getResource(); 
		Map<String, Object> powerNewmessage =new HashMap<String,Object>();
		try{  
		    powerNewmessage.put("newhour", client.get("hour"));
		    powerNewmessage.put("newMinute", client.get("minute"));
		    powerNewmessage.put("newPower", client.get("power"));
		}catch(Exception e){  
		    e.printStackTrace();  
		}finally{  
		    jedisPool.returnResource(client);
		}  
		return powerNewmessage;
	}
	public void saveLine(Num num) {
		JedisPool jedisPool = (JedisPool)SpringBeanFactory.getInstance().getBean("jedisPool");
		Jedis client = jedisPool.getResource();
		try{  
			client.set("line",num.getLine());
		    client.save();
		}catch(Exception e){  
		    e.printStackTrace();  
		}finally{  
		    jedisPool.returnResource(client);
		}  
	}
	public  Map<String,Object> findLine(){
		JedisPool jedisPool=(JedisPool)SpringBeanFactory.getInstance().getBean("jedisPool");
		Jedis client=jedisPool.getResource();
		Map<String, Object> powerLinemessage =new HashMap<String,Object>();
		try{
		powerLinemessage.put("numLine", client.get("line"));
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			jedisPool.returnResource(client);
		}
		return powerLinemessage ;
	}
	public static void main(String[] args) {
		NumDaoImpl imp = new NumDaoImpl();
		Num num = new Num();
		num.setHour("12");
		num.setMinute("hh");
		num.setProducepower("oo");
		imp.save(num);
	}
}
