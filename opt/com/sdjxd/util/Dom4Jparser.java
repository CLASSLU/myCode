package com.sdjxd.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import sun.misc.BASE64Encoder;

import com.sdjxd.common.FusionCharts.meritit.fusionchart.OracleBlob;
import com.sdjxd.pms.platform.base.Global;
import com.sdjxd.pms.platform.form.service.Form;
import com.sdjxd.pms.platform.form.service.FormCell;
import com.sdjxd.pms.platform.form.service.FormInstance;
import com.sdjxd.pms.platform.form.service.cell.Check;
import com.sdjxd.pms.platform.form.service.cell.ListCell;
import com.sdjxd.pms.platform.form.service.cell.ListStyle;
import com.sdjxd.pms.platform.form.service.cell.VisualCell;

public class Dom4Jparser {
	public static Logger log = Logger.getLogger(Dom4Jparser.class.getName());
	public static BASE64Encoder base64Encoder = new BASE64Encoder();

	public Dom4Jparser() {
		// construction
	}

	@SuppressWarnings("unchecked")
	public static void replaceAllCellText(InputStream input,
			FormInstance formInstance, OutputStream os, String strExportPicIDs) {
		Map allDataListMap = new HashMap();// 记录所有的列表数据
		try {
			VisualCell[] cells = formInstance.getPattern().getCells();
			// 使用 SAXReader 解析 XML 文档 catalog.xml
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(input);
			// Document document = saxReader.read("F:/test.xml");
			// 使用 XPath 表达式从 得到所有的文字结点
			List list = document.selectNodes("//w:t");
			Iterator iter = list.iterator();
			iter = list.iterator();
			while (iter.hasNext()) {
				Element element = (Element) iter.next();
				String oldString = element.getText();
				// 普通文本处理
				Pattern p = Pattern.compile("\\$\\{\\d+\\}");
				Matcher m = p.matcher(oldString);
				boolean b = m.find();
				while (b) {
					String tempSub = oldString.substring(m.start() + 2,
							m.end() - 1);
					int cellId = Integer.valueOf(tempSub);
					VisualCell cell = cells[cellId];

					if (cell instanceof Check) {
						// System.out.println(cell.getLabel());

						String celltext = formInstance.getCellText(cellId);
						Element tempelement = element.getParent().getParent();
						Element newtempelement = tempelement.createCopy();
						if (celltext != null) {
							String[] celltextArray = celltext.split(",");
							boolean first = true;
							Element temprowelement = tempelement;// 临时结点，始终指向最后一个
							for (String t : celltextArray) {
								if (first) {
									oldString = oldString.replaceFirst(p
											.pattern(), t);
									first = false;
								} else {
									Element copyelement = newtempelement
											.createCopy();
									if (copyelement.selectNodes(".//w:t")
											.size() > 0) {
										Element telement = (Element) (copyelement
												.selectNodes(".//w:t")).get(0);
										String copycelltext = telement
												.getText();
										telement.setText(copycelltext
												.replaceFirst(p.pattern(), t));
										List childList = tempelement
												.getParent().elements();
										childList.add(childList
												.indexOf(temprowelement) + 1,
												copyelement);
										temprowelement = copyelement;
									}
								}
							}
						}

					} else {
						oldString = oldString.replaceFirst(p.pattern(),
								formInstance.getCellText(cellId) == null ? ""
										: formInstance.getCellText(cellId));
					}
					m = p.matcher(oldString);
					b = m.find();
				}
				element.setText(oldString);
				// 列表元件处理
				p = Pattern.compile("\\$\\{list\\d+\\.\\w+\\}");
				m = p.matcher(oldString);
				b = m.find();
				if (b) {
					Element temprowelement = element.getParent();
					// 循环向上找到列表的行结点
					while (!temprowelement.getQualifiedName().equals("w:tr")
							&& temprowelement.getParent() != null) {
						temprowelement = temprowelement.getParent();
					}
					Element tableElement = null;
					if (!temprowelement.getQualifiedName().equals("w:tr")) {
						continue;
					} else {
						tableElement = temprowelement.getParent();
					}
					Element copytemprowelement = temprowelement.createCopy();
					log.debug(tableElement.getStringValue());
					String tempSub = listcellidAndName(oldString);
					int cellId = Integer.valueOf(tempSub.split("\\.")[0]);
					Form form = formInstance.getPattern();
					try {
						boolean firstFlag = true;
						String styleId = ((ListCell) (form.getCells()[cellId]))
								.getStyleId();
						ListStyle listStyle = ListCell.getListData(formInstance
								.getId(), styleId, null, null, -1);
						List<Map> dataList = listStyle.getData();
						allDataListMap.put("list" + cellId, dataList);
						// 没有数据的时候将原来的替换字段清空
						if (dataList == null || dataList.size() == 0) {
							List<Element> rowTextlist = temprowelement
									.selectNodes("//w:t");
							for (Element textElement : rowTextlist) {
								String textCellOldtext = textElement.getText();
								p = Pattern.compile("\\$\\{list\\d+\\.\\w+\\}");
								m = p.matcher(textCellOldtext);
								b = m.find();
								if (b) {
									String textcellOld = listcellidAndName(textCellOldtext);
									int textcellId = Integer
											.valueOf(textcellOld.split("\\.")[0]);
									String realValue = ("");
									if (realValue == null) {
										realValue = "";
									}
									textCellOldtext = textCellOldtext
											.replaceAll(
													"\\$\\{list" + textcellId
															+ "\\.\\w+\\}",
													realValue);
									textElement.setText(textCellOldtext);
								}
							}
						}
						for (Map dataInfo : dataList) {
							if (!firstFlag) {// 如果不是第一次创建一个新的拷贝
								Element old = temprowelement;
								temprowelement = copytemprowelement
										.createCopy();
								// tableElement.add(temprowelement);
								List childList = tableElement.elements();
								childList.add(childList.indexOf(old) + 1,
										temprowelement);
							}
							firstFlag = false;
							List<Element> rowTextlist = temprowelement
									.selectNodes("descendant::w:t");
							for (Element textElement : rowTextlist) {
								String textCellOldtext = textElement.getText();
								p = Pattern.compile("\\$\\{list\\d+\\.\\w+\\}");
								m = p.matcher(textCellOldtext);
								b = m.find();
								if (b) {
									String textcellOld = listcellidAndName(textCellOldtext);
									int textcellId = Integer
											.valueOf(textcellOld.split("\\.")[0]);
									String columnName = textcellOld
											.split("\\.")[1];
									String realValue = (String) dataInfo
											.get(columnName.toUpperCase());
									if (realValue == null) {
										realValue = "";
									}
									textCellOldtext = textCellOldtext
											.replaceAll(
													"\\$\\{list" + textcellId
															+ "\\.\\w+\\}",
													realValue);
									textElement.setText(textCellOldtext);
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// 合计
				p = Pattern.compile("\\$count\\{list\\d+\\.\\w+\\}");
				m = p.matcher(oldString);
				b = m.find();
				while (b) {
					String tempSub = oldString.substring(m.start() + 7,
							m.end() - 1);
					String cellId = tempSub.split("\\.")[0];
					String columnName = tempSub.split("\\.")[1];
					// 得到数据列表
					List<Map> dataList = (List<Map>) allDataListMap.get(cellId);
					double counts = 0;
					for (Map dataInfo : dataList) {
						Object value = dataInfo.get(columnName);
						counts += Double.valueOf((String) value);
					}
					oldString = oldString.replaceFirst(p.pattern(), String
							.valueOf(counts).equals("0") ? "" : String
							.valueOf(counts));
					// 如果是整数的时候去掉小数部分
					oldString = oldString.replaceAll("\\.0+$", "");
					element.setText(oldString);
					m = p.matcher(oldString);
					b = m.find();
				}
			}

			if (strExportPicIDs != null && strExportPicIDs.compareTo("NONE") != 0) // 导出图表
			{
				java.util.Properties dbProps = new java.util.Properties();
				dbProps = Global.loadProperties("jdbc.properties");
				String url = dbProps.getProperty("jdbc.url");
				String username = dbProps.getProperty("jdbc.username");
				String password = dbProps.getProperty("jdbc.password");
				OracleBlob ob = new OracleBlob(url, username, password);

				String temp;
				String[] arrPicIDs = strExportPicIDs.split(",");
				byte[] data = null;
				String strPicBase64 = "";
				List imglist = document.selectNodes("//w:binData");
				Iterator imgiter = imglist.iterator();
				imgiter = imglist.iterator();
				int i = 0;
				while (imgiter.hasNext() && i < arrPicIDs.length) {
					Element element = (Element) imgiter.next();
					String oldString = element.getText();

					data = ob.GetImgByteById(null, arrPicIDs[i]);
					strPicBase64 = base64Encoder.encode(data);

					temp = "\\$IMG0" + String.valueOf(i + 1);
					oldString = oldString.replaceAll(temp, strPicBase64);
					element.setText(oldString);
					i++;
				}
			}
			else if (strExportPicIDs != null && strExportPicIDs.compareTo("NONE") == 0)
			{
				List imgbinlist = document.selectNodes("//w:binData");
				List imgshapelist = document.selectNodes("//v:shape");
				for (int i=0; i<imgbinlist.size(); i++)
				{
					Node binnode = (Node) imgbinlist.get(i);
					Node shapenode = (Node) imgshapelist.get(i);
					Element element = binnode.getParent();
					element.remove(binnode);
					element.remove(shapenode);
				};
			}

			OutputFormat of = new OutputFormat("   ", true);
			XMLWriter xw = new XMLWriter(os, of);
			xw.write(document);
			xw.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 得到列表的ID和字段名
	 * 
	 * @param oldString
	 * @return
	 */
	private static String listcellidAndName(String oldString) {
		Pattern p = Pattern.compile("\\$\\{list\\d+\\.\\w+\\}");
		Matcher m = p.matcher(oldString);
		boolean b = m.find();
		if (b) {
			String tempSub = oldString.substring(m.start() + 6, m.end() - 1);
			return tempSub;
		} else {
			return oldString;
		}
	}

	public static void main(String argv[]) {
		Dom4Jparser dom4jParser = new Dom4Jparser();
		// dom4jParser
		// .modifyDocument(new File(
		// "F:/test.xml"));
	}

}
