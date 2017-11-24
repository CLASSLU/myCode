package kd.idp.cms.mapper.portal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;

import kd.idp.cms.bean.portal.NewsBean;

public class NewsRowMapper implements RowMapper<NewsBean>{

	
	private SimpleDateFormat sdf = null;
	
	public NewsRowMapper() {
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	public NewsBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		NewsBean news = new NewsBean();
		news.setNewsId(rs.getString("内容ID"));
		news.setNewsTitle(NullToString(rs.getString("标题")));
		news.setNewsType(NullToString(rs.getString("类型")));
		news.setNewsContent(NullToString(rs.getString("内容")));
		news.setNewsImage(NullToString(rs.getString("图片")));
		news.setNewsAttach(NullToString(rs.getString("附件")));
		news.setNewsUrl(NullToString(rs.getString("链接地址")));
		news.setStatus(rs.getInt("状态"));
		news.setOrder(rs.getInt("顺序"));
		news.setCreaterId(NullToString(rs.getString("创建人ID")));
		news.setCreater(NullToString(rs.getString("创建人")));
		news.setCreateTime(NullToString(dateToString(rs.getDate("创建时间"))));
		news.setStorePath(NullToString(rs.getString("存储路径")));
		news.setRelPriv(NullToString(rs.getString("关联权限")));
		news.setAssessorId(NullToString(rs.getString("审核人ID")));
		news.setAssessor(NullToString(rs.getString("审核人")));
		news.setAssessorTime(NullToString(dateToString(rs.getDate("审核时间"))));
		news.setDisableTime(NullToString(dateToString(rs.getDate("失效时间"))));
		news.setBrowsePriv(NullToString(rs.getString("浏览权限")));
		news.setIsRedFontDisplay(NullToString(rs.getString("是否红色字体显示")));
		news.setIsNewNoticeShow(NullToString(rs.getString("是否显示NEW标识符")));
		news.setClickCount(rs.getInt("点击次数"));
		news.setAuthor(NullToString(rs.getString("撰稿人")));
		news.setFb(NullToString(rs.getString("媒体类别")));
		return news;
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
