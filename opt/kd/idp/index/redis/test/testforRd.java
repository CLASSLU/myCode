package kd.idp.index.redis.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.spring.SpringBeanFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class testforRd {
	    public static void main(String[] args) {
		//在实际项目中使用注解注入到类中..就像service注入到Action里面一样..
		JedisPool jedisPool = (JedisPool)SpringBeanFactory.getInstance().getBean("jedisPool");
		Jedis client = jedisPool.getResource();  
		try{  
		    client.select(0);  
		    client.get("2");  
		    System.out.println(client.get("2"));  
		}catch(Exception e){  
		    e.printStackTrace();  
		}finally{  
		    jedisPool.returnResource(client);//must be  
		}  
	    }

}
