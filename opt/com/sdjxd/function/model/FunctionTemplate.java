package com.sdjxd.function.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 公式模板,规定公式的字符串形式的中缀表达式以及公式需要的参数,支持的常量
 * @author Key
 */
public class FunctionTemplate {
	private String id;									//id
	private String name;								//公式名	
	private List<Argument> arguments;		//需要的参数
	private List<Constant> constants;		//支持的常量
	String infixExpression;							//公式字符串形式的中缀表达式
	
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
	 * 添加变量参数
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
	 * 添加常量参数
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
	 * 根据符号名称返回常量对象
	 * @param constName 常量符号
	 * @return 找到返回符号对应的Constant对象,否则返回null
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
	 * 设置支持的常量
	 * @param constants
	 */
	public void setConstants(List<Constant> constants) {
		this.constants = constants;
	}
	
	/**
	 * 返回公式的字符串形式的中缀表达式
	 * @return String
	 */
	public String getInfixExpression() {
		return infixExpression;
	}
	/**
	 * 设置公式的中缀表示
	 * @param infixExpression 中缀表达式字符串
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
