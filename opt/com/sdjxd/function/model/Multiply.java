package com.sdjxd.function.model;

import java.util.List;

/**
 * 乘法运算符
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
			throw new Exception("错误的参数个数.");
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
			throw new Exception("执行:"+var.getName()+"运算时"+e.getMessage()); 
		}
		return sum;
	}
}
