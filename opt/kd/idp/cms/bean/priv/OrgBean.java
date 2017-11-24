package kd.idp.cms.bean.priv;

/**
 * 组织机构
 * @author caoyu
 *
 */
public class OrgBean {

	
	/**
	 * 组织机构ID
	 */
	private String orgId = null;
	
	/**
	 * 组织机构名
	 */
	private String orgName = null;
	
	/**
	 * 上级组织机构ID
	 */
	private String forgId = null;
	
	/**
	 * 排序
	 */
	private int order = 0;
	
	/**
	 * 类型
	 */
	private String type = null;
	
	/**
	 * 组织机构类型 --详细类型
	 */
	private String detailType = null;

	/**
	 * 简称
	 */
	private String sortName = null;
	
	/**
	 * 全称
	 */
	private String trueName = null;
	
	/**
	 * 地理位置
	 */
	private String location = null;
	
	/**
	 * 是否含有设备
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
