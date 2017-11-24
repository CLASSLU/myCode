package com.sdjxd.common;

import java.util.Date;
import java.util.HashMap;

public class FieldMeanReverseFactory 
{
	//��Чʱ��һ��Сʱ
	public static long LIFE_CYCLE = 3600000;
	//�ֶκ������ʵ��
	private static HashMap<String,FieldMeanReverse> instances = new HashMap<String,FieldMeanReverse>();
	//����ʵ���Ĵ���ʱ��
	private static HashMap<String,Long> birthTime = new HashMap<String,Long>();
	
	public static FieldMeanReverse getFieldMeanInstance(String meanId)
	{
		FieldMeanReverse instance = instances.get(meanId);
		long now = new Date().getTime();
		if(instance != null)
		{
			Long birthT = birthTime.get(meanId);
			if((now-birthT)<LIFE_CYCLE)
				instance = null;
		}
		if(instance == null)
		{
			instance = new FieldMeanReverse(meanId); 
			instances.put(meanId, instance);
			birthTime.put(meanId, now);
		}
		return instance;
	}
	public static void releaseMemory(String meanId)
	{
		if(meanId != null)
		{
			instances.remove(meanId);
			birthTime.remove(meanId);
		}
		else
		{
			instances.clear();
			birthTime.clear();
		}
		
	}
}
