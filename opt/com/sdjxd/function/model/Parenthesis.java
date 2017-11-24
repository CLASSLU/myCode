package com.sdjxd.function.model;

import java.util.List;

/**
 * �Զ��������������ʼ�ͽ��������ַ���,����
 * @author Key
 */
public class Parenthesis extends Operator{
	//��ʼ����(������)
	private ExpressionElement left;
	//��������(������)
	private ExpressionElement right;
	//�����ָ���
	private ExpressionElement delimiter;
	
	public Parenthesis(String symbol, int priority) throws Exception
	{
		this(symbol,priority,null);
	}
	/**
	 * @param symbol ����,��ʼ�ͽ���,����Գ�
	 * @param priority ���ȼ�
	 * @param delimiter �����ָ��
	 * @throws Exception ���Ų��Գ�,�׳��쳣
	 */
	public Parenthesis(String symbol, int priority, String delimiter) throws Exception
	{
		if(symbol == null || symbol.length()<=1 || symbol.length()%2 != 0)
			throw new Exception("�������Ų�ƥ��:"+symbol);
		this.setOperandCount(Operator.MAX_ARG_COUNT);
		this.setPriority(priority);
		this.setSymbol(symbol);
		this.isUserDefined(false);
		this.left = new ExpressionElement(symbol.substring(0,symbol.length()/2));
		this.right = new ExpressionElement(symbol.substring(symbol.length()/2));
		this.delimiter = delimiter==null?new ExpressionElement(","):new ExpressionElement(delimiter);
	}
	
	/**
	 * �������ִ�з���
	 * @param ����ֵ����
	 * @return ���ؿ�ʼ����+�������ַ�����ʽ���Ӵ�+�������� ���ӵĵ��ַ���
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
	 * ֧�ֺ��ַ����ıȽ�,��ʼ���źͽ���������ֻҪ��һ���Ͳ�����ͬ,������true
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
	 * ��ʼ����
	 * @return
	 */
	public ExpressionElement getLeft()
	{
		return this.left;
	}
	
	/**
	 * ��������
	 * @return
	 */
	public ExpressionElement getRight()
	{
		return this.right;
	}
}
