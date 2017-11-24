package kd.idp.cms.bean.priv;

import java.io.Serializable;


public class UserBean implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 123L;

	/**
	 * 用户ID
	 */
	private String userId = null;
	
	/**
	 * 用户名
	 */
	private String userName = null;
	
	/**
	 * 用户显示名
	 */
	private String userDisplayName = null;
	
	/**
	 * 密码
	 */
	private String userPwd = null;
	
	/**
	 * 组织机构ID
	 */
	private String userOrgId = null;
	
	/**
	 * 组织机构名称
	 */
	private String userOrgName = null;
	
	/**
	 * 单位ID
	 */
	private String userCompanyId = null;
	
	/**
	 * 单位名称
	 */
	private String userCompanyName = null;
	
	/**
	 * 是否冻结
	 */
	private int disable = 0;
	
	/**
	 * 顺序
	 */
	private int userOrder = 0;

	/**
	 * 是否审核
	 */
	private int audit = 0;
	
	/**
	 * 创建时间
	 */
	private String vertime = null;
	
	/**
	 * 所属单位类型
	 */
	private String companyType = null;
	/**
	 * 备注
	 */
	private String bak = null;
	
	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	/*
	 * 
	 * 用户属性
	private UserAttrBean attr = null;

	public UserAttrBean getAttr() {
		return attr;
	}

	public void setAttr(UserAttrBean attr) {
		this.attr = attr;
	}*/


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserDisplayName() {
		return userDisplayName;
	}

	public void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getUserOrgId() {
		return userOrgId;
	}

	public void setUserOrgId(String userOrgId) {
		this.userOrgId = userOrgId;
	}

	public int getDisable() {
		return disable;
	}

	public void setDisable(int disable) {
		this.disable = disable;
	}

	public int getUserOrder() {
		return userOrder;
	}

	public void setUserOrder(int userOrder) {
		this.userOrder = userOrder;
	}

	public int getAudit() {
		return audit;
	}

	public void setAudit(int audit) {
		this.audit = audit;
	}

	public String getVertime() {
		return vertime;
	}

	public void setVertime(String vertime) {
		this.vertime = vertime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserOrgName() {
		return userOrgName;
	}

	public void setUserOrgName(String userOrgName) {
		this.userOrgName = userOrgName;
	}

	public String getUserCompanyId() {
		return userCompanyId;
	}

	public void setUserCompanyId(String userCompanyId) {
		this.userCompanyId = userCompanyId;
	}

	public String getUserCompanyName() {
		return userCompanyName;
	}

	public void setUserCompanyName(String userCompanyName) {
		this.userCompanyName = userCompanyName;
	}

	public String getBak() {
		return bak;
	}

	public void setBak(String bak) {
		this.bak = bak;
	}
	
	
}
