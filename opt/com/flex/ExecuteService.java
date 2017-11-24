package com.flex;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.flex.bean.ExecuteBean;
import com.flex.dao.ExecuteServiceDao;
import com.flex.tool.BeanTool;




public class ExecuteService {

//	private static final Logger logger = LoggerFactory
//			.getLogger(ExecuteService.class);
	
	Logger logger = Logger.getLogger(this.getClass());
	private ExecuteServiceDao dao = new ExecuteServiceDao();


//	@SuppressWarnings("unchecked")
//	public Object execute(ExecuteBean bean) {
//		Object result = null;
//		try {
//
//			@SuppressWarnings("unused")
//			Class serviceClass = Class.forName(bean.getClassName());
//			if(bean.getCanshu() == null || bean.getCanshu().length == 0){
//				Method method = serviceClass.getMethod(bean.getMethodName());
//				Object instance = serviceClass.newInstance();
//				result = method.invoke(instance);
//			}else{
//				Object[] canshu = bean.getCanshu();
//				Method method = serviceClass.getMethod(bean.getMethodName(),getCanshuType(canshu));
//				Object instance = serviceClass.newInstance();
//				result = method.invoke(instance,canshu);
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("----- Flex Error :" + bean.getClassName()
//					+ bean.getMethodName() + " Not Found!!");
//			return null;
//		}
//		return result;
//	}
//
//	@SuppressWarnings({ "unused", "unchecked" })
//	private Class[] getCanshuType(Object[] canshu){
//		Class[] result = new Class[canshu.length];
//		for(int i=0; i< result.length; i++){
//			if(canshu[i] == null){
//				result[i] = String.class;
//			}else{
//				Class clazz = canshu[i].getClass();
//				if(canshu[i] instanceof ArrayCollection){
//					clazz = List.class;
//				}else if((canshu[i] instanceof ASObject) || (canshu[i] instanceof ObjectProxy)){
//					clazz = Map.class;
//				}
//				result[i] = clazz;
//			}
//		}
//		return result;
//	}
	
	public Object execute(ExecuteBean bean) {
		Object result = null;
		try {
			result = BeanTool.invokeMethod(bean.getClassName(), bean.getMethodName(), bean.getCanshu());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 通过 主键 删除数据[针对唯一主键]
	 * 
	 * @param tableName
	 * @param primaryKey
	 * @param ids
	 * @return
	 */
	public boolean commonDel(String tableName, String primaryKey, String[] ids) {
		logger.info("----- Flex Info :commonDel  tableName:" + tableName
				+ " ids:" + ids + "");
		return dao.commonDel(tableName, primaryKey, ids);
	}

	public boolean commonAdd(String tableName,Map<String,String> data){
		return dao.commonAdd(tableName, data);
	}
	/**
	 * 带父子关系的数据插入
	 * @param parentTable
	 * @param parentMap
	 * @param childTbale
	 * @param childDatas
	 * @return
	 */
	public boolean commonAddTables(String parentTable,Map<String,String> parentMap,String childTbale,List<Map<String,String>> childDatas){
		return dao.commonAddTables(parentTable, parentMap,childTbale,childDatas);
	}
	
	public boolean commonUpd(String tableName,Map<String,String> data,String primaryKey){
		return dao.commonUpd(tableName, data,primaryKey);
	}
	/**
	 * 根据sql 得到返回的 结果集
	 * 
	 * @param sql
	 * @return
	 */
	public List<Map<String, String>> query(String sql) {
		logger.info("----- Flex Info  sql:" + sql);
		return dao.query(sql);
	}
	/**
	 * 根据sql 得到返回的 一行数据
	 * @param sql
	 * @return
	 */
	public Map<String, String> queryRow(String sql) {
		logger.info("----- Flex Info  sql:" + sql);
		return dao.queryRow(sql);
	}
	/**
	 * 根据sql 得到返回的 一单元格数据
	 * @param sql
	 * @return
	 */
	public String queryCell(String sql) {
		logger.info("----- Flex Info  sql:" + sql);
		return dao.queryCell(sql);
	}
	/**
	 * 执行  sql 
	 * @param sql
	 * @return
	 */
	public boolean executeSql(String sql) {
		logger.info("----- Flex Info  sql:" + sql);
		return dao.executeSql(sql);
	}
	
	public Date getSysDate(){
		return new Date();
	}
	
	public String getUUID(){
		return UUID.randomUUID().toString();
	}
	
	/**
	 * 将 XML文件中的内容用字符串返回
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public String getXML(String filePath,String fileName){
		try {
			return dao.getXML(filePath,fileName);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
