package com.sdjxd.common.menu;

import java.util.ArrayList;
import java.util.List;

import com.sdjxd.pms.platform.organize.JsMenu;

public class JsMenuNode
{
	private JsMenu menu;
	private JsMenuNode parentMenu;
	private List<JsMenuNode> childMenus;
	
	public JsMenuNode(JsMenu menu)
	{
		this.menu = menu;
		this.childMenus = new ArrayList<JsMenuNode>(); 
		this.parentMenu = null;
	}
	public void collectChildren(int level, List<JsMenuNode> allChild)
	{
		int mLevel = Integer.parseInt(this.menu.getMenuLevel());
		if(level==mLevel+1)
			allChild.addAll(this.childMenus);
		else if(level>mLevel+1)
		{
			for(JsMenuNode child:this.childMenus)
			{
				child.collectChildren(level, allChild);
			}
		}
	}
	public void addChild(JsMenuNode child)
	{
		if(child != null)
			this.childMenus.add(child);
	}
	public JsMenu getMenu() {
		return menu;
	}
	public void setMenu(JsMenu menu) {
		this.menu = menu;
	}
	public JsMenuNode getParentMenu() {
		return parentMenu;
	}
	public void setParentMenu(JsMenuNode parentMenu) {
		this.parentMenu = parentMenu;
	}
	public List<JsMenuNode> getChildMenus() {
		return childMenus;
	}
	public void setChildMenus(List<JsMenuNode> childMenus) {
		this.childMenus = childMenus;
	}
}
