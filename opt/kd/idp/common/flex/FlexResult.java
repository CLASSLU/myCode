package kd.idp.common.flex;

public class FlexResult {

	
	/**
	 * 状态
	 */
	private int status = 0;
	
	/**
	 * 消息
	 */
	private String message = "";
	
	
	/**
	 * 返回处理对象
	 */
	private Object resultObject = null;
	
	public Object getResultObject() {
		return resultObject;
	}

	public void setResultObject(Object resultObject) {
		this.resultObject = resultObject;
	}

	public FlexResult() {
		
	}
	
	public FlexResult(int _status , String _message) {
		status = _status;
		message = _message;
	}

	

	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
