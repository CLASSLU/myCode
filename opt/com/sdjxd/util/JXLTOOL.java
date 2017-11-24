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
	private Workbook workbook = null; // ����������

	private HashMap<String, String> mapData = null; // data����

	private Sheet sheet = null; // ������

	public int totalRows = 0; // ������

	public int totalCells = 0; // ������

	/**
	 * ��һ��InputStreamΪ�����Ĺ�����
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
	 * ��һ��Struts FormFileΪ�����Ĺ�����
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
	 * ��һ��FileΪ�����Ĺ�����
	 * 
	 * @param file
	 * @throws IOException
	 * @throws BiffException
	 */
	public JXLTOOL(File file) throws BiffException, IOException {
		this(new FileInputStream(file));
	}

	/**
	 * ��һ���ļ�·��path�Ĺ�����
	 * 
	 * @param filePath
	 * @throws IOException
	 * @throws BiffException
	 */
	public JXLTOOL(String filePath) throws BiffException, IOException {

		this(new File(filePath));
	}

	/**
	 * ���������ݷŵ�һ��map��ȥ,keyΪ�кż��к�
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
	 * �õ�������
	 */
	private int getRows() {
		this.totalRows = sheet.getRows();
		return this.totalRows;
	}

	/**
	 * �õ�������
	 */
	private int getCells() {
		this.totalCells = this.sheet.getColumns();
		return this.totalCells;
	}

	/**
	 * �õ�����
	 * 
	 * @param cell
	 *            ��
	 * @param row
	 *            ��
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
		// ����json����������еĸ��Ӳ���
		for (Iterator iter = jsonObject.keys(); iter.hasNext();) {
			String key = (String) iter.next();
			System.out.println(jsonObject.getString(key));
			appendSqlColumn += "," + key;
			appendSqlValue += "','" + jsonObject.getString(key);

		}
		String commonSql = "";// ��������
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
			return DbOper.executeNonQuery(insertSqls) == -1 ? "����ʧ�ܣ�" : "����ɹ���";
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
		// ����json����������еĸ��Ӳ���
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
			return "��("+errorRows+")����ʧ�ܣ�";
		}else{
			return "����ɹ���";
		}
	}

	public static void main(String[] args) {
		try {
			JXLTOOL jxl = new JXLTOOL("D:\\test.xls");
			jxl.saveToDb("���Ա�", "");
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
