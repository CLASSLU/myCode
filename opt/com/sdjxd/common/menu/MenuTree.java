package com.sdjxd.common.menu;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.MultiHashMap;

public class MenuTree 
{
	private JsMenuNode rootMenu = null;
	private HashMap<String,JsMenuNode> nodeMap = null;
	private MultiHashMap levelNodeMap = null;
	private int maxLevel = 0;
	public void addNode(JsMenuNode parent,JsMenuNode node)
	{
		if(parent == null)
		{
			if(this.rootMenu == null)
				this.rootMenu = node;
			else
				//登录角色拥有子菜单权限但没有父菜单权限时,不显示子菜单
				return;
		}
		else
		{
			parent.addChild(node);
			node.setParentMenu(parent);
		}
		this.nodeMap.put(node.getMenu().getId(),node);
		this.levelNodeMap.put(node.getMenu().getMenuLevel(),node);
		int l = Integer.parseInt(node.getMenu().getMenuLevel()); 
		this.maxLevel = l>this.maxLevel?l:this.maxLevel;
	}
	public int getMaxLevel()
	{
		return this.maxLevel;
	}
	public void addNode(JsMenuNode node)
	{
		JsMenuNode pNode = this.getNodeById(node.getMenu().getParentId());
		this.addNode(pNode,node);
	}
	public JsMenuNode getNodeById(String menuId)
	{
		return this.nodeMap.get(menuId);
	}
	public List getNodesByLevel(String level)
	{
		return (List)this.levelNodeMap.get(level);
	}
	public MenuTree()
	{
		this.rootMenu = null;
		this.nodeMap = new HashMap<String,JsMenuNode>();
		this.levelNodeMap = new MultiHashMap();
	}
	public MenuTree(JsMenuNode root)
	{
		this.rootMenu = root;
		this.nodeMap = new HashMap<String,JsMenuNode>();
		this.levelNodeMap = new MultiHashMap();
		this.nodeMap.put(this.rootMenu.getMenu().getId(),this.rootMenu);
		this.levelNodeMap.put(root.getMenu().getMenuLevel(),root);
	}
	public JsMenuNode getRootMenu()
	{
		return this.rootMenu;
	}
}
