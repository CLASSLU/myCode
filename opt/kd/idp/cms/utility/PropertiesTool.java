package kd.idp.cms.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;


public class PropertiesTool{
	
	
	public static final  String root_dir = PropertiesTool.class.getResource("/").getPath().split("WEB-INF")[0];
	public static final  String class_dir = PropertiesTool.class.getResource("/").getPath();
	public static final  String file_dir = "pen/self/properties/file/";	
	public static final  String defaultProperties = "default.properties";
	
	/**
	 * @param path 为 WebRoot 下 目录
	 * @param fileName
	 * @return
	 */
	public static Properties getWebRootProperty(String path,String fileName){
		return openPropertyFile(root_dir + path, fileName);
	}
	
	/**
	 * @param path 为 Scr 下 目录
	 * @param fileName
	 * @return
	 */
	public static Properties getSrcProperty(String path,String fileName){
		return openPropertyFile(class_dir + path, fileName);
	}
	
	/**
	 * 获取 相对路径  系统默认配置文件  pen/self/properties/file/default.properties 文件
	 * @return
	 */
	public static Properties getdefaultProperty(){
		return openPropertyFile(class_dir + file_dir , defaultProperties);
	}
	
	/**
	 *  获取 相对路径  系统规定的 默认路径  pen/self/properties/file/*.properties 文件
	 * @param fileName
	 * @return
	 */
	public static Properties getdefaultPathProperty(String fileName){
		return openPropertyFile(class_dir + file_dir , fileName);
	}
	
	/**
	 * 获取 绝对路径下的  *.properties 文件
	 * @param path
	 * @param fileName
	 * @return
	 */
	public static Properties openPropertyFile(String path,String fileName) {
		Properties props = new Properties();
		FileInputStream is = null;
		try {
			String	propFilePath = path + fileName;
			is = new FileInputStream(path + fileName);
				PropertiesTool.class.getResourceAsStream(propFilePath);
			props.load(is);
			if (is != null) {
				is.close();
			}
			Object[] all = props.stringPropertyNames().toArray();
			for (Object o : all) {
				String s = (String) o;
				String val = props.getProperty(s);
//				val = new String(val.getBytes("ISO8859-1"), "GBK");
				props.setProperty(s, val);
			}
			return props;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}finally{
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
	
	/**
	 * 
	 * @param prop		
	 * @param filePath	文件绝对路径
	 * @param comments 	文件注释
	 * @return
	 */
	public static boolean createPropertiesFile(Properties prop,String filePath,String comments){
		File file = new File(filePath);
		OutputStream out = null;
		try {
			out = new FileOutputStream(file);
			prop.store(out, comments);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	public static String getValueByKey(String key,String path,String fileName ){
		Properties porp = PropertiesTool.openPropertyFile(path,fileName);
		if(porp == null){
			return null;
		}else{
			return (String)porp.get(key);
		}
	}

	public static void main(String[] args) {
		Properties prop = new Properties();
		
//		//  获取系统默认 的配置文件
//		prop = PropertiesTool.getdefaultProperty();
//		System.out.println(prop.getProperty("当前路径"));
//		
//		//  获取包  路径下 的 配置文件爱你
//		prop =	PropertiesTool.getSrcProperty("pen/self/properties/file/","default.properties");
//		System.out.println(prop.getProperty("当前路径"));
//	
//		//  获取默认 路径下的配置文件
//		prop =	PropertiesTool.getdefaultPathProperty("config.properties");
//		System.out.println(prop.getProperty("说明"));
//		
//		//  获取默认 绝对路劲下的配置文件
//		prop = PropertiesTool.openPropertyFile("D:/WorkSpace/Hunan/java/Collect/src/pen/self/properties/file/", "config.properties");
//		System.out.println(prop.getProperty("说明"));
		
//		prop.put("文件输出路径", "D:/test/pms/");
//		prop.put("接收地址", "湖南1.omshunan");
//		prop.put("发送地址", "华中3.omshuazhong");
//		PropertiesTool.createPropertiesFile(prop, "D:/WorkSpace/Hunan/java/psoms/src/kd/idp/psidpflexapp/flowTransf/pms//config.properties", "生成E文件 和 其头文件的相关配置信息 ");
		
		System.out.println(root_dir);
		System.out.println(class_dir);
		
	}
}
