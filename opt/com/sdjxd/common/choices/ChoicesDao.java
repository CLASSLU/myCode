package com.sdjxd.common.choices;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.sdjxd.pms.platform.data.DbOper;
import com.sdjxd.pms.platform.form.service.Form;

public class ChoicesDao 
{
	private static String tableName;

	/**
	 * @param tables [0]=题目,[1]=选项
	 * @param typeId 类型ID
	 * @param scoreTitle 分值列的标题
	 * @param pattern
	 */
	public static List<MultipleChoice> getChoiceList(String[] tables, String typeId, String scoreTitle, Form pattern) 
	{
		List<MultipleChoice> list = new ArrayList<MultipleChoice>();
		try
		{
			String sql1 = new String("select SHEETID,ISSINGLECHOICE,SHEETNAME,SHOWORDER FROM "+tables[0]+" where PARENTID='"+typeId+"' order by showorder");
			ResultSet rs = DbOper.executeQuery(sql1);
			while(rs.next())
			{
				String mcSheetId = rs.getString(1);
				boolean isSingleChoice = rs.getInt(2)==0?false:true;
				String testTitle = rs.getString(3);
				int id = rs.getInt(4);
				//题目 初始化
				MultipleChoice mc = new MultipleChoice(pattern);
				mc.setId(id);
				mc.setTestTitle(testTitle);
				mc.setSingleChoice(isSingleChoice);
				mc.setValueTitle(scoreTitle);
				mc.setShowValueTitle(true);
				
				String sql2 = "select SHOWORDER,SHEETNAME,SCORE from "+tables[1]+" where PARENTID='"+mcSheetId+"' order by showorder";
				ResultSet rs2 = DbOper.executeQuery(sql2);
				List<ChoiceItem> choiceItems = new ArrayList<ChoiceItem>();
				while(rs2.next())
				{//选项初始化
					int ciId = rs2.getInt(1);
					String ciName = rs2.getString(2);
					String score = rs2.getString(3);
					ChoiceItem ci = new ChoiceItem(ciId, isSingleChoice, ciName,score);
					choiceItems.add(ci);
				}
				mc.setChoiceItems(choiceItems);
				list.add(mc);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}
	
}
