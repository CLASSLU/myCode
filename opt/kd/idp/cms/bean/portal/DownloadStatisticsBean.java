package kd.idp.cms.bean.portal;

public class DownloadStatisticsBean {
	
	/**
	 * ����ID
	 */
	private String sourceId = null;
	
	/**
	 * �洢Ŀ¼
	 */
	private String storePath = null;
	
	/**
	 * ����Ȩ��
	 */
	private String relPriv = null;
	
	/**
	 * ������
	 */
	private String downloadUser = null;
	
	/**
	 * ����ʱ��
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
