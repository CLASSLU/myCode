package kd.idp.cms.bean.priv;

import java.io.Serializable;


public class UserBean implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 123L;

	/**
	 * �û�ID
	 */
	private String userId = null;
	
	/**
	 * �û���
	 */
	private String userName = null;
	
	/**
	 * �û���ʾ��
	 */
	private String userDisplayName = null;
	
	/**
	 * ����
	 */
	private String userPwd = null;
	
	/**
	 * ��֯����ID
	 */
	private String userOrgId = null;
	
	/**
	 * ��֯��������
	 */
	private String userOrgName = null;
	
	/**
	 * ��λID
	 */
	private String userCompanyId = null;
	
	/**
	 * ��λ����
	 */
	private String userCompanyName = null;
	
	/**
	 * �Ƿ񶳽�
	 */
	private int disable = 0;
	
	/**
	 * ˳��
	 */
	private int userOrder = 0;

	/**
	 * �Ƿ����
	 */
	private int audit = 0;
	
	/**
	 * ����ʱ��
	 */
	private String vertime = null;
	
	/**
	 * ������λ����
	 */
	private String companyType = null;
	/**
	 * ��ע
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
	 * �û�����
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
