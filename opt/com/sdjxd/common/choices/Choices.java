package com.sdjxd.common.choices;

import java.awt.Rectangle;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import com.sdjxd.pms.platform.form.model.CellBean;
import com.sdjxd.pms.platform.form.service.Form;
import com.sdjxd.pms.platform.form.service.FormInstance;
import com.sdjxd.pms.platform.form.service.cell.DynamicCell;
/**
 * 元件参数:String("TableName1,2;类型;分值列的标题;每行显示列数;是否纵向延伸优先")
 * @author Administrator
 *
 */
public class Choices extends DynamicCell{

	//所有的选择题
	private List<MultipleChoice> choiceList;
	//@Aggregation 汇总结果,js对象中存储的multipleChoiceId-multipleChoice.Score 二维数组,是字符串数组
	private Aggregation aggregation;
	//每行显示选择题个数(列/行)
	private int mcCountOfEachRow;
	//纵向延伸优先,还是横向延伸优先
	boolean isVerticalExtend;
	//选择题类型,根据类型确定题目
	private String typeId;
	
	/**
	 * 构造函数
	 * @param pattern 
	 */
	public Choices(Form pattern) 
	{
		super(pattern);
		this.tagName = "div";
		this.mcCountOfEachRow = 2;
		this.isVerticalExtend = false;
		this.choiceList = new ArrayList<MultipleChoice>();
		this.aggregation = new Aggregation();
	}
	/**
	 * 构造绘制元件的html语句
	 */
	@Override
	public void render(FormInstance form) throws Exception
	{
		for(MultipleChoice mc: this.choiceList)
		{
			mc.setEvents(this.events);
		}
		TreeMap<String, String> tree = form.getIndex();
		String value = tree.get(this.valueField);
		HashMap<String,ChoiceItemBean> values = parseValue(value);
		PrintWriter html = form.getRenderHtml();
		html.write("<div id='"+this.cellId+"'>");
		//题目
		for(MultipleChoice mc:choiceList)
		{
			mc.renderHtml(form, values);
		}
		//汇总结果
		if(this.aggregation.isVisible())
		{
			String aggStr = this.aggregation.renderHtml(this.aggregate(values));
			html.write(aggStr);
		}
		html.write("</div>");
		renderScriptObject(form);
	}
	/**
	 * 构造前台用js对象,将元件信息放入平台对象中.
	 */
	protected void renderScriptObject(FormInstance form)
	{
		TreeMap<String, String> tree = form.getIndex();
		String value = tree.get(this.valueField);
		String text = tree.get(this.textField);
		HashMap<String,ChoiceItemBean> values = parseValue(value);
		StringBuffer sb = new StringBuffer();
		//生成创建对象的脚本语句（必须要在平台中指定一个对应的js脚本）
		sb.append("var choices").append(this.id);
		//{}中为参数，在js脚本初始化时可以对这些参数进行引用
		sb.append(" = new com.sdjxd.common.userDefinedCell.Choices({id:");
		sb.append(this.id);
		sb.append(",needSave:false");
		sb.append(",typeId:'");
		sb.append(this.typeId);
		sb.append("',tagId:'");
		sb.append(this.cellId);
		sb.append("',value:'"+(value==null?"":value)+"'");
		sb.append(",text:'"+(text==null?"":text)+"'");
		sb.append(",multipleChoices:[");
		
		StringBuffer buffer = new StringBuffer();
		for(MultipleChoice mc:this.choiceList)
		{
			buffer.append(mc.getScriptObject(this.cellId,values)+",");
		}
		if(buffer.lastIndexOf(",")>1)
			buffer.deleteCharAt(buffer.length()-1);
		sb.append(buffer.toString());
		
		sb.append("],aggregation:");
		sb.append(this.aggregation.getScriptObject(this.aggregate(values)));
		sb.append("});\r\n");
		
		sb.append("defaultForm.addChild(choices").append(this.id).append(");\r\n");
		
		form.addScript("form_obj_cell_"+this.id, sb.toString());
	}
	/**
	 * 初始化元件的基本数据(坐标,id等)
	 */
	@Override
	public void setData(CellBean model) throws Exception
	{
		super.setData(model);
		if (model == null)
		{
			return;
		}
		this.data = model;
		this.cellType = model.cellType;
		this.id = model.cellId;
		this.label = model.cellLabel;
		setCellId(this.cellPrefix + this.id);
		setName(model.cellName);
		
		//动态信息和子元件初始化:结构&数据
		String[] info = this.userDefinfo.split(";");
		try{
			this.mcCountOfEachRow = Integer.valueOf(info[3]);
			this.isVerticalExtend = "true".equals(info[4])?true:false;
		}catch(Exception e){
			System.out.println("选择题元件参数信息不全,采用缺省值!"+e.getMessage());
		}
		this.typeId = info[1];
		this.choiceList = ChoicesDao.getChoiceList(info[0].split(","),info[1],info[2],this.pattern);
		for(MultipleChoice mc: this.choiceList)
		{
			MultipleChoiceBean mcBean = new MultipleChoiceBean();
			mcBean.cellPrefix = this.cellId+"_multipleChoice";
			mcBean.parentId = this.id;
			mc.setData(mcBean);
		}
		initChildrenPosition();
		initAggregation();
	}
	
