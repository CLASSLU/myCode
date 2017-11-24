package kd.idp.index.hisdb.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * ≤‚µ„∂‘œÛ
 * @author Administrator
 *
 */
public class MeasureBean implements Serializable {
	
	private String displayName = null;
	private String measureName = null;
	private String measureId   = null;
	private String date        = null;
	private List<String> columnList = new ArrayList<String>();
	private List<Float>  dataList   = new ArrayList<Float>();      
	
	public MeasureBean(String dispayName,String measureId){
		this.displayName = dispayName;
		this.measureId   = measureId;
	}
	
	public String getMeasureName() {
		return measureName;
	}
	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}
	public String getMeasureId() {
		return measureId;
	}
	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}
	
	
	public List<String> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<String> columnList) {
		this.columnList = columnList;
	}

	public List<Float> getDataList() {
		return dataList;
	}

	public void setDataList(List<Float> dataList) {
		this.dataList = dataList;
	}

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
}
