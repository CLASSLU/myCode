package kd.idp.cms.bean.priv;

public class RoleBean {

	
	/**
	 * ��ɫID
	 */
	private String roleId = null;
	
	/**
	 * ��ɫ��
	 */
	private String roleName = null;
	
	/**
	 * ��ɫ����
	 */
	private String roleDesc = null;
	
	/**
	 * ��ɫ״̬
	 */
	private String roleStatus = null;
	
	/**
	 * ������֯����ID
	 */
	private String orgId = null;
	
	/**
	 * ����
	 */
	private int order = 0;
	
	/**
	 * ����ʱ��
	 */
	private String vertime = null;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public String getRoleStatus() {
		return roleStatus;
	}

	public void setRoleStatus(String roleStatus) {
		this.roleStatus = roleStatus;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getVertime() {
		return vertime;
	}

	public void setVertime(String vertime) {
		this.vertime = vertime;
	}
}