	/**
	 * 根据元件的位置,大小,初始化每一道题的具体位置和大小
	 */
	private void initChildrenPosition()
	{
		int mcCount = this.choiceList.size();//选择题总数
		int rowCount = mcCount/this.mcCountOfEachRow + 
					(mcCount%this.mcCountOfEachRow == 0 ? 0:1);//行数
		int _left = this.left;
		int _top = this.top;
		int _height = this.height/(rowCount==0?1:rowCount);
		int avgWidth = this.width/this.mcCountOfEachRow;
		
		if(this.isVerticalExtend)
		{//纵向延伸优先
			int rowIndex = 0;//行索引
			int maxTop = _top;
			for(int i=0;i<mcCount;++i)
			{
				MultipleChoice mc = this.choiceList.get(i);
				_height = mc.getRowCount(avgWidth)*MultipleChoice.ROW_HEIGHT_STD;
				mc.setPosition(new Rectangle(_left,_top,avgWidth,_height));
				if(++rowIndex != rowCount)
				{
					_top += _height;
					maxTop = maxTop<_top?_top:maxTop;
				}
				else
				{
					maxTop = maxTop<(_top+_height)?(_top + _height):maxTop;
					_top = this.top;
					_left += avgWidth;
				}
			}
			this.aggregation.setRect(new Rectangle(this.left,maxTop+3,width,50));
		}
		else
		{//横向延伸优先
			int[] tops = {_top,_top,_top,_top,_top,_top,_top,_top,_top,_top};//不同列的top不同
			int i = 0;
			for(int row=0;row<rowCount;++row)
			{
				for(int col=0;col<this.mcCountOfEachRow && i<mcCount;++col,++i)
				{	
					MultipleChoice mc = this.choiceList.get(i);
					_height = mc.getRowCount(avgWidth)*MultipleChoice.ROW_HEIGHT_STD;
					mc.setPosition(new Rectangle(_left,tops[col],avgWidth,_height));
					_left += avgWidth;
					tops[col] += _height;
				}
				_left = this.left;
			}
			int maxTop = tops[0];
			for(int ii=1;ii<tops.length;++ii)
			{
				maxTop = tops[ii]>maxTop?tops[ii]:maxTop;
			}
			this.aggregation.setRect(new Rectangle(this.left,maxTop+3,width,50));
		}
	}
	private void initAggregation()
	{
		if(this.data.assitInfo != null)
		{
			String[] info = this.data.assitInfo.split(",");
			try{
				this.aggregation.setCellPrefix(this.cellId);
				this.aggregation.setVisible("true".equals(info[0])?true:false);
				this.aggregation.setTitle(info[1]);
				this.aggregation.setValueTitle(info[2]);
			}catch(Exception e)
			{
				System.out.println("选择题元件汇总信息参数不全:"+e.getMessage());
			}
		}
	}
	/**
	 * 根据元件结构score和实例进行汇总计算
	 * @param values
	 * @return
	 */
	private HashMap<Integer,String> aggregate(HashMap<String,ChoiceItemBean> values)
	{
		HashMap<Integer,String> aggResult = new HashMap<Integer,String>();
		int sum = 0;
		for(MultipleChoice mc:this.choiceList)
		{
			int mcScore = 0;
			for(ChoiceItem ci:mc.getChoiceItems())
			{
				ChoiceItemBean cib = values.get(""+mc.getId()+ci.getId());
				if(cib!=null && cib.isSelected)
				{
					mcScore += Integer.valueOf(cib.isDynamicScore?cib.score:ci.getScore());
				}
			}
			aggResult.put(mc.getId(), mcScore+"");
			sum += mcScore;
		}
		aggResult.put(-1, sum+"");
		return aggResult;
	}
	/**
	 * "1,1,true;1,2,true;2,1,true;2,2,false"
	 * @param form
	 * @return <题ID+项ID,isSelected>
	 */
	public HashMap<String,ChoiceItemBean> parseValue(String value)
	{
		HashMap<String,ChoiceItemBean> map = new HashMap<String, ChoiceItemBean>();
		try
		{
			String[] mcItem = value.split(";");
			for(String s: mcItem)
			{
				String[] arr = s.split(",");
				ChoiceItemBean cib = new ChoiceItemBean();
				boolean isSelected = "true".equals(arr[2])?true:false;
				cib.id = Integer.valueOf(arr[1]);
				cib.choicesId = this.id;
				cib.multipleChoiceId = Integer.valueOf(arr[0]);
				cib.isSelected = isSelected;
				cib.parentCellPrefix = this.cellId+"_multipleChoice"+cib.multipleChoiceId;

				if(arr.length >3)
				{
					cib.score = arr[3];
					cib.isDynamicScore = true;
				}
				else
					cib.isDynamicScore = false;
				
				map.put(arr[0]+arr[1], cib);
			}
		}
		catch(Exception e)
		{
			StringBuffer error = new StringBuffer();
			error.append("数据");
			error.append(value==null?"为空:":"已损坏:");
			error.append(e.getMessage());
			System.out.println(error.toString()); 
		}
		return map;
	}
	public List<MultipleChoice> getChoiceList() {
		return choiceList;
	}

	public void setChoiceList(List<MultipleChoice> choiceList) {
		this.choiceList = choiceList;
	}
}
