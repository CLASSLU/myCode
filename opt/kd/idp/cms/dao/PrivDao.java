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
	 * ��ѯȨ����Ϣ
	 * @param privId
	 * @return
	 */
	public PrivBean getPrivInfo(String privId){
		String sql = "SELECT * FROM "+TableConst.TABLE_MANAGE_PRIV+" WHERE Ȩ��ID = '"+privId+"'";
		PrivBean pb = DBTemplate.getInstance().getResultRowMapper(sql, new PrivRowMapper());
		pb.setAttrList(getAttrsFromPriv(privId));
		return pb;
	}
	
	/**
	 * ͨ��Ȩ������ѯȨ����Ϣ (����������)
	 * @param privName
	 * @return
	 */
	public List<PrivBean> getPrivInfosWithoutAttr(String privName){
		String sql = "SELECT * FROM "+TableConst.TABLE_MANAGE_PRIV+" WHERE Ȩ���� = '"+privName+"'";
		return DBTemplate.getInstance().getResultRowMapperList(sql, new PrivRowMapper());
	}
	
	/**
	 * ��ѯ��Ȩ����Ϣ
	 * @param nodeId
	 * @param isDirect  true:ֱ����Ȩ��,false �ݹ�������Ȩ��
	 * @param privOrgNameList �û�Ȩ�޹������ӽڵ�ID��ֱ���ӽڵ�ʱ�����ã�
	 * @return
	 */
	public List<PrivBean> getChildrenPrivInfo(String nodeId,boolean isDirect,List<String> privOrgNameList){
		StringBuffer sbSql = new StringBuffer();
		if (isDirect) {
			sbSql.append("SELECT * FROM ");
			sbSql.append(TableConst.TABLE_MANAGE_PRIV);
			sbSql.append(" WHERE Ȩ�޸�ID='");
			sbSql.append(nodeId);
			sbSql.append("'");
			if (privOrgNameList != null && privOrgNameList.size() > 0) {
				sbSql.append(" AND Ȩ���� in ( ");
				for (int i = 0; i < privOrgNameList.size(); i++) {
					sbSql.append("'" + privOrgNameList.get(i) + "'");
					if (i < privOrgNameList.size() - 1) {
						sbSql.append(",");
					}
				}
				sbSql.append(" )");
			}
			if (nodeId.equals(WebConfigUtil.getPrivTreeRootId())) {
				sbSql.append(" AND Ȩ��ID != 'org_root' ");
			}
			sbSql.append("ORDER BY Ȩ��˳��");
		} else {
			sbSql.append("SELECT * FROM ( SELECT * FROM ");
			sbSql.append(TableConst.TABLE_MANAGE_PRIV);
			sbSql.append(" ORDER BY Ȩ��˳�� ) start with Ȩ��ID = '");
			sbSql.append(nodeId);
			sbSql.append("' connect by Ȩ�޸�ID = prior Ȩ��ID");
		}
		return getPrivList(sbSql.toString());
	}
	
	/**
	 * ��ѯȨ�޵ĸ�Ȩ����Ϣ 
	 * @param nodeId
	 * @param isDirect true:ֱ�Ӹ�Ȩ��,false �ݹ����и�Ȩ��
	 * @return
	 */
	public List<PrivBean> getParentPrivInfo(String nodeId,boolean isDirect){
		String sql = "";
		if (isDirect) {
			sql = "SELECT b.*  FROM " + TableConst.TABLE_MANAGE_PRIV + " a, "
					+ TableConst.TABLE_MANAGE_PRIV + " b where a.Ȩ��ID = '"
					+ nodeId + "' and a.Ȩ�޸�ID=b.Ȩ��ID";
		} else {
			sql = "SELECT * FROM " + TableConst.TABLE_MANAGE_PRIV
					+ " start with Ȩ��ID = '" + nodeId
					+ "' connect by Ȩ��ID = prior Ȩ�޸�ID";
		}
		return getPrivList(sql);
	}
	
	/**
	 * ��ѯȨ��������Ϣ
	 * @param privId
	 * @return
	 */
	public List<PrivAttrBean> getAttrsFromPriv(String privId){
		String sql = "SELECT * FROM "+TableConst.TABLE_MANAGE_PRIV_ATTR+" WHERE Ȩ��ID = '"+privId+"' ORDER BY ˳�� asc";
		return getPrivAttrList(sql);
	}
	
	
	/**
	 * ����Ȩ��
	 * @param priv
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public String addNewPriv(PrivBean priv){
		String privId = CommonTools.createId("PRIV");
		String sql = "INSERT INTO " + TableConst.TABLE_MANAGE_PRIV
				+ " (Ȩ����,Ȩ��ID,Ȩ����,����,Ȩ�޸�ID,Ȩ��ͼƬ,Ȩ�޲��,Ȩ��˳��) VALUES('"
				+ priv.getPrivGroup() + "','" + privId + "','"
				+ priv.getPrivName() + "','" + priv.getPrivType() + "','"
				+ priv.getPrivParentId() + "','" + priv.getPrivImage() + "',"
				+ priv.getPrivLevel() + "," + priv.getPrivOrder() + ") ";
		DBTemplate.getInstance().updateSql(sql);
		// ɾ�����ڵ�����������Ϣ
		sql = "DELETE FROM " + TableConst.TABLE_REL_ROLE_PRIV
				+ " WHERE Ȩ��ID = '" + priv.getPrivParentId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		sql = "DELETE FROM " + TableConst.TABLE_REL_USER_PRIV
				+ " WHERE Ȩ��ID = '" + priv.getPrivParentId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		sql = "DELETE FROM " + TableConst.TABLE_MANAGE_PRIV_ATTR
				+ " WHERE Ȩ��ID = '" + priv.getPrivParentId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		return privId;
	}
	
	/**
	 * �޸�Ȩ��
	 * @param priv
	 * @return
	 */
	public String modPriv(PrivBean priv){
		try {
			String sql = "UPDATE " + TableConst.TABLE_MANAGE_PRIV
					+ " SET Ȩ����='" + priv.getPrivName() + "' , ����='"
					+ priv.getPrivType() +"',Ȩ�޲��='"+priv.getPrivLevel()+ "',Ȩ��˳�� = '" + priv.getPrivOrder()+"',Ȩ�޸�ID='"+priv.getPrivParentId()+"'"
					+ " WHERE Ȩ��ID='" + priv.getPrivId() + "'";
			DBTemplate.getInstance().updateSql(sql);
			return priv.getPrivId();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * ɾ��Ȩ��
	 * @param priv
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public String deletePriv(PrivBean priv){
		String sql = "DELETE FROM " + TableConst.TABLE_REL_ROLE_PRIV
				+ " WHERE Ȩ��ID='" + priv.getPrivId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		sql = "DELETE FROM " + TableConst.TABLE_REL_USER_PRIV + " WHERE Ȩ��ID='"
				+ priv.getPrivId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		sql = "DELETE FROM " + TableConst.TABLE_MANAGE_PRIV_ATTR
				+ " WHERE Ȩ��ID='" + priv.getPrivId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		sql = "DELETE FROM " + TableConst.TABLE_MANAGE_PRIV + " WHERE Ȩ��ID='"
				+ priv.getPrivId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		return priv.getPrivId();
	}
	
	/**
	 * ���Ȩ������
	 * @param attr
	 * @return
	 */
	public String addNewPrivAttr(PrivAttrBean attr){
		try {
			String attrId = CommonTools.createId("ATTR");
			String sql = "INSERT INTO " + TableConst.TABLE_MANAGE_PRIV_ATTR
					+ " (����ID,Ȩ��ID,��������,������,����ֵ,˳��) VALUES ('"+attrId+"','" + attr.getPrivId()
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
	 * �޸�Ȩ������
	 * @param attr
	 * @return
	 */
	public String modPrivAttr(PrivAttrBean attr){
		try {
			String sql = "UPDATE " + TableConst.TABLE_MANAGE_PRIV_ATTR
					+ " SET Ȩ��ID='" + attr.getPrivId() + "',��������='"
					+ attr.getPrivAttrType() + "',������='"
					+ attr.getPrivAttrName() + "',����ֵ='"
					+ attr.getPrivAttrValue() + "',˳��="+attr.getAttrOrder()+" WHERE ����ID='"
					+ attr.getPrivAttrId() + "'";
			DBTemplate.getInstance().updateSql(sql);
			return attr.getPrivAttrName();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * ɾ�� Ȩ������
	 * @param attr
	 * @return
	 */
	public String deletePrivAttr(PrivAttrBean attr){
		try {
			String sql = "DELETE FROM " + TableConst.TABLE_MANAGE_PRIV_ATTR
					+ " WHERE ����ID='"+attr.getPrivAttrId()+"'";
			DBTemplate.getInstance().updateSql(sql);
			return attr.getPrivAttrName();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * ɾ�����Ȩ������
	 * @param privId
	 * @param attrName
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public int deletePrivAttr(List<String> attrId){
		String sql = "DELETE FROM " + TableConst.TABLE_MANAGE_PRIV_ATTR
				+ " WHERE ����ID = ? ";
		List<List<String>> dataList = new ArrayList<List<String>>();
		for (int i = 0; i < attrId.size(); i++) {
			List<String> item = new ArrayList<String>();
			item.add(attrId.get(i));
			dataList.add(item);
		}
		//ɾ�����Թ�����ɫ���û�
		DBTemplate.getInstance().batchPreparedUpdate(sql, dataList);
		String usersql = "DELETE FROM " + TableConst.TABLE_REL_USER_PRIV
				+ " WHERE ����ID = ?";
		DBTemplate.getInstance().batchPreparedUpdate(usersql, dataList);
		String rolesql = "DELETE FROM " + TableConst.TABLE_REL_ROLE_PRIV
				+ " WHERE ����ID = ?";
		DBTemplate.getInstance().batchPreparedUpdate(rolesql, dataList);
		return attrId.size();
	}
	
	/**
	 * ����Ȩ������ �б�
	 * @param privId
	 * @param attrlist
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public int savePrivAttrs(String privId,List<PrivAttrBean> attrlist){
		try {
			String sql = "DELETE FROM "+TableConst.TABLE_MANAGE_PRIV_ATTR+" WHERE Ȩ��ID='"+privId+"'";
			DBTemplate.getInstance().updateSql(sql);
			String insSql = "INSERT INTO "+TableConst.TABLE_MANAGE_PRIV_ATTR+" (����ID,Ȩ��ID,��������,������,����ֵ,˳��) VALUES(?,'"+privId+"',?,?,?,?)";
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
	 * ����û���Ȩ�޹��� �� ��֯����
	 * @param nodeId
	 * @param attrId
	 * @return
	 */
	public List<OrgBean> getChildrenOrgWithUser(String nodeId,String attrId){
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct * from ");
		sb.append(TableConst.TABLE_MANAGE_ORG);
		sb.append(" where �ϼ���֯����ID = '");
		sb.append(nodeId);
		sb.append("' start with ��֯����ID in ( select distinct ��֯����ID from ");
		sb.append(TableConst.TABLE_REL_USER_PRIV);
		sb.append(" a , ");
		sb.append(TableConst.TABLE_MANAGE_USER);
		sb.append(" b where a.�û�ID = b.�û�ID and ����ID = '");
		sb.append(attrId);
		sb.append("' UNION select distinct ��֯����ID from ");
		sb.append(TableConst.TABLE_REL_ROLE_PRIV);
		sb.append(" a ,");
		sb.append(TableConst.TABLE_MANAGE_USER);
		sb.append(" b ,");
		sb.append(TableConst.TABLE_REL_USER_ROLE);
		sb.append(" c where c.�û�ID = b.�û�ID and c.��ɫID = a.��ɫID AND ����ID = '");
		sb.append(attrId);
		sb.append("') connect by ��֯����ID = prior �ϼ���֯����ID");
		return DBTemplate.getInstance().getResultRowMapperList(sb.toString(), new OrgRowMapper());
	}
	
	/**
	 * ��ý�ɫ��Ȩ�޹��� �� ��֯����
	 * @param nodeId
	 * @param attrId
	 * @return
	 */
	public List<OrgBean> getChildrenOrgWithRole(String nodeId,String attrId){
		String sql = "select distinct * from " + TableConst.TABLE_MANAGE_ORG
				+ "where �ϼ���֯����ID = '" + nodeId
				+ "' start with ��֯����ID in ( select distinct ������֯���� from "
				+ TableConst.TABLE_REL_ROLE_PRIV + " a , "
				+ TableConst.TABLE_MANAGE_ROLE
				+ " b where a.��ɫID = b.��ɫID and ����ID = '" + attrId
				+ "' ) connect by ��֯����ID = prior �ϼ���֯����ID";
		return DBTemplate.getInstance().getResultRowMapperList(sql, new OrgRowMapper());
	}
	
	
	/**
	 * ����Ȩ�����ɫ��ϵ
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
		//ɾ��Ȩ�޹����Ľ�ɫ
		String sql = "DELETE FROM " + TableConst.TABLE_REL_ROLE_PRIV
				+ " WHERE ����ID = '" + attr.getPrivAttrId() + "' ";
		DBTemplate.getInstance().updateSql(sql);
		//���Ȩ�޹����Ľ�ɫ
		String insSql = "INSERT INTO " + TableConst.TABLE_REL_ROLE_PRIV
				+ " (��ɫID,����ID,Ȩ��ID) VALUES(?,?,?)";
		DBTemplate.getInstance().batchPreparedUpdate(insSql, dataList);
		return dataList.size();
	}
	
	/**
	 * �����ɫ��Ȩ�޹�ϵ
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
		// ɾ����ɫȨ��
		String sql = "DELETE FROM " + TableConst.TABLE_REL_ROLE_PRIV
				+ " WHERE ��ɫID = '" + role.getRoleId() + "' AND Ȩ��ID = '"
				+ privId + "' ";
		DBTemplate.getInstance().updateSql(sql);
		// ��ӽ�ɫȨ��
		String insSql = "INSERT INTO " + TableConst.TABLE_REL_ROLE_PRIV
				+ " (��ɫID,����ID,Ȩ��ID) VALUES(?,?,?)";
		DBTemplate.getInstance().batchPreparedUpdate(insSql, dataList);
		return dataList.size();
	}
	
	
	/**
	 * ����Ȩ�����û���ϵ
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
				+ " WHERE ����ID = '" + attr.getPrivAttrId() + "' ";
		DBTemplate.getInstance().updateSql(sql);
		String insSql = "INSERT INTO " + TableConst.TABLE_REL_USER_PRIV
				+ " (�û�ID,����ID,Ȩ��ID) VALUES(?,?,?)";
		DBTemplate.getInstance().batchPreparedUpdate(insSql, dataList);
		return dataList.size();
	}
	
	/**
	 * �����û���Ȩ�޹�ϵ 
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
				+ " WHERE �û�ID = '" + user.getUserId() + "' AND Ȩ��ID = '"
				+ privId + "' ";
		DBTemplate.getInstance().updateSql(sql);
		String insSql = "INSERT INTO " + TableConst.TABLE_REL_USER_PRIV
				+ " (�û�ID,����ID,Ȩ��ID) VALUES(?,?,?)";
		DBTemplate.getInstance().batchPreparedUpdate(insSql, dataList);
		return dataList.size();
	}
	
	/**
	 * �û�������Ȩ��,��ѡ״̬
	 * @param nodeId
	 * @param userId
	 * @param isDirect true:ֱ����Ȩ��,false �ݹ�������Ȩ��
	 * @return
	 */
	public List<PrivBean> getChildrenPrivRelUser(String nodeId,String userId,boolean isDirect){
		StringBuffer sbSql = new StringBuffer();
		if (isDirect) {
			sbSql.append("select distinct * from ");
			sbSql.append(TableConst.TABLE_MANAGE_PRIV);
			sbSql.append(" where Ȩ�޸�ID = '");
			sbSql.append(nodeId);
			sbSql.append("' ");
			sbSql.append("start with Ȩ��ID in ( select distinct Ȩ��ID from ( select distinct Ȩ��ID from ");
			sbSql.append(TableConst.TABLE_REL_USER_PRIV);
			sbSql.append(" where �û�ID = '");
			sbSql.append(userId);
			sbSql.append("' ");
			sbSql.append("union select distinct Ȩ��ID from ");
			sbSql.append(TableConst.TABLE_REL_ROLE_PRIV);
			sbSql.append(" a , ");
			sbSql.append(TableConst.TABLE_REL_USER_ROLE);
			sbSql.append(" b where a.��ɫID = b.��ɫID AND �û�ID = '");
			sbSql.append(userId);
			sbSql.append("' ) )  connect by Ȩ��ID = prior Ȩ�޸�ID ORDER BY Ȩ��˳�� ");
		} else {
			sbSql.append("select * from ");
			sbSql.append(TableConst.TABLE_MANAGE_PRIV);
			sbSql.append(" where Ȩ��ID in ( select distinct Ȩ��ID from ");
			sbSql.append(TableConst.TABLE_MANAGE_PRIV);
			sbSql.append(" start with Ȩ��ID in ( select distinct Ȩ��ID from ");
			sbSql.append(TableConst.TABLE_REL_USER_PRIV);
			sbSql.append(" where �û�ID = '");
			sbSql.append(userId);
			sbSql.append("' union select distinct Ȩ��ID from ");
			sbSql.append(TableConst.TABLE_REL_ROLE_PRIV);
			sbSql.append(" a ,");
			sbSql.append(TableConst.TABLE_REL_USER_ROLE);
			sbSql.append(" b where a.��ɫID = b.��ɫID AND �û�ID = '");
			sbSql.append(userId);
			sbSql.append("' ) connect by Ȩ��ID = prior Ȩ�޸�ID ) start with Ȩ��ID = '");
			sbSql.append(nodeId);
			sbSql.append("' connect by Ȩ�޸�ID = prior Ȩ��ID ORDER BY Ȩ��˳��");
		}
		return getPrivList(sbSql.toString());
	}
	
	/**
	 * �û�������Ȩ�ޣ����˹���̨����,��ѡ״̬,�ж�Ӧ����Ȩ���Ƿ��ڸ��˹���̨���и��Ի�����
	 * @param nodeId
	 * @param userId
	 * @param isDirect true:ֱ����Ȩ��,false �ݹ�������Ȩ��
	 * @return
	 */
	public List<PrivBean>getAppChildrenPrivRelUser(String userId){
		StringBuffer sbSql = new StringBuffer();
		
			sbSql.append("select b.* from ");
			sbSql.append(TableConst.TABLE_MANAGE_APP_PRIV);
			sbSql.append(" a,");
			sbSql.append(TableConst.TABLE_MANAGE_PRIV);
			sbSql.append(" b");
			sbSql.append(" where a.Ȩ��ID=b.Ȩ��ID and a.�û�ID='");
			sbSql.append(userId);
			sbSql.append("' order by ˳��");
			
			List<PrivBean> pblist=getPrivList(sbSql.toString());
		
		return pblist;
	}
	/**
	 * ���湤��̨��Ӧ���б���Ի�����
	 * @param nodeId
	 * @param userId
	 * @param isDirect true:ֱ����Ȩ��,false �ݹ�������Ȩ��
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public void setAppChildrenPrivRelUser(List<Map<String,String>> list,String userId){
		StringBuffer sbSql = new StringBuffer();

		sbSql.append("delete from ");
		sbSql.append(TableConst.TABLE_MANAGE_APP_PRIV);
		sbSql.append("where �û�ID='");
		sbSql.append(userId);
		sbSql.append("'");
		DBTemplate.getInstance().updateSql(sbSql.toString());
		for(Map<String,String> map:list){
			String sql="insert into"+ TableConst.TABLE_MANAGE_APP_PRIV+"(�û�ID,Ȩ��ID,Ӧ�ñ���,˳��) values('"+
			userId+"','"+map.get("pirvId")+"','null',"+Integer.valueOf(map.get("order"))+")";
			DBTemplate.getInstance().updateSql(sql);
		}
		
	}
	/**
	 * ��� �û�������Ȩ������
	 * @param userId
	 * @param privId
	 * @return
	 */
	public List<PrivAttrBean> getUserPrivAttr(String userId,String privId){
		StringBuffer sbSql = new StringBuffer();
		try {
			sbSql.append("select * from ");
			sbSql.append(TableConst.TABLE_MANAGE_PRIV_ATTR);
			sbSql.append(" where ����ID in ( select ����ID from ");
			sbSql.append(TableConst.TABLE_REL_USER_ROLE);
			sbSql.append(" a, ");
			sbSql.append(TableConst.TABLE_REL_ROLE_PRIV);
			sbSql.append(" b where a.��ɫID = b.��ɫID AND a.�û�ID = '");
			sbSql.append(userId);
			sbSql.append("' union select ����ID from ");
			sbSql.append(TableConst.TABLE_REL_USER_PRIV);
			sbSql.append("where �û�ID = '");
			sbSql.append(userId);
			sbSql.append("' ) ");
			if (privId != null && !"".equals(privId)) {
				sbSql.append(" AND Ȩ��ID = '");
				sbSql.append(privId);
				sbSql.append("'");
			}
			sbSql.append(" ORDER BY Ȩ��ID,˳�� ASC");
			return getPrivAttrList(sbSql.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<PrivAttrBean>();
		}
	}
	
	/**
	 * ��� �û���������Ȩ������
	 * @param userId
	 * @param privId
	 * @param isDirect true ֱ���ӽڵ� 
	 * @return
	 */
	public List<PrivAttrBean> getUserChildrenPrivAttr(String userId,String privId,boolean isDirect){
		StringBuffer sb = new StringBuffer();
		if(isDirect){
			sb.append("SELECT c.����ID,c.Ȩ��ID,��������,������,����ֵ,c.˳�� FROM ");
			sb.append(TableConst.TABLE_MANAGE_PRIV);
			sb.append(" a, ");
			sb.append(TableConst.TABLE_REL_USER_PRIV);
			sb.append(" b, ");
			sb.append(TableConst.TABLE_MANAGE_PRIV_ATTR);
			sb.append(" c WHERE a.Ȩ��ID = b.Ȩ��ID AND b.����ID = c.����ID AND a.Ȩ�޸�ID = '");
			sb.append(privId);
			sb.append("' AND �û�ID = '");
			sb.append(userId);
			sb.append("' UNION SELECT c.����ID,c.Ȩ��ID,��������,������,����ֵ,c.˳�� FROM ");
			sb.append(TableConst.TABLE_MANAGE_PRIV);
			sb.append(" a, ");
			sb.append(TableConst.TABLE_REL_ROLE_PRIV);
			sb.append(" b, ");
			sb.append(TableConst.TABLE_REL_USER_ROLE);
			sb.append(" d, ");
			sb.append(TableConst.TABLE_MANAGE_PRIV_ATTR);
			sb.append(" c WHERE a.Ȩ��ID = b.Ȩ��ID AND b.����ID = c.����ID AND a.Ȩ�޸�ID = '");
			sb.append(privId);
			sb.append("' AND b.��ɫID = d.��ɫID AND �û�ID = '");
			sb.append(userId);
			sb.append("' order by Ȩ��ID ,˳�� asc ");
		}else{
			sb.append("select t.����ID,t.Ȩ��ID,��������,������,����ֵ,t.˳�� from ( select * from ");
			sb.append(TableConst.TABLE_MANAGE_PRIV);
			sb.append(" where Ȩ��ID in ( select distinct Ȩ��ID from ");
			sb.append(TableConst.TABLE_MANAGE_PRIV);
			sb.append(" start with Ȩ��ID in ( select distinct Ȩ��ID from ");
			sb.append(TableConst.TABLE_REL_USER_PRIV);
			sb.append(" where �û�ID = '");
			sb.append(userId);
			sb.append("' union select distinct Ȩ��ID from ");
			sb.append(TableConst.TABLE_REL_ROLE_PRIV);
			sb.append(" a ,");
			sb.append(TableConst.TABLE_REL_USER_ROLE);
			sb.append(" b where a.��ɫID = b.��ɫID AND �û�ID = '");
			sb.append(userId);
			sb.append("' ) connect by Ȩ��ID = prior Ȩ�޸�ID ) start with Ȩ��ID = '");
			sb.append(privId);
			sb.append("' connect by Ȩ�޸�ID = prior Ȩ��ID ) p, ");
			sb.append(TableConst.TABLE_MANAGE_PRIV_ATTR);
			sb.append(" t where p.Ȩ��ID = t.Ȩ��ID ");
		}
		return getPrivAttrList(sb.toString());
	}
	
	
	/**
	 * ��� ��ɫ������Ȩ������
	 * @param roleId
	 * @param privId
	 * @return
	 */
	public List<PrivAttrBean> getRolePrivAttr(String roleId,String privId){
		try {
			String sql = "SELECT * FROM " + TableConst.TABLE_MANAGE_PRIV_ATTR
					+ " a, " + TableConst.TABLE_REL_ROLE_PRIV
					+ " b WHERE a.����ID = b.����ID AND b.��ɫID='" + roleId + "'";
			if (privId != null && !"".equals(privId)) {
				sql += " AND b.Ȩ��ID='" + privId + "'";
			}
			return getPrivAttrList(sql + " ORDER BY a.˳�� ASC");
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<PrivAttrBean>();
		}
	}
	
	/**
	 * ��ɫ��������֯����,��ѡ״̬
	 * @param nodeId
	 * @param userId
	 * @param isDirect true:ֱ����Ȩ��,false �ݹ�������Ȩ��
	 * @return
	 */
	public List<PrivBean> getChildrenPrivRelRole(String nodeId,String roleId,boolean isDirect){
		String sql = "";
		if (isDirect) {
			sql = "select distinct * from " + TableConst.TABLE_MANAGE_PRIV
					+ " where Ȩ�޸�ID = '" + nodeId
					+ "' start with Ȩ��ID in ( select distinct Ȩ��ID from "
					+ TableConst.TABLE_REL_ROLE_PRIV + " where ��ɫID = '"
					+ roleId
					+ "' ) connect by Ȩ��ID = prior Ȩ�޸�ID ORDER BY Ȩ��˳��";
		} else {
			sql = "select * from ( select distinct * from "
					+ TableConst.TABLE_MANAGE_PRIV
					+ " start with Ȩ��ID in ( select distinct Ȩ��ID from "
					+ TableConst.TABLE_REL_ROLE_PRIV + " where ��ɫID = '"
					+ roleId
					+ "' ) connect by Ȩ��ID = prior Ȩ�޸�ID ) start with Ȩ��ID = '"
					+ nodeId + "' connect by Ȩ�޸�ID = prior Ȩ��ID ";
		}
		return getPrivList(sql);
	}

	/**
	 * ����Ȩ������
	 * @param sourcePrivId
	 * @param targetPrivId
	 * @return
	 */
	public int pastePrivAttrs(String sourcePrivId,String targetPrivId){
		String sql = "SELECT * FROM " + TableConst.TABLE_MANAGE_PRIV_ATTR
				+ " WHERE Ȩ��ID = '" + sourcePrivId
				+ "' AND ������ NOT IN ( SELECT ������ FROM "
				+ TableConst.TABLE_MANAGE_PRIV_ATTR + " WHERE Ȩ��ID = '"
				+ targetPrivId + "')";
		List<PrivAttrBean> attrs = DBTemplate.getInstance().getResultRowMapperList(
				sql, new PrivAttrRowMapper());
		String insSql = "INSERT INTO " + TableConst.TABLE_MANAGE_PRIV_ATTR
				+ " (����ID,Ȩ��ID,��������,������,����ֵ,˳��) VALUES(?,?,?,?,?,?)";
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
	 * ���Ȩ����XML
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
	 * ���Ȩ�޽ڵ� �� ��֯���� ������ϵ
	 * @param privId
	 * @param user
	 * @return
	 */
	public List<String> getOrgNamePrivList(String privId,UserBean user){
		List<String> privOrgNameList = null;
		if(privId != null && privId.equals(WebConfigUtil.getOrgPrivNode())){
			// ��֯����
			privOrgNameList = new ArrayList<String>();
			List<PrivAttrBean> orgattrs = getUserPrivAttr(user.getUserId(), WebConfigUtil.getOrgPrivNode());
			for(int a = 0;a<orgattrs.size();a++){
				privOrgNameList.add(orgattrs.get(a).getPrivAttrValue());
			}
		}
		else if(privId != null && privId.equals(WebConfigUtil.getNewsTreeRoot())){
			//����
			privOrgNameList = new ArrayList<String>();
			List<PrivAttrBean> newsattrs = getUserPrivAttr(user.getUserId(), WebConfigUtil.getOrgPrivNode());
			for(int a = 0;a<newsattrs.size();a++){
				privOrgNameList.add(newsattrs.get(a).getPrivAttrValue());
			}
		}
		else if(privId != null && privId.equals(WebConfigUtil.getSourceTreeRoot())){
			//�ĵ�����
			privOrgNameList = new ArrayList<String>();
			List<PrivAttrBean> sourceattrs = getUserPrivAttr(user.getUserId(), WebConfigUtil.getOrgPrivNode());
			for(int a = 0;a<sourceattrs.size();a++){
				privOrgNameList.add(sourceattrs.get(a).getPrivAttrValue());
			}
		}
		else if(privId != null && privId.equals(WebConfigUtil.getLinkTreeRoot())){
			//��վ����
			privOrgNameList = new ArrayList<String>();
			List<PrivAttrBean> linkattrs = getUserPrivAttr(user.getUserId(), WebConfigUtil.getOrgPrivNode());
			for(int a = 0;a<linkattrs.size();a++){
				privOrgNameList.add(linkattrs.get(a).getPrivAttrValue());
			}
		}
		return privOrgNameList;
	}
	
	
	/**
	 * ����������
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
		sbSql.append(" where Ȩ�޸�ID = '");
		sbSql.append(nodeId);
		sbSql.append("' ");
		sbSql.append("start with Ȩ��ID in ( select distinct Ȩ��ID from ( select distinct c.Ȩ��ID from ");
		sbSql.append(TableConst.TABLE_REL_USER_PRIV);
		sbSql.append(" c , ");
		sbSql.append(TableConst.TABLE_MANAGE_PRIV_ATTR);
		sbSql.append(" d where c.����ID = d.����ID and ");
		sbSql.append(attrFilter);
		sbSql.append(" and �û�ID = '");
		sbSql.append(userId);
		sbSql.append("' ");
		sbSql.append("union select distinct a.Ȩ��ID from ");
		sbSql.append(TableConst.TABLE_REL_ROLE_PRIV);
		sbSql.append(" a , ");
		sbSql.append(TableConst.TABLE_REL_USER_ROLE);
		sbSql.append(" b , ");
		sbSql.append(TableConst.TABLE_MANAGE_PRIV_ATTR);
		sbSql.append(" e where a.����ID = e.����ID and a.��ɫID = b.��ɫID  AND ");
		sbSql.append(attrFilter);
		sbSql.append(" AND �û�ID = '");
		sbSql.append(userId);
		sbSql.append("' ) )  connect by Ȩ��ID = prior Ȩ�޸�ID ORDER BY Ȩ��˳�� ");
		
		}else{
			sbSql.append("select * from ");
			sbSql.append(TableConst.TABLE_MANAGE_PRIV);
			sbSql.append(" where Ȩ��ID in ( select distinct Ȩ��ID from ");
			sbSql.append(TableConst.TABLE_MANAGE_PRIV);
			sbSql.append(" start with Ȩ��ID in ( select distinct a.Ȩ��ID from ");
			sbSql.append(TableConst.TABLE_REL_USER_PRIV);
			sbSql.append(" a, ");
			sbSql.append(TableConst.TABLE_MANAGE_PRIV_ATTR);
			sbSql.append(" b  ");
			sbSql.append(" where �û�ID = '");
			sbSql.append(userId);
			sbSql.append( "' and a.����ID=b.����ID and b.������='����Ȩ�� ");
			sbSql.append("' union select distinct a.Ȩ��ID from ");
			sbSql.append(TableConst.TABLE_REL_ROLE_PRIV);
			sbSql.append(" a ,");
			sbSql.append(TableConst.TABLE_REL_USER_ROLE);
			sbSql.append(" b , ");
			sbSql.append(TableConst.TABLE_MANAGE_PRIV_ATTR);
			sbSql.append(" c  ");
			sbSql.append(" where a.��ɫID = b.��ɫID and a.����ID=c.����ID and c.������='����Ȩ��' AND �û�ID = '");
			sbSql.append(userId);
			sbSql.append("' ) connect by Ȩ��ID = prior Ȩ�޸�ID ) start with Ȩ��ID = '");
			sbSql.append(nodeId);
			sbSql.append("' connect by Ȩ�޸�ID = prior Ȩ��ID ");
		}
		
		
		return getPrivList(sbSql.toString());
	}
	/**
	 * 
	 * @param nodeId	Ȩ��ID
	 * @return	��ӦȨ��ID������
	 */
	public String getPrivName(String privId) throws Exception{
		String privName = "";
		String sql = "SELECT Ȩ���� FROM PSIDP.IDP_WEBNEW.�Ż�_Ȩ��_����� WHERE Ȩ��ID='" + privId + "'";
		Map<String, Object> dataMap = DBTemplate.getInstance().getResultMap(sql);
		privName = (null == dataMap.get("Ȩ����") ? "" : (String)dataMap.get("Ȩ����"));
		return privName;
	}
	
}
