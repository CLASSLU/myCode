package kd.idp.cms.mapper.portal;

import java.sql.ResultSet;
import java.sql.SQLException;

import kd.idp.cms.bean.portal.LinkBean;

import org.springframework.jdbc.core.RowMapper;

public class LinkRowMapper implements RowMapper<LinkBean>{

	public LinkBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		LinkBean link = new LinkBean();
		link.setLinkId(rs.getString("����ID"));
		link.setLinkType(NullToString(rs.getString("��������")));
		link.setLinkName(NullToString(rs.getString("������")));
		link.setLinkUrl(NullToString(rs.getString("���ӵ�ַ")));
		link.setOpenTarget(NullToString(rs.getString("�򿪷�ʽ")));
		link.setOrder(rs.getInt("˳��"));
		link.setLinkIcon(NullToString(rs.getString("ͼ��")));
		link.setStatus(rs.getInt("״̬"));
		link.setRelPriv(NullToString(rs.getString("����Ȩ��")));
		return link;
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
