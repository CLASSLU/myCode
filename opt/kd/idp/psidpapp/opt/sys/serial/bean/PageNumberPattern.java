package kd.idp.psidpapp.opt.sys.serial.bean;

public class PageNumberPattern {
	
	public PageNumberPattern() {
		super();
	}
	
	private String patternId;
	
	private String patternName;
	
	private Integer serialCycle;
	
	private Integer serialLength;
	
	private Integer startNum;
	
	private String pageContent;

	public String getPatternId() {
		return patternId;
	}

	public void setPatternId(String patternId) {
		this.patternId = patternId;
	}

	public String getPatternName() {
		return patternName;
	}

	public void setPatternName(String patternName) {
		this.patternName = patternName;
	}

	public Integer getSerialCycle() {
		return serialCycle;
	}

	public void setSerialCycle(Integer serialCycle) {
		this.serialCycle = serialCycle;
	}

	public Integer getSerialLength() {
		return serialLength;
	}

	public void setSerialLength(Integer serialLength) {
		this.serialLength = serialLength;
	}

	public Integer getStartNum() {
		return startNum;
	}

	public void setStartNum(Integer startNum) {
		this.startNum = startNum;
	}

	public String getPageContent() {
		return pageContent;
	}

	public void setPageContent(String pageContent) {
		this.pageContent = pageContent;
	}
}
