package com.sdjxd.function.model;

import java.util.List;

/**
 * ���������
 * @author Key
 */
public class Minus extends Operator
{
	public Minus(String symbol, int priority, int argCount, boolean isUserDefined)
	{
		this.setOperandCount(argCount);
		this.setPriority(priority);
		this.setSymbol(symbol);
		this.isUserDefined(isUserDefined);
	}
	public Minus(String symbol, int priority, int argCount)
	{
		this(symbol,priority,argCount,false);
	}
	@Override
	public Object execute(List<Variable> variables) throws Exception {
		if(this.getOperandCount() != variables.size())
			throw new Exception("����Ĳ�������.");
		double sum = 0d;
		Variable var = null;
		try{
			var = variables.get(0);
			sum = Double.parseDouble(var.getValue()+"");
			for(int i=1;i<variables.size();++i)
			{
				var = variables.get(i);
				double v = Double.parseDouble(var.getValue()+"");
				sum -= v;
			}
		}catch(Exception e){
			throw new Exception("ִ��:"+this.getSymbol()+"����ʱ"+e.getMessage()); 
		}
		return sum;
	}
}
