package com.sdjxd.common.choices;

import java.util.List;
import com.sdjxd.pms.platform.form.model.CellBean;

public class MultipleChoiceBean
{
	public String cellId;
	public String tagName;
	public int id;
	public int parentId;
	public String cellPrefix;
	public String testTitle;
	public String valueTitle;
	public boolean showValueTitle;
	public boolean isSingleChoice;
	public List<ChoiceItem> choiceItems;
}
