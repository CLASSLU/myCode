package kd.idp.cms.bean.portal;

/**
 * ���Ź���
 * @author caoyu
 *
 */
public class NewsBean {

	/**
	 * ����ID
	 */
	private String newsId = "";
	
	/**
	 * ����
	 */
	private String newsTitle = "";
	
	/**
	 * ����
	 */
	private String newsType = "";
	
	/**
	 * ����
	 */
	private String newsContent = "";
	
	/**
	 * ͼƬ
	 */
	private String newsImage = "";
	
	/**
	 * ����
	 */
	private String newsAttach = "";
	
	/**
	 * ���ӵ�ַ
	 */
	private String newsUrl = "";
	
	/**
	 * ״̬
	 */
	private int status = 0;
	
	
	/**
	 * ˳��
	 */
	private int order = 0;
	
	/**
	 * �洢Ŀ¼
	 */
	private String storePath = "";
	
	/**
	 * ����Ȩ��
	 */
	private String relPriv = "";
	
	/**
	 * ������Id
	 */
	private String createrId = "";
	
	/**
	 * ������
	 */
	private String creater = "";
	
	/**
	 * ����ʱ��
	 */
	private String createTime = "";
	
	/**
	 * �����Id
	 */
	private String assessorId = "";
	
	/**
	 * �����
	 */
	private String assessor = "";
	
	/**
	 * ���ʱ��
	 */
	private String assessorTime = "";
	
	/**
	 * ʧЧʱ��
	 */
	private String disableTime = "";
	
	/**
	 * ���Ȩ��
	 */
	private String browsePriv = "";
	
	/**
	 * �Ƿ��ɫ������ʾ
	 */
	private String isRedFontDisplay = "";
	
	/**
	 * �Ƿ���ʾNEW��ʶ��
	 */
	private String isNewNoticeShow = "";
	
	/**
	 * �������
	 */
	private int clickCount = 0;

	
	private String fb = ""; //����λ��
	private String author="";//׫����
	
	
	public String getIsRedFontDisplay() {
		return isRedFontDisplay;
	}

	public void setIsRedFontDisplay(String isRedFontDisplay) {
		this.isRedFontDisplay = isRedFontDisplay;
	}

	public String getIsNewNoticeShow() {
		return isNewNoticeShow;
	}

	public void setIsNewNoticeShow(String isNewNoticeShow) {
		this.isNewNoticeShow = isNewNoticeShow;
	}

	public int getClickCount() {
		return clickCount+1;
	}

	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}

	public String getNewsId() {
		return newsId;
	}

	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}

	public String getNewsTitle() {
		return newsTitle;
	}

	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}

	public String getNewsType() {
		return newsType;
	}

	public void setNewsType(String newsType) {
		this.newsType = newsType;
	}

	public String getNewsContent() {
		return newsContent;
	}

	public void setNewsContent(String newsContent) {
		this.newsContent = newsContent;
	}

	public String getNewsImage() {
		return newsImage;
	}

	public void setNewsImage(String newsImage) {
		this.newsImage = newsImage;
	}

	public String getNewsAttach() {
		return newsAttach;
	}

	public void setNewsAttach(String newsAttach) {
		this.newsAttach = newsAttach;
	}

	public String getNewsUrl() {
		return newsUrl;
	}

	public void setNewsUrl(String newsUrl) {
		this.newsUrl = newsUrl;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
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

	public String getAssessorTime() {
		return assessorTime;
	}

	public void setAssessorTime(String assessorTime) {
		this.assessorTime = assessorTime;
	}

	public String getDisableTime() {
		return disableTime;
	}

	public void setDisableTime(String disableTime) {
		this.disableTime = disableTime;
	}

	public String getBrowsePriv() {
		return browsePriv;
	}

	public void setBrowsePriv(String browsePriv) {
		this.browsePriv = browsePriv;
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
	public String getFb() {
		return fb;
	}

	public void setFb(String fb) {
		this.fb = fb;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
}
