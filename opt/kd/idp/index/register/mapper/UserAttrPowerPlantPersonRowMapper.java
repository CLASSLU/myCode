package kd.idp.index.register.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kd.idp.cms.bean.priv.UserAttrBean;

import kd.idp.index.register.bean.UserAttrElectricCompanyPersonBean;
import kd.idp.index.register.bean.UserAttrPowerPlantPersonBean;
import kd.idp.index.register.bean.UserElectricCompanyPersonBean;

import org.springframework.jdbc.core.RowMapper;

public class UserAttrPowerPlantPersonRowMapper implements RowMapper<UserAttrPowerPlantPersonBean>{
	
	private SimpleDateFormat sdf = null;
	
	public UserAttrPowerPlantPersonRowMapper() {
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	public UserAttrPowerPlantPersonRowMapper(String pattern) {
		if(pattern != null && !"".equals(pattern)){
			sdf = new SimpleDateFormat(pattern);
		}else{
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		}
	}
	

	public UserAttrPowerPlantPersonBean mapRow(ResultSet rs, int rowNum) throws SQLException {

		UserAttrPowerPlantPersonBean attr= new UserAttrPowerPlantPersonBean();

		attr.setUserName(NullToString(rs.getString("姓名")));
		attr.setUserNamePinyin(NullToString(rs.getString("姓名全拼")));
		attr.setUserCompany(NullToString(rs.getString("所属单位")));
		attr.setUserGender(NullToString(rs.getString("性别")));
		attr.setUserBirthDate(NullToString(rs.getString("出生年月")));
		attr.setUserType(NullToString(rs.getString("人员类型")));
		attr.setUserJobTitle(NullToString(rs.getString("职务")));
		attr.setUserMajor(NullToString(rs.getString("专业")));
		attr.setUserOfficeTel(NullToString(rs.getString("办公电话")));
		attr.setUserPhoneNumber(NullToString(rs.getString("移动电话")));
		attr.setUserEmail(NullToString(rs.getString("电子邮箱")));
		attr.setUserPhoto(NullToString(rs.getString("一寸照片")));

//		attr.setUserGender(rs.getInt("性别"));
//		attr.setBirthDate(dateToString(rs.getDate("出生日期")));
//		attr.setEmployedDate(dateToString(rs.getDate("工作时间")));
//		attr.setEducation(NullToString(rs.getString("学历")));
//		attr.setSchool(NullToString(rs.getString("毕业学校")));
//		attr.setGraduationDate(dateToString(rs.getDate("毕业时间")));
//		attr.setPoliticsStatus(NullToString(rs.getString("政治面貌")));
//		attr.setJoinPartyDate(dateToString(rs.getDate("入党时间")));
//		attr.setPartyMemberInfo(NullToString(rs.getString("党员信息")));
//		attr.setTechnicalTitle(NullToString(rs.getString("技术职称")));
//		attr.setTechTitleDate(dateToString(rs.getDate("职称评级时间")));
//		attr.setOfficeTel(NullToString(rs.getString("办公电话")));
//		attr.setHomeTel(NullToString(rs.getString("家庭电话")));
//		attr.setPhoneNumber(NullToString(rs.getString("手机号")));
//		attr.setEmail(NullToString(rs.getString("邮箱")));
//		attr.setPosition(NullToString(rs.getString("岗位")));
//		attr.setPositionLevel(NullToString(rs.getString("职级")));
//		attr.setPhoto(NullToString(rs.getString("照片")));
//		attr.setResume(NullToString(rs.getString("个人简历")));
//		attr.setHonor(NullToString(rs.getString("个人荣誉")));
//		attr.setPersonalStatus(NullToString(rs.getString("个人状态")));
//		attr.setBak(NullToString(rs.getString("备注")));

		return attr;
	}
	
	/**
	 * 日期转换为字符串
	 * @param date
	 * @return
	 */
	private String dateToString(Date date){
		try {
			if(date != null){
				return sdf.format(date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 将null 转成 ''
	 * @param value
	 * @return
	 */
	private String NullToString(String value){
		if(value == null){
			return "";
		}else{
			return value;
		}
	}

}
