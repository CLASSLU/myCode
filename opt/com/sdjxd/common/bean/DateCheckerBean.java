package com.sdjxd.common.bean;

/**
 * 用于检查日期是否重复Bean
 * @author Key 2011-11-08
 */
public class DateCheckerBean
{
	private boolean isInteger;	//年度和月度字段在数据库中是否是整型
	private String year;
	private String month;	//可空
	private String tableName;
	private String yearField;
	private String monthField;	//可空
	private String pkField;			//缺省SHEETID
	private String pk;					//记录主键,可空
	private String patternId;
	private String otherField;		//排除该字段值为指定值的记录
	private String otherFieldValue;
	
	/**
	 * 检查该Bean的数据是否齐全
	 * @return 数据正确true,否则false
	 */
	public boolean checkData()
	{
		this.month = this.nvl(this.month);
		
		if(year == null	//年度为空
				||yearField == null	//年度字段为空
				||(monthField == null && month!=null)	//月度不空,但字段为空 
				||(tableName == null && patternId == null)	//表明为空
				||(pk != null && patternId == null)	//主键不为空但字段为空
				)
			return false;
		if(this.otherField != null)
		{
			String[] arr = this.otherField.split(",");
			String[] arr2 = this.otherFieldValue.split(",");
			if(arr.length != arr2.length)
				return false;
		}
		return true;
	}
	/**
	 * 查询sql
	 * @return String sql
	 */
	public String getCheckSql()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT 1 FROM ").append(this.tableName)
		.append(" WHERE ").append(this.yearField).append('=');
		if(this.isInteger)
			sb.append(this.year);
		else
			sb.append('\'').append(this.year).append('\'');
		
		if(this.monthField != null)
		{
			sb.append(" AND ").append(this.monthField);
			if(this.month == null)
				sb.append(" IS NULL");			//此处处理:月报和年共用一个表,年报数据月度字段为空的情形
			else if(this.isInteger)
				sb.append('=').append(this.month);
			else
				sb.append("='").append(this.month).append('\'');
		}
		if(this.pkField != null)
		{
			sb.append(" AND ").append(this.pkField).append(" !='").append(this.pk).append('\'');
		}
		if(this.otherField != null)
		{
			String[] arr = this.otherField.split(",");
			String[] vArr = this.otherFieldValue.split(",");
			for(int i=0;i<arr.length;++i)
			{
				sb.append(" AND ").append(arr[i]).append("='").append(vArr[i]).append('\'');
			}
		}
		return sb.toString();
	}
	public String getYear()
	{
		return this.year;
	}
	public void setYear(Object year)
	{
		this.year = nvl(year);
	}
	public String getMonth()
	{
		return this.month;
	}
	public void setMonth(Object month)
	{
		this.month = nvl(month);
	}
	public String getTableName()
	{
		return tableName;
	}
	public void setTableName(String tableName)
	{
		this.tableName = nvl(tableName);
	}
	public String getYearField()
	{
		return yearField;
	}
	public void setYearField(String yearField)
	{
		this.yearField = nvl(yearField);
	}
	public String getMonthField()
	{
		return monthField;
	}
	public void setMonthField(String monthField)
	{
		this.monthField = nvl(monthField);
	}
	public String getPkField()
	{
		return pkField;
	}
	public void setPkField(String pkField)
	{
		this.pkField = nvl(pkField);
	}
	public String getPk()
	{
		return pk;
	}
	public void setPk(String pk)
	{
		this.pk = nvl(pk);
	}
	
	public boolean isInteger()
	{
		return isInteger;
	}

	public void setInteger(boolean isInteger)
	{
		this.isInteger = isInteger;
	}
	public String getOtherField()
	{
		return otherField;
	}
	public void setOtherField(String otherField)
	{
		this.otherField = nvl(otherField);
	}
	public String getOtherFieldValue()
	{
		return otherFieldValue;
	}
	public void setOtherFieldValue(String otherFieldValue)
	{
		this.otherFieldValue = nvl(otherFieldValue);
	}
	public String getPatternId()
	{
		return patternId;
	}
	public void setPatternId(String patternId)
	{
		this.patternId = nvl(patternId);
	}
	private String nvl(Object v)
	{
		if(v == null)
			return null;
		if(v instanceof String && ((String)v).trim().equals(""))
			return null;
		if(v instanceof String)
			return (String)v;
		return String.valueOf(v);
	}
	
}
