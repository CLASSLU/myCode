package kd.idp.psidpapp.opt.sys.menu.dao;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.spring.dbservice.DBTemplate;

import kd.idp.psidpapp.opt.sys.constant.DataSourceName;
import kd.idp.psidpapp.opt.sys.constant.TableName;
import kd.idp.psidpapp.opt.sys.dao.BaseDao;

public class MenuDao extends BaseDao{
	
	/**
	 * 获取对应父节点下菜单节点树
	 * @param parentId		菜单ID
	 * @return				获取对应父节点下菜单节点树
	 * @throws Exception
	 */
	public List<Map<String, Object>> getMenuTreeNode(String parentId)throws Exception{
		StringBuilder sb = new StringBuilder("");
		List<Map<String, Object>> rootNoteMapList = new ArrayList<Map<String, Object>>();
		try{
			sb.setLength(0);
			sb.append("SELECT * FROM ").append(TableName.TABLE_SYS_MENU).append(" WHERE DATASTATUSID='1' AND PARENTID='")
			  .append(parentId).append("'  ORDER BY SORT ASC");
			List<Map<String, Object>> rootMapList = DBTemplate.getInstance(DataSourceName.DATASOURCE_DM).getResultMapList(sb.toString());
			for (int k=0; k<rootMapList.size(); k++){
				Map<String, Object> rootNoteMap = new HashMap<String, Object>();
				parentId = String.valueOf(rootMapList.get(k).get("ID"));
				sb.setLength(0);
				sb.append("SELECT * FROM(SELECT * FROM ").append(TableName.TABLE_SYS_MENU).append(" WHERE DATASTATUSID='1' ORDER BY SORT ASC) ")
				.append("START WITH ID='").append(parentId).append("' CONNECT BY PRIOR ID=PARENTID");
				List<Map<String, Object>> dataMapList = DBTemplate.getInstance(DataSourceName.DATASOURCE_DM).getResultMapList(sb.toString());
				for (int i=0; i<dataMapList.size(); i++){
					Map<String, Object> nodeMap = dataMapList.get(i);
					List<Map<String, Object>> childMapList = new ArrayList<Map<String, Object>>();
					for (int j=0; j<dataMapList.size(); j++){
						if (String.valueOf(nodeMap.get("ID")).equals(String.valueOf(dataMapList.get(j).get("PARENTID")))){
							childMapList.add(dataMapList.get(j));
						}
					}
					if (0 != childMapList.size()){
						nodeMap.put("children", childMapList);
						nodeMap.put("ISLEAF", false);
					}else{
						nodeMap.put("ISLEAF", true);
					}
				}
				if (null != dataMapList && 0 < dataMapList.size()){
					rootNoteMap = dataMapList.get(0);
					if (null != rootNoteMap.get("children")){
						rootNoteMap.put("ISLEAF", false);
					}else{
						rootNoteMap.put("ISLEAF", true);
					}
				}else{
					rootNoteMap = rootMapList.get(k);
					rootNoteMap.put("ISLEAF", true);
				}
				rootNoteMapList.add(rootNoteMap);
			}
			return rootNoteMapList;
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * @param roleId		角色ID
	 * @return				返回对应角色菜单信息集
	 * @throws Exception
	 */
	public List<Map<String, Object>> findMenuListIdByRoleId(String roleId)throws Exception{
		List<Map<String, Object>> menuMapList = null;
		StringBuilder sb = new StringBuilder("");
		try {
			sb.setLength(0);
			sb.append("SELECT * FROM ").append(TableName.TABLE_SYS_MENU).append(" WHERE ID IN(").append("SELECT MENUID FROM ").append(TableName.TABLE_SYS_ROLE_MENU)
			  .append(" WHERE ROLEID='").append(roleId).append("')");
			menuMapList = DBTemplate.getInstance(DataSourceName.DATASOURCE_DM).getResultMapList(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return menuMapList;
	}
	
	/**
	 * 保存角色菜单关系信息
	 * @param roleId		角色ID
	 * @param menuIdArray		关联菜单ID集
	 * @return				返回操作结果真假
	 * @throws Exception
	 */
	public Boolean saveRoleMenuInfo(String roleId, Object[] menuIdArray)throws Exception{
		StringBuilder sb = new StringBuilder("");
		try {
			List<String> sqlList = new ArrayList<String>();
			for (int i=0; i<menuIdArray.length; i++){
				String menuId = (String)menuIdArray[i];
				sb.setLength(0);
				sb.append("INSERT INTO ").append(TableName.TABLE_SYS_ROLE_MENU).append("(ROLEID,MENUID) VALUES('")
				  .append(roleId).append("','")
				  .append(menuId).append("')");
				sqlList.add(sb.toString());
			}
			if (0 != sqlList.size()){
				DBTemplate.getInstance(DataSourceName.DATASOURCE_DM).batchExecuteWithTransaction(sqlList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return true;
	}
	
}
