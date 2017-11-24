package kd.idp.index.register.dao;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spring.dbservice.DBTemplate;

import kd.idp.cms.bean.priv.UserBean;
import kd.idp.cms.dao.UserDao;
import kd.idp.common.CommonTools;
import kd.idp.common.consts.TableConst;
import kd.idp.index.register.bean.UserAttrElectricCompanyPersonBean;
import kd.idp.index.register.bean.UserAttrPowerPlantPersonBean;
import kd.idp.index.register.bean.UserElectricCompanyPersonBean;
import kd.idp.index.register.bean.UserPowerPlantPersonBean;
import kd.idp.index.register.mapper.UserAttrElectricCompanyPersonRowMapper;
import kd.idp.index.register.mapper.UserAttrPowerPlantPersonRowMapper;
import kd.idp.index.register.mapper.UserElectricCompanyPersonRowMapper;
import kd.idp.index.register.mapper.UserPowerPlantPersonRowMapper;


public class UserRegisterDao extends UserDao {

	/**
	 * 电力公司人员注册信息写入数据库
	 * @param user
	 */
	public void setElectricCompanyPerson(UserElectricCompanyPersonBean user){
		String userId = user.getUserId();
		if(userId == null || userId.equals("")){
			this.addNewUser(user, true);
		}else{
			this.modifyUser(user);
		}
		System.out.print(user.getUserName());
		
	}
	
	/**
	 * 发电厂人员注册信息写入数据库
	 * @param user
	 */
	public void setPowerPlantPerson(UserPowerPlantPersonBean user){
		String userId = user.getUserId();
		if(userId == null || userId.equals("")){
			this.addNewUser(user, true);
		}else{
			this.modifyUser(user);
		}
		System.out.print(user.getUserName());
	}
	
	/**
	 * 电力公司人员信息数据库新增记录
	 * @param user
	 * @param isAudit
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public boolean addNewUser(UserElectricCompanyPersonBean user, boolean isAudit){
		String userId = CommonTools.createId("USER");
		user.setUserId(userId);
		DBTemplate.getInstance().updateSql(getInsertUserSql(user, isAudit));
		DBTemplate.getInstance().updateSql(getInsertUserAttrElectricCompanyPersonSql(user));
		return true;
	}
	
	/**
	 * 发电厂人员信息数据库新增记录
	 * @param user
	 * @param isAudit
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public boolean addNewUser(UserPowerPlantPersonBean user, boolean isAudit){
		String userId = CommonTools.createId("USER");
		user.setUserId(userId);
		DBTemplate.getInstance().updateSql(getInsertUserSql(user, isAudit));
		DBTemplate.getInstance().updateSql(getInsertUserAttrPowerPlantPersonSql(user));
		return true;
	}	
	
	/**
	 * 修改电力公司人员注册信息
	 * @param user
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public boolean modifyUser(UserElectricCompanyPersonBean user){
		String userId = user.getUserId();
		try {
			DBTemplate.getInstance().updateSql(getUpdateUserSql(user));
			DBTemplate.getInstance().updateSql(getDeleteUserAttrElectricCompanyPersonSql(userId));
			DBTemplate.getInstance().updateSql(getInsertUserAttrElectricCompanyPersonSql(user));				
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * 修改发电厂人员注册信息
	 * @param user
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class})
	public boolean modifyUser(UserPowerPlantPersonBean user){
		String userId = user.getUserId();
		try {
			DBTemplate.getInstance().updateSql(getUpdateUserSql(user));
			DBTemplate.getInstance().updateSql(getDeleteUserAttrPowerPlantPersonSql(userId));
			DBTemplate.getInstance().updateSql(getInsertUserAttrPowerPlantPersonSql(user));				
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/**
	 * 通过用户Id获取用户信息
	 * @param userId
	 * @return 
	 */
	public UserBean getUserDetail(String userId){
		String companyType = this.getUserFromID(userId).getCompanyType();
		if(companyType.equals("电力公司")){
			return getUserElectricCompanyPersonDetail(userId);
		}else if(companyType.equals("发电厂")){
			return getUserPowerPlantPersonDetail(userId);
		}else{
			System.out.println("未知的所属单位类型");
		}
		return null;		
	}
	
	/**
	 * 通过用户Id获取电力公司人员注册信息
	 * @param userId
	 * @return
	 */
	public UserElectricCompanyPersonBean getUserElectricCompanyPersonDetail(String userId){
		UserElectricCompanyPersonBean user = this.getUserElectricCompanyPersonFromID(userId);
		user.setAttrElectricCompanyPerson(this.getUserAttrElectricCompanyPersonFromID(userId));
		return user;
	}
	
