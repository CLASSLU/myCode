package kd.idp.index.register.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kd.idp.cms.bean.priv.UserAttrBean;

import kd.idp.index.register.bean.UserAttrElectricCompanyPersonBean;
import kd.idp.index.register.bean.UserElectricCompanyPersonBean;

import org.springframework.jdbc.core.RowMapper;

public class UserAttrElectricCompanyPersonRowMapper implements RowMapper<UserAttrElectricCompanyPersonBean>{
	
	private SimpleDateFormat sdf = null;
	
	public UserAttrElectricCompanyPersonRowMapper() {
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	public UserAttrElectricCompanyPersonRowMapper(String pattern) {
		if(pattern != null && !"".equals(pattern)){
			sdf = new SimpleDateFormat(pattern);
		}else{
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		}
	}
	

	public UserAttrElectricCompanyPersonBean mapRow(ResultSet rs, int rowNum) throws SQLException {

		UserAttrElectricCompanyPersonBean attr= new UserAttrElectricCompanyPersonBean();

		attr.setUserName(NullToString(rs.getString("����")));
		attr.setUserNamePinyin(NullToString(rs.getString("����ȫƴ")));
		attr.setUserCompany(NullToString(rs.getString("������λ")));
		attr.setUserDepartment(NullToString(rs.getString("��������")));
		attr.setUserStaffType(NullToString(rs.getString("Ա������")));
		attr.setUserGender(NullToString(rs.getString("�Ա�")));
		attr.setUserBirthDate(NullToString(rs.getString("��������")));
		attr.setUserEthnic(NullToString(rs.getString("����")));
		attr.setUserHometown(NullToString(rs.getString("����")));
		attr.setUserPoliticsStatus(NullToString(rs.getString("������ò")));
		attr.setTimeForJoinParty(NullToString(rs.getString("�뵳ʱ��")));
		attr.setUserJobTitle(NullToString(rs.getString("ְ��")));
		attr.setUserPosition(NullToString(rs.getString("��λ")));
		attr.setUserTechnicalTitle(NullToString(rs.getString("ְ��")));
		attr.setUserTechnicalLevel(NullToString(rs.getString("���ܵȼ�")));
		attr.setUserIdNumber(NullToString(rs.getString("���֤��")));
		attr.setUserEmployedDate(NullToString(rs.getString("�μӹ���ʱ��")));
		attr.setUserInitialEducation(NullToString(rs.getString("��ʼѧ��")));
		attr.setUserInitialDegree(NullToString(rs.getString("��ʼѧλ")));
		attr.setUserInitialMajor(NullToString(rs.getString("��ʼѧλרҵ")));
		attr.setUserSchool(NullToString(rs.getString("��ҵԺУ")));
		attr.setUserHighestEducation(NullToString(rs.getString("���ѧ��")));
		attr.setUserHighestDegree(NullToString(rs.getString("���ѧλ")));
		attr.setUserHighestMajor(NullToString(rs.getString("���ѧλרҵ")));
		attr.setUserOfficeTel(NullToString(rs.getString("�칫�绰")));
		attr.setUserPhoneNumber(NullToString(rs.getString("�ƶ��绰")));
		attr.setUserEmail(NullToString(rs.getString("��������")));
		attr.setUserPhoto(NullToString(rs.getString("һ����Ƭ")));

//		attr.setUserGender(rs.getInt("�Ա�"));
//		attr.setBirthDate(dateToString(rs.getDate("��������")));
//		attr.setEmployedDate(dateToString(rs.getDate("����ʱ��")));
//		attr.setEducation(NullToString(rs.getString("ѧ��")));
//		attr.setSchool(NullToString(rs.getString("��ҵѧУ")));
//		attr.setGraduationDate(dateToString(rs.getDate("��ҵʱ��")));
//		attr.setPoliticsStatus(NullToString(rs.getString("������ò")));
//		attr.setJoinPartyDate(dateToString(rs.getDate("�뵳ʱ��")));
//		attr.setPartyMemberInfo(NullToString(rs.getString("��Ա��Ϣ")));
//		attr.setTechnicalTitle(NullToString(rs.getString("����ְ��")));
//		attr.setTechTitleDate(dateToString(rs.getDate("ְ������ʱ��")));
//		attr.setOfficeTel(NullToString(rs.getString("�칫�绰")));
//		attr.setHomeTel(NullToString(rs.getString("��ͥ�绰")));
//		attr.setPhoneNumber(NullToString(rs.getString("�ֻ���")));
//		attr.setEmail(NullToString(rs.getString("����")));
//		attr.setPosition(NullToString(rs.getString("��λ")));
//		attr.setPositionLevel(NullToString(rs.getString("ְ��")));
//		attr.setPhoto(NullToString(rs.getString("��Ƭ")));
//		attr.setResume(NullToString(rs.getString("���˼���")));
//		attr.setHonor(NullToString(rs.getString("��������")));
//		attr.setPersonalStatus(NullToString(rs.getString("����״̬")));
//		attr.setBak(NullToString(rs.getString("��ע")));

		return attr;
	}
	
	/**
	 * ����ת��Ϊ�ַ���
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
	 * ��null ת�� ''
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
