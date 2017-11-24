package com.sdjxd.function.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

import com.sdjxd.function.model.Constant;
import com.sdjxd.function.model.ExpressionElement;
import com.sdjxd.function.model.FunctionTemplate;
import com.sdjxd.function.model.Operator;
import com.sdjxd.function.model.Parenthesis;
import com.sdjxd.function.model.Variable;

/**
 * �沨��ʽ�㷨ʵ���Զ��幫ʽ�Ľ���������(��ʽģ���ʵ��ִ����)
 * @author Key
 * 2011-08-14
 */
public class ExpressionEvaluator 
{
	//�������沨��ʽ
	private List<ExpressionElement> rpn;
	//��ʽģ��
	private FunctionTemplate template = null;
	//֧�ֵ������(����)
	private OperatorManager operatorManager = null;
	//�Ƿ��ѱ���
	private boolean hasCompiled;
	
	/**
	 *�򵥹��캯��
	 */
	public ExpressionEvaluator()
	{
		this.rpn = new ArrayList<ExpressionElement>();
		this.hasCompiled = false;
	}
	/**
	 * ���빫ʽģ��,׼��ִ��
	 * @param funcTmp ����ģ��
	 * @throws Exception
	 */
	public void compile(FunctionTemplate funcTmp) throws Exception
	{
		checkGrammer(funcTmp);
		this.template = funcTmp;
		this.conver2Rpn();
		this.hasCompiled = true;
	}
	/**
	 * �����׺���ʽ(�����������⴦��,eg:fun(a,b),��ʵ��ǰ׺���ʽ)�﷨
	 * @param nifixExp ��׺���ʽ
	 * @return boolean
	 */
	public boolean checkGrammer(FunctionTemplate tmpFunc)
	{
		return true;
	}
	/**
	 * ��׺���ʽת�沨��ʽ(�����������⴦��,eg:fun(a,b),��ʵ��ǰ׺���ʽ)
	 * @return
	 * @throws Exception
	 */
	private void conver2Rpn() throws Exception 
	{
		if (this.template == null)
			throw new Exception("δ����Ĺ�ʽ.");
		//��ʱջ,�洢�����
		Stack<ExpressionElement> stack = new Stack<ExpressionElement>();
		//��׺���ʽ�ָ��
		List<String> infixExp = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(this.template.getInfixExpression(),
				this.operatorManager.getOperatorString()+Operator.ARGUMENTS_DELIMITER, true);
		StringBuffer sb =  new StringBuffer();
		while (st.hasMoreElements()) {
			String t = st.nextToken();
			if(t.length()>1)
			{
				if((t.charAt(0) == '\'' || t.charAt(0) == '"') && (t.charAt(t.length()-1) != '\'' && t.charAt(t.length()-1) != '"'))
					sb.append(t);
				else if((t.charAt(t.length()-1) != '\'' && t.charAt(t.length()-1) != '"')&&(t.charAt(t.length()-1) == '\'' && t.charAt(t.length()-1) == '"'))
				{
					sb.append(t);
					infixExp.add(sb.toString());
					sb.delete(0, sb.length());
				}
				else
					infixExp.add(t);
			}
			else if(t.equals("'") || t.equals("\""))
			{
				sb.append(t);
				if(sb.length()>1)
				{
					infixExp.add(sb.toString());
					sb.delete(0, sb.length());
				}
			}
			else if(sb.length()>0)
				sb.append(t);
			else
				infixExp.add(t);
		}
		
		int position = 0;
		//��׺���ʽ�ָ�ĵ���������
		ExpressionElement elem = null;
		int length = infixExp.size();
		//������е����ŷָ��,eg:"("��")"
		Parenthesis parenthesis = this.operatorManager.getParenthesis();
		while (position<length) 
		{
			elem = new ExpressionElement(infixExp.get(position));
			if (this.operatorManager.isOperator(elem))
			{
				//ȡ���ĵ�ǰ�������������
				if (parenthesis.getLeft().equals(elem)
						|| (!stack.isEmpty() && parenthesis.getRight().equals(stack.peek()))) 
				{
					//����������Ż���ջ����������,������ʱջ
					stack.push(elem);
				} 
				else if (parenthesis.getRight().equals(elem))
				{
					//�����������,��ʱջ��ջ�������沨��ʽ(this.rpn)ֱ��������һ��������
					while(!stack.isEmpty() && !parenthesis.getLeft().equals(stack.peek()))
					{
						ExpressionElement el = stack.pop();
						//��������ڷָ�������Ķ���,�����
						if(!this.operatorManager.isDelimiter(el))
							this.rpn.add(el);
					}
					if(!stack.isEmpty())
					{
						//���Ե�ջ�е�������
						stack.pop();
						//���ջ�����Զ��������(����),��ջ
						if(!stack.isEmpty() && this.operatorManager.isUserDefined(stack.peek()))
							this.rpn.add(stack.pop());
					}
				}
				else
				{
					//����,��ʱջ��ջ,�������沨��ʽֱ��������һ�����ȼ������ڵ�ǰ������������
					while(!stack.isEmpty() && this.operatorManager.getPriority(stack.peek())>=this.operatorManager.getPriority(elem))
					{
						this.rpn.add(stack.pop());
					}
					stack.push(elem);
				}
			} 
			else if(this.operatorManager.isDelimiter(elem))
			{
				//�Ƿָ���,ֱ��ѹջ
				stack.push(elem);
			}
			else
			{
				//���������,ֱ�ӷ����沨��ʽ
				this.rpn.add(elem);
			}
			++position;
		}
		while (!stack.isEmpty()) {
			this.rpn.add(stack.pop());
		}
	}
	/**
	 * �滻�沨��ʽ����,����ִ�н��ֵ
	 * @param variables ��Ҫ�滻�ı���
	 * @return ����ִ�н��
	 * @throws Exception 
	 */
	public Object execute(List<Variable> variables) throws Exception
	{
//		System.out.println("ִ��:"+this.rpn);
		if(!this.hasCompiled)
			throw new Exception("ִ�д���,δ����!");
		//��ʱջ,�洢�沨��ʽ�еĲ�����(�����������)
		Stack<ExpressionElement> tmpStack = new Stack<ExpressionElement>();
		Object result = null;				//�����ִ�н��
		Operator operator = null; 	//���������ʵ��
		
		//ѭ��һ��,ִ��һ�������
		for(int i=0;i<this.rpn.size();++i)
		{
			//��ǰ�ҵ���������Ĳ���ֵ����
			List<Variable> args = new ArrayList<Variable>();
			ExpressionElement el = this.rpn.get(i);
			//���������(����)ȡ��,ѹ����ʱջ,ֱ��������һ�������
			while(i<rpn.size() && !this.operatorManager.isOperator(el))
			{
				tmpStack.push(el);
				el = this.rpn.get(++i);
			}
			if(el != null)
			{
				//�õ����������
				operator = this.operatorManager.getOperator(el);
				//�����,֧�ֵĲ�������
				int argCount = operator.getOperandCount();
				//����ʱջ�а�˳��ȡ���������Ҫ�Ĳ���
				while(!tmpStack.isEmpty() && argCount>0)
				{
					el = tmpStack.pop();
					--argCount;
					if(el.isMetaExpression())
					{
						//��ʱջ�еĲ�������ֵ(��Ա�����������),��ǰ������������Ľ��ֵ
						args.add(0,new Variable(null,el.toString()));
					}
					else
					{
						//��ʱջ�в������Ǳ�������
						int index = variables.indexOf(el);
						if(index<0)
						{
							//�ṩ�Ĳ���ֵ�б���û���ҵ�,ȥ��ʽģ��ĳ�������
							Constant con = this.template.getConstant(el.toString());
							if(con != null)
								args.add(0,new Variable(null,con.getValue()));//�ǳ���,������Ҫ�ı���,�����������������
							else
								throw new Exception("����δ��ֵ:"+el);//����,�׳��쳣
						}	
						else
							args.add(0,variables.get(index));//�Ǳ���,ֱ�Ӵ��ṩ�Ĳ���ֵ�б���ȡ��,�����������������
					}
				}
				//ִ�����������,ִ�н����Ϊ������ѹ����ʱջ
				result = operator.execute(args);
				tmpStack.push(new ExpressionElement(result,true));
			}
		}
//		System.out.println("���:"+result);
		return result;
	}
	public OperatorManager getOperatorManager() {
		return operatorManager;
	}
	public void setOperatorManager(OperatorManager om) {
		this.operatorManager = om;
	}
}
