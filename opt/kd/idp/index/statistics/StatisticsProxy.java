package kd.idp.index.statistics;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import kd.idp.cms.bean.priv.OrgBean;
import kd.idp.cms.bean.priv.PrivBean;
import kd.idp.cms.bean.priv.UserBean;
import kd.idp.cms.client.UserPrivClient;
import kd.idp.cms.dao.UserDao;
import kd.idp.index.statistics.bean.AppStatisticsBean;
import kd.idp.index.statistics.bean.LoginStatisticsBean;
import kd.idp.index.statistics.bean.UseStatisticsBean;
import kd.idp.index.statistics.dao.StatisticsDao;
import kd.idp.index.statistics.exception.StatisticsException;

import com.spring.ServiceManager;

public class StatisticsProxy {

	/**
	 * 统计门户每日的访问和登录情况
	 * 
	 * @param dvName,type,date
	 * @return boolean
	 */
	public static UseStatisticsBean indexStatistics(String dv, String type,
			Date date, boolean update) throws StatisticsException {

		StatisticsDao statisticsDao = ServiceManager.getStatisticsDao();
		try {
			List<UseStatisticsBean> useStatisticsList = statisticsDao
					.getUseStatistics(dv, type, date);
			UseStatisticsBean useStatisticsBean = null;
			if (useStatisticsList.size() > 0) {
				useStatisticsBean = useStatisticsList.get(0);

				useStatisticsBean.setDate(dateToString(date));

				if (update) {
					useStatisticsBean
							.setCount(useStatisticsBean.getCount() + 1);
					useStatisticsBean.setTotal_count(statisticsDao
							.getUseSumStatistics(dv, type, date, 0) + 1);
					useStatisticsBean.setM_count(statisticsDao
							.getUseSumStatistics(dv, type, date, 1) + 1);
					useStatisticsBean.setY_count(statisticsDao
							.getUseSumStatistics(dv, type, date, 2) + 1);
					statisticsDao.upUserStatistics(useStatisticsBean, date);
				} else {
					useStatisticsBean.setCount(useStatisticsBean.getCount());
					useStatisticsBean.setTotal_count(statisticsDao
							.getUseSumStatistics(dv, type, date, 0));
					useStatisticsBean.setM_count(statisticsDao
							.getUseSumStatistics(dv, type, date, 1));
					useStatisticsBean.setY_count(statisticsDao
							.getUseSumStatistics(dv, type, date, 2));
				}
			} else {
				useStatisticsBean = new UseStatisticsBean();
				useStatisticsBean.setDv(dv);
				useStatisticsBean.setStatisticsContent(type);
				useStatisticsBean.setCount(1);
				useStatisticsBean.setDate(dateToString(date));
				useStatisticsBean.setTotal_count(statisticsDao
						.getUseSumStatistics(dv, type, date, 0) + 1);
				useStatisticsBean.setM_count(statisticsDao.getUseSumStatistics(
						dv, type, date, 1) + 1);
				useStatisticsBean.setY_count(statisticsDao.getUseSumStatistics(
						dv, type, date, 2) + 1);
				if (update)
					statisticsDao.upUserStatistics(useStatisticsBean, date);
			}
			return useStatisticsBean;
		} catch (StatisticsException e) {
			throw new StatisticsException("首页访问统计次数错误！ " + e.getMessage());
		}

	}

	/**
	 * 统计门户每日的访问和登录情况
	 * 
	 * @param dvName,type,date
	 * @return boolean
	 */
	public static boolean loginDetailStatistics(String dv, String depart,
			UserBean user, Date date) {
		boolean status = false;
		StatisticsDao statisticsDao = ServiceManager.getStatisticsDao();
		List<LoginStatisticsBean> loginStatisticsList = statisticsDao
				.getLoginDetailsStatistics(user.getUserId(), date);
		if (loginStatisticsList.size() > 0) {
			LoginStatisticsBean loginStatisticsBean = loginStatisticsList
					.get(0);
			loginStatisticsBean.setCount(loginStatisticsBean.getCount() + 1);
			statisticsDao.updateLoginDetailsStatistics(loginStatisticsBean,
					date);
		} else {
			LoginStatisticsBean loginStatisticsBean = new LoginStatisticsBean();
			loginStatisticsBean.setDv(dv);
			loginStatisticsBean.setDate(dateToString(date));
			loginStatisticsBean.setUserName(user.getUserDisplayName());
			loginStatisticsBean.setUserId(user.getUserId());
			loginStatisticsBean.setDepartment(depart);
			loginStatisticsBean.setCount(1);
			statisticsDao.updateLoginDetailsStatistics(loginStatisticsBean,
					date);

		}

		return status;
	}

	/**
	 * 统计应用使用情况
	 * 
	 * @param date
	 * @return
	 */
	public static boolean appStatistics(String userId, String appId, Date date) {
		boolean status = true;
		StatisticsDao statisticsDao = ServiceManager.getStatisticsDao();
		UserBean u = new UserDao().getUserFromID(userId);// 获取用户
		OrgBean orgBean = new UserPrivClient(u).getCompany();// 获取用户单位
		String depart = "";
		List<OrgBean> orglist = ServiceManager.getOrgDao().getOrgInfo(
				u.getUserOrgId());// 获取用户部门
		if (orglist.size() > 0) {
			OrgBean orgBean1 = orglist.get(0);
			depart = orgBean1.getOrgName();
		}
		PrivBean privBean = ServiceManager.getPrivDao().getPrivInfo(appId);
		List<AppStatisticsBean> list = statisticsDao.getAppStatistics(userId,
				orgBean.getOrgName(), privBean.getPrivName(), date);
		if (list.size() > 0) {
			AppStatisticsBean appStatisticsBean = list.get(0);
			appStatisticsBean.setCount(appStatisticsBean.getCount() + 1);
			statisticsDao.updateAppStatistics(appStatisticsBean, date);
		} else {

			PrivBean parentPrivBean = ServiceManager.getPrivDao().getPrivInfo(
					privBean.getPrivParentId());
			AppStatisticsBean appStatisticsBean = new AppStatisticsBean();
			appStatisticsBean.setDv(orgBean.getOrgName());
			appStatisticsBean.setDate(dateToString(date));
			appStatisticsBean.setUserId(u.getUserId());
			appStatisticsBean.setUserName(u.getUserDisplayName());
			appStatisticsBean.setDepartment(depart);
			appStatisticsBean.setAppName(privBean.getPrivName());
			appStatisticsBean.setAppType(parentPrivBean.getPrivName());
			appStatisticsBean.setCount(1);
			statisticsDao.updateAppStatistics(appStatisticsBean, date);
		}

		return status;
	}

	/**
	 * 日期转换为字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date) {
		try {
			if (date != null) {
				return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
