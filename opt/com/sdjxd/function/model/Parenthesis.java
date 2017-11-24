package com.sdjxd.function.model;

import java.util.List;

/**
 * 自定义运算符参数开始和结束的区分符号,括号
 * @author Key
 */
public class Parenthesis extends Operator{
	//开始符号(左括号)
	private ExpressionElement left;
	//结束符号(右括号)
	private ExpressionElement right;
	//参数分隔符
	private ExpressionElement delimiter;
	
	public Parenthesis(String symbol, int priority) throws Exception
	{
		this(symbol,priority,null);
	}
	/**
	 * @param symbol 符号,开始和结束,必须对称
	 * @param priority 优先级
	 * @param delimiter 参数分割符
	 * @throws Exception 符号不对称,抛出异常
	 */
	public Parenthesis(String symbol, int priority, String delimiter) throws Exception
	{
		if(symbol == null || symbol.length()<=1 || symbol.length()%2 != 0)
			throw new Exception("左右括号不匹配:"+symbol);
		this.setOperandCount(Operator.MAX_ARG_COUNT);
		this.setPriority(priority);
		this.setSymbol(symbol);
		this.isUserDefined(false);
		this.left = new ExpressionElement(symbol.substring(0,symbol.length()/2));
		this.right = new ExpressionElement(symbol.substring(symbol.length()/2));
		this.delimiter = delimiter==null?new ExpressionElement(","):new ExpressionElement(delimiter);
	}
	
	/**
	 * 运算符的执行方法
	 * @param 变量值链表
	 * @return 返回开始符号+变量的字符串形式连接串+结束符号 连接的的字符串
	 */
	@Override
	public Object execute(List<Variable> variables) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(this.left);
		int len = variables.size(); 
		if(len >=1)
		{
			Variable var = null;
			for(int i=0;i<len-1;++i)
			{
				var = variables.get(i);
				sb.append(var.getValue()).append(this.delimiter);
			}
			sb.append(variables.get(len-1).getValue());
		}
		sb.append(this.right);
		return sb.toString();
	}
	
	/**
	 * 支持和字符串的比较,开始符号和结束符号中只要有一个和参数相同,及返回true
	 */
	@Override
	public boolean equals(Object o)
	{
		if(this.getSymbol() == null && o==null)
			return true;
		else if(this.getSymbol()==null)
			return false;
		else if(o instanceof Parenthesis)
		{
			return this.getSymbol().equals(((Parenthesis)o).getSymbol());
		}
		else if(o instanceof String)
			return(this.left.equals(o)||this.right.equals(o));
		else
			return false;
	}
	
	/**
	 * 开始符号
	 * @return
	 */
	public ExpressionElement getLeft()
	{
		return this.left;
	}
	
	/**
	 * 结束符号
	 * @return
	 */
	public ExpressionElement getRight()
	{
		return this.right;
	}
}
