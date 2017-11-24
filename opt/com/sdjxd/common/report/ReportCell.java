package com.sdjxd.common.report;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.DefaultEditorKit.CutAction;

import org.apache.axis.utils.CLUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import net.sf.json.JSONArray;

import com.ql.util.express.ExpressRunner;
import com.sdjxd.pms.platform.base.Global;
import com.sdjxd.pms.platform.form.model.CellBean;
import com.sdjxd.pms.platform.form.service.Form;
import com.sdjxd.pms.platform.form.service.FormInstance;
import com.sdjxd.pms.platform.form.service.cell.DynamicCell;
import com.sdjxd.pms.platform.tool.StringTool;
import com.sdjxd.util.Dom4jUtil;

public class ReportCell extends DynamicCell {
	private static Logger log = Logger.getLogger(ReportCell.class.getName());
	ReportDao dao = new ReportDao();
	private String reportId;
	private Map<Integer, List<ReportCellInstanceInfo>> allCellsMap = new TreeMap<Integer, List<ReportCellInstanceInfo>>();// 报表上所人单元格实例信息MAP中只存放未扩展的行
	private Map<Integer, List<ReportCellInstanceInfo>> allInstanceCellsMap = new TreeMap<Integer, List<ReportCellInstanceInfo>>();// 报表上所有单元格实例信息
	private Map<Integer, ReportRowInstance> rriMap = new HashMap<Integer, ReportRowInstance>();
	private int rowIndex = 1;// 报表当前的行号
	private Map<String, ReportDataSet> dataSetMap = new HashMap<String, ReportDataSet>();
	ExpressRunner runner = new ExpressRunner();
	private boolean isLoadData = true;

	// 定义计算类型
	class CoutType {
		public static final int CELLVALUE = 1;// 计算元件值
		public static final int CLICKVALUE = 2;// 计算单击事件值
	};

	// 构造函数
	public ReportCell(Form pattern) {

		super(pattern);
	}

	public void setData(CellBean model) throws Exception {
		super.setData(model);
		if (!model.assitInfo.equals("")) {
			JSONArray jsonArray = JSONArray.fromObject(this.data.assitInfo);
			reportId = jsonArray.getString(0);
		}
		if (model.cellLabel.equals("false") || model.cellLabel.equals("0")) {
			isLoadData = false;
		}
	}

	// 在加载页面信息的时候调用，生成显示html语言
	public void render(FormInstance form) throws IOException {
		// 获得当前页面的文本输出对象
		PrintWriter html = form.getRenderHtml();
		// 定义一个Path变量用来存储flash的路径
		String Path = Global.getName() + this.label;
		// 将路径中的“\”替换成“/”，以便格式符合html语言中的需要
		Path = StringTool.replace(Path, "\\", "/");
		// 输出生成的html语句
		JSONArray jsonArray = JSONArray.fromObject(this.data.assitInfo);
		String reportId = jsonArray.getString(0);
		html.write("<div id='" + getCellId()
				+ "' style=\"position:absolute;top:" + this.top + ";left:"
				+ this.left + "\">");
		if (isLoadData) {
			html.write(createHtml(reportId));
		}
		html.write("</div>");
		// 调用renderScriptObject，以便生成初始化flash的脚本语句，参数form为表单对象
		renderScriptObject(form);
	}

	protected void renderScriptObject(FormInstance form) {
		StringBuffer sb = new StringBuffer();
		// 生成创建报表元件的脚本语句
		sb.append("defaultForm.addChild( ");
		// {}中为参数，在js脚本初始化时可以对这些参数进行引用
		sb.append("new com.sdjxd.common.userDefinedCell.ReportCell({id:");
		sb.append(this.id);
		sb.append(",needSave:false");
		sb.append(",divId:").append(this.data.areaId);
		sb.append(",tagId:'");
		sb.append(this.cellId);
		sb.append("'}));\r\n");
		// 将新建的flash对象添加到当前表单中去
		form.addScript("form_obj_cell_" + this.id, sb.toString());
	}

