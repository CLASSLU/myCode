package kd.idp.cms.dao;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kd.idp.cms.bean.priv.ManageCompanyOrgBean;
import kd.idp.cms.bean.priv.ManageDepartOrgBean;
import kd.idp.cms.bean.priv.ManageOrgBean;
import kd.idp.cms.bean.priv.OrgBean;
import kd.idp.cms.bean.priv.OtherOrgBean;
import kd.idp.cms.bean.priv.PowerCompanyOrgBean;
import kd.idp.cms.bean.priv.PowerOrgBean;
import kd.idp.cms.mapper.priv.OrgRowMapper;
import kd.idp.common.CommonTools;
import kd.idp.common.config.WebConfigUtil;
import kd.idp.common.consts.TableConst;

import com.spring.dbservice.DBTemplate;

@Transactional
public class OrgDao{

	/**
	 * ��ѯ��֯������Ϣ
	 * 
	 * @param nodeId
	 * @return
	 */
	public List<OrgBean> getOrgInfo(String nodeId) {
		String sql = "SELECT * FROM " + TableConst.TABLE_MANAGE_ORG
				+ " WHERE ��֯����ID='" + nodeId + "'";
		return getOrgList(sql);
	}

	/**
	 * ��ò�ͬ������֯������Ӧ��SQL
	 * 
	 * @param nodeId
	 * @param orgType
	 * @return
	 */
	public String getOrgSqlByType(String nodeId, String orgType) {
		String resultSql = "";
		if ("������˾".equals(orgType)) {
			resultSql = "select '������˾' ��������,a.*,��˾����, ��˾���, �ϼ���˾, ��˾����, ͨ�ŵ�ַ, ��������, ��ϵ����, ����, ��ϵ�绰, �����ַ from "
					+ TableConst.TABLE_MANAGE_ORG
					+ " a left join "
					+ TableConst.TABLE_MANAGE_ORG_MANAGE_COMPANY
					+ " b on a.��֯����ID=b.��֯����ID WHERE a.��֯����ID='" + nodeId + "'";
		} else if ("���Ȼ���".equals(orgType)) {
			resultSql = "SELECT '���Ȼ���' ��������,a.*,����ȫ��, ���ȼ��, ���ȼ���, ������˾, ��������, �ϼ�����, ͨ�ŵ�ַ, ��������, ��ϵ����, ����, ��ϵ�绰, �����ַ from "
					+ TableConst.TABLE_MANAGE_ORG
					+ " a left join "
					+ TableConst.TABLE_MANAGE_ORG_MANAGE_ORG
					+ " b on a.��֯����ID=b.��֯����ID WHERE a.��֯����ID='" + nodeId + "'";
		} else if ("���Ȼ�������".equals(orgType)) {
			resultSql = "SELECT '���Ȼ�������' ��������,a.*,��������, ���ż��, �������Ȼ��� from "
					+ TableConst.TABLE_MANAGE_ORG + " a left join "
					+ TableConst.TABLE_MANAGE_ORG_MANAGE_DEPART
					+ " b on a.��֯����ID=b.��֯����ID WHERE a.��֯����ID='" + nodeId + "'";
		} else if ("���繫˾".equals(orgType)) {
			resultSql = "SELECT '���繫˾' ��������,a.*,��˾ȫ��, ��˾���, ��ϵ����, ��ϵ�˵绰_����, ��ϵ�˵绰_�ֻ�, ��ϵ������, �������, ͨ�ŵ�ַ, ��������,��ע,��ϵ�� from "
					+ TableConst.TABLE_MANAGE_ORG
					+ " a left join "
					+ TableConst.TABLE_MANAGE_ORG_POWER_COMPANY
					+ " b on a.��֯����ID=b.��֯����ID WHERE a.��֯����ID='" + nodeId + "'";
		} else if ("���糧".equals(orgType)) {
			resultSql = "SELECT '���糧' ��������,a.*,�糧ȫ��, �糧���, �ϼ����繫˾, �糧����, ��ϵ����, ��ϵ��, ��ϵ�˵绰_����, ��ϵ�˵绰_�ֻ�, ��ϵ������, �������, ����˶Ե绰, ͨ�ŵ�ַ, ��������, ��ע from "
					+ TableConst.TABLE_MANAGE_ORG
					+ " a left join "
					+ TableConst.TABLE_MANAGE_ORG_POWER_STATION
					+ " b on a.��֯����ID=b.��֯����ID WHERE a.��֯����ID='" + nodeId + "'";
		} else if ("��������".equals(orgType)) {
			resultSql = "SELECT '��������' ��������,a.*,��������, �������, ������˾, �ϼ�����, ͨ�ŵ�ַ, ��������, ��ϵ����, ����, ��ϵ�绰, �����ַ from "
					+ TableConst.TABLE_MANAGE_ORG
					+ " a left join "
					+ TableConst.TABLE_MANAGE_ORG_OTHER
					+ " b on a.��֯����ID=b.��֯����ID WHERE a.��֯����ID='" + nodeId + "'";
		} else {
			resultSql = "SELECT '��������' ��������,* FROM " + TableConst.TABLE_MANAGE_ORG
					+ " WHERE ��֯����ID='" + nodeId + "'";
		}
		return resultSql;
	}

