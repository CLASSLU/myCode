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
 * 用户权限相关 门户展示
 * 
 * @author caoyu
 * 
 */
public class UserPrivContent {

	/**
	 * 用户ID
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
	 * 获得用户信息
	 * 
	 * @return
	 */
	public UserBean getUser() {
		String sql = "SELECT * FROM " + TableConst.VIEW_MANAGE_USER
				+ " where 用户ID='" + userId + "'";
		return DBTemplate.getInstance().getResultRowMapper(sql, new UserRowMapper());
	}

	/**
	 * 是否是管理员
	 * @return
	 */
	public boolean hasAdminRole(){
		return userpriv.hasAdminRole();
	}
	/**
	 * 获得模板路径
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
					model = attr.get("模板路径").getPrivAttrValue();
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
	 * 获得公共用户
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
					publicuser = attr.get("公共用户").getPrivAttrValue();
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
	 * 通过权限获得文档资料专业动态栏目 LQ
	 * 
	 * @param model、columnId、skipPage、inputText
	 * @return
	 */
	public List<Map<String, String>> getProfessionColumns() {
		List<Map<String, String>> columns = new ArrayList<Map<String, String>>();
		try {
			if (userpriv != null) {
				 // 取得父节点
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
	 * 个人工作台更多 LQ
	 * 
	 * @param model、columnId、skipPage、inputText
	 * @return
	 */
	public List<Map<String, Object>> getPersonalStation(String _userId) {
		List<Map<String, Object>> columns = new ArrayList<Map<String, Object>>(); // 返回的结果
		try {
			if (userpriv != null) {
				 // 取得父节点
				List<PrivBean> list2 = ServiceManager.getPrivDao().getChildrenPrivRelUser(
						WebConfigUtil.getAppTreeRoot(), _userId, true);
				UserPrivClient upriv = new UserPrivClient(_userId);
				for (int j = 0; j < list2.size(); j++) {
					List<PrivBean> pblist = upriv.getChildrenPrivsWithAttr(list2
							.get(j).getPrivId(), true); // 取得子节点
					Map<String, Object> map1 = new HashMap<String, Object>();
					
					map1.put("GroudName", list2.get(j).getPrivName());
					List<Map<String, String>> row = new ArrayList<Map<String, String>>(); // list装载各应用

					for (int i = 0; i < pblist.size(); i++) {

						Map<String, String> map2 = new HashMap<String, String>();
						map2.put("AppId", pblist.get(i).getPrivId());
						String appname = pblist.get(i).getPrivName();

						map2.put("AppName", appname);
						for (int a = 0; a < pblist.get(i).getAttrList().size(); a++) {
							if ("属性".equals(pblist.get(i).getAttrList().get(a)
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
	 * @param model、columnId、skipPage、inputText
	 * @return
	 */
	public List<Map<String, Object>> getPersonalVisualStation(String _userId) {
		List<Map<String, Object>> columns = new ArrayList<Map<String, Object>>(); // 返回的结果
		
		try {
			if (userpriv != null) {
				 // 取得父节点
				UserPrivClient upriv = new UserPrivClient(_userId);
				List<PrivBean> list2 = upriv.getPrivDirectory(WebConfigUtil.getAppTreeRoot(), true, false);
				
				for (int j = 0; j < list2.size(); j++) {
					List<PrivBean> pblist = upriv.getVisualPrivDirectory(list2
							.get(j).getPrivId(),true, true);
					List<PrivBean> pblist2 = upriv.getChildrenPrivsWithAttr(list2
							.get(j).getPrivId(), false); // 取得子节点
					Map<String, Object> map1 = new HashMap<String, Object>();
					
					map1.put("GroudName", list2.get(j).getPrivName());
					List<Map<String, String>> row = new ArrayList<Map<String, String>>(); // list装载各应用

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
							if ("属性".equals(pblist.get(i).getAttrList().get(a)
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
	 * 通过权限获得文档资料栏目
	 * 
	 * @param model、columnId、skipPage、inputText
	 * @return
	 */
	public Map<String,List<PrivBean>> getSourceColumns() {
		Map<String,List<PrivBean>> all=new HashMap<String,List<PrivBean>>();
		
		try {
			if (userpriv != null) {
				List<PrivBean> attlist=userpriv.getChildrenPrivsWithAttr(WebConfigUtil.getSourceTreeRoot(), false);
				//文档资料下的直接子节点
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
	 * 从权限列表中获取权限所有子权限
	 * 
	 * @param model、columnId、skipPage、inputText
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
	 * 通过权限和栏目ID获得文档资料内容
	 * 
	 * @param columnId
	 * @param skipPage
	 * @param inputText
	 * @return
	 */
	public PagingGridBean<SourceBean> getSourceList(String columnId,
			int skipPage, String inputText) {
//		String sql = "SELECT * FROM " + TableConst.TABLE_SOURCE
//				+ " WHERE 关联权限 = '" + columnId + "' AND 状态 > 0 ";
		String filtersql="SELECT  权限ID FROM " +TableConst.TABLE_MANAGE_PRIV+" start with 权限ID='"+columnId+"' connect by prior 权限ID=权限父ID";
		String sql = "SELECT * FROM " + TableConst.TABLE_SOURCE
		+ " WHERE 关联权限 in (" + filtersql + ") AND 状态 > 0 ";
		if (inputText != null) {
			sql += " AND 文档名称 like '%" + inputText + "%'";
		}
//		sql += " ORDER BY 审核时间 desc";
		sql += " ORDER BY 创建时间 desc";
		PagingGridBean<SourceBean> pagingGridBean = DBTools.getPagingData(sql,
				WebConfigUtil.getFlexGridPagingCount(), skipPage,
				new SourceRowMapper(), false);
		return pagingGridBean;
	}

	/**
	 * 通过权限和文档资料栏目ID获首页的文档资料内容
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
	 * 通过权限获取新闻栏目
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
	 * 通过权限获取新闻栏目,同一类别
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
	 * 通过权限和新闻栏目ID获首页的新闻内容
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
	 * 通过权限和新闻栏目ID、当前页和搜索条件获更多页的新闻内容
	 * 
	 * @param type;
	 * @return
	 */
	public PagingGridBean<NewsBean> getIndexNewsContents(String columnId,
			int skipPage, String inputText,Boolean isAll ) {
		String sql = "SELECT * FROM " + TableConst.TABLE_NEWS
				+ " WHERE 关联权限 = '" + columnId + "' AND 状态 > 0";
		if(!isAll){
			sql += " AND 失效时间 > sysdate ";
		}
//		String sql = "SELECT * FROM " + TableConst.TABLE_NEWS
//		+ " WHERE 关联权限 = '" + columnId + "' AND 状态 > 0";
		if (inputText != null) {
			sql += " AND UPPER(标题) like '%" + inputText.toUpperCase() + "%'";
		}
		//aviyy	2014-3-7	新闻拖拽排序	门户按'顺序'取新闻
		sql += " ORDER BY 顺序 desc, 审核时间 desc";
		//sql += " ORDER BY 审核时间 desc, 顺序 asc";
		PagingGridBean<NewsBean> pagingGridBean = DBTools.getPagingData(sql,
				WebConfigUtil.getFlexGridPagingCount(), skipPage,
				new NewsRowMapper(), false);
		return pagingGridBean;
	}

	/**
	 * 得到门户链接select数据，有4个select 暂时先写一个，如果多个改名getListXXXLink
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
	 * 获得链接内容列表
	 * 
	 * @param columnId
	 * @return
	 */
	public List<LinkBean> getLinkList(String columnId) {
		String sql = "SELECT * FROM " + TableConst.TABLE_LINK + " WHERE 关联权限='"
				+ columnId + "' AND 状态 = 1 ORDER BY 顺序";
		return DBTools.getDBQueryResultList(sql, new LinkRowMapper());
	}

	/**
	 * 获得IP配置
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
					if ("公共用户".equals(attrs.get(j).getPrivAttrName())) {
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
	 * 获得所有应用表 以列表形式显示 不分类
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
					if ("属性".equals(item.get(j).getAttrList().get(a)
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
	 * 获得所有应用表 以分类的方式展示
	 * 
	 * @return
	 */
	public Map<String,List<Map<String, Object>>> getAllAppListByType() {
		Map<String,List<Map<String, Object>>> m=new HashMap<String,List<Map<String, Object>>>();
		List<Map<String, Object>> apps = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> pseronalapps = new ArrayList<Map<String, Object>>();
		List<PrivBean> listatt = userpriv.getChildrenPrivsWithAttr(WebConfigUtil.getAppTreeRoot(), false);
		for(PrivBean priv:listatt){
			//如果是类别全下节点则取出下级子节点
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
					if ("属性".equals(priv.getAttrList().get(a)
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
	 * 获得所有应用表 以分类的方式展示(重写获取权限方法，为了解决门户图标权限混乱【待确定】)
	 * @param userId 用户ID(session) 
	 * @return
	 */
	public Map<String,List<Map<String, Object>>> getAllAppListByType(String userId) {
		Map<String,List<Map<String, Object>>> m=new HashMap<String,List<Map<String, Object>>>();
		List<Map<String, Object>> apps = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> pseronalapps = new ArrayList<Map<String, Object>>();
		List<PrivBean> listatt = userpriv.getChildrenPrivsWithAttr(WebConfigUtil.getAppTreeRoot(), false, userId);
		for(PrivBean priv:listatt){
			//如果是类别全下节点则取出下级子节点
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
					if ("属性".equals(priv.getAttrList().get(a)
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
					if ("属性".equals(priv.getAttrList().get(a)
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
	 * 根据应用类别ID获得一个应用类别下的所有应用
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
//					if ("属性".equals(item.get(j).getAttrList().get(a)
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
	 * 获得用户应用列表中用户显示在左边常用工作台的应用列表。默认，如果没有配置默认显示所有
	 * 
	 * @return
	 */
	public List<PrivBean> getAppList() {
		List<PrivBean> list =userpriv.getAppChildrenPrivsWithAttr();
		return list;
	}
	
	/**
	 * 获得用户应用列表中用户显示在左边常用工作台的应用列表。默认，如果没有配置默认显示所有
	 * (重写获取权限方法，为了解决门户图标权限混乱【待确定】)
	 * @param userId 用户ID(session) 
	 * @return
	 */
	public List<PrivBean> getAppList(String userId) {
		List<PrivBean> list =userpriv.getAppChildrenPrivsWithAttr(userId);
		return list;
	}

	
	/**
	 * 获得应用菜单
	 * 
	 * @param privId
	 * @return
	 */
	public List<Map<String, String>> getAppMenuList(String privId) {
		List<Map<String, String>> menu = new ArrayList<Map<String, String>>();
		List<PrivAttrBean> attrs = userpriv.getPrivAttrs(privId);
		for (int i = 0; i < attrs.size(); i++) {
			if ("链接".equals(attrs.get(i).getPrivAttrType())) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("label", attrs.get(i).getPrivAttrName());
				map.put("url", attrs.get(i).getPrivAttrValue());
				menu.add(map);
			}
		}
		return menu;
	}

	/**
	 * 获取专业动态节点基本信息 LQ
	 * 
	 * @return
	 */
	public String getDomainTrendsInfo(String PrivId,String userId) {
		
		List<PrivBean> privlist = ServiceManager.getPrivDao().getChildrenPrivRelUser(PrivId,userId,false);
		
		StringBuffer sql_time = new StringBuffer();
		if(privlist.size()>0){
			sql_time.append("SELECT top 1 审核时间  FROM "+TableConst.TABLE_SOURCE+" WHERE 关联权限 IN ( ");
			for(int i=0;i<privlist.size();i++){
				sql_time.append("'"+privlist.get(i).getPrivId()+"'");
				if(i < privlist.size() - 1){
					sql_time.append(",");
				}
			}
			sql_time.append(" )");
		}else {
			sql_time.append("SELECT * FROM "+TableConst.TABLE_SOURCE+" WHERE 关联权限 IN ('')");
		}
		
		sql_time.append(" AND 状态>0 ORDER BY 审核时间 desc ");
		
		StringBuffer sql_count = new StringBuffer();
		if(privlist.size()>0){
			sql_count.append("SELECT  count(内容ID) 内容ID   FROM "+TableConst.TABLE_SOURCE+" WHERE 关联权限 IN ( ");
			for(int i=0;i<privlist.size();i++){
				sql_count.append("'"+privlist.get(i).getPrivId()+"'");
				if(i < privlist.size() - 1){
					sql_count.append(",");
				}
			}
			sql_count.append(" )");
		}else {
			sql_count.append("SELECT  count(内容ID) 内容ID  FROM "+TableConst.TABLE_SOURCE+" WHERE 关联权限 IN ('')");
		}
		
		sql_count.append(" AND 状态>0 ORDER BY 审核时间 desc ");

		
		String sql_name = "select 权限名 from " + TableConst.TABLE_MANAGE_PRIV
				+ "where 权限ID='" + PrivId + "';";

		int count = DBTemplate.getInstance().getCount(sql_count.toString());
		Map<String, Object> rs = DBTemplate.getInstance()
				.getResultMap(sql_time.toString());
		Map<String, Object> rName = DBTemplate.getInstance()
				.getResultMap(sql_name);
		String time;
		String name;

		if (rName != null) {
			name = rName.get("权限名").toString();
		} else {
			name = " ";

		}

		if (rs != null) {
			time = rs.get("审核时间").toString().split(" ")[0];
		} else {
			time = " ";
		}
		return name + "," + count + "," + time;
	}

	/**
	 * 专业动态树节点查询文件，可根据父子节点循环查询 LQ
	 * 
	 * @param columnId
	 * @param skipPage
	 * @param inputText
	 * @return
	 */
	public PagingGridBean<SourceBean> getDomainTrendList(String columnId,
			int skipPage, String inputText,String userID) {
		//List<PrivBean> privlist = ServiceManager.getPrivDao().getChildrenPrivRelUser(columnId,userID,false);
		String filtersql="SELECT  权限ID FROM " +TableConst.TABLE_MANAGE_PRIV+" start with 权限ID='"+columnId+"' connect by prior 权限ID=权限父ID";
		StringBuffer sb = new StringBuffer();
		//if(privlist.size()>0){
			sb.append("SELECT * FROM "+TableConst.TABLE_SOURCE+" WHERE 关联权限 IN ( ");
//			for(int i=0;i<privlist.size();i++){
//				sb.append("'"+privlist.get(i).getPrivId()+"'");
//				if(i < privlist.size() - 1){
//					sb.append(",");
//				}
//			}
			sb.append(filtersql);
			sb.append(" )");
//		}else {
//			sb.append("SELECT * FROM "+TableConst.TABLE_SOURCE+" WHERE 关联权限 IN ('')");
//		}
		if (inputText != null) {
			sb.append(" AND 文档名称 like '%" + inputText + "%'");
		}
		sb.append(" AND 状态=1 ORDER BY 审核时间 desc ");
		
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
	 * 取得门户专业动态 LQ
	 */
	public ArrayList<Map<String, String>> getProFessionalList() {
		ArrayList<Map<String, String>> listProList = new ArrayList<Map<String, String>>();
		try {
			if (userpriv != null) {
				
				 // 取父节点
				List<PrivBean> parentlist = userpriv.getChildrenPrivsWithAttr(
						WebConfigUtil.getProfessionTreeRoot(), false);
				
				for (int i = 0; i < parentlist.size(); i++) {
//					List<PrivBean> childrenlist = userpriv.getChildrenPrivsWithAttr(
//							parentlist.get(i).getPrivId(), true);// 取子节点
					if(parentlist.get(i).getPrivParentId().equals(WebConfigUtil.getProfessionTreeRoot())){
						List<PrivBean> childrenlist = this.getChildrenPriv(parentlist, parentlist.get(i).getPrivId());// 取子节点
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
//	 * 获得专业动态内容列表
//	 * 
//	 * @param columnId
//	 * @return
//	 */
//	public List<SourceBean> getProList(ArrayList<String> columnId) {
//		String sql = "SELECT * FROM " + TableConst.TABLE_SOURCE
//				+ " WHERE 关联权限='" + columnId + "' AND 状态 = 1 ORDER BY 顺序";
//		return DBTools.getDBQueryResultList(sql, new SourceRowMapper());
//	}

}
