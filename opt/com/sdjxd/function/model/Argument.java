package com.sdjxd.function.model;
/**
 * �������,Constant��Variable��ʵ���ϵĻ���
 * @author Key
 */
public class Argument 
{
	public final static int CONST_VARIABLE = 1;	//����������־
	public final static int VARIABLE = 0;				//����������־
	private String id;			//id
	private String name;		//��������
	private String symbol; //��������
	
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
	 * ֧��Argument������ַ�����ֱ�ӱȽ�(�Ƚϲ����ķ���)
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
