package kd.idp.cms.bean.priv;

public class LoginResultBean {
	 private String status;
	 private String result;
	 public void setStatus(String _status){
		 this.status=_status;
	 }
	 public String getStatus(){
		 return this.status;
	 }
	 public void setResult(String _result){
		this.result=_result;
	 }
	 public String getResult(){
		 return this.result;
	 }

}
