package kd.idp.cms.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import kd.idp.cms.bean.priv.OrgBean;
import kd.idp.cms.bean.priv.PrivAttrBean;
import kd.idp.cms.bean.priv.PrivBean;
import kd.idp.cms.bean.priv.RoleBean;
import kd.idp.cms.bean.priv.UserBean;
import kd.idp.cms.mapper.priv.OrgRowMapper;
import kd.idp.cms.mapper.priv.PrivAttrRowMapper;
import kd.idp.cms.mapper.priv.PrivRowMapper;
import kd.idp.common.CommonTools;
import kd.idp.common.config.WebConfigUtil;
import kd.idp.common.consts.TableConst;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spring.dbservice.DBTemplate;



@Transactional
public class PrivDao{

	
	
	/**
	 * 查询权限信息
	 * @param privId
	 * @return
	 */
	public PrivBean getPrivInfo(String privId){
		String sql = "SELECT * FROM "+TableConst.TABLE_MANAGE_PRIV+" WHERE 权限ID = '"+privId+"'";
		PrivBean pb = DBTemplate.getInstance().getResultRowMapper(sql, new PrivRowMapper());
		pb.setAttrList(getAttrsFromPriv(privId));
		return pb;
	}
	
	/**
	 * 通过权限名查询权限信息 (不带有属性)
	 * @param privName
	 * @return
	 */
	public List<PrivBean> getPrivInfosWithoutAttr(String privName){
		String sql = "SELECT * FROM "+TableConst.TABLE_MANAGE_PRIV+" WHERE 权限名 = '"+privName+"'";
		return DBTemplate.getInstance().getResultRowMapperList(sql, new PrivRowMapper());
	}
	
	/**
	 * 查询子权限信息
	 * @param nodeId
	 * @param isDirect  true:直接子权限,false 递归所有子权限
	 * @param privOrgNameList 用户权限关联的子节点ID（直接子节点时起作用）
	 * @return
	 */
	public List<PrivBean> getChildrenPrivInfo(String nodeId,boolean isDirect,List<String> privOrgNameList){
		StringBuffer sbSql = new StringBuffer();
		if (isDirect) {
			sbSql.append("SELECT * FROM ");
			sbSql.append(TableConst.TABLE_MANAGE_PRIV);
			sbSql.append(" WHERE 权限父ID='");
			sbSql.append(nodeId);
			sbSql.append("'");
			if (privOrgNameList != null && privOrgNameList.size() > 0) {
				sbSql.append(" AND 权限名 in ( ");
				for (int i = 0; i < privOrgNameList.size(); i++) {
					sbSql.append("'" + privOrgNameList.get(i) + "'");
					if (i < privOrgNameList.size() - 1) {
						sbSql.append(",");
					}
				}
				sbSql.append(" )");
			}
			if (nodeId.equals(WebConfigUtil.getPrivTreeRootId())) {
				sbSql.append(" AND 权限ID != 'org_root' ");
			}
			sbSql.append("ORDER BY 权限顺序");
		} else {
			sbSql.append("SELECT * FROM ( SELECT * FROM ");
			sbSql.append(TableConst.TABLE_MANAGE_PRIV);
			sbSql.append(" ORDER BY 权限顺序 ) start with 权限ID = '");
			sbSql.append(nodeId);
			sbSql.append("' connect by 权限父ID = prior 权限ID");
		}
		return getPrivList(sbSql.toString());
	}
	
	/**
	 * 查询权限的父权限信息 
	 * @param nodeId
	 * @param isDirect true:直接父权限,false 递归所有父权限
	 * @return
	 */
	public List<PrivBean> getParentPrivInfo(String nodeId,boolean isDirect){
		String sql = "";
		if (isDirect) {
			sql = "SELECT b.*  FROM " + TableConst.TABLE_MANAGE_PRIV + " a, "
					+ TableConst.TABLE_MANAGE_PRIV + " b where a.权限ID = '"
					+ nodeId + "' and a.权限父ID=b.权限ID";
		} else {
			sql = "SELECT * FROM " + TableConst.TABLE_MANAGE_PRIV
					+ " start with 权限ID = '" + nodeId
					+ "' connect by 权限ID = prior 权限父ID";
		}
		return getPrivList(sql);
	}
	
