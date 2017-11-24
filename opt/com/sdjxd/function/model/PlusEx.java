package com.sdjxd.function.model;

import java.util.List;

/**
 * +号,支持连接字符串
 * @author Key
 */
public class PlusEx extends Operator {
	public PlusEx(String symbol, int priority, int argCount, boolean isUserDefined)
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
	public PlusEx(String symbol, int priority, int argCount)
	{
		this(symbol,priority,argCount,false);
	}
	@Override
	public Object execute(List<Variable> variables) throws Exception {
		if(this.getOperandCount() != variables.size())
			throw new Exception("错误的参数个数.");
		Double sum = Double.NaN;
		String sumS = "";
		for(Variable var:variables)
		{
			Double v;
			try{
				if(sumS.length()>0)
					sumS += var.getValue().toString();
				else
				{
					v = Double.parseDouble(var.getValue().toString());
					sum = (sum.isNaN()?0d:sum) + v;
				}
			}catch(NumberFormatException e){
				sumS =((sum.isNaN() || !sumS.equals(""))?sumS:sum.toString())+var.getValue().toString();
			}catch(NullPointerException e){
				throw new NullPointerException("执行:"+var !=null?var.getName():"+"+"运算时"+e.getMessage()); 
			}
		}
		return sumS.length()<1?sum:sumS;
	}

}
