package com.sdjxd.function.model;

/**
 * 字符串表达式分割后的单个子表达式(操作数),Variable类的简单形式,
 * 只在转换逆波兰式时和执行参数变量值替换时使用
 * 实际运算时,均使用Variable
 * @author Key
 *
 */
public class ExpressionElement
{
	//如果是变量(isMetaExpression为false时),则该对象的值为变量的参数符号,否则,是常量值
	private Object element;
	//是否是元表达式,即数字,常量字符串等等
	private boolean isMetaExpression;
	
	/**
	 * 简单构造函数,只能用于构造变量
	 * @param element 变量名称或常量值
	 */
	public ExpressionElement(Object element)
	{
		this.element = element;
		this.isMetaExpression = this.isMetaExpression(element);
		if(this.isMetaExpression && this.element != null)
		{
			 char ch = this.element.toString().charAt(0);
			 if(ch == '\'' || ch == '"')
			 {
				 if(this.element.toString().length()>2)
					 this.element = this.element.toString().substring(1,this.element.toString().length()-1);
				 else
					 this.element = "";
			 }
		}
	}
	/**
	 * 可以指定是否是变量或元表达式的构造函数
	 * @param element 变量符号(常量值)
	 * @param isMeta 是否是元表达式
	 */
	public ExpressionElement(Object element, boolean isMeta)
	{
		this.element = element;
		this.isMetaExpression = isMeta == true?true:false;
	}
	@Override
	public String toString()
	{
		return this.element.toString();
	}
	
	/**
	 * 支持和字符串,及Variable对象,Constant对象的直接比较
	 */
	@Override
	public boolean equals(Object o)
	{
		if(this == o)
			return true;
		else if(this.element == null && o==null)
			return true;
		else if(this.element==null)
			return false;
		else
			return this.element.equals(o.toString());
	}
	/**
	 * 是否是元表达式
	 * @return boolean
	 */
	public boolean isMetaExpression()
	{
		return this.isMetaExpression;
	}
	private boolean isMetaExpression(Object exp)
	{
		if(exp == null)
			return true;
		String s = exp.toString();
		try{
			Double.parseDouble(s);
			return true;
		}catch(NumberFormatException e){
			char sch = s.charAt(0);
			char ech = s.charAt(s.length()-1);
			if((sch == '"' && ech == '"')||(sch=='\'' && ech=='\''))
				return true;
			else
				return false;
		}
	}
}
