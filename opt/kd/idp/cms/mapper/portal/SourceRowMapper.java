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
		source.setSourceId(rs.getString("内容ID"));
		source.setSourceName(NullToString(rs.getString("文档名称")),true);
		source.setSourceType(NullToString(rs.getString("文档类型")));
		source.setFileName(NullToString(rs.getString("文件名称")));
		source.setSourceDetail(NullToString(rs.getString("文档说明")));
		source.setFileSize(rs.getDouble("文件大小"));
		source.setStorePath(NullToString(rs.getString("存储目录")));
		source.setRelPriv(NullToString(rs.getString("关联权限")));
		source.setCreaterId(NullToString(rs.getString("创建人ID")));
		source.setCreater(NullToString(rs.getString("创建人")));
		source.setCreateTime(NullToString(dateToString(rs.getDate("创建时间"))));
		source.setAssessorId(NullToString(rs.getString("审核人ID")));
		source.setAssessor(NullToString(rs.getString("审核人")));
		source.setAssessorTime(NullToString(dateToString(rs.getDate("审核时间"))));
		source.setStatus(rs.getInt("状态"));
		source.setOrder(rs.getInt("顺序"));
		return source;
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
