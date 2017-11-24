package com.sdjxd.function.service;

import java.util.ArrayList;
import java.util.List;

import com.sdjxd.function.model.ExpressionElement;
import com.sdjxd.function.model.Multiply;
import com.sdjxd.function.model.Parenthesis;
import com.sdjxd.function.model.Plus;
import com.sdjxd.function.model.Divide;
import com.sdjxd.function.model.Minus;
import com.sdjxd.function.model.Operator;
import com.sdjxd.function.model.PlusEx;
import com.sdjxd.function.model.Sqrt;

public class OperatorManager
{
	private static OperatorManager defaultInstance = null;
	private List<com.sdjxd.function.model.Operator> operators;
	private com.sdjxd.function.model.Parenthesis parenthesis;
	public OperatorManager()
	{
		this.operators = new ArrayList<Operator>();
	}
	/**
	 * 返回缺省的运算符管理
	 * @return defualtInstance
	 * @throws Exception 忽略
	 */
	public static OperatorManager getDefaultInstance() throws Exception
	{
		if(defaultInstance == null)
		{	
			defaultInstance = new OperatorManager();
			Operator plus = new PlusEx("+", 1,2);
			Operator minus = new Minus("-", 1,2);
			Operator multiply = new Multiply("*", 2,2);
			Operator divide = new Divide("/", 2,2);
			Operator sqrt = new Sqrt("sqrt",3);
			Parenthesis parenthesis = new Parenthesis("()",0);
			defaultInstance.addOperator(plus);
			defaultInstance.addOperator(minus);
			defaultInstance.addOperator(multiply);
			defaultInstance.addOperator(divide);
			defaultInstance.addOperator(sqrt);
			defaultInstance.setParenthesis(parenthesis);
		}
		return defaultInstance;
	}
	/**
	 * 判断是否为操作符号
	 * @param oper
	 * @return
	 */
	public boolean isOperator(ExpressionElement elem) 
	{
		if(elem.isMetaExpression())
			return false;
		if(this.operators.contains(elem))
			return true;
		else if(this.parenthesis.equals(elem.toString()))
			return true;
		else
			return false;
	}
	public boolean isOperator(String oper) 
	{
		return this.isOperator(new ExpressionElement(oper));
	}
	/**
	 * 添加运算符
	 * @param op
	 * @return 添加成功返回true,为空或已存在返回false
	 */
	public boolean addOperator(Operator op)
	{
		if(this.operators.contains(op) || op == null)
			return false;
		this.operators.add(op);
		return true;
	}
	public List<Operator> getOperators()
	{
		return this.operators;
	}
	/**
	 * 查找对应的运算符对象(不包括,括号和自定义运算符的参数分隔符)
	 * @param oper 运算符的表达式对象
	 * @return 可执行运算符对象或null
	 */
	public Operator getOperator(ExpressionElement oper)
	{
		int i = this.operators.indexOf(oper);
		if(i>=0)
			return this.operators.get(i);
		return null;
	}
	/**
	 * 根据运算符符号,返回运算符的优先级(包括括号)
	 * @param operator 
	 * @return
	 */
	public int getPriority(String operator) {
		for(Operator oper:this.operators)
		{
			if(oper.equals(operator))
				return oper.getPriority();
		}
		if(this.parenthesis.equals(operator))
			return this.parenthesis.getPriority();
		return Operator.LOWEST_PRIORITY;
	}
	public int getPriority(ExpressionElement el) {
		return this.getPriority(el.toString());
	}
	/**
	 * 返回非用户自定义运算符的连接字符串(包括括号,不包括自定义运算符参数分隔符)
	 * @return
	 */
	public String getOperatorString()
	{
		StringBuffer sb = new StringBuffer();
		for(Operator op:this.operators)
		{
			if(!op.isUserDefined())
				sb.append(op);
		}
		sb.append(this.parenthesis);
		return sb.toString();
	}
	/**
	 * 反回支持的运算符的符号(不包括括号)
	 * @return List
	 */
	public List<String> getOperatorSymbols()
	{
		List<String> symbols = new ArrayList<String>();
		for(Operator op:this.operators)
		{
			symbols.add(op.getSymbol());
		}
		return symbols;
	}
	/**
	 * 是否是分隔符(比如自定义运算符参数用','分割),分隔符优先级最高
	 * @param str
	 * @return
	 */
	public boolean isDelimiter(ExpressionElement el)
	{
		if(el.isMetaExpression())
			return false;
		else
			return Operator.ARGUMENTS_DELIMITER.equals(el.toString());
	}
	public void setParenthesis(Parenthesis pt)
	{
		this.parenthesis = pt;
	}
	public Parenthesis getParenthesis()
	{
		return this.parenthesis;
	}
	
	/**
	 * 是否值自定义运算符
	 * 自定义运算符在中缀表达式转逆波兰式时,不作为运算符处理
	 * @param el 操作数表达式
	 * @return
	 */
	public boolean isUserDefined(ExpressionElement el)
	{
		if(el.isMetaExpression())
			return false;
		for(Operator op:this.operators)
		{
			if(op.equals(el.toString()))
				return op.isUserDefined();
		}
		return false;
	}
}
