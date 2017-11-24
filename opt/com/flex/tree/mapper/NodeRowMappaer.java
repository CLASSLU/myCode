package com.flex.tree.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.flex.tree.bean.NodeBean;

public class NodeRowMappaer implements RowMapper<NodeBean>{

	private static String[] prop = new String[]{"节点ID","父节点ID","节点名称","选中状态","数据条数","层次","是否叶子"};
	
	public NodeBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		NodeBean node = new NodeBean();
		node.setNodeId(rs.getString(prop[0]));
		node.setNodedPId(rs.getString(prop[1]));
		node.setNodeName(rs.getString(prop[2]));
		node.setCheckedState(rs.getString(prop[3]));
		node.setNumCount(rs.getInt(prop[4]));
		node.setLevel(rs.getInt(prop[5]));
		node.setIsLeaf(rs.getInt(prop[6]));
		Map<String,String> map = new HashMap<String,String>();
		int colunmCount = rs.getMetaData().getColumnCount();
		for(int i=0;i<colunmCount;i++){
			map.put(rs.getMetaData().getColumnName(i+1).toUpperCase(), rs.getString(i+1));
		}
		for(String key : prop){
			map.remove(key);
		}
		node.setNodeData(node);
		return node;
	}

}
