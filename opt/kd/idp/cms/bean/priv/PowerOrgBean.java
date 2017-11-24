package kd.idp.cms.bean.priv;

/**
 * 发电厂 组织机构详细信息
 * @author Administrator
 *(组织机构ID,电厂全称,电厂简称,上级发电公司,电厂类型,联系部门,联系人,联系人电话_座机,
 *联系人电话_手机,联系人邮箱,传真号码,传真核对电话,通信地址,邮政编码,备注 )
 */
public class PowerOrgBean extends OrgBean{

	
	/**
	 * 上级发电公司
	 */
	private String ownerCompanyName = null;
	/**
	 * 电厂类型
	 */
	private String stationType = null;
	/**
	 * 联系部门
	 */
	private String contactDepartment = null;
	/**
	 * 联系人
	 */
	private String contactPerson = null;
	/**
	 * 联系人座机
	 */
	private String contactPhone = null;
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
	/**
	 * 传真核对电话
	 */
	private String faxCheckPhone = null;
	/**
	 * 通信地址
	 */
	private String address = null;
	/**
	 * 邮政编码
	 */
	private String zipCode = null;
	/**
	 * 备注
	 */
	private String comment = null;
	
	
	
	public String getOwnerCompanyName() {
		return ownerCompanyName;
	}
	public void setOwnerCompanyName(String ownerCompanyName) {
		this.ownerCompanyName = ownerCompanyName;
	}
	public String getStationType() {
		return stationType;
	}
	public void setStationType(String stationType) {
		this.stationType = stationType;
	}
	public String getContactDepartment() {
		return contactDepartment;
	}
	public void setContactDepartment(String contactDepartment) {
		this.contactDepartment = contactDepartment;
	}
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
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
	public String getFaxCheckPhone() {
		return faxCheckPhone;
	}
	public void setFaxCheckPhone(String faxCheckPhone) {
		this.faxCheckPhone = faxCheckPhone;
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
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
