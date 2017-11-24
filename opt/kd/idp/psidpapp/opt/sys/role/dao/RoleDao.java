package kd.idp.psidpapp.opt.sys.role.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.spring.dbservice.DBTemplate;

import kd.idp.psidpapp.opt.sys.constant.DataSourceName;
import kd.idp.psidpapp.opt.sys.constant.TableName;
import kd.idp.psidpapp.opt.sys.dao.BaseDao;

public class RoleDao extends BaseDao {

	/**
	 * 获取对应父节点下菜单节点树
	 * @param parentId		菜单ID
	 * @return				获取对应父节点下菜单节点树
	 * @throws Exception
	 */
	public List<Map<String, Object>> getRoleTreeNode()throws Exception{
		StringBuilder sb = new StringBuilder("");
		List<Map<String, Object>> rootNoteMapList = new ArrayList<Map<String, Object>>();
		try{
			sb.setLength(0);
			sb.append("SELECT * FROM ").append(TableName.TABLE_SYS_ROLE).append(" WHERE DATASTATUSID='1' ORDER BY SORT ASC");
			List<Map<String, Object>> rootMapList = DBTemplate.getInstance(DataSourceName.DATASOURCE_DM).getResultMapList(sb.toString());
			for (int k=0; k<rootMapList.size(); k++){
				Map<String, Object> rootNoteMap = new HashMap<String, Object>();
				rootNoteMap = rootMapList.get(k);
				rootNoteMap.put("ISLEAF", true);
				rootNoteMapList.add(rootNoteMap);
			}
			return rootNoteMapList;
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
}
