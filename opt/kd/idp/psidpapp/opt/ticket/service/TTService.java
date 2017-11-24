package kd.idp.psidpapp.opt.ticket.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.spring.SpringBeanFactory;

import kd.idp.psidpapp.opt.sys.serial.service.SerialService;
import kd.idp.psidpapp.opt.ticket.constant.DataSourceName;
import kd.idp.psidpapp.opt.ticket.constant.FieldName;
import kd.idp.psidpapp.opt.ticket.constant.TableName;
import kd.idp.psidpapp.opt.ticket.dao.TTDao;

public class TTService {
	
	private TTDao dao;
	
	public void setDao(TTDao dao) {
		this.dao = dao;
	}

	public boolean addMap() throws Exception{
		Map<String,Object> dataMap = new HashMap<String,Object>();
		dataMap.put("OO", SerialService.getSerialIdByMillisecond("ORG_"));
		dataMap.put("PP", "123");
		dao.addMapTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_OPT_TT, FieldName.FIELD_OPT_TT, dataMap);
		
//		dao.addMapTable(dataSourceName, tablName, propertyNames, viewMap);
		return true;
	}
	
	public static void main(String[] args) {
		try{
			TTService ss = (TTService) SpringBeanFactory.getInstance().getBean("ttService");
			ss.addMap();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
