package kd.idp.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.spring.ServiceManager;
import com.spring.dbservice.DBTemplate;
import com.spring.dbservice.DBTools;

import kd.idp.cms.bean.portal.LinkBean;
import kd.idp.cms.bean.portal.NewsBean;
import kd.idp.cms.bean.PagingGridBean;
import kd.idp.cms.bean.priv.PrivAttrBean;
import kd.idp.cms.bean.priv.PrivBean;
import kd.idp.cms.bean.portal.SourceBean;
import kd.idp.cms.bean.priv.UserBean;
import kd.idp.cms.client.UserPrivClient;
import kd.idp.cms.mapper.portal.LinkRowMapper;
import kd.idp.cms.mapper.portal.NewsRowMapper;
import kd.idp.cms.mapper.portal.SourceRowMapper;
import kd.idp.cms.mapper.priv.UserRowMapper;
import kd.idp.common.config.WebConfigUtil;
import kd.idp.common.consts.TableConst;

/**
 * �û�Ȩ����� �Ż�չʾ
 * 
 * @author caoyu
 * 
 */
public class UserPrivContent {

	/**
	 * �û�ID
	 */
	private String userId = null;

	private UserPrivClient userpriv = null;

	public UserPrivContent() {

	}

	public UserPrivContent(String _userId) {
		userId = _userId;
		userpriv = new UserPrivClient(_userId); 
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		userpriv = new UserPrivClient(userId);
		this.userId = userId;
	}

	/**
	 * ����û���Ϣ
	 * 
	 * @return
	 */
	public UserBean getUser() {
		String sql = "SELECT * FROM " + TableConst.VIEW_MANAGE_USER
				+ " where �û�ID='" + userId + "'";
		return DBTemplate.getInstance().getResultRowMapper(sql, new UserRowMapper());
	}

