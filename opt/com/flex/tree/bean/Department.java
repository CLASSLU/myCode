package com.flex.tree.bean;

import java.util.List;


public class Department {

	public String Id;
	
	public String parentId;
	
	public String nodeName;
	
	public String checkFlag = "0";
	
	public int count = -1;
	
	public Object data;
	
	
	public List<Department> children = null;
	
}
