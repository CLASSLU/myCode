package com.sdjxd.function.model;

/**
 * �ַ������ʽ�ָ��ĵ����ӱ��ʽ(������),Variable��ļ���ʽ,
 * ֻ��ת���沨��ʽʱ��ִ�в�������ֵ�滻ʱʹ��
 * ʵ������ʱ,��ʹ��Variable
 * @author Key
 *
 */
public class ExpressionElement
{
	//����Ǳ���(isMetaExpressionΪfalseʱ),��ö����ֵΪ�����Ĳ�������,����,�ǳ���ֵ
	private Object element;
	//�Ƿ���Ԫ���ʽ,������,�����ַ����ȵ�
	private boolean isMetaExpression;
	
	/**
	 * �򵥹��캯��,ֻ�����ڹ������
	 * @param element �������ƻ���ֵ
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
	 * ����ָ���Ƿ��Ǳ�����Ԫ���ʽ�Ĺ��캯��
	 * @param element ��������(����ֵ)
	 * @param isMeta �Ƿ���Ԫ���ʽ
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
	 * ֧�ֺ��ַ���,��Variable����,Constant�����ֱ�ӱȽ�
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
	 * �Ƿ���Ԫ���ʽ
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
