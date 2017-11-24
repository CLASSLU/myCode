package com.sdjxd.common.report;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Format {
	static enum type {
		t_number, t_string, t_datetime
	};

	/**
	 * 格式化输出
	 * @param a
	 * @param s
	 * @param format
	 * @return
	 */
	public static String doFormat(type a, String s, String format) {
		if(format==null||format.equals("")){
			return s;
		}
		if(a.t_number == type.t_number){
			BigDecimal bd = new BigDecimal(s);
			DecimalFormat df = new DecimalFormat(format); 
			s = df.format(bd);
		}
		return s;
	}
}
