package kd.idp.index.hisdb.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
public class PowerTime {
	public static String getYesterday(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		Date lastDayDate = calendar.getTime();
		SimpleDateFormat  sdf=new SimpleDateFormat("yyyy-MM-dd");
		String lastDay=sdf.format(lastDayDate);
		return lastDay;
	}
	public static String getQnday(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		Date lastYearDate = calendar.getTime();
		SimpleDateFormat  sdf=new SimpleDateFormat("yyyy-MM-dd");
		String lastYear=sdf.format(lastYearDate);
		return lastYear;
	}
	//获得当前yyyyMMdd格式的时间
	public static String getNowtime(){
		Calendar calendar=Calendar.getInstance();
		Date useTimedata = calendar.getTime();
		String useTime=new SimpleDateFormat("yyyyMMdd").format(useTimedata);
		return useTime;
	}
	public static void main(String[] args)  {
		System.out.println(PowerTime.getYesterday());
		System.out.println(PowerTime.getQnday());
		System.out.println(PowerTime.getNowtime());
	}
}
