package kd.idp.common.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kd.idp.common.consts.TableConst;

import com.spring.dbservice.DBTemplate;

public class WebConfigUtil {

	
	/**
	 * ����ԴBEAN��������
	 */
	public static String JDBC_BEAN_NAME = "JdbcTemplate";
	
	/**
	 * δ������֯����ID
	 */
	public static String UNINITIALIZE = "";
	
	/**
	 * ϵͳ���ò���
	 */
	private static Map<String, String> parameters = new HashMap<String, String>();
	
	
	/**
	 * @param key
	 * @return
	 */
	private static String getConfigParameter(String key){
		if (parameters.get(key) != null) {
			return parameters.get(key);
		}
		return null;
	}
	
	/**
	 * ��ʼ�� ����
	 * @return
	 */
	public static boolean loadConfigParameter(){
		try {
			List<Map<String, Object>> list = DBTemplate.getInstance().getResultMapList("SELECT ����,����ֵ FROM "+TableConst.TABLE_CONFIG_MANAGE + " WHERE ����='�Ż�'");
			for(int i=0;i<list.size();i++){
				System.out.println("�Ż����ò��� : "+list.get(i).get("����") + " , ����ֵ : " + list.get(i).get("����ֵ"));
				parameters.put(String.valueOf(list.get(i).get("����")), String.valueOf(list.get(i).get("����ֵ")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * �Ż�Ĭ���û�
	 */
	public static String getDefaultUser(){
		return getConfigParameter("DEFAULT_USER");
	}
	
	/**
	 * ��֯���������ڵ�
	 */
	public static String getOrgTreeRootId(){
		return getConfigParameter("ORG_TREE_ROOT");
	}
	
	/**
	 * Ȩ�������ڵ�
	 */
	public static String getPrivTreeRootId(){
		return getConfigParameter("PRIV_TREE_ROOT");
	}
	
	/**
	 * ���������ڵ�
	 */
	public static String getNewsTreeRoot(){
		return getConfigParameter("NEWS_TREE_ROOT");
	}
	
	
	/**
	 * �ĵ������ڵ�
	 */
	public static String getSourceTreeRoot(){
		return getConfigParameter("SOURCE_TREE_ROOT");
	}
	
	/**
	 * Ӧ�������ڵ�
	 */
	public static String getAppTreeRoot(){
		return getConfigParameter("APP_TREE_ROOT");
	}
	
	/**
	 * IP�����ڵ�
	 */
	public static String getIPTreeRoot(){
		return getConfigParameter("IP_TREE_ROOT");
	}
	
	/**
	 * ��֯����(Ȩ��)���ڵ�
	 */
	public static String getOrgPrivNode(){
		return getConfigParameter("ORG_PRIV_NODE");
	}
	
	/**
	 * ��վ���������ڵ�
	 */
	public static String getLinkTreeRoot(){
		return getConfigParameter("LINK_TREE_ROOT");
	}
	
	/**
	 * רҵ��̬�����ڵ�
	 * @return
	 */
	public static String getProfessionTreeRoot(){
		return getConfigParameter("PRO_TREE_ROOT");
	}
	
	
	/**
	 * �Ż�Ĭ��ģ��
	 */
	public static String getWebDefaultModel(){
		return getConfigParameter("WEB_DEFAULT_MODEL");
	}
	
	/**
	 * �Ż�ģ��Ȩ�޸��ڵ�
	 */
	public static String getWebModelRootId(){
		return getConfigParameter("WEB_MODEL_ID");
	}
	
	
	/**
	 * FLEX��ҳ��� ÿҳĬ����ʾҳ��
	 */
	public static int getFlexGridPagingCount(){
		try {
			return Integer.parseInt(getConfigParameter("FLEXGRID_PAGINGG_COUNT"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 20;
	}
	
	
	/**
	 * �ĵ��ϴ���Ŀ¼
	 * @return
	 */
	public static String getSourceFilePath(){
		return getConfigParameter("SOURCE_FILE_PATH");
	}
	
	/**
	 * �����ļ�·��
	 * @return
	 */
	public static String getNewsFilePath(){
		return getConfigParameter("NEWS_FILE_PATH");
	}
	
	
	/**
	 * ����Ĭ��ʧЧʱ��(��)
	 * @return
	 */
	public static int getNewsDisableTime(){
		try {
			return Integer.parseInt(getConfigParameter("NEWS_DEFAULT_DISABLE"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 30;
	}
	
	/**
	 * ͼƬ����Ĭ��ͼƬ
	 * @return
	 */
	public static String getNewsDefaultImage(){
		return getConfigParameter("NEWS_DEFAULT_IMAGE");
	}
	
	/**
	 * ��ʱ�����Ƿ����
	 * @return
	 */
	public static boolean getSystemTimerEnable(){
		if("1".equals(getConfigParameter("SYSTEM_TIMER_ENABLE"))){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * ����IP
	 * @return
	 */
	public static String getProjectIp(){
		return getConfigParameter("PROJECT_IP");
	}
	
	/**
	 * ͬ������WEB����IP
	 * @return
	 */
	public static String getSyncNewsWSIp(){
		return getConfigParameter("SYNCNEWS_TO_IP");
	}
	
	
	/**
	 * �����ļ��и�Ŀ¼
	 * @return
	 */
	public static String getPersonalFilePath(){
		return getConfigParameter("PERSONAL_FILE_PATH");
	}
	
	
	/**
	 * ��Ŀ�ָ���
	 */
	public final static String SEP_ITEM = "sep;;sep";
	/**
	 * ��ֵ�Էָ���
	 */
	public final static String SEP_KEY_VALUE = "sep==sep";


	public static Map<String, String> getParameters() {
		return parameters;
	}

	public static void setParameters(Map<String, String> _parameters) {
		parameters = _parameters;
	}
	
}
