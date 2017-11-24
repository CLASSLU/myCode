package kd.idp.cms.bean.portal;

/**
 * 网站链接
 * @author caoyu
 *
 */
public class LinkBean {

	/**
	 * 内容ID
	 */
	private String linkId = null;
	
	/**
	 * 链接类型
	 */
	private String linkType = null;
	
	/**
	 * 内容名
	 */
	private String linkName = null;
	
	/**
	 * 链接地址
	 */
	private String linkUrl = null;
	
	/**
	 * 打开方式
	 */
	private String openTarget = null;
	
	/**
	 * 顺序
	 */
	private int order = 0;
	
	/**
	 * 图标
	 */
	private String linkIcon = null;
	
	/**
	 * 状态
	 */
	private int status = 0;
	
	/**
	 * 关联权限
	 */
	private String relPriv = null;

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getOpenTarget() {
		return openTarget;
	}

	public void setOpenTarget(String openTarget) {
		this.openTarget = openTarget;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getLinkIcon() {
		return linkIcon;
	}

	public void setLinkIcon(String linkIcon) {
		this.linkIcon = linkIcon;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRelPriv() {
		return relPriv;
	}

	public void setRelPriv(String relPriv) {
		this.relPriv = relPriv;
	}

	
}
