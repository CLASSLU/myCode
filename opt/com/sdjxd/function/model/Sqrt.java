package com.sdjxd.function.model;

import java.util.List;
import java.lang.Math;

/**
 * �󿪷�,�Զ��������
 * @author Key
 */
public class Sqrt extends Operator {

	public Sqrt(String symbol, int priority)
	{
		this.setOperandCount(1);
		this.setPriority(priority);
		this.setSymbol(symbol);
		this.isUserDefined(true);
	}
	
	@Override
	public Object execute(List<Variable> variables) throws Exception {
		if(this.getOperandCount() != variables.size())
			throw new Exception("����Ĳ�������.");
		double sum = 0d;
		Variable var = null;
		try{
			var = variables.get(0);
			double v = Double.parseDouble(var.getValue()+"");
			sum = Math.sqrt(v);
		}catch(NumberFormatException e){
			throw new NumberFormatException("ִ��:"+var.getName()+"����ʱ"+e.getMessage()); 
		}catch(NullPointerException e){
			throw new NullPointerException("ִ��:"+var.getName()+"����ʱ"+e.getMessage()); 
		}
		return sum;
	}
	@Override
	public boolean equals(Object o)
	{
		if(this.getSymbol() == null && o==null)
			return true;
		else if(this.getSymbol()==null)
			return false;
		else if(o instanceof Sqrt)
		{
			return this.getSymbol().equals(((Sqrt)o).getSymbol());
		}
		else if(o instanceof String)
			return this.getSymbol().toUpperCase().equals(((String) o).toUpperCase());
		else
			return false;
	}
}
