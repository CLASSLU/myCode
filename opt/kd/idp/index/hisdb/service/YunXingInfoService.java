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
			resultMap.put("����",dao.getGzList());
			resultMap.put("����", dao.getJxList());
			//resultMap.put("����", dao.getHqList());
			resultMap.put("ˮ��", dao.getSqList());
			//resultMap.put("����", dao.getYxjsList());
			resultMap.put("����", dao.getZzList());
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
