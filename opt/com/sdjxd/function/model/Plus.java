package com.sdjxd.function.model;

import java.util.List;

/**
 * 加法运算符
 * @author Key
 */
public class Plus extends Operator {
	public Plus(String symbol, int priority, int argCount, boolean isUserDefined)
	{
		this.setOperandCount(argCount);
		this.setPriority(priority);
		this.setSymbol(symbol);
		this.isUserDefined(isUserDefined);
	}
	/**
	 * @param symbol 符号
	 * @param priority 优先级
	 * @param argCount 参数个数
	 */
	public Plus(String symbol, int priority, int argCount)
	{
		this(symbol,priority,argCount,false);
	}
	@Override
	public Object execute(List<Variable> variables) throws Exception {
		if(this.getOperandCount() != variables.size())
			throw new Exception("错误的参数个数.");
		double sum = 0d;
		for(Variable var:variables)
		{
			try{
				double v = Double.parseDouble(var.getValue()+"");
				sum += v;
			}catch(Exception e){
				throw new Exception("执行:"+var.getName()+"运算时"+e.getMessage()); 
			}
		}
		return sum;
	}

}
