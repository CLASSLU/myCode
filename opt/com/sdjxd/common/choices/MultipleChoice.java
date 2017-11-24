package com.sdjxd.common.choices;

import java.awt.Rectangle;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import com.sdjxd.pms.platform.form.service.CellEvent;
import com.sdjxd.pms.platform.form.service.Form;
import com.sdjxd.pms.platform.form.service.FormInstance;
import com.sdjxd.pms.platform.form.service.cell.DynamicCell;
/**
 * 选择题类,一个实例对应一道题
 * @author Key
 */
public final class MultipleChoice extends DynamicCell
{
	//题目内容列的列标题
	private String testTitle;
	//得分列的列标题
	private String valueTitle;
	//是否显示得分列
	private boolean showValueTitle;
	//是否单选(或多选)
	private boolean isSingleChoice;
	//该题的选项
	private List<ChoiceItem> choiceItems;
	//父元件id
	private int parentId;
	//事件是否已初始化
	private boolean eventsHaveRendered;
	//行高,列宽 标准值
	public final static int ROW_HEIGHT_STD = 24;
	public final static int COL_WIDTH_STD = 90;
	
	public MultipleChoice(Form pattern) 
	{
		super(pattern);
		this.eventsHaveRendered = false;
	}
	public void setData(MultipleChoiceBean model) throws Exception
	{
		if (model == null)
		{
			return;
		}
		this.parentId = model.parentId;
		this.cellPrefix = model.cellPrefix;
		setCellId(this.cellPrefix + this.id);
	}
	/**
	 * 渲染html构造语句
	 * @param form 表单实例
	 * @param values 全部的选项
	 * @throws Exception
	 */
	public void renderHtml(FormInstance form, HashMap<String,ChoiceItemBean> values) throws Exception
	{
		this.renderEvent(form);
		PrintWriter html = form.getRenderHtml();
		html.write("<table border='1' id='"
				+ this.cellId
				+"' style=\"border-color:#92C1ED;font:'宋体'; font-size:12px;border-collapse:collapse;position:absolute;top:" + this.top + 
				";left:" +   this.left + 
				";width:" +  this.width +
				";height:" + this.height + "\">");
		//试题标题 
		html.write("<tr style='font-weight:bold;background-color:#E1EEEF;font-size:14px;' height=\""+MultipleChoice.ROW_HEIGHT_STD+"\"><td align='center' colspan='2'>"+this.testTitle+"</td>");
		if(this.showValueTitle)
			html.write("<td>"+this.valueTitle+"</td>");
		html.write("</tr>");
	
		//渲染试题选项
		for(ChoiceItem item:choiceItems)
		{
			int itemId = item.getId();
			ChoiceItemBean cib = values.get(this.id+""+itemId);
			if(cib == null)
			{//新增时,没有具体数据,采用缺省值
				cib = new ChoiceItemBean();
				cib.id = item.getId();
				cib.choicesId = this.parentId;
				cib.multipleChoiceId = this.id;
				cib.isSelected = false;
				cib.parentCellPrefix = this.cellId;
				cib.score = item.isDynamicScore()?
						(item.getOptionalScores().get(0)+"")
						:"0";
			}
			item.render(form,cib);
		}
		html = form.getRenderHtml();
		html.write("</table>");
	}
	//未使用
	public void render(FormInstance form) throws Exception
	{
		this.renderHtml(form,null);
		renderScriptObject(form);
	}
	/**
	 * 获得对象的JS对象构造语句,该子元件JS对象的构造托管给父元件对象Choice的实例进行初始化
	 * @param cellIdPrefix 父元件tagId,用于拼接this.tagId
	 * @param values 所有选项
	 * @return
	 */
	public String getScriptObject(String cellIdPrefix,HashMap<String,ChoiceItemBean> values)
	{
		StringBuffer scr = new StringBuffer();
		int _score = 0;
		scr.append(" new com.sdjxd.common.userDefinedCell.MultipleChoice(");
		scr.append("{isSingleChoice:"+this.isSingleChoice+",");
		scr.append("id:"+this.id+",");
		scr.append("tagId:'"+this.cellId+"',");
		//构造选择题的选项的JS对象数组
		scr.append("choiceItems:[");
		StringBuffer sb = new StringBuffer();
		for(ChoiceItem ci:this.choiceItems)
		{
			ChoiceItemBean cib = values.get(this.id+""+ci.getId());
			if(cib == null)
			{//新增时,没有具体数据,采用缺省值
				cib = new ChoiceItemBean();
				cib.id = ci.getId();
				cib.choicesId = this.parentId;
				cib.multipleChoiceId = this.id;
				cib.isSelected = false;
				cib.parentCellPrefix = this.cellId;
				cib.isDynamicScore = ci.isDynamicScore();
				cib.score = ci.isDynamicScore()?
						(ci.getOptionalScores().get(0)+"")
						:"0";
			}
			sb.append(ci.getScriptObject(cib)+",");
			if(cib.isSelected)
			{
				_score  += Integer.valueOf(cib.isDynamicScore?cib.score:ci.getScore());
			}
		}
		if(sb.lastIndexOf(",")>1)
			sb.deleteCharAt(sb.length()-1);
		scr.append(sb.toString());
		//本题的得分,各(选中的)选项累加值,方便汇总
		scr.append("],score:"+_score+"})");
		
		return scr.toString();
	}

