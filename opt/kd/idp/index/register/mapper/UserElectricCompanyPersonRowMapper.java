package kd.idp.index.register.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kd.idp.cms.bean.priv.UserAttrBean;

import kd.idp.index.register.bean.UserAttrElectricCompanyPersonBean;
import kd.idp.index.register.bean.UserElectricCompanyPersonBean;

import org.springframework.jdbc.core.RowMapper;

public class UserElectricCompanyPersonRowMapper implements RowMapper<UserElectricCompanyPersonBean>{
	
	private SimpleDateFormat sdf = null;
	
	public UserElectricCompanyPersonRowMapper() {
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	public UserElectricCompanyPersonRowMapper(String pattern) {
		if(pattern != null && !"".equals(pattern)){
			sdf = new SimpleDateFormat(pattern);
		}else{
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		}
	}
	

	public UserElectricCompanyPersonBean mapRow(ResultSet rs, int rowNum) throws SQLException {

		UserElectricCompanyPersonBean user= new UserElectricCompanyPersonBean();
		user.setUserId(rs.getString("用户ID"));
		user.setUserName(rs.getString("登录名"));
		user.setUserDisplayName(NullToString(rs.getString("显示名")));
		user.setUserPwd(NullToString(rs.getString("密码")));
		user.setUserCompanyId(NullToString(rs.getString("单位ID")));
		user.setUserCompanyName(NullToString(rs.getString("单位名称")));
		user.setUserOrgId(NullToString(rs.getString("组织机构ID")));
		user.setUserOrgName(NullToString(rs.getString("组织机构名称")));
		user.setDisable(rs.getInt("是否冻结"));
		user.setUserOrder(rs.getInt("顺序"));
		user.setAudit(rs.getInt("是否审核"));
		user.setVertime(dateToString(rs.getDate("创建时间")));
		user.setCompanyType(NullToString(rs.getString("所属单位类型")));
//		attr.setUserBirthDate(NullToString(rs.getString("出生日期")));
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

		return user;
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
