package kd.idp.cms.proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kd.idp.cms.bean.PagingGridBean;
import kd.idp.cms.bean.priv.UserBean;
import kd.idp.cms.bean.system.DictionaryBean;
import kd.idp.cms.bean.system.TimerTaskBean;
import kd.idp.cms.dao.SystemDao;
import kd.idp.cms.utility.TimerTaskUtil;
import kd.idp.common.config.ChartConfigUtil;
import kd.idp.common.config.WebConfigUtil;
import kd.idp.common.flex.FlexConst;
import kd.idp.common.flex.FlexResult;
import kd.idp.common.flex.FlexSessionInvalidException;
import kd.idp.common.flex.FlexUtil;

import com.spring.ServiceManager;

public class SystemProxy {

	private UserBean user = null;


	public SystemProxy() {
		user = FlexUtil.getFlexCurrentUser();
//		System.out.println("当前登录用户: "
//				+ ((user != null) ? user.getUserDisplayName() : "未登录"));
	}

	public SystemProxy(UserBean _user) {
		user = _user;
//		System.out.println("设置当前登录用户: "
//				+ ((user != null) ? user.getUserDisplayName() : "未登录"));
	}

	/**
	 * 获得系统 配置信息
	 * 
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public Map<String, String> getSystemConfig(Boolean reload)
			throws FlexSessionInvalidException {
		if(FlexUtil.checkSession){
			if(user == null){
				throw new FlexSessionInvalidException();
			}
		}
		if (reload) {
			WebConfigUtil.loadConfigParameter();
		}
		return WebConfigUtil.getParameters();
	}

	/**
	 * 保存配置信息
	 * 
	 * @param config
	 * @return
	 */
	public FlexResult saveSystemConfig(Map<String, String> config)
			throws FlexSessionInvalidException {
		if(FlexUtil.checkSession){
			if(user == null){
				throw new FlexSessionInvalidException();
			}
		}
		FlexResult result = new FlexResult();
		try {
			SystemDao sysdao = ServiceManager.getSystemDao();
			if (sysdao.saveSystemConfig(config) > 0) {
				WebConfigUtil.loadConfigParameter();
				//更新 新闻同步 WS 地址
//				new SyncWebService().resetUrl();
			}
			result.setStatus(FlexConst.RESULT_OK);
			result.setMessage("保存成功!");
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}

	/**
	 * 查询系统字典
	 * 
	 * @param type
	 * @return
	 */
	public List<DictionaryBean> getSystemDictionary(String type)
			throws FlexSessionInvalidException {
		if(FlexUtil.checkSession){
			if(user == null){
				throw new FlexSessionInvalidException();
			}
		}
		List<DictionaryBean> dics = new ArrayList<DictionaryBean>();
		try {
			SystemDao sysdao = ServiceManager.getSystemDao();
			dics = sysdao.getSystemDictionary(type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dics;
	}

	/**
	 * 查询系统字典
	 * 
	 * @return
	 */
	public Map<String, List<DictionaryBean>> getSystemDictionary()
			throws FlexSessionInvalidException {
		if(FlexUtil.checkSession){
			if(user == null){
				throw new FlexSessionInvalidException();
			}
		}
		try {
			SystemDao sysdao = ServiceManager.getSystemDao();
			return sysdao.getSystemDictionary();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * 查询定时器分页数据
	 * @param pageNum
	 * @param filter
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public PagingGridBean<TimerTaskBean> getTimerConfigList(int pageNum,String filter) throws FlexSessionInvalidException{
		if(FlexUtil.checkSession){
			if(user == null){
				throw new FlexSessionInvalidException();
			}
		}
		PagingGridBean<TimerTaskBean> timerGrid = new PagingGridBean<TimerTaskBean>();
		try {
			SystemDao sysdao = ServiceManager.getSystemDao();
			timerGrid = sysdao.getTimerConfigList(WebConfigUtil.getFlexGridPagingCount(), pageNum, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return timerGrid;
	}
	
	
	/**
	 * 删除定时器
	 * @param timerList
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public FlexResult deleteTimers(List<TimerTaskBean> timerList) throws FlexSessionInvalidException{
		if(FlexUtil.checkSession){
			if(user == null){
				throw new FlexSessionInvalidException();
			}
		}
		FlexResult result = new FlexResult();
		try {
			SystemDao sysdao = ServiceManager.getSystemDao();
			result.setMessage("执行成功,删除" + sysdao.deleteTimers(timerList) + " 条记录!");
			result.setStatus(FlexConst.RESULT_OK);
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 修改定时器任务
	 * @param timerTask
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public FlexResult saveTimerTask(TimerTaskBean timerTask) throws FlexSessionInvalidException{
		if(FlexUtil.checkSession){
			if(user == null){
				throw new FlexSessionInvalidException();
			}
		}
		FlexResult result = new FlexResult();
		try {
			SystemDao sysdao = ServiceManager.getSystemDao();
			if(timerTask.getTaskId() == null || "".equals(timerTask.getTaskId())){
				result.setMessage("执行成功,新增" + sysdao.addNewTimerTask(timerTask) + " 条记录!");
			}else{
				result.setMessage("执行成功,修改" + sysdao.modTimerTask(timerTask) + " 条记录!");
			}
			result.setStatus(FlexConst.RESULT_OK);
			if(timerTask.getStatus() == 1){
				TimerTaskUtil.reloadTimerTask(timerTask);
			}else{
				TimerTaskUtil.removeTimerTask(timerTask);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * 查询曲线配置信息
	 * @param reload
	 * @return
	 * @throws FlexSessionInvalidException
	 */
	public Map<String, String> getChartConfig(Boolean reload)
		throws FlexSessionInvalidException {
		if(FlexUtil.checkSession){
			if(user == null){
				throw new FlexSessionInvalidException();
			}
		}
		if (reload) {
			ChartConfigUtil.loadConfigParameter();
		}
		return ChartConfigUtil.getParameters();
	}
	
	/**
	 * 保存曲线配置信息
	 * 
	 * @param config
	 * @return
	 */
	public FlexResult saveChartConfig(Map<String, String> config)
			throws FlexSessionInvalidException {
		if(FlexUtil.checkSession){
			if(user == null){
				throw new FlexSessionInvalidException();
			}
		}
		FlexResult result = new FlexResult();
		try {
			SystemDao sysdao = ServiceManager.getSystemDao();
			if (sysdao.saveChartConfig(config) > 0) {
				ChartConfigUtil.loadConfigParameter();
			}
			result.setStatus(FlexConst.RESULT_OK);
			result.setMessage("保存成功!");
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}

}
