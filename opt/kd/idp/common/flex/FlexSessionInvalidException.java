package kd.idp.common.flex;

public class FlexSessionInvalidException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5466792718900738907L;

	public FlexSessionInvalidException(String message) {
		super(message);
	}
	
	
	public FlexSessionInvalidException() {
		super("用户Session已经失效,请重新登录!");
	}

}
