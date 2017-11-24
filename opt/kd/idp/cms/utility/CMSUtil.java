package kd.idp.cms.utility;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kd.idp.cms.bean.portal.NewsBean;
import kd.idp.cms.bean.priv.PrivBean;
import kd.idp.common.CommonTools;
import kd.idp.common.config.WebConfigUtil;
import kd.idp.common.consts.TableConst;

import com.spring.ServiceManager;
import com.spring.dbservice.DBTemplate;

public class CMSUtil {

	/**
	 * 获得 文件 (文档资料) 存储路径
	 * 
	 * @param privId
	 * @return
	 */
	public static String getSourceStorePath(String privId) {
		return getFileStorePath(privId, "source");
	}

	/**
	 * 获得 文件 (新闻) 存储路径
	 * 
	 * @param privId
	 * @return
	 */
	public static String getNewsStorePath(String privId) {
		return getFileStorePath(privId, "news");
	}

	/**
	 * 获得 文件 (专业动态) 存储路径
	 * 
	 * @param privId
	 * @return
	 */
	public static String getProStorePath(String privId) {
		return getFileStorePath(privId, "pro");
	}

	/**
	 * 获得 文件 (文档资料/新闻) 存储路径
	 * 
	 * @param privId
	 * @param fileType
	 *            news 新闻, source 文档资料 pro 专业动态
	 * @return
	 */
	private static String getFileStorePath(String privId, String fileType) {
//		String newspath = "";
//		try {
//			if (privId != null && !"".equals(privId)) {
//				String rootId = "";
//				if ("news".equals(fileType)) {
//					rootId = WebConfigUtil.getNewsTreeRoot();
//				} else if ("pro".equals(fileType)) {
//					rootId = WebConfigUtil.getProfessionTreeRoot();
//				} else {
//					rootId = WebConfigUtil.getSourceTreeRoot();
//				}
//				PrivDao privdao = ServiceManager.getPrivDao();
//				List<PrivBean> privs = privdao.getParentPrivInfo(privId, false);
//				boolean flag = false;
//				for (int i = privs.size() - 1; i >= 0; i--) {
//					if (rootId.equals(privs.get(i).getPrivId())) {
//						flag = true;
//					}
//					if (flag) {
//						newspath += privs.get(i).getPrivName() + "/";
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return newspath;
		return privId;
	}
	
	
	/**
	 * 同步新闻
	 * @param news
	 * @return
	 */
	public static boolean sendNewsToSync(NewsBean news){
		String sql = "SELECT * FROM " + TableConst.TABLE_NEWS
				+ " WHERE 内容ID = '" + news.getNewsId() + "'";
		Map<String, Object> map = DBTemplate.getInstance()
				.getResultMap(sql);
		ArrayList<String> sqlList = new ArrayList<String>();
		Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
		String insSql = "INSERT INTO " + TableConst.D_SYNC_SENDTEXT	+ " (ID,列名,数据,时间,数据类型) VALUES (";
		StringBuffer sb = null;
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			sb = new StringBuffer();
			sb.append(insSql);
			sb.append("'"+news.getNewsId()+"',");
			sb.append("'"+entry.getKey()+"',");
			if ("链接地址".equals(entry.getKey())) {
				sb.append("'"+WebConfigUtil.getProjectIp()+ "/"+CommonTools.getContextPath()+"/"+String.valueOf(entry.getValue())+"',");
			} else if("关联权限".equals(entry.getKey())){
				PrivBean priv = ServiceManager.getPrivDao().getPrivInfo(String.valueOf(entry.getValue()));
				sb.append("'"+priv.getPrivName()+"',");
			}else {
				sb.append("'"+String.valueOf((entry.getValue() == null)?"":entry.getValue())+"',");
			}
			sb.append("sysdate,");
			sb.append("'"+getObjectType(entry.getValue())+"'");
			sb.append(")");
			sqlList.add(sb.toString());
		}
		ArrayList<String> updateSqls = new ArrayList<String>();
		updateSqls.add("DELETE FROM "+TableConst.D_SYNC_SENDTEXT + " WHERE ID = '"+news.getNewsId()+"'");
		updateSqls.addAll(sqlList);
		if(sqlList.size() > 0){
			ServiceManager.getPortalInfoDao().updateSyncSQL(CommonTools.listToArray(updateSqls));
		}
		return false;
	}
	
	public static String getObjectType(Object value){
		if(value == null){
			return "NULL";
		}
		if(value instanceof Integer){
			return "Integer";
		}else if(value instanceof Timestamp){
			return "Timestamp";
		}else if(value instanceof BigDecimal){
			return "Integer";
		}else{
			return "String";
		}
	}
	

	
	private static String getSubSql(Map<String, Object> item){
		if(item != null){
			String value = String.valueOf(item.get("数据"));
			String type = String.valueOf(item.get("数据类型"));
			if("NULL".equals(type)){
				return "''";
			}else if("Integer".equals(type)){
				return value;
			}else if("Timestamp".equals(type)){
				return "'"+value+"'";
			}else{
				return "'"+value+"'";
			}
		}else{
			return "''";
		}
	}
	
	/**
	 * 接收同步新闻内容
	 * @return
	 */
	public static boolean recvNewsFromSync(String id,String name){
		String sql = "SELECT * FROM " + TableConst.D_SYNC_RECVTEXT
				+ " ORDER BY ID";
		List<Map<String, Object>> list = DBTemplate.getInstance().getResultMapList(sql);
		List<Map<String, Map<String, Object>>> newsMapList = new ArrayList<Map<String, Map<String, Object>>>();
		List<String> newsIDList = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			if (newsIDList.size() == 0 || !newsIDList.get(newsIDList.size() - 1).equals(map.get("ID"))){
				newsIDList.add(String.valueOf(map.get("ID")));
				Map<String, Map<String, Object>> itemMap = new HashMap<String, Map<String, Object>>();
				newsMapList.add(itemMap);
			}
			newsMapList.get(newsMapList.size()-1).put(String.valueOf(map.get("列名")), map);
		}
		String inssql = "INSERT INTO "
				+ TableConst.TABLE_NEWS
				+ " (内容ID,标题,类型,内容,图片,附件,链接地址,状态,顺序,创建人ID,创建人,创建时间,存储路径,关联权限,审核人ID,审核人,审核时间,失效时间,浏览权限) VALUES (";
		StringBuffer sb = null;
		ArrayList<String> sqlList = new ArrayList<String>();
		for (int j = 0; j < newsMapList.size(); j++) {
			Map<String, Map<String, Object>> newsItem = newsMapList.get(j);
			List<PrivBean> privList = ServiceManager.getPrivDao()
					.getPrivInfosWithoutAttr(
							String.valueOf(newsItem.get("关联权限").get("数据")));
			for (int k = 0; k < privList.size(); k++) {
				sb = new StringBuffer();
				sb.append(inssql);
				sb.append("'"+newsIDList.get(j) + "_" + k + "',");
				sb.append(getSubSql(newsItem.get("标题")) + ",");
				sb.append(getSubSql(newsItem.get("类型")) + ",");
				sb.append(getSubSql(newsItem.get("内容")) + ",");
				sb.append(getSubSql(newsItem.get("图片")) + ",");
				sb.append(getSubSql(newsItem.get("附件")) + ",");
				sb.append(getSubSql(newsItem.get("链接地址")) + ",");
				sb.append(getSubSql(newsItem.get("状态")) + ",");
				sb.append(getSubSql(newsItem.get("顺序")) + ",");
				sb.append(getSubSql(newsItem.get("创建人ID")) + ",");
				sb.append(getSubSql(newsItem.get("创建人")) + ",");
				sb.append(getSubSql(newsItem.get("创建时间")) + ",");
				sb.append(getSubSql(newsItem.get("存储路径")) + ",");
				sb.append("'"+privList.get(k).getPrivId() + "',");
				sb.append(getSubSql(newsItem.get("审核人ID")) + ",");
				sb.append(getSubSql(newsItem.get("审核人")) + ",");
				sb.append(getSubSql(newsItem.get("审核时间")) + ",");
				sb.append(getSubSql(newsItem.get("失效时间")) + ",");
				sb.append(getSubSql(newsItem.get("浏览权限")) + ")");
				sqlList.add(sb.toString());
			}
		}
		ArrayList<String> updateSqls = new ArrayList<String>();
		for (int i = 0; i < newsIDList.size(); i++) {
			updateSqls.add("DELETE FROM " + TableConst.TABLE_NEWS + " WHERE 内容ID like '%" + newsIDList.get(i) + "%'");
			updateSqls.add("DELETE FROM " + TableConst.D_SYNC_RECVTEXT + " WHERE ID='" + newsIDList.get(i) + "'");
		}
		updateSqls.addAll(sqlList);
		if(sqlList.size() > 0){
			ServiceManager.getPortalInfoDao().updateSyncSQL(CommonTools.listToArray(updateSqls));
		}
		return true;
	}
	
	
	
	public static void main(String[] args) {
		try {
			CMSUtil.recvNewsFromSync("", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
