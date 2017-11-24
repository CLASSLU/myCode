package kd.idp.cms.bean.portal;

public class SourceBean {

	/**
	 * ����ID
	 */
	private String sourceId = null;
	
	/**
	 * �ĵ�����
	 */
	private String sourceName = null;
	
	/**
	 * �ĵ�����
	 */
	private String sourceType = null;
	
	/**
	 * �ļ�����
	 */
	private String fileName = null;
	
	/**
	 * �ĵ�˵��
	 */
	private String sourceDetail = null;
	
	/**
	 * �ļ���С
	 */
	private double fileSize = 0;
	
	/**
	 * �洢Ŀ¼
	 */
	private String storePath = null;
	
	/**
	 * ����Ȩ��
	 */
	private String relPriv = null;
	
	/**
	 * ������ID
	 */
	private String createrId = null;
	
	/**
	 * ������
	 */
	private String creater = null;
	
	/**
	 * ��������
	 */
	private String createTime = null;
	
	/**
	 * �����ID
	 */
	private String assessorId = null;
	
	/**
	 * �����
	 */
	private String assessor = null;
	
	/**
	 * ���ʱ��
	 */
	private String assessorTime = null;
	
	/**
	 * ״̬
	 */
	private int status = 0;
	
	/**
	 * ˳��
	 */
	private int order = 0;

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public void setSourceName(String sourceName,boolean boo) {
		if(sourceName!=null&&sourceName.length()>5){
			String[] fileName=sourceName.split("_");
			if(fileName[0].length()==5&&"F".equals(fileName[0].substring(0,1))){
				this.sourceName=sourceName.substring(6, sourceName.length());
			}else{
				this.sourceName = sourceName;
			}
		}else{
			this.sourceName = sourceName;
		}
	}
	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSourceDetail() {
		return sourceDetail;
	}

	public void setSourceDetail(String sourceDetail) {
		this.sourceDetail = sourceDetail;
	}

	public double getFileSize() {
		return fileSize;
	}

	public void setFileSize(double fileSize) {
		this.fileSize = fileSize;
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

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getAssessor() {
		return assessor;
	}

	public void setAssessor(String assessor) {
		this.assessor = assessor;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getAssessorTime() {
		return assessorTime;
	}

	public void setAssessorTime(String assessorTime) {
		this.assessorTime = assessorTime;
	}

	public String getCreaterId() {
		return createrId;
	}

	public void setCreaterId(String createrId) {
		this.createrId = createrId;
	}

	public String getAssessorId() {
		return assessorId;
	}

	public void setAssessorId(String assessorId) {
		this.assessorId = assessorId;
	}
	
	
}
