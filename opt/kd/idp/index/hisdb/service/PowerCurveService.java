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
		ydMeasureBeanList.add(new MeasureBean("����","122723133466635093"));
		ydMeasureBeanList.add(new MeasureBean("����","122723133466629140"));
		ydMeasureBeanList.add(new MeasureBean("����","122723133466629187"));
		ydMeasureBeanList.add(new MeasureBean("����","122723133466629279"));
		ydMeasureBeanList.add(new MeasureBean("����","122723133466629233"));
		
		fdMeasureBeanList.add(new MeasureBean("����","122723133466635107"));
		fdMeasureBeanList.add(new MeasureBean("����","122723133466629099"));
		fdMeasureBeanList.add(new MeasureBean("����","122723133466629145"));
		fdMeasureBeanList.add(new MeasureBean("����","122723133466629237"));
		fdMeasureBeanList.add(new MeasureBean("����","122723133466629191"));
	}	
	public  Map<String, Object> getPowerCurve(){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> ySeries = new ArrayList<Map<String,Object>>();
			List<MeasureBean> jrfd = dao.getDataListById(jr, fdMeasureBeanList);
			List<MeasureBean> jryd = dao.getDataListById(jr, ydMeasureBeanList);
			List<MeasureBean> zrfd = dao.getDataListById(zr, fdMeasureBeanList);
			List<MeasureBean> zryd = dao.getDataListById(zr, ydMeasureBeanList);
			ySeries.addAll(getMapFromMeasureBean(jrfd,"����","����"));
			ySeries.addAll(getMapFromMeasureBean(jryd,"����","�õ�"));
			ySeries.addAll(getMapFromMeasureBean(zrfd,"����","����"));
			ySeries.addAll(getMapFromMeasureBean(zryd,"����","�õ�"));
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
		if (messageMap.get("����").equals(lastDay)){
			resultMap.put("�����õ�",messageMap.get("����"));
			resultMap.put("������󸺺�", messageMap.get("������"));
			resultMap.put("������󸺺ɷ���ʱ��", messageMap.get("ʵʱ���ʱ��"));
		}
		if (messageMap.get("����").equals(lastYear)){
			resultMap.put("ȥ��ͬ����󸺺�", messageMap.get("������"));
			resultMap.put("ȥ����󸺺ɷ���ʱ��", messageMap.get("ʵʱ���ʱ��"));
		   }
	  }
		return resultMap;
	}
	/**
	 * ��ȡ��ǰ����ֵ��ʱ���Map
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
					//�趨������ǰѭ����һ�����
					Object thisPowerneed=nowPowerList.get(0).get(timePowerneed.toString());
	                powrNeedmap.put("��ǰ����ֵ",HisdbUtil.getFloatByObject(thisPowerneed).toString());
	        		powrNeedmap.put("����ʱ��",(x>0?(""+x):("0"+x))+":"+(y>0?(""+y):("0"+y)));
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
					//�趨������ǰѭ����һ�����
					Object thisPowerneed=pinLvList.get(0).get(timePowerneed.toString());
	                powrNeedmap.put("��ǰƵ��ֵ",HisdbUtil.getFloatByObject(thisPowerneed).toString());
	                powrNeedmap.put("Ƶ��ʱ��",(x>0?(""+x):("0"+x))+":"+(y>0?(""+y):("0"+y)));
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
			map.get("��ǰ����ֵ");
			map.get("x");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println();
	}
}
