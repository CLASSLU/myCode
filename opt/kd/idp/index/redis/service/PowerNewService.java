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
	 * ��redis�еĵ����������ݷ���
	 * @return
	 */
	public  Map<String,Object> lineBack(){
		Map<String, Object> powerLineMap =new HashMap<String,Object>();
		NumDao numdao=new NumDaoImpl();
		powerLineMap=numdao.findLine();
		return powerLineMap;
	}
	/**
	 * ��redis�еĵ����͵������ݷ���
	 * @return
	 */
	public  Map<String,Object> back(){
		Map<String, Object> powerNumMap =new HashMap<String,Object>();
		NumDao numdao=new NumDaoImpl();
		powerNumMap=numdao.find();
		return powerNumMap;
	}
	/**
	 * ��DM���ݿ����ݴ��뵽redis��
	 */
	public  void add(){
		   Num num=new Num();
		   Map<String,Object> redisNum=service.getNowpower();
	       num.setHour(redisNum.get("ʱ").toString());
	       num.setMinute(redisNum.get("��").toString()); 
	       num.setProducepower(redisNum.get("��ǰ����ֵ").toString());
		   NumDao numdao = new NumDaoImpl();
	       numdao.save(num);
		}
	/**
	 * ���������ߴ���redis��
	 */
	public void addLine(){
		   Num num=new Num();
		   num.setLine(JSONObject.fromObject(service.getPowerCurve()).toString());
		   NumDao numdao=new NumDaoImpl();
		   numdao.saveLine(num);
	}
}
