package com.sdjxd.function.model;

import java.util.List;

/**
 * ���������Ļ���,�Զ�����������������Ը���
 * @author Key
 */
public abstract class Operator implements Comparable<Operator>
{
	public final static int LOWEST_PRIORITY = -1;								//������ȼ�ֵ
	public final static int HIGHEST_PRIORITY = Integer.MAX_VALUE;	//������ȼ�ֵ
	public final static int MAX_ARG_COUNT = Integer.MAX_VALUE; 	//֧�ֵ�����������
	public final static String ARGUMENTS_DELIMITER = ",";				//�Զ��������(����)�Ĳ����ָ���
	private int operandCount;		//��������
	private String symbol;				//����
	private int priority;					//���ȼ�
	
	/**
	 * �Ƿ����Զ��������,�Զ���������ڹ�ʽ����׺���ʽ������ǰ׺���ʽ��ʽ���ڵ�
	 * eg:ǰ׺���ʽ: fun(a,b) ��Ӧ�沨��ʽ:a,b,fun
	 * 		��׺���ʽ: a+b 		��Ӧ�沨��ʽ:a,b,+
	 */
	private boolean isUserDefined = false;		
	
	/**
	 * �����ִ�ж�Ӧ�ķ���
	 */
	public abstract Object execute(List<Variable> variables) throws Exception;
	
	/**
	 * ��������
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
	 * ���������
	 * @return
	 */
	public String getSymbol()
	{
		return this.symbol;
	}
	/**
	 * ��������ȼ�
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
	 * �ɱȽ�,���ȼ����ߴ�
	 */
	public int compareTo(Operator o) 
	{
		if(o == null)
			return 1;
		else
			return this.priority - o.getPriority();
	}
	
	/**
	 * ֧�����ַ�����ֱ�ӱȽ�(ͨ�����������)
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
	 * �Ƿ��û��Զ��������
	 * @return boolean
	 */
	public boolean isUserDefined()
	{
		return this.isUserDefined;
	}
	
	/**
	 * �����Ƿ��û��Զ������,���������ú��ֵ
	 * @param b
	 * @return
	 */
	public boolean isUserDefined(boolean b)
	{
		this.isUserDefined = b;
		return this.isUserDefined;
	}
}
