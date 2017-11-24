package com.sdjxd.function.model;

import java.util.List;

/**
 * 减法运算符
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
			throw new Exception("错误的参数个数.");
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
			throw new Exception("执行:"+this.getSymbol()+"运算时"+e.getMessage()); 
		}
		return sum;
	}
}