	/**
	 * 从门户_用户_管理表中根据用户Id获取电力公司人员注册信息
	 * @param userId
	 * @return
	 */
	public UserElectricCompanyPersonBean getUserElectricCompanyPersonFromID(String userId){
		String sql = "SELECT * FROM "+TableConst.TABLE_MANAGE_USER +" where 用户ID='"+userId+"'";
		try {
			List<UserElectricCompanyPersonBean> userList = getUserElectricCompanyPersonList(sql);
			if(userList != null && userList.size()>0){
				return userList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new UserElectricCompanyPersonBean();
	}
	
	/**
	 * 查询数据库获取电力公司人员注册信息
	 * @param sql
	 * @return
	 */
	private List<UserElectricCompanyPersonBean> getUserElectricCompanyPersonList(String sql){
		return DBTemplate.getInstance().getResultRowMapperList(sql, new UserElectricCompanyPersonRowMapper());
	}
	
	/**
	 * 从门户_用户_电力公司人员_属性表中根据用户Id获取发电厂人员详细属性信息
	 * @param userId
	 * @return
	 */
	public UserAttrElectricCompanyPersonBean getUserAttrElectricCompanyPersonFromID(String userId){
		String sql = "SELECT * FROM "+TableConst.TABLE_MANAGE_USER_ATTR_ELECTRIC_COMPANY_PERSON +" where 用户ID='"+userId+"'";
		try {
			List<UserAttrElectricCompanyPersonBean> attrList = getUserAttrElectricCompanyPersonList(sql);
			if(attrList != null && attrList.size()>0){
				return attrList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new UserAttrElectricCompanyPersonBean();
	}
	
	/**
	 * 查询数据库获取电力公司人员详细属性信息
	 * @param sql
	 * @return
	 */
	private List<UserAttrElectricCompanyPersonBean> getUserAttrElectricCompanyPersonList(String sql){
		return DBTemplate.getInstance().getResultRowMapperList(sql, new UserAttrElectricCompanyPersonRowMapper());
	}
	
	/**
	 * 通过用户Id获取发电厂人员注册信息
	 * @param userId
	 * @return
	 */
	public UserPowerPlantPersonBean getUserPowerPlantPersonDetail(String userId){
		UserPowerPlantPersonBean user = this.getUserPowerPlantPersonFromID(userId);
		user.setAttrPowerPlantPerson(this.getUserAttrPowerPlantPersonFromID(userId));
		return user;
	}
	
	/**
	 * 从门户_用户_管理表中根据用户Id获取发电厂人员注册信息
	 * @param userId
	 * @return
	 */
	public UserPowerPlantPersonBean getUserPowerPlantPersonFromID(String userId){
		String sql = "SELECT * FROM "+TableConst.TABLE_MANAGE_USER +" where 用户ID='"+userId+"'";
		try {
			List<UserPowerPlantPersonBean> userList = getUserPowerPlantPersonList(sql);
			if(userList != null && userList.size()>0){
				return userList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new UserPowerPlantPersonBean();
	}
	
	/**
	 * 查询数据库获取发电厂人员注册信息
	 * @param sql
	 * @return
	 */
	private List<UserPowerPlantPersonBean> getUserPowerPlantPersonList(String sql){
		return DBTemplate.getInstance().getResultRowMapperList(sql, new UserPowerPlantPersonRowMapper());
	}
	
	/**
	 * 从门户_用户_发电厂人员_属性表中根据用户Id获取发电厂人员详细属性信息
	 * @param userId
	 * @return
	 */
	public UserAttrPowerPlantPersonBean getUserAttrPowerPlantPersonFromID(String userId){
		String sql = "SELECT * FROM "+TableConst.TABLE_MANAGE_USER_ATTR_POWER_PLANT_PERSON +" where 用户ID='"+userId+"'";
		try {
			List<UserAttrPowerPlantPersonBean> attrList = getUserAttrPowerPlantPersonList(sql);
			if(attrList != null && attrList.size()>0){
				return attrList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new UserAttrPowerPlantPersonBean();
	}
	
	/**
	 * 查询数据库获取发电厂人员详细属性信息
	 * @param sql
	 * @return
	 */
	private List<UserAttrPowerPlantPersonBean> getUserAttrPowerPlantPersonList(String sql){
		return DBTemplate.getInstance().getResultRowMapperList(sql, new UserAttrPowerPlantPersonRowMapper());
	}
	
	/**
	 * 电力公司人员信息新增记录sql语句
	 * @param user
	 * @return
	 */
	private String getInsertUserAttrElectricCompanyPersonSql(UserElectricCompanyPersonBean user){
		UserAttrElectricCompanyPersonBean attr = user.getAttrElectricCompanyPerson();
		return "INSERT INTO "
				+ TableConst.TABLE_MANAGE_USER_ATTR_ELECTRIC_COMPANY_PERSON
				+ "(用户ID, 姓名, 姓名全拼, 所属单位, 所属部门, 员工属性, 性别, 出生年月, 民族, 籍贯, 政治面貌, 入党时间, 职务, 岗位, 职称, 技能等级, 身份证号, "
				+ "参加工作时间, 初始学历, 初始学位, 初始学位专业, 毕业院校, 最高学历, 最高学位, 最高学位专业, 办公电话, 移动电话, 电子邮箱, 一寸照片)"
				+ "VALUES('" + user.getUserId() + "', '" + user.getUserDisplayName() + "', '" + user.getUserName()
				+ "', '" + attr.getUserCompany() + "', '" + attr.getUserDepartment() + "', '" + attr.getUserStaffType() + "', '" + attr.getUserGender() + "', '" 
				+ attr.getUserBirthDate() + "', '" + attr.getUserEthnic() + "', '" + attr.getUserHometown() + "', '" 
				+ attr.getUserPoliticsStatus() + "', '" + attr.getTimeForJoinParty() + "', '" + attr.getUserJobTitle() + "', '" + attr.getUserPosition()
				+ "', '" + attr.getUserTechnicalTitle() + "', '" + attr.getUserTechnicalLevel() + "', '" 
				+ attr.getUserIdNumber() + "', '" + attr.getUserEmployedDate() + "', '" + attr.getUserInitialEducation()
				+ "', '" + attr.getUserInitialDegree() + "', '" + attr.getUserInitialMajor() + "', '" + attr.getUserSchool()
				+ "', '" + attr.getUserHighestEducation() + "', '" + attr.getUserHighestDegree() + "', '"
				+ attr.getUserHighestMajor() + "', '" + attr.getUserOfficeTel() + "', '" + attr.getUserPhoneNumber()
				+ "', '" + attr.getUserEmail() + "', '" + attr.getUserPhoto() + "')";		
	}
	
	/**
	 * 获得 用户表 删除SQL
	 * @param user
	 * @param isAudit true 已审核 false 未审核
	 * @return
	 */
	protected String getDeleteUserSql(String userId) {
		return "DELETE FROM " + TableConst.TABLE_MANAGE_USER
				+ "WHERE 用户ID='" + userId + "'";

	}
	/**
	 * 电力公司人员信息删除记录sql语句
	 * @param user
	 * @return
	 */
	private String getDeleteUserAttrElectricCompanyPersonSql(String userId){
		return "DELETE FROM "
				+ TableConst.TABLE_MANAGE_USER_ATTR_ELECTRIC_COMPANY_PERSON
				+ "WHERE 用户ID='" + userId + "'";
	}
	
	/**
	 * 发电厂人员信息删除记录sql语句
	 * @param user
	 * @return
	 */
	private String getDeleteUserAttrPowerPlantPersonSql(String userId){
		return "DELETE FROM "
				+ TableConst.TABLE_MANAGE_USER_ATTR_POWER_PLANT_PERSON
				+ "WHERE 用户ID='" + userId + "'";
	}
	
	/**
	 * 发电厂人员信息新增记录sql语句
	 * @param user
	 * @return
	 */
	private String getInsertUserAttrPowerPlantPersonSql(UserPowerPlantPersonBean user){
		UserAttrPowerPlantPersonBean attr = user.getAttrPowerPlantPerson();
		return "INSERT INTO "
				+ TableConst.TABLE_MANAGE_USER_ATTR_POWER_PLANT_PERSON
				+ "(用户ID, 姓名, 姓名全拼, 所属单位, 性别, 出生年月, 人员类型, 职务, 专业, "
				+ "办公电话, 移动电话, 电子邮箱, 一寸照片)"
				+ "VALUES('" + user.getUserId() + "', '" + user.getUserDisplayName() + "', '" + user.getUserName()
				+ "', '" + attr.getUserCompany() + "', '" + attr.getUserGender() + "', '" 
				+ attr.getUserBirthDate() + "', '" + attr.getUserType() + "', '" + attr.getUserJobTitle() + "', '" + attr.getUserMajor()
				+ "', '" + attr.getUserOfficeTel() + "', '" + attr.getUserPhoneNumber()
				+ "', '" + attr.getUserEmail() + "', '" + attr.getUserPhoto() + "')";		
	}
	

}
