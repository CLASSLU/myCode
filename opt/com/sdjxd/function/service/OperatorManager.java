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
	 * ����ȱʡ�����������
	 * @return defualtInstance
	 * @throws Exception ����
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
	 * �ж��Ƿ�Ϊ��������
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
	 * ��������
	 * @param op
	 * @return ��ӳɹ�����true,Ϊ�ջ��Ѵ��ڷ���false
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
	 * ���Ҷ�Ӧ�����������(������,���ź��Զ���������Ĳ����ָ���)
	 * @param oper ������ı��ʽ����
	 * @return ��ִ������������null
	 */
	public Operator getOperator(ExpressionElement oper)
	{
		int i = this.operators.indexOf(oper);
		if(i>=0)
			return this.operators.get(i);
		return null;
	}
	/**
	 * �������������,��������������ȼ�(��������)
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
	 * ���ط��û��Զ���������������ַ���(��������,�������Զ�������������ָ���)
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
	 * ����֧�ֵ�������ķ���(����������)
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
	 * �Ƿ��Ƿָ���(�����Զ��������������','�ָ�),�ָ������ȼ����
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
	 * �Ƿ�ֵ�Զ��������
	 * �Զ������������׺���ʽת�沨��ʽʱ,����Ϊ���������
	 * @param el ���������ʽ
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
