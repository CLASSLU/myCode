package com.sdjxd.common.menu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.RowSet;

import org.apache.commons.collections.MultiHashMap;

import com.sdjxd.pms.platform.data.DbOper;
import com.sdjxd.pms.platform.organize.JsMenu;
import com.sdjxd.pms.platform.organize.model.MenuBean;

public class MyMenuDao {
	private MyMenuSql sqlHelper = new MyMenuSql();

	public List getFirstMenu(String filter) {
		String sql = sqlHelper.getFirstMenu(filter);
		List list = new ArrayList();
		try {
			RowSet rs = DbOper.executeQuery(sql);
			while (rs.next()) {
				MenuBean bean = new MenuBean();
				bean.setId(rs.getString("MENUID"));
				bean.setName(rs.getString("MENUNAME"));
				bean.setParentId(rs.getString("PARENTMENUID"));
				bean.setOpenTypeId(rs.getInt("OPENTYPEID"));
				bean.setMenuLevel(rs.getString("MENULEVEL"));
				bean.setMenuTypeId(rs.getInt("MENUTYPEID"));
				bean.setFillContionType(rs.getInt("FILLCONTIONTYPE"));
				bean.setFillContion(rs.getString("FILLCONTION"));
				bean.setContion(rs.getString("CONTION"));
				bean.setOrdernum(rs.getInt("ORDERNUM"));
				bean.setArgument(rs.getString("ARGUMENT"));

				bean.setFunctionId(rs.getString("FUNCTIONID"));
				bean.setUrl(rs.getString("URL"));
				bean.setAppId(rs.getString("APPID"));
				bean.setPatternId(rs.getString("PATTERNID"));
				bean.setWorkflowId(rs.getString("WORKFLOWID"));

				list.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	public MultiHashMap getChildMenu(String parentMenuId) {
		String sql = sqlHelper.getChildMenu(parentMenuId);
		MultiHashMap map = new MultiHashMap();
		try {
			RowSet rs = DbOper.executeQuery(sql);
			while (rs.next()) {
				MenuBean bean = new MenuBean();
				bean.setId(rs.getString("MENUID"));
				bean.setName(rs.getString("MENUNAME"));
				bean.setParentId(rs.getString("PARENTMENUID"));
				bean.setOpenTypeId(rs.getInt("OPENTYPEID"));
				bean.setMenuLevel(rs.getString("TREE_LEVEL"));
				bean.setMenuTypeId(rs.getInt("MENUTYPEID"));
				bean.setFillContionType(rs.getInt("FILLCONTIONTYPE"));
				bean.setFillContion(rs.getString("FILLCONTION"));
				bean.setContion(rs.getString("CONTION"));
				bean.setOrdernum(rs.getInt("ORDERNUM"));
				bean.setArgument(rs.getString("ARGUMENT"));
				bean.setFunctionId(rs.getString("FUNCTIONID"));
				bean.setUrl(rs.getString("URL"));
				bean.setAppId(rs.getString("APPID"));
				bean.setPatternId(rs.getString("PATTERNID"));
				bean.setWorkflowId(rs.getString("WORKFLOWID"));
				
				JsMenu menu = new JsMenu();
				menu.setMenuLevel(bean.getMenuLevel());
				menu.setId(bean.getId()); 
				menu.setParentId(bean.getParentId());
				menu.setText(bean.getName());
				menu.setTarget(bean.getOpenTypeId());
				menu.setFunctionId(bean.getFunctionId());
				menu.setLink(bean.getUrl(), bean.getPatternId(), bean.getAppId(),
						bean.getWorkflowId(), bean.getArgument());
				JsMenuNode node = new JsMenuNode(menu);
				
				map.put(bean.getMenuLevel(),node);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
}
