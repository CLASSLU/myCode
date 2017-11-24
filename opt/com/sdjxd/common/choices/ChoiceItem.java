package com.sdjxd.common.choices;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.sdjxd.pms.platform.form.model.CellEventBean;
import com.sdjxd.pms.platform.form.service.Action;
import com.sdjxd.pms.platform.form.service.CellEvent;
import com.sdjxd.pms.platform.form.service.FormInstance;
import com.sdjxd.pms.platform.form.service.cell.Cell;

/**
 * 选择题选项
 * @author Key
 */
public class ChoiceItem
{
	//是否单选
	private boolean isSingleChoice;
	//选项内容
	private String title;
	//该项选中的分值
	private String score;
	//动态打分可选分值
	private List<Integer> optionalScores;
	//是否选中
	private boolean isSelected;
	//元件id
	private int id;
	//标签名称
	private String tagName;
	//是否为答案,暂无用
	private boolean isAnswer;
	//是否动态打分
	private boolean isDynamicScore;
	//事件
	private CellEvent[] events;
	
	/**
	 * 构造函数
	 * @param int id 元件ID
	 * @param boolean isSingleChoice 是否单选 
	 * @param String title 选项内容
	 * @param String scores 得分,用户录入的值,格式:
	 * 						1,静态分值,即单个的整数 ;
	 * 						2,动态范围,eg:2-10 则为动态打分,可选取值[2,3,4,5,6,7,8,9,10]
	 */
	public ChoiceItem(int id, boolean isSingleChoice, String title, String scores) {
		super();
		this.events = new CellEvent[Cell.MAXEVENT];
		this.id = id;
		this.tagName = "item";
		this.isSingleChoice = isSingleChoice;
		this.title = title;
		this.optionalScores =  new ArrayList<Integer>();
		initScore(scores);
	}
	/**
	 * 初始化分值
	 * @param String scores
	 * @see ChoiceItem(...)
	 */
	private void initScore(String scores)
	{
		try{
			if(scores.contains("-"))
			{
				this.isDynamicScore = true;
				this.score = "0";
				String[] intStr = scores.split("-");
			
				int low = Integer.valueOf(intStr[0]);
				int up = Integer.valueOf(intStr[1]);
				while(low<=up)
				{
					this.optionalScores.add(low++);
				}
			}
			else
			{
				Integer.valueOf(scores);
				this.score = scores;
				this.isDynamicScore = false;
			}
		}catch(Exception e){
			System.out.println("选项分值 维护错误,采用缺省值 0分! "+e.getMessage());
			this.score = "0";
			this.isDynamicScore = false;
		}
	}
	/**
	 * 返回构造对应JS对象的语句
	 * @param ChoiceItemBean cib 包含选项的一些动态信息 
	 * @return String 
	 */
	public String getScriptObject(ChoiceItemBean cib)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(" new com.sdjxd.common.userDefinedCell.ChoiceItem(");
		sb.append("{id:"+this.id);
		sb.append(",choicesId:"+cib.choicesId);
		sb.append(",multipleChoiceId:"+cib.multipleChoiceId);
		sb.append(",tagId:'"+cib.parentCellPrefix+"_"+this.tagName+this.id);
		//若是静态分数,则score在元件结构初始化时即以赋值(this.score此时已赋值,
		//若是动态打分则必须运行时才能确定其值(此时this.score尚未初始化)
		sb.append("',score:"+(cib.isDynamicScore?cib.score:(Integer.parseInt(this.score)>=0?this.score:"0")));
		sb.append(",isAnswer:"+this.isAnswer);
		sb.append(",isDynamicScore:"+this.isDynamicScore);
		sb.append(",isSelected:"+cib.isSelected+"})");