	/**
	 * ��ѯ����֯������Ϣ
	 * 
	 * @param nodeId
	 * @param isDirect
	 *            true ֱ���ӽڵ� false �ݹ�ȫ���ӽڵ�
	 * @param privOrgNameList
	 *            �û�Ȩ�޹������ӽڵ�ID��ֱ���ӽڵ�ʱ�����ã�
	 * @return
	 */
	public List<OrgBean> getChildrenOrgInfo(String nodeId, boolean isDirect,
			List<String> privOrgNameList) {
		StringBuffer sbSql = new StringBuffer();
		if (isDirect) {
			sbSql.append("SELECT * FROM ");
			sbSql.append(TableConst.TABLE_MANAGE_ORG);
			sbSql.append(" WHERE �ϼ���֯����ID='");
			sbSql.append(nodeId);
			sbSql.append("'");
			if (privOrgNameList != null && privOrgNameList.size() > 0) {
				sbSql.append(" AND ��֯������ in ( ");
				for (int i = 0; i < privOrgNameList.size(); i++) {
					sbSql.append("'" + privOrgNameList.get(i) + "'");
					if (i < privOrgNameList.size() - 1) {
						sbSql.append(",");
					}
				}
				sbSql.append(" )");
			}
			sbSql.append(" ORDER BY ���� asc ");
		} else {
			sbSql.append("SELECT * FROM ");
			sbSql.append(TableConst.TABLE_MANAGE_ORG);
			sbSql.append(" START WITH �ϼ���֯����ID = '");
			sbSql.append(nodeId);
			sbSql.append("' CONNECT BY �ϼ���֯����ID = PRIOR ��֯����ID");
		}
		return getOrgList(sbSql.toString());
	}

	/**
	 * ��ɫ��������֯����,��ѡ״̬
	 * 
	 * @param nodeId
	 * @param userId
	 * @return
	 */
	public List<OrgBean> getChildrenOrgRelRole(String nodeId, String userId) {
		String sql = "select distinct * from " + TableConst.TABLE_MANAGE_ORG
				+ " where �ϼ���֯����ID = '" + nodeId
				+ "' start with ��֯����ID in ( select distinct ������֯���� from "
				+ TableConst.TABLE_MANAGE_ROLE + " a,"
				+ TableConst.TABLE_REL_USER_ROLE
				+ " b where a.��ɫID = b.��ɫID and b.�û�ID='" + userId
				+ "' ) connect by ��֯����ID = prior �ϼ���֯����ID ";
		return getOrgList(sql);
	}

	/**
	 * �û���������֯����,��ѡ״̬
	 * 
	 * @param nodeId
	 * @param roleId
	 * @return
	 */
	public List<OrgBean> getChildrenOrgRelUser(String nodeId, String roleId) {
		String sql = "select distinct * from " + TableConst.TABLE_MANAGE_ORG
				+ " where �ϼ���֯����ID = '" + nodeId
				+ "' start with ��֯����ID in ( select distinct ��֯����ID from "
				+ TableConst.TABLE_MANAGE_USER + " a,"
				+ TableConst.TABLE_REL_USER_ROLE
				+ " b where a.�û�ID = b.�û�ID and b.��ɫID='" + roleId
				+ "' ) connect by ��֯����ID = prior �ϼ���֯����ID ";
		return getOrgList(sql);
	}

