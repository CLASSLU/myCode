package com.flex.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.spring.dbservice.DBTemplate;
import com.spring.mapper.HashMapStrRowMapper;
import com.spring.mapper.StringMapper;


public class ExecuteServiceDao {

	private DBTemplate dbt = DBTemplate.getInstance();

	public boolean commonDel(String tableName, String primaryKey, String[] ids) {
		if (tableName == null || primaryKey == null) {
			return false;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("DELETE FROM ").append(tableName).append(" WHERE ").append(
				primaryKey).append(" =? ;");
		dbt.updateSqlWithParam(sb.toString(), ids);
		return true;
	}

	public List<Map<String, String>> query(String sql) {
		List<Map<String, String>> result = dbt.getResultRowMapperList(sql,
				new HashMapStrRowMapper());
		return result;
	}

	public boolean commonAdd(String tableName, Map<String, String> data) {
		String sql = getInsertSql(tableName, data);
		dbt.updateSql(sql);
		return true;
	}

	public boolean commonAddTables(String parentTable,
			Map<String, String> parentMap, String childTbale,
			List<Map<String, String>> childDatas) {
		List<String> sqlList = new ArrayList<String>();
		String parentSql = getInsertSql(parentTable, parentMap);
		sqlList.add(parentSql);
		for (Map<String, String> childData : childDatas) {
			String childSql = getInsertSql(childTbale, childData);
			sqlList.add(childSql);
		}
		dbt.batchExecuteWithTransaction(sqlList);
		return true;
	}

	private String getInsertSql(String tableName, Map<String, String> data) {
		String colnum = "";
		String values = "";
		for (String key : data.keySet()) {
			if ("MX_INTERNAL_UID".equals(key) || "mx_internal_uid".equals(key)) {
				continue;
			}
			colnum += key + ",";
			values += "'" + data.get(key) + "',";
		}
		colnum = colnum.substring(0, colnum.length() - 1);
		values = values.substring(0, values.length() - 1);
		String sql = "INSERT INTO " + tableName + " (" + colnum + ") VALUES("
				+ values + ");";
		sql = sql.replaceAll("'null'", "null").replaceAll("''", "null");
		return sql;
	}

	public boolean commonUpd(String tableName, Map<String, String> data,
			String primaryKey) {
		String sql = getUpdateSql(tableName, data, primaryKey);
		dbt.updateSql(sql);
		return true;
	}
	
	private String getUpdateSql(String tableName, Map<String, String> data,
			String primaryKey) {
		String primaryValue = data.get(primaryKey);
		data.remove(primaryValue);
		String updContent = "";
		for (String key : data.keySet()) {
			updContent += key + "='" + data.get(key) + "',";
		}
		updContent = updContent.substring(0, updContent.length() - 1);
		String sql = "UPDATE " + tableName + " SET " + updContent + " WHERE "
				+ primaryKey + "='" + primaryValue + "';";
		sql = sql.replaceAll("'null'", "null").replaceAll("''", "null");
		return sql;
	}

	public boolean executeSql(String sql) {
		dbt.executeSql(sql);
		return true;
	}

	public Map<String, String> queryRow(String sql) {
		Map<String, String> result = dbt.getResultRowMapper(sql,
				new HashMapStrRowMapper());
		return result;
	}

	public String queryCell(String sql) {
		String result = dbt.getResultRowMapper(sql, new StringMapper());
		return result;
	}

	public String getXML(String filePath, String fileName) throws IOException {
		int len = 0;
		StringBuffer sb = new StringBuffer("");
		File file = new File(filePath + fileName);

		FileInputStream is = new FileInputStream(file);
		InputStreamReader isReader = new InputStreamReader(is, "utf-8");
		BufferedReader in = new BufferedReader(isReader);
		String line = null;
		while ((line = in.readLine()) != null) {
			if (len != 0) { // 处理换行符的问题
				sb.append("\r\n" + line);
			} else {
				sb.append(line);
			}
			len++;
		}
		in.close();
		isReader.close();
		is.close();
		return sb.toString();
	}

}
