package kd.idp.cms.bean.priv;

/**
 * 调度机构 详细组织机构信息
 * @author Administrator
 *(组织机构ID,调度全称,调度简称,调度级别,所属公司,电网名称,
 *上级调度,通信地址,邮政编码,联系部门,传真,联系电话,邮箱地址 )
 */
public class ManageOrgBean extends OrgBean{
	
	
	/*
	 * 调度级别
	 */
	private String orgLevel = null;
	/**
	 * 所属公司
	 */
	private String ownerCompanyName = null;
	/**
	 * 电网名称
	 */
	private String	powerGridName = null;
	/**
	 * 上级调度
	 */
	private String parentOrgName = null;
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
	 * 传真号码
	 */
	private String faxNumber = null;
	/**
	 * 联系电话
	 */
	private String contactPhone = null;
	/**
	 * 邮箱地址
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
