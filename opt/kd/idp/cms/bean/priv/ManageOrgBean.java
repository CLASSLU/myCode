package kd.idp.cms.bean.priv;

/**
 * ���Ȼ��� ��ϸ��֯������Ϣ
 * @author Administrator
 *(��֯����ID,����ȫ��,���ȼ��,���ȼ���,������˾,��������,
 *�ϼ�����,ͨ�ŵ�ַ,��������,��ϵ����,����,��ϵ�绰,�����ַ )
 */
public class ManageOrgBean extends OrgBean{
	
	
	/*
	 * ���ȼ���
	 */
	private String orgLevel = null;
	/**
	 * ������˾
	 */
	private String ownerCompanyName = null;
	/**
	 * ��������
	 */
	private String	powerGridName = null;
	/**
	 * �ϼ�����
	 */
	private String parentOrgName = null;
	/**
	 * ͨ�ŵ�ַ
	 */
	private String address = null;
	/**
	 * ��������
	 */
	private String zipCode = null;
	/**
	 * ��ϵ����
	 */
	private String contactDepartment = null;
	/**
	 * �������
	 */
	private String faxNumber = null;
	/**
	 * ��ϵ�绰
	 */
	private String contactPhone = null;
	/**
	 * �����ַ
	 */
	private String contactEmail = null;
	
	
	public String getOrgLevel() {
		return orgLevel;
	}
	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}
	public String getOwnerCompanyName() {
		return ownerCompanyName;
	}
	public void setOwnerCompanyName(String ownerCompanyName) {
		this.ownerCompanyName = ownerCompanyName;
	}
	public String getPowerGridName() {
		return powerGridName;
	}
	public void setPowerGridName(String powerGridName) {
		this.powerGridName = powerGridName;
	}
	public String getParentOrgName() {
		return parentOrgName;
	}
	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getContactDepartment() {
		return contactDepartment;
	}
	public void setContactDepartment(String contactDepartment) {
		this.contactDepartment = contactDepartment;
	}
	public String getFaxNumber() {
		return faxNumber;
	}
	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	
	
}
