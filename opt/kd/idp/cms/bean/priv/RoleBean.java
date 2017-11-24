package kd.idp.cms.bean.priv;

public class RoleBean {

	
	/**
	 * 角色ID
	 */
	private String roleId = null;
	
	/**
	 * 角色名
	 */
	private String roleName = null;
	
	/**
	 * 角色描述
	 */
	private String roleDesc = null;
	
	/**
	 * 角色状态
	 */
	private String roleStatus = null;
	
	/**
	 * 所属组织机构ID
	 */
	private String orgId = null;
	
	/**
	 * 排序
	 */
	private int order = 0;
	
	/**
	 * 创建时间
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
