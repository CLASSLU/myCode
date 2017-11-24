package kd.idp.cms.mapper.portal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kd.idp.cms.bean.portal.SourceBean;

import org.springframework.jdbc.core.RowMapper;

public class SourceRowMapper implements RowMapper<SourceBean>{

	private SimpleDateFormat sdf = null;
	
	public SourceRowMapper() {
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	public SourceBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		SourceBean source = new SourceBean();
		source.setSourceId(rs.getString("����ID"));
		source.setSourceName(NullToString(rs.getString("�ĵ�����")),true);
		source.setSourceType(NullToString(rs.getString("�ĵ�����")));
		source.setFileName(NullToString(rs.getString("�ļ�����")));
		source.setSourceDetail(NullToString(rs.getString("�ĵ�˵��")));
		source.setFileSize(rs.getDouble("�ļ���С"));
		source.setStorePath(NullToString(rs.getString("�洢Ŀ¼")));
		source.setRelPriv(NullToString(rs.getString("����Ȩ��")));
		source.setCreaterId(NullToString(rs.getString("������ID")));
		source.setCreater(NullToString(rs.getString("������")));
		source.setCreateTime(NullToString(dateToString(rs.getDate("����ʱ��"))));
		source.setAssessorId(NullToString(rs.getString("�����ID")));
		source.setAssessor(NullToString(rs.getString("�����")));
		source.setAssessorTime(NullToString(dateToString(rs.getDate("���ʱ��"))));
		source.setStatus(rs.getInt("״̬"));
		source.setOrder(rs.getInt("˳��"));
		return source;
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
