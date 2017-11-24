package kd.idp.cms.dao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kd.idp.cms.bean.PagingGridBean;
import kd.idp.cms.bean.portal.DownloadStatisticsBean;
import kd.idp.cms.bean.portal.LinkBean;
import kd.idp.cms.bean.portal.NewsBean;
import kd.idp.cms.bean.portal.SourceBean;
import kd.idp.cms.bean.priv.UserBean;
import kd.idp.cms.mapper.portal.LinkRowMapper;
import kd.idp.cms.mapper.portal.NewsRowMapper;
import kd.idp.cms.mapper.portal.SourceRowMapper;
import kd.idp.common.CommonTools;
import kd.idp.common.consts.TableConst;

import com.spring.dbservice.DBTemplate;
import com.spring.dbservice.DBTools;

@Transactional
public class PortalInfoDao{

	/**
	 * ��ѯ���ŷ�ҳ����
	 * @param privId
	 * @param pageSize
	 * @param pageNum
	 * @param filtersql
	 * @param isDirect
	 * @return
	 */
	public PagingGridBean<NewsBean> getNewsFromPriv(String privId,
			int pageSize, int pageNum, String filtersql, boolean isDirect) {
		PagingGridBean<NewsBean> newslist = new PagingGridBean<NewsBean>();
		try {
			String subSql = TableConst.TABLE_NEWS;
			if (filtersql != null && !"".equals(filtersql)) {
				subSql = "( SELECT * FROM " + TableConst.TABLE_NEWS + " WHERE "
						+ filtersql + " )";
				//pageNum = 1;
			}
			String sql = "";
			if (isDirect) {
				sql = "select  *  from " + subSql + " where ����Ȩ�� = '" + privId
						//+ "' order by ����ʱ�� desc ";
						//aviyy 2014-3-7 ������ק����	����̨��'˳��'ȡ����
						+ "' order by ˳��  desc ";
			} else {
				sql = "select  *  from " + subSql
						+ " a  where exists ( select Ȩ��ID from "
						+ TableConst.TABLE_MANAGE_PRIV
						+ " b where a.����Ȩ�� = b.Ȩ��ID " + "	start with Ȩ��ID = '"
						+ privId + "' connect by Ȩ�޸�ID = prior Ȩ��ID ) "
						//+" order by ����ʱ�� desc ";
						//aviyy 2014-3-7 ������ק����	����̨��'˳��'ȡ����
						+" order by ˳�� desc ";
			}
			newslist = DBTools.getPagingData(sql, pageSize, pageNum,
					new NewsRowMapper(), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newslist;
	}

	/**
	 * ���� ����
	 * @param news
	 * @return
	 */
	public int addNews(NewsBean news) {
		try {
			String sql = getInsertNewsSql(news);
			return DBTemplate.getInstance().updateSql(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * ͬ������
	 * @param fileList
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { RuntimeException.class })
	public int addNews1(UserBean user, NewsBean news) {
		int maxShowOrder = 0;
		String maxOrderSql = "SELECT MAX(˳��) AS SHOWORDER FROM " + TableConst.TABLE_NEWS
						   + " WHERE ����Ȩ��='" + news.getRelPriv() + "'";
		maxShowOrder = DBTemplate.getInstance().queryForInt(maxOrderSql) + 1;
		news.setOrder(maxShowOrder);
		
		String sql = "INSERT INTO "
				+ TableConst.TABLE_NEWS
				+ " (����ID,����,����,����,ͼƬ,����,���ӵ�ַ,״̬,˳��,������ID,������,�洢·��,����Ȩ��,�����ID,�����,���ʱ��,ʧЧʱ��,���Ȩ��,�Ƿ��ɫ������ʾ,�Ƿ���ʾNEW��ʶ��,�������) "
				+ " VALUES('" + news.getNewsId() + "','"
				+ news.getNewsTitle() + "','" + news.getNewsType()
				+ "','" + news.getNewsContent() + "','"
				+ news.getNewsImage() + "','" + news.getNewsAttach() + "','" + news.getNewsUrl() + "','"
				+ news.getStatus() + "','" 
				+ maxShowOrder + "','" 
				+ user.getUserId() + "','"
				+ user.getUserDisplayName() + "','"
				+ news.getStorePath() + "','" + news.getRelPriv()				
				+ "',null,null,null,null,null,'0','0',0) ";
		//System.out.println(sql);
		return DBTemplate.getInstance().updateSql(sql);
	}	
	
	/**
	 * ���� ���� ��ɾ��
	 * @param news
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { RuntimeException.class })
	public int addNewsWithDelete(NewsBean news) {
		List<NewsBean> newsList = new ArrayList<NewsBean>();
		newsList.add(news);
		deleteNews(newsList);
		String sql = getInsertNewsSql(news);
		return DBTemplate.getInstance().updateSql(sql);
	}

	/**
	 * ɾ������
	 * @param files
	 * @return
	 */
	public int deleteNews(List<NewsBean> news) {
		String sql = "DELETE FROM " + TableConst.TABLE_NEWS + " WHERE ����ID = ?";
		List<List<String>> dataList = new ArrayList<List<String>>();
		for (int i = 0; i < news.size(); i++) {
			List<String> item = new ArrayList<String>();
			item.add(news.get(i).getNewsId());
			dataList.add(item);
		}
		return DBTemplate.getInstance().batchPreparedUpdate(sql, dataList).length;
	}

	/**
	 * ��ѯ����
	 * @param newsId
	 * @return
	 */
	public NewsBean getNewsInfo(String newsId) {
		String sql = "SELECT * FROM " + TableConst.TABLE_NEWS
				+ " WHERE ����ID = '" + newsId + "'";
		return DBTemplate.getInstance()
				.getResultRowMapper(sql, new NewsRowMapper());
	}
	
	/**
	 * ���������¼
	 * @param newsId	����ID
	 * @param userId	�û�ID
	 * @param ip		�û�IP
	 * @return
	 */
	public boolean recordScanNews(String newsId, String userId, String ip){
		ip = null == ip ? "" : ip;
		if (!"10.40.36.120".equals(ip) && !"10.40.36.121".equals(ip) && !"10.40.36.123".equals(ip) && !"10.40.36.124".equals(ip) && !"0:0:0:0:0:0:0:1".equals(ip) && !"127.0.0.1".equals(ip) ){
			if (null != newsId && !"".equals(newsId) && null != userId && !"".equals(userId)){
				Calendar calendar = new GregorianCalendar().getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String scanTime = sdf.format(calendar.getTime());
				String sql = "INSERT INTO IDP_KH.���������¼��(NEWSID,USERID,IP,SCANTIME) VALUES('" + newsId + "','"
				           + userId + "','" + ip + "','" + scanTime + "')";
				DBTemplate.getInstance().updateSql(sql);
			}
		}
		return true;
	}
	
	/**
	 * ���ŵ��������1
	 */
	public void updateNewsClickCount(NewsBean news){
		String sql = "UPDATE " + TableConst.TABLE_NEWS
				+ " SET �������=" + news.getClickCount()
				+ " WHERE ����ID = '" + news.getNewsId() + "'";
		DBTemplate.getInstance().executeSql(sql);
		//DBTemplate.getInstance().getResultRowMapper(sql, new NewsRowMapper());
	}

	/**
	 * �޸����ŷ���״̬
	 * @param user
	 * @param news
	 * @param publishFlag
	 * @return �����޸ĺ�����Ŷ���
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { RuntimeException.class })
	public List<NewsBean> publishNews(UserBean user, NewsBean news,
			boolean publishFlag) {
		String sql = "";
		if (publishFlag) {
			sql = "UPDATE " + TableConst.TABLE_NEWS + " SET �����ID='"
					+ user.getUserId() + "',�����='" + user.getUserDisplayName()
					+ "'," + "���ʱ��=sysdate,״̬=1,ͼƬ='" + news.getNewsImage()
					+ "',ʧЧʱ��=to_date('" + news.getDisableTime()
					+ "','YYYY-MM-DD HH:MI:SS'),���Ȩ��='" + news.getBrowsePriv()
					+ "',����ʱ��=to_date('" + news.getCreateTime() + "','YYYY-MM-DD HH:MI:SS')"
					+ ",�Ƿ��ɫ������ʾ='" + news.getIsRedFontDisplay() 
					+ "',�Ƿ���ʾNEW��ʶ��='" + news.getIsNewNoticeShow() + "'"
					//+ "',׫����='" + news.getAuthor() + "'"
					//+ "',ý�����='" + news.getFb() + "'"
					+ " WHERE ����ID = '" + news.getNewsId() + "'";
		} else {
			if(news.getRelPriv().equals(news.getStorePath())){
				sql = "UPDATE "
						+ TableConst.TABLE_NEWS
						//aviyy 2014-3-8 ������ק����		ȥ����������ͬʱ˳���Ϊ0
						+ " SET �����ID='',�����='',���ʱ��='',״̬=0,ʧЧʱ��='',���Ȩ��='',�Ƿ��ɫ������ʾ='0',�Ƿ���ʾNEW��ʶ��='0' WHERE ����ID = '"
						//+ " SET �����ID='',�����='',���ʱ��='',״̬=0,ʧЧʱ��='',���Ȩ��='',˳��='',�Ƿ��ɫ������ʾ='0',�Ƿ���ʾNEW��ʶ��='0' WHERE ����ID = '"
						+ news.getNewsId() + "'" ;
						DBTemplate.getInstance().updateSql("DELETE FROM " + TableConst.TABLE_NEWS + " WHERE ����ID = '" + news.getNewsId() + "' AND ����Ȩ�� != �洢·��");
						
			}else{
				sql = "DELETE FROM " + TableConst.TABLE_NEWS
						+ "  WHERE ����ID = '"+ news.getNewsId() + "' AND ����Ȩ�� ='" + news.getRelPriv()+"'";
			}
		}
		if (DBTemplate.getInstance().updateSql(sql) > 0) {
			return DBTemplate.getInstance().getResultRowMapperList("SELECT * FROM " + TableConst.TABLE_NEWS
					+ " WHERE ����ID = '" + news.getNewsId() + "'",
					new NewsRowMapper());
		} else {
			return new ArrayList<NewsBean>();
		}
	}

	/**
	 * ��������
	 * @param news
	 * @return
	 */
	public int updateNews(NewsBean news) {
		try {
			return DBTemplate.getInstance().updateSql(getUpdateNewsSql(news));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	
	/**
	 * ����ĵ�����BEAN
	 * @param sourceId
	 * @return
	 */
	public SourceBean getSourceInfo(String sourceId){
		String sql = "SELECT * FROM "+TableConst.TABLE_SOURCE + " WHERE ����ID = '"+sourceId+"'";
		return DBTemplate.getInstance().getResultRowMapper(sql, new SourceRowMapper());
	}
	/**
	 * ����ĵ�����BEAN
	 * @param sourceId
	 * @return
	 */
	public SourceBean getSourceInfo(String sourceId,String relPrivId){
		String sql = "SELECT * FROM "+TableConst.TABLE_SOURCE + " WHERE ����ID = '"+sourceId+"' and ����Ȩ��='"+relPrivId+"'";
		return DBTemplate.getInstance().getResultRowMapper(sql, new SourceRowMapper());
	}
	/**
	 * ��� �ĵ�����
	 * @param privId
	 * @param pageSize
	 * @param pageNum
	 * @param filtersql
	 * @param isDirect
	 *            true ֱ���ӽڵ� false �ݹ����нڵ�
	 * @return
	 */
	public PagingGridBean<SourceBean> getSourceFromPriv(String privId,
			int pageSize, int pageNum, String filtersql, boolean isDirect) {
		PagingGridBean<SourceBean> sourcelist = new PagingGridBean<SourceBean>();
		try {
			String subSql = TableConst.TABLE_SOURCE;
			if (filtersql != null && !"".equals(filtersql)) {
				subSql = "( SELECT * FROM " + TableConst.TABLE_SOURCE
						+ " WHERE " + filtersql + " )";
			}
			String sql = "";
			if (isDirect) {
				sql = "select  *  from " + subSql + " where ����Ȩ�� = '" + privId
						+ "' ";
			} else {
				sql = "select  *  from " + subSql
						+ " a  where exists ( select Ȩ��ID from "
						+ TableConst.TABLE_MANAGE_PRIV
						+ " b where a.����Ȩ�� = b.Ȩ��ID " + "	start with Ȩ��ID = '"
						+ privId + "' connect by Ȩ�޸�ID = prior Ȩ��ID )";
			}
			sourcelist = DBTools.getPagingData(sql, pageSize, pageNum,
					new SourceRowMapper(), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sourcelist;
	}

	/**
	 * ����ĵ�����	2014-04-03 ����ʱ �Զ����� �����
	 * @param fileList
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { RuntimeException.class })
	public int addSource(UserBean user, ArrayList<Map<String, Object>> fileList) {
		int order = getSourceOrder();
		String sql = "INSERT INTO "
				+ TableConst.TABLE_SOURCE
				+ " (����ID,�ĵ�����,�ĵ�����,�ļ�����,�ĵ�˵��,�ļ���С,�洢Ŀ¼,����Ȩ��,������ID,������,����ʱ��,�����ID,�����,���ʱ��,״̬,˳��) "
				+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		List<List<Object>> dataList = new ArrayList<List<Object>>();
		for (int i = 0; i < fileList.size(); i++) {
			List<Object> file = new ArrayList<Object>();
			file.add(CommonTools.createId("SOURCE"));
			String name = String.valueOf(fileList.get(i).get("name"));
			file.add(name.subSequence(0, name.lastIndexOf(".")));
			file.add(fileList.get(i).get("type"));
			file.add(name);
			file.add(name);
			file.add(fileList.get(i).get("size"));
			file.add(fileList.get(i).get("path"));
			file.add(fileList.get(i).get("priv"));
			file.add(user.getUserId());
			file.add(user.getUserDisplayName());
			file.add(new Timestamp(new Date().getTime()));
			file.add(null);
			file.add(null);
			file.add(null);
			file.add(0);
			file.add(order + i);
			dataList.add(file);
		}
		return DBTemplate.getInstance().batchPreparedUpdate(sql, dataList).length;
	}
	
	
	/**
	 * ����ĵ�����
	 * @param fileList
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { RuntimeException.class })
	public int addSource(UserBean user, SourceBean source) {
		int order = getSourceOrder();
		String sql = "INSERT INTO "
				+ TableConst.TABLE_SOURCE
				+ " (����ID,�ĵ�����,�ĵ�����,�ļ�����,�ĵ�˵��,�ļ���С,�洢Ŀ¼,����Ȩ��,������ID,������,����ʱ��,�����ID,�����,���ʱ��,״̬,˳��) "
				+ " VALUES('" + CommonTools.createId("SOURCE") + "','"
				+ source.getSourceName() + "','" + source.getSourceType()
				+ "','" + source.getFileName() + "','"
				+ source.getSourceDetail() + "'," + source.getFileSize() + ",'"
				+ source.getStorePath() + "','" + source.getRelPriv() + "','"
				+ user.getUserId() + "','" + user.getUserDisplayName()
				+ "',sysdate,null,null,null,null,'"+ order +"') ";
		System.out.println(sql);
		return DBTemplate.getInstance().updateSql(sql);
	}
	/**
	 * �����ĵ�����
	 * @param fileList
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { RuntimeException.class })
	public int addSource1(UserBean user, SourceBean source) {
		String sql = "INSERT INTO "
				+ TableConst.TABLE_SOURCE
				+ " (����ID,�ĵ�����,�ĵ�����,�ļ�����,�ĵ�˵��,�ļ���С,�洢Ŀ¼,����Ȩ��,������ID,������,����ʱ��,�����ID,�����,���ʱ��,״̬) "
				+ " VALUES('" + source.getSourceId() + "','"
				+ source.getSourceName() + "','" + source.getSourceType()
				+ "','" + source.getFileName() + "','"
				+ source.getSourceDetail() + "'," + source.getFileSize() + ",'"
				+ source.getStorePath() + "','" + source.getRelPriv() + "','"
				+ user.getUserId() + "','" + user.getUserDisplayName()
				+ "',sysdate,null,null,null,null) ";
		System.out.println(sql);
		return DBTemplate.getInstance().updateSql(sql);
	}

	/**
	 * ɾ���ļ�
	 * @param files
	 * @return
	 */
	public int deleteSource(List<SourceBean> files) {
		String sql = "DELETE FROM " + TableConst.TABLE_SOURCE
				+ " WHERE ����ID = ?";
		List<List<String>> dataList = new ArrayList<List<String>>();
		for (int i = 0; i < files.size(); i++) {
			List<String> item = new ArrayList<String>();
			item.add(files.get(i).getSourceId());
			dataList.add(item);
		}
		return DBTemplate.getInstance().batchPreparedUpdate(sql, dataList).length;
	}

	/**
	 * �޸��ĵ����Ϸ���״̬
	 * @param user
	 * @param source
	 * @param publishFlag
	 * @return �����޸ĺ���ĵ����϶���
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { RuntimeException.class })
	public List<SourceBean> publishSourceFile(UserBean user, SourceBean source,
			boolean publishFlag) {
		String sql = "";
		if (publishFlag) {
			sql = "UPDATE " + TableConst.TABLE_SOURCE + " SET �����ID='"
					+ user.getUserId()
					+ "',�����='"+user.getUserDisplayName()+"'," 
					+ "���ʱ��=sysdate,״̬=1 WHERE ����ID = '"
					+ source.getSourceId() + "'";
		} else {
			if(source.getRelPriv().equals(source.getStorePath())){
				//����Դ
				sql = "UPDATE " + TableConst.TABLE_SOURCE
					+ " SET �����ID='',�����='',���ʱ��='',״̬=0 WHERE ����ID = '"
					+ source.getSourceId() + "'" ;
					DBTemplate.getInstance().updateSql("DELETE FROM " + TableConst.TABLE_SOURCE +" WHERE ����ID = '"+ source.getSourceId() + "' AND ����Ȩ�� != �洢Ŀ¼");
					
			}else{
				//������Դ��������
				sql = "DELETE FROM " + TableConst.TABLE_SOURCE
					+ "  WHERE ����ID = '"+ source.getSourceId() + "' AND ����Ȩ�� ='"+source.getRelPriv()+"'";
				
			}
		}
		DBTemplate.getInstance().updateSql(sql);
		List<SourceBean> list =DBTemplate.getInstance().getResultRowMapperList("SELECT * FROM " + TableConst.TABLE_SOURCE
				+ " WHERE ����ID = '" + source.getSourceId() + "' AND ����Ȩ�� ='"+source.getRelPriv()+"'",
				new SourceRowMapper());
			return list;
	}

	/**
	 * �����ĵ���������ͳ����Ϣ
	 * @param news
	 * @return
	 */
	public int addDownloadStat(DownloadStatisticsBean downloadStat) {
		try {
			String sql = getInsertDownloadStatSql(downloadStat);
			System.out.println(sql);
			return DBTemplate.getInstance().updateSql(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * ��ѯ��վ���ӷ�ҳ����
	 * @param privId
	 * @param pageSize
	 * @param pageNum
	 * @param filtersql
	 * @param isDirect
	 * @return
	 */
	public PagingGridBean<LinkBean> getLinkFromPriv(String privId,
			int pageSize, int pageNum, String filtersql, boolean isDirect) {
		PagingGridBean<LinkBean> linklist = new PagingGridBean<LinkBean>();
		try {
			String subSql = TableConst.TABLE_LINK;
			if (filtersql != null && !"".equals(filtersql)) {
				subSql = "( SELECT * FROM " + TableConst.TABLE_LINK + " WHERE "
						+ filtersql + " )";
			}
			String sql = "";
			if (isDirect) {
				sql = "select  *  from " + subSql + " where ����Ȩ�� = '" + privId
						+ "' ";
			} else {
				sql = "select  *  from " + subSql
						+ " a  where exists ( select Ȩ��ID from "
						+ TableConst.TABLE_MANAGE_PRIV
						+ " b where a.����Ȩ�� = b.Ȩ��ID " + "	start with Ȩ��ID = '"
						+ privId + "' connect by Ȩ�޸�ID = prior Ȩ��ID )";
			}
			linklist = DBTools.getPagingData(sql, pageSize, pageNum,
					new LinkRowMapper(), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return linklist;
	}
	
	/**
	 * ��������
	 * @param link
	 * @return
	 */
	public int addNewLink(LinkBean link){
		String sql = "INSERT INTO " + TableConst.TABLE_LINK
				+ " (����ID,��������,������,���ӵ�ַ,�򿪷�ʽ,˳��,ͼ��,״̬,����Ȩ��) VALUES('"
				+ CommonTools.createId("LINK") + "','" + link.getLinkType()
				+ "','" + link.getLinkName() + "','" + link.getLinkUrl()
				+ "','" + link.getOpenTarget() + "'," + link.getOrder() + ",'"
				+ link.getLinkIcon() + "'," + link.getStatus() + ",'"
				+ link.getRelPriv() + "')";
		return DBTemplate.getInstance().updateSql(sql);
	}
	
	/**
	 * �޸�����
	 * @param link
	 * @return
	 */
	public int modifyLink(LinkBean link){
		String sql = "UPDATE " + TableConst.TABLE_LINK + " SET ��������='"
				+ link.getLinkType() + "',������='" + link.getLinkName()
				+ "',���ӵ�ַ='" + link.getLinkUrl() + "',�򿪷�ʽ='"
				+ link.getOpenTarget() + "',˳��=" + link.getOrder() + ",ͼ��='"
				+ link.getLinkIcon() + "',״̬=" + link.getStatus() + ",����Ȩ��='"
				+ link.getRelPriv() + "' WHERE ����ID = '" + link.getLinkId()
				+ "'";
		return DBTemplate.getInstance().updateSql(sql);
	}
	
	/**
	 * ɾ������
	 * @param files
	 * @return
	 */
	public int deleteLinks(List<LinkBean> links) {
		String sql = "DELETE FROM " + TableConst.TABLE_LINK
				+ " WHERE ����ID = ?";
		List<List<String>> dataList = new ArrayList<List<String>>();
		for (int i = 0; i < links.size(); i++) {
			List<String> item = new ArrayList<String>();
			item.add(links.get(i).getLinkId());
			dataList.add(item);
		}
		return DBTemplate.getInstance().batchPreparedUpdate(sql, dataList).length;
	}
	
	/**
	 * �޸����ӷ���״̬
	 * @param user
	 * @param link
	 * @param publishFlag
	 * @return �����޸ĺ�����Ӷ���
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { RuntimeException.class })
	public int publishLink(UserBean user, LinkBean link,
			boolean publishFlag) {
		String sql = "";
		if (publishFlag) {
			sql = "UPDATE " + TableConst.TABLE_LINK + " SET ״̬=1 WHERE ����ID = '"+link.getLinkId()+"'";
		} else {
			sql = "UPDATE " + TableConst.TABLE_LINK + " SET ״̬=0 WHERE ����ID = '"+link.getLinkId()+"'";
		}
		return DBTemplate.getInstance().updateSql(sql);
	}
	
	
	
	/**
	 * �����������SQL
	 * @param news
	 * @return
	 */
	private String getInsertNewsSql(NewsBean news) {
		if (news.getNewsId() == null || "".equals(news.getNewsId())) {
			news.setNewsId(CommonTools.createId("NEWS"));
		}
		return "INSERT INTO " + TableConst.TABLE_NEWS
				+ " (����ID,����,����,����,ͼƬ,����,���ӵ�ַ,״̬,˳��,������ID,������,����ʱ��,�洢·��,"
				+ " ����Ȩ��,�����ID,�����,���ʱ��,ʧЧʱ��,���Ȩ��,�Ƿ��ɫ������ʾ,�Ƿ���ʾNEW��ʶ��,�������,ý�����,׫����) VALUES('" + news.getNewsId()
				+ "','" + news.getNewsTitle() + "','" + news.getNewsType()
				+ "','" + news.getNewsContent() + "','" + news.getNewsImage()
				+ "','" + news.getNewsAttach() + "','" + news.getNewsUrl()
				//+ "'," + news.getStatus() + "," + news.getOrder() + ",'"
				+ "'," + news.getStatus() + "," + getNewsOrder() + ",'"
				+ news.getCreaterId() + "','" + news.getCreater()
				+ "',sysdate,'" + news.getStorePath() + "','"
				+ news.getRelPriv() + "','','','','','" + news.getBrowsePriv()
				+ "','0','0',0,'"+news.getFb()+"','"+news.getAuthor()+"')";
	}

	/**
	 * ��ø�������SQL
	 * @param news
	 * @return
	 */
	private String getUpdateNewsSql(NewsBean news) {
		if (news.getNewsId() != null && !"".equals(news.getNewsId())) {
			return "UPDATE " + TableConst.TABLE_NEWS + " SET ����='"
					+ news.getNewsTitle() + "',����='" + news.getNewsType()
					+ "',����='" + news.getNewsContent() + "',ͼƬ='"
					+ news.getNewsImage() + "',����='" + news.getNewsAttach()
					+ "',���ӵ�ַ='" + news.getNewsUrl() + "',˳��="
					+ news.getOrder() + ",׫����='"+news.getAuthor()+"',ý�����='"+news.getFb()+"' WHERE ����ID = '" + news.getNewsId()
					+ "'";
		}
		return "";
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { RuntimeException.class })
	public int updateSyncSQL(String[] sqls){
		return DBTemplate.getInstance().batchUpdateSql(sqls).length;
	}

	/**
	 * ��������ĵ���������ͳ����ϢSQL
	 * @param news
	 * @return
	 */
	private String getInsertDownloadStatSql(DownloadStatisticsBean downloadStat) {
		if (downloadStat.getSourceId() != null && !"".equals(downloadStat.getSourceId())) {
			return "INSERT INTO " + TableConst.TABLE_SOURCE_STATISTICS
					+ " (����ID,�洢Ŀ¼,����Ȩ��,������,����ʱ��) VALUES('" + downloadStat.getSourceId()
					+ "','" + downloadStat.getStorePath() + "','" + downloadStat.getRelPriv()
					+ "','" + downloadStat.getDownloadUser() + "','" + downloadStat.getDownloadTime()
					+ "')";
		}
		return "";
	}
	/**
	 * aviyy	2014-3-7	������ק����	������˳��+1
	 * @return
	 */
	public int getNewsOrder(){
		String sql = "select max(˳��) from "+TableConst.TABLE_NEWS;
		return DBTemplate.getInstance().queryForInt(sql)+1;
	}
	/**
	 * pengain	2014-04-03	��Դ��ק����	������˳��+1
	 * @return
	 */
	private int getSourceOrder(){
		String sql = "select max(˳��) from "+TableConst.TABLE_SOURCE;
		return DBTemplate.getInstance().queryForInt(sql)+1;
	}
	
	
	public void updateSource(SourceBean sourceBean) {
		if (sourceBean.getSourceId() != null && sourceBean.getRelPriv() != null) {
			String sql = "UPDATE " + TableConst.TABLE_SOURCE + " SET ˳��='"
			+ sourceBean.getOrder() + "'  WHERE ����ID = '" + sourceBean.getSourceId()
			+ "' AND ����Ȩ�� = '" +sourceBean.getRelPriv() + "'";
			DBTemplate.getInstance().updateSql(sql);
		}
	}
}