	/**
	 * 查询权限属性信息
	 * @param privId
	 * @return
	 */
	public List<PrivAttrBean> getAttrsFromPriv(String privId){
		String sql = "SELECT * FROM "+TableConst.TABLE_MANAGE_PRIV_ATTR+" WHERE 权限ID = '"+privId+"' ORDER BY 顺序 asc";
		return getPrivAttrList(sql);
	}
	
	
	/**
	 * 新增权限
	 * @param priv
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public String addNewPriv(PrivBean priv){
		String privId = CommonTools.createId("PRIV");
		String sql = "INSERT INTO " + TableConst.TABLE_MANAGE_PRIV
				+ " (权限组,权限ID,权限名,类型,权限父ID,权限图片,权限层次,权限顺序) VALUES('"
				+ priv.getPrivGroup() + "','" + privId + "','"
				+ priv.getPrivName() + "','" + priv.getPrivType() + "','"
				+ priv.getPrivParentId() + "','" + priv.getPrivImage() + "',"
				+ priv.getPrivLevel() + "," + priv.getPrivOrder() + ") ";
		DBTemplate.getInstance().updateSql(sql);
		// 删除父节点的属性相关信息
		sql = "DELETE FROM " + TableConst.TABLE_REL_ROLE_PRIV
				+ " WHERE 权限ID = '" + priv.getPrivParentId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		sql = "DELETE FROM " + TableConst.TABLE_REL_USER_PRIV
				+ " WHERE 权限ID = '" + priv.getPrivParentId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		sql = "DELETE FROM " + TableConst.TABLE_MANAGE_PRIV_ATTR
				+ " WHERE 权限ID = '" + priv.getPrivParentId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		return privId;
	}
	
	/**
	 * 修改权限
	 * @param priv
	 * @return
	 */
	public String modPriv(PrivBean priv){
		try {
			String sql = "UPDATE " + TableConst.TABLE_MANAGE_PRIV
					+ " SET 权限名='" + priv.getPrivName() + "' , 类型='"
					+ priv.getPrivType() +"',权限层次='"+priv.getPrivLevel()+ "',权限顺序 = '" + priv.getPrivOrder()+"',权限父ID='"+priv.getPrivParentId()+"'"
					+ " WHERE 权限ID='" + priv.getPrivId() + "'";
			DBTemplate.getInstance().updateSql(sql);
			return priv.getPrivId();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 删除权限
	 * @param priv
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public String deletePriv(PrivBean priv){
		String sql = "DELETE FROM " + TableConst.TABLE_REL_ROLE_PRIV
				+ " WHERE 权限ID='" + priv.getPrivId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		sql = "DELETE FROM " + TableConst.TABLE_REL_USER_PRIV + " WHERE 权限ID='"
				+ priv.getPrivId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		sql = "DELETE FROM " + TableConst.TABLE_MANAGE_PRIV_ATTR
				+ " WHERE 权限ID='" + priv.getPrivId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		sql = "DELETE FROM " + TableConst.TABLE_MANAGE_PRIV + " WHERE 权限ID='"
				+ priv.getPrivId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		return priv.getPrivId();
	}
	
	/**
	 * 添加权限属性
	 * @param attr
	 * @return
	 */
	public String addNewPrivAttr(PrivAttrBean attr){
		try {
			String attrId = CommonTools.createId("ATTR");
			String sql = "INSERT INTO " + TableConst.TABLE_MANAGE_PRIV_ATTR
					+ " (属性ID,权限ID,属性类型,属性名,属性值,顺序) VALUES ('"+attrId+"','" + attr.getPrivId()
					+ "','" + attr.getPrivAttrType() + "','"
					+ attr.getPrivAttrName() + "','" + attr.getPrivAttrValue()
					+ "',"+attr.getAttrOrder()+")";
			DBTemplate.getInstance().updateSql(sql);
			return attr.getPrivAttrName();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * 修改权限属性
	 * @param attr
	 * @return
	 */
	public String modPrivAttr(PrivAttrBean attr){
		try {
			String sql = "UPDATE " + TableConst.TABLE_MANAGE_PRIV_ATTR
					+ " SET 权限ID='" + attr.getPrivId() + "',属性类型='"
					+ attr.getPrivAttrType() + "',属性名='"
					+ attr.getPrivAttrName() + "',属性值='"
					+ attr.getPrivAttrValue() + "',顺序="+attr.getAttrOrder()+" WHERE 属性ID='"
					+ attr.getPrivAttrId() + "'";
			DBTemplate.getInstance().updateSql(sql);
			return attr.getPrivAttrName();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 删除 权限属性
	 * @param attr
	 * @return
	 */
	public String deletePrivAttr(PrivAttrBean attr){
		try {
			String sql = "DELETE FROM " + TableConst.TABLE_MANAGE_PRIV_ATTR
					+ " WHERE 属性ID='"+attr.getPrivAttrId()+"'";
			DBTemplate.getInstance().updateSql(sql);
			return attr.getPrivAttrName();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 删除多个权限属性
	 * @param privId
	 * @param attrName
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public int deletePrivAttr(List<String> attrId){
		String sql = "DELETE FROM " + TableConst.TABLE_MANAGE_PRIV_ATTR
				+ " WHERE 属性ID = ? ";
		List<List<String>> dataList = new ArrayList<List<String>>();
		for (int i = 0; i < attrId.size(); i++) {
			List<String> item = new ArrayList<String>();
			item.add(attrId.get(i));
			dataList.add(item);
		}
		//删除属性关联角色与用户
		DBTemplate.getInstance().batchPreparedUpdate(sql, dataList);
		String usersql = "DELETE FROM " + TableConst.TABLE_REL_USER_PRIV
				+ " WHERE 属性ID = ?";
		DBTemplate.getInstance().batchPreparedUpdate(usersql, dataList);
		String rolesql = "DELETE FROM " + TableConst.TABLE_REL_ROLE_PRIV
				+ " WHERE 属性ID = ?";
		DBTemplate.getInstance().batchPreparedUpdate(rolesql, dataList);
		return attrId.size();
	}
	
	/**
	 * 保存权限属性 列表
	 * @param privId
	 * @param attrlist
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public int savePrivAttrs(String privId,List<PrivAttrBean> attrlist){
		try {
			String sql = "DELETE FROM "+TableConst.TABLE_MANAGE_PRIV_ATTR+" WHERE 权限ID='"+privId+"'";
			DBTemplate.getInstance().updateSql(sql);
			String insSql = "INSERT INTO "+TableConst.TABLE_MANAGE_PRIV_ATTR+" (属性ID,权限ID,属性类型,属性名,属性值,顺序) VALUES(?,'"+privId+"',?,?,?,?)";
			List<List<Object>> dataList = new ArrayList<List<Object>>();
			for(int i=0;i<attrlist.size();i++){
				List<Object> item = new ArrayList<Object>();
				if(attrlist.get(i).getPrivAttrId() == null || "".equals(attrlist.get(i).getPrivAttrId().trim())){
					item.add(CommonTools.createId("ATTR"));
				}else{
					item.add(attrlist.get(i).getPrivAttrId());
				}
				item.add(attrlist.get(i).getPrivAttrType());
				item.add(attrlist.get(i).getPrivAttrName());
				item.add(attrlist.get(i).getPrivAttrValue());
				item.add(i+1);
				dataList.add(item);
			}
			DBTemplate.getInstance().batchPreparedUpdate(insSql, dataList);
			return attrlist.size();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * 获得用户与权限关联 的 组织机构
	 * @param nodeId
	 * @param attrId
	 * @return
	 */
	public List<OrgBean> getChildrenOrgWithUser(String nodeId,String attrId){
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct * from ");
		sb.append(TableConst.TABLE_MANAGE_ORG);
		sb.append(" where 上级组织机构ID = '");
		sb.append(nodeId);
		sb.append("' start with 组织机构ID in ( select distinct 组织机构ID from ");
		sb.append(TableConst.TABLE_REL_USER_PRIV);
		sb.append(" a , ");
		sb.append(TableConst.TABLE_MANAGE_USER);
		sb.append(" b where a.用户ID = b.用户ID and 属性ID = '");
		sb.append(attrId);
		sb.append("' UNION select distinct 组织机构ID from ");
		sb.append(TableConst.TABLE_REL_ROLE_PRIV);
		sb.append(" a ,");
		sb.append(TableConst.TABLE_MANAGE_USER);
		sb.append(" b ,");
		sb.append(TableConst.TABLE_REL_USER_ROLE);
		sb.append(" c where c.用户ID = b.用户ID and c.角色ID = a.角色ID AND 属性ID = '");
		sb.append(attrId);
		sb.append("') connect by 组织机构ID = prior 上级组织机构ID");
		return DBTemplate.getInstance().getResultRowMapperList(sb.toString(), new OrgRowMapper());
	}
	
	/**
	 * 获得角色与权限关联 的 组织机构
	 * @param nodeId
	 * @param attrId
	 * @return
	 */
	public List<OrgBean> getChildrenOrgWithRole(String nodeId,String attrId){
		String sql = "select distinct * from " + TableConst.TABLE_MANAGE_ORG
				+ "where 上级组织机构ID = '" + nodeId
				+ "' start with 组织机构ID in ( select distinct 所属组织机构 from "
				+ TableConst.TABLE_REL_ROLE_PRIV + " a , "
				+ TableConst.TABLE_MANAGE_ROLE
				+ " b where a.角色ID = b.角色ID and 属性ID = '" + attrId
				+ "' ) connect by 组织机构ID = prior 上级组织机构ID";
		return DBTemplate.getInstance().getResultRowMapperList(sql, new OrgRowMapper());
	}
	
	
	/**
	 * 保存权限与角色关系
	 * @param attr
	 * @param roleList
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public int saveRoleRelPrivAttr(PrivAttrBean attr,List<RoleBean> roleList){
		List<List<String>> dataList = new ArrayList<List<String>>();
		for (int i = 0; i < roleList.size(); i++) {
			List<String> item = new ArrayList<String>();
			item.add(roleList.get(i).getRoleId());
			item.add(attr.getPrivAttrId());
			item.add(attr.getPrivId());
			dataList.add(item);
		}
		//删除权限关联的角色
		String sql = "DELETE FROM " + TableConst.TABLE_REL_ROLE_PRIV
				+ " WHERE 属性ID = '" + attr.getPrivAttrId() + "' ";
		DBTemplate.getInstance().updateSql(sql);
		//添加权限关联的角色
		String insSql = "INSERT INTO " + TableConst.TABLE_REL_ROLE_PRIV
				+ " (角色ID,属性ID,权限ID) VALUES(?,?,?)";
		DBTemplate.getInstance().batchPreparedUpdate(insSql, dataList);
		return dataList.size();
	}
	
	/**
	 * 保存角色与权限关系
	 * @param attrList
	 * @param role
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public int saveRoleRelPrivAttr(List<PrivAttrBean> attrList,RoleBean role){
		List<List<String>> dataList = new ArrayList<List<String>>();
		for (int i = 0; i < attrList.size(); i++) {
			List<String> item = new ArrayList<String>();
			item.add(role.getRoleId());
			if (attrList.get(i).getPrivAttrId() == null
					|| "".equals(attrList.get(i).getPrivAttrId())) {
				continue;
			}
			item.add(attrList.get(i).getPrivAttrId());
			item.add(attrList.get(i).getPrivId());
			dataList.add(item);
		}
		String privId = (attrList.size() > 0) ? attrList.get(0).getPrivId()
				: "";
		// 删除角色权限
		String sql = "DELETE FROM " + TableConst.TABLE_REL_ROLE_PRIV
				+ " WHERE 角色ID = '" + role.getRoleId() + "' AND 权限ID = '"
				+ privId + "' ";
		DBTemplate.getInstance().updateSql(sql);
		// 添加角色权限
		String insSql = "INSERT INTO " + TableConst.TABLE_REL_ROLE_PRIV
				+ " (角色ID,属性ID,权限ID) VALUES(?,?,?)";
		DBTemplate.getInstance().batchPreparedUpdate(insSql, dataList);
		return dataList.size();
	}
	
	
	/**
	 * 保存权限与用户关系
	 * 
	 * @param attr
	 * @param roleList
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public int saveUserRelPrivAttr(PrivAttrBean attr,List<UserBean> userList){
		List<List<String>> dataList = new ArrayList<List<String>>();
		for (int i = 0; i < userList.size(); i++) {
			List<String> item = new ArrayList<String>();
			item.add(userList.get(i).getUserId());
			item.add(attr.getPrivAttrId());
			item.add(attr.getPrivId());
			dataList.add(item);
		}
		String sql = "DELETE FROM " + TableConst.TABLE_REL_USER_PRIV
				+ " WHERE 属性ID = '" + attr.getPrivAttrId() + "' ";
		DBTemplate.getInstance().updateSql(sql);
		String insSql = "INSERT INTO " + TableConst.TABLE_REL_USER_PRIV
				+ " (用户ID,属性ID,权限ID) VALUES(?,?,?)";
		DBTemplate.getInstance().batchPreparedUpdate(insSql, dataList);
		return dataList.size();
	}
	
	/**
	 * 保存用户与权限关系 
	 * @param attrList
	 * @param user
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public int saveUserRelPrivAttr(List<PrivAttrBean> attrList,UserBean user){
		List<List<String>> dataList = new ArrayList<List<String>>();
		for (int i = 0; i < attrList.size(); i++) {
			List<String> item = new ArrayList<String>();
			item.add(user.getUserId());
			if (attrList.get(i).getPrivAttrId() == null
					|| "".equals(attrList.get(i).getPrivAttrId())) {
				break;
			}
			item.add(attrList.get(i).getPrivAttrId());
			item.add(attrList.get(i).getPrivId());
			dataList.add(item);
		}
		String privId = (attrList.size() > 0) ? attrList.get(0).getPrivId()
				: "";
		String sql = "DELETE FROM " + TableConst.TABLE_REL_USER_PRIV
				+ " WHERE 用户ID = '" + user.getUserId() + "' AND 权限ID = '"
				+ privId + "' ";
		DBTemplate.getInstance().updateSql(sql);
		String insSql = "INSERT INTO " + TableConst.TABLE_REL_USER_PRIV
				+ " (用户ID,属性ID,权限ID) VALUES(?,?,?)";
		DBTemplate.getInstance().batchPreparedUpdate(insSql, dataList);
		return dataList.size();
	}
	
	/**
	 * 用户关联子权限,勾选状态
	 * @param nodeId
	 * @param userId
	 * @param isDirect true:直接子权限,false 递归所有子权限
	 * @return
	 */
	public List<PrivBean> getChildrenPrivRelUser(String nodeId,String userId,boolean isDirect){
		StringBuffer sbSql = new StringBuffer();
		if (isDirect) {
			sbSql.append("select distinct * from ");
			sbSql.append(TableConst.TABLE_MANAGE_PRIV);
			sbSql.append(" where 权限父ID = '");
			sbSql.append(nodeId);
			sbSql.append("' ");
			sbSql.append("start with 权限ID in ( select distinct 权限ID from ( select distinct 权限ID from ");
			sbSql.append(TableConst.TABLE_REL_USER_PRIV);
			sbSql.append(" where 用户ID = '");
			sbSql.append(userId);
			sbSql.append("' ");
			sbSql.append("union select distinct 权限ID from ");
			sbSql.append(TableConst.TABLE_REL_ROLE_PRIV);
			sbSql.append(" a , ");
			sbSql.append(TableConst.TABLE_REL_USER_ROLE);
			sbSql.append(" b where a.角色ID = b.角色ID AND 用户ID = '");
			sbSql.append(userId);
			sbSql.append("' ) )  connect by 权限ID = prior 权限父ID ORDER BY 权限顺序 ");
		} else {
			sbSql.append("select * from ");
			sbSql.append(TableConst.TABLE_MANAGE_PRIV);
			sbSql.append(" where 权限ID in ( select distinct 权限ID from ");
			sbSql.append(TableConst.TABLE_MANAGE_PRIV);
			sbSql.append(" start with 权限ID in ( select distinct 权限ID from ");
			sbSql.append(TableConst.TABLE_REL_USER_PRIV);
			sbSql.append(" where 用户ID = '");
			sbSql.append(userId);
			sbSql.append("' union select distinct 权限ID from ");
			sbSql.append(TableConst.TABLE_REL_ROLE_PRIV);
			sbSql.append(" a ,");
			sbSql.append(TableConst.TABLE_REL_USER_ROLE);
			sbSql.append(" b where a.角色ID = b.角色ID AND 用户ID = '");
			sbSql.append(userId);
			sbSql.append("' ) connect by 权限ID = prior 权限父ID ) start with 权限ID = '");
			sbSql.append(nodeId);
			sbSql.append("' connect by 权限父ID = prior 权限ID ORDER BY 权限顺序");
		}
		return getPrivList(sbSql.toString());
	}
	
	/**
	 * 用户关联子权限，个人工作台设置,勾选状态,判断应用类权限是否在个人工作台进行个性化设置
	 * @param nodeId
	 * @param userId
	 * @param isDirect true:直接子权限,false 递归所有子权限
	 * @return
	 */
	public List<PrivBean>getAppChildrenPrivRelUser(String userId){
		StringBuffer sbSql = new StringBuffer();
		
			sbSql.append("select b.* from ");
			sbSql.append(TableConst.TABLE_MANAGE_APP_PRIV);
			sbSql.append(" a,");
			sbSql.append(TableConst.TABLE_MANAGE_PRIV);
			sbSql.append(" b");
			sbSql.append(" where a.权限ID=b.权限ID and a.用户ID='");
			sbSql.append(userId);
			sbSql.append("' order by 顺序");
			
			List<PrivBean> pblist=getPrivList(sbSql.toString());
		
		return pblist;
	}
	/**
	 * 保存工作台的应用列表个性化设置
	 * @param nodeId
	 * @param userId
	 * @param isDirect true:直接子权限,false 递归所有子权限
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public void setAppChildrenPrivRelUser(List<Map<String,String>> list,String userId){
		StringBuffer sbSql = new StringBuffer();

		sbSql.append("delete from ");
		sbSql.append(TableConst.TABLE_MANAGE_APP_PRIV);
		sbSql.append("where 用户ID='");
		sbSql.append(userId);
		sbSql.append("'");
		DBTemplate.getInstance().updateSql(sbSql.toString());
		for(Map<String,String> map:list){
			String sql="insert into"+ TableConst.TABLE_MANAGE_APP_PRIV+"(用户ID,权限ID,应用别名,顺序) values('"+
			userId+"','"+map.get("pirvId")+"','null',"+Integer.valueOf(map.get("order"))+")";
			DBTemplate.getInstance().updateSql(sql);
		}
		
	}
	/**
	 * 获得 用户关联的权限属性
	 * @param userId
	 * @param privId
	 * @return
	 */
	public List<PrivAttrBean> getUserPrivAttr(String userId,String privId){
		StringBuffer sbSql = new StringBuffer();
		try {
			sbSql.append("select * from ");
			sbSql.append(TableConst.TABLE_MANAGE_PRIV_ATTR);
			sbSql.append(" where 属性ID in ( select 属性ID from ");
			sbSql.append(TableConst.TABLE_REL_USER_ROLE);
			sbSql.append(" a, ");
			sbSql.append(TableConst.TABLE_REL_ROLE_PRIV);
			sbSql.append(" b where a.角色ID = b.角色ID AND a.用户ID = '");
			sbSql.append(userId);
			sbSql.append("' union select 属性ID from ");
			sbSql.append(TableConst.TABLE_REL_USER_PRIV);
			sbSql.append("where 用户ID = '");
			sbSql.append(userId);
			sbSql.append("' ) ");
			if (privId != null && !"".equals(privId)) {
				sbSql.append(" AND 权限ID = '");
				sbSql.append(privId);
				sbSql.append("'");
			}
			sbSql.append(" ORDER BY 权限ID,顺序 ASC");
			return getPrivAttrList(sbSql.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<PrivAttrBean>();
		}
	}
	
	/**
	 * 获得 用户关联的子权限属性
	 * @param userId
	 * @param privId
	 * @param isDirect true 直接子节点 
	 * @return
	 */
	public List<PrivAttrBean> getUserChildrenPrivAttr(String userId,String privId,boolean isDirect){
		StringBuffer sb = new StringBuffer();
		if(isDirect){
			sb.append("SELECT c.属性ID,c.权限ID,属性类型,属性名,属性值,c.顺序 FROM ");
			sb.append(TableConst.TABLE_MANAGE_PRIV);
			sb.append(" a, ");
			sb.append(TableConst.TABLE_REL_USER_PRIV);
			sb.append(" b, ");
			sb.append(TableConst.TABLE_MANAGE_PRIV_ATTR);
			sb.append(" c WHERE a.权限ID = b.权限ID AND b.属性ID = c.属性ID AND a.权限父ID = '");
			sb.append(privId);
			sb.append("' AND 用户ID = '");
			sb.append(userId);
			sb.append("' UNION SELECT c.属性ID,c.权限ID,属性类型,属性名,属性值,c.顺序 FROM ");
			sb.append(TableConst.TABLE_MANAGE_PRIV);
			sb.append(" a, ");
			sb.append(TableConst.TABLE_REL_ROLE_PRIV);
			sb.append(" b, ");
			sb.append(TableConst.TABLE_REL_USER_ROLE);
			sb.append(" d, ");
			sb.append(TableConst.TABLE_MANAGE_PRIV_ATTR);
			sb.append(" c WHERE a.权限ID = b.权限ID AND b.属性ID = c.属性ID AND a.权限父ID = '");
			sb.append(privId);
			sb.append("' AND b.角色ID = d.角色ID AND 用户ID = '");
			sb.append(userId);
			sb.append("' order by 权限ID ,顺序 asc ");
		}else{
			sb.append("select t.属性ID,t.权限ID,属性类型,属性名,属性值,t.顺序 from ( select * from ");
			sb.append(TableConst.TABLE_MANAGE_PRIV);
			sb.append(" where 权限ID in ( select distinct 权限ID from ");
			sb.append(TableConst.TABLE_MANAGE_PRIV);
			sb.append(" start with 权限ID in ( select distinct 权限ID from ");
			sb.append(TableConst.TABLE_REL_USER_PRIV);
			sb.append(" where 用户ID = '");
			sb.append(userId);
			sb.append("' union select distinct 权限ID from ");
			sb.append(TableConst.TABLE_REL_ROLE_PRIV);
			sb.append(" a ,");
			sb.append(TableConst.TABLE_REL_USER_ROLE);
			sb.append(" b where a.角色ID = b.角色ID AND 用户ID = '");
			sb.append(userId);
			sb.append("' ) connect by 权限ID = prior 权限父ID ) start with 权限ID = '");
			sb.append(privId);
			sb.append("' connect by 权限父ID = prior 权限ID ) p, ");
			sb.append(TableConst.TABLE_MANAGE_PRIV_ATTR);
			sb.append(" t where p.权限ID = t.权限ID ");
		}
		return getPrivAttrList(sb.toString());
	}
	
	
	/**
	 * 获得 角色关联的权限属性
	 * @param roleId
	 * @param privId
	 * @return
	 */
	public List<PrivAttrBean> getRolePrivAttr(String roleId,String privId){
		try {
			String sql = "SELECT * FROM " + TableConst.TABLE_MANAGE_PRIV_ATTR
					+ " a, " + TableConst.TABLE_REL_ROLE_PRIV
					+ " b WHERE a.属性ID = b.属性ID AND b.角色ID='" + roleId + "'";
			if (privId != null && !"".equals(privId)) {
				sql += " AND b.权限ID='" + privId + "'";
			}
			return getPrivAttrList(sql + " ORDER BY a.顺序 ASC");
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<PrivAttrBean>();
		}
	}
	
	/**
	 * 角色关联子组织机构,勾选状态
	 * @param nodeId
	 * @param userId
	 * @param isDirect true:直接子权限,false 递归所有子权限
	 * @return
	 */
	public List<PrivBean> getChildrenPrivRelRole(String nodeId,String roleId,boolean isDirect){
		String sql = "";
		if (isDirect) {
			sql = "select distinct * from " + TableConst.TABLE_MANAGE_PRIV
					+ " where 权限父ID = '" + nodeId
					+ "' start with 权限ID in ( select distinct 权限ID from "
					+ TableConst.TABLE_REL_ROLE_PRIV + " where 角色ID = '"
					+ roleId
					+ "' ) connect by 权限ID = prior 权限父ID ORDER BY 权限顺序";
		} else {
			sql = "select * from ( select distinct * from "
					+ TableConst.TABLE_MANAGE_PRIV
					+ " start with 权限ID in ( select distinct 权限ID from "
					+ TableConst.TABLE_REL_ROLE_PRIV + " where 角色ID = '"
					+ roleId
					+ "' ) connect by 权限ID = prior 权限父ID ) start with 权限ID = '"
					+ nodeId + "' connect by 权限父ID = prior 权限ID ";
		}
		return getPrivList(sql);
	}

	/**
	 * 复制权限属性
	 * @param sourcePrivId
	 * @param targetPrivId
	 * @return
	 */
	public int pastePrivAttrs(String sourcePrivId,String targetPrivId){
		String sql = "SELECT * FROM " + TableConst.TABLE_MANAGE_PRIV_ATTR
				+ " WHERE 权限ID = '" + sourcePrivId
				+ "' AND 属性名 NOT IN ( SELECT 属性名 FROM "
				+ TableConst.TABLE_MANAGE_PRIV_ATTR + " WHERE 权限ID = '"
				+ targetPrivId + "')";
		List<PrivAttrBean> attrs = DBTemplate.getInstance().getResultRowMapperList(
				sql, new PrivAttrRowMapper());
		String insSql = "INSERT INTO " + TableConst.TABLE_MANAGE_PRIV_ATTR
				+ " (属性ID,权限ID,属性类型,属性名,属性值,顺序) VALUES(?,?,?,?,?,?)";
		List<List<Object>> dataList = new ArrayList<List<Object>>();
		for (int i = 0; i < attrs.size(); i++) {
			List<Object> one = new ArrayList<Object>();
			one.add(CommonTools.createId("ATTR"));
			one.add(targetPrivId);
			one.add(attrs.get(i).getPrivAttrType());
			one.add(attrs.get(i).getPrivAttrName());
			one.add(attrs.get(i).getPrivAttrValue());
			one.add(attrs.get(i).getAttrOrder());
			dataList.add(one);
		}
		return DBTemplate.getInstance().batchPreparedUpdate(insSql, dataList).length;
	}
	
	/**
	 * 
	 * @param sql
	 * @return
	 */
	private List<PrivBean> getPrivList(String sql){
		return DBTemplate.getInstance().getResultRowMapperList(sql, new PrivRowMapper());
	}
	
	/**
	 * 
	 * @param sql
	 * @return
	 */
	private List<PrivAttrBean> getPrivAttrList(String sql){
		return DBTemplate.getInstance().getResultRowMapperList(sql, new PrivAttrRowMapper());
	}
	
	/**
	 * 获得权限树XML
	 * @param privList
	 * @return
	 */
	public String getPrivXML(List<PrivBean> privList){
		String xmlStr = "";
		try {
			if(privList.size() > 0){
				Document doc = DocumentHelper.createDocument();   
			    doc.setXMLEncoding("GBK");   
			    Element root = doc.addElement("node"); 
			    root.addAttribute("nodeId", privList.get(0).getPrivId());
			    root.addAttribute("label", privList.get(0).getPrivName()); 
			    CreatePrivXmlNode(privList,root,1);
			    xmlStr = doc.asXML();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xmlStr;
	}
	
	private void CreatePrivXmlNode(List<PrivBean> privList,Element el,int index){
		try {
			if(index < privList.size()){
				PrivBean pb = privList.get(index);
				if(el != null && pb.getPrivParentId().equals(el.attributeValue("nodeId"))){
					Element sel = el.addElement("node");   
					sel.addAttribute("nodeId", pb.getPrivId());
					sel.addAttribute("label", pb.getPrivName());
					sel.addAttribute("parentId", pb.getPrivParentId());  
				    sel.addAttribute("type", pb.getPrivType());
					sel.addAttribute("group", pb.getPrivGroup());
				    index++;
				    CreatePrivXmlNode(privList, sel, index);
				}else{
					Element sel = el.getParent();
					if(sel != null){
						CreatePrivXmlNode(privList, sel, index);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 获得权限节点 与 组织机构 关联关系
	 * @param privId
	 * @param user
	 * @return
	 */
	public List<String> getOrgNamePrivList(String privId,UserBean user){
		List<String> privOrgNameList = null;
		if(privId != null && privId.equals(WebConfigUtil.getOrgPrivNode())){
			// 组织机构
			privOrgNameList = new ArrayList<String>();
			List<PrivAttrBean> orgattrs = getUserPrivAttr(user.getUserId(), WebConfigUtil.getOrgPrivNode());
			for(int a = 0;a<orgattrs.size();a++){
				privOrgNameList.add(orgattrs.get(a).getPrivAttrValue());
			}
		}
		else if(privId != null && privId.equals(WebConfigUtil.getNewsTreeRoot())){
			//新闻
			privOrgNameList = new ArrayList<String>();
			List<PrivAttrBean> newsattrs = getUserPrivAttr(user.getUserId(), WebConfigUtil.getOrgPrivNode());
			for(int a = 0;a<newsattrs.size();a++){
				privOrgNameList.add(newsattrs.get(a).getPrivAttrValue());
			}
		}
		else if(privId != null && privId.equals(WebConfigUtil.getSourceTreeRoot())){
			//文档资料
			privOrgNameList = new ArrayList<String>();
			List<PrivAttrBean> sourceattrs = getUserPrivAttr(user.getUserId(), WebConfigUtil.getOrgPrivNode());
			for(int a = 0;a<sourceattrs.size();a++){
				privOrgNameList.add(sourceattrs.get(a).getPrivAttrValue());
			}
		}
		else if(privId != null && privId.equals(WebConfigUtil.getLinkTreeRoot())){
			//网站链接
			privOrgNameList = new ArrayList<String>();
			List<PrivAttrBean> linkattrs = getUserPrivAttr(user.getUserId(), WebConfigUtil.getOrgPrivNode());
			for(int a = 0;a<linkattrs.size();a++){
				privOrgNameList.add(linkattrs.get(a).getPrivAttrValue());
			}
		}
		return privOrgNameList;
	}
	
	
	/**
	 * 带过滤属性
	 * @param nodeId
	 * @param userId
	 * @param attrFilter
	 * @return
	 */
	public List<PrivBean> getChildrenPrivRelUser(String nodeId,String userId,boolean isDirect,String attrFilter){
		StringBuffer sbSql = new StringBuffer();
		if(isDirect){
		sbSql.append("select distinct * from ");
		sbSql.append(TableConst.TABLE_MANAGE_PRIV);
		sbSql.append(" where 权限父ID = '");
		sbSql.append(nodeId);
		sbSql.append("' ");
		sbSql.append("start with 权限ID in ( select distinct 权限ID from ( select distinct c.权限ID from ");
		sbSql.append(TableConst.TABLE_REL_USER_PRIV);
		sbSql.append(" c , ");
		sbSql.append(TableConst.TABLE_MANAGE_PRIV_ATTR);
		sbSql.append(" d where c.属性ID = d.属性ID and ");
		sbSql.append(attrFilter);
		sbSql.append(" and 用户ID = '");
		sbSql.append(userId);
		sbSql.append("' ");
		sbSql.append("union select distinct a.权限ID from ");
		sbSql.append(TableConst.TABLE_REL_ROLE_PRIV);
		sbSql.append(" a , ");
		sbSql.append(TableConst.TABLE_REL_USER_ROLE);
		sbSql.append(" b , ");
		sbSql.append(TableConst.TABLE_MANAGE_PRIV_ATTR);
		sbSql.append(" e where a.属性ID = e.属性ID and a.角色ID = b.角色ID  AND ");
		sbSql.append(attrFilter);
		sbSql.append(" AND 用户ID = '");
		sbSql.append(userId);
		sbSql.append("' ) )  connect by 权限ID = prior 权限父ID ORDER BY 权限顺序 ");
		
		}else{
			sbSql.append("select * from ");
			sbSql.append(TableConst.TABLE_MANAGE_PRIV);
			sbSql.append(" where 权限ID in ( select distinct 权限ID from ");
			sbSql.append(TableConst.TABLE_MANAGE_PRIV);
			sbSql.append(" start with 权限ID in ( select distinct a.权限ID from ");
			sbSql.append(TableConst.TABLE_REL_USER_PRIV);
			sbSql.append(" a, ");
			sbSql.append(TableConst.TABLE_MANAGE_PRIV_ATTR);
			sbSql.append(" b  ");
			sbSql.append(" where 用户ID = '");
			sbSql.append(userId);
			sbSql.append( "' and a.属性ID=b.属性ID and b.属性名='管理权限 ");
			sbSql.append("' union select distinct a.权限ID from ");
			sbSql.append(TableConst.TABLE_REL_ROLE_PRIV);
			sbSql.append(" a ,");
			sbSql.append(TableConst.TABLE_REL_USER_ROLE);
			sbSql.append(" b , ");
			sbSql.append(TableConst.TABLE_MANAGE_PRIV_ATTR);
			sbSql.append(" c  ");
			sbSql.append(" where a.角色ID = b.角色ID and a.属性ID=c.属性ID and c.属性名='管理权限' AND 用户ID = '");
			sbSql.append(userId);
			sbSql.append("' ) connect by 权限ID = prior 权限父ID ) start with 权限ID = '");
			sbSql.append(nodeId);
			sbSql.append("' connect by 权限父ID = prior 权限ID ");
		}
		
		
		return getPrivList(sbSql.toString());
	}
	/**
	 * 
	 * @param nodeId	权限ID
	 * @return	对应权限ID的名称
	 */
	public String getPrivName(String privId) throws Exception{
		String privName = "";
		String sql = "SELECT 权限名 FROM PSIDP.IDP_WEBNEW.门户_权限_管理表 WHERE 权限ID='" + privId + "'";
		Map<String, Object> dataMap = DBTemplate.getInstance().getResultMap(sql);
		privName = (null == dataMap.get("权限名") ? "" : (String)dataMap.get("权限名"));
		return privName;
	}
	
}
