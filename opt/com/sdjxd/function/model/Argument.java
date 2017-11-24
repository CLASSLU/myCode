package com.sdjxd.function.model;
/**
 * 广义参数,Constant和Variable类实际上的基类
 * @author Key
 */
public class Argument 
{
	public final static int CONST_VARIABLE = 1;	//常量参数标志
	public final static int VARIABLE = 0;				//变量参数标志
	private String id;			//id
	private String name;		//参数含义
	private String symbol; //参数符号
	
	public Argument()
	{
		this.id = null;
		this.name = "undefined";
		this.symbol = "undefined";
	}
	public Argument(String name, String symbol)
	{
		this.name = name;
		this.symbol = symbol;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public String getId()
	{
		return this.id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	@Override
	public String toString()
	{
		return this.symbol;
	}
	/**
	 * 支持Argument对象和字符串的直接比较(比较参数的符号)
	 */
	@Override
	public boolean equals(Object o)
	{
		if(this == o)
			return true;
		else if(o instanceof Argument)
		{
			if(this.symbol ==null && ((Argument)o).getSymbol() == null)
				return true;
			else if(this.symbol != null)
				return this.symbol.equals(((Argument)o).getSymbol());
		}
		else if(o instanceof String)
		{
			boolean f = false;
			if(this.symbol != null)
				f = f || this.symbol.equals(o);
			if(this.id != null)
				f = f ||this.id.equals(o);
			return f;
		}
		return false;
	}
}
