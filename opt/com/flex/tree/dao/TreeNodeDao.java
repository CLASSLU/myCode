package com.flex.tree.dao;

import java.util.ArrayList;
import java.util.List;
import com.flex.tree.bean.NodeBean;
import com.flex.tree.mapper.NodeRowMappaer;
import com.spring.dbservice.DBTemplate;

public class TreeNodeDao {

	public List<NodeBean> getTreeNodes(String sql) {
		List<NodeBean> nodes = DBTemplate.getInstance().query(sql,
				new NodeRowMappaer());
		if (nodes.size() < 1) {
			return null;
		}
		NodeBean root = nodes.get(0);
		nodes.remove(0);
		addChildren(root, nodes);
		List<NodeBean> result = new ArrayList<NodeBean>();
		return result;
	}

	private void addChildren(NodeBean root, List<NodeBean> nodes) {
		int index = 0;
		while (true) {
			if (index >= nodes.size()) {
				break;
			}
			NodeBean node = nodes.get(index);
			if (root.getNodeId().equals(node.getNodedPId())) {
				if (root.getChildren() == null) {
					root.setChildren(new ArrayList<NodeBean>());
				}
				root.getChildren().add(node);
				nodes.remove(index);
			} else {
				index++;
			}
		}
		if (root.getChildren() != null) {
			for (NodeBean child : root.getChildren()) {
				addChildren(child, nodes);
			}
		}
	}
}
