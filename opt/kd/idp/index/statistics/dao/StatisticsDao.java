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
	/**获取门户的首页访问和登录情况
	 * @param dvName,type
	 * @return List
	 */
	public List<UseStatisticsBean> getUseStatistics(String dvName,String type,Date date) throws StatisticsException{
		String sql="select * from "+TableConst.USER_STATISTICS+" where 地区='"+dvName+"' and 统计内容='"+type+"' and to_char(日期,'yyyy-MM-dd')=to_char("+"to_date('"+dateToString(date)+"','yyyy-MM-dd'"+"),'yyyy-MM-dd')";
		try{
		return getUseStatisticsList(sql);
		}
		catch(Exception e)
		{
			throw new StatisticsException("获取首页访问统计错误！ "+e.getMessage());
		}
	}
	/**获取门户的首页访问和登录的年、月统计情况
	 * @param dvName,type
	 * @return List
	 */
	public int getUseSumStatistics(String dvName,String type,Date date,int sumtype){
		String sql="select sum(使用次数) from "+TableConst.USER_STATISTICS+" where 地区='"+dvName+"' and 统计内容='"+type+"'";
		
        switch(sumtype){
        case 0:sql="select sum(使用次数) from "+TableConst.USER_STATISTICS+" where 地区='"+dvName+"' and 统计内容='"+type+"'";break;
        case 1:sql="select sum(使用次数) from "+TableConst.USER_STATISTICS+" where 地区='"+dvName+"' and 统计内容='"+type+"' and to_char(日期,'MM')=to_char("+"to_date('"+dateToString(date)+"','yyyy-MM-dd'"+"),'MM')";break;
        case 2:sql="select sum(使用次数) from "+TableConst.USER_STATISTICS+" where 地区='"+dvName+"' and 统计内容='"+type+"' and to_char(日期,'yy')=to_char("+"to_date('"+dateToString(date)+"','yyyy-MM-dd'"+"),'yy')";break;
        }
		
		
		
		
		
		return getSumStatstics(sql);
	}
	/**更新门户的访问和登录情况
	 * @param dvName,type
	 * @return List
	 */
	public boolean upUserStatistics(UseStatisticsBean useStatisticsBean,Date date) throws StatisticsException{
		boolean status=true;
		try{
		String sql="delete from "+TableConst.USER_STATISTICS+" where 地区='"+useStatisticsBean.getDv()+"' and 统计内容='"+useStatisticsBean.getStatisticsContent()+"'and to_char(日期,'yyyy-MM-dd')=to_char("+"to_date('"+dateToString(date)+"','yyyy-MM-dd'"+"),'yyyy-MM-dd')";
		DBTemplate.getInstance().updateSql(sql);
		sql="insert into "+TableConst.USER_STATISTICS+"(地区,统计内容,使用次数,备注,日期) values('"+useStatisticsBean.getDv()+"','"+useStatisticsBean.getStatisticsContent()+"',"+
		useStatisticsBean.getCount()+",'"+useStatisticsBean.getComment()+"',to_date('" + useStatisticsBean.getDate()
					+ "','YYYY-MM-DD HH:MI:SS'))";
		DBTemplate.getInstance().updateSql(sql);
		}catch(Exception e){
			throw new StatisticsException("更新首页访问统计次数错误！ "+e.getMessage());
			//status=false;
		}
		return status;
	}
	
	/**获取门户详细登录情况
	 * @param dvName,type
	 * @return List
	 */
	public List<LoginStatisticsBean> getLoginDetailsStatistics(String userId,Date date){
		String sql="select * from "+TableConst.LOGIN_STATISTICS+" where USERID='"+userId+"' and to_char(日期,'yyyy-MM-dd')=to_char("+"to_date('"+dateToString(date)+"','yyyy-MM-dd'"+"),'yyyy-MM-dd')";
		return getLogInStatisticsList(sql);
	}
	/**更新门户详细登录情况
	 * @param dvName,type
	 * @return List
	 */
	
	public boolean updateLoginDetailsStatistics(LoginStatisticsBean loginStatisticsBean,Date date){
		boolean status=true;
		try{
			String sql="delete from "+TableConst.LOGIN_STATISTICS+" where USERID='"+loginStatisticsBean.getUserId()+"' and to_char(日期,'yyyy-MM-dd')=to_char("+"to_date('"+dateToString(date)+"','yyyy-MM-dd'"+"),'yyyy-MM-dd')";
			DBTemplate.getInstance().updateSql(sql);
			sql="insert into "+TableConst.LOGIN_STATISTICS+"(地区,日期,登录人,USERID,处室,登录次数) values('"+loginStatisticsBean.getDv()+"',to_date('" + loginStatisticsBean.getDate()
					+ "','YYYY-MM-DD HH:MI:SS')"+",'"+loginStatisticsBean.getUserName()+"','"+loginStatisticsBean.getUserId()+"','"+loginStatisticsBean.getDepartment()+"',"+loginStatisticsBean.getCount()+")";
			DBTemplate.getInstance().updateSql(sql);
			}catch(Exception e){
				e.printStackTrace();
				status=false;
			}
		return status;
	}
	
	/**获取应用使用情况
	 * @param dvName,type
	 * @return List
	 */
	public List<AppStatisticsBean> getAppStatistics(String userId,String dvName,String appName,Date date){
		String sql="select * from "+TableConst.APP_STATISTICS+" where USERID='"+userId+"' and 地区='"+dvName+"' and 模块='"+appName+"' and to_char(日期,'yyyy-MM-dd')=to_char("+"to_date('"+dateToString(date)+"','yyyy-MM-dd'"+"),'yyyy-MM-dd')";
		return getAppUseStatisticsList(sql);
	}
	/**更新应用录情况
	 * @param dvName,type
	 * @return List
	 */
	public boolean updateAppStatistics(AppStatisticsBean appStatisticsBean,Date date){
		boolean status=true;
		String sql="delete from "+TableConst.APP_STATISTICS+" where USERID='"+appStatisticsBean.getUserId()+"' and 地区='"+appStatisticsBean.getDv()+"' and 模块='"+appStatisticsBean.getAppName()+"' and to_char(日期,'yyyy-MM-dd')=to_char("+"to_date('"+dateToString(date)+"','yyyy-MM-dd'"+"),'yyyy-MM-dd')";
		DBTemplate.getInstance().updateSql(sql);
		sql="insert into "+TableConst.APP_STATISTICS+"(地区,日期,登录人,USERID,处室,应用类别,模块,应用次数,模块ID,部门ID) values('"+
		appStatisticsBean.getDv()+"',"+"to_date('" + appStatisticsBean.getDate()+ "','YYYY-MM-DD HH:MI:SS')"+",'"+appStatisticsBean.getUserName()+"','"+
		appStatisticsBean.getUserId()+"','"+appStatisticsBean.getDepartment()+"','"+appStatisticsBean.getAppType()+"','"+appStatisticsBean.getAppName()+"',"+
		appStatisticsBean.getCount()+",'null','null')";
		DBTemplate.getInstance().updateSql(sql);
		return status;
		
	}
	/**
	 * @param 首页访问情况
	 * @return
	 */
	private List<UseStatisticsBean> getUseStatisticsList(String sql){
		return DBTemplate.getInstance().getResultRowMapperList(sql, new UseRowMapper());
	}
	/**
	 * @param 门户登录情况
	 * @return
	 */
	private List<LoginStatisticsBean> getLogInStatisticsList(String sql){
		return DBTemplate.getInstance().getResultRowMapperList(sql, new LogInRowMapper());
	}
	/**
	 * @param 应用使用情况
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
}
