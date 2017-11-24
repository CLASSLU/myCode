package com.sdjxd.common.report;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.RowSet;

import com.sdjxd.pms.platform.data.DbOper;
import com.sdjxd.pms.platform.tool.StringTool;

public class ReportDataSet {
	private String id;
	private String sql_select;// sql 语句中的select 查询相关字段，不包含select如id,name,age
	private String dyb;// 对应表
	private String cxtj;// 查询条件
	private String sql_other;// sql 查询语句中除select查询字段以外的部分

	/**
	 * 限制不能通过new创建对象
	 */
	private ReportDataSet() {

	}

	public String getSql_select() {
		return sql_select;
	}

	public void setSql_select(String sql_select) {
		this.sql_select = sql_select;
	}

	public String getSql_other() {
		return sql_other;
	}

	public void setSql_other(String sql_other) {
		this.sql_other = sql_other;
	}

	/**
	 * 生成数据显示html
	 * 
	 * @return
	 */
	public String viewHtml() {
		RowSet rs = null;
		StringBuffer html = new StringBuffer(
				"<table width=\"100%\" style = 'background-color:#6095AE' border=\"0\" cellspacing=\"1\" cellpadding=\"0\">");
		try {
			String sql = "select "
					+ this.sql_select
					+ " from "
					+ this.getDyb()
					+ " where "
					+ (this.getCxtj() == null || this.getCxtj().length() == 0 ? "1=1"
							: this.getCxtj()) + " "
					+ (this.sql_other == null ? "" : this.sql_other);
			sql = sql.replaceAll("\\{", "[");
			sql = sql.replaceAll("\\}", "]");
			rs = DbOper.executeQuery(StringTool.replaceKeyWord(sql));
			String[] columnArray = this.sql_select.split(",");
			html
					.append("<tr style='background-color:#D1EBF7; text-align:center'>");
			int columnSize = rs.getMetaData().getColumnCount();
			for (int i = 0; i < columnSize; ++i) {
				html
						.append("<th style='background-color:#B9D8F5; text-align:center'>");
				html.append(rs.getMetaData().getColumnName(i + 1));
				html.append("<th>");
			}
			html.append("</tr>");
			boolean flag = false;
			while (rs.next()) {
				html.append("<tr>");
				for (int i = 0; i < columnSize; ++i) {
					if (flag) {
						html.append("<td style = 'background-color:#E9F3FF'>");
					} else {
						html.append("<td style = 'background-color:white'>");
					}
					html.append(rs.getString(i + 1));
					html.append("<td>");
				}
				html.append("</tr>");
				flag = !flag;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			rs = null;
		}
		html.append("</table>");
		return html.toString();
	}

	/**
	 * 通过静态方法得到数据显示字符串
	 * 
	 * @param sql_select
	 * @param sql_other
	 * @return
	 */
	public static String getViewHtml(String sql_select, String sql_other) {
		ReportDataSet rds = new ReportDataSet();
		rds.setSql_select(sql_select);
		rds.setSql_other(sql_other);
		return rds.viewHtml();
	}

	/**
	 * 
	 * @param sheetid
	 * @return
	 */
	public static String getViewHtml(String sheetid) {
		ReportDataSet rds = new ReportDataSet();
		RowSet rs;
		try {
			rs = DbOper
					.executeQuery("select * from  XMGG_REPORT_DATASET t where t.sheetid = '"
							+ sheetid + "'");
			if (rs.next()) {
				rds.setSql_select(rs.getString("SQL_SELECT"));
				rds.setDyb(rs.getString("DYB"));
				rds.setCxtj(rs.getString("CXTJ"));
				rds.setSql_other(rs.getString("SQL_OTHER"));
			} else {
				return "";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			rs = null;
		}

		return rds.viewHtml();
	}

	/**
	 * 返回实例
	 * 
	 * @param sheetid
	 * @return
	 */
	public static ReportDataSet instanceOfReportDataSet(String sheetid) {
		ReportDataSet rds = new ReportDataSet();
		RowSet rs;
		try {
			rs = DbOper
					.executeQuery("select * from  XMGG_REPORT_DATASET t where t.sheetid = '"
							+ sheetid + "'");
			if (rs.next()) {
				rds.setSql_select(rs.getString("SQL_SELECT"));
				rds.setDyb(rs.getString("DYB"));
				rds.setCxtj(rs.getString("CXTJ"));
				rds.setSql_other(rs.getString("SQL_OTHER"));
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			rs = null;
		}
		return rds;
	}

	/**
	 * 通过传入条件计算统计数量
	 * 
	 * @param condition
	 *            查询条件
	 * @return
	 */
	public int count(String condition) {
		int result = 0;
		try {
			String sql = "select count(1) as result "
					+ " from "
					+ this.getDyb()
					+ " where "
					+ (this.getCxtj() == null || this.getCxtj().length() == 0 ? "1=1"
							: this.getCxtj()) + " ";
			if (condition != null && condition.trim().length() > 0) {
				sql += " and " + condition + " ";
			}
			sql += (this.sql_other == null ? "" : this.sql_other);
			sql = sql.replaceAll("\\{", "[");
			sql = sql.replaceAll("\\}", "]");
			RowSet rs = DbOper.executeQuery(StringTool.replaceKeyWord(sql));
			if (rs.next()) {
				result = rs.getInt("result");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	public int sum(String column,String condition) {
		int result = 0;
		try {
			String sql = "select sum("+column+") as result "
					+ " from "
					+ this.getDyb()
					+ " where "
					+ (this.getCxtj() == null || this.getCxtj().length() == 0 ? "1=1"
							: this.getCxtj()) + " ";
			if (condition != null && condition.trim().length() > 0) {
				sql += " and " + condition + " ";
			}
			sql += (this.sql_other == null ? "" : this.sql_other);
			sql = sql.replaceAll("\\{", "[");
			sql = sql.replaceAll("\\}", "]");
			RowSet rs = DbOper.executeQuery(StringTool.replaceKeyWord(sql));
			if (rs.next()) {
				result = rs.getInt("result");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 得到数据集查询返回的结果
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getDataSetResultList() {
		List<Object> dataSetResultList = new ArrayList<Object>();
		try {
			String sql = "select "
					+ this.sql_select
					+ " from "
					+ this.getDyb()
					+ " where "
					+ (this.getCxtj() == null || this.getCxtj().length() == 0 ? "1=1"
							: this.getCxtj()) + " "
					+ (this.sql_other == null ? "" : this.sql_other);
			sql = sql.replaceAll("\\{", "[");
			sql = sql.replaceAll("\\}", "]");
			dataSetResultList = DbOper.executeList(StringTool
					.replaceKeyWord(sql));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return dataSetResultList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCxtj() {
		return cxtj;
	}

	public void setCxtj(String cxtj) {
		this.cxtj = cxtj;
	}

	public String getDyb() {
		return dyb;
	}

	public void setDyb(String dyb) {
		this.dyb = dyb;
	}
}
