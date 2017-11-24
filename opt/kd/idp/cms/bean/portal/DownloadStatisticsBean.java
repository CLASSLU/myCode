package kd.idp.cms.bean.portal;

public class DownloadStatisticsBean {
	
	/**
	 * 内容ID
	 */
	private String sourceId = null;
	
	/**
	 * 存储目录
	 */
	private String storePath = null;
	
	/**
	 * 关联权限
	 */
	private String relPriv = null;
	
	/**
	 * 下载人
	 */
	private String downloadUser = null;
	
	/**
	 * 下载时间
	 */
	private String downloadTime = null;

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getStorePath() {
		return storePath;
	}

	public void setStorePath(String storePath) {
		this.storePath = storePath;
	}

	public String getRelPriv() {
		return relPriv;
	}

	public void setRelPriv(String relPriv) {
		this.relPriv = relPriv;
	}

	public String getDownloadUser() {
		return downloadUser;
	}

	public void setDownloadUser(String downloadUser) {
		this.downloadUser = downloadUser;
	}

	public String getDownloadTime() {
		return downloadTime;
	}

	public void setDownloadTime(String downloadTime) {
		this.downloadTime = downloadTime;
	}
	
}
