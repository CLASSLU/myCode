package kd.idp.cms.bean.priv;

/**
 * ��֯����
 * @author caoyu
 *
 */
public class OrgBean {

	
	/**
	 * ��֯����ID
	 */
	private String orgId = null;
	
	/**
	 * ��֯������
	 */
	private String orgName = null;
	
	/**
	 * �ϼ���֯����ID
	 */
	private String forgId = null;
	
	/**
	 * ����
	 */
	private int order = 0;
	
	/**
	 * ����
	 */
	private String type = null;
	
	/**
	 * ��֯�������� --��ϸ����
	 */
	private String detailType = null;

	/**
	 * ���
	 */
	private String sortName = null;
	
	/**
	 * ȫ��
	 */
	private String trueName = null;
	
	/**
	 * ����λ��
	 */
	private String location = null;
	
	/**
	 * �Ƿ����豸
	 */
	private int hasEqu = 0;

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getForgId() {
		return forgId;
	}

	public void setForgId(String forgId) {
		this.forgId = forgId;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getDetailType() {
		return detailType;
	}

	public void setDetailType(String detailType) {
		this.detailType = detailType;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getHasEqu() {
		return hasEqu;
	}

	public void setHasEqu(int hasEqu) {
		this.hasEqu = hasEqu;
	}
	
}