	/**
	 * 创建html代码
	 * 
	 * @param reportId
	 * @return
	 */
	protected String createHtml(String reportId) {
		Document document = DocumentHelper.createDocument();
		Element divElement = document.addElement("div");
		Element tableElement = divElement.addElement("table");
		tableSetStyle(tableElement);
		SqlInfo sqlInfo = new SqlInfo();
		// 所有单元格信息以sheetid为键值
		Map<String, ReportCellInfo> cellMap = new HashMap<String, ReportCellInfo>();
		// 所有单元格信息第一级以行的sheetid为键，第二级以列的sheetid为键
		Map<String, HashMap<String, ReportCellInfo>> colMap = new HashMap<String, HashMap<String, ReportCellInfo>>();
		// 所有单元格信息第一级为列的sheetid为键,第二级以行的sheetid为键。
		Map<String, HashMap<String, ReportCellInfo>> rowMap = new HashMap<String, HashMap<String, ReportCellInfo>>();
		List<ReportCellInfo> rciList = dao.getReportCellInfoList(reportId);
		for (ReportCellInfo rci : rciList) {
			// 所有单元格MAP初始化
			cellMap.put(rci.getSheetId(), rci);
			// 为以行为索引的MAP初始化，第一级的键为行sheetid,第二级的键为列sheetid.
			if (colMap.get(rci.getColSheetId()) == null) {
				colMap.put(rci.getColSheetId(),
						new HashMap<String, ReportCellInfo>());
			}
			colMap.get(rci.getColSheetId()).put(rci.getRowSheetId(), rci);
			// 为以列为键索引的MSP初始化，第一次的键为列sheetid,第二级的键为行sheetid.
			if (rowMap.get(rci.getRowSheetId()) == null) {
				rowMap.put(rci.getRowSheetId(),
						new HashMap<String, ReportCellInfo>());
			}
			rowMap.get(rci.getRowSheetId()).put(rci.getColSheetId(), rci);
		}
		// 得到行的信息以列表形式加载列表内为每一行的具体信息
		List<Map<String, String>> rowList = (List<Map<String, String>>) ReportCellDefined
				.getRow(reportId);
		// 得到列的信息以列表形式加载列表内为每一列的具体信息
		List<Map<String, String>> colList = (List<Map<String, String>>) ReportCellDefined
				.getCol(reportId);
		// 先遍历每一行数据以行为单位创建报表，不对单元格进行扩展
		for (int rowi = 0; rowi < rowList.size(); rowi++) {
			Map rowInfo = rowList.get(rowi);
			// 扩展前的信息
			ReportRowInstance rri = new ReportRowInstance();
			rri.setRowIndex(rowi);
			rri.setRowStyle((String) rowInfo.get("YSB"));
			rri.setRowHight(Integer
					.valueOf((rowInfo.get("HEIGHT") == null ? "0"
							: (String) rowInfo.get("HEIGHT"))));
			rri.setHeadRow(Integer.valueOf((rowInfo.get("SFBT") == null ? "0"
					: (String) rowInfo.get("SFBT"))));
			rriMap.put(rowi + 1, rri);// 保存扩展前的信息
			Map<Integer, ReportCellInfo> tempRowMap = new TreeMap();// 临时行数据
			int cellIndex = 1;// 单元格列索引
			ReportCellInfo mainCell = null;// 主单元格
			// 遍历每一列数据
			for (Map colInfo : colList) {
				ReportCellInfo tempRci = null;
				if (rowMap.size() > 0
						&& rowMap.get(rowInfo.get("SHEETID")) != null) {
					tempRci = rowMap.get(rowInfo.get("SHEETID")).get(
							colInfo.get("SHEETID"));
				}
				// 如果指定单元格不存在创建一个新单元格
				if (tempRci == null) {
					tempRci = new ReportCellInfo();
				}
				// 设置当前主格
				if (tempRci.getIsMainCell() == 1) {
					mainCell = tempRci;
				}
				tempRci.setRowIndex(Integer.valueOf((String) rowInfo
						.get("ROWINDEX")));// 设置行号
				tempRci.setColIndex(Integer.valueOf((String) colInfo
						.get("COLINDEX")));// 设置列号
				tempRowMap.put(cellIndex++, tempRci);
			}
			allCellsMap.put(Integer.valueOf((String) rowInfo.get("ROWINDEX")),
					new ArrayList());
			// 对单元格进行扩展和值计算
			if (mainCell != null
					&& (mainCell.getExpandDirection() == null
							|| mainCell.getExpandDirection().length() == 0 || mainCell
							.getExpandDirection().equals("1"))) {// 是扩展单元格时为当前表格进行扩展扩展方向为向下或是默认扩展方向时执行如下操作
				// 主单元格合并后占的每一行数据的一个MAP映射
				Map<Integer, Map<Integer, ReportCellInfo>> unionRowMap = new HashMap<Integer, Map<Integer, ReportCellInfo>>();
				unionRowMap.put(1, tempRowMap);
				// 主单元格合并后所占行的行信息的一个映射
				Map<Integer, Map<?, ?>> unionRowInfoMap = new HashMap<Integer, Map<?, ?>>();
				unionRowInfoMap.put(1, rowInfo);
				// 生成一个主单元格的实例
				ReportCellInstanceInfo mainrcii = new ReportCellInstanceInfo();
				mainrcii.setValue(mainCell.getValue());
				// 主格对应的查询结果
				List<Map> resultList = (List<Map>) countCellValue(mainrcii,
						CoutType.CELLVALUE);
				String mainCellDataSetName = getDataSetName(mainCell.getValue());
				int expandRowIndex = 0;// 行扩展索引
				// 遍历主格查询结果的所有数据
				for (Map dataSetRowValue : resultList) {
					// 主格为合并单元格的时候
					for (int unionRowi = 0; unionRowi < mainCell.getRowspan(); unionRowi++) {
						// 当为原始主格的的扩展行的时候进行的操作
						if (unionRowMap.get(unionRowi + 1) == null) {
							// 定义一个扩展行的数据映射
							Map<Integer, ReportCellInfo> uniontempRowMap = new TreeMap();
							// 得到当前行的模板行信息
							Map unionrowInfo = rowList.get(rowi + unionRowi);
							// 扩展前的信息
							ReportRowInstance uniontemprri = new ReportRowInstance();
							uniontemprri.setRowIndex(rowi + unionRowi);// 行索引
							uniontemprri.setRowStyle((String) unionrowInfo
									.get("YSB"));// 样式表
							uniontemprri
									.setRowHight(Integer.valueOf((unionrowInfo
											.get("HEIGHT") == null ? "0"
											: (String) unionrowInfo
													.get("HEIGHT"))));// 行高度
							uniontemprri
									.setHeadRow(Integer
											.valueOf((unionrowInfo.get("SFBT") == null ? "0"
													: (String) unionrowInfo
															.get("SFBT"))));// 是否表头
							rriMap.put(rowi + unionRowi + 1, uniontemprri);// 保存扩展前的信息
							allCellsMap.put(Integer
									.valueOf((String) unionrowInfo
											.get("ROWINDEX")), new ArrayList());// 添加所有扩展前的单元格信息
							// 生成每一列数据
							for (Map colInfo : colList) {
								ReportCellInfo tempRci = null;
								// 如果存在当前单元格，将当前单元格查找到
								if (rowMap.size() > 0
										&& rowMap.get(unionrowInfo
												.get("SHEETID")) != null) {
									tempRci = rowMap.get(
											unionrowInfo.get("SHEETID")).get(
											colInfo.get("SHEETID"));
								}
								// 如果指定单元格不存在创建一个新单元格
								if (tempRci == null) {
									tempRci = new ReportCellInfo();
								}
								tempRci.setRowIndex(Integer
										.valueOf((String) unionrowInfo
												.get("ROWINDEX")));// 设置行索引
								tempRci.setColIndex(Integer
										.valueOf((String) colInfo
												.get("COLINDEX")));// 设置列索引
								uniontempRowMap.put(cellIndex++, tempRci);// 当前行映射中添加当前单元格
							}

							unionRowInfoMap.put(unionRowi + 1, unionrowInfo);// 合并行信息映射中添加当前行信息
							unionRowMap.put(unionRowi + 1, uniontempRowMap);// 合并行单元格信息映射中添加当前行单元格映射
						}
						// 报表所有单元格实例中添加当前行
						allInstanceCellsMap.put(rowIndex, new ArrayList());
						// 循环主单元格合并的所有行单元格映射数据
						for (int tempCellIndex : unionRowMap.get(unionRowi + 1)
								.keySet()) {
							// 生成单元格实例
							ReportCellInstanceInfo temprcii = new ReportCellInstanceInfo();
							// 查找模板单元格
							ReportCellInfo temprci = unionRowMap.get(
									unionRowi + 1).get(tempCellIndex);
							temprcii.setRowspan(temprci.getRowspan());// 复制行数
							temprcii.setColspan(temprci.getColspan());// 复制列数
							temprcii
									.setWidth(colList.get(
											temprci.getColIndex() - 1).get(
											"WIDTH") == null ? "" : colList
											.get(temprci.getColIndex() - 1)
											.get("WIDTH"));// 设置单元格的宽度
							temprcii.setHeigth((String) unionRowInfoMap.get(
									unionRowi + 1).get("HEIGHT"));// 设置单元格的高度
							temprcii.setStyle(temprci.getStyle());
							// 扩展前源坐标
							temprcii
									.setCellCoordinate(new ReportCellCoordinate(
											temprci.getColIndex(), temprci
													.getRowIndex()));// 设置原始坐标
							if (expandRowIndex > 0) {
								// 设置扩展坐标
								temprcii
										.setExpandCellCoordinate(new ReportCellCoordinate(
												0, expandRowIndex));
							}
							// 设置当前单元格的原始值
							temprcii.setValue(temprci.getValue());
							// 得到原始的点击事件
							temprcii.setClickEvent(temprci.getClickEvent());
							// 使用数据集与主格相同时
							if (mainCellDataSetName
									.equals(getDataSetName(temprci.getValue()))) {
								// 计算当前单元格的实际值，并以当前行数据映射进行替换
								temprcii.setValue(countCellValue(temprcii,
										dataSetRowValue, CoutType.CELLVALUE));
								temprcii.setClickEvent(countCellValue(temprcii,
										dataSetRowValue, CoutType.CLICKVALUE));
							} else {
								// 计算当前单元格的实际值
								temprcii.setValue((String) countCellValue(
										temprcii, CoutType.CELLVALUE));
								temprcii.setClickEvent((String) countCellValue(
										temprcii, CoutType.CLICKVALUE));
							}
							temprcii.setDataType(temprci.getDataType());
							temprcii.setFormat(temprci.getFormat());
							if (temprcii.getDataType() != null
									&& temprcii.getDataType().equals("数字")) {
								try {
									Object o = runner.execute(temprcii
											.getValue(), null, false, null);
									temprcii.setValue(Format.doFormat(
											Format.type.t_number, String
													.valueOf(o), temprcii
													.getFormat()));
								} catch (Exception e) {
									log.error("错误的表达式：" + temprcii.getValue()
											+ e.getMessage());
									e.printStackTrace();
								}
							}
							// 如果当前行号与没有扩展前的行的行号相同，设置原始单元格
							if (allCellsMap.get(
									Integer
											.valueOf((String) unionRowInfoMap
													.get(unionRowi + 1).get(
															"ROWINDEX")))
									.size() < unionRowMap.get(unionRowi + 1)
									.size()) {
								allCellsMap
										.get(
												Integer
														.valueOf((String) unionRowInfoMap
																.get(
																		unionRowi + 1)
																.get("ROWINDEX")))
										.add(temprcii);
							} else {// 设置扩展单元格，为原始单元格添加扩展单元格
								List<ReportCellInstanceInfo> rciiExpanList = allCellsMap
										.get(temprci.getRowIndex()).get(
												temprci.getColIndex() - 1)
										.getRciiExpandList();
								// 如果扩展单元格列表为空则新创建一个扩展单元格列表
								if (rciiExpanList == null) {
									rciiExpanList = new ArrayList();
									allCellsMap.get(temprci.getRowIndex()).get(
											temprci.getColIndex() - 1)
											.setRciiExpandList(rciiExpanList);
								}
								// 在原始单元格的扩展单元格列表中添加当前单元格
								rciiExpanList.add(temprcii);
								allCellsMap
										.get(
												Integer
														.valueOf((String) unionRowInfoMap
																.get(
																		unionRowi + 1)
																.get("ROWINDEX")))
										.add(temprcii);
							}
							// 为所有报表单元格当前行添加当前单元格
							allInstanceCellsMap.get(rowIndex).add(temprcii);
							String topXY = temprci.getTopMain();
							/**
							 * 如果是双向扩展进行向右扩展
							 */
							if (topXY != null && topXY.length() > 0) {
								temprcii.setHaveTopMainCell(true);
								ReportCellInstanceInfo topMainCellInstance = getSourceCellByString(topXY);
								if (topMainCellInstance.getRciiExpandList() != null
										&& topMainCellInstance
												.getRciiExpandList().size() > 0) {
									for (int xei = 1; xei <= topMainCellInstance
											.getRciiExpandList().size(); xei++) {
										// 生成单元格实例
										ReportCellInstanceInfo temprciiexi = new ReportCellInstanceInfo();
										// 查找模板单元格
										ReportCellInfo temprciexi = unionRowMap
												.get(unionRowi + 1).get(
														tempCellIndex);
										temprciiexi.setRowspan(temprciexi
												.getRowspan());// 复制行数
										temprciiexi.setColspan(temprciexi
												.getColspan());// 复制列数
										temprciiexi
												.setWidth(colList
														.get(
																temprciexi
																		.getColIndex() - 1)
														.get("WIDTH") == null ? ""
														: colList
																.get(
																		temprciexi
																				.getColIndex() - 1)
																.get("WIDTH"));// 设置单元格的宽度
										temprciiexi
												.setHeigth((String) unionRowInfoMap
														.get(unionRowi + 1)
														.get("HEIGHT"));// 设置单元格的高度
										temprciiexi.setStyle(temprciexi
												.getStyle());
										// 扩展前源坐标
										temprciiexi
												.setCellCoordinate(new ReportCellCoordinate(
														temprciexi
																.getColIndex(),
														temprciexi
																.getRowIndex()));// 设置原始坐标
										// if (expandRowIndex > 0) {
										// 设置扩展坐标
										temprciiexi
												.setExpandCellCoordinate(new ReportCellCoordinate(
														xei, expandRowIndex));
										// }
										// 设置当前单元格的原始值
										temprciiexi.setValue(temprciexi
												.getValue());
										// 得到原始的点击事件
										temprciiexi.setClickEvent(temprciexi
												.getClickEvent());
										// 使用数据集与主格相同时
										if (mainCellDataSetName
												.equals(getDataSetName(temprciexi
														.getValue()))) {
											// 计算当前单元格的实际值，并以当前行数据映射进行替换
											temprciiexi
													.setValue(countCellValue(
															temprciiexi,
															dataSetRowValue,
															CoutType.CELLVALUE));
											temprciiexi
													.setClickEvent(countCellValue(
															temprciiexi,
															dataSetRowValue,
															CoutType.CLICKVALUE));
										} else {
											// 计算当前单元格的实际值
											temprciiexi
													.setValue((String) countCellValue(
															temprciiexi,
															CoutType.CELLVALUE));
											temprciiexi
													.setClickEvent((String) countCellValue(
															temprciiexi,
															CoutType.CLICKVALUE));
										}

										List<ReportCellInstanceInfo> rciiExpanList = allCellsMap
												.get(temprciexi.getRowIndex())
												.get(
														temprciexi
																.getColIndex() - 1)
												.getRciiExpandList();
										// 如果扩展单元格列表为空则新创建一个扩展单元格列表
										if (rciiExpanList == null) {
											rciiExpanList = new ArrayList();
											allCellsMap
													.get(
															temprciexi
																	.getRowIndex())
													.get(
															temprciexi
																	.getColIndex() - 1)
													.setRciiExpandList(
															rciiExpanList);
										}
										// 在原始单元格的扩展单元格列表中添加当前单元格
										rciiExpanList.add(temprciiexi);
										allCellsMap
												.get(
														Integer
																.valueOf((String) unionRowInfoMap
																		.get(
																				unionRowi + 1)
																		.get(
																				"ROWINDEX")))
												.add(temprciiexi);
										// 为所有报表单元格当前行添加当前单元格
										allInstanceCellsMap.get(rowIndex).add(
												temprciiexi);
									}
								}
							}
						}
						expandRowIndex++;// 扩展行++
						rowIndex++;// 所有行++
					}
				}
				rowi = rowi + mainCell.getRowspan() - 1;// 添加合并单元格的行数
			} else if (mainCell != null
					&& mainCell.getExpandDirection().equals("2")) {// 是扩展主格并且向右扩展时执行如下操作
				Map<Integer, Map<Integer, ReportCellInfo>> unionRowMap = new HashMap<Integer, Map<Integer, ReportCellInfo>>();
				unionRowMap.put(1, tempRowMap);
				Map<Integer, Map<?, ?>> unionRowInfoMap = new HashMap<Integer, Map<?, ?>>();
				unionRowInfoMap.put(1, rowInfo);
				ReportCellInstanceInfo mainrcii = new ReportCellInstanceInfo();
				mainrcii.setValue(mainCell.getValue());
				mainrcii.setStyle(mainCell.getStyle());
				List<Map> resultList = (List<Map>) countCellValue(mainrcii,
						CoutType.CELLVALUE);
				allInstanceCellsMap.put(rowIndex, new ArrayList());
				// 当前行内所有数据
				for (int tempCellIndex : tempRowMap.keySet()) {

					ReportCellInfo temprci = tempRowMap.get(tempCellIndex);
					int expandCowIndex = 0;
					// 主单元格和当前单元格的列号相同时
					if (mainCell.getColIndex() == temprci.getColIndex()) {
						ReportCellInstanceInfo maintemprcii = null;
						for (Map dataSetColValue : resultList) {
							ReportCellInstanceInfo temprcii = new ReportCellInstanceInfo();
							if (maintemprcii == null) {
								maintemprcii = temprcii;
								maintemprcii
										.setRciiExpandList(new ArrayList<ReportCellInstanceInfo>());
							} else {
								maintemprcii.getRciiExpandList().add(temprcii);
							}
							temprcii.setRowspan(temprci.getRowspan());// 复制行数
							temprcii.setColspan(temprci.getColspan());// 复制列数
							temprcii.setExpandDirection(temprci
									.getExpandDirection());// 扩展方向
							temprcii.setStyle(temprci.getStyle());
							temprcii.setWidth(colList.get(tempCellIndex - 1)
									.get("WIDTH") == null ? "" : colList.get(
									tempCellIndex - 1).get("WIDTH"));
							temprcii.setHeigth((String) rowInfo.get("HEIGHT"));
							// 扩展前源坐标
							temprcii
									.setCellCoordinate(new ReportCellCoordinate(
											temprci.getColIndex(), temprci
													.getRowIndex()));
							if (expandCowIndex > 0) {
								// 扩展坐标
								temprcii
										.setExpandCellCoordinate(new ReportCellCoordinate(
												expandCowIndex, 0));
							}
							temprcii.setValue(temprci.getValue());
							temprcii.setClickEvent(temprci.getClickEvent());
							temprcii.setValue(countCellValue(temprcii,
									dataSetColValue, CoutType.CELLVALUE));
							temprcii.setClickEvent(countCellValue(temprcii,
									dataSetColValue, CoutType.CLICKVALUE));
							// temprcii.setDataColSet(dataSetColValue);
							allInstanceCellsMap.get(rowIndex).add(temprcii);
							allCellsMap.get(temprci.getRowIndex())
									.add(temprcii);
							temprcii.setDataColSet(dataSetColValue);
							expandCowIndex++;
						}

					} else {
						ReportCellInstanceInfo temprcii = new ReportCellInstanceInfo();
						temprcii.setValue(temprci.getValue());// 设置原始值
						temprcii.setClickEvent(temprci.getClickEvent());// 设置原始点击事件
						temprcii.setCellCoordinate(new ReportCellCoordinate(
								temprci.getColIndex(), temprci.getRowIndex()));
						temprcii.setValue(String.valueOf(countCellValue(
								temprcii, CoutType.CELLVALUE)));// 设置显示值
						temprcii.setClickEvent(String.valueOf(countCellValue(
								temprcii, CoutType.CLICKVALUE)));// 设置点击事件
						temprcii.setRowspan(temprci.getRowspan());// 复制行数
						temprcii.setColspan(temprci.getColspan());// 复制列数
						temprcii.setExpandDirection(temprci
								.getExpandDirection());// 扩展方向
						temprcii.setStyle(temprci.getStyle());
						temprcii.setWidth(colList.get(tempCellIndex - 1).get(
								"WIDTH") == null ? "" : colList.get(
								tempCellIndex - 1).get("WIDTH"));
						temprcii.setHeigth((String) rowInfo.get("HEIGHT"));
						allInstanceCellsMap.get(rowIndex).add(temprcii);
						allCellsMap.get(temprci.getRowIndex()).add(temprcii);
					}

				}
				rowIndex++;

			} else {// 行没有扩展单元格时只加载当前数据
				allInstanceCellsMap.put(rowIndex, new ArrayList());
				for (int tempCellIndex : tempRowMap.keySet()) {
					ReportCellInfo temprci = tempRowMap.get(tempCellIndex);
					ReportCellInstanceInfo temprcii = new ReportCellInstanceInfo();
					temprcii.setValue(temprci.getValue());
					temprcii.setCellCoordinate(new ReportCellCoordinate(temprci
							.getColIndex(), temprci.getRowIndex()));
					temprcii.setValue(String.valueOf(countCellValue(temprcii,
							CoutType.CELLVALUE)));// 设置显示值
					temprcii.setClickEvent(String.valueOf(countCellValue(
							temprcii, CoutType.CLICKVALUE)));// 设置单击事件
					temprcii.setDataType(temprci.getDataType());// 数据类型
					temprcii.setFormat(temprci.getFormat());// 设置显示格式
					// 如果是数字的时候进行数据格式化
					if (temprci.getDataType() != null
							&& temprci.getDataType().equals("数字")
							&& temprci.getFormat() != null) {
						String value = temprcii.getValue();
						value = Format.doFormat(Format.type.t_number, value,
								temprci.getFormat());
						temprcii.setValue(value);
					}
					temprcii.setRowspan(temprci.getRowspan());// 复制行数
					temprcii.setColspan(temprci.getColspan());// 复制列数
					temprcii.setExpandDirection(temprci.getExpandDirection());// 扩展方向
					// 设置宽度
					temprcii.setWidth(colList.get(tempCellIndex - 1).get(
							"WIDTH") == null ? "" : colList.get(
							tempCellIndex - 1).get("WIDTH"));
					// 设置高度
					temprcii.setHeigth((String) rowInfo.get("HEIGHT"));
					// 设置样式表
					temprcii.setStyle(temprci.getStyle());
					temprcii
							.setRciiExpandList(new ArrayList<ReportCellInstanceInfo>());
					allInstanceCellsMap.get(rowIndex).add(temprcii);
					allCellsMap.get(temprci.getRowIndex()).add(temprcii);
					/**
					 * 如果有上主格的时候按上主格的扩展自动扩展
					 */
					if (temprci.getTopMain() != null
							&& temprci.getTopMain().length() > 0) {
						ReportCellCoordinate rcc = str2rcc(temprci.getTopMain());
						ReportCellInstanceInfo sourcercii = getSourceCellByCoordinate(rcc);
						for (ReportCellInstanceInfo expandtcii : sourcercii
								.getRciiExpandList()) {
							if (expandtcii.getExpandCellCoordinate().getY() == 0) {
								ReportCellInstanceInfo temp = new ReportCellInstanceInfo();
								try {
									BeanUtils.copyProperties(temp, temprcii);
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									e.printStackTrace();
								}
								temp
										.setExpandCellCoordinate(new ReportCellCoordinate(
												expandtcii
														.getExpandCellCoordinate()
														.getX(), 0));
								String value = "";
								if (temprci.getValue().matches(
										"sum\\(\\w*\\d*\\{\\}\\)")) {
									temp.setValue(temprci.getValue());
									value = String.valueOf(countCellValue(temp,
											CoutType.CELLVALUE));
								} else {
									value = (String) expandtcii.getDataColSet()
											.get(
													temprci.getValue().split(
															"\\.")[1]
															.toUpperCase()
															.trim());
								}
								if (temprci.getDataType() != null
										&& temprci.getDataType().equals("数字")) {
									value = Format.doFormat(
											Format.type.t_number, value,
											temprci.getFormat());
								}
								temp.setValue(value);
								allInstanceCellsMap.get(rowIndex).add(temp);
								allCellsMap.get(temprci.getRowIndex())
										.add(temp);
								temprcii.getRciiExpandList().add(temp);
							}
						}
						String value = "";
						if (temprci.getValue().matches(
								"sum\\(\\w*\\d*\\{\\}\\)")) {
							value = String.valueOf(countCellValue(temprcii,
									CoutType.CELLVALUE));
						} else {
							value = (String) sourcercii.getDataColSet().get(
									temprci.getValue().split("\\.")[1]
											.toUpperCase().trim());
						}
						if (temprci.getDataType() != null
								&& temprci.getDataType().equals("数字")) {
							value = Format.doFormat(Format.type.t_number,
									value, temprci.getFormat());
						}
						temprcii.setValue(value);
					}
				}
				rowIndex++;
			}
		}
		for (int i = 1; i <= allInstanceCellsMap.size(); i++) {// 填充数据
			int rowi = 0;
			Element trElement = tableElement.addElement("tr");
			List<ReportCellInstanceInfo> resultRowList = allInstanceCellsMap
					.get(i);
			/**
			 * 添加行内所有单元格
			 */
			for (int j = 0; j < resultRowList.size(); j++) {
				if (j == 0) {// 使用未扩展前的坐标
					rowi = resultRowList.get(j).getCellCoordinate().getY();
				}
				ReportCellInstanceInfo cellinstance = resultRowList.get(j);
				Element tdElement = trElement.addElement("td");
				tdElement.setText(cellinstance.getValue() == null ? ""
						: cellinstance.getValue());
				tdElement.addAttribute("title", String.valueOf(cellinstance
						.getValue() == null ? "" : cellinstance.getValue()));
				tdElement.addAttribute("rowspan", String.valueOf(cellinstance
						.getRowspan()));
				tdElement.addAttribute("colspan", String.valueOf(cellinstance
						.getColspan()));
				tdElement.addAttribute("width", String.valueOf(cellinstance
						.getWidth()));
				// 添加样式
				if (cellinstance.getStyle() != null
						&& cellinstance.getStyle().length() > 0&&!cellinstance.getValue().equals("0")) {
					tdElement.addAttribute("style", String.valueOf(cellinstance
							.getStyle()));
				}
				// 添加单击事件
				if (cellinstance.getClickEvent() != null
						&& cellinstance.getClickEvent().length() > 0&&!cellinstance.getValue().equals("0")) {
					tdElement.addAttribute("onclick", cellinstance
							.getClickEvent());
				}
				if (cellinstance.getRciiExpandList() != null) {
					// tdElement.addAttribute("xep", String.valueOf(cellinstance
					// .getExpandCellCoordinate().getX()));
					// tdElement.addAttribute("yep", String.valueOf(cellinstance
					// .getExpandCellCoordinate().getY()));
				}
				if (rriMap.get(rowi).getHeadRow() == 1) {
					Map<String, String> colInfoMap = colList.get(cellinstance
							.getCellCoordinate().getX() - 1);
					tdElement.addAttribute("width",
							colInfoMap.get("WIDTH") == null ? "" : colInfoMap
									.get("WIDTH"));
				}
				/**
				 * 判断何时隐藏
				 */
				if (cellinstance.getColspan() == 0
						|| cellinstance.getRowspan() == 0
						|| (cellinstance.getWidth() != null && cellinstance
								.getWidth().equals("0"))
						|| (cellinstance.getHeigth() != null && cellinstance
								.getHeigth().equals("0"))) {
					tdElement.addAttribute("style", "display:none");
				}

			}
			/**
			 * 添加行样式
			 */
			ReportRowInstance row = rriMap.get(rowi);
			if(row.getRowHight()>0){
				trElement.addAttribute("height",String.valueOf(row.getRowHight()));
			}
			if (row.getHeadRow() == 1) {
				headRowSetStyle(trElement, row);
			} else {
				rowSetStyle(trElement, row, i);
			}
		}
		return Dom4jUtil.formatToXML(document);
	}