	/**
	 * 当作单独元件使用时,调用此方法渲染JS对象
	 */
	@Override
	protected void renderScriptObject(FormInstance form)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("var mc").append(this.id);
		sb.append(" = new com.sdjxd.common.userDefinedCell.MultipleChoice({id:");
		sb.append(this.id);
		sb.append(",needSave:false");
		sb.append(",tagId:");
		sb.append(this.cellId);
		sb.append("});\r\n");
		sb.append("defaultForm.addChild(mc").append(this.id).append(");\r\n");
		
		form.addScript("form_obj_cell_"+this.id, sb.toString());
	}
	protected void renderEvent(FormInstance form)
	{
		if(!this.eventsHaveRendered)
			super.renderEvent(form);
		for(ChoiceItem ci:this.choiceItems)
		{
			ci.setEvents(this.events);
		}
	}
	/**
	 * 提供给父元件的接口
	 * 通过父元件的事件初始化自己的事件
	 * @param events
	 */
	public void setEvents(CellEvent[] events)
	{
		this.events = events;
		this.eventsHaveRendered = true;
	}
	/**
	 * 初始化元件的坐标位置
	 * @param pos Rectangle
	 */
	public void setPosition(Rectangle pos)
	{
		super.setLeft(pos.x);
		super.setTop(pos.y);
		super.setWidth((int)pos.getWidth());
		super.setHeight((int)pos.getHeight());
	}
	public int getRowCount(int w)
	{
		int rowCount = 1;
		try{
			for(ChoiceItem ci:this.choiceItems){
				double stdWidth = w*0.6;
				int wordWidth = ci.getTitle().length()*8;
				rowCount += (int)(wordWidth/(stdWidth==0?1:stdWidth) + (wordWidth%stdWidth==0?0:1));
			}
		}catch(Exception e){
			rowCount = this.choiceItems.size()+1;
		}
		return rowCount;
	}
	public List<ChoiceItem> getChoiceItems() {
		return choiceItems;
	}
	public void setChoiceItems(List<ChoiceItem> choiceItems) {
		this.choiceItems = choiceItems;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getTestTitle() {
		return testTitle;
	}
	public void setTestTitle(String testTitle) {
		this.testTitle = testTitle;
	}
	public String getValueTitle() {
		return valueTitle;
	}
	public void setValueTitle(String valueTitle) {
		this.valueTitle = valueTitle;
	}
	public boolean isShowValueTitle() {
		return showValueTitle;
	}
	public void setShowValueTitle(boolean showValueTitle) {
		this.showValueTitle = showValueTitle;
	}
	public boolean isSingleChoice() {
		return isSingleChoice;
	}
	public void setSingleChoice(boolean isSingleChoice) {
		this.isSingleChoice = isSingleChoice;
	}
}
