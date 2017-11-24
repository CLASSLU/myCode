package kd.idp.common.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kd.idp.common.consts.TableConst;

import com.spring.dbservice.DBTemplate;

public class WebConfigUtil {

	
	/**
	 * 数据源BEAN对象名称
	 */
	public static String JDBC_BEAN_NAME = "JdbcTemplate";
	
	/**
	 * 未分配组织机构ID
	 */
	public static String UNINITIALIZE = "";
	
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
			List<Map<String, Object>> list = DBTemplate.getInstance().getResultMapList("SELECT 属性,属性值 FROM "+TableConst.TABLE_CONFIG_MANAGE + " WHERE 类型='门户'");
			for(int i=0;i<list.size();i++){
				System.out.println("门户配置参数 : "+list.get(i).get("属性") + " , 属性值 : " + list.get(i).get("属性值"));
				parameters.put(String.valueOf(list.get(i).get("属性")), String.valueOf(list.get(i).get("属性值")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * 门户默认用户
	 */
	public static String getDefaultUser(){
		return getConfigParameter("DEFAULT_USER");
	}
	
	/**
	 * 组织机构树根节点
	 */
	public static String getOrgTreeRootId(){
		return getConfigParameter("ORG_TREE_ROOT");
	}
	
	/**
	 * 权限树根节点
	 */
	public static String getPrivTreeRootId(){
		return getConfigParameter("PRIV_TREE_ROOT");
	}
	
	/**
	 * 新闻树根节点
	 */
	public static String getNewsTreeRoot(){
		return getConfigParameter("NEWS_TREE_ROOT");
	}
	
	
	/**
	 * 文档树根节点
	 */
	public static String getSourceTreeRoot(){
		return getConfigParameter("SOURCE_TREE_ROOT");
	}
	
	/**
	 * 应用树根节点
	 */
	public static String getAppTreeRoot(){
		return getConfigParameter("APP_TREE_ROOT");
	}
	
	/**
	 * IP树根节点
	 */
	public static String getIPTreeRoot(){
		return getConfigParameter("IP_TREE_ROOT");
	}
	
	/**
	 * 组织机构(权限)树节点
	 */
	public static String getOrgPrivNode(){
		return getConfigParameter("ORG_PRIV_NODE");
	}
	
	/**
	 * 网站链接树根节点
	 */
	public static String getLinkTreeRoot(){
		return getConfigParameter("LINK_TREE_ROOT");
	}
	
	/**
	 * 专业动态树根节点
	 * @return
	 */
	public static String getProfessionTreeRoot(){
		return getConfigParameter("PRO_TREE_ROOT");
	}
	
	
	/**
	 * 门户默认模板
	 */
	public static String getWebDefaultModel(){
		return getConfigParameter("WEB_DEFAULT_MODEL");
	}
	
	/**
	 * 门户模板权限根节点
	 */
	public static String getWebModelRootId(){
		return getConfigParameter("WEB_MODEL_ID");
	}
	
	
	/**
	 * FLEX分页表格 每页默认显示页数
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
	 * 文档上传根目录
	 * @return
	 */
	public static String getSourceFilePath(){
		return getConfigParameter("SOURCE_FILE_PATH");
	}
	
	/**
	 * 新闻文件路径
	 * @return
	 */
	public static String getNewsFilePath(){
		return getConfigParameter("NEWS_FILE_PATH");
	}
	
	
	/**
	 * 新闻默认失效时间(日)
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
	 * 图片新闻默认图片
	 * @return
	 */
	public static String getNewsDefaultImage(){
		return getConfigParameter("NEWS_DEFAULT_IMAGE");
	}
	
	/**
	 * 定时任务是否可用
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
	 * 工程IP
	 * @return
	 */
	public static String getProjectIp(){
		return getConfigParameter("PROJECT_IP");
	}
	
	/**
	 * 同步新闻WEB服务IP
	 * @return
	 */
	public static String getSyncNewsWSIp(){
		return getConfigParameter("SYNCNEWS_TO_IP");
	}
	
	
	/**
	 * 个人文件夹根目录
	 * @return
	 */
	public static String getPersonalFilePath(){
		return getConfigParameter("PERSONAL_FILE_PATH");
	}
	
	
	/**
	 * 项目分隔符
	 */
	public final static String SEP_ITEM = "sep;;sep";
	/**
	 * 键值对分隔符
	 */
	public final static String SEP_KEY_VALUE = "sep==sep";


	public static Map<String, String> getParameters() {
		return parameters;
	}

	public static void setParameters(Map<String, String> _parameters) {
		parameters = _parameters;
	}
	
}
