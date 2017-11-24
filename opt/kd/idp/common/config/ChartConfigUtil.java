package kd.idp.common.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kd.idp.common.consts.TableConst;

import com.spring.dbservice.DBTemplate;

public class ChartConfigUtil {

	
	/**
	 * 数据源BEAN对象名称
	 */
	public static String JDBC_BEAN_NAME = "JdbcTemplate";
	

	
	/**
	 * 系统配置参数
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
	 * 初始化 配置
	 * @return
	 */
	public static boolean loadConfigParameter(){
		try {
			List<Map<String, Object>> list = DBTemplate.getInstance().getResultMapList("SELECT 属性,属性值 FROM "+TableConst.TABLE_CONFIG_MANAGE + " WHERE 类型='曲线'");
			for(int i=0;i<list.size();i++){
				System.out.println("曲线配置参数 : "+list.get(i).get("属性") + " , 属性值 : " + list.get(i).get("属性值"));
				parameters.put(String.valueOf(list.get(i).get("属性")), String.valueOf(list.get(i).get("属性值")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * 曲线ID
	 */
	public static String getCurveId(){
		return getConfigParameter("CURVE_ID");
	}
	/**
	 * 曲线图片路径
	 */
	public static String getPortalChartPath(){
		return getConfigParameter("PortalChartPath");
	}
	/**
	 * 曲线组数
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
	 * 每组曲线曲线条数
	 */
	public static String getPerGroupNums(){
		return getConfigParameter("perGroupNums");
	}
	/**
	 * 是否显示在首页
	 */
	public static String getIsPortalDisplay(){
		return getConfigParameter("isPortalDisplay");
	}
	/**
	 * 数据来源
	 */
	public static String getDatalocation(){
		return getConfigParameter("datalocation");
	}
	/**
	 * 曲线业务类型
	 */
	public static String getGraphcategory(){
		return getConfigParameter("graphcategory");
	}
	/**
	 * 量测电ID列表
	 */
	public static String getIddescrLists(){
		return getConfigParameter("iddescrLists");
	}
	/**
	 * 开始时间
	 */
	public static String getStartDates(){
		return getConfigParameter("startDates");
	}
	/**
	 * 结束时间
	 */
	public static String getEndDates(){
		return getConfigParameter("endDates");
	}
	
	/**
	 * 曲线组名称
	 */
	public static String getGroupTitles(){
		return getConfigParameter("groupTitles");
	}
	/**
	 * 曲线子标题
	 */
	public static String getSubTitles(){
		return getConfigParameter("subTitles");
	}
	/**
	 * 日期格式
	 */
	public static String getDateFormat(){
		return getConfigParameter("dateFormat");
	}
	/**
	 * 时间刻度类型
	 */
	public static String getDateTickType(){
		return getConfigParameter("dateTickType");
	}
	/**
	 * 时间刻度间隔
	 */
	public static String getDateTick(){
		return getConfigParameter("dateTick");
	}
	/**
	 * 是否创建图例
	 */
	public static String getCreateLegend(){
		return getConfigParameter("createLegend");
	}
	/**
	 * 生成位置
	 */
	public static String getPoistion(){
		return getConfigParameter("poistion");
	}
	/**
	 * x轴是否从零开始
	 */
	public static String getxStartZero(){
		return getConfigParameter("xStartZero");
	}
	/**
	 * y轴是否从零开始
	 */
	public static String getyStartZero(){
		return getConfigParameter("yStartZero");
	}
	/**
	 * 时间轴最小刻度
	 */
	public static String getTimeaxisdatescale(){
		return getConfigParameter("timeaxisdatescale");
	}
	/**
	 * 时间轴类型
	 */
	public static String getTimeaxistimetype(){
		return getConfigParameter("timeaxistimetype");
	}
	/**
	 * 拖拽起始点
	 */
	public static String getEventstartorder(){
		return getConfigParameter("eventstartorder");
	}
	/**
	 * 拖拽结束点
	 */
	public static String getEventendorder(){
		return getConfigParameter("eventendorder");
	}
	/**
	 * 图像宽度
	 */
	public static String getChartwidth(){
		return getConfigParameter("chartwidth");
	}
	/**
	 * 图像高度
	 */
	public static String getChartheight(){
		return getConfigParameter("chartheight");
	}
	/**
	 * 图形数据库模式
	 */
	public static String getChartschema(){
		return getConfigParameter("chartschema");
	}
	/**
	 * 图形数据表
	 */
	public static String getCharttable(){
		return getConfigParameter("charttable");
	}
	/**
	 *图形类型
	 */
	public static String getCharttype(){
		return getConfigParameter("charttype");
	}
	/**
	 * 曲线类型
	 */
	public static String getJfreechartlinestyle(){
		return getConfigParameter("jfreechartlinestyle");
	}
	/**
	 * 曲线颜色样式
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
