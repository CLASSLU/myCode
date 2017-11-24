package com.sdjxd.common;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.sdjxd.pms.platform.data.DbOper;
import com.sdjxd.pms.platform.data.ListMap;
import com.sdjxd.pms.platform.table.dao.TableDao;
import com.sdjxd.pms.platform.table.model.FieldMeanBean;
import com.sdjxd.pms.platform.table.service.mean.FieldMean;
import com.sdjxd.pms.platform.table.service.mean.IFieldMean;
import com.sdjxd.pms.platform.webapp.BeanFactory;

public class FieldMeanReverse 
{
	private String meanId;
	private FieldMeanBean meanBean;
	public static String COMBOBOX = "IDStringPicker";
	public static int FROM_TABLE = 2;										//从数据库读取
	public static int COMMON_SOURCE = 0;								//常数
	public static String USER_DEFINE = "UserDef";						//用户自定义
	public static String OBJ_SELECT = "ID_Selector";						//对象选择
	private static TableDao dao = (TableDao) BeanFactory.getSingleInstance("TableDao");;
	private HashMap<Object,Object> meanData;
	private FieldMeanReverse()
	{
		this.meanId = null;
	}
	public FieldMeanReverse(String meanId)
	{
		this.meanId = meanId;
		this.meanData = new HashMap<Object, Object>();
		try{
			FieldMeanBean fmb = dao.getFieldMean(meanId);
			this.meanBean = fmb;
			Object[] assistInfo = fmb.getAssitinfo();
			String type = fmb.getType();
			//下拉框选择
			if(FieldMeanReverse.COMBOBOX.equals(type))
			{
				Object[] tableInfo = (Object[])assistInfo[1];
				int t = Integer.parseInt((String)assistInfo[0]);
				if(t == FieldMeanReverse.FROM_TABLE)
				{
					//从数据库读取
					Object tn = tableInfo[0];
					Object idn = tableInfo[2];
					Object ndn = tableInfo[1];
					Object f = tableInfo[3];
					ArrayList list = executeQuery(tn,idn,ndn,f);
					for(Object obj:list)
					{
						ListMap map = (ListMap)obj;
						this.meanData.put(map.get(ndn), map.get(idn));
					}
				}
				else if(t == FieldMeanReverse.COMMON_SOURCE)
				{
					//常数
					for(Object obj:tableInfo)
					{
						String[] s = ((String)obj).split(",");
						this.meanData.put(s[1], s[0]);
					}
				}
				else
				{
					//其他
				}
			}
			//对象选择
			else if (FieldMeanReverse.OBJ_SELECT.equals(type))
			{
				Object tn = assistInfo[3];
				Object idn = assistInfo[1];
				Object ndn = assistInfo[2];
				ArrayList list = executeQuery(tn,idn,ndn,"");
				for(Object obj:list)
				{
					ListMap map = (ListMap)obj;
					this.meanData.put(map.get(ndn), map.get(idn));
				}
			}
			//用户自定义
			else if(type == FieldMeanReverse.USER_DEFINE)
			{
			}
			//其他
			else
			{
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 根据显示字段获取ID
	 * @param name
	 * @return
	 */
	public Object getIdByName(Object name)
	{
		return this.meanData.get(name);
	}
	public String getNameById(Object value)
	{
		if(value == null)
			return "";
		IFieldMean ifm = FieldMean.getInstance(this.meanBean);
		return ifm.getText(value.toString());
	}
	private ArrayList executeQuery(Object tableName,Object vField,Object tField,Object filter)
	{
		ArrayList list = new ArrayList();
		try{
			list = DbOper.executeList("select "+vField+","+tField+" from "+tableName + " " + filter);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
}
