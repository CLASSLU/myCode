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
		user.setUserId(rs.getString("�û�ID"));
		user.setUserName(rs.getString("��¼��"));
		user.setUserDisplayName(NullToString(rs.getString("��ʾ��")));
		user.setUserPwd(NullToString(rs.getString("����")));
		user.setUserCompanyId(NullToString(rs.getString("��λID")));
		user.setUserCompanyName(NullToString(rs.getString("��λ����")));
		user.setUserOrgId(NullToString(rs.getString("��֯����ID")));
		user.setUserOrgName(NullToString(rs.getString("��֯��������")));
		user.setDisable(rs.getInt("�Ƿ񶳽�"));
		user.setUserOrder(rs.getInt("˳��"));
		user.setAudit(rs.getInt("�Ƿ����"));
		user.setVertime(dateToString(rs.getDate("����ʱ��")));
		user.setCompanyType(NullToString(rs.getString("������λ����")));
//		attr.setUserBirthDate(NullToString(rs.getString("��������")));
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

		return user;
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
