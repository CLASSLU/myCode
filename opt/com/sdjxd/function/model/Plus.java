package com.sdjxd.function.model;

import java.util.List;

/**
 * �ӷ������
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
	 * @param symbol ����
	 * @param priority ���ȼ�
	 * @param argCount ��������
	 */
	public Plus(String symbol, int priority, int argCount)
	{
		this(symbol,priority,argCount,false);
	}
	@Override
	public Object execute(List<Variable> variables) throws Exception {
		if(this.getOperandCount() != variables.size())
			throw new Exception("����Ĳ�������.");
		double sum = 0d;
		for(Variable var:variables)
		{
			try{
				double v = Double.parseDouble(var.getValue()+"");
				sum += v;
			}catch(Exception e){
				throw new Exception("ִ��:"+var.getName()+"����ʱ"+e.getMessage()); 
			}
		}
		return sum;
	}

}
