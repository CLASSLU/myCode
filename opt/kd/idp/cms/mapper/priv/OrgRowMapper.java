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
			String orgType = NullToString(rs.getString("��������"));
			if ("������˾".equals(orgType)) {
				org = getManageCompanyOrgBean(rs);
			} else if ("���Ȼ���".equals(orgType)) {
				org = getManageOrgBean(rs);
			} else if ("���Ȼ�������".equals(orgType)) {
				org = getManageDepartOrgBean(rs);
			} else if ("���繫˾".equals(orgType)) {
				org = getPowerCompanyOrgBean(rs);
			} else if ("���糧".equals(orgType)) {
				org = getPowerOrgBean(rs);
			} else if ("��������".equals(orgType)) {
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
	 * ������˾
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private ManageCompanyOrgBean getManageCompanyOrgBean(ResultSet rs) throws SQLException {
		ManageCompanyOrgBean org = new ManageCompanyOrgBean();
		org.setOrgId(rs.getString("��֯����ID"));
		org.setOrgName(rs.getString("��֯������"));
		org.setForgId(rs.getString("�ϼ���֯����ID"));
		org.setOrder(rs.getInt("����"));
		org.setType(NullToString(rs.getString("����")));
		org.setSortName(NullToString(rs.getString("���")));
		org.setTrueName(NullToString(rs.getString("ȫ��")));
		org.setLocation(NullToString(rs.getString("����λ��")));
		org.setHasEqu(rs.getInt("�Ƿ����豸"));
		org.setParentCompanyName(NullToString(rs.getString("�ϼ���˾")));
		org.setCompanyLevel(NullToString(rs.getString("��˾����")));
		org.setAddress(NullToString(rs.getString("ͨ�ŵ�ַ")));
		org.setZipCode(NullToString(rs.getString("��������")));
		org.setFaxNumber(NullToString(rs.getString("����")));
		org.setContactMobile(NullToString(rs.getString("��ϵ�绰")));
		org.setContactDepartment(NullToString(rs.getString("��ϵ����")));
		org.setContactEmail(NullToString(rs.getString("�����ַ")));
		return org;
	}
	/**
	 * ���Ȼ���
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private ManageOrgBean getManageOrgBean(ResultSet rs) throws SQLException {
		ManageOrgBean org = new ManageOrgBean();
		org.setOrgId(rs.getString("��֯����ID"));
		org.setOrgName(rs.getString("��֯������"));
		org.setForgId(rs.getString("�ϼ���֯����ID"));
		org.setOrder(rs.getInt("����"));
		org.setType(NullToString(rs.getString("����")));
		org.setSortName(NullToString(rs.getString("���")));
		org.setTrueName(NullToString(rs.getString("ȫ��")));
		org.setLocation(NullToString(rs.getString("����λ��")));
		org.setHasEqu(rs.getInt("�Ƿ����豸"));
		org.setOrgName(NullToString(rs.getString("���ȼ��")));
		org.setOrgLevel(NullToString(rs.getString("���ȼ���")));
		org.setOwnerCompanyName(NullToString(rs.getString("������˾")));
		org.setPowerGridName(NullToString(rs.getString("��������")));
		org.setParentOrgName(NullToString(rs.getString("�ϼ�����")));
		org.setAddress(NullToString(rs.getString("ͨ�ŵ�ַ")));
		org.setFaxNumber(NullToString(rs.getString("��������")));
		org.setContactDepartment(NullToString(rs.getString("��ϵ����")));
		org.setZipCode(NullToString(rs.getString("����")));
		org.setContactPhone(NullToString(rs.getString("��ϵ�绰")));
		org.setContactEmail(NullToString(rs.getString("�����ַ")));
		return org;
	}
	/**
	 * ���Ȼ�������
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private ManageDepartOrgBean getManageDepartOrgBean(ResultSet rs) throws SQLException {
		ManageDepartOrgBean org = new ManageDepartOrgBean();
		org.setOrgId(rs.getString("��֯����ID"));
		org.setOrgName(rs.getString("��֯������"));
		org.setForgId(rs.getString("�ϼ���֯����ID"));
		org.setOrder(rs.getInt("����"));
		org.setType(NullToString(rs.getString("����")));
		org.setSortName(NullToString(rs.getString("���")));
		org.setTrueName(NullToString(rs.getString("ȫ��")));
		org.setLocation(NullToString(rs.getString("����λ��")));
		org.setHasEqu(rs.getInt("�Ƿ����豸"));
		org.setOwnerCompanyName(NullToString(rs.getString("�������Ȼ���")));
		return org;
	}
	/**
	 * ���繫˾
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private PowerCompanyOrgBean getPowerCompanyOrgBean(ResultSet rs) throws SQLException {
		PowerCompanyOrgBean org = new PowerCompanyOrgBean();
		org.setOrgId(rs.getString("��֯����ID"));
		org.setOrgName(rs.getString("��֯������"));
		org.setForgId(rs.getString("�ϼ���֯����ID"));
		org.setOrder(rs.getInt("����"));
		org.setType(NullToString(rs.getString("����")));
		org.setSortName(NullToString(rs.getString("���")));
		org.setTrueName(NullToString(rs.getString("ȫ��")));
		org.setLocation(NullToString(rs.getString("����λ��")));
		org.setHasEqu(rs.getInt("�Ƿ����豸"));
		org.setAddress(NullToString(rs.getString("ͨ�ŵ�ַ")));
		org.setZipCode(NullToString(rs.getString("��������")));
		org.setFaxNumber(NullToString(rs.getString("�������")));
		org.setContactPhone(NullToString(rs.getString("��ϵ�˵绰_����")));
		org.setContactMobile(NullToString(rs.getString("��ϵ�˵绰_�ֻ�")));
		org.setContactDepartment(NullToString(rs.getString("��ϵ����")));
		org.setContactEmail(NullToString(rs.getString("��ϵ������")));
		org.setComment(NullToString(rs.getString("��ע")));
		org.setContactPerson(NullToString(rs.getString("��ϵ��")));
		return org;
	}
	/**
	 * ���繫˾����
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private PowerOrgBean getPowerOrgBean(ResultSet rs) throws SQLException {
		PowerOrgBean org = new PowerOrgBean();
		org.setOrgId(rs.getString("��֯����ID"));
		org.setOrgName(rs.getString("��֯������"));
		org.setForgId(rs.getString("�ϼ���֯����ID"));
		org.setOrder(rs.getInt("����"));
		org.setType(NullToString(rs.getString("����")));
		org.setSortName(NullToString(rs.getString("���")));
		org.setTrueName(NullToString(rs.getString("ȫ��")));
		org.setLocation(NullToString(rs.getString("����λ��")));
		org.setHasEqu(rs.getInt("�Ƿ����豸"));
		org.setOwnerCompanyName(NullToString(rs.getString("�ϼ����繫˾")));
		org.setStationType(NullToString(rs.getString("�糧����")));
		org.setContactDepartment(NullToString(rs.getString("��ϵ����")));
		org.setContactPerson(NullToString(rs.getString("��ϵ��")));
		org.setContactPhone(NullToString(rs.getString("��ϵ�˵绰_����")));
		org.setContactMobile(NullToString(rs.getString("��ϵ�˵绰_�ֻ�")));
		org.setContactEmail(NullToString(rs.getString("��ϵ������")));
		org.setFaxNumber(NullToString(rs.getString("�������")));
		org.setFaxCheckPhone(NullToString(rs.getString("����˶Ե绰")));
		org.setAddress(NullToString(rs.getString("ͨ�ŵ�ַ")));
		org.setZipCode(NullToString(rs.getString("��������")));
		org.setComment(NullToString(rs.getString("��ע")));
		return org;
	}
	/**
	 * ��������
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private OtherOrgBean getOtherOrgBean(ResultSet rs) throws SQLException {
		OtherOrgBean org = new OtherOrgBean();
		org.setOrgId(rs.getString("��֯����ID"));
		org.setOrgName(rs.getString("��֯������"));
		org.setForgId(rs.getString("�ϼ���֯����ID"));
		org.setOrder(rs.getInt("����"));
		org.setType(NullToString(rs.getString("����")));
		org.setSortName(NullToString(rs.getString("���")));
		org.setTrueName(NullToString(rs.getString("ȫ��")));
		org.setLocation(NullToString(rs.getString("����λ��")));
		org.setHasEqu(rs.getInt("�Ƿ����豸"));
		org.setOwnerCompanyName(NullToString(rs.getString("������˾")));
		org.setParentOrgName(NullToString(rs.getString("�ϼ�����")));
		org.setAddress(NullToString(rs.getString("ͨ�ŵ�ַ")));
		org.setFaxNumber(NullToString(rs.getString("����")));
		org.setContactDepartment(NullToString(rs.getString("��ϵ����")));
		org.setZipCode(NullToString(rs.getString("��������")));
		org.setContactPhone(NullToString(rs.getString("��ϵ�绰")));
		org.setContactEmail(NullToString(rs.getString("�����ַ")));
		return org;
	}
	/**
	 * ����������Ϣ
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private OrgBean getCommonOrgBean(ResultSet rs) throws SQLException {
		OrgBean org = new OrgBean();
		org.setOrgId(rs.getString("��֯����ID"));
		org.setOrgName(rs.getString("��֯������"));
		org.setForgId(rs.getString("�ϼ���֯����ID"));
		org.setOrder(rs.getInt("����"));
		org.setType(NullToString(rs.getString("����")));
		org.setSortName(NullToString(rs.getString("���")));
		org.setTrueName(NullToString(rs.getString("ȫ��")));
		org.setLocation(NullToString(rs.getString("����λ��")));
		org.setHasEqu(rs.getInt("�Ƿ����豸"));
		return org;
	}
	/**
	 * ��null ת�� ''
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
