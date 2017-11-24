package com.sdjxd.function.model;

import java.util.ArrayList;
import java.util.List;

/**
 * ��ʽģ��,�涨��ʽ���ַ�����ʽ����׺���ʽ�Լ���ʽ��Ҫ�Ĳ���,֧�ֵĳ���
 * @author Key
 */
public class FunctionTemplate {
	private String id;									//id
	private String name;								//��ʽ��	
	private List<Argument> arguments;		//��Ҫ�Ĳ���
	private List<Constant> constants;		//֧�ֵĳ���
	String infixExpression;							//��ʽ�ַ�����ʽ����׺���ʽ
	
	public FunctionTemplate()
	{
		this.arguments = new ArrayList<Argument>() ;
		this.constants = new ArrayList<Constant>();;
		this.infixExpression = "";
	}
	public List<Argument> getArguments() {
		return arguments;
	}
	public void setArguments(List<Argument> arguments) {
		this.arguments = arguments;
	}
	public List<Constant> getConstants() {
		return constants;
	}
	/**
	 * ��ӱ�������
	 * @param arg
	 * @return
	 */
	public boolean addArgument(Argument arg)
	{
		if(this.arguments.contains(arg))
			return false;
		this.arguments.add(arg);
		return true;
	}
	/**
	 * ��ӳ�������
	 * @param arg
	 * @return
	 */
	public boolean addConstant(Constant con)
	{
		if(this.constants.contains(con))
			return false;
		this.constants.add(con);
		return true;
	}
	/**
	 * ���ݷ������Ʒ��س�������
	 * @param constName ��������
	 * @return �ҵ����ط��Ŷ�Ӧ��Constant����,���򷵻�null
	 */
	public Constant getConstant(String constName)
	{
		for(Constant con:this.constants)
		{
			if(con.equals(constName))
				return con;
		}
		return null;
	}
	/**
	 * ����֧�ֵĳ���
	 * @param constants
	 */
	public void setConstants(List<Constant> constants) {
		this.constants = constants;
	}
	
	/**
	 * ���ع�ʽ���ַ�����ʽ����׺���ʽ
	 * @return String
	 */
	public String getInfixExpression() {
		return infixExpression;
	}
	/**
	 * ���ù�ʽ����׺��ʾ
	 * @param infixExpression ��׺���ʽ�ַ���
	 */
	public void setInfixExpression(String infixExpression) {
		this.infixExpression = infixExpression;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
