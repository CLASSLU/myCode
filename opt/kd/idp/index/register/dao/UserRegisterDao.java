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
	 * ������˾��Աע����Ϣд�����ݿ�
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
	 * ���糧��Աע����Ϣд�����ݿ�
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
	 * ������˾��Ա��Ϣ���ݿ�������¼
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
	 * ���糧��Ա��Ϣ���ݿ�������¼
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
	 * �޸ĵ�����˾��Աע����Ϣ
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
	 * �޸ķ��糧��Աע����Ϣ
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
	 * ͨ���û�Id��ȡ�û���Ϣ
	 * @param userId
	 * @return 
	 */
	public UserBean getUserDetail(String userId){
		String companyType = this.getUserFromID(userId).getCompanyType();
		if(companyType.equals("������˾")){
			return getUserElectricCompanyPersonDetail(userId);
		}else if(companyType.equals("���糧")){
			return getUserPowerPlantPersonDetail(userId);
		}else{
			System.out.println("δ֪��������λ����");
		}
		return null;		
	}
	
	/**
	 * ͨ���û�Id��ȡ������˾��Աע����Ϣ
	 * @param userId
	 * @return
	 */
	public UserElectricCompanyPersonBean getUserElectricCompanyPersonDetail(String userId){
		UserElectricCompanyPersonBean user = this.getUserElectricCompanyPersonFromID(userId);
		user.setAttrElectricCompanyPerson(this.getUserAttrElectricCompanyPersonFromID(userId));
		return user;
	}
	
	/**
	 * ���Ż�_�û�_������и����û�Id��ȡ������˾��Աע����Ϣ
	 * @param userId
	 * @return
	 */
	public UserElectricCompanyPersonBean getUserElectricCompanyPersonFromID(String userId){
		String sql = "SELECT * FROM "+TableConst.TABLE_MANAGE_USER +" where �û�ID='"+userId+"'";
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
	 * ��ѯ���ݿ��ȡ������˾��Աע����Ϣ
	 * @param sql
	 * @return
	 */
	private List<UserElectricCompanyPersonBean> getUserElectricCompanyPersonList(String sql){
		return DBTemplate.getInstance().getResultRowMapperList(sql, new UserElectricCompanyPersonRowMapper());
	}
	
	/**
	 * ���Ż�_�û�_������˾��Ա_���Ա��и����û�Id��ȡ���糧��Ա��ϸ������Ϣ
	 * @param userId
	 * @return
	 */
	public UserAttrElectricCompanyPersonBean getUserAttrElectricCompanyPersonFromID(String userId){
		String sql = "SELECT * FROM "+TableConst.TABLE_MANAGE_USER_ATTR_ELECTRIC_COMPANY_PERSON +" where �û�ID='"+userId+"'";
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
	 * ��ѯ���ݿ��ȡ������˾��Ա��ϸ������Ϣ
	 * @param sql
	 * @return
	 */
	private List<UserAttrElectricCompanyPersonBean> getUserAttrElectricCompanyPersonList(String sql){
		return DBTemplate.getInstance().getResultRowMapperList(sql, new UserAttrElectricCompanyPersonRowMapper());
	}
	
	/**
	 * ͨ���û�Id��ȡ���糧��Աע����Ϣ
	 * @param userId
	 * @return
	 */
	public UserPowerPlantPersonBean getUserPowerPlantPersonDetail(String userId){
		UserPowerPlantPersonBean user = this.getUserPowerPlantPersonFromID(userId);
		user.setAttrPowerPlantPerson(this.getUserAttrPowerPlantPersonFromID(userId));
		return user;
	}
	
	/**
	 * ���Ż�_�û�_������и����û�Id��ȡ���糧��Աע����Ϣ
	 * @param userId
	 * @return
	 */
	public UserPowerPlantPersonBean getUserPowerPlantPersonFromID(String userId){
		String sql = "SELECT * FROM "+TableConst.TABLE_MANAGE_USER +" where �û�ID='"+userId+"'";
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
	 * ��ѯ���ݿ��ȡ���糧��Աע����Ϣ
	 * @param sql
	 * @return
	 */
	private List<UserPowerPlantPersonBean> getUserPowerPlantPersonList(String sql){
		return DBTemplate.getInstance().getResultRowMapperList(sql, new UserPowerPlantPersonRowMapper());
	}
	
	/**
	 * ���Ż�_�û�_���糧��Ա_���Ա��и����û�Id��ȡ���糧��Ա��ϸ������Ϣ
	 * @param userId
	 * @return
	 */
	public UserAttrPowerPlantPersonBean getUserAttrPowerPlantPersonFromID(String userId){
		String sql = "SELECT * FROM "+TableConst.TABLE_MANAGE_USER_ATTR_POWER_PLANT_PERSON +" where �û�ID='"+userId+"'";
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
	 * ��ѯ���ݿ��ȡ���糧��Ա��ϸ������Ϣ
	 * @param sql
	 * @return
	 */
	private List<UserAttrPowerPlantPersonBean> getUserAttrPowerPlantPersonList(String sql){
		return DBTemplate.getInstance().getResultRowMapperList(sql, new UserAttrPowerPlantPersonRowMapper());
	}
	
	/**
	 * ������˾��Ա��Ϣ������¼sql���
	 * @param user
	 * @return
	 */
	private String getInsertUserAttrElectricCompanyPersonSql(UserElectricCompanyPersonBean user){
		UserAttrElectricCompanyPersonBean attr = user.getAttrElectricCompanyPerson();
		return "INSERT INTO "
				+ TableConst.TABLE_MANAGE_USER_ATTR_ELECTRIC_COMPANY_PERSON
				+ "(�û�ID, ����, ����ȫƴ, ������λ, ��������, Ա������, �Ա�, ��������, ����, ����, ������ò, �뵳ʱ��, ְ��, ��λ, ְ��, ���ܵȼ�, ���֤��, "
				+ "�μӹ���ʱ��, ��ʼѧ��, ��ʼѧλ, ��ʼѧλרҵ, ��ҵԺУ, ���ѧ��, ���ѧλ, ���ѧλרҵ, �칫�绰, �ƶ��绰, ��������, һ����Ƭ)"
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
	 * ��� �û��� ɾ��SQL
	 * @param user
	 * @param isAudit true ����� false δ���
	 * @return
	 */
	protected String getDeleteUserSql(String userId) {
		return "DELETE FROM " + TableConst.TABLE_MANAGE_USER
				+ "WHERE �û�ID='" + userId + "'";

	}
	/**
	 * ������˾��Ա��Ϣɾ����¼sql���
	 * @param user
	 * @return
	 */
	private String getDeleteUserAttrElectricCompanyPersonSql(String userId){
		return "DELETE FROM "
				+ TableConst.TABLE_MANAGE_USER_ATTR_ELECTRIC_COMPANY_PERSON
				+ "WHERE �û�ID='" + userId + "'";
	}
	
	/**
	 * ���糧��Ա��Ϣɾ����¼sql���
	 * @param user
	 * @return
	 */
	private String getDeleteUserAttrPowerPlantPersonSql(String userId){
		return "DELETE FROM "
				+ TableConst.TABLE_MANAGE_USER_ATTR_POWER_PLANT_PERSON
				+ "WHERE �û�ID='" + userId + "'";
	}
	
	/**
	 * ���糧��Ա��Ϣ������¼sql���
	 * @param user
	 * @return
	 */
	private String getInsertUserAttrPowerPlantPersonSql(UserPowerPlantPersonBean user){
		UserAttrPowerPlantPersonBean attr = user.getAttrPowerPlantPerson();
		return "INSERT INTO "
				+ TableConst.TABLE_MANAGE_USER_ATTR_POWER_PLANT_PERSON
				+ "(�û�ID, ����, ����ȫƴ, ������λ, �Ա�, ��������, ��Ա����, ְ��, רҵ, "
				+ "�칫�绰, �ƶ��绰, ��������, һ����Ƭ)"
				+ "VALUES('" + user.getUserId() + "', '" + user.getUserDisplayName() + "', '" + user.getUserName()
				+ "', '" + attr.getUserCompany() + "', '" + attr.getUserGender() + "', '" 
				+ attr.getUserBirthDate() + "', '" + attr.getUserType() + "', '" + attr.getUserJobTitle() + "', '" + attr.getUserMajor()
				+ "', '" + attr.getUserOfficeTel() + "', '" + attr.getUserPhoneNumber()
				+ "', '" + attr.getUserEmail() + "', '" + attr.getUserPhoto() + "')";		
	}
	

}
