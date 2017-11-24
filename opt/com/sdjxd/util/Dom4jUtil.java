package com.sdjxd.util;

import java.io.StringWriter;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class Dom4jUtil {
	public static String formatToXML(Document doc) {
		try {
			OutputFormat format = new OutputFormat("    ", true);
			// ���ñ���
			format.setEncoding("gb2312");
			// xml�����
			StringWriter out = new StringWriter();
			XMLWriter xmlWriter = new XMLWriter(out, format);
			// ��ӡdoc
			xmlWriter.write(doc);
			xmlWriter.flush();
			// �ر����������������printWriter
			String s = out.toString();
			return s;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
