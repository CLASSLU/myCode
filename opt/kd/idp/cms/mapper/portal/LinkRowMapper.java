package kd.idp.cms.mapper.portal;

import java.sql.ResultSet;
import java.sql.SQLException;

import kd.idp.cms.bean.portal.LinkBean;

import org.springframework.jdbc.core.RowMapper;

public class LinkRowMapper implements RowMapper<LinkBean>{

	public LinkBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		LinkBean link = new LinkBean();
		link.setLinkId(rs.getString("内容ID"));
		link.setLinkType(NullToString(rs.getString("链接类型")));
		link.setLinkName(NullToString(rs.getString("链接名")));
		link.setLinkUrl(NullToString(rs.getString("链接地址")));
		link.setOpenTarget(NullToString(rs.getString("打开方式")));
		link.setOrder(rs.getInt("顺序"));
		link.setLinkIcon(NullToString(rs.getString("图标")));
		link.setStatus(rs.getInt("状态"));
		link.setRelPriv(NullToString(rs.getString("关联权限")));
		return link;
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
