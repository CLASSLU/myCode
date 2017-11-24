package com.sdjxd.function.model;

import java.util.List;
import java.lang.Math;

/**
 * 求开方,自定义运算符
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
			throw new Exception("错误的参数个数.");
		double sum = 0d;
		Variable var = null;
		try{
			var = variables.get(0);
			double v = Double.parseDouble(var.getValue()+"");
			sum = Math.sqrt(v);
		}catch(NumberFormatException e){
			throw new NumberFormatException("执行:"+var.getName()+"运算时"+e.getMessage()); 
		}catch(NullPointerException e){
			throw new NullPointerException("执行:"+var.getName()+"运算时"+e.getMessage()); 
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
