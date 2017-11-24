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

		attr.setUserName(NullToString(rs.getString("����")));
		attr.setUserNamePinyin(NullToString(rs.getString("����ȫƴ")));
		attr.setUserCompany(NullToString(rs.getString("������λ")));
		attr.setUserGender(NullToString(rs.getString("�Ա�")));
		attr.setUserBirthDate(NullToString(rs.getString("��������")));
		attr.setUserType(NullToString(rs.getString("��Ա����")));
		attr.setUserJobTitle(NullToString(rs.getString("ְ��")));
		attr.setUserMajor(NullToString(rs.getString("רҵ")));
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
