package kd.idp.cms.bean.priv;

/**
 * ���糧 ��֯������ϸ��Ϣ
 * @author Administrator
 *(��֯����ID,�糧ȫ��,�糧���,�ϼ����繫˾,�糧����,��ϵ����,��ϵ��,��ϵ�˵绰_����,
 *��ϵ�˵绰_�ֻ�,��ϵ������,�������,����˶Ե绰,ͨ�ŵ�ַ,��������,��ע )
 */
public class PowerOrgBean extends OrgBean{

	
	/**
	 * �ϼ����繫˾
	 */
	private String ownerCompanyName = null;
	/**
	 * �糧����
	 */
	private String stationType = null;
	/**
	 * ��ϵ����
	 */
	private String contactDepartment = null;
	/**
	 * ��ϵ��
	 */
	private String contactPerson = null;
	/**
	 * ��ϵ������
	 */
	private String contactPhone = null;
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
	/**
	 * ����˶Ե绰
	 */
	private String faxCheckPhone = null;
	/**
	 * ͨ�ŵ�ַ
	 */
	private String address = null;
	/**
	 * ��������
	 */
	private String zipCode = null;
	/**
	 * ��ע
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
