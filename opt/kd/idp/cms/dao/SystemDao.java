package kd.idp.cms.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import kd.idp.cms.bean.PagingGridBean;
import kd.idp.cms.bean.system.DictionaryBean;
import kd.idp.cms.bean.system.TimerTaskBean;
import kd.idp.cms.mapper.system.DictionaryRowMapper;
import kd.idp.cms.mapper.system.TimerTaskRowMapper;
import kd.idp.cms.utility.PropertiesTool;
import kd.idp.common.CommonTools;
import kd.idp.common.config.ChartConfigUtil;
import kd.idp.common.config.WebConfigUtil;
import kd.idp.common.consts.TableConst;

import com.spring.dbservice.DBTemplate;
import com.spring.dbservice.DBTools;

public class SystemDao{

	
	/**
	 * 保存系统配置
	 * @param config
	 * @return
	 */
	public int saveSystemConfig(Map<String, String> config){
		return saveConfig(WebConfigUtil.getParameters(), config);
	}
	
	/**
	 * 保存曲线配置
	 * @param config
	 * @return
	 */
	public int saveChartConfig(Map<String, String> config){
		return saveConfig(ChartConfigUtil.getParameters(), config);
	}
	
	/**
	 * 保存配置
	 * @param config
	 * @return
	 */
	private int saveConfig(Map<String, String> getParameters,Map<String, String> config){
		Iterator<Entry<String, String>> it = config.entrySet().iterator();
		List<List<String>> dataList = new ArrayList<List<String>>();
		while(it.hasNext()){
			Entry<String, String> entry = it.next();
			String key = entry.getKey();
			String value = String.valueOf(entry.getValue());
			if(value != null && !value.equals(getParameters.get(key))){
				List<String> item = new ArrayList<String>();
				item.add(value);
				item.add(key);
				dataList.add(item);
			}
		}
		String sql = "UPDATE "+TableConst.TABLE_CONFIG_MANAGE+" SET 属性值 = ? WHERE 属性 = ?";
		int[] res = DBTemplate.getInstance().batchPreparedUpdate(sql, dataList);
		return (res.length>0)?res[0]:0;
	}
	
	
	/**
	 * 查询系统字典
	 * @param type
	 * @return
	 */
	public List<DictionaryBean> getSystemDictionary(String type){
		String sql = "SELECT * FROM "+TableConst.TABLE_DICTIONARY+" WHERE 分类 = '"+type+"' ORDER BY 顺序";
		return DBTemplate.getInstance().getResultRowMapperList(sql, new DictionaryRowMapper());
	}
	
