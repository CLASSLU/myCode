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
	 * 查询组织机构信息
	 * 
	 * @param nodeId
	 * @return
	 */
	public List<OrgBean> getOrgInfo(String nodeId) {
		String sql = "SELECT * FROM " + TableConst.TABLE_MANAGE_ORG
				+ " WHERE 组织机构ID='" + nodeId + "'";
		return getOrgList(sql);
	}

	/**
	 * 获得不同类型组织机构对应的SQL
	 * 
	 * @param nodeId
	 * @param orgType
	 * @return
	 */
	public String getOrgSqlByType(String nodeId, String orgType) {
		String resultSql = "";
		if ("电网公司".equals(orgType)) {
			resultSql = "select '电网公司' 机构类型,a.*,公司名称, 公司简称, 上级公司, 公司级别, 通信地址, 邮政编码, 联系部门, 传真, 联系电话, 邮箱地址 from "
					+ TableConst.TABLE_MANAGE_ORG
					+ " a left join "
					+ TableConst.TABLE_MANAGE_ORG_MANAGE_COMPANY
					+ " b on a.组织机构ID=b.组织机构ID WHERE a.组织机构ID='" + nodeId + "'";
		} else if ("调度机构".equals(orgType)) {
			resultSql = "SELECT '调度机构' 机构类型,a.*,调度全称, 调度简称, 调度级别, 所属公司, 电网名称, 上级调度, 通信地址, 邮政编码, 联系部门, 传真, 联系电话, 邮箱地址 from "
					+ TableConst.TABLE_MANAGE_ORG
					+ " a left join "
					+ TableConst.TABLE_MANAGE_ORG_MANAGE_ORG
					+ " b on a.组织机构ID=b.组织机构ID WHERE a.组织机构ID='" + nodeId + "'";
		} else if ("调度机构部门".equals(orgType)) {
			resultSql = "SELECT '调度机构部门' 机构类型,a.*,部门名称, 部门简称, 所属调度机构 from "
					+ TableConst.TABLE_MANAGE_ORG + " a left join "
					+ TableConst.TABLE_MANAGE_ORG_MANAGE_DEPART
					+ " b on a.组织机构ID=b.组织机构ID WHERE a.组织机构ID='" + nodeId + "'";
		} else if ("发电公司".equals(orgType)) {
			resultSql = "SELECT '发电公司' 机构类型,a.*,公司全称, 公司简称, 联系部门, 联系人电话_座机, 联系人电话_手机, 联系人邮箱, 传真号码, 通信地址, 邮政编码,备注,联系人 from "
					+ TableConst.TABLE_MANAGE_ORG
					+ " a left join "
					+ TableConst.TABLE_MANAGE_ORG_POWER_COMPANY
					+ " b on a.组织机构ID=b.组织机构ID WHERE a.组织机构ID='" + nodeId + "'";
		} else if ("发电厂".equals(orgType)) {
			resultSql = "SELECT '发电厂' 机构类型,a.*,电厂全称, 电厂简称, 上级发电公司, 电厂类型, 联系部门, 联系人, 联系人电话_座机, 联系人电话_手机, 联系人邮箱, 传真号码, 传真核对电话, 通信地址, 邮政编码, 备注 from "
					+ TableConst.TABLE_MANAGE_ORG
					+ " a left join "
					+ TableConst.TABLE_MANAGE_ORG_POWER_STATION
					+ " b on a.组织机构ID=b.组织机构ID WHERE a.组织机构ID='" + nodeId + "'";
		} else if ("其他机构".equals(orgType)) {
			resultSql = "SELECT '其他机构' 机构类型,a.*,机构名称, 机构简称, 所属公司, 上级机构, 通信地址, 邮政编码, 联系部门, 传真, 联系电话, 邮箱地址 from "
					+ TableConst.TABLE_MANAGE_ORG
					+ " a left join "
					+ TableConst.TABLE_MANAGE_ORG_OTHER
					+ " b on a.组织机构ID=b.组织机构ID WHERE a.组织机构ID='" + nodeId + "'";
		} else {
			resultSql = "SELECT '基本机构' 机构类型,* FROM " + TableConst.TABLE_MANAGE_ORG
					+ " WHERE 组织机构ID='" + nodeId + "'";
		}
		return resultSql;
	}

	/**
	 * 查询子组织机构信息
	 * 
	 * @param nodeId
	 * @param isDirect
	 *            true 直接子节点 false 递归全部子节点
	 * @param privOrgNameList
	 *            用户权限关联的子节点ID（直接子节点时起作用）
	 * @return
	 */
	public List<OrgBean> getChildrenOrgInfo(String nodeId, boolean isDirect,
			List<String> privOrgNameList) {
		StringBuffer sbSql = new StringBuffer();
		if (isDirect) {
			sbSql.append("SELECT * FROM ");
			sbSql.append(TableConst.TABLE_MANAGE_ORG);
			sbSql.append(" WHERE 上级组织机构ID='");
			sbSql.append(nodeId);
			sbSql.append("'");
			if (privOrgNameList != null && privOrgNameList.size() > 0) {
				sbSql.append(" AND 组织机构名 in ( ");
				for (int i = 0; i < privOrgNameList.size(); i++) {
					sbSql.append("'" + privOrgNameList.get(i) + "'");
					if (i < privOrgNameList.size() - 1) {
						sbSql.append(",");
					}
				}
				sbSql.append(" )");
			}
			sbSql.append(" ORDER BY 排序 asc ");
		} else {
			sbSql.append("SELECT * FROM ");
			sbSql.append(TableConst.TABLE_MANAGE_ORG);
			sbSql.append(" START WITH 上级组织机构ID = '");
			sbSql.append(nodeId);
			sbSql.append("' CONNECT BY 上级组织机构ID = PRIOR 组织机构ID");
		}
		return getOrgList(sbSql.toString());
	}

	/**
	 * 角色关联子组织机构,勾选状态
	 * 
	 * @param nodeId
	 * @param userId
	 * @return
	 */
	public List<OrgBean> getChildrenOrgRelRole(String nodeId, String userId) {
		String sql = "select distinct * from " + TableConst.TABLE_MANAGE_ORG
				+ " where 上级组织机构ID = '" + nodeId
				+ "' start with 组织机构ID in ( select distinct 所属组织机构 from "
				+ TableConst.TABLE_MANAGE_ROLE + " a,"
				+ TableConst.TABLE_REL_USER_ROLE
				+ " b where a.角色ID = b.角色ID and b.用户ID='" + userId
				+ "' ) connect by 组织机构ID = prior 上级组织机构ID ";
		return getOrgList(sql);
	}

	/**
	 * 用户关联子组织机构,勾选状态
	 * 
	 * @param nodeId
	 * @param roleId
	 * @return
	 */
	public List<OrgBean> getChildrenOrgRelUser(String nodeId, String roleId) {
		String sql = "select distinct * from " + TableConst.TABLE_MANAGE_ORG
				+ " where 上级组织机构ID = '" + nodeId
				+ "' start with 组织机构ID in ( select distinct 组织机构ID from "
				+ TableConst.TABLE_MANAGE_USER + " a,"
				+ TableConst.TABLE_REL_USER_ROLE
				+ " b where a.用户ID = b.用户ID and b.角色ID='" + roleId
				+ "' ) connect by 组织机构ID = prior 上级组织机构ID ";
		return getOrgList(sql);
	}

	/**
	 * 添加组织机构
	 * 
	 * @param org
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { RuntimeException.class })
	public String addNewOrg(OrgBean org) {
		String orgId = CommonTools.createId("ORG");
		org.setOrgId(orgId);
		if(org.getType().endsWith("部门")){
			org.setDetailType("部门");
		}else{
			org.setDetailType("单位");
		}
		String sql = "INSERT INTO " + TableConst.TABLE_MANAGE_ORG
				+ " (组织机构ID,组织机构名,上级组织机构ID,排序,类型,简称,全称,地理位置,是否有设备,组织机构类型)"
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
	 * 添加组织机构详细信息
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
					+ "(公司名称,公司简称,上级公司,公司级别,通信地址,邮政编码,联系部门,传真,联系电话,邮箱地址,组织机构ID) "
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
					+ "(调度全称,调度简称,调度级别,所属公司,电网名称,上级调度,通信地址,邮政编码,联系部门,传真,联系电话,邮箱地址,组织机构ID)"
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
					+ "(部门名称,部门简称,所属调度机构,组织机构ID)" 
					+ " VALUES(?,?,?,?)";
			param = new String[] { childOrg.getTrueName(),
					childOrg.getSortName(), childOrg.getOwnerCompanyName(),
					childOrg.getOrgId() };
		} else if (org instanceof PowerCompanyOrgBean) {
			PowerCompanyOrgBean childOrg = (PowerCompanyOrgBean) org;
			paramSql = "INSERT INTO " + TableConst.TABLE_MANAGE_ORG_POWER_COMPANY
					+ " (公司全称,公司简称,联系部门,联系人电话_座机,联系人电话_手机,联系人邮箱,传真号码,通信地址,邮政编码,备注,联系人,组织机构ID)"
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
					+ "(电厂全称,电厂简称,上级发电公司,电厂类型,联系部门,联系人,联系人电话_座机,联系人电话_手机,联系人邮箱,传真号码,传真核对电话,通信地址,邮政编码,备注,组织机构ID)"
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
					+ "(机构名称,机构简称,所属公司,上级机构,通信地址,邮政编码,联系部门,传真,联系电话,邮箱地址,组织机构ID)"
					+ "  VALUES(?,?,?,?,?,?,?,?,?,?,?)";
			param = new String[] { childOrg.getTrueName(),
					childOrg.getSortName(), childOrg.getOwnerCompanyName(),
					childOrg.getParentOrgName(), childOrg.getAddress(),
					childOrg.getZipCode(), childOrg.getContactDepartment(),
					childOrg.getFaxNumber(), childOrg.getContactPhone(),
					childOrg.getContactEmail(),  org.getOrgId() };
		}
		String[] deleteInfo = {" delete "+ TableConst.TABLE_MANAGE_ORG_MANAGE_COMPANY+" where 组织机构ID='"+org.getOrgId()+"'"
				," delete "+ TableConst.TABLE_MANAGE_ORG_MANAGE_ORG+" where 组织机构ID='"+org.getOrgId()+"'"
				," delete "+ TableConst.TABLE_MANAGE_ORG_MANAGE_DEPART+" where 组织机构ID='"+org.getOrgId()+"'"
				," delete "+ TableConst.TABLE_MANAGE_ORG_POWER_COMPANY+" where 组织机构ID='"+org.getOrgId()+"'"
				," delete "+ TableConst.TABLE_MANAGE_ORG_POWER_STATION+" where 组织机构ID='"+org.getOrgId()+"'"
				," delete "+ TableConst.TABLE_MANAGE_ORG_OTHER+" where 组织机构ID='"+org.getOrgId()+"'"};
		if (paramSql != null){
			DBTemplate.getInstance().batchUpdateSql(deleteInfo);
			DBTemplate.getInstance().updateSqlWithParam(paramSql, param);
		}
		return org.getOrgId();
	}
	/**
	 * 修改组织机构信息
	 * 
	 * @param org
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { RuntimeException.class })
	public String modOrg(OrgBean org) {
		if(org.getType().endsWith("部门")){
			org.setDetailType("部门");
		}else{
			org.setDetailType("单位");
		}
		String sql = "UPDATE " + TableConst.TABLE_MANAGE_ORG + " SET 组织机构名='"
				+ org.getOrgName() + "',上级组织机构ID='" + org.getForgId() + "',排序="
				+ org.getOrder() + ",类型='" + org.getType() + "',简称='"
				+ org.getSortName() + "',全称='" + org.getTrueName() + "',地理位置='"
				+ org.getLocation() + "',是否有设备=" + org.getHasEqu() + ",组织机构类型='" + org.getDetailType()
				+ "' WHERE 组织机构ID='" + org.getOrgId() + "'";
		DBTemplate.getInstance().updateSql(sql);
		addNewOrgDetail(org);
		return org.getOrgId();
	}

	/**
	 * 删除组织机构
	 * 
	 * @param org
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { RuntimeException.class })
	public String deleteOrg(OrgBean org) {
		String subSql = "select 组织机构ID from " + TableConst.TABLE_MANAGE_ORG
				+ " start with 组织机构ID = '" + org.getOrgId()
				+ "' connect by 上级组织机构ID = prior 组织机构ID ";
		String sql = "UPDATE " + TableConst.TABLE_MANAGE_USER
				+ " SET 组织机构ID = '" + WebConfigUtil.UNINITIALIZE
				+ "' WHERE 组织机构ID in ( " + subSql + " )";
		DBTemplate.getInstance().updateSql(sql);
		sql = "UPDATE " + TableConst.TABLE_MANAGE_ROLE + " SET 所属组织机构 = '"
				+ WebConfigUtil.UNINITIALIZE + "' WHERE 所属组织机构 in ( " + subSql
				+ " )";
		DBTemplate.getInstance().updateSql(sql);
		sql = "DELETE FROM " + TableConst.TABLE_MANAGE_ORG
				+ " WHERE 组织机构ID in ( " + subSql + " )";
		String paramSql = null;
		if (org instanceof ManageCompanyOrgBean) {
			paramSql = "DELETE FROM "
					+ TableConst.TABLE_MANAGE_ORG_MANAGE_COMPANY
					+ " WHERE 组织机构ID in ( " + subSql + " )";
		} else if (org instanceof ManageCompanyOrgBean) {
			paramSql = "DELETE FROM " + TableConst.TABLE_MANAGE_ORG_MANAGE_ORG
					+ " WHERE 组织机构ID in ( " + subSql + " )";
		} else if (org instanceof ManageCompanyOrgBean) {
			paramSql = "DELETE FROM "
					+ TableConst.TABLE_MANAGE_ORG_MANAGE_DEPART
					+ " WHERE 组织机构ID in ( " + subSql + " )";
		} else if (org instanceof PowerCompanyOrgBean) {
			paramSql = "DELETE FROM "
					+ TableConst.TABLE_MANAGE_ORG_POWER_COMPANY
					+ " WHERE 组织机构ID in ( " + subSql + " )";
		} else if (org instanceof PowerOrgBean) {
			paramSql = "DELETE FROM "
					+ TableConst.TABLE_MANAGE_ORG_POWER_STATION
					+ " WHERE 组织机构ID in ( " + subSql + " )";
		} else if (org instanceof OtherOrgBean) {
			paramSql = "DELETE FROM " + TableConst.TABLE_MANAGE_ORG_OTHER
					+ " WHERE 组织机构ID in ( " + subSql + " )";
		}
		DBTemplate.getInstance().updateSql(sql);
		if (paramSql != null)
			DBTemplate.getInstance().updateSql(paramSql);
		return org.getOrgId();
	}

	/**
	 * 获得组织机构XML
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
