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
	 * @param path Ϊ WebRoot �� Ŀ¼
	 * @param fileName
	 * @return
	 */
	public static Properties getWebRootProperty(String path,String fileName){
		return openPropertyFile(root_dir + path, fileName);
	}
	
	/**
	 * @param path Ϊ Scr �� Ŀ¼
	 * @param fileName
	 * @return
	 */
	public static Properties getSrcProperty(String path,String fileName){
		return openPropertyFile(class_dir + path, fileName);
	}
	
	/**
	 * ��ȡ ���·��  ϵͳĬ�������ļ�  pen/self/properties/file/default.properties �ļ�
	 * @return
	 */
	public static Properties getdefaultProperty(){
		return openPropertyFile(class_dir + file_dir , defaultProperties);
	}
	
	/**
	 *  ��ȡ ���·��  ϵͳ�涨�� Ĭ��·��  pen/self/properties/file/*.properties �ļ�
	 * @param fileName
	 * @return
	 */
	public static Properties getdefaultPathProperty(String fileName){
		return openPropertyFile(class_dir + file_dir , fileName);
	}
	
	/**
	 * ��ȡ ����·���µ�  *.properties �ļ�
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
	 * @param filePath	�ļ�����·��
	 * @param comments 	�ļ�ע��
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
		
//		//  ��ȡϵͳĬ�� �������ļ�
//		prop = PropertiesTool.getdefaultProperty();
//		System.out.println(prop.getProperty("��ǰ·��"));
//		
//		//  ��ȡ��  ·���� �� �����ļ�����
//		prop =	PropertiesTool.getSrcProperty("pen/self/properties/file/","default.properties");
//		System.out.println(prop.getProperty("��ǰ·��"));
//	
//		//  ��ȡĬ�� ·���µ������ļ�
//		prop =	PropertiesTool.getdefaultPathProperty("config.properties");
//		System.out.println(prop.getProperty("˵��"));
//		
//		//  ��ȡĬ�� ����·���µ������ļ�
//		prop = PropertiesTool.openPropertyFile("D:/WorkSpace/Hunan/java/Collect/src/pen/self/properties/file/", "config.properties");
//		System.out.println(prop.getProperty("˵��"));
		
//		prop.put("�ļ����·��", "D:/test/pms/");
//		prop.put("���յ�ַ", "����1.omshunan");
//		prop.put("���͵�ַ", "����3.omshuazhong");
//		PropertiesTool.createPropertiesFile(prop, "D:/WorkSpace/Hunan/java/psoms/src/kd/idp/psidpflexapp/flowTransf/pms//config.properties", "����E�ļ� �� ��ͷ�ļ������������Ϣ ");
		
		System.out.println(root_dir);
		System.out.println(class_dir);
		
	}
}
