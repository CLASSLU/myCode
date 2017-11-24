package com.sdjxd.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.sdjxd.pms.platform.base.Global;

public class CommonConfig {
	static Properties commonConfig;

	public static String getConfig(String key) {
		if (commonConfig == null||Global.isDebug()) {
			commonConfig = new Properties();

			InputStream inStream = getInStream("commonConfig.properties");
			try {
				commonConfig.load(inStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String value = commonConfig.getProperty(key);
		return (value == null) ? "" : value;
	}

	private static InputStream getInStream(String filename) {
		InputStream inStream;
		try {
			inStream = new FileInputStream(Global.getPath() + "/WEB-INF/"
					+ filename);
		} catch (FileNotFoundException e) {
			try {
				inStream = new FileInputStream(Global.getPath()
						+ "/WEB-INF/classes/" + filename);
			} catch (FileNotFoundException e1) {
				try {
					inStream = new FileInputStream(Global.getPath()
							+ "/WEB-INF/classfactory/" + filename);
				} catch (FileNotFoundException e2) {
					inStream = null;
				}
			}
		}

		return inStream;
	}

}
