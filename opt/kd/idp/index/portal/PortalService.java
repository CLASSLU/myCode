package kd.idp.index.portal;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kd.idp.cms.bean.portal.SourceBean;
import kd.idp.cms.bean.priv.OrgBean;
import kd.idp.cms.bean.priv.PrivBean;
import kd.idp.cms.bean.priv.UserBean;
import kd.idp.cms.client.UserPrivClient;
import kd.idp.cms.dao.UserDao;
import kd.idp.common.consts.PageConst;
import kd.idp.index.UserPrivContent;
import kd.idp.index.register.bean.UserElectricCompanyPersonBean;
import kd.idp.index.statistics.StatisticsProxy;
import kd.idp.index.util.FreeMarkerUtil;

import org.springframework.ui.ModelMap;

import com.spring.ServiceManager;
import com.spring.SpringBeanFactory;

public class PortalService {
	HashMap<Object, Object> params = null;
	public PortalService(){
		this.params = (HashMap<Object, Object>)SpringBeanFactory.getInstance().getBean("params");
	}
	/**
	 * 创建门户静态页面
	 * @throws Exception
	 */
	public void createMenhuView(UserBean userBean,UserPrivContent userPrivContent) throws Exception{
		ModelMap model = new ModelMap();
		String outputFile = "menhu.ftl";
		String webSitePath = params.get("webSite").toString();
		String location = params.get("location").toString();
		String ftlDir    = params.get("ftlPath").toString();
		String outputDir = params.get("pagePath").toString()+File.separator+"page"+File.separator+(userBean==null?location:userBean.getUserName())+File.separator;
		try {
			getMenhuModelMap(userPrivContent, model, userBean, location, webSitePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		FreeMarkerUtil.writeFileByFtl(model,outputDir,outputFile, ftlDir, "menhu.ftl", "");
	}
	/**
	 * 获得门户数据
	 * @param userPrivContent
	 * @param model
	 * @param template
	 * @param webSitePath
	 * @param location
	 * @throws Exception
	 */
	public void getMenhuModelMap(UserPrivContent userPrivContent,ModelMap model,UserBean userBean
			,String location,String webSitePath) throws Exception{
		if (model.get("userBean")==null) {
			model.addAttribute("userBean", userBean);
		}
		/* 以下是根据用户ID组织model层数据 */
		List<PrivBean> columnList = userPrivContent.getIndexNewsColumns();
		if (columnList != null && columnList.size() > 0) {
			for (PrivBean priv : columnList) {
				model.addAttribute(priv.getPrivName(),
						userPrivContent.getIndexNewsContents(priv.getPrivId()));
				model.addAttribute(priv.getPrivName() + "名称",
						priv.getPrivName());
				model.addAttribute(priv.getPrivName() + "ID",
						priv.getPrivId());
			}
		}
		model.addAttribute("LISTCOLUMNS", columnList);
		/******/
		List<Map<String, String>> linkColumn = userPrivContent.getIndexListLink();
		if (linkColumn != null && linkColumn.size() > 0) {
			for (Map<String, String> column : linkColumn) {
				for (String key : column.keySet()) {
					if (key.equals("linkId")) {
						model.addAttribute(column.get("linkName"),
								userPrivContent
										.getLinkList(column.get(key)));
						model.addAttribute(column.get("linkName") + "名称",
								column.get("linkName"));
						model.addAttribute(column.get("linkName") + "ID",
								column.get(key));
						break;
					}
				}
			}
		}
		model.addAttribute("LISTLINKS", userPrivContent.getIndexListLink());
		/* 公共参数开始 */
		model.addAttribute("WORKCONSOLE", location);
		model.addAttribute("WEBBASEPATH", webSitePath);
		model.addAttribute("BASEPATH", webSitePath+ "template/" + location + "/");
		/* 公共参数结束 */
		/* 登录统计开始 */
		OrgBean CurrentOrgBean = new UserPrivClient(ServiceManager.getUserDao().getUserFromName(location)).getCompany();// 当前地调
		model.addAttribute("COUNT", StatisticsProxy.indexStatistics(CurrentOrgBean.getOrgName(),"累计首页访问次数", new Date(), true));
	}
	/**
	 * 创建门户工作台页面
	 * @throws Exception
	 */
	public void createWorkConsoleView(UserBean userBean,UserPrivContent userPrivContent) throws Exception{
		ModelMap model = new ModelMap();
		String outputFile = "workconsole.ftl";
		String webSitePath = params.get("webSite").toString();
		String serverName = params.get("serverName").toString();
		String ftlDir    = params.get("ftlPath").toString();
		String outputDir = params.get("pagePath").toString()+File.separator+"page"+File.separator+(userBean==null?serverName:userBean.getUserName())+File.separator;
		try {
			getWorkConsoleData(userPrivContent, model, userBean, webSitePath,serverName );
		} catch (Exception e) {
			e.printStackTrace();
		}
		FreeMarkerUtil.writeFileByFtl(model,outputDir,outputFile, ftlDir, "WorkConsole.ftl", "");
	}
	/**
	 * 获得工作台数据
	 * @param userPrivContent
	 * @param model
	 * @param userBean
	 * @param webSitePath
	 * @param serverName
	 */
	public void getWorkConsoleData(UserPrivContent userPrivContent,ModelMap model,UserBean userBean,String webSitePath,String serverName){
		
		if(userBean instanceof UserElectricCompanyPersonBean){
			String photo=((UserElectricCompanyPersonBean)userBean).getAttrElectricCompanyPerson().getUserPhoto();
			if(photo!=null&&!photo.equals(""))
			model.addAttribute("photo",photo);
		}
		String template= params.get("location").toString();
		if (model.get("userBean")==null) {
			model.addAttribute("userBean", userBean);
		}
		model.addAttribute("BASEPATH", webSitePath + "template/" + template + "/");
		model.addAttribute("WEBBASEPATH", webSitePath);
		model.addAttribute("SERVERNAME", serverName);
		Map<String,List<Map<String, Object>>> appmap=userPrivContent.getAllAppListByType(userBean.getUserId());//获得所有应用
		model.addAttribute("APPS", appmap.get("psersonal"));
		model.addAttribute("ALLAPPS",appmap.get("all"));
	}
	/**
	 * 创建用户登录后展示的信息
	 * @throws Exception
	 */
	public void createUserInfoView(UserBean userBean,UserPrivContent userPrivContent) throws Exception{
		ModelMap model = new ModelMap();
		String outputFile = "console.ftl";
		String webSitePath = params.get("webSite").toString();
		String location = params.get("location").toString();
		String ftlDir    = params.get("ftlPath").toString();
		String outputDir = params.get("pagePath").toString()+File.separator+"page"+File.separator+(userBean==null?location:userBean.getUserName())+File.separator;
		try {
			getUserInfoData(userPrivContent, model, userBean, webSitePath,location );
		} catch (Exception e) {
			e.printStackTrace();
		}
		FreeMarkerUtil.writeFileByFtl(model,outputDir,outputFile, ftlDir, PageConst.CONSOLE_INDEX, "");
	}
	/**
	 * 用户登录后展示的信息
	 * @param userPrivContent
	 * @param model
	 * @param userBean
	 * @param webSitePath
	 * @param location
	 */
	public void getUserInfoData(UserPrivContent userPrivContent,ModelMap model,UserBean userBean,String webSitePath,String location){
		if (model.get("userBean")==null) {
			model.addAttribute("userBean", userBean);
		}
		List<PrivBean> columnList = userPrivContent.getIndexNewsColumns();
		//model.addAttribute("LISTCOLUMNS", columnList);
		if (columnList != null && columnList.size() > 0) {
			for (PrivBean priv : columnList) {
				model.addAttribute(priv.getPrivName(),
						userPrivContent.getIndexNewsContents(priv.getPrivId()));
				model.addAttribute(priv.getPrivName() + "名称",
						priv.getPrivName());
				model.addAttribute(priv.getPrivName() + "ID",
						priv.getPrivId());
			}
			
			
		}
		// 专业动态
		ArrayList<Map<String, String>> proColumn = userPrivContent.getProFessionalList();
		if (proColumn != null && proColumn.size() > 0) {

			model.addAttribute("专业动态", proColumn);
		}
//		List<Map<String, String>> documentColumn = userPrivContent
//				.getSourceColumns();
		Map<String,List<PrivBean>> documentColumn = userPrivContent.getSourceColumns();
		//model.addAttribute("DOCUMENTCOLUMN", documentColumn);
		Map<String,List<Map>> list=new HashMap<String,List<Map>>();
		for(String k:documentColumn.keySet()) {
			List<PrivBean> l=documentColumn.get(k);
			List<Map> c=new ArrayList<Map>();
			for(PrivBean priv:l){
				Map map=new HashMap();
				map.put("columnname", priv.getPrivName());
				List<SourceBean> data=userPrivContent.getIndexSourceContents(priv.getPrivId());
				map.put("data", data);
				map.put("columnid",priv.getPrivId());
				map.put("parentid",priv.getPrivParentId());
				c.add(map);

			}
			list.put(k, c);
		}

			
			
		
		model.addAttribute("ducuments",list);
		
		model.addAttribute("BASEPATH",webSitePath+ "template/" + location + "/");
		model.addAttribute("WEBBASEPATH", webSitePath);
	}
	/**
	 * 中心内部用户
	 * @param userId
	 * @return
	 */
	public boolean isMainUser(String userId){
		List<UserBean> userList = getUserBeanList();
		for (UserBean userBean : userList) {
			if (userId.equals(userBean.getUserId())) {
				return true;
			}
		}
		return false;
	}
	public List<UserBean> getUserBeanList(){
		List<UserBean> userList = new UserDao().getUsersFromRole("ROLE_1365475977482_4183");
		return userList;
	}
	public void refreshMenhuPages(){ 
		try { 
			if (params.get("static").equals("true")) {
				long start = System.currentTimeMillis();
				UserBean userBean = ServiceManager.getUserDao().getUserFromName("admin");
				UserPrivContent userPrivContent = new UserPrivContent();
				userPrivContent.setUserId(userBean.getUserId()); 
				createMenhuView(null, userPrivContent);
				createMenhuView(userBean, userPrivContent);
				long end = System.currentTimeMillis();
			//	System.out.println("耗时1："+(end-start)/1000+"秒");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void refreshUserInfoPages(){ 
		try { 
			if (params.get("static").equals("true")) {
				long start = System.currentTimeMillis();
				//WebConfigUtil.loadConfigParameter();
				List<UserBean> userList = getUserBeanList();
				for (UserBean userBean : userList) {
					UserPrivContent userPrivContent = new UserPrivContent();
					userPrivContent.setUserId(userBean.getUserId());
					createUserInfoView(userBean, userPrivContent);
				}
				long end = System.currentTimeMillis();
				//System.out.println("耗时2："+(end-start)/1000+"秒");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void refreshWorkConsolePages(){ 
		try { 
			if (params.get("static").equals("true")) {
				long start = System.currentTimeMillis();
				//WebConfigUtil.loadConfigParameter();
				List<UserBean> userList = getUserBeanList();
				for (UserBean userBean : userList) {
					UserPrivContent userPrivContent = new UserPrivContent();
					userPrivContent.setUserId(userBean.getUserId());
					createWorkConsoleView(userBean, userPrivContent);
				}
				long end = System.currentTimeMillis();
			//	System.out.println("耗时3："+(end-start)/1000+"秒");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		PortalService portalService   = new PortalService();
		portalService.refreshMenhuPages();
	}
}
