package kd.idp.common.locator.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 
 * 反射 服务类 
 * 调用方法 , 生成动态bean对象
 * 
 * @author caoyu
 *
 */
@SuppressWarnings("unchecked")
public class ReflectService {
	
	private Class reflectClass;

	private String methodName;
	
	private Object[] paramValues ;
	
	private Class[] paramTypes;
	
	public ReflectService() {
		
	}

	public ReflectService(String _className,String _methodName) {
		try {
			this.reflectClass = Class.forName(_className);
			this.methodName = _methodName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 设置 方法 参数，获得其类型
	 * @param values
	 * @param request
	 * @param response
	 */
	public void setParamValues(ArrayList<Object> values,HttpServletRequest request, HttpServletResponse response){
		try {
			if(values.size() > 0){
				this.paramValues = new Object[values.size()];
				this.paramTypes = new Class[values.size()];
				for(int i=0;i<values.size();i++){
					Object item = values.get(i);
					this.paramValues[i] = item;
					if(item.getClass().getName().equals("java.lang.Integer")){
						this.paramTypes[i] = int.class;
					}else if(item.getClass().getName().equals("java.lang.Double")){
						this.paramTypes[i] = double.class;
					}else if(item.getClass().getName().equals("java.lang.Boolean")){
						this.paramTypes[i] = boolean.class;
					}else if(item.getClass().getName().equals("java.lang.Float")){
						this.paramTypes[i] = float.class;
					}
					else if(item.getClass().getName().equals(ServletRequestReflect.class.getCanonicalName())){
						//request 对象
						this.paramValues[i] = request;
						this.paramTypes[i] = HttpServletRequest.class;
					}
					else if(item.getClass().getName().equals(ServletResponseReflect.class.getCanonicalName())){
						//response 对象
						this.paramValues[i] = response;
						this.paramTypes[i] = HttpServletResponse.class;
					}
					else{
						this.paramTypes[i] = item.getClass();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 调用方法
	 * @return
	 */
	public Object invokeMethod(){
		Object result = new Object();
		try {
			Method method = this.reflectClass.getMethod(this.methodName, this.paramTypes);
			result = method.invoke(this.reflectClass.newInstance(),this.paramValues);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	
	
	/**
	 * map 对象 映射到 bean
	 * @param map
	 * @param beanClassName
	 * @return
	 */
	public static Object getBeanObject(Map<Object, Object> map,String beanClassName){
		Object bean = new Object();
		try {
			Class<?> beanclass = Class.forName(beanClassName);
			bean = beanclass.newInstance();
			Field[] fs = beanclass.getDeclaredFields();
			for(int i=0;i<fs.length;i++){
				String pname = fs[i].getName();
				System.out.println("------- " + beanClassName + " set"+pname.substring(0,1).toUpperCase()+pname.substring(1));
				Object item = map.get(pname);
				Method ms ;
				Class<?> type = null;
				if(item.getClass().getName().equals("java.lang.Integer")){
					type = int.class;
				}else if(item.getClass().getName().equals("java.lang.Double")){
					type = double.class;
				}else if(item.getClass().getName().equals("java.lang.Boolean")){
					type = boolean.class;
				}else{
					type = item.getClass();
				}
				ms = beanclass.getMethod("set"+pname.substring(0,1).toUpperCase()+pname.substring(1), type);
				ms.invoke(bean, map.get(pname));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	/**
	 * 转换为java 复杂类型对象
	 * @param map
	 * @param objClassName
	 * @return
	 */
	public static Object getComplexObject(Map<Object, Object> map,String objClassName){
		try {
			if("java.util.HashMap".equals(objClassName)){
				//HashMap 类型
				HashMap mapobj = new HashMap();
				for (Map.Entry entry : map.entrySet()) {  
					Object key = entry.getKey();  
					if(!"_ObjectClass".equals(key.toString())){
						mapobj.put(key, entry.getValue());
					}
				}  
				return mapobj;
			}else if("java.util.Hashtable".equals(objClassName)){
				//HashMap 类型
				Hashtable tableobj = new Hashtable();
				for (Map.Entry entry : map.entrySet()) {  
					Object key = entry.getKey();  
					if(!"_ObjectClass".equals(key.toString())){
						tableobj.put(key, entry.getValue());
					}
				}  
				return tableobj;
			}
			else if("java.util.Date".equals(objClassName)){
				//date 类型
				return transformDateFromStr(map.get("date").toString(), map.get("format").toString());
			}
			else if("java.util.Float".equals(objClassName)){
				//date 类型
				return Float.parseFloat(map.get("float").toString());
			}
			else if("javax.servlet.http.HttpServletRequest".equals(objClassName) ){
				//request  
				return new ServletRequestReflect();
			}else if("javax.servlet.http.HttpServletResponse".equals(objClassName)){
				//response
				return new ServletResponseReflect();
			}
			else{
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 将字符串转换为日期型
	 * yyyy-mm-dd hh:mm:ss
	 * @param dateStr
	 * @return
	 */
	public static Date transformDateFromStr(String dateStr,String format){
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			date = sdf.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	
	
}
