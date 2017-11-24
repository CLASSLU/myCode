package kd.idp.cms.mapper.priv;

import java.sql.ResultSet;
import java.sql.SQLException;

import kd.idp.cms.bean.priv.ManageCompanyOrgBean;
import kd.idp.cms.bean.priv.ManageDepartOrgBean;
import kd.idp.cms.bean.priv.ManageOrgBean;
import kd.idp.cms.bean.priv.OrgBean;
import kd.idp.cms.bean.priv.OtherOrgBean;
import kd.idp.cms.bean.priv.PowerCompanyOrgBean;
import kd.idp.cms.bean.priv.PowerOrgBean;

import org.springframework.jdbc.core.RowMapper;

public class OrgRowMapper implements RowMapper<OrgBean>{

	public OrgBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		OrgBean org = null;
		try {
			String orgType = NullToString(rs.getString("机构类型"));
			if ("电网公司".equals(orgType)) {
				org = getManageCompanyOrgBean(rs);
			} else if ("调度机构".equals(orgType)) {
				org = getManageOrgBean(rs);
			} else if ("调度机构部门".equals(orgType)) {
				org = getManageDepartOrgBean(rs);
			} else if ("发电公司".equals(orgType)) {
				org = getPowerCompanyOrgBean(rs);
			} else if ("发电厂".equals(orgType)) {
				org = getPowerOrgBean(rs);
			} else if ("其他机构".equals(orgType)) {
				org = getOtherOrgBean(rs);
			} else {
				org = getCommonOrgBean(rs);
			}
		} catch (Exception e) {
			return getCommonOrgBean(rs);
		}
		return org;
	}
	
	/**
	 * 电网公司
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private ManageCompanyOrgBean getManageCompanyOrgBean(ResultSet rs) throws SQLException {
		ManageCompanyOrgBean org = new ManageCompanyOrgBean();
		org.setOrgId(rs.getString("组织机构ID"));
		org.setOrgName(rs.getString("组织机构名"));
		org.setForgId(rs.getString("上级组织机构ID"));
		org.setOrder(rs.getInt("排序"));
		org.setType(NullToString(rs.getString("类型")));
		org.setSortName(NullToString(rs.getString("简称")));
		org.setTrueName(NullToString(rs.getString("全称")));
		org.setLocation(NullToString(rs.getString("地理位置")));
		org.setHasEqu(rs.getInt("是否有设备"));
		org.setParentCompanyName(NullToString(rs.getString("上级公司")));
		org.setCompanyLevel(NullToString(rs.getString("公司级别")));
		org.setAddress(NullToString(rs.getString("通信地址")));
		org.setZipCode(NullToString(rs.getString("邮政编码")));
		org.setFaxNumber(NullToString(rs.getString("传真")));
		org.setContactMobile(NullToString(rs.getString("联系电话")));
		org.setContactDepartment(NullToString(rs.getString("联系部门")));
		org.setContactEmail(NullToString(rs.getString("邮箱地址")));
		return org;
	}
	/**
	 * 调度机构
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private ManageOrgBean getManageOrgBean(ResultSet rs) throws SQLException {
		ManageOrgBean org = new ManageOrgBean();
		org.setOrgId(rs.getString("组织机构ID"));
		org.setOrgName(rs.getString("组织机构名"));
		org.setForgId(rs.getString("上级组织机构ID"));
		org.setOrder(rs.getInt("排序"));
		org.setType(NullToString(rs.getString("类型")));
		org.setSortName(NullToString(rs.getString("简称")));
		org.setTrueName(NullToString(rs.getString("全称")));
		org.setLocation(NullToString(rs.getString("地理位置")));
		org.setHasEqu(rs.getInt("是否有设备"));
		org.setOrgName(NullToString(rs.getString("调度简称")));
		org.setOrgLevel(NullToString(rs.getString("调度级别")));
		org.setOwnerCompanyName(NullToString(rs.getString("所属公司")));
		org.setPowerGridName(NullToString(rs.getString("电网名称")));
		org.setParentOrgName(NullToString(rs.getString("上级调度")));
		org.setAddress(NullToString(rs.getString("通信地址")));
		org.setFaxNumber(NullToString(rs.getString("邮政编码")));
		org.setContactDepartment(NullToString(rs.getString("联系部门")));
		org.setZipCode(NullToString(rs.getString("传真")));
		org.setContactPhone(NullToString(rs.getString("联系电话")));
		org.setContactEmail(NullToString(rs.getString("邮箱地址")));
		return org;
	}
	/**
	 * 调度机构部门
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private ManageDepartOrgBean getManageDepartOrgBean(ResultSet rs) throws SQLException {
		ManageDepartOrgBean org = new ManageDepartOrgBean();
		org.setOrgId(rs.getString("组织机构ID"));
		org.setOrgName(rs.getString("组织机构名"));
		org.setForgId(rs.getString("上级组织机构ID"));
		org.setOrder(rs.getInt("排序"));
		org.setType(NullToString(rs.getString("类型")));
		org.setSortName(NullToString(rs.getString("简称")));
		org.setTrueName(NullToString(rs.getString("全称")));
		org.setLocation(NullToString(rs.getString("地理位置")));
		org.setHasEqu(rs.getInt("是否有设备"));
		org.setOwnerCompanyName(NullToString(rs.getString("所属调度机构")));
		return org;
	}
	/**
	 * 发电公司
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private PowerCompanyOrgBean getPowerCompanyOrgBean(ResultSet rs) throws SQLException {
		PowerCompanyOrgBean org = new PowerCompanyOrgBean();
		org.setOrgId(rs.getString("组织机构ID"));
		org.setOrgName(rs.getString("组织机构名"));
		org.setForgId(rs.getString("上级组织机构ID"));
		org.setOrder(rs.getInt("排序"));
		org.setType(NullToString(rs.getString("类型")));
		org.setSortName(NullToString(rs.getString("简称")));
		org.setTrueName(NullToString(rs.getString("全称")));
		org.setLocation(NullToString(rs.getString("地理位置")));
		org.setHasEqu(rs.getInt("是否有设备"));
		org.setAddress(NullToString(rs.getString("通信地址")));
		org.setZipCode(NullToString(rs.getString("邮政编码")));
		org.setFaxNumber(NullToString(rs.getString("传真号码")));
		org.setContactPhone(NullToString(rs.getString("联系人电话_座机")));
		org.setContactMobile(NullToString(rs.getString("联系人电话_手机")));
		org.setContactDepartment(NullToString(rs.getString("联系部门")));
		org.setContactEmail(NullToString(rs.getString("联系人邮箱")));
		org.setComment(NullToString(rs.getString("备注")));
		org.setContactPerson(NullToString(rs.getString("联系人")));
		return org;
	}
	/**
	 * 发电公司部门
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private PowerOrgBean getPowerOrgBean(ResultSet rs) throws SQLException {
		PowerOrgBean org = new PowerOrgBean();
		org.setOrgId(rs.getString("组织机构ID"));
		org.setOrgName(rs.getString("组织机构名"));
		org.setForgId(rs.getString("上级组织机构ID"));
		org.setOrder(rs.getInt("排序"));
		org.setType(NullToString(rs.getString("类型")));
		org.setSortName(NullToString(rs.getString("简称")));
		org.setTrueName(NullToString(rs.getString("全称")));
		org.setLocation(NullToString(rs.getString("地理位置")));
		org.setHasEqu(rs.getInt("是否有设备"));
		org.setOwnerCompanyName(NullToString(rs.getString("上级发电公司")));
		org.setStationType(NullToString(rs.getString("电厂类型")));
		org.setContactDepartment(NullToString(rs.getString("联系部门")));
		org.setContactPerson(NullToString(rs.getString("联系人")));
		org.setContactPhone(NullToString(rs.getString("联系人电话_座机")));
		org.setContactMobile(NullToString(rs.getString("联系人电话_手机")));
		org.setContactEmail(NullToString(rs.getString("联系人邮箱")));
		org.setFaxNumber(NullToString(rs.getString("传真号码")));
		org.setFaxCheckPhone(NullToString(rs.getString("传真核对电话")));
		org.setAddress(NullToString(rs.getString("通信地址")));
		org.setZipCode(NullToString(rs.getString("邮政编码")));
		org.setComment(NullToString(rs.getString("备注")));
		return org;
	}
	/**
	 * 其他机构
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private OtherOrgBean getOtherOrgBean(ResultSet rs) throws SQLException {
		OtherOrgBean org = new OtherOrgBean();
		org.setOrgId(rs.getString("组织机构ID"));
		org.setOrgName(rs.getString("组织机构名"));
		org.setForgId(rs.getString("上级组织机构ID"));
		org.setOrder(rs.getInt("排序"));
		org.setType(NullToString(rs.getString("类型")));
		org.setSortName(NullToString(rs.getString("简称")));
		org.setTrueName(NullToString(rs.getString("全称")));
		org.setLocation(NullToString(rs.getString("地理位置")));
		org.setHasEqu(rs.getInt("是否有设备"));
		org.setOwnerCompanyName(NullToString(rs.getString("所属公司")));
		org.setParentOrgName(NullToString(rs.getString("上级机构")));
		org.setAddress(NullToString(rs.getString("通信地址")));
		org.setFaxNumber(NullToString(rs.getString("传真")));
		org.setContactDepartment(NullToString(rs.getString("联系部门")));
		org.setZipCode(NullToString(rs.getString("邮政编码")));
		org.setContactPhone(NullToString(rs.getString("联系电话")));
		org.setContactEmail(NullToString(rs.getString("邮箱地址")));
		return org;
	}
	/**
	 * 机构基本信息
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private OrgBean getCommonOrgBean(ResultSet rs) throws SQLException {
		OrgBean org = new OrgBean();
		org.setOrgId(rs.getString("组织机构ID"));
		org.setOrgName(rs.getString("组织机构名"));
		org.setForgId(rs.getString("上级组织机构ID"));
		org.setOrder(rs.getInt("排序"));
		org.setType(NullToString(rs.getString("类型")));
		org.setSortName(NullToString(rs.getString("简称")));
		org.setTrueName(NullToString(rs.getString("全称")));
		org.setLocation(NullToString(rs.getString("地理位置")));
		org.setHasEqu(rs.getInt("是否有设备"));
		return org;
	}
	/**
	 * 将null 转成 ''
	 * @param value
	 * @return
	 */
	private String NullToString(String value){
		if(value == null){
			return "";
		}else{
			return value;
		}
	}

}
