package com.sdjxd.function.model;
/**
 * 常量,只提供get方法
 * @author Key
 */
public class Constant {
	private Argument argument;
	private double value;	//值
	private int precision;	//精度
	
	public Constant(Argument arg, double value)
	{
		this.argument = arg;
		this.value = value;
	}
	public void setPrecision(int pre)
	{
		this.precision = pre;
	}
	public int getPrecision()
	{
		return this.precision;
	}
	public double getValue()
	{
		return this.value;
	}
	public String getName()
	{
		return this.argument.getName();
	}
	public String getSymbol()
	{
		return this.argument.getSymbol();
	}
	@Override 
	public boolean equals(Object o)
	{
		if(this == o)
			return true;
		else if(o == null)
			return false;
		else if(this.argument != null)
			return this.argument.equals(o);
		else if(o instanceof Constant)
		{
			if(((Constant)o).argument == null)
				return true;
		}
		return false;
	}
}
