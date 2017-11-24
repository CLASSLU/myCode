package com.sdjxd.function.model;

import java.util.List;

/**
 * +��,֧�������ַ���
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
	 * @param symbol ����
	 * @param priority ���ȼ�
	 * @param argCount ��������
	 */
	public PlusEx(String symbol, int priority, int argCount)
	{
		this(symbol,priority,argCount,false);
	}
	@Override
	public Object execute(List<Variable> variables) throws Exception {
		if(this.getOperandCount() != variables.size())
			throw new Exception("����Ĳ�������.");
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
				throw new NullPointerException("ִ��:"+var !=null?var.getName():"+"+"����ʱ"+e.getMessage()); 
			}
		}
		return sumS.length()<1?sum:sumS;
	}

}
