package kd.idp.cms.bean.priv;


/**
 * ���� ������ϸ��Ϣ
 * 
 * @author Administrator
 *         (��֯����ID,��������,�������,������˾,�ϼ�����,ͨ�ŵ�ַ,��������,��ϵ����,����,��ϵ�绰,�����ַ)
 */
public class OtherOrgBean extends OrgBean {

	/**
	 * ������˾
	 */
	private String ownerCompanyName = null;
	/**
	 * �ϼ���֯����
	 */
	private String parentOrgName = null;
	/**
	 * ��ϵ����
	 */
	private String contactDepartment = null;
	/**
	 * ��ϵ������
	 */
	private String contactPhone = null;
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


	public String getOwnerCompanyName() {
		return ownerCompanyName;
	}

	public void setOwnerCompanyName(String ownerCompanyName) {
		this.ownerCompanyName = ownerCompanyName;
	}

	public String getParentOrgName() {
		return parentOrgName;
	}

	public void setParentOrgName(String parentOrgName) {
		this.parentOrgName = parentOrgName;
	}

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

	

	public static float[][] getData(float[][][] data, float cap) {
		float[][] result = new float[data.length][1440];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < 1440; j++) {
				int dcLength = data[i].length;
				float tmp = 0;
				for (int k = 0; k < dcLength; k++) {
					tmp += data[i][k][j];
				}
				result[i][j] = tmp / cap;
			}
		}
		return result;
	}

//	private float getF(float data[][], float start, float end) {
//		float result = 0;
//		int n = 0;
//		for (int i = 0; i < data.length; i++) {
//			for (int j = 0; j < data[i].length; j++) {
//				if (data[i][j] >= start && data[i][j] <= end) {
//					n++;
//				}
//			}
//		}
//		result = n / (1440 * data.length);
//		return result;
//	}

}
