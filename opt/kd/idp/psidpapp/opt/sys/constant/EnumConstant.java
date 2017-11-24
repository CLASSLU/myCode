package kd.idp.psidpapp.opt.sys.constant;

public class EnumConstant {
	/**登录成功**/
	public static String AUTH_SUCCESS_FLAG = "0000";//成功
	/**用户名或密码错误**/
	public static String AUTH_ERROR_NAME_OR_PASSWORD = "0001";//用户名或密码错误！
	/**用户状态已被冻结**/
	public static String AUTH_ERROR_USER_STATUS = "0002";//用户状态已被冻结！
	/**您申请的账号、单位信息未通过审批，请联系系统管理员**/
	public static String AUTH_ERROR_USER_OR_ORG = "0003";//您申请的账号、单位信息未通过审批，请联系系统管理员！
	/**您申请的账号不存在单位信息，请联系系统管理员**/
	public static String AUTH_ERROR_ORG_NULL = "0004";//您申请的账号不存在单位信息，请联系系统管理员！
	/****************************************用户状态 **************************************/
	public static String USER_STATUS_NORMAL = "1";//正常
	
	
	public static String MD5_ON = "ON";//是否启用md5加密
	
	/* isValid:是否有效1：有效，0：无效 */
	public static String ISVALID_FALSE = "0";
	public static String ISVALID_TRUE = "1";
	
	
	/**********************************系统超级管理员的角色ID***************************/
	public static String ADMIN_SYSTEM_ROLE="admin";
	
}
