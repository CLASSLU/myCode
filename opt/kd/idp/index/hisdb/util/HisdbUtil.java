package kd.idp.index.hisdb.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class HisdbUtil {
	/**
	 * 将Object对象转化为Float
	 * @param source
	 * @return 
	 */
	public static  Float getFloatByObject(Object source){
		if (source==null) {
			return null;
		}
		BigDecimal bigDecimal = new BigDecimal(Float.parseFloat(source.toString()));
		Double value = bigDecimal.setScale(2,RoundingMode.HALF_UP).doubleValue();
		Float resultFloat = Float.parseFloat(value.toString());
		return resultFloat;
	}
	/**
	 * 通过统计周期动态获得列名
	 * @param interval
	 * @return
	 */
	public static List<String> getColumnByTime(int interval){
		List<String> resultList = new ArrayList<String>();
		for(int i=0;i<24;i++){
			for(int j=0;j<60;j+=interval){
				resultList.add("DATA_"+i+"_"+j);
			}
		}
		return resultList;
	}
	
	/**
	 * 通过统计周期动态获得时间周期
	 * @param interval
	 * @return
	 */
	public static List<String> getTimeColumnByTime(int interval){
		List<String> resultList = new ArrayList<String>();
		for(int i=0;i<24;i++){
			for(int j=0;j<60;j+=interval){
				if (j==0) {
					resultList.add(i+"");
				}else{
					resultList.add(i+":"+j);
				}
			}
		}
		return resultList;
	}
	public static void main(String[] args) {
		System.out.println(getFloatByObject("2.1345"));
	}
}