	/**
	 * 查询系统字典
	 * @return
	 */
	public Map<String, List<DictionaryBean>> getSystemDictionary(){
		Map<String, List<DictionaryBean>> map = new HashMap<String, List<DictionaryBean>>();
		String sql = "SELECT * FROM "+TableConst.TABLE_DICTIONARY+" ORDER BY 分类,顺序";
		List<DictionaryBean> list = DBTemplate.getInstance().getResultRowMapperList(sql, new DictionaryRowMapper());
		for(int i=0;i<list.size();i++){
			DictionaryBean dic = list.get(i);
			if(map.get(dic.getType()) == null){
				map.put(dic.getType(), new ArrayList<DictionaryBean>());
			}
			map.get(dic.getType()).add(dic);
		}
		return map;
	}
	
	
	/**
	 * 获得系统定时器
	 * @return
	 */
	public List<TimerTaskBean> getTimerTaskList(){
		String type = PropertiesTool.getValueByKey("biaozhi", "/home/d5000/huazhong/psidp/", "Psoms_Conf.properties");
		if(type == null || type.equals("")){
			type = "12315";
		}
		System.out.println("[   " + type +"   ]");
		String sql = "SELECT * FROM "+TableConst.TABLE_CONFIG_TIMER+" WHERE 状态 = " + type;
		return DBTemplate.getInstance().getResultRowMapperList(sql, new TimerTaskRowMapper());
	}
	
	
	public static void main(String[] args) {
		String type = "1";
		type = PropertiesTool.getValueByKey("biaozhi", "/home/d5000/huazhong/psidp/", "Psoms_Conf.properties");
		System.out.println(type);
	}
	/**
	 * 查询定时器分页数据
	 * @param pageSize
	 * @param pageNum
	 * @param filtersql
	 * @return
	 */
	public PagingGridBean<TimerTaskBean> getTimerConfigList(int pageSize, int pageNum, String filtersql){
		String sql = "SELECT * FROM " + TableConst.TABLE_CONFIG_TIMER;
		if (filtersql != null && !"".equals(filtersql)) {
			sql += "WHERE " + filtersql;
			pageNum = 1;
		}
		return DBTools.getPagingData(sql, pageSize, pageNum, new TimerTaskRowMapper(),
				true);
	}
	
	
	/**
	 * 刪除定时器
	 * @param timelist
	 * @return
	 */
	public int deleteTimers(List<TimerTaskBean> timelist){
		String sql = "DELETE FROM "+TableConst.TABLE_CONFIG_TIMER+" WHERE 任务ID = ?";
		List<List<String>> dataList = new ArrayList<List<String>>();
		for (int i = 0; i < timelist.size(); i++) {
			List<String> item = new ArrayList<String>();
			item.add(timelist.get(i).getTaskId());
			dataList.add(item);
		}
		return DBTemplate.getInstance().batchPreparedUpdate(sql, dataList).length;
	}
	
	/**
	 * 新增定时器任务
	 * @param timerTask
	 * @return
	 */
	public int addNewTimerTask(TimerTaskBean timerTask){
		String timerid = CommonTools.createId("TIMER");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "INSERT INTO "
				+ TableConst.TABLE_CONFIG_TIMER
				+ " (任务ID,任务名称,类名称,方法名,定时类型,状态,启动时间,停止时间,周期,时间,单位,任务描述) VALUES('"
				+ timerid + "','" + timerTask.getTaskName() + "','"
				+ timerTask.getClassName() + "','" + timerTask.getMethodName()
				+ "','" + timerTask.getTimerType() + "',"
				+ timerTask.getStatus() + ",to_date('"
				+ sdf.format(timerTask.getStartTime())
				+ "','yyyy-MM-dd HH24:mi:ss'),to_date('"
				+ sdf.format(timerTask.getStopTime())
				+ "','yyyy-MM-dd HH24:mi:ss')," + timerTask.getPeriod()
				+ ",sysdate,'" + timerTask.getUnits() + "','"
				+ timerTask.getTaskDesc() + "')";
		return DBTemplate.getInstance().updateSql(sql);
	}
	
	
	/**
	 * 修改定时器任务
	 * @param timerTask
	 * @return
	 */
	public int modTimerTask(TimerTaskBean timerTask){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String endtime = "''";
		if (timerTask.getStopTime() != null) {
			endtime = "to_date('" + sdf.format(timerTask.getStopTime())
					+ "','yyyy-MM-dd HH24:mi:ss')";
		}
		String sql = "UPDATE " + TableConst.TABLE_CONFIG_TIMER + " SET 任务名称='"
				+ timerTask.getTaskName() + "',类名称='"
				+ timerTask.getClassName() + "',方法名='"
				+ timerTask.getMethodName() + "',定时类型='"
				+ timerTask.getTimerType() + "',状态=" + timerTask.getStatus()
				+ ",启动时间=to_date('" + sdf.format(timerTask.getStartTime())
				+ "','yyyy-MM-dd HH24:mi:ss'),停止时间=" + endtime + ",周期="
				+ timerTask.getPeriod() + ",时间=sysdate,单位='"
				+ timerTask.getUnits() + "',任务描述='" + timerTask.getTaskDesc()
				+ "' WHERE 任务ID='" + timerTask.getTaskId() + "'";
		return DBTemplate.getInstance().updateSql(sql);
	}

}
