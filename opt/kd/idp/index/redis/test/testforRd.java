package kd.idp.index.redis.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.spring.SpringBeanFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class testforRd {
	    public static void main(String[] args) {
		//��ʵ����Ŀ��ʹ��ע��ע�뵽����..����serviceע�뵽Action����һ��..
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
