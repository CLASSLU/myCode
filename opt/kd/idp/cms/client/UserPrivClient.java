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
	 * �Ƿ��ǹ���Ա
	 * @return
	 */
	public boolean hasAdminRole(){
		List<RoleBean> roleList = ServiceManager.getRoleDao().getRolesFromUser(userId);
		for (int i=0;i<roleList.size();i++){
			if(roleList.get(i).getRoleName().indexOf("����Ա") > -1){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * �޸��û�����
	 * @param newPwd
	 * @return
	 */
	public int updateUserPwd(String newPwd){
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE ");
		sb.append(TableConst.TABLE_MANAGE_USER);
		sb.append(" SET ���� = '");
		sb.append(BeanMd5.MD5HexEncode(newPwd));
		sb.append("' WHERE �û�ID = '");
		sb.append(userId);
		sb.append("'");
		return DBTemplate.getInstance().updateSql(sb.toString());
	}
	
	/**
	 * ����û�Ȩ������
	 * @param privId
	 * @return
	 */
	public List<PrivAttrBean> getPrivAttrs(String privId){
		return ServiceManager.getPrivDao().getUserPrivAttr(userId, privId);
	}
	
	/**
	 * ����û�Ȩ������
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
	 * ����û�Ȩ��
	 * @param privId
	 * @param hasAttrs �Ƿ��������
	 * @return
	 */
	public PrivBean getPriv(String privId,boolean hasAttrs){
		return new PrivBean();
	}
	
	
	/**
	 * ����û�Ȩ�ް�������
	 * @param privId
	 * @param isDirect true ֱ���ӽڵ�,false �ݹ��ӽڵ�
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
	 * ����û�Ȩ�ް�������(��д��ȡȨ�޷�����Ϊ�˽���Ż�ͼ��Ȩ�޻��ҡ���ȷ����)
	 * @param privId
	 * @param isDirect true ֱ���ӽڵ�,false �ݹ��ӽڵ�
	 * @param userId �û�ID(session)
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
	 * ����û�Ӧ����Ȩ�ް�������
	 * @param privId
	 * @param IsPersonSet true ��������,false �Ǹ�������
	 * @return
	 */
	public List<PrivBean> getAppChildrenPrivsWithAttr(){
		List<PrivBean> pblist = null;
		
			
			pblist=ServiceManager.getPrivDao().getAppChildrenPrivRelUser(userId);//���и��Ի����ù���
		
		return pblist;
	}
	
	/**
	 * ����û�Ӧ����Ȩ�ް�������(��д��ȡȨ�޷�����Ϊ�˽���Ż�ͼ��Ȩ�޻��ҡ���ȷ����)
	 * @param userId �û�ID(session) 
	 * @return
	 */
	public List<PrivBean> getAppChildrenPrivsWithAttr(String userId){
		List<PrivBean> pblist = null;
		
			
			pblist=ServiceManager.getPrivDao().getAppChildrenPrivRelUser(userId);//���и��Ի����ù���
		
		return pblist;
	}
	
	
	
	/**
	 * ���Ȩ��Ŀ¼
	 * @param privId
	 * @param isDirect true ֱ���ӽڵ�,false �ݹ��ӽڵ�
	 * @param hasAttrs �Ƿ��������
	 * @return
	 */
	public List<PrivBean> getPrivDirectory(String privId,boolean isDirect,boolean hasAttrs){
		String sql = "select * from " + TableConst.TABLE_MANAGE_PRIV
				+ " start with Ȩ��ID = '" + privId
				+ "' connect by Ȩ�޸�ID = prior Ȩ��ID";
		if (isDirect) {
			sql = "select * from " + TableConst.TABLE_MANAGE_PRIV
					+ " where Ȩ�޸�ID = '" + privId + "' ";
			if(privId.equals(WebConfigUtil.getAppTreeRoot())){
				sql = "select * from " + TableConst.TABLE_MANAGE_PRIV
				+ " where Ȩ�޸�ID = '" + privId + "' order by Ȩ��˳�� ";
			}
		}
		List<PrivBean> privList = DBTemplate.getInstance()
				.getResultRowMapperList(sql, new PrivRowMapper());
		if (hasAttrs) {
			String attrSql = "select b.����ID,b.Ȩ��ID,��������,������,����ֵ,b.˳�� from ( select * from "
					+ TableConst.TABLE_MANAGE_PRIV
					+ " start with Ȩ��ID = '"
					+ WebConfigUtil.getIPTreeRoot()
					+ "' connect by Ȩ�޸�ID = prior Ȩ��ID ) a,"
					+ TableConst.TABLE_MANAGE_PRIV_ATTR
					+ " b where a.Ȩ��ID = b.Ȩ��ID order by b.˳�� asc";
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
				+ " start with Ȩ��ID = '" + privId
				+ "' connect by Ȩ�޸�ID = prior Ȩ��ID";
		if (isDirect) {
			sql = "select * from " + TableConst.TABLE_MANAGE_PRIV
					+ " where Ȩ�޸�ID = '" + privId + "' ";
		}
		List<PrivBean> privList = DBTemplate.getInstance()
				.getResultRowMapperList(sql, new PrivRowMapper());
		if (hasAttrs) {
			String attrSql = "select b.����ID,b.Ȩ��ID,��������,������,����ֵ,b.˳�� from ( select * from "
					+ TableConst.TABLE_MANAGE_PRIV
					+ " start with Ȩ��ID = '"
//					+ WebConfigUtil.getIPTreeRoot()
					+ privId
					+ "' connect by Ȩ�޸�ID = prior Ȩ��ID ) a,"
					+ TableConst.TABLE_MANAGE_PRIV_ATTR
					+ " b where a.Ȩ��ID = b.Ȩ��ID order by b.˳�� asc";
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
	 * ����û��ĵ�λ
	 * @return
	 */
	public OrgBean getCompany(){
		if (user == null) {
			initUser();
		}
		String sql = "select * from " + TableConst.TABLE_MANAGE_ORG
				+ " start with ��֯����ID = '" + user.getUserOrgId()
				+ "' connect by ��֯����ID = prior �ϼ���֯����ID ";
		List<OrgBean> orgList = DBTemplate.getInstance()
				.getResultRowMapperList(sql, new OrgRowMapper());
		for (int i = 0; i < orgList.size(); i++) {
			if ("��λ".equals(orgList.get(i).getType())) {
				return orgList.get(i);
			}
		}
		return orgList.get((orgList.size() > 0)?(orgList.size()-1):orgList.size());
	}
	
}
