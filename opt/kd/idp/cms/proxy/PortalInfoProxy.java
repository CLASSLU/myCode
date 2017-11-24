package kd.idp.cms.proxy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import kd.idp.cms.bean.PagingGridBean;
import kd.idp.cms.bean.portal.LinkBean;
import kd.idp.cms.bean.portal.NewsBean;
import kd.idp.cms.bean.portal.SourceBean;
import kd.idp.cms.bean.priv.PrivAttrBean;
import kd.idp.cms.bean.priv.PrivBean;
import kd.idp.cms.bean.priv.UserBean;
import kd.idp.cms.client.UserPrivClient;
import kd.idp.cms.dao.PortalInfoDao;
import kd.idp.cms.dao.PrivDao;
import kd.idp.cms.utility.CMSUtil;
import kd.idp.common.CommonTools;
import kd.idp.common.config.WebConfigUtil;
import kd.idp.common.flex.FlexConst;
import kd.idp.common.flex.FlexResult;
import kd.idp.common.flex.FlexSessionInvalidException;
import kd.idp.common.flex.FlexUtil;

import com.spring.ServiceManager;

public class PortalInfoProxy {

	private UserBean user = null;

	public PortalInfoProxy() {
		user = FlexUtil.getFlexCurrentUser();
//		System.out.println("当前登录用户: "
//				+ ((user != null) ? user.getUserDisplayName() : "未登录"));
	}

	public PortalInfoProxy(UserBean _user) {
		user = _user;
//		System.out.println("设置当前登录用户: "
//				+ ((user != null) ? user.getUserDisplayName() : "未登录"));
	}

