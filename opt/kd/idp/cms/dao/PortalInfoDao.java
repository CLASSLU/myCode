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
	 * 查询新闻分页数据
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
				sql = "select  *  from " + subSql + " where 关联权限 = '" + privId
						//+ "' order by 创建时间 desc ";
						//aviyy 2014-3-7 新闻拖拽排序	控制台按'顺序'取新闻
						+ "' order by 顺序  desc ";
			} else {
				sql = "select  *  from " + subSql
						+ " a  where exists ( select 权限ID from "
						+ TableConst.TABLE_MANAGE_PRIV
						+ " b where a.关联权限 = b.权限ID " + "	start with 权限ID = '"
						+ privId + "' connect by 权限父ID = prior 权限ID ) "
						//+" order by 创建时间 desc ";
						//aviyy 2014-3-7 新闻拖拽排序	控制台按'顺序'取新闻
						+" order by 顺序 desc ";
			}
			newslist = DBTools.getPagingData(sql, pageSize, pageNum,
					new NewsRowMapper(), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newslist;
	}

	/**
	 * 新增 新闻
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
	 * 同步新闻
	 * @param fileList
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { RuntimeException.class })
	public int addNews1(UserBean user, NewsBean news) {
		int maxShowOrder = 0;
		String maxOrderSql = "SELECT MAX(顺序) AS SHOWORDER FROM " + TableConst.TABLE_NEWS
						   + " WHERE 关联权限='" + news.getRelPriv() + "'";
		maxShowOrder = DBTemplate.getInstance().queryForInt(maxOrderSql) + 1;
		news.setOrder(maxShowOrder);
		
		String sql = "INSERT INTO "
				+ TableConst.TABLE_NEWS
				+ " (内容ID,标题,类型,内容,图片,附件,链接地址,状态,顺序,创建人ID,创建人,存储路径,关联权限,审核人ID,审核人,审核时间,失效时间,浏览权限,是否红色字体显示,是否显示NEW标识符,点击次数) "
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
	 * 新增 新闻 先删除
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
	 * 删除新闻
	 * @param files
	 * @return
	 */
	public int deleteNews(List<NewsBean> news) {
		String sql = "DELETE FROM " + TableConst.TABLE_NEWS + " WHERE 内容ID = ?";
		List<List<String>> dataList = new ArrayList<List<String>>();
		for (int i = 0; i < news.size(); i++) {
			List<String> item = new ArrayList<String>();
			item.add(news.get(i).getNewsId());
			dataList.add(item);
		}
		return DBTemplate.getInstance().batchPreparedUpdate(sql, dataList).length;
	}

	/**
	 * 查询新闻
	 * @param newsId
	 * @return
	 */
	public NewsBean getNewsInfo(String newsId) {
		String sql = "SELECT * FROM " + TableConst.TABLE_NEWS
				+ " WHERE 内容ID = '" + newsId + "'";
		return DBTemplate.getInstance()
				.getResultRowMapper(sql, new NewsRowMapper());
	}
	
	/**
	 * 新闻浏览记录
	 * @param newsId	新闻ID
	 * @param userId	用户ID
	 * @param ip		用户IP
	 * @return
	 */
	public boolean recordScanNews(String newsId, String userId, String ip){
		ip = null == ip ? "" : ip;
		if (!"10.40.36.120".equals(ip) && !"10.40.36.121".equals(ip) && !"10.40.36.123".equals(ip) && !"10.40.36.124".equals(ip) && !"0:0:0:0:0:0:0:1".equals(ip) && !"127.0.0.1".equals(ip) ){
			if (null != newsId && !"".equals(newsId) && null != userId && !"".equals(userId)){
				Calendar calendar = new GregorianCalendar().getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String scanTime = sdf.format(calendar.getTime());
				String sql = "INSERT INTO IDP_KH.新闻浏览记录表(NEWSID,USERID,IP,SCANTIME) VALUES('" + newsId + "','"
				           + userId + "','" + ip + "','" + scanTime + "')";
				DBTemplate.getInstance().updateSql(sql);
			}
		}
		return true;
	}
	
	/**
	 * 新闻点击次数加1
	 */
	public void updateNewsClickCount(NewsBean news){
		String sql = "UPDATE " + TableConst.TABLE_NEWS
				+ " SET 点击次数=" + news.getClickCount()
				+ " WHERE 内容ID = '" + news.getNewsId() + "'";
		DBTemplate.getInstance().executeSql(sql);
		//DBTemplate.getInstance().getResultRowMapper(sql, new NewsRowMapper());
	}

	/**
	 * 修改新闻发布状态
	 * @param user
	 * @param news
	 * @param publishFlag
	 * @return 返回修改后的新闻对象
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { RuntimeException.class })
	public List<NewsBean> publishNews(UserBean user, NewsBean news,
			boolean publishFlag) {
		String sql = "";
		if (publishFlag) {
			sql = "UPDATE " + TableConst.TABLE_NEWS + " SET 审核人ID='"
					+ user.getUserId() + "',审核人='" + user.getUserDisplayName()
					+ "'," + "审核时间=sysdate,状态=1,图片='" + news.getNewsImage()
					+ "',失效时间=to_date('" + news.getDisableTime()
					+ "','YYYY-MM-DD HH:MI:SS'),浏览权限='" + news.getBrowsePriv()
					+ "',创建时间=to_date('" + news.getCreateTime() + "','YYYY-MM-DD HH:MI:SS')"
					+ ",是否红色字体显示='" + news.getIsRedFontDisplay() 
					+ "',是否显示NEW标识符='" + news.getIsNewNoticeShow() + "'"
					//+ "',撰稿人='" + news.getAuthor() + "'"
					//+ "',媒体类别='" + news.getFb() + "'"
					+ " WHERE 内容ID = '" + news.getNewsId() + "'";
		} else {
			if(news.getRelPriv().equals(news.getStorePath())){
				sql = "UPDATE "
						+ TableConst.TABLE_NEWS
						//aviyy 2014-3-8 新闻拖拽排序		去除撤销发布同时顺序变为0
						+ " SET 审核人ID='',审核人='',审核时间='',状态=0,失效时间='',浏览权限='',是否红色字体显示='0',是否显示NEW标识符='0' WHERE 内容ID = '"
						//+ " SET 审核人ID='',审核人='',审核时间='',状态=0,失效时间='',浏览权限='',顺序='',是否红色字体显示='0',是否显示NEW标识符='0' WHERE 内容ID = '"
						+ news.getNewsId() + "'" ;
						DBTemplate.getInstance().updateSql("DELETE FROM " + TableConst.TABLE_NEWS + " WHERE 内容ID = '" + news.getNewsId() + "' AND 关联权限 != 存储路径");
						
			}else{
				sql = "DELETE FROM " + TableConst.TABLE_NEWS
						+ "  WHERE 内容ID = '"+ news.getNewsId() + "' AND 关联权限 ='" + news.getRelPriv()+"'";
			}
		}
		if (DBTemplate.getInstance().updateSql(sql) > 0) {
			return DBTemplate.getInstance().getResultRowMapperList("SELECT * FROM " + TableConst.TABLE_NEWS
					+ " WHERE 内容ID = '" + news.getNewsId() + "'",
					new NewsRowMapper());
		} else {
			return new ArrayList<NewsBean>();
		}
	}

	/**
	 * 更新新闻
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
	 * 获得文档资料BEAN
	 * @param sourceId
	 * @return
	 */
	public SourceBean getSourceInfo(String sourceId){
		String sql = "SELECT * FROM "+TableConst.TABLE_SOURCE + " WHERE 内容ID = '"+sourceId+"'";
		return DBTemplate.getInstance().getResultRowMapper(sql, new SourceRowMapper());
	}
	/**
	 * 获得文档资料BEAN
	 * @param sourceId
	 * @return
	 */
	public SourceBean getSourceInfo(String sourceId,String relPrivId){
		String sql = "SELECT * FROM "+TableConst.TABLE_SOURCE + " WHERE 内容ID = '"+sourceId+"' and 关联权限='"+relPrivId+"'";
		return DBTemplate.getInstance().getResultRowMapper(sql, new SourceRowMapper());
	}
	/**
	 * 获得 文档资料
	 * @param privId
	 * @param pageSize
	 * @param pageNum
	 * @param filtersql
	 * @param isDirect
	 *            true 直接子节点 false 递归所有节点
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
				sql = "select  *  from " + subSql + " where 关联权限 = '" + privId
						+ "' ";
			} else {
				sql = "select  *  from " + subSql
						+ " a  where exists ( select 权限ID from "
						+ TableConst.TABLE_MANAGE_PRIV
						+ " b where a.关联权限 = b.权限ID " + "	start with 权限ID = '"
						+ privId + "' connect by 权限父ID = prior 权限ID )";
			}
			sourcelist = DBTools.getPagingData(sql, pageSize, pageNum,
					new SourceRowMapper(), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sourcelist;
	}

	/**
	 * 添加文档资料	2014-04-03 新增时 自动排序 变最大
	 * @param fileList
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { RuntimeException.class })
	public int addSource(UserBean user, ArrayList<Map<String, Object>> fileList) {
		int order = getSourceOrder();
		String sql = "INSERT INTO "
				+ TableConst.TABLE_SOURCE
				+ " (内容ID,文档名称,文档类型,文件名称,文档说明,文件大小,存储目录,关联权限,创建人ID,创建人,创建时间,审核人ID,审核人,审核时间,状态,顺序) "
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
	 * 添加文档资料
	 * @param fileList
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { RuntimeException.class })
	public int addSource(UserBean user, SourceBean source) {
		int order = getSourceOrder();
		String sql = "INSERT INTO "
				+ TableConst.TABLE_SOURCE
				+ " (内容ID,文档名称,文档类型,文件名称,文档说明,文件大小,存储目录,关联权限,创建人ID,创建人,创建时间,审核人ID,审核人,审核时间,状态,顺序) "
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
	 * 发布文档资料
	 * @param fileList
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { RuntimeException.class })
	public int addSource1(UserBean user, SourceBean source) {
		String sql = "INSERT INTO "
				+ TableConst.TABLE_SOURCE
				+ " (内容ID,文档名称,文档类型,文件名称,文档说明,文件大小,存储目录,关联权限,创建人ID,创建人,创建时间,审核人ID,审核人,审核时间,状态) "
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
	 * 删除文件
	 * @param files
	 * @return
	 */
	public int deleteSource(List<SourceBean> files) {
		String sql = "DELETE FROM " + TableConst.TABLE_SOURCE
				+ " WHERE 内容ID = ?";
		List<List<String>> dataList = new ArrayList<List<String>>();
		for (int i = 0; i < files.size(); i++) {
			List<String> item = new ArrayList<String>();
			item.add(files.get(i).getSourceId());
			dataList.add(item);
		}
		return DBTemplate.getInstance().batchPreparedUpdate(sql, dataList).length;
	}

	/**
	 * 修改文档资料发布状态
	 * @param user
	 * @param source
	 * @param publishFlag
	 * @return 返回修改后的文档资料对象
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { RuntimeException.class })
	public List<SourceBean> publishSourceFile(UserBean user, SourceBean source,
			boolean publishFlag) {
		String sql = "";
		if (publishFlag) {
			sql = "UPDATE " + TableConst.TABLE_SOURCE + " SET 审核人ID='"
					+ user.getUserId()
					+ "',审核人='"+user.getUserDisplayName()+"'," 
					+ "审核时间=sysdate,状态=1 WHERE 内容ID = '"
					+ source.getSourceId() + "'";
		} else {
			if(source.getRelPriv().equals(source.getStorePath())){
				//发布源
				sql = "UPDATE " + TableConst.TABLE_SOURCE
					+ " SET 审核人ID='',审核人='',审核时间='',状态=0 WHERE 内容ID = '"
					+ source.getSourceId() + "'" ;
					DBTemplate.getInstance().updateSql("DELETE FROM " + TableConst.TABLE_SOURCE +" WHERE 内容ID = '"+ source.getSourceId() + "' AND 关联权限 != 存储目录");
					
			}else{
				//发布来源其他部门
				sql = "DELETE FROM " + TableConst.TABLE_SOURCE
					+ "  WHERE 内容ID = '"+ source.getSourceId() + "' AND 关联权限 ='"+source.getRelPriv()+"'";
				
			}
		}
		DBTemplate.getInstance().updateSql(sql);
		List<SourceBean> list =DBTemplate.getInstance().getResultRowMapperList("SELECT * FROM " + TableConst.TABLE_SOURCE
				+ " WHERE 内容ID = '" + source.getSourceId() + "' AND 关联权限 ='"+source.getRelPriv()+"'",
				new SourceRowMapper());
			return list;
	}

	/**
	 * 新增文档资料下载统计信息
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
	 * 查询网站链接分页数据
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
				sql = "select  *  from " + subSql + " where 关联权限 = '" + privId
						+ "' ";
			} else {
				sql = "select  *  from " + subSql
						+ " a  where exists ( select 权限ID from "
						+ TableConst.TABLE_MANAGE_PRIV
						+ " b where a.关联权限 = b.权限ID " + "	start with 权限ID = '"
						+ privId + "' connect by 权限父ID = prior 权限ID )";
			}
			linklist = DBTools.getPagingData(sql, pageSize, pageNum,
					new LinkRowMapper(), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return linklist;
	}
	
	/**
	 * 新增链接
	 * @param link
	 * @return
	 */
	public int addNewLink(LinkBean link){
		String sql = "INSERT INTO " + TableConst.TABLE_LINK
				+ " (内容ID,链接类型,链接名,链接地址,打开方式,顺序,图标,状态,关联权限) VALUES('"
				+ CommonTools.createId("LINK") + "','" + link.getLinkType()
				+ "','" + link.getLinkName() + "','" + link.getLinkUrl()
				+ "','" + link.getOpenTarget() + "'," + link.getOrder() + ",'"
				+ link.getLinkIcon() + "'," + link.getStatus() + ",'"
				+ link.getRelPriv() + "')";
		return DBTemplate.getInstance().updateSql(sql);
	}
	
	/**
	 * 修改链接
	 * @param link
	 * @return
	 */
	public int modifyLink(LinkBean link){
		String sql = "UPDATE " + TableConst.TABLE_LINK + " SET 链接类型='"
				+ link.getLinkType() + "',链接名='" + link.getLinkName()
				+ "',链接地址='" + link.getLinkUrl() + "',打开方式='"
				+ link.getOpenTarget() + "',顺序=" + link.getOrder() + ",图标='"
				+ link.getLinkIcon() + "',状态=" + link.getStatus() + ",关联权限='"
				+ link.getRelPriv() + "' WHERE 内容ID = '" + link.getLinkId()
				+ "'";
		return DBTemplate.getInstance().updateSql(sql);
	}
	
	/**
	 * 删除链接
	 * @param files
	 * @return
	 */
	public int deleteLinks(List<LinkBean> links) {
		String sql = "DELETE FROM " + TableConst.TABLE_LINK
				+ " WHERE 内容ID = ?";
		List<List<String>> dataList = new ArrayList<List<String>>();
		for (int i = 0; i < links.size(); i++) {
			List<String> item = new ArrayList<String>();
			item.add(links.get(i).getLinkId());
			dataList.add(item);
		}
		return DBTemplate.getInstance().batchPreparedUpdate(sql, dataList).length;
	}
	
	/**
	 * 修改链接发布状态
	 * @param user
	 * @param link
	 * @param publishFlag
	 * @return 返回修改后的链接对象
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { RuntimeException.class })
	public int publishLink(UserBean user, LinkBean link,
			boolean publishFlag) {
		String sql = "";
		if (publishFlag) {
			sql = "UPDATE " + TableConst.TABLE_LINK + " SET 状态=1 WHERE 内容ID = '"+link.getLinkId()+"'";
		} else {
			sql = "UPDATE " + TableConst.TABLE_LINK + " SET 状态=0 WHERE 内容ID = '"+link.getLinkId()+"'";
		}
		return DBTemplate.getInstance().updateSql(sql);
	}
	
	
	
	/**
	 * 获得新增新闻SQL
	 * @param news
	 * @return
	 */
	private String getInsertNewsSql(NewsBean news) {
		if (news.getNewsId() == null || "".equals(news.getNewsId())) {
			news.setNewsId(CommonTools.createId("NEWS"));
		}
		return "INSERT INTO " + TableConst.TABLE_NEWS
				+ " (内容ID,标题,类型,内容,图片,附件,链接地址,状态,顺序,创建人ID,创建人,创建时间,存储路径,"
				+ " 关联权限,审核人ID,审核人,审核时间,失效时间,浏览权限,是否红色字体显示,是否显示NEW标识符,点击次数,媒体类别,撰稿人) VALUES('" + news.getNewsId()
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
	 * 获得更新新闻SQL
	 * @param news
	 * @return
	 */
	private String getUpdateNewsSql(NewsBean news) {
		if (news.getNewsId() != null && !"".equals(news.getNewsId())) {
			return "UPDATE " + TableConst.TABLE_NEWS + " SET 标题='"
					+ news.getNewsTitle() + "',类型='" + news.getNewsType()
					+ "',内容='" + news.getNewsContent() + "',图片='"
					+ news.getNewsImage() + "',附件='" + news.getNewsAttach()
					+ "',链接地址='" + news.getNewsUrl() + "',顺序="
					+ news.getOrder() + ",撰稿人='"+news.getAuthor()+"',媒体类别='"+news.getFb()+"' WHERE 内容ID = '" + news.getNewsId()
					+ "'";
		}
		return "";
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { RuntimeException.class })
	public int updateSyncSQL(String[] sqls){
		return DBTemplate.getInstance().batchUpdateSql(sqls).length;
	}

	/**
	 * 获得新增文档资料下载统计信息SQL
	 * @param news
	 * @return
	 */
	private String getInsertDownloadStatSql(DownloadStatisticsBean downloadStat) {
		if (downloadStat.getSourceId() != null && !"".equals(downloadStat.getSourceId())) {
			return "INSERT INTO " + TableConst.TABLE_SOURCE_STATISTICS
					+ " (内容ID,存储目录,关联权限,下载人,下载时间) VALUES('" + downloadStat.getSourceId()
					+ "','" + downloadStat.getStorePath() + "','" + downloadStat.getRelPriv()
					+ "','" + downloadStat.getDownloadUser() + "','" + downloadStat.getDownloadTime()
					+ "')";
		}
		return "";
	}
	/**
	 * aviyy	2014-3-7	新闻拖拽排序	获得最大顺序+1
	 * @return
	 */
	public int getNewsOrder(){
		String sql = "select max(顺序) from "+TableConst.TABLE_NEWS;
		return DBTemplate.getInstance().queryForInt(sql)+1;
	}
	/**
	 * pengain	2014-04-03	资源拖拽排序	获得最大顺序+1
	 * @return
	 */
	private int getSourceOrder(){
		String sql = "select max(顺序) from "+TableConst.TABLE_SOURCE;
		return DBTemplate.getInstance().queryForInt(sql)+1;
	}
	
	
	public void updateSource(SourceBean sourceBean) {
		if (sourceBean.getSourceId() != null && sourceBean.getRelPriv() != null) {
			String sql = "UPDATE " + TableConst.TABLE_SOURCE + " SET 顺序='"
			+ sourceBean.getOrder() + "'  WHERE 内容ID = '" + sourceBean.getSourceId()
			+ "' AND 关联权限 = '" +sourceBean.getRelPriv() + "'";
			DBTemplate.getInstance().updateSql(sql);
		}
	}
}
