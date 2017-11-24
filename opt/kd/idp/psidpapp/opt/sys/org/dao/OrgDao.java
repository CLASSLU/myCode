package kd.idp.psidpapp.opt.sys.org.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.spring.dbservice.DBTemplate;

import kd.idp.psidpapp.opt.sys.constant.DataSourceName;
import kd.idp.psidpapp.opt.sys.constant.TableName;
import kd.idp.psidpapp.opt.sys.dao.BaseDao;

public class OrgDao extends BaseDao {
	
	/**
	 * ��ȡ��Ӧ����֯����ID�ڵ���
	 * @param parentId		��֯����ID
	 * @return				��ȡ��Ӧ����֯����ID�ڵ���
	 * @throws Exception
	 */
	public List<Map<String, Object>> getOrgTreeNode(String parentId)throws Exception{
		StringBuilder sb = new StringBuilder("");
		List<Map<String, Object>> rootNoteMapList = new ArrayList<Map<String, Object>>();
		try{
			sb.setLength(0);
			sb.append("SELECT * FROM(SELECT * FROM ").append(TableName.TABLE_SYS_ORG).append(" WHERE DATASTATUSID='1' AND CLASS IN('����','��������','�������Ĳ���') AND PARENTID='" + parentId + "' ORDER BY SORT ASC) ");
			List<Map<String, Object>> rootMapList = DBTemplate.getInstance(DataSourceName.DATASOURCE_DM).getResultMapList(sb.toString());
			for (int k=0; k<rootMapList.size(); k++){
				Map<String, Object> rootNoteMap = new HashMap<String, Object>();
				parentId = String.valueOf(rootMapList.get(k).get("ID"));
				sb.setLength(0);
				sb.append("SELECT * FROM(SELECT * FROM ").append(TableName.TABLE_SYS_ORG).append(" WHERE DATASTATUSID='1' AND CLASS IN('����','��������','�������Ĳ���') ORDER BY SORT ASC) ")
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
					//�����糧�����վ��֯����
					if ("����".equals(String.valueOf(nodeMap.get("CLASS"))) && !"����".equals(String.valueOf(nodeMap.get("NAME")))){
						//A�����糧
						Map<String, Object> fdcMap = new HashMap<String, Object>();
						List<Map<String, Object>> fdcMapList = findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_ORG, "DATASTATUSID='1' AND CLASS IN('���糧') AND PARENTID IN('" + String.valueOf(nodeMap.get("ID")) + "')");
						mapListAddAttr(fdcMapList);
						fdcMap.put("ID", String.valueOf(nodeMap.get("ID")));
						fdcMap.put("NAME", String.valueOf("���糧"));
						fdcMap.put("NICKNAME", String.valueOf("���糧(" + 0 + ")"));
						fdcMap.put("PARENTID", String.valueOf(nodeMap.get("ID")));
						fdcMap.put("PARENTIDS", String.valueOf(nodeMap.get("PARENTIDS")));
						fdcMap.put("CLASS", String.valueOf("����ڵ�"));
						fdcMap.put("ISLEAF", true);
						if (null != fdcMapList && 0 < fdcMapList.size()){
							fdcMap.put("children", fdcMapList);
							fdcMap.put("NICKNAME", String.valueOf("���糧(" + fdcMapList.size() + ")"));
							fdcMap.put("ISLEAF", false);
						}
						//B�����վ
						Map<String, Object> bdzMap = new HashMap<String, Object>();
						List<Map<String, Object>> bdzMapList = findMapListTable(DataSourceName.DATASOURCE_DM, TableName.TABLE_SYS_ORG, "DATASTATUSID='1' AND CLASS IN('���վ') AND PARENTID IN('" + String.valueOf(nodeMap.get("ID")) + "')");
						mapListAddAttr(bdzMapList);
						bdzMap.put("ID", String.valueOf(nodeMap.get("ID")) );
						bdzMap.put("NAME", String.valueOf("���վ"));
						bdzMap.put("NICKNAME", String.valueOf("���վ(" + 0 + ")"));
						bdzMap.put("PARENTID", String.valueOf(nodeMap.get("ID")));
						bdzMap.put("PARENTIDS", String.valueOf(nodeMap.get("PARENTIDS")));
						bdzMap.put("CLASS", String.valueOf("����ڵ�"));
						bdzMap.put("ISLEAF", true);
						if (null != bdzMapList && 0 < bdzMapList.size()){
							bdzMap.put("children", bdzMapList);
							bdzMap.put("NICKNAME", String.valueOf("���վ(" + bdzMapList.size() + ")"));
							bdzMap.put("ISLEAF", false);
						}
						childMapList.add(fdcMap);
						childMapList.add(bdzMap);
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
	
	//�����糧�����վ���ISLEAF����
	public List<Map<String, Object>> mapListAddAttr(List<Map<String, Object>> dataMapList)throws Exception{
		for (Map<String, Object> dataMap : dataMapList){
			dataMap.put("ISLEAF", true);
		}
		return dataMapList;
	}
	
	public static void main(String[] args) {
		try {
			List<Map<String, Object>> rootNoteMapList = new OrgDao().getOrgTreeNode("0");
			System.out.println(rootNoteMapList.size());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
