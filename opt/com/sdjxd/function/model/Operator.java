package com.sdjxd.function.model;

import java.util.List;

/**
 * 运算符对象的基类,自定义运算符必须派生自该类
 * @author Key
 */
public abstract class Operator implements Comparable<Operator>
{
	public final static int LOWEST_PRIORITY = -1;								//最低优先级值
	public final static int HIGHEST_PRIORITY = Integer.MAX_VALUE;	//最高优先级值
	public final static int MAX_ARG_COUNT = Integer.MAX_VALUE; 	//支持的最大参数个数
	public final static String ARGUMENTS_DELIMITER = ",";				//自定义运算符(函数)的参数分隔符
	private int operandCount;		//参数个数
	private String symbol;				//符号
	private int priority;					//优先级
	
	/**
	 * 是否是自定义运算符,自定义运算符在公式的中缀表达式中是以前缀表达式形式存在的
	 * eg:前缀表达式: fun(a,b) 对应逆波兰式:a,b,fun
	 * 		中缀表达式: a+b 		对应逆波兰式:a,b,+
	 */
	private boolean isUserDefined = false;		
	
	/**
	 * 运算符执行对应的方法
	 */
	public abstract Object execute(List<Variable> variables) throws Exception;
	
	/**
	 * 参数个数
	 * @return int
	 */
	public int getOperandCount()
	{
		return this.operandCount;
	}
	public void setOperandCount(int c)
	{
		this.operandCount = c;
	}
	public void setSymbol(String s)
	{
		this.symbol = s;
	}
	/**
	 * 运算符符号
	 * @return
	 */
	public String getSymbol()
	{
		return this.symbol;
	}
	/**
	 * 运算符优先级
	 * @return
	 */
	public int getPriority()
	{
		return this.priority;
	}
	public void setPriority(int p)
	{
		this.priority = p;
	}
	/**
	 * 可比较,优先级高者大
	 */
	public int compareTo(Operator o) 
	{
		if(o == null)
			return 1;
		else
			return this.priority - o.getPriority();
	}
	
	/**
	 * 支持与字符串的直接比较(通过运算符符号)
	 */
	@Override
	public boolean equals(Object o)
	{
		if(this == o)
			return true;
		else if(this.symbol == null && o==null)
			return true;
		else if(this.symbol==null)
			return false;
		else if(o instanceof Operator)
		{
			return this.symbol.equals(((Operator)o).getSymbol());
		}
		else
			return this.symbol.equals(o.toString());
	}
	@Override
	public String toString()
	{
		return this.symbol;
	}
	
	/**
	 * 是否用户自定义运算符
	 * @return boolean
	 */
	public boolean isUserDefined()
	{
		return this.isUserDefined;
	}
	
	/**
	 * 设置是否用户自定运算符,并返回设置后的值
	 * @param b
	 * @return
	 */
	public boolean isUserDefined(boolean b)
	{
		this.isUserDefined = b;
		return this.isUserDefined;
	}
}
