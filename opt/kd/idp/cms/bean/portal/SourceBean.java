package kd.idp.cms.bean.portal;

public class SourceBean {

	/**
	 * 内容ID
	 */
	private String sourceId = null;
	
	/**
	 * 文档名称
	 */
	private String sourceName = null;
	
	/**
	 * 文档类型
	 */
	private String sourceType = null;
	
	/**
	 * 文件名称
	 */
	private String fileName = null;
	
	/**
	 * 文档说明
	 */
	private String sourceDetail = null;
	
	/**
	 * 文件大小
	 */
	private double fileSize = 0;
	
	/**
	 * 存储目录
	 */
	private String storePath = null;
	
	/**
	 * 关联权限
	 */
	private String relPriv = null;
	
	/**
	 * 创建人ID
	 */
	private String createrId = null;
	
	/**
	 * 创建人
	 */
	private String creater = null;
	
	/**
	 * 创建日期
	 */
	private String createTime = null;
	
	/**
	 * 审核人ID
	 */
	private String assessorId = null;
	
	/**
	 * 审核人
	 */
	private String assessor = null;
	
	/**
	 * 审核时间
	 */
	private String assessorTime = null;
	
	/**
	 * 状态
	 */
	private int status = 0;
	
	/**
	 * 顺序
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
