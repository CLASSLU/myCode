package kd.idp.index.statistics.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import kd.idp.common.consts.TableConst;
import kd.idp.index.statistics.bean.AppStatisticsBean;
import kd.idp.index.statistics.bean.LoginStatisticsBean;
import kd.idp.index.statistics.bean.UseStatisticsBean;
import kd.idp.index.statistics.exception.StatisticsException;
import kd.idp.index.statistics.mapper.AppUseRowMapper;
import kd.idp.index.statistics.mapper.CountMapper;
import kd.idp.index.statistics.mapper.LogInRowMapper;
import kd.idp.index.statistics.mapper.UseRowMapper;

import com.spring.dbservice.DBTemplate;

@Transactional
public class StatisticsDao{
	private SimpleDateFormat sdf = null;
	public StatisticsDao() {
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	/**��ȡ�Ż�����ҳ���ʺ͵�¼���
	 * @param dvName,type
	 * @return List
	 */
	public List<UseStatisticsBean> getUseStatistics(String dvName,String type,Date date) throws StatisticsException{
		String sql="select * from "+TableConst.USER_STATISTICS+" where ����='"+dvName+"' and ͳ������='"+type+"' and to_char(����,'yyyy-MM-dd')=to_char("+"to_date('"+dateToString(date)+"','yyyy-MM-dd'"+"),'yyyy-MM-dd')";
		try{
		return getUseStatisticsList(sql);
		}
		catch(Exception e)
		{
			throw new StatisticsException("��ȡ��ҳ����ͳ�ƴ��� "+e.getMessage());
		}
	}
	/**��ȡ�Ż�����ҳ���ʺ͵�¼���ꡢ��ͳ�����
	 * @param dvName,type
	 * @return List
	 */
	public int getUseSumStatistics(String dvName,String type,Date date,int sumtype){
		String sql="select sum(ʹ�ô���) from "+TableConst.USER_STATISTICS+" where ����='"+dvName+"' and ͳ������='"+type+"'";
		
        switch(sumtype){
        case 0:sql="select sum(ʹ�ô���) from "+TableConst.USER_STATISTICS+" where ����='"+dvName+"' and ͳ������='"+type+"'";break;
        case 1:sql="select sum(ʹ�ô���) from "+TableConst.USER_STATISTICS+" where ����='"+dvName+"' and ͳ������='"+type+"' and to_char(����,'MM')=to_char("+"to_date('"+dateToString(date)+"','yyyy-MM-dd'"+"),'MM')";break;
        case 2:sql="select sum(ʹ�ô���) from "+TableConst.USER_STATISTICS+" where ����='"+dvName+"' and ͳ������='"+type+"' and to_char(����,'yy')=to_char("+"to_date('"+dateToString(date)+"','yyyy-MM-dd'"+"),'yy')";break;
        }
		
		
		
		
		
		return getSumStatstics(sql);
	}
	/**�����Ż��ķ��ʺ͵�¼���
	 * @param dvName,type
	 * @return List
	 */
	public boolean upUserStatistics(UseStatisticsBean useStatisticsBean,Date date) throws StatisticsException{
		boolean status=true;
		try{
		String sql="delete from "+TableConst.USER_STATISTICS+" where ����='"+useStatisticsBean.getDv()+"' and ͳ������='"+useStatisticsBean.getStatisticsContent()+"'and to_char(����,'yyyy-MM-dd')=to_char("+"to_date('"+dateToString(date)+"','yyyy-MM-dd'"+"),'yyyy-MM-dd')";
		DBTemplate.getInstance().updateSql(sql);
		sql="insert into "+TableConst.USER_STATISTICS+"(����,ͳ������,ʹ�ô���,��ע,����) values('"+useStatisticsBean.getDv()+"','"+useStatisticsBean.getStatisticsContent()+"',"+
		useStatisticsBean.getCount()+",'"+useStatisticsBean.getComment()+"',to_date('" + useStatisticsBean.getDate()
					+ "','YYYY-MM-DD HH:MI:SS'))";
		DBTemplate.getInstance().updateSql(sql);
		}catch(Exception e){
			throw new StatisticsException("������ҳ����ͳ�ƴ������� "+e.getMessage());
			//status=false;
		}
		return status;
	}
	
	/**��ȡ�Ż���ϸ��¼���
	 * @param dvName,type
	 * @return List
	 */
	public List<LoginStatisticsBean> getLoginDetailsStatistics(String userId,Date date){
		String sql="select * from "+TableConst.LOGIN_STATISTICS+" where USERID='"+userId+"' and to_char(����,'yyyy-MM-dd')=to_char("+"to_date('"+dateToString(date)+"','yyyy-MM-dd'"+"),'yyyy-MM-dd')";
		return getLogInStatisticsList(sql);
	}
	/**�����Ż���ϸ��¼���
	 * @param dvName,type
	 * @return List
	 */
	
	public boolean updateLoginDetailsStatistics(LoginStatisticsBean loginStatisticsBean,Date date){
		boolean status=true;
		try{
			String sql="delete from "+TableConst.LOGIN_STATISTICS+" where USERID='"+loginStatisticsBean.getUserId()+"' and to_char(����,'yyyy-MM-dd')=to_char("+"to_date('"+dateToString(date)+"','yyyy-MM-dd'"+"),'yyyy-MM-dd')";
			DBTemplate.getInstance().updateSql(sql);
			sql="insert into "+TableConst.LOGIN_STATISTICS+"(����,����,��¼��,USERID,����,��¼����) values('"+loginStatisticsBean.getDv()+"',to_date('" + loginStatisticsBean.getDate()
					+ "','YYYY-MM-DD HH:MI:SS')"+",'"+loginStatisticsBean.getUserName()+"','"+loginStatisticsBean.getUserId()+"','"+loginStatisticsBean.getDepartment()+"',"+loginStatisticsBean.getCount()+")";
			DBTemplate.getInstance().updateSql(sql);
			}catch(Exception e){
				e.printStackTrace();
				status=false;
			}
		return status;
	}
	
	/**��ȡӦ��ʹ�����
	 * @param dvName,type
	 * @return List
	 */
	public List<AppStatisticsBean> getAppStatistics(String userId,String dvName,String appName,Date date){
		String sql="select * from "+TableConst.APP_STATISTICS+" where USERID='"+userId+"' and ����='"+dvName+"' and ģ��='"+appName+"' and to_char(����,'yyyy-MM-dd')=to_char("+"to_date('"+dateToString(date)+"','yyyy-MM-dd'"+"),'yyyy-MM-dd')";
		return getAppUseStatisticsList(sql);
	}
	/**����Ӧ��¼���
	 * @param dvName,type
	 * @return List
	 */
	public boolean updateAppStatistics(AppStatisticsBean appStatisticsBean,Date date){
		boolean status=true;
		String sql="delete from "+TableConst.APP_STATISTICS+" where USERID='"+appStatisticsBean.getUserId()+"' and ����='"+appStatisticsBean.getDv()+"' and ģ��='"+appStatisticsBean.getAppName()+"' and to_char(����,'yyyy-MM-dd')=to_char("+"to_date('"+dateToString(date)+"','yyyy-MM-dd'"+"),'yyyy-MM-dd')";
		DBTemplate.getInstance().updateSql(sql);
		sql="insert into "+TableConst.APP_STATISTICS+"(����,����,��¼��,USERID,����,Ӧ�����,ģ��,Ӧ�ô���,ģ��ID,����ID) values('"+
		appStatisticsBean.getDv()+"',"+"to_date('" + appStatisticsBean.getDate()+ "','YYYY-MM-DD HH:MI:SS')"+",'"+appStatisticsBean.getUserName()+"','"+
		appStatisticsBean.getUserId()+"','"+appStatisticsBean.getDepartment()+"','"+appStatisticsBean.getAppType()+"','"+appStatisticsBean.getAppName()+"',"+
		appStatisticsBean.getCount()+",'null','null')";
		DBTemplate.getInstance().updateSql(sql);
		return status;
		
	}
	/**
	 * @param ��ҳ�������
	 * @return
	 */
	private List<UseStatisticsBean> getUseStatisticsList(String sql){
		return DBTemplate.getInstance().getResultRowMapperList(sql, new UseRowMapper());
	}
	/**
	 * @param �Ż���¼���
	 * @return
	 */
	private List<LoginStatisticsBean> getLogInStatisticsList(String sql){
		return DBTemplate.getInstance().getResultRowMapperList(sql, new LogInRowMapper());
	}
	/**
	 * @param Ӧ��ʹ�����
	 * @return
	 */
	private List<AppStatisticsBean> getAppUseStatisticsList(String sql){
		return DBTemplate.getInstance().getResultRowMapperList(sql, new AppUseRowMapper());
	}
	/**
	 * @param sql
	 * @return
	 */
	private Integer getSumStatstics(String sql){
		Integer count = 0;
		count = DBTemplate.getInstance().getResultRowMapper(sql,
				new CountMapper());
		return count;
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
}
