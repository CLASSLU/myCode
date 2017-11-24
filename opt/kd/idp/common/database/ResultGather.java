package kd.idp.common.database;

import java.util.List;
import java.sql.ResultSet;
import java.util.Map;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.ArrayList;
import java.sql.Statement;
import org.apache.log4j.Level;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Logger;
import java.sql.SQLException;
import kd.idp.common.database.DBService;

//import conn.DBConnManager;//数据库连接池
//import java.sql.*;
//import java.util.*;

public class ResultGather {

	public ResultGather() {

	}

	public static ArrayList query(DBService dbService, String sql)
			throws SQLException, Exception {
		return query(dbService, sql, "");
	}

	public static ArrayList query(DBService dbService, String sql, String replace)
			throws SQLException, Exception { // 数据结果在这里装入List和Map
		ArrayList data = new ArrayList();

		ResultSet rs = null;
		Map row;

		Statement stmt = dbService.getStatement();
		rs = stmt.executeQuery(sql);

		ResultSetMetaData rsmd = rs.getMetaData();
		int numberOfColumns = rsmd.getColumnCount();
		while (rs.next()) {
			row = new HashMap(numberOfColumns);
			for (int r = 1; r < numberOfColumns + 1; r++) {
				String value = "";

				if (rs.getObject(rsmd.getColumnName(r)) != null
						&& rs.getObject(rsmd.getColumnName(r)) instanceof String) {
					value = rs.getObject(rsmd.getColumnName(r)).toString();
					row.put(rsmd.getColumnName(r), value);
				} else if (rs.getObject(rsmd.getColumnName(r)) == null) {
					row.put(rsmd.getColumnName(r), replace);
				} else {
					row.put(rsmd.getColumnName(r), rs.getObject(rsmd
							.getColumnName(r)));
				}
			}
			data.add(row);
		}

		return data;

	}
}