	/**
	 * 获得新闻目录树
	 * 
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public String getNewsDirTree() throws FlexSessionInvalidException {
		if (FlexUtil.checkSession) {
			if (user == null) {
				throw new FlexSessionInvalidException();
			}
		}
		String xmllist = "";
		try {
			PrivDao privdao = ServiceManager.getPrivDao();
			List<PrivBean> allPrivList=privdao.getChildrenPrivRelUser(
					WebConfigUtil.getNewsTreeRoot(), user.getUserId(),false,"属性名 = '管理权限'");
			
	//		List<PrivBean> allPrivList=new UserPrivClient(user.getUserId()).getChildrenPrivsWithAttr(WebConfigUtil.getNewsTreeRoot(), false);
			//List<PrivBean> allPrivList =this.getProfessionPrivManageNode(WebConfigUtil.getNewsTreeRoot());
			xmllist = privdao.getPrivXML(allPrivList);
			xmllist = xmllist.replace(CommonTools.xmlHeader, "");
			xmllist = xmllist.replace("\n", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xmllist;
	}
	/**
	 * aviyy 2014-3-7	新闻发布定位到栏目
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public String getNewsDirSingleTree(String nodeId) throws FlexSessionInvalidException {
		if (FlexUtil.checkSession) {
			if (user == null) {
				throw new FlexSessionInvalidException();
			}
		}
		String xmllist = "";
		try {
			PrivDao privdao = ServiceManager.getPrivDao();
			List<PrivBean> allPrivList=privdao.getChildrenPrivRelUser(
					nodeId, user.getUserId(),false,"属性名 = '管理权限'");
			
	//		List<PrivBean> allPrivList=new UserPrivClient(user.getUserId()).getChildrenPrivsWithAttr(WebConfigUtil.getNewsTreeRoot(), false);
			//List<PrivBean> allPrivList =this.getProfessionPrivManageNode(WebConfigUtil.getNewsTreeRoot());
			xmllist = privdao.getPrivXML(allPrivList);
			xmllist = xmllist.replace(CommonTools.xmlHeader, "");
			xmllist = xmllist.replace("\n", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xmllist;
	}
	/**
	 * 获得新闻目录树 异步加载
	 * @param nodeId
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public String getNewsDirTreeNode(String nodeId) throws FlexSessionInvalidException {
		if (FlexUtil.checkSession) {
			if (user == null) {
				throw new FlexSessionInvalidException();
			}
		}
//		user = new UserBean ();
//		user.setUserId("USER_1282113554156_7943");
		StringBuffer sb = new StringBuffer();
		try {
			if(nodeId == null || "".equals(nodeId)){
				// 根节点
				nodeId = WebConfigUtil.getNewsTreeRoot();
				System.out.println(nodeId);
				sb.append("<node nodeId='"+nodeId+"' label='新闻'>");
			}
			PrivDao privdao = ServiceManager.getPrivDao();
			List<PrivBean> privlist = privdao.getChildrenPrivRelUser(nodeId, user.getUserId(), true,"属性名 = '管理权限'");
			
			
			if(privlist != null && privlist.size()>0){
				for(int i=0;i<privlist.size();i++){
					PrivBean p = privlist.get(i);
					sb.append("<node nodeId='" + p.getPrivId() + "' label='"
							+ p.getPrivName() + "'");
					if (privdao.getChildrenPrivInfo(p.getPrivId(), true, null)
							.size() > 0) {
						sb.append(" isBranch='true' ");
					} else {
						sb.append(" isBranch='false' ");
					}
					sb.append(" />");
				}
			}
			if(nodeId.equals(WebConfigUtil.getNewsTreeRoot())){
				// 根节点
				sb.append("</node>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println(sb.toString());
		return sb.toString();
	}
	
	/**
	 * 获得 新闻 列表
	 * @param privId
	 * @param pageNum
	 * @param filter
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public PagingGridBean<NewsBean> getNewsList(String privId, int pageNum,
			String filter) throws FlexSessionInvalidException {
		if (FlexUtil.checkSession) {
			if (user == null) {
				throw new FlexSessionInvalidException();
			}
		}
		PagingGridBean<NewsBean> newsGrid = new PagingGridBean<NewsBean>();
		try {
			PortalInfoDao portaldao = ServiceManager.getPortalInfoDao();
			newsGrid = portaldao.getNewsFromPriv(privId, WebConfigUtil
					.getFlexGridPagingCount(), pageNum, filter, true);
			
			//冀北地调动态特殊处理
			String userName=user.getUserName();
			String userDiaplayName=user.getUserDisplayName();
			if(userName.indexOf("dt")>-1){
				List<NewsBean> list=new ArrayList<NewsBean>();
				for(NewsBean news:newsGrid.getGridData()){
					if(news.getCreaterId().equals(user.getUserId())){
						list.add(news);
					}
				}
				newsGrid.setGridData(list);
			}
			//冀北地调特殊处理结束
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newsGrid;
	}

	/**
	 * 保存 新闻修改
	 * 
	 * @param news
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public FlexResult updateNews(NewsBean news)
			throws FlexSessionInvalidException {
		if (FlexUtil.checkSession) {
			if (user == null) {
				throw new FlexSessionInvalidException();
			}
		}
		FlexResult result = new FlexResult();
		try {
			if (news.getStatus() != 0
					|| !user.getUserId().equals(news.getCreaterId())) {
				result.setStatus(FlexConst.RESULT_FAIL);
				result.setMessage("用戶只能修改本人编写的未发布的新闻!");
			}
			PortalInfoDao portaldao = ServiceManager.getPortalInfoDao();
			result.setMessage("执行成功,更新" + portaldao.updateNews(news) + " 条记录!");
			result.setStatus(FlexConst.RESULT_OK);
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}

	/**
	 * aviyy 2014-3-7 	新闻拖拽排序	置顶
	 * @param news
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public FlexResult newsToTop(List<NewsBean> newsList) throws FlexSessionInvalidException {
		if (FlexUtil.checkSession) {
			if (user == null) {
				throw new FlexSessionInvalidException();
			}
		}
		FlexResult result = new FlexResult();
		try {
			PortalInfoDao portaldao = ServiceManager.getPortalInfoDao();
			for (NewsBean news:newsList) {
				news.setOrder(portaldao.getNewsOrder());
				portaldao.updateNews(news);
			}
			result.setMessage("执行成功！");
			result.setStatus(FlexConst.RESULT_OK);
			
		}catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}
	/**
	 * aviyy 2014-3-7 	新闻拖拽排序	拖拽交换
	 * @param newsList
	 * @param up
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	@Transactional
	public FlexResult newsChangeOrder(List<NewsBean> newsList) throws FlexSessionInvalidException {
		if (FlexUtil.checkSession) {
			if (user == null) {
				throw new FlexSessionInvalidException();
			}
		}
		FlexResult result = new FlexResult();
		try {
			PortalInfoDao portaldao = ServiceManager.getPortalInfoDao();
			if(newsList.size()>1){
				int order = newsList.get(0).getOrder();
				for (int i=0;i<newsList.size()-1;i++) {
					newsList.get(i).setOrder(newsList.get(i+1).getOrder());
					portaldao.updateNews(newsList.get(i));
				}
				newsList.get(newsList.size()-1).setOrder(order);
				portaldao.updateNews(newsList.get(newsList.size()-1));
			}
			result.setMessage("执行成功！");
			result.setStatus(FlexConst.RESULT_OK);
		}catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}
	/**
	 * 删除新闻
	 * 
	 * @param news
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public FlexResult deleteNews(List<NewsBean> news)
			throws FlexSessionInvalidException {
		if (FlexUtil.checkSession) {
			if (user == null) {
				throw new FlexSessionInvalidException();
			}
		}
		FlexResult result = new FlexResult();
		try {
			PortalInfoDao portaldao = ServiceManager.getPortalInfoDao();
			result.setMessage("执行成功,删除" + portaldao.deleteNews(news) + " 条记录!");
			result.setStatus(FlexConst.RESULT_OK);
			// 删除文件
			for (int i = 0; i < news.size(); i++) {
				File f = new File(WebConfigUtil.getNewsFilePath() + "/"
						+ news.get(i).getStorePath(), news.get(i).getNewsId()
						+ ".html");
				if (f.exists()) {
					f.delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}

	/**
	 * 发布新闻
	 * 
	 * @param news
	 * @param publishFlag
	 * @param syncNews 是否同步新闻
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public FlexResult publishNews(NewsBean news, boolean publishFlag,boolean syncNews)
			throws FlexSessionInvalidException {
		if (FlexUtil.checkSession) {
			if (user == null) {
				throw new FlexSessionInvalidException();
			}
		}
		FlexResult result = new FlexResult();
		try {
			PortalInfoDao portaldao = ServiceManager.getPortalInfoDao();
			List<NewsBean> sblist = portaldao.publishNews(user, news,
					publishFlag);
			if (sblist.size() > 0) {
				result.setStatus(FlexConst.RESULT_OK);
				result.setMessage("发布成功!");
				result.setResultObject(sblist.get(0));
				if(syncNews){
					CMSUtil.sendNewsToSync(news);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}

	/**
	 * 新闻同步发布
	 * @param source
	 * @param subPrivs
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public FlexResult saveNewsPriv(NewsBean news, boolean publishFlag, boolean syncNews, List<String> subNewsPrivs ) throws FlexSessionInvalidException{
		FlexResult result = new FlexResult();
		PortalInfoDao portaldao = ServiceManager.getPortalInfoDao();
		for(int i=0;i<subNewsPrivs.size();i++){
			if(!news.getRelPriv().equals(subNewsPrivs.get(i))){
				news.setRelPriv(subNewsPrivs.get(i));
				portaldao.addNews1(user,news);
			}
		}
		result = publishNews(news,true,syncNews);
		return result;
	}
	/**
	 * 获得文档资料目录树
	 * 
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public String getSourceDirTree() throws FlexSessionInvalidException {
		if (FlexUtil.checkSession) {
			if (user == null) {
				throw new FlexSessionInvalidException();
			}
		}
		String xmllist = "";
		try {
			PrivDao privdao = ServiceManager.getPrivDao();
			xmllist = privdao.getPrivXML(privdao.getChildrenPrivRelUser(
					WebConfigUtil.getSourceTreeRoot(), user.getUserId(), false));
			xmllist = xmllist.replace(CommonTools.xmlHeader, "");
			xmllist = xmllist.replace("\n", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xmllist;
	}

	/**
	 * 获得 文档资料 列表
	 * 
	 * @param privId
	 * @param pageNum
	 * @param filter
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public PagingGridBean<SourceBean> getSourceList(String privId, int pageNum,
			String filter) throws FlexSessionInvalidException {
		if (FlexUtil.checkSession) {
			if (user == null) {
				throw new FlexSessionInvalidException();
			}
		}
		PagingGridBean<SourceBean> sourceGrid = new PagingGridBean<SourceBean>();
		try {
			PortalInfoDao portaldao = ServiceManager.getPortalInfoDao();
			sourceGrid = portaldao.getSourceFromPriv(privId, WebConfigUtil
					.getFlexGridPagingCount(), pageNum, filter, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sourceGrid;
	}

	/**
	 * 保存上传文档资料
	 * 
	 * @param fileList
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public FlexResult saveUploadedFiles(ArrayList<Map<String, Object>> fileList)
			throws FlexSessionInvalidException {
		if (FlexUtil.checkSession) {
			if (user == null) {
				throw new FlexSessionInvalidException();
			}
		}
		FlexResult result = new FlexResult();
		try {
			PortalInfoDao portaldao = ServiceManager.getPortalInfoDao();
			result.setStatus(FlexConst.RESULT_OK);
			result.setMessage("执行成功,插入" + portaldao.addSource(user, fileList)
					+ " 条记录!");
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 添加文档资料
	 * @param fileList
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public FlexResult addSource(SourceBean source)
			throws FlexSessionInvalidException {
		if (FlexUtil.checkSession) {
			if (user == null) {
				throw new FlexSessionInvalidException();
			}
		}
		FlexResult result = new FlexResult();
		try {
			PortalInfoDao portaldao = ServiceManager.getPortalInfoDao();
			result.setStatus(FlexConst.RESULT_OK);
			result.setMessage("执行成功,插入" + portaldao.addSource(user, source)
					+ " 条记录!");
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}
	

	/**
	 * 删除文档资料
	 * 
	 * @param files
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public FlexResult deleteSourceFiles(List<SourceBean> files)
			throws FlexSessionInvalidException {
		if (FlexUtil.checkSession) {
			if (user == null) {
				throw new FlexSessionInvalidException();
			}
		}
		FlexResult result = new FlexResult();
		try {
			PortalInfoDao portaldao = ServiceManager.getPortalInfoDao();
			result.setMessage("执行成功,删除" + portaldao.deleteSource(files)
					+ " 条记录!");
			result.setStatus(FlexConst.RESULT_OK);
			// 删除文件
			for (int i = 0; i < files.size(); i++) {
				File f = new File(WebConfigUtil.getSourceFilePath() + "/"
						+ files.get(i).getStorePath(), files.get(i)
						.getFileName());
				if (f.exists()) {
					f.delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}

	/**
	 * 发布文档资料
	 * 
	 * @param source
	 * @param publishFlag
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public FlexResult publishSourceFile(SourceBean source, boolean publishFlag)
			throws FlexSessionInvalidException {
		if (FlexUtil.checkSession) {
			if (user == null) {
				throw new FlexSessionInvalidException();
			}
		}
		FlexResult result = new FlexResult();
		try {
			PortalInfoDao portaldao = ServiceManager.getPortalInfoDao();
			List<SourceBean> sblist = portaldao.publishSourceFile(user, source,
					publishFlag);
			result.setStatus(FlexConst.RESULT_OK);
			result.setMessage("操作成功!");
			if (sblist.size() > 0) {
				result.setResultObject(sblist.get(0));
			}else{
				result.setResultObject(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}

	/**
	 * 保存修改的文档资料
	 * 
	 * @param source
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public FlexResult saveSource(SourceBean source)
			throws FlexSessionInvalidException {
		if (FlexUtil.checkSession) {
			if (user == null) {
				throw new FlexSessionInvalidException();
			}
		}
		FlexResult result = new FlexResult();
		try {

		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}
	
	/**
	 * aviyy 2014-04-02 	文档拖拽排序	拖拽交换
	 * @param newsList
	 * @param up
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	@Transactional
	public FlexResult sourceChangeOrder(List<SourceBean> sourceList) throws FlexSessionInvalidException {
		if (FlexUtil.checkSession) {
			if (user == null) {
				throw new FlexSessionInvalidException();
			}
		}
		FlexResult result = new FlexResult();
		try {
			PortalInfoDao portaldao = ServiceManager.getPortalInfoDao();
			if(sourceList.size()>1){
				int order = sourceList.get(0).getOrder();
				for (int i=0;i<sourceList.size()-1;i++) {
					sourceList.get(i).setOrder(sourceList.get(i+1).getOrder());
					portaldao.updateSource(sourceList.get(i));
				}
				sourceList.get(sourceList.size()-1).setOrder(order);
				portaldao.updateSource(sourceList.get(sourceList.size()-1));
			}
			result.setMessage("执行成功！");
			result.setStatus(FlexConst.RESULT_OK);
		}catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}

	/**
	 * 获得网站链接目录树
	 * 
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public String getLinkDirTree() throws FlexSessionInvalidException {
		if (FlexUtil.checkSession) {
			if (user == null) {
				throw new FlexSessionInvalidException();
			}
		}
		String xmllist = "";
		try {
			PrivDao privdao = ServiceManager.getPrivDao();
			xmllist = privdao.getPrivXML(privdao.getChildrenPrivRelUser(
					WebConfigUtil.getLinkTreeRoot(), user.getUserId(), false));
			xmllist = xmllist.replace(CommonTools.xmlHeader, "");
			xmllist = xmllist.replace("\n", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xmllist;
	}

	/**
	 * 获得 网站链接 列表
	 * 
	 * @param privId
	 * @param pageNum
	 * @param filter
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public PagingGridBean<LinkBean> getLinkList(String privId, int pageNum,
			String filter) throws FlexSessionInvalidException {
		if (FlexUtil.checkSession) {
			if (user == null) {
				throw new FlexSessionInvalidException();
			}
		}
		PagingGridBean<LinkBean> linkGrid = new PagingGridBean<LinkBean>();
		try {
			PortalInfoDao portaldao = ServiceManager.getPortalInfoDao();
			linkGrid = portaldao.getLinkFromPriv(privId, WebConfigUtil
					.getFlexGridPagingCount(), pageNum, filter, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return linkGrid;
	}

	/**
	 * 保存修改,新增链接
	 * 
	 * @param link
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public FlexResult saveLink(LinkBean link)
			throws FlexSessionInvalidException {
		if (FlexUtil.checkSession) {
			if (user == null) {
				throw new FlexSessionInvalidException();
			}
		}
		FlexResult result = new FlexResult();
		try {
			PortalInfoDao portaldao = ServiceManager.getPortalInfoDao();
			if (link.getLinkId() == null || "".equals(link.getLinkId())) {
				if (portaldao.addNewLink(link) > 0) {
					result.setStatus(FlexConst.RESULT_OK);
					result.setMessage("新增链接成功!");
				}
			} else {
				if (portaldao.modifyLink(link) > 0) {
					result.setStatus(FlexConst.RESULT_OK);
					result.setMessage("修改链接成功!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}

	/**
	 * 删除链接
	 * 
	 * @param links
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public FlexResult deleteLinks(List<LinkBean> links)
			throws FlexSessionInvalidException {
		if (FlexUtil.checkSession) {
			if (user == null) {
				throw new FlexSessionInvalidException();
			}
		}
		FlexResult result = new FlexResult();
		try {
			PortalInfoDao portaldao = ServiceManager.getPortalInfoDao();
			result.setMessage("执行成功,删除" + portaldao.deleteLinks(links)
					+ " 条记录!");
			result.setStatus(FlexConst.RESULT_OK);
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}

	/**
	 * 发布网站链接
	 * 
	 * @param link
	 * @param publishFlag
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public FlexResult publishLink(LinkBean link, boolean publishFlag)
			throws FlexSessionInvalidException {
		if (FlexUtil.checkSession) {
			if (user == null) {
				throw new FlexSessionInvalidException();
			}
		}
		FlexResult result = new FlexResult();
		try {
			PortalInfoDao portaldao = ServiceManager.getPortalInfoDao();
			if (portaldao.publishLink(user, link, publishFlag) > 0) {
				result.setStatus(FlexConst.RESULT_OK);
				result.setMessage("操作成功!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}

	/**
	 * 获得专业动态目录树
	 * 
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public String getProDirTree() throws FlexSessionInvalidException {
		if (FlexUtil.checkSession) {
			if (user == null) {
				throw new FlexSessionInvalidException();
			}
		}
		String xmllist = "";
		try {
			PrivDao privdao = ServiceManager.getPrivDao();
			xmllist = privdao.getPrivXML(getProfessionPrivManageNode(WebConfigUtil.getProfessionTreeRoot()));
			xmllist = xmllist.replace(CommonTools.xmlHeader, "");
			xmllist = xmllist.replace("\n", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xmllist;
	}
	
	/**
	 * 获得专业动态目录树 异步加载（2014-02-23实现传参加载对应目录树）
	 * @param nodeId
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public String getProDirTreeNodeRecvParam(String nodeId) throws FlexSessionInvalidException {
		if (FlexUtil.checkSession) {
			if (user == null) {
				throw new FlexSessionInvalidException();
			}
		}
//		user = new UserBean ();
//		user.setUserId("USER_1282113554156_7943");
		StringBuffer sb = new StringBuffer();
		try {
			PrivDao privdao = ServiceManager.getPrivDao();
			if(nodeId == null || "".equals(nodeId)){
				// 根节点
				nodeId = WebConfigUtil.getProfessionTreeRoot();
			}
			String privName = privdao.getPrivName(nodeId);
			sb.append("<node nodeId='"+nodeId+"' label='" + privName + "'>");
			
			List<PrivBean> privlist = privdao.getChildrenPrivRelUser(nodeId, user.getUserId(), true,"属性名 = '管理权限'");
			if(privlist != null && privlist.size()>0){
				for(int i=0;i<privlist.size();i++){
					PrivBean p = privlist.get(i);
					sb.append("<node nodeId='" + p.getPrivId() + "' label='"
							+ p.getPrivName() + "'");
					if (privdao.getChildrenPrivInfo(p.getPrivId(), true, null)
							.size() > 0) {
						sb.append(" isBranch='true' ");
					} else {
						sb.append(" isBranch='false' ");
					}
					sb.append(" />");
				}
			}
			sb.append("</node>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	/**
	 * 获得专业动态目录树 异步加载
	 * @param nodeId
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public String getProDirTreeNode(String nodeId) throws FlexSessionInvalidException {
		if (FlexUtil.checkSession) {
			if (user == null) {
				throw new FlexSessionInvalidException();
			}
		}
//		user = new UserBean ();
//		user.setUserId("USER_1282113554156_7943");
		StringBuffer sb = new StringBuffer();
		try {
			if(nodeId == null || "".equals(nodeId)){
				// 根节点
				nodeId = WebConfigUtil.getProfessionTreeRoot();
				sb.append("<node nodeId='"+nodeId+"' label='文档资料'>");
			}
			PrivDao privdao = ServiceManager.getPrivDao();
			List<PrivBean> privlist = privdao.getChildrenPrivRelUser(nodeId, user.getUserId(), true,"属性名 = '管理权限'");
			if(privlist != null && privlist.size()>0){
				for(int i=0;i<privlist.size();i++){
					PrivBean p = privlist.get(i);
					sb.append("<node nodeId='" + p.getPrivId() + "' label='"
							+ p.getPrivName() + "'");
					if (privdao.getChildrenPrivInfo(p.getPrivId(), true, null)
							.size() > 0) {
						sb.append(" isBranch='true' ");
					} else {
						sb.append(" isBranch='false' ");
					}
					sb.append(" />");
				}
			}
			if(nodeId.equals(WebConfigUtil.getProfessionTreeRoot())){
				// 根节点
				sb.append("</node>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
//	/**
//	 * 获得专业动态目录树 异步加载
//	 * @param nodeId
//	 * @return
//	 * @throws FlexSessionInvalidException
//	 */
//	public String getProPublicDirTreeNode(String nodeId,String conid) throws FlexSessionInvalidException {
//		if (FlexUtil.checkSession) {
//			if (user == null) {
//				throw new FlexSessionInvalidException();
//			}
//		}
//		StringBuffer sb = new StringBuffer();
//		try {
//			if(nodeId == null || "".equals(nodeId)){
//				// 根节点
//				nodeId = WebConfigUtil.getProfessionTreeRoot();
//				sb.append("<node nodeId='"+nodeId+"' label='专业动态'>");
//			}
//			PrivDao privdao = ServiceManager.getPrivDao();
//			List<PrivPublicBean> privlist = privdao.getChildrenPriUser(nodeId, user.getUserId(), "属性名 = '管理权限'",conid);
//			if(privlist != null && privlist.size()>0){
//				for(int i=0;i<privlist.size();i++){
//					PrivBean p = privlist.get(i);
//					sb.append("<node nodeId='" + p.getPrivId() + "' label='"
//							+ p.getPrivName() + "'");
//					if (privdao.getChildrenPrivInfo(p.getPrivId(), true, null)
//							.size() > 0) {
//						sb.append(" isBranch='true' ");
//					} else {
//						sb.append(" isBranch='false' ");
//					}
//					sb.append(" />");
//				}
//			}
//			if(nodeId.equals(WebConfigUtil.getProfessionTreeRoot())){
//				// 根节点
//				sb.append("</node>");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return sb.toString();
//	}
	
