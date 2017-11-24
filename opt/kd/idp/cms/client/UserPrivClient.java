package kd.idp.cms.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.spring.ServiceManager;
import com.spring.dbservice.DBTemplate;

import kd.idp.cms.bean.priv.OrgBean;
import kd.idp.cms.bean.priv.PrivAttrBean;
import kd.idp.cms.bean.priv.PrivBean;
import kd.idp.cms.bean.priv.RoleBean;
import kd.idp.cms.bean.priv.UserBean;
import kd.idp.cms.mapper.priv.OrgRowMapper;
import kd.idp.cms.mapper.priv.PrivAttrRowMapper;
import kd.idp.cms.mapper.priv.PrivRowMapper;
import kd.idp.common.config.WebConfigUtil;
import kd.idp.common.consts.TableConst;
import kd.idp.security.password.BeanMd5;

public class UserPrivClient {

	
	private String userId = null;
	
	private UserBean user = null;
	
	public UserPrivClient(String _userId) {
		userId = _userId;
	}
	
	public UserPrivClient(UserBean _user){
		user = _user;
	}
	
	private boolean initUser(){
		user = ServiceManager.getUserDao().getUserFromID(userId);
		if(user != null){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 是否是管理员
	 * @return
	 */
	public boolean hasAdminRole(){
		List<RoleBean> roleList = ServiceManager.getRoleDao().getRolesFromUser(userId);
		for (int i=0;i<roleList.size();i++){
			if(roleList.get(i).getRoleName().indexOf("管理员") > -1){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 修改用户密码
	 * @param newPwd
	 * @return
	 */
	public int updateUserPwd(String newPwd){
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE ");
		sb.append(TableConst.TABLE_MANAGE_USER);
		sb.append(" SET 密码 = '");
		sb.append(BeanMd5.MD5HexEncode(newPwd));
		sb.append("' WHERE 用户ID = '");
		sb.append(userId);
		sb.append("'");
		return DBTemplate.getInstance().updateSql(sb.toString());
	}
	
	/**
	 * 获得用户权限属性
	 * @param privId
	 * @return
	 */
	public List<PrivAttrBean> getPrivAttrs(String privId){
		return ServiceManager.getPrivDao().getUserPrivAttr(userId, privId);
	}
	
	/**
	 * 获得用户权限属性
	 * @param privId
	 * @return
	 */
	public Map<String, PrivAttrBean> getPrivAttrMap(String privId){
		List<PrivAttrBean> attrs = ServiceManager.getPrivDao().getUserPrivAttr(userId, privId);
		Map<String, PrivAttrBean> map = new HashMap<String, PrivAttrBean>();
		for(int i = 0;i<attrs.size();i++){
			map.put(attrs.get(i).getPrivAttrName(), attrs.get(i));
		}
		return map;
	}
	
	
	/**
	 * 获得用户权限
	 * @param privId
	 * @param hasAttrs 是否包含属性
	 * @return
	 */
	public PrivBean getPriv(String privId,boolean hasAttrs){
		return new PrivBean();
	}
	
	
	/**
	 * 获得用户权限包括属性
	 * @param privId
	 * @param isDirect true 直接子节点,false 递归子节点
	 * @return
	 */
	public List<PrivBean> getChildrenPrivsWithAttr(String privId,boolean isDirect){
		List<PrivBean> pblist = ServiceManager.getPrivDao()
				.getChildrenPrivRelUser(privId, userId, isDirect);
		List<PrivAttrBean> attrList = ServiceManager.getPrivDao()
				.getUserChildrenPrivAttr(userId, privId, isDirect);
		if (pblist.size() > 0) {
			for (int i = 0; i < pblist.size(); i++) {
				for (int j = 0; j < attrList.size(); j++) {
					if (pblist.get(i).getPrivId().equals(
							attrList.get(j).getPrivId())) {
						pblist.get(i).getAttrList().add(attrList.get(j));
					}
				}
			}
		}
		return pblist;
	}
	
	/**
	 * 获得用户权限包括属性(重写获取权限方法，为了解决门户图标权限混乱【待确定】)
	 * @param privId
	 * @param isDirect true 直接子节点,false 递归子节点
	 * @param userId 用户ID(session)
	 * @return
	 */
	public List<PrivBean> getChildrenPrivsWithAttr(String privId,boolean isDirect,String userId){
		List<PrivBean> pblist = ServiceManager.getPrivDao()
				.getChildrenPrivRelUser(privId, userId, isDirect);
		List<PrivAttrBean> attrList = ServiceManager.getPrivDao()
				.getUserChildrenPrivAttr(userId, privId, isDirect);
		if (pblist.size() > 0) {
			for (int i = 0; i < pblist.size(); i++) {
				for (int j = 0; j < attrList.size(); j++) {
					if (pblist.get(i).getPrivId().equals(
							attrList.get(j).getPrivId())) {
						pblist.get(i).getAttrList().add(attrList.get(j));
					}
				}
			}
		}
		return pblist;
	}
	

	
	
	/**
	 * 获得用户应用类权限包括属性
	 * @param privId
	 * @param IsPersonSet true 个人配置,false 非个人配置
	 * @return
	 */
	public List<PrivBean> getAppChildrenPrivsWithAttr(){
		List<PrivBean> pblist = null;
		
			
			pblist=ServiceManager.getPrivDao().getAppChildrenPrivRelUser(userId);//进行个性化配置过滤
		
		return pblist;
	}
	
	/**
	 * 获得用户应用类权限包括属性(重写获取权限方法，为了解决门户图标权限混乱【待确定】)
	 * @param userId 用户ID(session) 
	 * @return
	 */
	public List<PrivBean> getAppChildrenPrivsWithAttr(String userId){
		List<PrivBean> pblist = null;
		
			
			pblist=ServiceManager.getPrivDao().getAppChildrenPrivRelUser(userId);//进行个性化配置过滤
		
		return pblist;
	}
	
	
	
	/**
	 * 获得权限目录
	 * @param privId
	 * @param isDirect true 直接子节点,false 递归子节点
	 * @param hasAttrs 是否包含属性
	 * @return
	 */
	public List<PrivBean> getPrivDirectory(String privId,boolean isDirect,boolean hasAttrs){
		String sql = "select * from " + TableConst.TABLE_MANAGE_PRIV
				+ " start with 权限ID = '" + privId
				+ "' connect by 权限父ID = prior 权限ID";
		if (isDirect) {
			sql = "select * from " + TableConst.TABLE_MANAGE_PRIV
					+ " where 权限父ID = '" + privId + "' ";
			if(privId.equals(WebConfigUtil.getAppTreeRoot())){
				sql = "select * from " + TableConst.TABLE_MANAGE_PRIV
				+ " where 权限父ID = '" + privId + "' order by 权限顺序 ";
			}
		}
		List<PrivBean> privList = DBTemplate.getInstance()
				.getResultRowMapperList(sql, new PrivRowMapper());
		if (hasAttrs) {
			String attrSql = "select b.属性ID,b.权限ID,属性类型,属性名,属性值,b.顺序 from ( select * from "
					+ TableConst.TABLE_MANAGE_PRIV
					+ " start with 权限ID = '"
					+ WebConfigUtil.getIPTreeRoot()
					+ "' connect by 权限父ID = prior 权限ID ) a,"
					+ TableConst.TABLE_MANAGE_PRIV_ATTR
					+ " b where a.权限ID = b.权限ID order by b.顺序 asc";
			List<PrivAttrBean> attrList = DBTemplate.getInstance()
					.getResultRowMapperList(attrSql, new PrivAttrRowMapper());
			if (privList.size() > 0) {
				for (int i = 0; i < privList.size(); i++) {
					for (int j = 0; j < attrList.size(); j++) {
						if (privList.get(i).getPrivId().equals(
								attrList.get(j).getPrivId())) {
							privList.get(i).getAttrList().add(attrList.get(j));
						}
					}
				}
			}
		}
		return privList;
	}
	
	/**
	 * all privileges as tree
	 * aviyy
	 * 2014-01-24
	 * @param privId
	 * @param isDirect true: only the first child nodes,false: all the child nodes...
	 * @param hasAttrs with attribution
	 * @return
	 */
	public List<PrivBean> getVisualPrivDirectory(String privId,boolean isDirect,boolean hasAttrs){
		String sql = "select * from " + TableConst.TABLE_MANAGE_PRIV
				+ " start with 权限ID = '" + privId
				+ "' connect by 权限父ID = prior 权限ID";
		if (isDirect) {
			sql = "select * from " + TableConst.TABLE_MANAGE_PRIV
					+ " where 权限父ID = '" + privId + "' ";
		}
		List<PrivBean> privList = DBTemplate.getInstance()
				.getResultRowMapperList(sql, new PrivRowMapper());
		if (hasAttrs) {
			String attrSql = "select b.属性ID,b.权限ID,属性类型,属性名,属性值,b.顺序 from ( select * from "
					+ TableConst.TABLE_MANAGE_PRIV
					+ " start with 权限ID = '"
//					+ WebConfigUtil.getIPTreeRoot()
					+ privId
					+ "' connect by 权限父ID = prior 权限ID ) a,"
					+ TableConst.TABLE_MANAGE_PRIV_ATTR
					+ " b where a.权限ID = b.权限ID order by b.顺序 asc";
			List<PrivAttrBean> attrList = DBTemplate.getInstance()
					.getResultRowMapperList(attrSql, new PrivAttrRowMapper());
			if (privList.size() > 0) {
				for (int i = 0; i < privList.size(); i++) {
					for (int j = 0; j < attrList.size(); j++) {
						if (privList.get(i).getPrivId().equals(
								attrList.get(j).getPrivId())) {
							privList.get(i).getAttrList().add(attrList.get(j));
						}
					}
				}
			}
		}
		return privList;
	}
	
	/**
	 * 获得用户的单位
	 * @return
	 */
	public OrgBean getCompany(){
		if (user == null) {
			initUser();
		}
		String sql = "select * from " + TableConst.TABLE_MANAGE_ORG
				+ " start with 组织机构ID = '" + user.getUserOrgId()
				+ "' connect by 组织机构ID = prior 上级组织机构ID ";
		List<OrgBean> orgList = DBTemplate.getInstance()
				.getResultRowMapperList(sql, new OrgRowMapper());
		for (int i = 0; i < orgList.size(); i++) {
			if ("单位".equals(orgList.get(i).getType())) {
				return orgList.get(i);
			}
		}
		return orgList.get((orgList.size() > 0)?(orgList.size()-1):orgList.size());
	}
	
}
