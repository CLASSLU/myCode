package kd.idp.index.hisdb.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kd.idp.index.hisdb.bean.MeasureBean;
import kd.idp.index.hisdb.dao.HisdbDao;
import kd.idp.index.hisdb.dao.PowerTime;
import kd.idp.index.hisdb.util.HisdbUtil;

public class PowerCurveService {
	
	private  String id = "184HuaZhongFSDLP";
	private  String dataId = "122723133466635093";
	private  String pinlvId = "122723133466615568";
	private  List<MeasureBean> fdMeasureBeanList = new ArrayList<MeasureBean>();
	private  List<MeasureBean> ydMeasureBeanList = new ArrayList<MeasureBean>();
	private  Date jr = new Date();
	private  Date zr = new Date(new Date().getTime()-1000*3600*24);
	private HisdbDao dao = new HisdbDao();
	public PowerCurveService() {
		ydMeasureBeanList.add(new MeasureBean("华中","122723133466635093"));
		ydMeasureBeanList.add(new MeasureBean("湖北","122723133466629140"));
		ydMeasureBeanList.add(new MeasureBean("河南","122723133466629187"));
		ydMeasureBeanList.add(new MeasureBean("湖南","122723133466629279"));
		ydMeasureBeanList.add(new MeasureBean("江西","122723133466629233"));
		
		fdMeasureBeanList.add(new MeasureBean("华中","122723133466635107"));
		fdMeasureBeanList.add(new MeasureBean("湖北","122723133466629099"));
		fdMeasureBeanList.add(new MeasureBean("河南","122723133466629145"));
		fdMeasureBeanList.add(new MeasureBean("湖南","122723133466629237"));
		fdMeasureBeanList.add(new MeasureBean("江西","122723133466629191"));
	}	
	public  Map<String, Object> getPowerCurve(){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> ySeries = new ArrayList<Map<String,Object>>();
			List<MeasureBean> jrfd = dao.getDataListById(jr, fdMeasureBeanList);
			List<MeasureBean> jryd = dao.getDataListById(jr, ydMeasureBeanList);
			List<MeasureBean> zrfd = dao.getDataListById(zr, fdMeasureBeanList);
			List<MeasureBean> zryd = dao.getDataListById(zr, ydMeasureBeanList);
			ySeries.addAll(getMapFromMeasureBean(jrfd,"今日","发电"));
			ySeries.addAll(getMapFromMeasureBean(jryd,"今日","用电"));
			ySeries.addAll(getMapFromMeasureBean(zrfd,"昨日","发电"));
			ySeries.addAll(getMapFromMeasureBean(zryd,"昨日","用电"));
			resultMap.put("xSeries", HisdbUtil.getTimeColumnByTime(1));
			resultMap.put("ySeries", ySeries);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	private  List<Map<String, Object>> getMapFromMeasureBean(List<MeasureBean> measureList,String name,String type){
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for (MeasureBean bean : measureList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", name);
			map.put("type", type);
			map.put("area", bean.getDisplayName());
			map.put("data", bean.getDataList());
			resultList.add(map);
		}
		return resultList;
	}
	public  Map<String, Object> getMapFromFindByID() throws Exception{
	    Map<String, Object> resultMap = new HashMap<String,Object>();
	    String lastDay=PowerTime.getYesterday();
		String lastYear=PowerTime.getQnday();
		List<Map<String,Object>> messageList = dao.findById(id,lastDay,lastYear);
	   for (int i = 0; i < messageList.size(); i++) {
		   Map<String, Object> messageMap = new HashMap<String,Object>();
		   messageMap=messageList.get(i);
		if (messageMap.get("日期").equals(lastDay)){
			resultMap.put("昨日用电",messageMap.get("电量"));
			resultMap.put("昨日最大负荷", messageMap.get("最大电力"));
			resultMap.put("昨日最大负荷发生时间", messageMap.get("实时最大时间"));
		}
		if (messageMap.get("日期").equals(lastYear)){
			resultMap.put("去年同期最大负荷", messageMap.get("最大电力"));
			resultMap.put("去年最大负荷发生时间", messageMap.get("实时最大时间"));
		   }
	  }
		return resultMap;
	}
	/**
	 * 获取当前负荷值与时间的Map
	 * @return
	 */
	public  Map<String, Object> getNowpower(){
		Map<String, Object> powrNeedmap=new HashMap<String, Object>();
		String useTime=PowerTime.getNowtime();
		List<Map<String,Object>> nowPowerList=dao.findByDataId(dataId,useTime);
		List<Map<String,Object>> pinLvList=dao.findByDataId(pinlvId,useTime);
		for (int x = 23; x>=0;x--) {
			boolean flag = false;
			for (int y = 59; y>=0; y--) {
				String timePowerneed="DATA_"+x+"_"+y;	
				if (nowPowerList.size()>0&&nowPowerList.get(0).get(timePowerneed.toString())!=null) {
					//设定跳出当前循环的一个标号
					Object thisPowerneed=nowPowerList.get(0).get(timePowerneed.toString());
	                powrNeedmap.put("当前负荷值",HisdbUtil.getFloatByObject(thisPowerneed).toString());
	        		powrNeedmap.put("负荷时间",(x>0?(""+x):("0"+x))+":"+(y>0?(""+y):("0"+y)));
	                flag = true;break;
				}
			}
			if (flag) {
				break;
			}
		}	
		for (int x = 23; x>=0;x--) {
			boolean flag = false;
			for (int y = 59; y>=0; y--) {
				String timePowerneed="DATA_"+x+"_"+y;	
				if (pinLvList.size()>0&&pinLvList.get(0).get(timePowerneed.toString())!=null) {
					//设定跳出当前循环的一个标号
					Object thisPowerneed=pinLvList.get(0).get(timePowerneed.toString());
	                powrNeedmap.put("当前频率值",HisdbUtil.getFloatByObject(thisPowerneed).toString());
	                powrNeedmap.put("频率时间",(x>0?(""+x):("0"+x))+":"+(y>0?(""+y):("0"+y)));
	                flag = true;break;
				}
			}
			if (flag) {
				break;
			}
		}	
		return powrNeedmap ;
	}
	public static void main(String[] args) {
		try {
			Map<String, Object> map = new PowerCurveService().getNowpower();
			map.get("当前负荷值");
			map.get("x");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println();
	}
}
