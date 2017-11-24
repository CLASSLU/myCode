package kd.idp.cms.bean.priv;

/**
 * 电网公司 详细组织机构信息
 * @author Administrator
 *(组织机构ID,公司名称,公司简称,上级公司,公司级别,通信地址,邮政编码,联系部门,传真,联系电话,邮箱地址)
 */
public class ManageCompanyOrgBean extends OrgBean{
	
	/**
	 * 上级公司
	 */
	private String parentCompanyName = null;
	/**
	 * 公司级别
	 */
	private String companyLevel = null;
	/**
	 * 通信地址
	 */
	private String address = null;
	/**
	 * 邮政编码
	 */
	private String zipCode = null;
	/**
	 * 联系部门
	 */
	private String contactDepartment = null;
	/**
	 * 联系人手机
	 */
	private String contactMobile = null;
	/**
	 * 联系人邮箱
	 */
	private String contactEmail = null;
	/**
	 * 传真号码
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
