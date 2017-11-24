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
 * ѡ����ѡ��
 * @author Key
 */
public class ChoiceItem
{
	//�Ƿ�ѡ
	private boolean isSingleChoice;
	//ѡ������
	private String title;
	//����ѡ�еķ�ֵ
	private String score;
	//��̬��ֿ�ѡ��ֵ
	private List<Integer> optionalScores;
	//�Ƿ�ѡ��
	private boolean isSelected;
	//Ԫ��id
	private int id;
	//��ǩ����
	private String tagName;
	//�Ƿ�Ϊ��,������
	private boolean isAnswer;
	//�Ƿ�̬���
	private boolean isDynamicScore;
	//�¼�
	private CellEvent[] events;
	
	/**
	 * ���캯��
	 * @param int id Ԫ��ID
	 * @param boolean isSingleChoice �Ƿ�ѡ 
	 * @param String title ѡ������
	 * @param String scores �÷�,�û�¼���ֵ,��ʽ:
	 * 						1,��̬��ֵ,������������ ;
	 * 						2,��̬��Χ,eg:2-10 ��Ϊ��̬���,��ѡȡֵ[2,3,4,5,6,7,8,9,10]
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
	 * ��ʼ����ֵ
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
			System.out.println("ѡ���ֵ ά������,����ȱʡֵ 0��! "+e.getMessage());
			this.score = "0";
			this.isDynamicScore = false;
		}
	}
	/**
	 * ���ع����ӦJS��������
	 * @param ChoiceItemBean cib ����ѡ���һЩ��̬��Ϣ 
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
		//���Ǿ�̬����,��score��Ԫ���ṹ��ʼ��ʱ���Ը�ֵ(this.score��ʱ�Ѹ�ֵ,
		//���Ƕ�̬������������ʱ����ȷ����ֵ(��ʱthis.score��δ��ʼ��)
		sb.append("',score:"+(cib.isDynamicScore?cib.score:(Integer.parseInt(this.score)>=0?this.score:"0")));
		sb.append(",isAnswer:"+this.isAnswer);
		sb.append(",isDynamicScore:"+this.isDynamicScore);
		sb.append(",isSelected:"+cib.isSelected+"})");

		return sb.toString();
	}

	/**
	 * ��Ⱦhtml���
	 * @param FormInstance form ��ʵ��
	 * @param ChoiceItemBean cib ����ѡ���һЩ��̬��Ϣ 
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
				+",true" //���ĸ�����ָ����check�����Ļ��������򴥷���
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
		 * ��̬��ֵ������б�,ID=����choiceItem��id+"_option"
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
			{//��̬�����ĵ÷�,��ʵ����ʼ��ǰ��û�и�ֵ��,�ʴ���cib.score����this.score
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
	 * onchange�¼���������(��Ҫ�����ڲ�����)
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