	/**
	 * �Ƿ��ǹ���Ա
	 * @return
	 */
	public boolean hasAdminRole(){
		return userpriv.hasAdminRole();
	}
	/**
	 * ���ģ��·��
	 * 
	 * @return
	 */
	public String getModelPath() {
		String model = null;
		try {
			if (userpriv != null) {
				List<PrivBean> pblist = ServiceManager.getPrivDao().getChildrenPrivRelUser(
								WebConfigUtil.getWebModelRootId(), userId, true);
				if (pblist.size() > 0) {
					Map<String, PrivAttrBean> attr = userpriv
							.getPrivAttrMap(pblist.get(0).getPrivId());
					model = attr.get("ģ��·��").getPrivAttrValue();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (model == null) {
			//model = WebConfigUtil.getWebDefaultModel();
			
		}
		return model;
	}
	
	/**
	 * ��ù����û�
	 * @return
	 */
	public String getPublicUser(){
		String publicuser = null;
		try {
			if (userpriv != null) {
				List<PrivBean> pblist = ServiceManager.getPrivDao().getChildrenPrivRelUser(
								WebConfigUtil.getWebModelRootId(), userId, true);
				if (pblist.size() > 0) {
					Map<String, PrivAttrBean> attr = userpriv
							.getPrivAttrMap(pblist.get(0).getPrivId());
					publicuser = attr.get("�����û�").getPrivAttrValue();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (publicuser == null) {
			publicuser = WebConfigUtil.getDefaultUser();
			return null;
		}
		return publicuser;
	}

	/**
	 * ͨ��Ȩ�޻���ĵ�����רҵ��̬��Ŀ LQ
	 * 
	 * @param model��columnId��skipPage��inputText
	 * @return
	 */
	public List<Map<String, String>> getProfessionColumns() {
		List<Map<String, String>> columns = new ArrayList<Map<String, String>>();
		try {
			if (userpriv != null) {
				 // ȡ�ø��ڵ�
				List<PrivBean> list = ServiceManager.getPrivDao().getChildrenPrivRelUser(
						WebConfigUtil.getProfessionTreeRoot(), userId, false);
				for (int j = 0; j < list.size(); j++) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("COLUMNID", list.get(j).getPrivId());
						map.put("COLUMNNAME", list.get(j).getPrivName());
						columns.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return columns;
	}

	/**
	 * ���˹���̨���� LQ
	 * 
	 * @param model��columnId��skipPage��inputText
	 * @return
	 */
	public List<Map<String, Object>> getPersonalStation(String _userId) {
		List<Map<String, Object>> columns = new ArrayList<Map<String, Object>>(); // ���صĽ��
		try {
			if (userpriv != null) {
				 // ȡ�ø��ڵ�
				List<PrivBean> list2 = ServiceManager.getPrivDao().getChildrenPrivRelUser(
						WebConfigUtil.getAppTreeRoot(), _userId, true);
				UserPrivClient upriv = new UserPrivClient(_userId);
				for (int j = 0; j < list2.size(); j++) {
					List<PrivBean> pblist = upriv.getChildrenPrivsWithAttr(list2
							.get(j).getPrivId(), true); // ȡ���ӽڵ�
					Map<String, Object> map1 = new HashMap<String, Object>();
					
					map1.put("GroudName", list2.get(j).getPrivName());
					List<Map<String, String>> row = new ArrayList<Map<String, String>>(); // listװ�ظ�Ӧ��

					for (int i = 0; i < pblist.size(); i++) {

						Map<String, String> map2 = new HashMap<String, String>();
						map2.put("AppId", pblist.get(i).getPrivId());
						String appname = pblist.get(i).getPrivName();

						map2.put("AppName", appname);
						for (int a = 0; a < pblist.get(i).getAttrList().size(); a++) {
							if ("����".equals(pblist.get(i).getAttrList().get(a)
									.getPrivAttrType())) {
								map2.put(pblist.get(i).getAttrList().get(a)
										.getPrivAttrName(), pblist.get(i)
										.getAttrList().get(a)
										.getPrivAttrValue());
							}
						}
						row.add(map2);
					}
					map1.put("GroudContent", row);
					columns.add(map1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return columns;
	}

	/**
	 * all privileges both user has and not has
	 * aviyy
	 * 2014-01-24
	 * @param model��columnId��skipPage��inputText
	 * @return
	 */
	public List<Map<String, Object>> getPersonalVisualStation(String _userId) {
		List<Map<String, Object>> columns = new ArrayList<Map<String, Object>>(); // ���صĽ��
		
		try {
			if (userpriv != null) {
				 // ȡ�ø��ڵ�
				UserPrivClient upriv = new UserPrivClient(_userId);
				List<PrivBean> list2 = upriv.getPrivDirectory(WebConfigUtil.getAppTreeRoot(), true, false);
				
				for (int j = 0; j < list2.size(); j++) {
					List<PrivBean> pblist = upriv.getVisualPrivDirectory(list2
							.get(j).getPrivId(),true, true);
					List<PrivBean> pblist2 = upriv.getChildrenPrivsWithAttr(list2
							.get(j).getPrivId(), false); // ȡ���ӽڵ�
					Map<String, Object> map1 = new HashMap<String, Object>();
					
					map1.put("GroudName", list2.get(j).getPrivName());
					List<Map<String, String>> row = new ArrayList<Map<String, String>>(); // listװ�ظ�Ӧ��

					for (int i = 0; i < pblist.size(); i++) {

						Map<String, String> map2 = new HashMap<String, String>();
						map2.put("AppId", pblist.get(i).getPrivId());
						String appname = pblist.get(i).getPrivName();

						map2.put("AppName", appname);
						//aviyy	2014-01-24	if user has this privilege
						map2.put("isHas","no");
						for(PrivBean pri:pblist2){
							if(pri.getPrivId().equals(pblist.get(i).getPrivId())){
								map2.put("isHas","yes");
								break;
							}
						}
						//
						for (int a = 0; a < pblist.get(i).getAttrList().size(); a++) {
							if ("����".equals(pblist.get(i).getAttrList().get(a)
									.getPrivAttrType())) {
								map2.put(pblist.get(i).getAttrList().get(a)
										.getPrivAttrName(), pblist.get(i)
										.getAttrList().get(a)
										.getPrivAttrValue());
							}
						}
						row.add(map2);
					}
					map1.put("GroudContent", row);
					columns.add(map1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return columns;
	}
	
	
	/**
	 * ͨ��Ȩ�޻���ĵ�������Ŀ
	 * 
	 * @param model��columnId��skipPage��inputText
	 * @return
	 */
	public Map<String,List<PrivBean>> getSourceColumns() {
		Map<String,List<PrivBean>> all=new HashMap<String,List<PrivBean>>();
		
		try {
			if (userpriv != null) {
				List<PrivBean> attlist=userpriv.getChildrenPrivsWithAttr(WebConfigUtil.getSourceTreeRoot(), false);
				//�ĵ������µ�ֱ���ӽڵ�
				List<PrivBean> next=this.getChildrenPriv(attlist, WebConfigUtil.getSourceTreeRoot());
			
				for(PrivBean priv:next){
						List<PrivBean> list=this.getChildrenPriv(attlist, priv.getPrivId());
						all.put(priv.getPrivName(), list);
				}
				
//				List<PrivBean> list = ServiceManager.getPrivDao().getChildrenPrivRelUser(
//						WebConfigUtil.getSourceTreeRoot(), userId, true);
//				for(int k=0;k<list.size();k++) {
//					List<PrivBean> pblist = userpriv.getChildrenPrivsWithAttr(list.get(
//							k).getPrivId(), true);
//					all.put(list.get(k).getPrivName(), pblist);
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return all;
	}
	/**
	 * ��Ȩ���б��л�ȡȨ��������Ȩ��
	 * 
	 * @param model��columnId��skipPage��inputText
	 * @return
	 */
	public List<PrivBean> getChildrenPriv(List<PrivBean> list,String privId){
		List<PrivBean> result=new ArrayList<PrivBean>();
		for(PrivBean priv:list){
			if(priv.getPrivParentId().equals(privId)){
				result.add(priv);
			}
		}
		return result;
	}

	/**
	 * ͨ��Ȩ�޺���ĿID����ĵ���������
	 * 
	 * @param columnId
	 * @param skipPage
	 * @param inputText
	 * @return
	 */
	public PagingGridBean<SourceBean> getSourceList(String columnId,
			int skipPage, String inputText) {
//		String sql = "SELECT * FROM " + TableConst.TABLE_SOURCE
//				+ " WHERE ����Ȩ�� = '" + columnId + "' AND ״̬ > 0 ";
		String filtersql="SELECT  Ȩ��ID FROM " +TableConst.TABLE_MANAGE_PRIV+" start with Ȩ��ID='"+columnId+"' connect by prior Ȩ��ID=Ȩ�޸�ID";
		String sql = "SELECT * FROM " + TableConst.TABLE_SOURCE
		+ " WHERE ����Ȩ�� in (" + filtersql + ") AND ״̬ > 0 ";
		if (inputText != null) {
			sql += " AND �ĵ����� like '%" + inputText + "%'";
		}
//		sql += " ORDER BY ���ʱ�� desc";
		sql += " ORDER BY ����ʱ�� desc";
		PagingGridBean<SourceBean> pagingGridBean = DBTools.getPagingData(sql,
				WebConfigUtil.getFlexGridPagingCount(), skipPage,
				new SourceRowMapper(), false);
		return pagingGridBean;
	}

	/**
	 * ͨ��Ȩ�޺��ĵ�������ĿID����ҳ���ĵ���������
	 * 
	 * @param type;
	 * @return
	 */
	public List<SourceBean> getIndexSourceContents(String columnId) {
		PagingGridBean<SourceBean> pagingGridBean = getSourceList(columnId, 1,
				null);
		return pagingGridBean.getGridData();
	}

	/**
	 * ͨ��Ȩ�޻�ȡ������Ŀ
	 * 
	 * @param type;
	 * @return
	 */
	public List<PrivBean> getIndexNewsColumns() {
		List<PrivBean> columns = new ArrayList<PrivBean>();
		try {
			if (userpriv != null) {
				List<PrivBean> list = ServiceManager.getPrivDao().getChildrenPrivRelUser(
						WebConfigUtil.getNewsTreeRoot(), userId, true);
				if (list.size() > 0) {
					List<PrivBean> pblist = userpriv.getChildrenPrivsWithAttr(list.get(
							0).getPrivId(), false);
					//System.out.println(pblist);
					for(PrivBean priv:pblist){
						if(priv.getPrivLevel()==2){
							columns.add(priv);
						}
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return columns;
	}
	/**
	 * ͨ��Ȩ�޻�ȡ������Ŀ,ͬһ���
	 * aviyy
	 */
	public List<PrivBean> getNewsListColumns(String parentId) {
		List<PrivBean> columns = new ArrayList<PrivBean>();
		try {
			if (userpriv != null) {
					List<PrivBean> pblist = userpriv.getChildrenPrivsWithAttr(parentId, false);
					//System.out.println(pblist);
					for(PrivBean priv:pblist){
						if(priv.getPrivLevel()==2){
							columns.add(priv);
						}
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return columns;
	}
	

	/**
	 * ͨ��Ȩ�޺�������ĿID����ҳ����������
	 * 
	 * @param type;
	 * @return
	 */
	public List<NewsBean> getIndexNewsContents(String columnId) {
		PagingGridBean<NewsBean> pagingGridBean = getIndexNewsContents(
				columnId, 1, null, false);
		return pagingGridBean.getGridData();
	}

	/**
	 * ͨ��Ȩ�޺�������ĿID����ǰҳ���������������ҳ����������
	 * 
	 * @param type;
	 * @return
	 */
	public PagingGridBean<NewsBean> getIndexNewsContents(String columnId,
			int skipPage, String inputText,Boolean isAll ) {
		String sql = "SELECT * FROM " + TableConst.TABLE_NEWS
				+ " WHERE ����Ȩ�� = '" + columnId + "' AND ״̬ > 0";
		if(!isAll){
			sql += " AND ʧЧʱ�� > sysdate ";
		}
//		String sql = "SELECT * FROM " + TableConst.TABLE_NEWS
//		+ " WHERE ����Ȩ�� = '" + columnId + "' AND ״̬ > 0";
		if (inputText != null) {
			sql += " AND UPPER(����) like '%" + inputText.toUpperCase() + "%'";
		}
		//aviyy	2014-3-7	������ק����	�Ż���'˳��'ȡ����
		sql += " ORDER BY ˳�� desc, ���ʱ�� desc";
		//sql += " ORDER BY ���ʱ�� desc, ˳�� asc";
		PagingGridBean<NewsBean> pagingGridBean = DBTools.getPagingData(sql,
				WebConfigUtil.getFlexGridPagingCount(), skipPage,
				new NewsRowMapper(), false);
		return pagingGridBean;
	}

	/**
	 * �õ��Ż�����select���ݣ���4��select ��ʱ��дһ��������������getListXXXLink
	 */
	public List<Map<String, String>> getIndexListLink() {
		List<Map<String, String>> listLinks = new ArrayList<Map<String, String>>();
		try {
			if (userpriv != null) {
				List<PrivBean> list = ServiceManager.getPrivDao().getChildrenPrivRelUser(
						WebConfigUtil.getLinkTreeRoot(), userId, true);
				if (list.size() > 0) {
					List<PrivBean> pblist = userpriv.getChildrenPrivsWithAttr(list.get(
							0).getPrivId(), false);
					//System.out.println(pblist);
					for (int i = 0; i < pblist.size(); i++) {
						Map<String, String> map = new HashMap<String, String>();

						map.put("linkName", pblist.get(i).getPrivName());
						map.put("linkId", pblist.get(i).getPrivId());
						listLinks.add(map);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listLinks;
	}

	/**
	 * ������������б�
	 * 
	 * @param columnId
	 * @return
	 */
	public List<LinkBean> getLinkList(String columnId) {
		String sql = "SELECT * FROM " + TableConst.TABLE_LINK + " WHERE ����Ȩ��='"
				+ columnId + "' AND ״̬ = 1 ORDER BY ˳��";
		return DBTools.getDBQueryResultList(sql, new LinkRowMapper());
	}

	/**
	 * ���IP����
	 * 
	 * @return
	 */
	public Map<String, String> getIPConfig() {
		Map<String, String> ipModel = new HashMap<String, String>();
		try {
			List<PrivBean> list = userpriv.getPrivDirectory(WebConfigUtil
					.getIPTreeRoot(), true, true);
			for (int i = 0; i < list.size(); i++) {
				List<PrivAttrBean> attrs = list.get(i).getAttrList();
				String modelPath = "";
				List<String> iplist = new ArrayList<String>();
				for (int j = 0; j < attrs.size(); j++) {
					if ("�����û�".equals(attrs.get(j).getPrivAttrName())) {
						modelPath = attrs.get(j).getPrivAttrValue();
					} else {
						iplist.add(attrs.get(j).getPrivAttrValue());
					}
				}
				for (int l = 0; l < iplist.size(); l++) {
					ipModel.put(iplist.get(l), modelPath);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ipModel;
	}

	/**
	 * �������Ӧ�ñ� ���б���ʽ��ʾ ������
	 * 
	 * @return
	 */
	public List<Map<String, String>> getAllAppList() {
		List<Map<String, String>> apps = new ArrayList<Map<String, String>>();
		List<PrivBean> list = ServiceManager.getPrivDao().getChildrenPrivRelUser(
				WebConfigUtil.getAppTreeRoot(), userId, true);
		for (int i = 0; i < list.size(); i++) {
			List<PrivBean> item = userpriv.getChildrenPrivsWithAttr(list.get(i)
					.getPrivId(), true);
			
			for (int j = 0; j < item.size(); j++) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("privName", item.get(j).getPrivName());
				map.put("privImage", item.get(j).getPrivImage());
				map.put("privId", item.get(j).getPrivId());
				for (int a = 0; a < item.get(j).getAttrList().size(); a++) {
					if ("����".equals(item.get(j).getAttrList().get(a)
							.getPrivAttrType())) {
						map.put(item.get(j).getAttrList().get(a)
								.getPrivAttrName(), item.get(j).getAttrList()
								.get(a).getPrivAttrValue());
					}
				}
				apps.add(map);
			}
		}
		return apps;
	}
	
	/**
	 * �������Ӧ�ñ� �Է���ķ�ʽչʾ
	 * 
	 * @return
	 */
	public Map<String,List<Map<String, Object>>> getAllAppListByType() {
		Map<String,List<Map<String, Object>>> m=new HashMap<String,List<Map<String, Object>>>();
		List<Map<String, Object>> apps = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> pseronalapps = new ArrayList<Map<String, Object>>();
		List<PrivBean> listatt = userpriv.getChildrenPrivsWithAttr(WebConfigUtil.getAppTreeRoot(), false);
		for(PrivBean priv:listatt){
			//��������ȫ�½ڵ���ȡ���¼��ӽڵ�
			if(priv.getPrivLevel()==1){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("privName", priv.getPrivName());
				map.put("privId",priv.getPrivId());
				map.put("children", getChildrenPrivById(listatt,priv.getPrivId()));
				apps.add(map);
			}
		}
		
		List<PrivBean> personallist=this.getAppList();
		
		for(PrivBean psersonalpriv:personallist){
			for(PrivBean priv:listatt){
				
			if(psersonalpriv.getPrivId().equals(priv.getPrivId())){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("privName", priv.getPrivName());
				map.put("privId",priv.getPrivId());
				for (int a = 0; a < priv.getAttrList().size(); a++) {
					if ("����".equals(priv.getAttrList().get(a)
							.getPrivAttrType())) {
						map.put(priv.getAttrList().get(a)
								.getPrivAttrName(), priv.getAttrList()
								.get(a).getPrivAttrValue());
					}
				}
				pseronalapps.add(map);
				break;	
			}
			
			}
				
				//map.put("children", getChildrenPrivById(listatt,priv.getPrivId()));
				
			
		}
		
		m.put("all",apps);
		m.put("psersonal",pseronalapps);
		
//		List<PrivBean> list = ServiceManager.getPrivDao().getChildrenPrivRelUser(
//				WebConfigUtil.getAppTreeRoot(), userId, true);
//		for (int i = 0; i < list.size(); i++) {
//			PrivBean item =list.get(i);
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("privName", item.getPrivName());
//			map.put("privId",item.getPrivId());
//			map.put("children", getAllAppListById(item.getPrivId()));
//			apps.add(map);
//			
//		}
		
		
		return m;
	}
	
	/**
	 * �������Ӧ�ñ� �Է���ķ�ʽչʾ(��д��ȡȨ�޷�����Ϊ�˽���Ż�ͼ��Ȩ�޻��ҡ���ȷ����)
	 * @param userId �û�ID(session) 
	 * @return
	 */
	public Map<String,List<Map<String, Object>>> getAllAppListByType(String userId) {
		Map<String,List<Map<String, Object>>> m=new HashMap<String,List<Map<String, Object>>>();
		List<Map<String, Object>> apps = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> pseronalapps = new ArrayList<Map<String, Object>>();
		List<PrivBean> listatt = userpriv.getChildrenPrivsWithAttr(WebConfigUtil.getAppTreeRoot(), false, userId);
		for(PrivBean priv:listatt){
			//��������ȫ�½ڵ���ȡ���¼��ӽڵ�
			if(priv.getPrivLevel()==1){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("privName", priv.getPrivName());
				map.put("privId",priv.getPrivId());
				map.put("children", getChildrenPrivById(listatt,priv.getPrivId()));
				apps.add(map);
			}
		}
		
		List<PrivBean> personallist=this.getAppList(userId);
		
		for(PrivBean psersonalpriv:personallist){
			for(PrivBean priv:listatt){
				
			if(psersonalpriv.getPrivId().equals(priv.getPrivId())){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("privName", priv.getPrivName());
				map.put("privId",priv.getPrivId());
				for (int a = 0; a < priv.getAttrList().size(); a++) {
					if ("����".equals(priv.getAttrList().get(a)
							.getPrivAttrType())) {
						map.put(priv.getAttrList().get(a)
								.getPrivAttrName(), priv.getAttrList()
								.get(a).getPrivAttrValue());
					}
				}
				pseronalapps.add(map);
				break;	
			}
			
			}
				
				//map.put("children", getChildrenPrivById(listatt,priv.getPrivId()));
				
			
		}
		
		m.put("all",apps);
		m.put("psersonal",pseronalapps);
		
//		List<PrivBean> list = ServiceManager.getPrivDao().getChildrenPrivRelUser(
//				WebConfigUtil.getAppTreeRoot(), userId, true);
//		for (int i = 0; i < list.size(); i++) {
//			PrivBean item =list.get(i);
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("privName", item.getPrivName());
//			map.put("privId",item.getPrivId());
//			map.put("children", getAllAppListById(item.getPrivId()));
//			apps.add(map);
//			
//		}
		
		
		return m;
	}
	
	
	public List<Map<String, String>> getChildrenPrivById(List<PrivBean> list,String id) {
		List<Map<String, String>> apps = new ArrayList<Map<String, String>>();
		for(PrivBean priv:list){
			if(priv.getPrivParentId().equals(id)){
				Map<String, String> map = new HashMap<String, String>();
				map.put("privName", priv.getPrivName());
				map.put("privImage", priv.getPrivImage());
				map.put("privId", priv.getPrivId());
				for (int a = 0; a < priv.getAttrList().size(); a++) {
					if ("����".equals(priv.getAttrList().get(a)
							.getPrivAttrType())) {
						map.put(priv.getAttrList().get(a)
								.getPrivAttrName(), priv.getAttrList()
								.get(a).getPrivAttrValue());
					}
				}
				
				apps.add(map);
			}
		}
		return apps;
		
	}
	/**
	 * ����Ӧ�����ID���һ��Ӧ������µ�����Ӧ��
	 * 
	 * @return
	 */
//	public List<Map<String, String>> getAllAppListById(String id) {
//		List<Map<String, String>> apps = new ArrayList<Map<String, String>>();
//		
//		
//			List<PrivBean> item = userpriv.getChildrenPrivsWithAttr(id, true);
//			
//			for (int j = 0; j < item.size(); j++) {
//				Map<String, String> map = new HashMap<String, String>();
//				map.put("privName", item.get(j).getPrivName());
//				map.put("privImage", item.get(j).getPrivImage());
//				map.put("privId", item.get(j).getPrivId());
//				for (int a = 0; a < item.get(j).getAttrList().size(); a++) {
//					if ("����".equals(item.get(j).getAttrList().get(a)
//							.getPrivAttrType())) {
//						map.put(item.get(j).getAttrList().get(a)
//								.getPrivAttrName(), item.get(j).getAttrList()
//								.get(a).getPrivAttrValue());
//					}
//				}
//				apps.add(map);
//			}
//		
//		return apps;
//	}
	
	/**
	 * ����û�Ӧ���б����û���ʾ����߳��ù���̨��Ӧ���б�Ĭ�ϣ����û������Ĭ����ʾ����
	 * 
	 * @return
	 */
	public List<PrivBean> getAppList() {
		List<PrivBean> list =userpriv.getAppChildrenPrivsWithAttr();
		return list;
	}
	
	/**
	 * ����û�Ӧ���б����û���ʾ����߳��ù���̨��Ӧ���б�Ĭ�ϣ����û������Ĭ����ʾ����
	 * (��д��ȡȨ�޷�����Ϊ�˽���Ż�ͼ��Ȩ�޻��ҡ���ȷ����)
	 * @param userId �û�ID(session) 
	 * @return
	 */
	public List<PrivBean> getAppList(String userId) {
		List<PrivBean> list =userpriv.getAppChildrenPrivsWithAttr(userId);
		return list;
	}

	
	/**
	 * ���Ӧ�ò˵�
	 * 
	 * @param privId
	 * @return
	 */
	public List<Map<String, String>> getAppMenuList(String privId) {
		List<Map<String, String>> menu = new ArrayList<Map<String, String>>();
		List<PrivAttrBean> attrs = userpriv.getPrivAttrs(privId);
		for (int i = 0; i < attrs.size(); i++) {
			if ("����".equals(attrs.get(i).getPrivAttrType())) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("label", attrs.get(i).getPrivAttrName());
				map.put("url", attrs.get(i).getPrivAttrValue());
				menu.add(map);
			}
		}
		return menu;
	}

	/**
	 * ��ȡרҵ��̬�ڵ������Ϣ LQ
	 * 
	 * @return
	 */
	public String getDomainTrendsInfo(String PrivId,String userId) {
		
		List<PrivBean> privlist = ServiceManager.getPrivDao().getChildrenPrivRelUser(PrivId,userId,false);
		
		StringBuffer sql_time = new StringBuffer();
		if(privlist.size()>0){
			sql_time.append("SELECT top 1 ���ʱ��  FROM "+TableConst.TABLE_SOURCE+" WHERE ����Ȩ�� IN ( ");
			for(int i=0;i<privlist.size();i++){
				sql_time.append("'"+privlist.get(i).getPrivId()+"'");
				if(i < privlist.size() - 1){
					sql_time.append(",");
				}
			}
			sql_time.append(" )");
		}else {
			sql_time.append("SELECT * FROM "+TableConst.TABLE_SOURCE+" WHERE ����Ȩ�� IN ('')");
		}
		
		sql_time.append(" AND ״̬>0 ORDER BY ���ʱ�� desc ");
		
		StringBuffer sql_count = new StringBuffer();
		if(privlist.size()>0){
			sql_count.append("SELECT  count(����ID) ����ID   FROM "+TableConst.TABLE_SOURCE+" WHERE ����Ȩ�� IN ( ");
			for(int i=0;i<privlist.size();i++){
				sql_count.append("'"+privlist.get(i).getPrivId()+"'");
				if(i < privlist.size() - 1){
					sql_count.append(",");
				}
			}
			sql_count.append(" )");
		}else {
			sql_count.append("SELECT  count(����ID) ����ID  FROM "+TableConst.TABLE_SOURCE+" WHERE ����Ȩ�� IN ('')");
		}
		
		sql_count.append(" AND ״̬>0 ORDER BY ���ʱ�� desc ");

		
		String sql_name = "select Ȩ���� from " + TableConst.TABLE_MANAGE_PRIV
				+ "where Ȩ��ID='" + PrivId + "';";

		int count = DBTemplate.getInstance().getCount(sql_count.toString());
		Map<String, Object> rs = DBTemplate.getInstance()
				.getResultMap(sql_time.toString());
		Map<String, Object> rName = DBTemplate.getInstance()
				.getResultMap(sql_name);
		String time;
		String name;

		if (rName != null) {
			name = rName.get("Ȩ����").toString();
		} else {
			name = " ";

		}

		if (rs != null) {
			time = rs.get("���ʱ��").toString().split(" ")[0];
		} else {
			time = " ";
		}
		return name + "," + count + "," + time;
	}

	/**
	 * רҵ��̬���ڵ��ѯ�ļ����ɸ��ݸ��ӽڵ�ѭ����ѯ LQ
	 * 
	 * @param columnId
	 * @param skipPage
	 * @param inputText
	 * @return
	 */
	public PagingGridBean<SourceBean> getDomainTrendList(String columnId,
			int skipPage, String inputText,String userID) {
		//List<PrivBean> privlist = ServiceManager.getPrivDao().getChildrenPrivRelUser(columnId,userID,false);
		String filtersql="SELECT  Ȩ��ID FROM " +TableConst.TABLE_MANAGE_PRIV+" start with Ȩ��ID='"+columnId+"' connect by prior Ȩ��ID=Ȩ�޸�ID";
		StringBuffer sb = new StringBuffer();
		//if(privlist.size()>0){
			sb.append("SELECT * FROM "+TableConst.TABLE_SOURCE+" WHERE ����Ȩ�� IN ( ");
//			for(int i=0;i<privlist.size();i++){
//				sb.append("'"+privlist.get(i).getPrivId()+"'");
//				if(i < privlist.size() - 1){
//					sb.append(",");
//				}
//			}
			sb.append(filtersql);
			sb.append(" )");
//		}else {
//			sb.append("SELECT * FROM "+TableConst.TABLE_SOURCE+" WHERE ����Ȩ�� IN ('')");
//		}
		if (inputText != null) {
			sb.append(" AND �ĵ����� like '%" + inputText + "%'");
		}
		sb.append(" AND ״̬=1 ORDER BY ���ʱ�� desc ");
		
		PagingGridBean<SourceBean> pagingGridBean = DBTools.getPagingData(sb.toString(),
				WebConfigUtil.getFlexGridPagingCount(), skipPage,
				new SourceRowMapper(), false);
		return pagingGridBean;
	}

	public static void main(String[] args) {
		WebConfigUtil.loadConfigParameter();
		new UserPrivContent("USER_HAERBIN_064").getAppList();
	}

	/**
	 * ȡ���Ż�רҵ��̬ LQ
	 */
	public ArrayList<Map<String, String>> getProFessionalList() {
		ArrayList<Map<String, String>> listProList = new ArrayList<Map<String, String>>();
		try {
			if (userpriv != null) {
				
				 // ȡ���ڵ�
				List<PrivBean> parentlist = userpriv.getChildrenPrivsWithAttr(
						WebConfigUtil.getProfessionTreeRoot(), false);
				
				for (int i = 0; i < parentlist.size(); i++) {
//					List<PrivBean> childrenlist = userpriv.getChildrenPrivsWithAttr(
//							parentlist.get(i).getPrivId(), true);// ȡ�ӽڵ�
					if(parentlist.get(i).getPrivParentId().equals(WebConfigUtil.getProfessionTreeRoot())){
						List<PrivBean> childrenlist = this.getChildrenPriv(parentlist, parentlist.get(i).getPrivId());// ȡ�ӽڵ�
						Map<String, String> map = new HashMap<String, String>();
						map.put("privName", parentlist.get(i).getPrivName());
						map.put("privId", parentlist.get(i).getPrivId());
						for (int k = 0; k < childrenlist.size(); k++) {
							map.put("privName" + k, childrenlist.get(k)
									.getPrivName());
							map.put("privId" + k, childrenlist.get(k).getPrivId());
						}
						listProList.add(map);
					}
					
					
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listProList;
	}
	
	


//	/**
//	 * ���רҵ��̬�����б�
//	 * 
//	 * @param columnId
//	 * @return
//	 */
//	public List<SourceBean> getProList(ArrayList<String> columnId) {
//		String sql = "SELECT * FROM " + TableConst.TABLE_SOURCE
//				+ " WHERE ����Ȩ��='" + columnId + "' AND ״̬ = 1 ORDER BY ˳��";
//		return DBTools.getDBQueryResultList(sql, new SourceRowMapper());
//	}

}
