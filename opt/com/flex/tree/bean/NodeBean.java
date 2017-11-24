package com.flex.tree.bean;

import java.util.List;

public class NodeBean {

	private String nodeId;
	
	private String nodedPId;
	
	private String nodeName;
	
	private Object nodeData;
	
	private String checkedState;//0:δѡ�У�1:ѡ�У�2:��ѡ��״̬(��:�в���Ҷ�ӱ�ѡ��)
	
	private int numCount = -1;
	
	private int level;//���
	
	private int isLeaf;//1:�ӽڵ�,0:���ӽڵ�
	
	private List<NodeBean> children = null;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}


	public String getNodedPId() {
		return nodedPId;
	}

	public void setNodedPId(String nodedPId) {
		this.nodedPId = nodedPId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public Object getNodeData() {
		return nodeData;
	}

	public void setNodeData(Object nodeData) {
		this.nodeData = nodeData;
	}

	public String getCheckedState() {
		return checkedState;
	}

	public void setCheckedState(String checkedState) {
		this.checkedState = checkedState;
	}

	public int getNumCount() {
		return numCount;
	}

	public void setNumCount(int numCount) {
		this.numCount = numCount;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(int isLeaf) {
		this.isLeaf = isLeaf;
	}

	public List<NodeBean> getChildren() {
		return children;
	}

	public void setChildren(List<NodeBean> children) {
		this.children = children;
	}
	
}
