package kd.idp.index.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ParseTool {
	
	//������ֵ�Ը�ʽ����1[records:һ���ַ��� dataIndex:��Ҫ���ݵ���ʼ����] ��:# 1 ����1 'ֵ1'
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
	
	//������ֵ�Ը�ʽ����2[records:һ���ַ��� dataIndex:��Ҫ���ݵ���ʼ����] ��:<������ 'ϵͳ����Ա'/>
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
	
	//������ֵ�Ը�ʽ����1[records:һ���ַ��� dataIndex:��Ҫ���ݵ���ʼ����] ��:# 1 ����1 'ֵ1'
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
	
	//������ֵ�Ը�ʽ����2[records:һ���ַ��� dataIndex:��Ҫ���ݵ���ʼ����] ��:<������ 'ϵͳ����Ա'/>
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

	//�����б��ʽ��ͷ[headers:һ���ַ���] ��:"@ ��� ������1 ������2 ������3"
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
	
	//�����б��ʽ��ͷ��Ӧ����[records:һ���ַ��� headerList:���ݱ�ͷ dataIndex:��Ҫ���ݼ��ϵ���ʼ����] ��:"# 1 ����1ֵ1 ����1ֵ2 ����1ֵ3"
	public static Map<String, String> parseListFormatData_1(String records, List<String> headerList, int dataIndex) throws Exception{
		Map<String, String> recordMap = new HashMap<String, String>();
		records = records.replaceAll("#D#A", "\r\n").replaceAll("<br/>", "\r\n");
		records = records.replaceAll("\'\'", "��");
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
			System.out.println("��ƥ��");
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
