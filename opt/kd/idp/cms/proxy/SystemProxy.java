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
//		System.out.println("��ǰ��¼�û�: "
//				+ ((user != null) ? user.getUserDisplayName() : "δ��¼"));
	}

	public SystemProxy(UserBean _user) {
		user = _user;
//		System.out.println("���õ�ǰ��¼�û�: "
//				+ ((user != null) ? user.getUserDisplayName() : "δ��¼"));
	}

	/**
	 * ���ϵͳ ������Ϣ
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
	 * ����������Ϣ
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
				//���� ����ͬ�� WS ��ַ
//				new SyncWebService().resetUrl();
			}
			result.setStatus(FlexConst.RESULT_OK);
			result.setMessage("����ɹ�!");
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}

	/**
	 * ��ѯϵͳ�ֵ�
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
	 * ��ѯϵͳ�ֵ�
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
	 * ��ѯ��ʱ����ҳ����
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
	 * ɾ����ʱ��
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
			result.setMessage("ִ�гɹ�,ɾ��" + sysdao.deleteTimers(timerList) + " ����¼!");
			result.setStatus(FlexConst.RESULT_OK);
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}
	
	/**
	 * �޸Ķ�ʱ������
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
				result.setMessage("ִ�гɹ�,����" + sysdao.addNewTimerTask(timerTask) + " ����¼!");
			}else{
				result.setMessage("ִ�гɹ�,�޸�" + sysdao.modTimerTask(timerTask) + " ����¼!");
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
	 * ��ѯ����������Ϣ
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
	 * ��������������Ϣ
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
			result.setMessage("����ɹ�!");
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(FlexConst.RESULT_FAIL);
			result.setMessage(e.getMessage());
		}
		return result;
	}

}
