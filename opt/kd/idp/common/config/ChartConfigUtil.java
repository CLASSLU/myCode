package kd.idp.common.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kd.idp.common.consts.TableConst;

import com.spring.dbservice.DBTemplate;

public class ChartConfigUtil {

	
	/**
	 * ����ԴBEAN��������
	 */
	public static String JDBC_BEAN_NAME = "JdbcTemplate";
	

	
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
			List<Map<String, Object>> list = DBTemplate.getInstance().getResultMapList("SELECT ����,����ֵ FROM "+TableConst.TABLE_CONFIG_MANAGE + " WHERE ����='����'");
			for(int i=0;i<list.size();i++){
				System.out.println("�������ò��� : "+list.get(i).get("����") + " , ����ֵ : " + list.get(i).get("����ֵ"));
				parameters.put(String.valueOf(list.get(i).get("����")), String.valueOf(list.get(i).get("����ֵ")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * ����ID
	 */
	public static String getCurveId(){
		return getConfigParameter("CURVE_ID");
	}
	/**
	 * ����ͼƬ·��
	 */
	public static String getPortalChartPath(){
		return getConfigParameter("PortalChartPath");
	}
	/**
	 * ��������
	 */
	public static int getGroupNums(){
		try {
			return Integer.parseInt(getConfigParameter("groupNums"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 5;
	}
	/**
	 * ÿ��������������
	 */
	public static String getPerGroupNums(){
		return getConfigParameter("perGroupNums");
	}
	/**
	 * �Ƿ���ʾ����ҳ
	 */
	public static String getIsPortalDisplay(){
		return getConfigParameter("isPortalDisplay");
	}
	/**
	 * ������Դ
	 */
	public static String getDatalocation(){
		return getConfigParameter("datalocation");
	}
	/**
	 * ����ҵ������
	 */
	public static String getGraphcategory(){
		return getConfigParameter("graphcategory");
	}
	/**
	 * �����ID�б�
	 */
	public static String getIddescrLists(){
		return getConfigParameter("iddescrLists");
	}
	/**
	 * ��ʼʱ��
	 */
	public static String getStartDates(){
		return getConfigParameter("startDates");
	}
	/**
	 * ����ʱ��
	 */
	public static String getEndDates(){
		return getConfigParameter("endDates");
	}
	
	/**
	 * ����������
	 */
	public static String getGroupTitles(){
		return getConfigParameter("groupTitles");
	}
	/**
	 * �����ӱ���
	 */
	public static String getSubTitles(){
		return getConfigParameter("subTitles");
	}
	/**
	 * ���ڸ�ʽ
	 */
	public static String getDateFormat(){
		return getConfigParameter("dateFormat");
	}
	/**
	 * ʱ��̶�����
	 */
	public static String getDateTickType(){
		return getConfigParameter("dateTickType");
	}
	/**
	 * ʱ��̶ȼ��
	 */
	public static String getDateTick(){
		return getConfigParameter("dateTick");
	}
	/**
	 * �Ƿ񴴽�ͼ��
	 */
	public static String getCreateLegend(){
		return getConfigParameter("createLegend");
	}
	/**
	 * ����λ��
	 */
	public static String getPoistion(){
		return getConfigParameter("poistion");
	}
	/**
	 * x���Ƿ���㿪ʼ
	 */
	public static String getxStartZero(){
		return getConfigParameter("xStartZero");
	}
	/**
	 * y���Ƿ���㿪ʼ
	 */
	public static String getyStartZero(){
		return getConfigParameter("yStartZero");
	}
	/**
	 * ʱ������С�̶�
	 */
	public static String getTimeaxisdatescale(){
		return getConfigParameter("timeaxisdatescale");
	}
	/**
	 * ʱ��������
	 */
	public static String getTimeaxistimetype(){
		return getConfigParameter("timeaxistimetype");
	}
	/**
	 * ��ק��ʼ��
	 */
	public static String getEventstartorder(){
		return getConfigParameter("eventstartorder");
	}
	/**
	 * ��ק������
	 */
	public static String getEventendorder(){
		return getConfigParameter("eventendorder");
	}
	/**
	 * ͼ����
	 */
	public static String getChartwidth(){
		return getConfigParameter("chartwidth");
	}
	/**
	 * ͼ��߶�
	 */
	public static String getChartheight(){
		return getConfigParameter("chartheight");
	}
	/**
	 * ͼ�����ݿ�ģʽ
	 */
	public static String getChartschema(){
		return getConfigParameter("chartschema");
	}
	/**
	 * ͼ�����ݱ�
	 */
	public static String getCharttable(){
		return getConfigParameter("charttable");
	}
	/**
	 *ͼ������
	 */
	public static String getCharttype(){
		return getConfigParameter("charttype");
	}
	/**
	 * ��������
	 */
	public static String getJfreechartlinestyle(){
		return getConfigParameter("jfreechartlinestyle");
	}
	/**
	 * ������ɫ��ʽ
	 */
	public static String getSeriesPaint(){
		return getConfigParameter("seriesPaint");
	}
	public static Map<String, String> getParameters() {
		return parameters;
	}

	public static void setParameters(Map<String, String> _parameters) {
		parameters = _parameters;
	}
	
}
