package kd.idp.index.hisdb.service;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.sdjxd.common.export.Dao;

import kd.idp.index.hisdb.dao.YunXingInfoDao;

public class YunXingInfoService {
	
	private YunXingInfoDao dao = new YunXingInfoDao();
	
	public Map<String, Object> getYunXingInfo() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			resultMap.put("故障",dao.getGzList());
			resultMap.put("检修", dao.getJxList());
			//resultMap.put("火情", dao.getHqList());
			resultMap.put("水情", dao.getSqList());
			//resultMap.put("记事", dao.getYxjsList());
			resultMap.put("重载", dao.getZzList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	public static void main(String[] args) {
		YunXingInfoService service = new YunXingInfoService();
		 Map<String, Object>  map = service.getYunXingInfo();
		 JSONObject object = JSONObject.fromObject(map);
		 System.out.println(object.toString());
	}

}
