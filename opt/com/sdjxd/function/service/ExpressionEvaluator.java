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
 * 逆波兰式算法实现自定义公式的解析及运算(公式模板的实际执行类)
 * @author Key
 * 2011-08-14
 */
public class ExpressionEvaluator 
{
	//编译后的逆波兰式
	private List<ExpressionElement> rpn;
	//公式模板
	private FunctionTemplate template = null;
	//支持的运算符(函数)
	private OperatorManager operatorManager = null;
	//是否已编译
	private boolean hasCompiled;
	
	/**
	 *简单构造函数
	 */
	public ExpressionEvaluator()
	{
		this.rpn = new ArrayList<ExpressionElement>();
		this.hasCompiled = false;
	}
	/**
	 * 编译公式模板,准备执行
	 * @param funcTmp 方法模板
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
	 * 检查中缀表达式(函数部分特殊处理,eg:fun(a,b),其实是前缀表达式)语法
	 * @param nifixExp 中缀表达式
	 * @return boolean
	 */
	public boolean checkGrammer(FunctionTemplate tmpFunc)
	{
		return true;
	}
	/**
	 * 中缀表达式转逆波兰式(函数部分特殊处理,eg:fun(a,b),其实是前缀表达式)
	 * @return
	 * @throws Exception
	 */
	private void conver2Rpn() throws Exception 
	{
		if (this.template == null)
			throw new Exception("未编译的公式.");
		//临时栈,存储运算符
		Stack<ExpressionElement> stack = new Stack<ExpressionElement>();
		//中缀表达式分割后
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
		//中缀表达式分割的单个操作数
		ExpressionElement elem = null;
		int length = infixExp.size();
		//运算符中的括号分割符,eg:"("和")"
		Parenthesis parenthesis = this.operatorManager.getParenthesis();
		while (position<length) 
		{
			elem = new ExpressionElement(infixExp.get(position));
			if (this.operatorManager.isOperator(elem))
			{
				//取出的当前操作数是运算符
				if (parenthesis.getLeft().equals(elem)
						|| (!stack.isEmpty() && parenthesis.getRight().equals(stack.peek()))) 
				{
					//如果是左括号或者栈顶是右括号,放入临时栈
					stack.push(elem);
				} 
				else if (parenthesis.getRight().equals(elem))
				{
					//如果是右括号,临时栈出栈并放入逆波兰式(this.rpn)直到遇到第一个左括号
					while(!stack.isEmpty() && !parenthesis.getLeft().equals(stack.peek()))
					{
						ExpressionElement el = stack.pop();
						//如果是用于分割函数参数的逗号,则忽略
						if(!this.operatorManager.isDelimiter(el))
							this.rpn.add(el);
					}
					if(!stack.isEmpty())
					{
						//忽略掉栈中的左括号
						stack.pop();
						//如果栈顶是自定义运算符(函数),出栈
						if(!stack.isEmpty() && this.operatorManager.isUserDefined(stack.peek()))
							this.rpn.add(stack.pop());
					}
				}
				else
				{
					//否则,临时栈出栈,并放入逆波兰式直到遇到第一个优先级不高于当前运算符的运算符
					while(!stack.isEmpty() && this.operatorManager.getPriority(stack.peek())>=this.operatorManager.getPriority(elem))
					{
						this.rpn.add(stack.pop());
					}
					stack.push(elem);
				}
			} 
			else if(this.operatorManager.isDelimiter(elem))
			{
				//是分隔符,直接压栈
				stack.push(elem);
			}
			else
			{
				//不是运算符,直接放入逆波兰式
				this.rpn.add(elem);
			}
			++position;
		}
		while (!stack.isEmpty()) {
			this.rpn.add(stack.pop());
		}
	}
	/**
	 * 替换逆波兰式参数,返回执行结果值
	 * @param variables 需要替换的变量
	 * @return 返回执行结果
	 * @throws Exception 
	 */
	public Object execute(List<Variable> variables) throws Exception
	{
//		System.out.println("执行:"+this.rpn);
		if(!this.hasCompiled)
			throw new Exception("执行错误,未编译!");
		//临时栈,存储逆波兰式中的操作数(不包括运算符)
		Stack<ExpressionElement> tmpStack = new Stack<ExpressionElement>();
		Object result = null;				//运算符执行结果
		Operator operator = null; 	//运算符对象实例
		
		//循环一次,执行一个运算符
		for(int i=0;i<this.rpn.size();++i)
		{
			//当前找到的运算符的参数值链表
			List<Variable> args = new ArrayList<Variable>();
			ExpressionElement el = this.rpn.get(i);
			//逐个操作数(变量)取出,压入临时栈,直到遇到第一个运算符
			while(i<rpn.size() && !this.operatorManager.isOperator(el))
			{
				tmpStack.push(el);
				el = this.rpn.get(++i);
			}
			if(el != null)
			{
				//得到运算符对象
				operator = this.operatorManager.getOperator(el);
				//运算符,支持的参数个数
				int argCount = operator.getOperandCount();
				//从临时栈中按顺序取出运算符需要的参数
				while(!tmpStack.isEmpty() && argCount>0)
				{
					el = tmpStack.pop();
					--argCount;
					if(el.isMetaExpression())
					{
						//临时栈中的操作数是值(相对变量或常量而言),即前面的运算符运算的结果值
						args.add(0,new Variable(null,el.toString()));
					}
					else
					{
						//临时栈中操作数是变量或常量
						int index = variables.indexOf(el);
						if(index<0)
						{
							//提供的参数值列表里没有找到,去公式模板的常量中找
							Constant con = this.template.getConstant(el.toString());
							if(con != null)
								args.add(0,new Variable(null,con.getValue()));//是常量,构造需要的变量,放入运算符参数链表
							else
								throw new Exception("变量未赋值:"+el);//否则,抛出异常
						}	
						else
							args.add(0,variables.get(index));//是变量,直接从提供的参数值列表中取出,放入运算符参数链表
					}
				}
				//执行运算符运算,执行结果作为操作数压入临时栈
				result = operator.execute(args);
				tmpStack.push(new ExpressionElement(result,true));
			}
		}
//		System.out.println("结果:"+result);
		return result;
	}
	public OperatorManager getOperatorManager() {
		return operatorManager;
	}
	public void setOperatorManager(OperatorManager om) {
		this.operatorManager = om;
	}
}
