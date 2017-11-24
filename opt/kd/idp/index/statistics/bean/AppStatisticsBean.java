package kd.idp.index.statistics.bean;

/**
 * 应用使用情况统计
 * @author pcj
 *
 */
public class AppStatisticsBean extends LoginStatisticsBean {
 
	private String appType;
	private String appName;
	public void setAppType(String _appType){
		this.appType=_appType;
	}
	public String getAppType(){
		return this.appType;
	}
	public void setAppName(String _appName){
		this.appName=_appName;
	}
	public String getAppName(){
		return this.appName;
	}
}
