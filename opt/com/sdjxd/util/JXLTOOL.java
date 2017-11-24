package com.sdjxd.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.apache.struts.upload.FormFile;

import com.sdjxd.pms.platform.data.DbOper;
import com.sdjxd.pms.platform.form.service.FormInstance;
import com.sdjxd.pms.platform.tool.Guid;
import com.sdjxd.pms.platform.workflow.service.Flow;
import com.sdjxd.pms.platform.workflow.service.FlowInstance;
import com.sdjxd.pms.platform.workflow.service.FlowNodeInstance;

/**
 * @author
 * 
 */
public class JXLTOOL {
	private Workbook workbook = null; // 工作部对象

	private HashMap<String, String> mapData = null; // data数据

	private Sheet sheet = null; // 工作表

	public int totalRows = 0; // 总行数

	public int totalCells = 0; // 总列数

	/**
	 * 以一个InputStream为参数的构造器
	 * 
	 * @param inputStream
	 * @throws IOException
	 * @throws BiffException
	 */
	public JXLTOOL(InputStream inputStream) throws BiffException, IOException {
		this.workbook = Workbook.getWorkbook(inputStream);
		this.sheet = this.workbook.getSheet(0);
		this.getRows();
		this.getCells();
	}

	/**
	 * 以一个Struts FormFile为参数的构造器
	 * 
	 * @param file
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws BiffException
	 */
	public JXLTOOL(FormFile file) throws FileNotFoundException, IOException,
			BiffException {
		this(file.getInputStream());
	}

	/**
	 * 以一个File为参数的构造器
	 * 
	 * @param file
	 * @throws IOException
	 * @throws BiffException
	 */
	public JXLTOOL(File file) throws BiffException, IOException {
		this(new FileInputStream(file));
	}

	/**
	 * 以一个文件路径path的构造器
	 * 
	 * @param filePath
	 * @throws IOException
	 * @throws BiffException
	 */
	public JXLTOOL(String filePath) throws BiffException, IOException {

		this(new File(filePath));
	}

	/**
	 * 把所有数据放到一个map中去,key为行号加列号
	 * 
	 * @return
	 */
	public HashMap<String, String> getExcelDate() {
		mapData = new HashMap<String, String>();
		for (int i = 0; i < this.totalRows; i++) {
			for (int j = 0; j < this.totalCells; j++) {
				this.mapData.put(i + "" + j, this.getData(j, i));
			}
		}
		return this.mapData;
	}

	/**
	 * 得到总行数
	 */
	private int getRows() {
		this.totalRows = sheet.getRows();
		return this.totalRows;
	}

	/**
	 * 得到总列数
	 */
	private int getCells() {
		this.totalCells = this.sheet.getColumns();
		return this.totalCells;
	}

	/**
	 * 得到数据
	 * 
	 * @param cell
	 *            列
	 * @param row
	 *            行
	 * @return
	 */
	private String getData(int cell, int row) {
		Cell rs = this.sheet.getCell(cell, row);
		return rs.getContents();
	}

	public String saveToDb(String TableName, String json) {
		String[] insertSqls = new String[this.getRows() - 2];
		String appendSqlColumn = "";
		String appendSqlValue = "";
		JSONObject jsonObject = JSONObject.fromObject(json);
		// 遍历json对象，添加所有的附加参数
		for (Iterator iter = jsonObject.keys(); iter.hasNext();) {
			String key = (String) iter.next();
			System.out.println(jsonObject.getString(key));
			appendSqlColumn += "," + key;
			appendSqlValue += "','" + jsonObject.getString(key);

		}
		String commonSql = "";// 公共部分
		for (int i = 0; i < this.getRows(); i++) {
			if (i == 0) {
				commonSql = " insert into " + DbOper.getSchema() + TableName
						+ " (";
				for (int j = 0; j < this.getCells(); j++) {
					if (j < this.getCells() - 1) {
						commonSql += this.getData(j, i) + ",";
					} else {
						commonSql += this.getData(j, i);
					}
				}
				commonSql += appendSqlColumn;
				commonSql += ") values ('";
			} else if (i > 1) {
				insertSqls[i - 2] = commonSql;
				for (int j = 0; j < this.getCells(); j++) {
					if (j < this.getCells() - 1) {
						insertSqls[i - 2] += this.getData(j, i) + "','";
					} else {
						insertSqls[i - 2] += this.getData(j, i)
								+ appendSqlValue.replaceAll("'GUID'",
										Guid.create()).replaceAll("'sysdate'",
										"sysdate") + "')";
					}
				}
				System.out.println(insertSqls[i - 2]);
			}

		}
		try {
			return DbOper.executeNonQuery(insertSqls) == -1 ? "导入失败！" : "导入成功！";
		} catch (SQLException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@SuppressWarnings("unchecked")
	public String createFlow(String flowVersionId, String patternid, String json) {
		String errorRows = "";
		Map[] maps = new Map[this.getRows() - 2];
		JSONObject jsonObject = JSONObject.fromObject(json);
		// 遍历json对象，添加所有的附加参数
		for (Iterator iter = jsonObject.keys(); iter.hasNext();) {
			String key = (String) iter.next();
			System.out.println(jsonObject.getString(key));
			// appendSqlColumn += "," + key;
			// appendSqlValue += "','" + jsonObject.getString(key);
			for (int i = 0; i < maps.length; i++) {
				Map map = maps[i];
				if (map == null) {
					map = new HashMap<String, String>();
					maps[i] = map;
				}
				map.put(key, jsonObject.getString(key).replaceAll("GUID",
						Guid.create()));
			}

		}
		for (int i = 0; i < this.getRows(); i++) {
			if (i > 1) {
				for (int j = 0; j < this.getCells(); j++) {
					maps[i - 2].put(this.getData(j, 0), this.getData(j, i));
				}

				try {
					FlowInstance flowInstance = Flow
							.createInstance(flowVersionId);
					FlowNodeInstance nodeInstance = flowInstance
							.getLastNodeInstance();
					flowInstance.exec(nodeInstance);
					int flowFormId = nodeInstance.getDefaultForm();
					int nodeInstanceId = nodeInstance.getNodeInstanceId();

					FormInstance formInstance = FormInstance.create(patternid);
					formInstance.setFlowInstance(flowInstance, nodeInstanceId,
							flowFormId);
					flowInstance.addFormInstance(flowFormId, formInstance
							.getId());
					Map<String, String> map = maps[i - 2];
					for (Object obj : map.keySet()) {
						formInstance.initCell(obj.toString().toUpperCase(), map
								.get(obj));
					}
					formInstance.clearCache();
					formInstance.save();
					
				} catch (Exception e) {
					e.printStackTrace();
					if (errorRows.length() > 0) {
						errorRows += i;
					} else {
						errorRows += "," + i;
					}
				}
			}
		}
		if(errorRows.length()>0){
			return "第("+errorRows+")导入失败！";
		}else{
			return "导入成功！";
		}
	}

	public static void main(String[] args) {
		try {
			JXLTOOL jxl = new JXLTOOL("D:\\test.xls");
			jxl.saveToDb("测试表", "");
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
