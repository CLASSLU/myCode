package com.sdjxd.function.model;

import java.util.List;

/**
 * �˷������
 * @author Key
 */
public class Multiply extends Operator {
	
	public Multiply(String symbol, int priority, int argCount, boolean isUserDefined)
	{
		this.setOperandCount(argCount);
		this.setPriority(priority);
		this.setSymbol(symbol);
		this.isUserDefined(isUserDefined);
	}
	public Multiply(String symbol, int priority, int argCount)
	{
		this(symbol,priority,argCount,false);
	}
	
	@Override
	public Object execute(List<Variable> variables) throws Exception
	{
		if(this.getOperandCount() != variables.size())
			throw new Exception("����Ĳ�������.");
		double sum = 1d;
		Variable var = null;
		try{
			for(int i=0;i<variables.size();++i)
			{
				var = variables.get(i);
				double v = Double.parseDouble(var.getValue()+"");
				sum *= v;
			}
		}catch(Exception e){
			throw new Exception("ִ��:"+var.getName()+"����ʱ"+e.getMessage()); 
		}
		return sum;
	}
}
