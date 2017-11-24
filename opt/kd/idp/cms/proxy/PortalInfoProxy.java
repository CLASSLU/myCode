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
//		System.out.println("��ǰ��¼�û�: "
//				+ ((user != null) ? user.getUserDisplayName() : "δ��¼"));
	}

	public PortalInfoProxy(UserBean _user) {
		user = _user;
//		System.out.println("���õ�ǰ��¼�û�: "
//				+ ((user != null) ? user.getUserDisplayName() : "δ��¼"));
	}

	/**
	 * �������Ŀ¼��
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
					WebConfigUtil.getNewsTreeRoot(), user.getUserId(),false,"������ = '����Ȩ��'");
			
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
	 * aviyy 2014-3-7	���ŷ�����λ����Ŀ
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
					nodeId, user.getUserId(),false,"������ = '����Ȩ��'");
			
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
	 * �������Ŀ¼�� �첽����
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
				// ���ڵ�
				nodeId = WebConfigUtil.getNewsTreeRoot();
				System.out.println(nodeId);
				sb.append("<node nodeId='"+nodeId+"' label='����'>");
			}
			PrivDao privdao = ServiceManager.getPrivDao();
			List<PrivBean> privlist = privdao.getChildrenPrivRelUser(nodeId, user.getUserId(), true,"������ = '����Ȩ��'");
			
			
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
				// ���ڵ�
				sb.append("</node>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println(sb.toString());
		return sb.toString();
	}
	
	/**
	 * ��� ���� �б�
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
			
			//�����ص���̬���⴦��
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
			//�����ص����⴦�����
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newsGrid;
	}

	/**
	 * ���� �����޸�
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
				result.setMessage("�Ñ�ֻ���޸ı��˱�д��δ����������!");
			}
			PortalInfoDao portaldao = ServiceManager.getPortalInfoDao();
			result.setMessage("ִ�гɹ�,����" + portaldao.updateNews(news) + " ����¼!");
			result.setStatus(FlexConst.RESULT_OK);
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}

	/**
	 * aviyy 2014-3-7 	������ק����	�ö�
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
			result.setMessage("ִ�гɹ���");
			result.setStatus(FlexConst.RESULT_OK);
			
		}catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}
	/**
	 * aviyy 2014-3-7 	������ק����	��ק����
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
			result.setMessage("ִ�гɹ���");
			result.setStatus(FlexConst.RESULT_OK);
		}catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}
	/**
	 * ɾ������
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
			result.setMessage("ִ�гɹ�,ɾ��" + portaldao.deleteNews(news) + " ����¼!");
			result.setStatus(FlexConst.RESULT_OK);
			// ɾ���ļ�
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
	 * ��������
	 * 
	 * @param news
	 * @param publishFlag
	 * @param syncNews �Ƿ�ͬ������
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
				result.setMessage("�����ɹ�!");
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
	 * ����ͬ������
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
	 * ����ĵ�����Ŀ¼��
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
	 * ��� �ĵ����� �б�
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
	 * �����ϴ��ĵ�����
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
			result.setMessage("ִ�гɹ�,����" + portaldao.addSource(user, fileList)
					+ " ����¼!");
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}
	
	/**
	 * ����ĵ�����
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
			result.setMessage("ִ�гɹ�,����" + portaldao.addSource(user, source)
					+ " ����¼!");
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}
	

	/**
	 * ɾ���ĵ�����
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
			result.setMessage("ִ�гɹ�,ɾ��" + portaldao.deleteSource(files)
					+ " ����¼!");
			result.setStatus(FlexConst.RESULT_OK);
			// ɾ���ļ�
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
	 * �����ĵ�����
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
			result.setMessage("�����ɹ�!");
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
	 * �����޸ĵ��ĵ�����
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
	 * aviyy 2014-04-02 	�ĵ���ק����	��ק����
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
			result.setMessage("ִ�гɹ���");
			result.setStatus(FlexConst.RESULT_OK);
		}catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}

	/**
	 * �����վ����Ŀ¼��
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
	 * ��� ��վ���� �б�
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
	 * �����޸�,��������
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
					result.setMessage("�������ӳɹ�!");
				}
			} else {
				if (portaldao.modifyLink(link) > 0) {
					result.setStatus(FlexConst.RESULT_OK);
					result.setMessage("�޸����ӳɹ�!");
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
	 * ɾ������
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
			result.setMessage("ִ�гɹ�,ɾ��" + portaldao.deleteLinks(links)
					+ " ����¼!");
			result.setStatus(FlexConst.RESULT_OK);
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}

	/**
	 * ������վ����
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
				result.setMessage("�����ɹ�!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}

	/**
	 * ���רҵ��̬Ŀ¼��
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
	 * ���רҵ��̬Ŀ¼�� �첽���أ�2014-02-23ʵ�ִ��μ��ض�ӦĿ¼����
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
				// ���ڵ�
				nodeId = WebConfigUtil.getProfessionTreeRoot();
			}
			String privName = privdao.getPrivName(nodeId);
			sb.append("<node nodeId='"+nodeId+"' label='" + privName + "'>");
			
			List<PrivBean> privlist = privdao.getChildrenPrivRelUser(nodeId, user.getUserId(), true,"������ = '����Ȩ��'");
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
	 * ���רҵ��̬Ŀ¼�� �첽����
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
				// ���ڵ�
				nodeId = WebConfigUtil.getProfessionTreeRoot();
				sb.append("<node nodeId='"+nodeId+"' label='�ĵ�����'>");
			}
			PrivDao privdao = ServiceManager.getPrivDao();
			List<PrivBean> privlist = privdao.getChildrenPrivRelUser(nodeId, user.getUserId(), true,"������ = '����Ȩ��'");
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
				// ���ڵ�
				sb.append("</node>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
//	/**
//	 * ���רҵ��̬Ŀ¼�� �첽����
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
//				// ���ڵ�
//				nodeId = WebConfigUtil.getProfessionTreeRoot();
//				sb.append("<node nodeId='"+nodeId+"' label='רҵ��̬'>");
//			}
//			PrivDao privdao = ServiceManager.getPrivDao();
//			List<PrivPublicBean> privlist = privdao.getChildrenPriUser(nodeId, user.getUserId(), "������ = '����Ȩ��'",conid);
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
//				// ���ڵ�
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
					if ("����Ȩ��".equals(attrs.get(j).getPrivAttrName())) {
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
	 * רҵ��̬����
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