	/**
	 * �����֯����
	 * 
	 * @param org
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { RuntimeException.class })
	public String addNewOrg(OrgBean org) {
		String orgId = CommonTools.createId("ORG");
		org.setOrgId(orgId);
		if(org.getType().endsWith("����")){
			org.setDetailType("����");
		}else{
			org.setDetailType("��λ");
		}
		String sql = "INSERT INTO " + TableConst.TABLE_MANAGE_ORG
				+ " (��֯����ID,��֯������,�ϼ���֯����ID,����,����,���,ȫ��,����λ��,�Ƿ����豸,��֯��������)"
				+ " VALUES('" + orgId + "','" + org.getOrgName() + "','"
				+ org.getForgId() + "'," + org.getOrder() + ",'"
				+ org.getType() + "'," + "'" + org.getSortName() + "','"
				+ org.getTrueName() + "','" + org.getLocation() + "',"
				+ org.getHasEqu() + ",'" + org.getDetailType() + "')";
		
		DBTemplate.getInstance().updateSql(sql);
		addNewOrgDetail(org);
		return orgId;
	}
	/**
	 * �����֯������ϸ��Ϣ
	 * 
	 * @param org
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { RuntimeException.class })
	public String addNewOrgDetail(OrgBean org){
		String paramSql = null;
		String[] param = null;
		if (org instanceof ManageCompanyOrgBean) {
			ManageCompanyOrgBean childOrg = (ManageCompanyOrgBean) org;
			paramSql = "INSERT INTO  " + TableConst.TABLE_MANAGE_ORG_MANAGE_COMPANY
					+ "(��˾����,��˾���,�ϼ���˾,��˾����,ͨ�ŵ�ַ,��������,��ϵ����,����,��ϵ�绰,�����ַ,��֯����ID) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
			param = new String[] { childOrg.getTrueName(),
					childOrg.getSortName(), childOrg.getParentCompanyName(),
					childOrg.getCompanyLevel(), childOrg.getAddress(),
					childOrg.getZipCode(), childOrg.getContactDepartment(),
					childOrg.getFaxNumber(), childOrg.getContactMobile(),
					childOrg.getContactEmail(),  org.getOrgId() };
		} else if (org instanceof ManageOrgBean) {
			ManageOrgBean childOrg = (ManageOrgBean) org;
			paramSql = "INSERT INTO  " + TableConst.TABLE_MANAGE_ORG_MANAGE_ORG
					+ "(����ȫ��,���ȼ��,���ȼ���,������˾,��������,�ϼ�����,ͨ�ŵ�ַ,��������,��ϵ����,����,��ϵ�绰,�����ַ,��֯����ID)"
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			param = new String[] { childOrg.getTrueName(),
					childOrg.getSortName(), childOrg.getOrgLevel(),
					childOrg.getOwnerCompanyName(),
					childOrg.getPowerGridName(), childOrg.getParentOrgName(),
					childOrg.getAddress(), childOrg.getZipCode(),childOrg.getContactDepartment(),
					childOrg.getFaxNumber(), childOrg.getContactPhone(),
					childOrg.getContactEmail(), org.getOrgId() };
		} else if (org instanceof ManageDepartOrgBean) {
			ManageDepartOrgBean childOrg = (ManageDepartOrgBean) org;
			paramSql = "INSERT INTO " + TableConst.TABLE_MANAGE_ORG_MANAGE_DEPART
					+ "(��������,���ż��,�������Ȼ���,��֯����ID)" 
					+ " VALUES(?,?,?,?)";
			param = new String[] { childOrg.getTrueName(),
					childOrg.getSortName(), childOrg.getOwnerCompanyName(),
					childOrg.getOrgId() };
		} else if (org instanceof PowerCompanyOrgBean) {
			PowerCompanyOrgBean childOrg = (PowerCompanyOrgBean) org;
			paramSql = "INSERT INTO " + TableConst.TABLE_MANAGE_ORG_POWER_COMPANY
					+ " (��˾ȫ��,��˾���,��ϵ����,��ϵ�˵绰_����,��ϵ�˵绰_�ֻ�,��ϵ������,�������,ͨ�ŵ�ַ,��������,��ע,��ϵ��,��֯����ID)"
					+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
			param = new String[] { childOrg.getTrueName(),
					childOrg.getSortName(), childOrg.getContactDepartment(),
					childOrg.getContactPhone(), childOrg.getContactMobile(),
					childOrg.getContactEmail(), childOrg.getFaxNumber(),
					childOrg.getAddress(), childOrg.getZipCode(),
					childOrg.getComment(),childOrg.getContactPerson(), org.getOrgId() };
		} else if (org instanceof PowerOrgBean) {
			PowerOrgBean childOrg = (PowerOrgBean) org;
			paramSql = "INSERT INTO " + TableConst.TABLE_MANAGE_ORG_POWER_STATION
					+ "(�糧ȫ��,�糧���,�ϼ����繫˾,�糧����,��ϵ����,��ϵ��,��ϵ�˵绰_����,��ϵ�˵绰_�ֻ�,��ϵ������,�������,����˶Ե绰,ͨ�ŵ�ַ,��������,��ע,��֯����ID)"
					+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			param = new String[] { childOrg.getTrueName(),
					childOrg.getSortName(), childOrg.getOwnerCompanyName(),
					childOrg.getStationType(), childOrg.getContactDepartment(),
					childOrg.getContactPerson(), childOrg.getContactPhone(),
					childOrg.getContactMobile(), childOrg.getContactEmail(),
					childOrg.getFaxNumber(), childOrg.getFaxCheckPhone(),
					childOrg.getAddress(), childOrg.getZipCode(),
					childOrg.getComment(), org.getOrgId()};
		} else if (org instanceof OtherOrgBean) {
			OtherOrgBean childOrg = (OtherOrgBean) org;
			paramSql = "INSERT INTO " + TableConst.TABLE_MANAGE_ORG_OTHER
					+ "(��������,�������,������˾,�ϼ�����,ͨ�ŵ�ַ,��������,��ϵ����,����,��ϵ�绰,�����ַ,��֯����ID)"
					+ "  VALUES(?,?,?,?,?,?,?,?,?,?,?)";
			param = new String[] { childOrg.getTrueName(),
					childOrg.getSortName(), childOrg.getOwnerCompanyName(),
					childOrg.getParentOrgName(), childOrg.getAddress(),
					childOrg.getZipCode(), childOrg.getContactDepartment(),
					childOrg.getFaxNumber(), childOrg.getContactPhone(),
					childOrg.getContactEmail(),  org.getOrgId() };
		}
		String[] deleteInfo = {" delete "+ TableConst.TABLE_MANAGE_ORG_MANAGE_COMPANY+" where ��֯����ID='"+org.getOrgId()+"'"
				," delete "+ TableConst.TABLE_MANAGE_ORG_MANAGE_ORG+" where ��֯����ID='"+org.getOrgId()+"'"
				," delete "+ TableConst.TABLE_MANAGE_ORG_MANAGE_DEPART+" where ��֯����ID='"+org.getOrgId()+"'"
				," delete "+ TableConst.TABLE_MANAGE_ORG_POWER_COMPANY+" where ��֯����ID='"+org.getOrgId()+"'"
				," delete "+ TableConst.TABLE_MANAGE_ORG_POWER_STATION+" where ��֯����ID='"+org.getOrgId()+"'"
				," delete "+ TableConst.TABLE_MANAGE_ORG_OTHER+" where ��֯����ID='"+org.getOrgId()+"'"};
		if (paramSql != null){
			DBTemplate.getInstance().batchUpdateSql(deleteInfo);
			DBTemplate.getInstance().updateSqlWithParam(paramSql, param);
		}
		return org.getOrgId();
	}
	/**
	 * �޸���֯������Ϣ
	 * 
	 * @param org
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { RuntimeException.class })
	public String modOrg(OrgBean org) {
		if(org.getType().endsWith("����")){
			org.setDetailType("����");
		}else{
			org.setDetailType("��λ");
		}
		String sql = "UPDATE " + TableConst.TABLE_MANAGE_ORG + " SET ��֯������='"
				+ org.getOrgName() + "',�ϼ���֯����ID='" + org.getForgId() + "',����="
				+ org.getOrder() + ",����='" + org.getType() + "',���='"
				+ org.getSortName() + "',ȫ��='" + org.getTrueName() + "',����λ��='"
				+ org.getLocation() + "',�Ƿ����豸=" + org.getHasEqu() + ",��֯��������='" + org.getDetailType()
				+ "' WHERE ��֯����ID='" + org.getOrgId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		addNewOrgDetail(org);
		return org.getOrgId();
	}

	/**
	 * ɾ����֯����
	 * 
	 * @param org
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { RuntimeException.class })
	public String deleteOrg(OrgBean org) {
		String subSql = "select ��֯����ID from " + TableConst.TABLE_MANAGE_ORG
				+ " start with ��֯����ID = '" + org.getOrgId()
				+ "' connect by �ϼ���֯����ID = prior ��֯����ID ";
		String sql = "UPDATE " + TableConst.TABLE_MANAGE_USER
				+ " SET ��֯����ID = '" + WebConfigUtil.UNINITIALIZE
				+ "' WHERE ��֯����ID in ( " + subSql + " )";
		DBTemplate.getInstance().updateSql(sql);
		sql = "UPDATE " + TableConst.TABLE_MANAGE_ROLE + " SET ������֯���� = '"
				+ WebConfigUtil.UNINITIALIZE + "' WHERE ������֯���� in ( " + subSql
				+ " )";
		DBTemplate.getInstance().updateSql(sql);
		sql = "DELETE FROM " + TableConst.TABLE_MANAGE_ORG
				+ " WHERE ��֯����ID in ( " + subSql + " )";
		String paramSql = null;
		if (org instanceof ManageCompanyOrgBean) {
			paramSql = "DELETE FROM "
					+ TableConst.TABLE_MANAGE_ORG_MANAGE_COMPANY
					+ " WHERE ��֯����ID in ( " + subSql + " )";
		} else if (org instanceof ManageCompanyOrgBean) {
			paramSql = "DELETE FROM " + TableConst.TABLE_MANAGE_ORG_MANAGE_ORG
					+ " WHERE ��֯����ID in ( " + subSql + " )";
		} else if (org instanceof ManageCompanyOrgBean) {
			paramSql = "DELETE FROM "
					+ TableConst.TABLE_MANAGE_ORG_MANAGE_DEPART
					+ " WHERE ��֯����ID in ( " + subSql + " )";
		} else if (org instanceof PowerCompanyOrgBean) {
			paramSql = "DELETE FROM "
					+ TableConst.TABLE_MANAGE_ORG_POWER_COMPANY
					+ " WHERE ��֯����ID in ( " + subSql + " )";
		} else if (org instanceof PowerOrgBean) {
			paramSql = "DELETE FROM "
					+ TableConst.TABLE_MANAGE_ORG_POWER_STATION
					+ " WHERE ��֯����ID in ( " + subSql + " )";
		} else if (org instanceof OtherOrgBean) {
			paramSql = "DELETE FROM " + TableConst.TABLE_MANAGE_ORG_OTHER
					+ " WHERE ��֯����ID in ( " + subSql + " )";
		}
		DBTemplate.getInstance().updateSql(sql);
		if (paramSql != null)
			DBTemplate.getInstance().updateSql(paramSql);
		return org.getOrgId();
	}

	/**
	 * �����֯����XML
	 * 
	 * @param orgList
	 * @return
	 */
	public String getOrgXML(List<OrgBean> orgList) {
		String xmlStr = "";
		try {
			if (orgList.size() > 0) {
				Document doc = DocumentHelper.createDocument();
				doc.setXMLEncoding("GBK");
				Element root = doc.addElement("node");
				root.addAttribute("nodeId", orgList.get(0).getOrgId());
				root.addAttribute("label", orgList.get(0).getOrgName());
				CreateOrgXmlNode(orgList, root, 1);
				xmlStr = doc.asXML();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xmlStr;
	}

	private void CreateOrgXmlNode(List<OrgBean> orgList, Element el, int index) {
		try {
			if (index < orgList.size()) {
				OrgBean org = orgList.get(index);
				if (org.getForgId().equals(el.attributeValue("nodeId"))) {
					Element sel = el.addElement("node");
					sel.addAttribute("nodeId", org.getOrgId());
					sel.addAttribute("label", org.getOrgName());
					sel.addAttribute("parentId", org.getForgId());
					sel.addAttribute("type", org.getType());
					index++;
					CreateOrgXmlNode(orgList, sel, index);
				} else {
					Element sel = el.getParent();
					CreateOrgXmlNode(orgList, sel, index);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param sql
	 * @return
	 */
	public List<OrgBean> getOrgList(String sql) {
		return DBTemplate.getInstance().getResultRowMapperList(sql, new OrgRowMapper());
	}

}
