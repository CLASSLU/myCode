package kd.idp.index.statistics.bean;
/**
 * �����Ż���¼���ͳ��
 * @author pcj
 *
 */
public class LoginStatisticsBean extends UseStatisticsBean {

	private String userName;
	private String userId;
	private String department;
	public void setUserName(String _userName){
		this.userName=_userName;
	}
	public String getUserName(){
		return this.userName;
	}
	public void setUserId(String _userId){
		this.userId=_userId;
	}
	public String getUserId(){
		return this.userId;
	}
	public void setDepartment(String _department){
		this.department=_department;
	}
	public String getDepartment(){
		return this.department;
	}
}
