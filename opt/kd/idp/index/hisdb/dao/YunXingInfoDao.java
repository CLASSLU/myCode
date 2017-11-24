package kd.idp.index.hisdb.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;

import com.spring.dbservice.DBTemplate;
import com.spring.mapper.HashMapStrRowMapper;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class YunXingInfoDao {
	private  DBTemplate template = DBTemplate.getInstance();
	
	/**
	 * 检修
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,String>> getJxList() throws Exception{
		String sql = "select top 8 票号,主题,计划开工,计划完工 from IDP_DDCZ.ZZ_检修票 order by 填报时间 desc ";
		return template.query(sql, new HashMapStrRowMapper());
	}
	/**
	 * 故障
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,String>> getGzList() throws Exception{
		String sql = "select top 8 所属电网,故障设备名称,故障时间,故障情况 from IDP_DDRZGX.主网故障表 where 所属电网='华中电网' and 故障时间 is not null and   是否共享='是' and (是否删除!='是' or 是否删除 is null) order by 记录时间 desc  ";
		List<Map<String,String>> resultList = template.query(sql, new HashMapStrRowMapper());
		for (Map<String, String> map : resultList) {
			map.put("故障情况", map.get("故障情况").replaceAll(" ", ""));
		}
		return resultList;
	}
	/**
	 * 运行记事
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,String>> getYxjsList() throws Exception{
		String sql = "select top 8 内容,日期,班次 from IDP_DDCZ.ZZ_运行记事 order by 填报时间 desc ";
		return template.query(sql, new HashMapStrRowMapper());
	}
	/**
	 * 火情
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,String>> getHqList() throws Exception{
		String sql = "select top 8 所属线路名称,对设备影响程度,山火地点塔杆号,火险消除时间 from IDP_DDCZ.FZ_火情 order by 填报时间 desc ";
		return template.query(sql, new HashMapStrRowMapper());
	}
	/**
	 * 重载
	 * @return
	 */
	public List<Map<String,String>> getZzList() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = simpleDateFormat.format(new Date(new Date().getTime()-24000*3600));
		String sql = "select a.NAME 断面名称,a.重载次数,a.重载8时间,a.重载9时间 from IDPDWDATANEW.数据中心_断面计算 a, IDPDWIDNEW.数据中心_断面测点表 b "
		+" where a.日期='"+date+"' and a.重载次数>=6 and b.类型='断面' and b.是否启用='是'  and a.ID=b.ID order by 重载次数 desc ";
		List<Map<String,String>> resultList = template.query(sql, new HashMapStrRowMapper());
		for (int i = resultList.size(); i < 8; i++) {
			resultList.add(new HashMap<String, String>());
		}
		return resultList;
	}
	/**
	 * 水情
	 * @return
	 */
	public List<Map<String,String>> getSqList() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = simpleDateFormat.format(new Date(new Date().getTime()-24000*3600));
		String sql = "select top 100 单位, 上游水位,下游水位,入库流量,出库流量,蓄能值 from IDPDWDATANEW.数据中心_水情 where 日期='"+date+"' "
		+" and 单位 in('葛洲坝','三峡','水布垭','隔河岩','三板溪','白市','托口','五强溪') ";
		List<Map<String,String>> resultList = template.query(sql, new HashMapStrRowMapper());
		for (int i = resultList.size(); i < 8; i++) {
			resultList.add(new HashMap<String, String>());
		}
		return resultList;
	}
}