		return sb.toString();
	}

	/**
	 * 渲染html语句
	 * @param FormInstance form 表单实例
	 * @param ChoiceItemBean cib 包含选项的一些动态信息 
	 * @throws Exception
	 */
	public void render(FormInstance form, ChoiceItemBean cib) throws Exception
	{
		String eventCode = getEventCode();
		String cellId = cib.parentCellPrefix+"_"+this.tagName+this.id;
		PrintWriter html = form.getRenderHtml();
		html.write("<tr height='"+MultipleChoice.ROW_HEIGHT_STD+"'><td width='20'>");
		if(this.isSingleChoice)
		{
			int _score = Integer.parseInt(this.score);
			if(_score>=0)
				html.write("<input type='radio' name='"+cib.parentCellPrefix+"' ");
			else
				html.write("<input type='hidden' readonly='true' name='"+cib.parentCellPrefix+"' ");
		}
		else
		{
			html.write("<input type='checkbox' ");
		}
		html.write(eventCode);
		StringBuffer onchangeEventCode = getOnChangeEventCode();
		onchangeEventCode.insert(onchangeEventCode.indexOf("{")+1,
				"\n\tcom.sdjxd.common.userDefinedCell.Choices.onChanged("
				+cib.choicesId+","+cib.multipleChoiceId+","+this.id
				+",true" //第四个参数指定是check触发的还是下拉框触发的
				+");");
		
		html.write(onchangeEventCode.toString());
		html.write(" id='"+ cellId +"' ");
		if(cib.isSelected)
			html.write("checked=true");
		html.write("/></td><td>"+this.title+"</td><td style='width:50' align='center'>");
		if(!this.isDynamicScore)
		{
			int _score = Integer.parseInt(this.score);
			html.write(_score>=0?this.score:"");
		}
		/**
		 * 动态打分的下拉列表,ID=所属choiceItem的id+"_option"
		 */
		else
		{
			onchangeEventCode = getOnChangeEventCode();
			onchangeEventCode.insert(onchangeEventCode.indexOf("{")+1,
					"\n\tcom.sdjxd.common.userDefinedCell.Choices.onChanged("
					+cib.choicesId+","+cib.multipleChoiceId+","+this.id+",false);");
			html.write("<select "+onchangeEventCode.toString());
			html.write(" id='"+cellId+ "_option'>");
			for(Integer i:this.optionalScores)
			{//动态打分项的得分,在实例初始化前是没有赋值的,故此用cib.score而非this.score
				html.write("<option value='"+i.intValue()+"' ");
				if(i.intValue() == Integer.valueOf(cib.score).intValue())
					html.write("selected='selected'");
				html.write(">"+i.intValue()+"</option>");
			}
			html.write("</select");
		}
		html.write("</td></tr>");
	}
	private String getEventCode()
	{
		StringBuffer sb = new StringBuffer();
		for(CellEvent event:this.events)
		{
			if(event == null || "onchange".equals(event.getEventCode()))
				continue;
			sb.append(event.getEventCode());
			sb.append("='new function(){\n\t");
			for (int i = 0; i < 10; i++)
			{
				Action action = event.getAction(i);
				if (action == null)
					continue;
				sb.append(action.toString()).append(";\n");
			}
			sb.append("}' ");
		}
		return sb.toString();
	}
	/**
	 * onchange事件单独处理(需要加入内部方法)
	 * @return
	 */
	private StringBuffer getOnChangeEventCode()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("onchange");
		sb.append("='new function(){\n\t");
		for(CellEvent event:this.events)
		{
			if(event != null && "onchange".equals(event.getEventCode()))
			{
				for (int i = 0; i < 10; i++)
				{
					Action action = event.getAction(i);
					if (action == null)
						continue;
					sb.append(action.toString()).append(";\n");
				}
				break;
			}
		}
		sb.append("}' ");
		return sb;
	}
	public void setEvents(CellEvent[] events)
	{
		this.events = events;
	}
	public boolean isSingleChoice() {
		return isSingleChoice;
	}

	public void setSingleChoice(boolean isSingleChoice) {
		this.isSingleChoice = isSingleChoice;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public List<Integer> getOptionalScores() {
		return optionalScores;
	}

	public void setOptionalScores(List<Integer> optionalScores) {
		this.optionalScores = optionalScores;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public int getId() {
		return id;
	}
	public boolean isDynamicScore() {
		return isDynamicScore;
	}
}
