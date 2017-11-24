package kd.idp.cms.mapper.priv;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kd.idp.cms.bean.priv.UserBean;

import org.springframework.jdbc.core.RowMapper;

public class UserRowMapper implements RowMapper<UserBean>{
	
	private SimpleDateFormat sdf = null;
	
	public UserRowMapper() {
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	public UserRowMapper(String pattern) {
		if(pattern != null && !"".equals(pattern)){
			sdf = new SimpleDateFormat(pattern);
		}else{
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		}
	}
	

	public UserBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserBean user = new UserBean();
		user.setUserId(rs.getString("�û�ID"));
		user.setUserName(rs.getString("��¼��"));
		user.setUserDisplayName(NullToString(rs.getString("��ʾ��")));
		user.setUserPwd(NullToString(rs.getString("����")));
		user.setUserOrgId(NullToString(rs.getString("��֯����ID")));
		user.setUserOrgName(NullToString(rs.getString("��֯��������")));
		user.setDisable(rs.getInt("�Ƿ񶳽�"));
		user.setUserOrder(rs.getInt("˳��"));
		user.setAudit(rs.getInt("�Ƿ����"));
		user.setVertime(dateToString(rs.getDate("����ʱ��")));
		user.setUserCompanyId(NullToString(rs.getString("��λID")));
		user.setUserCompanyName(NullToString(rs.getString("��λ����")));
		try {
			//��ʱ����Щsqlû��������λ���ͣ����������޸�
			user.setBak(NullToString(rs.getString("��ע")));
			user.setCompanyType(NullToString(rs.getString("������λ����")));
		} catch (Exception e) {
			try{
				user.setCompanyType(NullToString(rs.getString("������λ����")));
				user.setBak(NullToString(rs.getString("��ע")));
			}catch (Exception e1) {
				return user;
			}
			return user;
		}
		
//		UserAttrBean attr= new UserAttrBean();
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
//		user.setAttr(attr);
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
