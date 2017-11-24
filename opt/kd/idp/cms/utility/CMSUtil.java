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
	 * ��� �ļ� (�ĵ�����) �洢·��
	 * 
	 * @param privId
	 * @return
	 */
	public static String getSourceStorePath(String privId) {
		return getFileStorePath(privId, "source");
	}

	/**
	 * ��� �ļ� (����) �洢·��
	 * 
	 * @param privId
	 * @return
	 */
	public static String getNewsStorePath(String privId) {
		return getFileStorePath(privId, "news");
	}

	/**
	 * ��� �ļ� (רҵ��̬) �洢·��
	 * 
	 * @param privId
	 * @return
	 */
	public static String getProStorePath(String privId) {
		return getFileStorePath(privId, "pro");
	}

	/**
	 * ��� �ļ� (�ĵ�����/����) �洢·��
	 * 
	 * @param privId
	 * @param fileType
	 *            news ����, source �ĵ����� pro רҵ��̬
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
	 * ͬ������
	 * @param news
	 * @return
	 */
	public static boolean sendNewsToSync(NewsBean news){
		String sql = "SELECT * FROM " + TableConst.TABLE_NEWS
				+ " WHERE ����ID = '" + news.getNewsId() + "'";
		Map<String, Object> map = DBTemplate.getInstance()
				.getResultMap(sql);
		ArrayList<String> sqlList = new ArrayList<String>();
		Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
		String insSql = "INSERT INTO " + TableConst.D_SYNC_SENDTEXT	+ " (ID,����,����,ʱ��,��������) VALUES (";
		StringBuffer sb = null;
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			sb = new StringBuffer();
			sb.append(insSql);
			sb.append("'"+news.getNewsId()+"',");
			sb.append("'"+entry.getKey()+"',");
			if ("���ӵ�ַ".equals(entry.getKey())) {
				sb.append("'"+WebConfigUtil.getProjectIp()+ "/"+CommonTools.getContextPath()+"/"+String.valueOf(entry.getValue())+"',");
			} else if("����Ȩ��".equals(entry.getKey())){
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
			String value = String.valueOf(item.get("����"));
			String type = String.valueOf(item.get("��������"));
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
	 * ����ͬ����������
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
			newsMapList.get(newsMapList.size()-1).put(String.valueOf(map.get("����")), map);
		}
		String inssql = "INSERT INTO "
				+ TableConst.TABLE_NEWS
				+ " (����ID,����,����,����,ͼƬ,����,���ӵ�ַ,״̬,˳��,������ID,������,����ʱ��,�洢·��,����Ȩ��,�����ID,�����,���ʱ��,ʧЧʱ��,���Ȩ��) VALUES (";
		StringBuffer sb = null;
		ArrayList<String> sqlList = new ArrayList<String>();
		for (int j = 0; j < newsMapList.size(); j++) {
			Map<String, Map<String, Object>> newsItem = newsMapList.get(j);
			List<PrivBean> privList = ServiceManager.getPrivDao()
					.getPrivInfosWithoutAttr(
							String.valueOf(newsItem.get("����Ȩ��").get("����")));
			for (int k = 0; k < privList.size(); k++) {
				sb = new StringBuffer();
				sb.append(inssql);
				sb.append("'"+newsIDList.get(j) + "_" + k + "',");
				sb.append(getSubSql(newsItem.get("����")) + ",");
				sb.append(getSubSql(newsItem.get("����")) + ",");
				sb.append(getSubSql(newsItem.get("����")) + ",");
				sb.append(getSubSql(newsItem.get("ͼƬ")) + ",");
				sb.append(getSubSql(newsItem.get("����")) + ",");
				sb.append(getSubSql(newsItem.get("���ӵ�ַ")) + ",");
				sb.append(getSubSql(newsItem.get("״̬")) + ",");
				sb.append(getSubSql(newsItem.get("˳��")) + ",");
				sb.append(getSubSql(newsItem.get("������ID")) + ",");
				sb.append(getSubSql(newsItem.get("������")) + ",");
				sb.append(getSubSql(newsItem.get("����ʱ��")) + ",");
				sb.append(getSubSql(newsItem.get("�洢·��")) + ",");
				sb.append("'"+privList.get(k).getPrivId() + "',");
				sb.append(getSubSql(newsItem.get("�����ID")) + ",");
				sb.append(getSubSql(newsItem.get("�����")) + ",");
				sb.append(getSubSql(newsItem.get("���ʱ��")) + ",");
				sb.append(getSubSql(newsItem.get("ʧЧʱ��")) + ",");
				sb.append(getSubSql(newsItem.get("���Ȩ��")) + ")");
				sqlList.add(sb.toString());
			}
		}
		ArrayList<String> updateSqls = new ArrayList<String>();
		for (int i = 0; i < newsIDList.size(); i++) {
			updateSqls.add("DELETE FROM " + TableConst.TABLE_NEWS + " WHERE ����ID like '%" + newsIDList.get(i) + "%'");
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
