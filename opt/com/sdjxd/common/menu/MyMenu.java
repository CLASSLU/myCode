package com.sdjxd.common.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.MultiHashMap;

import com.sdjxd.pms.platform.base.BaseObject;
import com.sdjxd.pms.platform.organize.JsMenu;
import com.sdjxd.pms.platform.organize.model.MenuBean;

public class MyMenu extends BaseObject { 
	private static MyMenuDao dao = new MyMenuDao();

	public static JsMenu[] getFirstMenu(String filter) {
		List<?> list = dao.getFirstMenu(filter);
		int len = list.size();
		JsMenu[] jsMenuList = new JsMenu[len];

		for (int i = 0; i < len; ++i) { 
			MenuBean bean = (MenuBean) list.get(i);
			JsMenu menu = new JsMenu();
			menu.setId(bean.getId()); 
			menu.setText(bean.getName());
			menu.setTarget(bean.getOpenTypeId());
			menu.setFunctionId(bean.getFunctionId());
			menu.setLink(bean.getUrl(), bean.getPatternId(), bean.getAppId(),
					bean.getWorkflowId(), bean.getArgument());
			jsMenuList[i] = menu;
		}
		return jsMenuList;
	}
	public static MenuTree getMenuTree(String parentMenuId)
	{
		MultiHashMap  mmap = dao.getChildMenu(parentMenuId);
		Set keys = mmap.keySet();
		int len = keys.size();
		MenuTree menuTree = new MenuTree();
		for (int i=1;i<=len;++i) { 
			List nodeList = (List)mmap.get(i+"");
			for(Object node:nodeList)
			{
				JsMenuNode jsnode = (JsMenuNode)node; 
				menuTree.addNode(jsnode);
			}
		}
		return menuTree;
	}

	public static void main(String[]argc)
	{
		MenuTree menuTree = MyMenu.getMenuTree("0E856A37-4E31-4514-B514-14B0DF2BFE1E");
		//first level menus are function menus
		List<JsMenuNode> functionMenus = menuTree.getRootMenu().getChildMenus();
		for (int i = 1; i <= functionMenus.size() && i<=10; ++i) 
		{
			JsMenuNode funMenu = (JsMenuNode)functionMenus.get(i-1);
			List<JsMenuNode> firstTreeLevelMenus = funMenu.getChildMenus();
			System.out.println("-----------"+funMenu.getMenu().getText()+"---------------");
			//功能菜单下的一级菜单(树的根节点)
			for(int j=1;j<=firstTreeLevelMenus.size() && j<=10;++j)
			{ 
				JsMenuNode child = (JsMenuNode)firstTreeLevelMenus.get(j-1);
				JsMenu childMenu = child.getMenu();
				int childLevel = Integer.parseInt(childMenu.getMenuLevel());
				System.out.println(childLevel+"--"+childMenu.getText());
				//一级菜单下的其他节点
				for(int k=childLevel+1;k<=menuTree.getMaxLevel();++k)
				{
					List<JsMenuNode> levelKChild = new ArrayList<JsMenuNode>();
					child.collectChildren(k,levelKChild);
					if(levelKChild.size()<1)
							break;
					else
					{
						for(Object node:levelKChild)
						{
							JsMenu jm = ((JsMenuNode)node).getMenu();
							System.out.println(jm.getMenuLevel()+"----"+jm.getText());
						}
					}
				}
			}
		}
	}
	public static void printMenu(JsMenuNode menu)
	{
		int l = Integer.parseInt(menu.getMenu().getMenuLevel());
		for(int i=0;i<l;++i)
			System.out.print(" - - ");
		System.out.println(menu.getMenu().getText());
		List<JsMenuNode> children = menu.getChildMenus();
		for(JsMenuNode node:children)
		{
			printMenu(node);
		}
	}
}
