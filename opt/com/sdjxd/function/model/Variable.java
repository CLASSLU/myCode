package com.sdjxd.function.model;

/**
 * ������
 * @author Key
 */
public class Variable
{
	private Argument argument;		//��Ӧ�Ĳ���
	private Object value;					//����ֵ
	/**
	 * ���캯��
	 * @param arg ��Ӧ����,ֵȱʡΪ�մ�
	 */
	public Variable(Argument arg)
	{
		this.argument = arg;
		this.value = "";
	}
	
	/**
	 * ���ӹ��캯��
	 * @param arg ��Ӧ����,�ɿ�
	 * @param v ����ֵ
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
	 * ֧�ֺ��ַ�����ֱ�ӱȽ�
	 * �������Ĳ�����Variable������ֵ��Ϊ��,����ź�ֵ��ƥ���Ϊ��ͬ
	 * ����,������ͬ,����ͬ
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
