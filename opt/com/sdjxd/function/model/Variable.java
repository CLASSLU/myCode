package com.sdjxd.function.model;

/**
 * 变量类
 * @author Key
 */
public class Variable
{
	private Argument argument;		//对应的参数
	private Object value;					//参数值
	/**
	 * 构造函数
	 * @param arg 对应参数,值缺省为空串
	 */
	public Variable(Argument arg)
	{
		this.argument = arg;
		this.value = "";
	}
	
	/**
	 * 复杂构造函数
	 * @param arg 对应参数,可空
	 * @param v 参数值
	 */
	public Variable(Argument arg, Object v)
	{
		this.value = v;
		this.argument = arg;
		if(this.argument == null)
		{
			String s = v ==null?null:v.toString();
			this.argument = new Argument(s,s);
		}
	}
	public void setValue(Object v)
	{
		this.value = v;
	}
	public Object getValue()
	{
		return this.value;
	}
	public String getSymbol()
	{
		return this.argument.getSymbol();
	}
	public String getName()
	{
		return this.argument.getName();
	}
	public String getId()
	{
		return this.argument.getId();
	}
	/**
	 * 支持和字符串的直接比较
	 * 如果传入的参数是Variable对象且值不为空,则符号和值都匹配才为相同
	 * 否则,符号相同,即相同
	 */
	@Override
	public boolean equals(Object o)
	{
		if(super.equals(o))
		{
			if(o != null && o instanceof Variable)
			{
				Variable var = (Variable)o;
				if(this.value == null && var.getValue() == null)
					return true;
				else if(this.value == null)
					return false;
				else
					return this.value.equals(var.getValue());
			}
			else
				return true;
		}
		else
			return false;
	}
	@Override
	public String toString()
	{
		return this.argument.toString();
	}
}
