package com.flex.tree.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.flex.tree.bean.Department;




public class RecomTreeData {
	
	/**
	 * @param datas			数据源
	 * @param id			节点主键ID
	 * @param parentId		父节点ID
	 * @param nodeName		节点名称
	 * @return
	 */
	public Department getTreeData(List<Map<String,Object>> datas,String id,String parentId,String nodeName){
		List<Department> items = new ArrayList<Department>();
		for(Map<String,Object> data : datas){
			Department item = new Department();
			item.data 		= data;
			item.Id 		= (String)data.get(id);
			item.parentId 	= (String)data.get(parentId);
			item.nodeName 	= (String)data.get(nodeName);
			item.checkFlag 	= (String)data.get("是否选中");
			items.add(item);
		}
		Department result = null;
		if(items.size() > 0){
			result = toTreeData(items);
		}
		return result;
	}
	
	public Department getTreeDataByStartId(List<Map<String,Object>> datas,String id,String parentId,String nodeName,String stratId){
		List<Department> items = new ArrayList<Department>();
		for(Map<String,Object> data : datas){
			Department item = new Department();
			item.data 		= data;
			item.Id 		= (String)data.get(id);
			item.parentId 	= (String)data.get(parentId);
			item.nodeName 	= (String)data.get(nodeName);
			item.checkFlag 	= (String)data.get("是否选中");
			items.add(item);
		}
		Department result = null;
		if(items.size() > 0){
			result = toTreeData(items,stratId);
		}
		return result;
	}
	
	private Department toTreeData(List<Department> items, String stratId) {
		Department root = new Department();
		for(int index = 0;index < items.size() ;index++){
			Department item = items.get(index);
			if(stratId.equals(item.Id)){
				root = item;
				items.remove(index);
				break;
			}
		}
		addChildren(root, items);
		return root;
	}

	/**
	 * 分离 数据中的 根节点和子节点
	 * @param items
	 * @return
	 */
	private Department toTreeData(List<Department> items){
		Department root = null;
		for(int index = 0;index < items.size() ;index++){
			Department item = items.get(index);
			if(item.parentId == null || item.parentId == item.Id){
				root = item;
				items.remove(index);
				break;
			}
		}
		if(root == null){
			root = items.get(0);
			items.remove(0);
		}
		addChildren(root, items);
		return root;
	}

	/**
	 * 递归  给 每个根节点set child
	 * @param root
	 * @param items
	 */
	private void addChildren(Department root, List<Department> items) {
		int index = 0;
		while(true){
			if(index >= items.size()){
				break;
			}
			Department item = items.get(index);
			if(root.Id.equals(item.parentId) == true ){
				if(root.children == null){
					root.children = new ArrayList<Department>();
				}
				root.children.add(item);
				items.remove(index);
			}else{
				index++;
			}
		}
		if(root.children != null){
			for(Department child : root.children){
				addChildren(child, items);
			}
		}
	}
	
	
	
}
