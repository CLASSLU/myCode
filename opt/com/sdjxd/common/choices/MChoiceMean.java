package com.sdjxd.common.choices;

import com.sdjxd.pms.platform.table.model.FieldMeanBean;
import com.sdjxd.pms.platform.table.service.mean.FieldMean;

/**
 * FieldMean of MultipleChoice
 * @author Key
 *
 */
public class MChoiceMean extends FieldMean
{
	@Override
	public String getText(String arg0) 
	{
		return calculateScore(super.getText(arg0));
	}
	private String calculateScore(String text) {
		return null;
	}
	@Override
	public void init(FieldMeanBean arg0) 
	{
		super.init(arg0);
	}
}