	/**
	 * 
	 * @param privClient
	 * @param nodeId
	 * @return
	 */
	private List<PrivBean> getProfessionPrivManageNode(String privId){
		List<PrivBean> privList = new UserPrivClient(user.getUserId())
				.getChildrenPrivsWithAttr(privId, false);
		boolean hasManage = false;
		List<PrivBean> managePrivList = new ArrayList<PrivBean>();
		for (int i = 0; i < privList.size(); i++) {
			List<PrivAttrBean> attrs = privList.get(i).getAttrList();
			if (attrs != null) {
				for (int j = 0; j < attrs.size(); j++) {
					if ("管理权限".equals(attrs.get(j).getPrivAttrName())) {
						hasManage = true;
						break;
					}
				}
			}
			if (hasManage) {
				managePrivList.add(privList.get(i));
			}
			hasManage = false;
		}
		return managePrivList;
	}
	/**
	 * 专业动态发布
	 * @param source
	 * @param subPrivs
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public FlexResult savePriv(SourceBean source,List<String> subPrivs ) throws FlexSessionInvalidException{
		FlexResult result = new FlexResult();
		PortalInfoDao portaldao = ServiceManager.getPortalInfoDao();
		for(int i=0;i<subPrivs.size();i++){
			if(!source.getRelPriv().equals(subPrivs.get(i))){
				source.setRelPriv(subPrivs.get(i));
				portaldao.addSource1(user,source);
			}
		}
		result = publishSourceFile(source,true);
		return result;
	}

}
