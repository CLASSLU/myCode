package com.sdjxd.common.choices;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * 汇总栏
 * @author Key
 * |------------------------------
 * |title 	  |..............|总计|
 * |valueTitle| .............|    |
 */
public class Aggregation 
{
	//元件的坐标位置
	private Rectangle rect;
	//行标题
	private String title;
	//值行 行标题
	private String valueTitle;
	//是否可见
	private boolean visible;
	//是否纵向延伸,未用
	private boolean isVertical;
	//是否允许换行,横向延伸时有效,未用
	private boolean allowNewLine;
	//父元件js对象id
	private String cellPrefix;
	//元件js对象id
	private String cellId;
	public static final String tagName = "aggregation"; 
	/**
	 * 构造函数,缺省不可见,横向延伸
	 */
	public Aggregation()
	{
		this.visible = false;
		this.isVertical = false;
		this.allowNewLine = false;
		this.title = "标题";
		this.valueTitle = "值标题";
	}
	/**
	 * 返回构造JS对象的语句字符串
	 * @param model HashMap<Integer,String> 
	 * 				Key:MultipleChoice的id,Value:其分值 
	 * @return String
	 */
	public String getScriptObject(HashMap<Integer,String> model)
	{
		StringBuffer scr = new StringBuffer();
		scr.append("new com.sdjxd.common.userDefinedCell.Aggregation({cellId:'"+this.cellId
				+"',values:[");
		Set<Integer> keys = model.keySet();
		for(Integer t:keys)
		{
			String v = model.get(t);
			scr.append("['");
			scr.append(t);
			scr.append("','");
			scr.append(v);
			scr.append("'],");
		}
		if(scr.toString().endsWith(","))
			scr.deleteCharAt(scr.length()-1);
		scr.append("]})");
		return scr.toString();
	}
	/**
	 * 渲染html语句
	 * @param model 
	 * @see getScriptObject
	 * @return
	 */
	public String renderHtml(HashMap<Integer,String> model)
	{
		StringBuffer tBuffer = new StringBuffer();
		StringBuffer vBuffer = new StringBuffer(); 
		tBuffer.append("<table border='1' style=\"border-color:#92C1ED; font:'宋体'; font-size:14px;border-collapse:collapse;position:absolute;top:" + rect.y + 
				";left:" + rect.x + 
				";width:" + rect.width +
				";height:" + rect.height+"\"><tr style='font-weight:bold' align='center'><td>"+this.title+"</td>");
		vBuffer.append("<tr align='center'><td style='font-weight:bold' align='center'>"+this.valueTitle+"</td>");
		Set<Integer> keys = model.keySet();
		for(Integer t:keys)
		{
			String v = model.get(t);
			if(t.intValue() == -1)
				continue;
			tBuffer.append("<td>"+(char)(t.intValue()+64)+"</td>");
			vBuffer.append("<td><input type='text' readonly style='text-align:center;border:none;background-color:#E4EEEF;width:30px;font-size:14px\' id='"
					+this.cellId+"_v"+t+"' value='"+v+"'/></td>");
		}
		tBuffer.append("<td>总计</td></tr>");
		vBuffer.append("<td><input align='middle' type='text' readonly style='text-align:center;border:none;background-color:#E4EEEF;width:30px;font-size:14px\' id='"+
				this.cellId+"_sum' value='"+(model.get(new Integer(-1)))+"'/></td></tr>");
		tBuffer.append(vBuffer);
		tBuffer.append("</table>");
		return tBuffer.toString();
	}
	public void setCellPrefix(String cellPrefix)
	{
		this.cellPrefix = cellPrefix;
		this.cellId = cellPrefix +"_"+ Aggregation.tagName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getValueTitle() {
		return valueTitle;
	}
	public void setValueTitle(String valueTitle) {
		this.valueTitle = valueTitle;
	}
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public boolean isVertical() {
		return isVertical;
	}
	public void setVertical(boolean isVertical) {
		this.isVertical = isVertical;
	}
	public void setRect(Rectangle rect)
	{
		this.rect = rect;
	}
}