	/**
	 * 
	 * 表格添加样式表
	 * 
	 * @param tableElement
	 */
	private void tableSetStyle(Element tableElement) {
		tableElement.addAttribute("border", "1");
		tableElement.addAttribute("class", "report_t");
	}

	/**
	 * 数据行添加样式表
	 * 
	 * @param trElement
	 * @param rowInfo
	 */
	private void rowSetStyle(Element trElement, ReportRowInstance rowInfo,
			int rowIndex) {
		if (rowIndex % 2 == 1) {
			trElement.addAttribute("class", "report_a1");
		} else {
			trElement.addAttribute("class", "report_a2");
		}
	}

	/**
	 * 表头添加样式表
	 * 
	 * @param trElement
	 * @param rowInfo
	 */
	private void headRowSetStyle(Element trElement, ReportRowInstance rowInfo) {
		trElement.addAttribute("class", "report_th");
	}

	private String createSqlByCellValue(String cellValue) {
		StringBuffer sql = new StringBuffer();
		return sql.toString();
	}

	/**
	 * 计算单元格的值
	 * 
	 * @param cellValue
	 * @return
	 */
	private Object countCellValue(ReportCellInstanceInfo cellinstance, int type) {
		if (cellinstance.getValue() == null) {
			return "";
		}
		String oldString = cellinstance.getValue();
		if (type == CoutType.CLICKVALUE) {
			oldString = cellinstance.getClickEvent();
		}
		Pattern p = Pattern.compile("\\w*\\.select\\(.*\\)");
		Matcher m = p.matcher(oldString);
		boolean b = m.find();
		if (b) {
			String tempSub = oldString.substring(m.start(), m.end());
			ReportDataSet rds = dao.getDataSetByName(tempSub.split("\\.")[0],
					reportId);
			return rds.getDataSetResultList();
		}
		p = Pattern.compile("rowNum\\(\\)");
		m = p.matcher(oldString);
		b = m.find();
		if (b) {
			oldString = oldString.replaceAll("rowNum\\(\\)", String
					.valueOf(rowIndex));
			try {// 页号有运算符时进行表达式计算
				oldString = String.valueOf(runner.execute(oldString, null,
						false, null));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return oldString;
		}
		// 如果单元格是求合计算
		p = Pattern.compile("sum\\(\\w*\\d*\\{\\}\\)");
		m = p.matcher(oldString);
		b = m.find();
		if (b) {
			double result = 0;
			String tempSub = oldString.substring(4, oldString.length() - 3);
			// 字符串坐标转换为数字坐标
			ReportCellCoordinate temprcc = str2rcc(tempSub);
			ReportCellInstanceInfo sourceCell = getSourceCellByCoordinate(temprcc);
			// 原格的所有扩展格
			List<ReportCellInstanceInfo> expandCells = sourceCell
					.getRciiExpandList();
			// 如果为源格的时候设置初始值
			result = Double.valueOf(sourceCell.getValue()==null||sourceCell.getValue().equals("")?"0":sourceCell.getValue());
			// 如果当前格不是源格的时候设置初始值为0
			if (cellinstance.getExpandCellCoordinate() != null) {
				result = 0;
			}
			if (expandCells != null)
				for (ReportCellInstanceInfo rcii : expandCells) {
					String tempValue = rcii.getValue();
					if (tempValue == null || tempValue.equals("")) {
					} else {
						// 当前格的是扩展格
						if (cellinstance.getExpandCellCoordinate() != null
								&& rcii.getExpandCellCoordinate() != null) {

							if (sourceCell.isHaveTopMainCell()
									&& sourceCell.getExpandDirection().equals(
											"2")) {
								result += Double.valueOf(tempValue);
							} else {
								// 和当前格是同一列的扩展格
								if (cellinstance.getExpandCellCoordinate()
										.getX() == rcii
										.getExpandCellCoordinate().getX()) {
									result += Double.valueOf(tempValue);
								}
							}
						} else if (rcii.getExpandCellCoordinate() == null
								|| rcii.getExpandCellCoordinate().getX() == 0) {
							result += Double.valueOf(tempValue);
						}
						//如果源格是向右扩展的，则将所有值统计
						if (sourceCell.getExpandDirection().equals("2")) {
							result += Double.valueOf(tempValue);
						}
					}
				}
			oldString = String.valueOf(result);
			return result;
		}
		// 数据集统计数量
		p = Pattern.compile("\\w+\\.count\\(.*\\)");
		m = p.matcher(oldString);
		b = m.find();
		if (b) {
			String dataSetName = oldString.split("\\.")[0];
			ReportDataSet temptds = getDataSet(dataSetName);
			p = Pattern.compile("\\.count\\(.*\\)");
			m = p.matcher(oldString);
			b = m.find();
			if (b) {
				String condition = oldString.substring(m.start() + 7,
						m.end() - 1);
				condition = " " + condition + " ";
				p = Pattern.compile("[^-\\w]\\p{Upper}+\\d+[^-\\w]");
				m = p.matcher(condition);
				b = m.find();
				while (b) {
					String cellxy = condition.substring(m.start() + 1,
							m.end() - 1);
					ReportCellCoordinate cellrcc = str2rcc(cellxy);
					ReportCellInstanceInfo rcii = getSourceCellByCoordinate(cellrcc);
					// 当不是扩展格的时候用当前格的值替换
					if (cellinstance.getExpandCellCoordinate() == null
							|| cellinstance.getExpandCellCoordinate().getX() == -1
							|| cellinstance.getExpandCellCoordinate().getY() == -1) {
						String tempcondition = condition.substring(m.start(), m
								.end());
						condition = condition.replaceFirst(
								"[^-\\w]\\p{Upper}+\\d+[^-\\w]", tempcondition
										.substring(0, 1)
										+ rcii.getValue()
										+ tempcondition.substring(tempcondition
												.length() - 1));
					} else {// 当有扩展格的时候用扩展格的值进行替换
						boolean flag = true;
						ReportCellInstanceInfo ecpanercii = null;
						if (rcii.getExpandDirection().equals("2")) {
							if (cellinstance.getExpandCellCoordinate().getX() > 0) {
								ecpanercii = rcii.getRciiExpandList().get(
										cellinstance.getExpandCellCoordinate()
												.getX() - 1);
							} else {
								ecpanercii = rcii;
							}
						} else {
							if (cellinstance.getExpandCellCoordinate().getY() == 0) {
								String tempcondition = condition.substring(m
										.start(), m.end());
								condition = condition
										.replaceFirst(
												"[^-\\w]\\p{Upper}+\\d+[^-\\w]",
												tempcondition.substring(0, 1)
														+ rcii.getValue()
														+ tempcondition
																.substring(tempcondition
																		.length() - 1));
								flag = false;
							} else {
								if (rcii.getCellCoordinate().getY() == cellinstance
										.getCellCoordinate().getY()) {
									ecpanercii = rcii.getRciiExpandList().get(
											cellinstance
													.getExpandCellCoordinate()
													.getY() - 1);
								} else {
									ecpanercii = rcii;
								}
							}
						}
						if (flag) {
							String tempcondition = condition.substring(m
									.start(), m.end());
							condition = condition.replaceFirst(
									"[^-\\w]\\p{Upper}+\\d+[^-\\w]",
									tempcondition.substring(0, 1)
											+ ecpanercii.getValue()
											+ tempcondition
													.substring(tempcondition
															.length() - 1));
						}
					}
					p = Pattern.compile("[^-\\w]\\p{Upper}+\\d+[^-\\w]");
					m = p.matcher(condition);
					b = m.find();
				}
				return String.valueOf(temptds.count(condition));
			}

		}
		// 数据集统计数量
		p = Pattern.compile("\\w+\\.sum\\(\\w+,.+\\)");
		m = p.matcher(oldString);
		b = m.find();
		if (b) {
			String dataSetName = oldString.split("\\.")[0];
			ReportDataSet temptds = getDataSet(dataSetName);
			p = Pattern.compile("\\.sum\\(\\w+,.+\\)");
			m = p.matcher(oldString);
			b = m.find();
			if (b) {
				String tempstr = oldString.substring(m.start(), m.end() - 1);
				String column = tempstr.split(",")[0].substring(5);
				String condition = tempstr.split(",")[1];
				condition = " " + condition + " ";
				p = Pattern.compile("[^-\\w]\\p{Upper}+\\d+[^-\\w]");
				m = p.matcher(condition);
				b = m.find();
				while (b) {
					String cellxy = condition.substring(m.start() + 1,
							m.end() - 1);
					ReportCellCoordinate cellrcc = str2rcc(cellxy);
					ReportCellInstanceInfo rcii = getSourceCellByCoordinate(cellrcc);
					// 当不是扩展格的时候用当前格的值替换
					if (cellinstance.getExpandCellCoordinate() == null
							|| cellinstance.getExpandCellCoordinate().getX() == -1
							|| cellinstance.getExpandCellCoordinate().getY() == -1) {
						String tempcondition = condition.substring(m.start(), m
								.end());
						condition = condition.replaceFirst(
								"[^-\\w]\\p{Upper}+\\d+[^-\\w]", tempcondition
										.substring(0, 1)
										+ rcii.getValue()
										+ tempcondition.substring(tempcondition
												.length() - 1));
					} else {// 当有扩展格的时候用扩展格的值进行替换
						boolean flag = true;// 是否为扩展前原行
						ReportCellInstanceInfo ecpanercii = null;
						if (rcii.getExpandDirection().equals("2")) {
							if (cellinstance.getExpandCellCoordinate().getX() > 0) {
								ecpanercii = rcii.getRciiExpandList().get(
										cellinstance.getExpandCellCoordinate()
												.getX() - 1);
							} else {
								ecpanercii = rcii;
							}
						} else {
							if (cellinstance.getExpandCellCoordinate().getY() == 0) {
								String tempcondition = condition.substring(m
										.start(), m.end());
								condition = condition
										.replaceFirst(
												"[^-\\w]\\p{Upper}+\\d+[^-\\w]",
												tempcondition.substring(0, 1)
														+ rcii.getValue()
														+ tempcondition
																.substring(tempcondition
																		.length() - 1));
								flag = false;
							} else {
								if (rcii.getCellCoordinate().getY() == cellinstance
										.getCellCoordinate().getY()) {
									// 如果把扩展前的一行也记入了测去除一行
									if (rcii.getRciiExpandList().get(0)
											.getExpandCellCoordinate() == null) {
										ecpanercii = rcii
												.getRciiExpandList()
												.get(
														cellinstance
																.getExpandCellCoordinate()
																.getY());
									} else {
										ecpanercii = rcii
												.getRciiExpandList()
												.get(
														cellinstance
																.getExpandCellCoordinate()
																.getY() - 1);
									}
								} else {
									ecpanercii = rcii;
								}
							}
						}
						if (flag) {
							String tempcondition = condition.substring(m
									.start(), m.end());
							condition = condition.replaceFirst(
									"[^-\\w]\\p{Upper}+\\d+[^-\\w]",
									tempcondition.substring(0, 1)
											+ ecpanercii.getValue()
											+ tempcondition
													.substring(tempcondition
															.length() - 1));
						}
					}
					p = Pattern.compile("[^-\\w]\\p{Upper}+\\d+[^-\\w]");
					m = p.matcher(condition);
					b = m.find();
				}
				return String.valueOf(temptds.sum(column, condition));
			}

		}
		// 替换引用了其它单元的值
		p = Pattern.compile("[^-\\w]\\p{Upper}+\\d+[^-\\w]");
		m = p.matcher(oldString);
		b = m.find();
		while (b) {
			String cellxy = oldString.substring(m.start() + 1, m.end() - 1);
			ReportCellCoordinate cellrcc = str2rcc(cellxy);
			ReportCellInstanceInfo rcii = null;
			int expendLength = 0;
			if (cellrcc.getY() == cellinstance.getCellCoordinate().getY()
					&& (cellinstance.getExpandCellCoordinate() == null || cellinstance
							.getExpandCellCoordinate().getY() == 0)) {
				for (int x = 1; x < cellrcc.getX(); x++) {
					ReportCellInstanceInfo temp = getSourceCellByCoordinate(new ReportCellCoordinate(
							x, cellrcc.getY()));
					if (temp.getRciiExpandList() != null) {
						for (ReportCellInstanceInfo rciitemp : temp
								.getRciiExpandList()) {
							if (rciitemp.getCellCoordinate().getX() == x
									&& rciitemp.getCellCoordinate().getY() == cellrcc
											.getY() && temp.isHaveTopMainCell()) {
								++expendLength;
							}
						}
					}
				}
				rcii = getSourceCellByCoordinate(new ReportCellCoordinate(
						cellrcc.getX() + expendLength, cellrcc.getY()));
			} else {
				rcii = getSourceCellByCoordinate(cellrcc);
			}
			// 当不是扩展格的时候用当前格的值替换
			if (cellinstance.getExpandCellCoordinate() == null
					|| cellinstance.getExpandCellCoordinate().getX() == -1
					|| cellinstance.getExpandCellCoordinate().getY() == -1) {
				String tempcondition = oldString.substring(m.start(), m.end());
				oldString = oldString.replaceFirst(
						"[^-\\w]\\p{Upper}+\\d+[^-\\w]", tempcondition
								.substring(0, 1)
								+ rcii.getValue()
								+ tempcondition.substring(tempcondition
										.length() - 1));
			} else {// 当有扩展格的时候用扩展格的值进行替换
				boolean flag = true;// 是否为扩展前原行
				ReportCellInstanceInfo ecpanercii = null;
				if (rcii.getExpandDirection().equals("2")) {
					if (cellinstance.getExpandCellCoordinate().getX() > 0) {
						ecpanercii = rcii.getRciiExpandList()
								.get(
										cellinstance.getExpandCellCoordinate()
												.getX() - 1);
					} else {
						ecpanercii = rcii;
					}
				} else {
					if (cellinstance.getExpandCellCoordinate().getY() == 0) {
						String tempcondition = oldString.substring(m.start(), m
								.end());
						oldString = oldString.replaceFirst(
								"[^-\\w]\\p{Upper}+\\d+[^-\\w]", tempcondition
										.substring(0, 1)
										+ rcii.getValue()
										+ tempcondition.substring(tempcondition
												.length() - 1));
						flag = false;
					} else {
						if (rcii.getCellCoordinate().getY() == cellinstance
								.getCellCoordinate().getY()) {
							// 如果把扩展前的一行也记入了测去除一行
							if (rcii.getRciiExpandList().get(0)
									.getExpandCellCoordinate() == null) {
								ecpanercii = rcii.getRciiExpandList().get(
										cellinstance.getExpandCellCoordinate()
												.getY());
							} else {
								ecpanercii = rcii.getRciiExpandList().get(
										cellinstance.getExpandCellCoordinate()
												.getY() - 1);
							}
						} else {
							ecpanercii = rcii;
						}
					}
				}
				if (flag) {
					String tempcondition = oldString.substring(m.start(), m
							.end());
					oldString = oldString.replaceFirst(
							"[^-\\w]\\p{Upper}+\\d+[^-\\w]", tempcondition
									.substring(0, 1)
									+ ecpanercii.getValue()
									+ tempcondition.substring(tempcondition
											.length() - 1));
				}
			}
			p = Pattern.compile("[^-\\w]\\p{Upper}+\\d+[^-\\w]");
			m = p.matcher(oldString);
			b = m.find();
		}
		return oldString;
	}

	/**
	 * 坐标转换 如果不存在返回(-1,-1)
	 * 
	 * @param strCoordinate
	 * @return
	 */
	private ReportCellCoordinate str2rcc(String strCoordinate) {
		String tempSub = strCoordinate;
		Pattern p = Pattern.compile("\\p{Upper}+");
		Matcher m = p.matcher(tempSub);
		boolean b = m.find();
		String colstr = null;
		if (b) {
			colstr = tempSub.substring(m.start(), m.end());
		} else {
			colstr = "-1";
		}
		p = Pattern.compile("\\d+");
		m = p.matcher(tempSub);
		b = m.find();
		String rowstr = null;
		if (b) {
			rowstr = tempSub.substring(m.start(), m.end());
		} else {
			colstr = "-1";
		}
		if (colstr.length() == 1) {
			colstr = String.valueOf(colstr.toCharArray()[0] - 64);
		}
		return new ReportCellCoordinate(Integer.valueOf(colstr), Integer
				.valueOf(rowstr));
	}

	/**
	 * 通过元件坐标得到元件
	 * 
	 * @param rcc
	 * @return
	 */
	ReportCellInstanceInfo getSourceCellByCoordinate(ReportCellCoordinate rcc) {
		List<ReportCellInstanceInfo> rowcii = allCellsMap.get(rcc.getY());
		ReportCellInstanceInfo sourceCell = new ReportCellInstanceInfo();
		if(rowcii!=null&&rowcii.size()>0){
			sourceCell = rowcii.get(rcc.getX() - 1);// List索引从0开始
		}
		return sourceCell;
	}

	/**
	 * 通过元件坐标字符串得到元件
	 * 
	 * @param strrcc
	 * @return
	 */
	ReportCellInstanceInfo getSourceCellByString(String strrcc) {
		ReportCellCoordinate rcc = str2rcc(strrcc);
		List<ReportCellInstanceInfo> rowcii = allCellsMap.get(rcc.getY());
		ReportCellInstanceInfo sourceCell = rowcii.get(rcc.getX() - 1);// List索引从0开始
		return sourceCell;
	}

	/**
	 * 扩展单元格计算单元格的值
	 * 
	 * @param cellValue
	 * @param valueMap
	 * @return
	 */
	private String countCellValue(ReportCellInstanceInfo cellinstance,
			Map valueMap, int type) {
		String oldString = cellinstance.getValue();
		if (type == CoutType.CLICKVALUE) {
			oldString = cellinstance.getClickEvent();
		}
		// 数据集主格数据显示
		Pattern p = Pattern.compile("\\w+\\.select\\(.+\\)");
		Matcher m = p.matcher(oldString);
		boolean b = m.find();
		if (b) {
			String tempSub = oldString.substring(m.start(), m.end());
			p = Pattern.compile("\\(\\w+");
			m = p.matcher(tempSub);
			b = m.find();
			if (b) {
				String fieldName = tempSub.substring(m.start() + 1, m.end());
				String result = (String) valueMap.get(fieldName.toUpperCase()) == null ? ""
						: (String) valueMap.get(fieldName.toUpperCase());
				oldString = oldString.replaceAll("\\w+\\.select\\(.+\\)",
						result);
				return result;
			}
		}

		// 数据集统计数量
		p = Pattern.compile("\\w+\\.count\\(.*\\)");
		m = p.matcher(oldString);
		b = m.find();
		if (b) {
			String dataSetName = oldString.split("\\.")[0];
			ReportDataSet temptds = getDataSet(dataSetName);
			p = Pattern.compile("\\.count\\(.*\\)");
			m = p.matcher(oldString);
			b = m.find();
			if (b) {
				String condition = oldString.substring(m.start() + 7,
						m.end() - 1);
				condition = " " + condition + " ";
				p = Pattern.compile("\\W\\p{Upper}+\\d+\\W");
				m = p.matcher(condition);
				b = m.find();
				while (b) {
					String cellxy = condition.substring(m.start() + 1,
							m.end() - 1);
					ReportCellCoordinate cellrcc = str2rcc(cellxy);
					ReportCellInstanceInfo rcii = getSourceCellByCoordinate(cellrcc);
					// 当不是扩展格的时候用当前格的值替换
					if (cellinstance.getExpandCellCoordinate() == null
							|| cellinstance.getExpandCellCoordinate().getX() == -1
							|| cellinstance.getExpandCellCoordinate().getY() == -1) {
						String tempcondition = condition.substring(m.start(), m
								.end());
						condition = condition.replaceFirst(
								"\\W\\p{Upper}+\\d+\\W", tempcondition
										.substring(0, 1)
										+ rcii.getValue()
										+ tempcondition.substring(tempcondition
												.length() - 1));
					} else {// 当有扩展格的时候用扩展格的值进行替换
						ReportCellInstanceInfo ecpanercii = null;
						if (rcii.getExpandDirection().equals("2")) {
							ecpanercii = rcii.getRciiExpandList().get(
									cellinstance.getExpandCellCoordinate()
											.getX() - 1);
						} else {
							ecpanercii = rcii.getRciiExpandList().get(
									cellinstance.getExpandCellCoordinate()
											.getY() - 1);
						}
						String tempcondition = condition.substring(m.start(), m
								.end());
						condition = condition.replaceFirst(
								"\\W\\p{Upper}+\\d+\\W", tempcondition
										.substring(0, 1)
										+ ecpanercii.getValue()
										+ tempcondition.substring(tempcondition
												.length() - 1));
					}
					p = Pattern.compile("\\W\\p{Upper}+\\d+\\W");
					m = p.matcher(condition);
					b = m.find();
				}
				return String.valueOf(temptds.count(condition));
			}

		}
		p = Pattern.compile("\\w+\\.sum\\(.*\\)");
		m = p.matcher(oldString);
		b = m.find();
		if (b) {
			return countCellValue(cellinstance, type).toString();
		}
		// 数据集数据直接显示
		p = Pattern.compile("\\w+\\.\\w+");
		m = p.matcher(oldString);
		b = m.find();
		if (b) {
			String tempSub = oldString.substring(m.start() + 1, m.end());
			String result = (String) valueMap.get(tempSub.split("\\.")[1]
					.toUpperCase()) == null ? "" : (String) valueMap
					.get(tempSub.split("\\.")[1].toUpperCase());
			oldString = oldString.replaceAll("\\w+\\.\\w+", result);
			if (type == CoutType.CELLVALUE) {
				cellinstance.setValue(oldString);
			}
			if (type == CoutType.CLICKVALUE) {
				cellinstance.setClickEvent(oldString);
			}
			// 有多个替换值存在的时候进行递归操作
			oldString = countCellValue(cellinstance, valueMap,
					CoutType.CELLVALUE);
		}
		return oldString;
	}

	/**
	 * 得到单元格使用的数据源的名称
	 * 
	 * @param cellValue
	 * @return
	 */
	private String getDataSetName(String cellValue) {
		if (cellValue == null) {
			return null;
		}
		String oldString = cellValue;
		Pattern p = Pattern.compile("\\w+\\..+");
		Matcher m = p.matcher(oldString);
		boolean b = m.find();
		if (b) {
			String tempSub = oldString.substring(m.start(), m.end());
			return tempSub.split("\\.")[0];
		}
		return null;
	}

	/**
	 * 得到数据集
	 * 
	 * @param deatSetName
	 * @return
	 */
	private ReportDataSet getDataSet(String deatSetName) {
		if (dataSetMap.get(deatSetName) != null) {
			return dataSetMap.get(deatSetName);
		} else {
			ReportDataSet tempDataSet = dao.getDataSetByName(deatSetName,
					reportId);
			dataSetMap.put(deatSetName, tempDataSet);
			return tempDataSet;
		}
	}

	/**
	 * 单元格数据格式化
	 * 
	 * @param value
	 * @param rci
	 * @return
	 */
	private String cellValueFormat(String value, ReportCellInfo rci) {
		return value;
	}

	public static String getHtmlStr(String patternId, int cellId) {
		Form form;
		try {
			form = Form.getPattern(patternId);
			ReportCell reportCell = (ReportCell) form.getCells()[cellId];
			return reportCell.createHtml(reportCell.getReportId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "没有符合条件的记录！";
	}

	public static Map<Integer, List<ReportCellInstanceInfo>> toExcel(
			String patternId, int cellId) {
		Form form;
		try {
			form = Form.getPattern(patternId);
			ReportCell reportCell = (ReportCell) form.getCells()[cellId];
			reportCell.createHtml(reportCell.getReportId());
			return reportCell.allInstanceCellsMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
}
