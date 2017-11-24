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
		news.setNewsId(rs.getString("����ID"));
		news.setNewsTitle(NullToString(rs.getString("����")));
		news.setNewsType(NullToString(rs.getString("����")));
		news.setNewsContent(NullToString(rs.getString("����")));
		news.setNewsImage(NullToString(rs.getString("ͼƬ")));
		news.setNewsAttach(NullToString(rs.getString("����")));
		news.setNewsUrl(NullToString(rs.getString("���ӵ�ַ")));
		news.setStatus(rs.getInt("״̬"));
		news.setOrder(rs.getInt("˳��"));
		news.setCreaterId(NullToString(rs.getString("������ID")));
		news.setCreater(NullToString(rs.getString("������")));
		news.setCreateTime(NullToString(dateToString(rs.getDate("����ʱ��"))));
		news.setStorePath(NullToString(rs.getString("�洢·��")));
		news.setRelPriv(NullToString(rs.getString("����Ȩ��")));
		news.setAssessorId(NullToString(rs.getString("�����ID")));
		news.setAssessor(NullToString(rs.getString("�����")));
		news.setAssessorTime(NullToString(dateToString(rs.getDate("���ʱ��"))));
		news.setDisableTime(NullToString(dateToString(rs.getDate("ʧЧʱ��"))));
		news.setBrowsePriv(NullToString(rs.getString("���Ȩ��")));
		news.setIsRedFontDisplay(NullToString(rs.getString("�Ƿ��ɫ������ʾ")));
		news.setIsNewNoticeShow(NullToString(rs.getString("�Ƿ���ʾNEW��ʶ��")));
		news.setClickCount(rs.getInt("�������"));
		news.setAuthor(NullToString(rs.getString("׫����")));
		news.setFb(NullToString(rs.getString("ý�����")));
		return news;
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
