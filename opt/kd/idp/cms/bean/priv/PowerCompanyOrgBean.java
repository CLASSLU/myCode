package kd.idp.cms.bean.priv;

/**
 * ���繫˾ ��֯������ϸ��Ϣ
 * @author Administrator
 *(��֯����ID,��˾ȫ��,��˾���,��ϵ����,��ϵ�˵绰_����,��ϵ�˵绰_�ֻ�,��ϵ������,�������,ͨ�ŵ�ַ,�������� )
 */
public class PowerCompanyOrgBean extends OrgBean{

	/**
	 * ��ϵ����
	 */
	private String contactDepartment = null;
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
	
	/**
	 * ��ϵ��
	 */
	private String contactPerson = null;
	
	public String getContactDepartment() {
		return contactDepartment;
	}
	public void setContactDepartment(String contactDepartment) {
		this.contactDepartment = contactDepartment;
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
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

}
