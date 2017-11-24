package kd.idp.cms.bean.portal;

/**
 * ��վ����
 * @author caoyu
 *
 */
public class LinkBean {

	/**
	 * ����ID
	 */
	private String linkId = null;
	
	/**
	 * ��������
	 */
	private String linkType = null;
	
	/**
	 * ������
	 */
	private String linkName = null;
	
	/**
	 * ���ӵ�ַ
	 */
	private String linkUrl = null;
	
	/**
	 * �򿪷�ʽ
	 */
	private String openTarget = null;
	
	/**
	 * ˳��
	 */
	private int order = 0;
	
	/**
	 * ͼ��
	 */
	private String linkIcon = null;
	
	/**
	 * ״̬
	 */
	private int status = 0;
	
	/**
	 * ����Ȩ��
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
