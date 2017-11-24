package kd.idp.index.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ParseTool {
	
	//解析键值对格式数据1[records:一行字符串 dataIndex:需要数据的起始索引] 如:# 1 属性1 '值1'
	public static Map<String, String> parseMapFormat_1(String records, int dataIndex) throws Exception{
		Map<String, String> recordMap = new HashMap<String, String>();
		records = records.replaceAll("#D#A", "\r\n").replaceAll("<br/>", "\r\n");
		String[] recordArray = records.split("'"); 
		List<String> dataList = new ArrayList<String>();
		for (int i=0; i<recordArray.length; i++){
			if (0 != i && 0 != i % 2){
				recordArray[i] = recordArray[i];
				dataList.add(recordArray[i]);
			}else{
				recordArray[i] = recordArray[i].replaceAll("	", " ");
				while (-1 != recordArray[i].indexOf("  ")){
					recordArray[i] = recordArray[i].replaceAll("  ", " ");
				}
				String[] tempColumnArray = recordArray[i].split(" ");
				for (String tempColumn : tempColumnArray){
					if (null != tempColumn && !"".equals(tempColumn)){
						dataList.add(tempColumn);
					}
				}
			}
		}
		if (dataIndex == dataList.size()-1){
			dataList.add("");
		}
		recordMap.put(dataList.get(dataIndex), "-".equals(dataList.get(dataIndex + 1)) ? "" : dataList.get(dataIndex + 1));
		return recordMap;
	}
	
	//解析键值对格式数据2[records:一行字符串 dataIndex:需要数据的起始索引] 如:<申请人 '系统管理员'/>
	public static Map<String, String> parseMapFormat_2(String records, int dataIndex) throws Exception{
		Map<String, String> recordMap = new HashMap<String, String>();
		if (records.startsWith("<")){
			records = records.substring(1, records.length());
		}
		if (records.endsWith("/>")){
			records = records.substring(0, records.length()-2);
		}
		records = records.replaceAll("#D#A", "\r\n").replaceAll("<br/>", "\r\n");
		String[] recordArray = records.split("'");
		List<String> dataList = new ArrayList<String>();
		for (int i=0; i<recordArray.length; i++){
			if (0 != i && 0 != i % 2){
				recordArray[i] = recordArray[i];
				dataList.add(recordArray[i]);
			}else{
				recordArray[i] = recordArray[i].replaceAll("	", " ");
				while (-1 != recordArray[i].indexOf("  ")){
					recordArray[i] = recordArray[i].replaceAll("  ", " ");
				}
				String[] tempColumnArray = recordArray[i].split(" ");
				for (String tempColumn : tempColumnArray){
					if (null != tempColumn && !"".equals(tempColumn)){
						dataList.add(tempColumn);
					}
				}
			}
		}
		if (dataIndex == dataList.size()-1){
			dataList.add("");
		}
		recordMap.put(dataList.get(dataIndex), "-".equals(dataList.get(dataIndex + 1)) ? "" : dataList.get(dataIndex + 1));
		return recordMap;
	}
	
	//解析键值对格式数据1[records:一行字符串 dataIndex:需要数据的起始索引] 如:# 1 属性1 '值1'
	public static Map<String, String[]> parseMapFormat_3(String records, int dataIndex) throws Exception{
		Map<String, String[]> recordMap = new HashMap<String, String[]>();
		records = records.replaceAll("#D#A", "\r\n").replaceAll("<br/>", "\r\n");
		String[] recordArray = records.split("'"); 
		List<String> dataList = new ArrayList<String>();
		for (int i=0; i<recordArray.length; i++){
			if (0 != i && 0 != i % 2){
				recordArray[i] = recordArray[i];
				dataList.add(recordArray[i]);
			}else{
				recordArray[i] = recordArray[i].replaceAll("	", " ");
				while (-1 != recordArray[i].indexOf("  ")){
					recordArray[i] = recordArray[i].replaceAll("  ", " ");
				}
				String[] tempColumnArray = recordArray[i].split(" ");
				for (String tempColumn : tempColumnArray){
					if (null != tempColumn && !"".equals(tempColumn)){
						dataList.add(tempColumn);
					}
				}
			}
		}
		String text = "";
		if (dataIndex == dataList.size()-1){
			dataList.add("");
		}
		text = "-".equals(dataList.get(dataIndex + 1)) ? "" : dataList.get(dataIndex + 1);
		recordMap.put(dataList.get(dataIndex), new String[]{text,text});
		return recordMap;
	}
	
	//解析键值对格式数据2[records:一行字符串 dataIndex:需要数据的起始索引] 如:<申请人 '系统管理员'/>
	public static Map<String, String[]> parseMapFormat_4(String records, int dataIndex) throws Exception{
		Map<String, String[]> recordMap = new HashMap<String, String[]>();
		if (records.startsWith("<")){
			records = records.substring(1, records.length());
		}
		if (records.endsWith("/>")){
			records = records.substring(0, records.length()-2);
		}
		records = records.replaceAll("#D#A", "\r\n").replaceAll("<br/>", "\r\n");
		String[] recordArray = records.split("'");
		List<String> dataList = new ArrayList<String>();
		for (int i=0; i<recordArray.length; i++){
			if (0 != i && 0 != i % 2){
				recordArray[i] = recordArray[i];
				dataList.add(recordArray[i]);
			}else{
				recordArray[i] = recordArray[i].replaceAll("	", " ");
				while (-1 != recordArray[i].indexOf("  ")){
					recordArray[i] = recordArray[i].replaceAll("  ", " ");
				}
				String[] tempColumnArray = recordArray[i].split(" ");
				for (String tempColumn : tempColumnArray){
					if (null != tempColumn && !"".equals(tempColumn)){
						dataList.add(tempColumn);
					}
				}
			}
		}
		String text = "";
		if (dataIndex == dataList.size()-1){
			dataList.add("");
		}
		text = "-".equals(dataList.get(dataIndex + 1)) ? "" : dataList.get(dataIndex + 1);
		recordMap.put(dataList.get(dataIndex), new String[]{text,text});
		return recordMap;
	}

	//解析列表格式表头[headers:一行字符串] 如:"@ 序号 属性名1 属性名2 属性名3"
	public static List<String> parseListFormatHeader_1(String headers) throws Exception{
		List<String> headerList = new ArrayList<String>();
		headers = headers.replaceAll("#D#A", "\r\n").replaceAll("<br/>", "\r\n");
		String[] headerArray = headers.split("'");
		for (int i=0; i<headerArray.length; i++){
			if (0 != i && 0 != i % 2){
				headerArray[i] = headerArray[i];
				headerList.add(headerArray[i]);
			}else{
				headerArray[i] = headerArray[i].replaceAll("	", " ");
				while (-1 != headerArray[i].indexOf("  ")){
					headerArray[i] = headerArray[i].replaceAll("  ", " ");
				}
				String[] tempColumnArray = headerArray[i].split(" ");
				for (String tempColumn : tempColumnArray){
					if (null != tempColumn && !"".equals(tempColumn)){
						headerList.add(tempColumn);
					}
				}
			}
		}
		return headerList;
	}
	
	//解析列表格式表头对应数据[records:一行字符串 headerList:数据表头 dataIndex:需要数据集合的起始索引] 如:"# 1 对象1值1 对象1值2 对象1值3"
	public static Map<String, String> parseListFormatData_1(String records, List<String> headerList, int dataIndex) throws Exception{
		Map<String, String> recordMap = new HashMap<String, String>();
		records = records.replaceAll("#D#A", "\r\n").replaceAll("<br/>", "\r\n");
		records = records.replaceAll("\'\'", "无");
		String[] recordArray = records.split("'");
		List<String> columnList = new ArrayList<String>();
		for (int i=0; i<recordArray.length; i++){
			if (0 != i && 0 != i % 2){
				recordArray[i] = recordArray[i];
				columnList.add(recordArray[i]);
			}else{
				recordArray[i] = recordArray[i].replaceAll("	", " ");
				while (-1 != recordArray[i].indexOf("  ")){
					recordArray[i] = recordArray[i].replaceAll("  ", " ");
				}
				String[] tempColumnArray = recordArray[i].split(" ");
				for (String tempColumn : tempColumnArray){
					if (null != tempColumn && !"".equals(tempColumn)){
						columnList.add(tempColumn);
					}
				}
			}
		}
		if (columnList.size() != headerList.size()){
			System.out.println("不匹配");
			return null;
		}else{
			for (int i=dataIndex; i<headerList.size(); i++){
				String tempValue = columnList.get(i);
				if ("-".equals(tempValue)){
					tempValue = "";
				}
				recordMap.put(headerList.get(i), tempValue);
			}
		}
		return recordMap;
	}
	

}
