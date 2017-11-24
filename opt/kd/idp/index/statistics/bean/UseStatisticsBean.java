package kd.idp.index.statistics.bean;




/**
 * 地区使用统计
 * @author pcj
 *
 */
public class UseStatisticsBean {

	private String dv;
	private String statisticsContent;
	private int count;
	private String comment;
	private String date;
	private int m_count;    //备用 月统计
	private int y_count;    //备用 年统计
	private int total_count;//备用 总量
	public void setDv(String _dv){
		this.dv=_dv;
	}
	public String getDv(){
		return this.dv;
	}
	public void setStatisticsContent(String _statisticsContent){
		this.statisticsContent=_statisticsContent;
	}
	public String getStatisticsContent(){
		return this.statisticsContent;
	}
	public void setCount(int _count){
		this.count=_count;
	}
	public int getCount(){
		return this.count;
	}
	public void setComment(String _comment){
		this.comment=_comment;
	}
	public String getComment(){
		return this.comment;
	}
	public void setDate(String _date){
		this.date=_date;
	}
	public String getDate(){
		return this.date;
	}
	public void setM_count(int m_count){
		this.m_count=m_count;
	}
	public int getM_count(){
		return this.m_count;
	}
	public void setY_count(int y_count){
		this.y_count=y_count;
	}
	public int getY_count(){
		return this.y_count;
	}
	public void setTotal_count(int total_count){
		this.total_count=total_count;
	}
	public int getTotal_count(){
		return this.total_count;
	}
	
}
