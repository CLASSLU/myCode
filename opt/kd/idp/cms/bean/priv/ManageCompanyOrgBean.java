package kd.idp.cms.bean.priv;

/**
 * ������˾ ��ϸ��֯������Ϣ
 * @author Administrator
 *(��֯����ID,��˾����,��˾���,�ϼ���˾,��˾����,ͨ�ŵ�ַ,��������,��ϵ����,����,��ϵ�绰,�����ַ)
 */
public class ManageCompanyOrgBean extends OrgBean{
	
	/**
	 * �ϼ���˾
	 */
	private String parentCompanyName = null;
	/**
	 * ��˾����
	 */
	private String companyLevel = null;
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
	 * ��ϵ���ֻ�
	 */
	private String contactMobile = null;
	/**
	 * ��ϵ������
	 */
	private String contactEmail = null;
	/**
	 * �������
	 */
	private String faxNumber = null;
	
	public String getParentCompanyName() {
		return parentCompanyName;
	}
	public void setParentCompanyName(String parentCompanyName) {
		this.parentCompanyName = parentCompanyName;
	}
	public String getCompanyLevel() {
		return companyLevel;
	}
	public void setCompanyLevel(String companyLevel) {
		this.companyLevel = companyLevel;
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
	public String getContactMobile() {
		return contactMobile;
	}
	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	public String getFaxNumber() {
		return faxNumber;
	}
	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}
	
}
