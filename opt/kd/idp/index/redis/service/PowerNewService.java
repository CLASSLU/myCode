package kd.idp.index.redis.service;

import java.util.HashMap;
import java.util.Map;

import kd.idp.index.hisdb.service.PowerCurveService;
import kd.idp.index.redis.bean.Num;
import kd.idp.index.redis.dao.NumDao;
import kd.idp.index.redis.dao.impl.NumDaoImpl;
import net.sf.json.JSONObject;

public class PowerNewService {
	
	private PowerCurveService service = new PowerCurveService();
	
	/**
	 * 将redis中的电力曲线数据返回
	 * @return
	 */
	public  Map<String,Object> lineBack(){
		Map<String, Object> powerLineMap =new HashMap<String,Object>();
		NumDao numdao=new NumDaoImpl();
		powerLineMap=numdao.findLine();
		return powerLineMap;
	}
	/**
	 * 将redis中的电力和电量数据返回
	 * @return
	 */
	public  Map<String,Object> back(){
		Map<String, Object> powerNumMap =new HashMap<String,Object>();
		NumDao numdao=new NumDaoImpl();
		powerNumMap=numdao.find();
		return powerNumMap;
	}
	/**
	 * 将DM数据库数据存入到redis中
	 */
	public  void add(){
		   Num num=new Num();
		   Map<String,Object> redisNum=service.getNowpower();
	       num.setHour(redisNum.get("时").toString());
	       num.setMinute(redisNum.get("分").toString()); 
	       num.setProducepower(redisNum.get("当前负荷值").toString());
		   NumDao numdao = new NumDaoImpl();
	       numdao.save(num);
		}
	/**
	 * 将电力曲线存入redis中
	 */
	public void addLine(){
		   Num num=new Num();
		   num.setLine(JSONObject.fromObject(service.getPowerCurve()).toString());
		   NumDao numdao=new NumDaoImpl();
		   numdao.saveLine(num);
	}
}
