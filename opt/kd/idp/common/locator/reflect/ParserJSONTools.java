package kd.idp.common.locator.reflect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * ����JSON�ַ���,����java����
 * 
 * @author caoyu
 *
 */
@SuppressWarnings("unchecked")
public class ParserJSONTools {

	
	private String jsonStr ;
	
	public ParserJSONTools(String _jsonStr) {
		// TODO Auto-generated constructor stub
		this.jsonStr = _jsonStr;
	}
	
	/**
	 * ��json ת���� �����б�
	 * ["a",1,["q","w","e"],{"key":"value"}]
	 */
	public ArrayList<Object> parserJSON(){
		ArrayList<Object> params = new ArrayList<Object>();
		try {
			if(this.jsonStr != null && !"".equals(this.jsonStr.trim())){
				JSONObject jsonObj = JSONObject.fromObject("{\"jsonObject\":"+this.jsonStr+"}");
				JSONArray array = jsonObj.getJSONArray("jsonObject");
				params = JSONArraytoArrayList(array);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return params;
	}
	
	/**
	 * ��JSONArray ת���� ArrayList
	 * @param item
	 * @return
	 */
	public ArrayList<Object> JSONArraytoArrayList(JSONArray item){
		ArrayList<Object> list = new ArrayList<Object>();
		try {
			for(Object obj : item){
				//�ַ���
				if(obj instanceof String){
					list.add(obj);
				}
				//���� - int 
				else if(obj instanceof Integer){
					list.add(Integer.parseInt(obj.toString()));
				}
				//����
				else if(obj instanceof JSONArray){
					list.add(JSONArraytoArrayList((JSONArray)obj));
				}
				//json���� - map 
				else if(obj instanceof JSONObject){
					list.add(JSONObjecttoMap((JSONObject)obj));
				}
				else{
					//���� - Double
					//���� - Float 
					//������ - Boolean
					//����
					list.add(obj);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * ��JSONObject ת���� Map
	 * @param item
	 * @return
	 */
	public Object JSONObjecttoMap(JSONObject item){
		Object result = new Object();
		try {
			Map<Object, Object> map = new HashMap<Object, Object>();
			Iterator keys = item.keys();
			while(keys.hasNext()){
				Object key= keys.next();
				Object value= item.get(key);
				if(value instanceof JSONObject){
					map.put(key, JSONObjecttoMap((JSONObject)value));
				}else if(value instanceof JSONArray){
					map.put(key, JSONArraytoArrayList((JSONArray)value));
				}
				else{
					map.put(key, value);
				}
			}
			result = map;
			if(map.get("_RemoteClass") != null){
				//ת���Զ���BEAN����
				result = ReflectService.getBeanObject(map, map.get("_RemoteClass").toString());
			}else if(map.get("_ObjectClass") != null){
				//ת�� ���Ӷ���
				result = ReflectService.getComplexObject(map, map.get("_ObjectClass").toString());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}
	
}
