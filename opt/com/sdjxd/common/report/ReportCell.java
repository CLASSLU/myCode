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
	private Map<Integer, List<ReportCellInstanceInfo>> allCellsMap = new TreeMap<Integer, List<ReportCellInstanceInfo>>();// ���������˵�Ԫ��ʵ����ϢMAP��ֻ���δ��չ����
	private Map<Integer, List<ReportCellInstanceInfo>> allInstanceCellsMap = new TreeMap<Integer, List<ReportCellInstanceInfo>>();// ���������е�Ԫ��ʵ����Ϣ
	private Map<Integer, ReportRowInstance> rriMap = new HashMap<Integer, ReportRowInstance>();
	private int rowIndex = 1;// ����ǰ���к�
	private Map<String, ReportDataSet> dataSetMap = new HashMap<String, ReportDataSet>();
	ExpressRunner runner = new ExpressRunner();
	private boolean isLoadData = true;

	// �����������
	class CoutType {
		public static final int CELLVALUE = 1;// ����Ԫ��ֵ
		public static final int CLICKVALUE = 2;// ���㵥���¼�ֵ
	};

	// ���캯��
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

	// �ڼ���ҳ����Ϣ��ʱ����ã�������ʾhtml����
	public void render(FormInstance form) throws IOException {
		// ��õ�ǰҳ����ı��������
		PrintWriter html = form.getRenderHtml();
		// ����һ��Path���������洢flash��·��
		String Path = Global.getName() + this.label;
		// ��·���еġ�\���滻�ɡ�/�����Ա��ʽ����html�����е���Ҫ
		Path = StringTool.replace(Path, "\\", "/");
		// ������ɵ�html���
		JSONArray jsonArray = JSONArray.fromObject(this.data.assitInfo);
		String reportId = jsonArray.getString(0);
		html.write("<div id='" + getCellId()
				+ "' style=\"position:absolute;top:" + this.top + ";left:"
				+ this.left + "\">");
		if (isLoadData) {
			html.write(createHtml(reportId));
		}
		html.write("</div>");
		// ����renderScriptObject���Ա����ɳ�ʼ��flash�Ľű���䣬����formΪ������
		renderScriptObject(form);
	}

	protected void renderScriptObject(FormInstance form) {
		StringBuffer sb = new StringBuffer();
		// ���ɴ�������Ԫ���Ľű����
		sb.append("defaultForm.addChild( ");
		// {}��Ϊ��������js�ű���ʼ��ʱ���Զ���Щ������������
		sb.append("new com.sdjxd.common.userDefinedCell.ReportCell({id:");
		sb.append(this.id);
		sb.append(",needSave:false");
		sb.append(",divId:").append(this.data.areaId);
		sb.append(",tagId:'");
		sb.append(this.cellId);
		sb.append("'}));\r\n");
		// ���½���flash������ӵ���ǰ����ȥ
		form.addScript("form_obj_cell_" + this.id, sb.toString());
	}

	/**
	 * ����html����
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
		// ���е�Ԫ����Ϣ��sheetidΪ��ֵ
		Map<String, ReportCellInfo> cellMap = new HashMap<String, ReportCellInfo>();
		// ���е�Ԫ����Ϣ��һ�����е�sheetidΪ�����ڶ������е�sheetidΪ��
		Map<String, HashMap<String, ReportCellInfo>> colMap = new HashMap<String, HashMap<String, ReportCellInfo>>();
		// ���е�Ԫ����Ϣ��һ��Ϊ�е�sheetidΪ��,�ڶ������е�sheetidΪ����
		Map<String, HashMap<String, ReportCellInfo>> rowMap = new HashMap<String, HashMap<String, ReportCellInfo>>();
		List<ReportCellInfo> rciList = dao.getReportCellInfoList(reportId);
		for (ReportCellInfo rci : rciList) {
			// ���е�Ԫ��MAP��ʼ��
			cellMap.put(rci.getSheetId(), rci);
			// Ϊ����Ϊ������MAP��ʼ������һ���ļ�Ϊ��sheetid,�ڶ����ļ�Ϊ��sheetid.
			if (colMap.get(rci.getColSheetId()) == null) {
				colMap.put(rci.getColSheetId(),
						new HashMap<String, ReportCellInfo>());
			}
			colMap.get(rci.getColSheetId()).put(rci.getRowSheetId(), rci);
			// Ϊ����Ϊ��������MSP��ʼ������һ�εļ�Ϊ��sheetid,�ڶ����ļ�Ϊ��sheetid.
			if (rowMap.get(rci.getRowSheetId()) == null) {
				rowMap.put(rci.getRowSheetId(),
						new HashMap<String, ReportCellInfo>());
			}
			rowMap.get(rci.getRowSheetId()).put(rci.getColSheetId(), rci);
		}
		// �õ��е���Ϣ���б���ʽ�����б���Ϊÿһ�еľ�����Ϣ
		List<Map<String, String>> rowList = (List<Map<String, String>>) ReportCellDefined
				.getRow(reportId);
		// �õ��е���Ϣ���б���ʽ�����б���Ϊÿһ�еľ�����Ϣ
		List<Map<String, String>> colList = (List<Map<String, String>>) ReportCellDefined
				.getCol(reportId);
		// �ȱ���ÿһ����������Ϊ��λ�����������Ե�Ԫ�������չ
		for (int rowi = 0; rowi < rowList.size(); rowi++) {
			Map rowInfo = rowList.get(rowi);
			// ��չǰ����Ϣ
			ReportRowInstance rri = new ReportRowInstance();
			rri.setRowIndex(rowi);
			rri.setRowStyle((String) rowInfo.get("YSB"));
			rri.setRowHight(Integer
					.valueOf((rowInfo.get("HEIGHT") == null ? "0"
							: (String) rowInfo.get("HEIGHT"))));
			rri.setHeadRow(Integer.valueOf((rowInfo.get("SFBT") == null ? "0"
					: (String) rowInfo.get("SFBT"))));
			rriMap.put(rowi + 1, rri);// ������չǰ����Ϣ
			Map<Integer, ReportCellInfo> tempRowMap = new TreeMap();// ��ʱ������
			int cellIndex = 1;// ��Ԫ��������
			ReportCellInfo mainCell = null;// ����Ԫ��
			// ����ÿһ������
			for (Map colInfo : colList) {
				ReportCellInfo tempRci = null;
				if (rowMap.size() > 0
						&& rowMap.get(rowInfo.get("SHEETID")) != null) {
					tempRci = rowMap.get(rowInfo.get("SHEETID")).get(
							colInfo.get("SHEETID"));
				}
				// ���ָ����Ԫ�񲻴��ڴ���һ���µ�Ԫ��
				if (tempRci == null) {
					tempRci = new ReportCellInfo();
				}
				// ���õ�ǰ����
				if (tempRci.getIsMainCell() == 1) {
					mainCell = tempRci;
				}
				tempRci.setRowIndex(Integer.valueOf((String) rowInfo
						.get("ROWINDEX")));// �����к�
				tempRci.setColIndex(Integer.valueOf((String) colInfo
						.get("COLINDEX")));// �����к�
				tempRowMap.put(cellIndex++, tempRci);
			}
			allCellsMap.put(Integer.valueOf((String) rowInfo.get("ROWINDEX")),
					new ArrayList());
			// �Ե�Ԫ�������չ��ֵ����
			if (mainCell != null
					&& (mainCell.getExpandDirection() == null
							|| mainCell.getExpandDirection().length() == 0 || mainCell
							.getExpandDirection().equals("1"))) {// ����չ��Ԫ��ʱΪ��ǰ��������չ��չ����Ϊ���»���Ĭ����չ����ʱִ�����²���
				// ����Ԫ��ϲ���ռ��ÿһ�����ݵ�һ��MAPӳ��
				Map<Integer, Map<Integer, ReportCellInfo>> unionRowMap = new HashMap<Integer, Map<Integer, ReportCellInfo>>();
				unionRowMap.put(1, tempRowMap);
				// ����Ԫ��ϲ�����ռ�е�����Ϣ��һ��ӳ��
				Map<Integer, Map<?, ?>> unionRowInfoMap = new HashMap<Integer, Map<?, ?>>();
				unionRowInfoMap.put(1, rowInfo);
				// ����һ������Ԫ���ʵ��
				ReportCellInstanceInfo mainrcii = new ReportCellInstanceInfo();
				mainrcii.setValue(mainCell.getValue());
				// �����Ӧ�Ĳ�ѯ���
				List<Map> resultList = (List<Map>) countCellValue(mainrcii,
						CoutType.CELLVALUE);
				String mainCellDataSetName = getDataSetName(mainCell.getValue());
				int expandRowIndex = 0;// ����չ����
				// ���������ѯ�������������
				for (Map dataSetRowValue : resultList) {
					// ����Ϊ�ϲ���Ԫ���ʱ��
					for (int unionRowi = 0; unionRowi < mainCell.getRowspan(); unionRowi++) {
						// ��Ϊԭʼ����ĵ���չ�е�ʱ����еĲ���
						if (unionRowMap.get(unionRowi + 1) == null) {
							// ����һ����չ�е�����ӳ��
							Map<Integer, ReportCellInfo> uniontempRowMap = new TreeMap();
							// �õ���ǰ�е�ģ������Ϣ
							Map unionrowInfo = rowList.get(rowi + unionRowi);
							// ��չǰ����Ϣ
							ReportRowInstance uniontemprri = new ReportRowInstance();
							uniontemprri.setRowIndex(rowi + unionRowi);// ������
							uniontemprri.setRowStyle((String) unionrowInfo
									.get("YSB"));// ��ʽ��
							uniontemprri
									.setRowHight(Integer.valueOf((unionrowInfo
											.get("HEIGHT") == null ? "0"
											: (String) unionrowInfo
													.get("HEIGHT"))));// �и߶�
							uniontemprri
									.setHeadRow(Integer
											.valueOf((unionrowInfo.get("SFBT") == null ? "0"
													: (String) unionrowInfo
															.get("SFBT"))));// �Ƿ��ͷ
							rriMap.put(rowi + unionRowi + 1, uniontemprri);// ������չǰ����Ϣ
							allCellsMap.put(Integer
									.valueOf((String) unionrowInfo
											.get("ROWINDEX")), new ArrayList());// ���������չǰ�ĵ�Ԫ����Ϣ
							// ����ÿһ������
							for (Map colInfo : colList) {
								ReportCellInfo tempRci = null;
								// ������ڵ�ǰ��Ԫ�񣬽���ǰ��Ԫ����ҵ�
								if (rowMap.size() > 0
										&& rowMap.get(unionrowInfo
												.get("SHEETID")) != null) {
									tempRci = rowMap.get(
											unionrowInfo.get("SHEETID")).get(
											colInfo.get("SHEETID"));
								}
								// ���ָ����Ԫ�񲻴��ڴ���һ���µ�Ԫ��
								if (tempRci == null) {
									tempRci = new ReportCellInfo();
								}
								tempRci.setRowIndex(Integer
										.valueOf((String) unionrowInfo
												.get("ROWINDEX")));// ����������
								tempRci.setColIndex(Integer
										.valueOf((String) colInfo
												.get("COLINDEX")));// ����������
								uniontempRowMap.put(cellIndex++, tempRci);// ��ǰ��ӳ������ӵ�ǰ��Ԫ��
							}

							unionRowInfoMap.put(unionRowi + 1, unionrowInfo);// �ϲ�����Ϣӳ������ӵ�ǰ����Ϣ
							unionRowMap.put(unionRowi + 1, uniontempRowMap);// �ϲ��е�Ԫ����Ϣӳ������ӵ�ǰ�е�Ԫ��ӳ��
						}
						// �������е�Ԫ��ʵ������ӵ�ǰ��
						allInstanceCellsMap.put(rowIndex, new ArrayList());
						// ѭ������Ԫ��ϲ��������е�Ԫ��ӳ������
						for (int tempCellIndex : unionRowMap.get(unionRowi + 1)
								.keySet()) {
							// ���ɵ�Ԫ��ʵ��
							ReportCellInstanceInfo temprcii = new ReportCellInstanceInfo();
							// ����ģ�嵥Ԫ��
							ReportCellInfo temprci = unionRowMap.get(
									unionRowi + 1).get(tempCellIndex);
							temprcii.setRowspan(temprci.getRowspan());// ��������
							temprcii.setColspan(temprci.getColspan());// ��������
							temprcii
									.setWidth(colList.get(
											temprci.getColIndex() - 1).get(
											"WIDTH") == null ? "" : colList
											.get(temprci.getColIndex() - 1)
											.get("WIDTH"));// ���õ�Ԫ��Ŀ��
							temprcii.setHeigth((String) unionRowInfoMap.get(
									unionRowi + 1).get("HEIGHT"));// ���õ�Ԫ��ĸ߶�
							temprcii.setStyle(temprci.getStyle());
							// ��չǰԴ����
							temprcii
									.setCellCoordinate(new ReportCellCoordinate(
											temprci.getColIndex(), temprci
													.getRowIndex()));// ����ԭʼ����
							if (expandRowIndex > 0) {
								// ������չ����
								temprcii
										.setExpandCellCoordinate(new ReportCellCoordinate(
												0, expandRowIndex));
							}
							// ���õ�ǰ��Ԫ���ԭʼֵ
							temprcii.setValue(temprci.getValue());
							// �õ�ԭʼ�ĵ���¼�
							temprcii.setClickEvent(temprci.getClickEvent());
							// ʹ�����ݼ���������ͬʱ
							if (mainCellDataSetName
									.equals(getDataSetName(temprci.getValue()))) {
								// ���㵱ǰ��Ԫ���ʵ��ֵ�����Ե�ǰ������ӳ������滻
								temprcii.setValue(countCellValue(temprcii,
										dataSetRowValue, CoutType.CELLVALUE));
								temprcii.setClickEvent(countCellValue(temprcii,
										dataSetRowValue, CoutType.CLICKVALUE));
							} else {
								// ���㵱ǰ��Ԫ���ʵ��ֵ
								temprcii.setValue((String) countCellValue(
										temprcii, CoutType.CELLVALUE));
								temprcii.setClickEvent((String) countCellValue(
										temprcii, CoutType.CLICKVALUE));
							}
							temprcii.setDataType(temprci.getDataType());
							temprcii.setFormat(temprci.getFormat());
							if (temprcii.getDataType() != null
									&& temprcii.getDataType().equals("����")) {
								try {
									Object o = runner.execute(temprcii
											.getValue(), null, false, null);
									temprcii.setValue(Format.doFormat(
											Format.type.t_number, String
													.valueOf(o), temprcii
													.getFormat()));
								} catch (Exception e) {
									log.error("����ı��ʽ��" + temprcii.getValue()
											+ e.getMessage());
									e.printStackTrace();
								}
							}
							// �����ǰ�к���û����չǰ���е��к���ͬ������ԭʼ��Ԫ��
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
							} else {// ������չ��Ԫ��Ϊԭʼ��Ԫ�������չ��Ԫ��
								List<ReportCellInstanceInfo> rciiExpanList = allCellsMap
										.get(temprci.getRowIndex()).get(
												temprci.getColIndex() - 1)
										.getRciiExpandList();
								// �����չ��Ԫ���б�Ϊ�����´���һ����չ��Ԫ���б�
								if (rciiExpanList == null) {
									rciiExpanList = new ArrayList();
									allCellsMap.get(temprci.getRowIndex()).get(
											temprci.getColIndex() - 1)
											.setRciiExpandList(rciiExpanList);
								}
								// ��ԭʼ��Ԫ�����չ��Ԫ���б�����ӵ�ǰ��Ԫ��
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
							// Ϊ���б���Ԫ��ǰ����ӵ�ǰ��Ԫ��
							allInstanceCellsMap.get(rowIndex).add(temprcii);
							String topXY = temprci.getTopMain();
							/**
							 * �����˫����չ����������չ
							 */
							if (topXY != null && topXY.length() > 0) {
								temprcii.setHaveTopMainCell(true);
								ReportCellInstanceInfo topMainCellInstance = getSourceCellByString(topXY);
								if (topMainCellInstance.getRciiExpandList() != null
										&& topMainCellInstance
												.getRciiExpandList().size() > 0) {
									for (int xei = 1; xei <= topMainCellInstance
											.getRciiExpandList().size(); xei++) {
										// ���ɵ�Ԫ��ʵ��
										ReportCellInstanceInfo temprciiexi = new ReportCellInstanceInfo();
										// ����ģ�嵥Ԫ��
										ReportCellInfo temprciexi = unionRowMap
												.get(unionRowi + 1).get(
														tempCellIndex);
										temprciiexi.setRowspan(temprciexi
												.getRowspan());// ��������
										temprciiexi.setColspan(temprciexi
												.getColspan());// ��������
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
																.get("WIDTH"));// ���õ�Ԫ��Ŀ��
										temprciiexi
												.setHeigth((String) unionRowInfoMap
														.get(unionRowi + 1)
														.get("HEIGHT"));// ���õ�Ԫ��ĸ߶�
										temprciiexi.setStyle(temprciexi
												.getStyle());
										// ��չǰԴ����
										temprciiexi
												.setCellCoordinate(new ReportCellCoordinate(
														temprciexi
																.getColIndex(),
														temprciexi
																.getRowIndex()));// ����ԭʼ����
										// if (expandRowIndex > 0) {
										// ������չ����
										temprciiexi
												.setExpandCellCoordinate(new ReportCellCoordinate(
														xei, expandRowIndex));
										// }
										// ���õ�ǰ��Ԫ���ԭʼֵ
										temprciiexi.setValue(temprciexi
												.getValue());
										// �õ�ԭʼ�ĵ���¼�
										temprciiexi.setClickEvent(temprciexi
												.getClickEvent());
										// ʹ�����ݼ���������ͬʱ
										if (mainCellDataSetName
												.equals(getDataSetName(temprciexi
														.getValue()))) {
											// ���㵱ǰ��Ԫ���ʵ��ֵ�����Ե�ǰ������ӳ������滻
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
											// ���㵱ǰ��Ԫ���ʵ��ֵ
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
										// �����չ��Ԫ���б�Ϊ�����´���һ����չ��Ԫ���б�
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
										// ��ԭʼ��Ԫ�����չ��Ԫ���б�����ӵ�ǰ��Ԫ��
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
										// Ϊ���б���Ԫ��ǰ����ӵ�ǰ��Ԫ��
										allInstanceCellsMap.get(rowIndex).add(
												temprciiexi);
									}
								}
							}
						}
						expandRowIndex++;// ��չ��++
						rowIndex++;// ������++
					}
				}
				rowi = rowi + mainCell.getRowspan() - 1;// ��Ӻϲ���Ԫ�������
			} else if (mainCell != null
					&& mainCell.getExpandDirection().equals("2")) {// ����չ������������չʱִ�����²���
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
				// ��ǰ������������
				for (int tempCellIndex : tempRowMap.keySet()) {

					ReportCellInfo temprci = tempRowMap.get(tempCellIndex);
					int expandCowIndex = 0;
					// ����Ԫ��͵�ǰ��Ԫ����к���ͬʱ
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
							temprcii.setRowspan(temprci.getRowspan());// ��������
							temprcii.setColspan(temprci.getColspan());// ��������
							temprcii.setExpandDirection(temprci
									.getExpandDirection());// ��չ����
							temprcii.setStyle(temprci.getStyle());
							temprcii.setWidth(colList.get(tempCellIndex - 1)
									.get("WIDTH") == null ? "" : colList.get(
									tempCellIndex - 1).get("WIDTH"));
							temprcii.setHeigth((String) rowInfo.get("HEIGHT"));
							// ��չǰԴ����
							temprcii
									.setCellCoordinate(new ReportCellCoordinate(
											temprci.getColIndex(), temprci
													.getRowIndex()));
							if (expandCowIndex > 0) {
								// ��չ����
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
						temprcii.setValue(temprci.getValue());// ����ԭʼֵ
						temprcii.setClickEvent(temprci.getClickEvent());// ����ԭʼ����¼�
						temprcii.setCellCoordinate(new ReportCellCoordinate(
								temprci.getColIndex(), temprci.getRowIndex()));
						temprcii.setValue(String.valueOf(countCellValue(
								temprcii, CoutType.CELLVALUE)));// ������ʾֵ
						temprcii.setClickEvent(String.valueOf(countCellValue(
								temprcii, CoutType.CLICKVALUE)));// ���õ���¼�
						temprcii.setRowspan(temprci.getRowspan());// ��������
						temprcii.setColspan(temprci.getColspan());// ��������
						temprcii.setExpandDirection(temprci
								.getExpandDirection());// ��չ����
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

			} else {// ��û����չ��Ԫ��ʱֻ���ص�ǰ����
				allInstanceCellsMap.put(rowIndex, new ArrayList());
				for (int tempCellIndex : tempRowMap.keySet()) {
					ReportCellInfo temprci = tempRowMap.get(tempCellIndex);
					ReportCellInstanceInfo temprcii = new ReportCellInstanceInfo();
					temprcii.setValue(temprci.getValue());
					temprcii.setCellCoordinate(new ReportCellCoordinate(temprci
							.getColIndex(), temprci.getRowIndex()));
					temprcii.setValue(String.valueOf(countCellValue(temprcii,
							CoutType.CELLVALUE)));// ������ʾֵ
					temprcii.setClickEvent(String.valueOf(countCellValue(
							temprcii, CoutType.CLICKVALUE)));// ���õ����¼�
					temprcii.setDataType(temprci.getDataType());// ��������
					temprcii.setFormat(temprci.getFormat());// ������ʾ��ʽ
					// ��������ֵ�ʱ��������ݸ�ʽ��
					if (temprci.getDataType() != null
							&& temprci.getDataType().equals("����")
							&& temprci.getFormat() != null) {
						String value = temprcii.getValue();
						value = Format.doFormat(Format.type.t_number, value,
								temprci.getFormat());
						temprcii.setValue(value);
					}
					temprcii.setRowspan(temprci.getRowspan());// ��������
					temprcii.setColspan(temprci.getColspan());// ��������
					temprcii.setExpandDirection(temprci.getExpandDirection());// ��չ����
					// ���ÿ��
					temprcii.setWidth(colList.get(tempCellIndex - 1).get(
							"WIDTH") == null ? "" : colList.get(
							tempCellIndex - 1).get("WIDTH"));
					// ���ø߶�
					temprcii.setHeigth((String) rowInfo.get("HEIGHT"));
					// ������ʽ��
					temprcii.setStyle(temprci.getStyle());
					temprcii
							.setRciiExpandList(new ArrayList<ReportCellInstanceInfo>());
					allInstanceCellsMap.get(rowIndex).add(temprcii);
					allCellsMap.get(temprci.getRowIndex()).add(temprcii);
					/**
					 * ������������ʱ�����������չ�Զ���չ
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
										&& temprci.getDataType().equals("����")) {
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
								&& temprci.getDataType().equals("����")) {
							value = Format.doFormat(Format.type.t_number,
									value, temprci.getFormat());
						}
						temprcii.setValue(value);
					}
				}
				rowIndex++;
			}
		}
		for (int i = 1; i <= allInstanceCellsMap.size(); i++) {// �������
			int rowi = 0;
			Element trElement = tableElement.addElement("tr");
			List<ReportCellInstanceInfo> resultRowList = allInstanceCellsMap
					.get(i);
			/**
			 * ����������е�Ԫ��
			 */
			for (int j = 0; j < resultRowList.size(); j++) {
				if (j == 0) {// ʹ��δ��չǰ������
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
				// �����ʽ
				if (cellinstance.getStyle() != null
						&& cellinstance.getStyle().length() > 0&&!cellinstance.getValue().equals("0")) {
					tdElement.addAttribute("style", String.valueOf(cellinstance
							.getStyle()));
				}
				// ��ӵ����¼�
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
				 * �жϺ�ʱ����
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
			 * �������ʽ
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
	 * ��������ʽ��
	 * 
	 * @param tableElement
	 */
	private void tableSetStyle(Element tableElement) {
		tableElement.addAttribute("border", "1");
		tableElement.addAttribute("class", "report_t");
	}

	/**
	 * �����������ʽ��
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
	 * ��ͷ�����ʽ��
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
	 * ���㵥Ԫ���ֵ
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
			try {// ҳ���������ʱ���б��ʽ����
				oldString = String.valueOf(runner.execute(oldString, null,
						false, null));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return oldString;
		}
		// �����Ԫ������ϼ���
		p = Pattern.compile("sum\\(\\w*\\d*\\{\\}\\)");
		m = p.matcher(oldString);
		b = m.find();
		if (b) {
			double result = 0;
			String tempSub = oldString.substring(4, oldString.length() - 3);
			// �ַ�������ת��Ϊ��������
			ReportCellCoordinate temprcc = str2rcc(tempSub);
			ReportCellInstanceInfo sourceCell = getSourceCellByCoordinate(temprcc);
			// ԭ���������չ��
			List<ReportCellInstanceInfo> expandCells = sourceCell
					.getRciiExpandList();
			// ���ΪԴ���ʱ�����ó�ʼֵ
			result = Double.valueOf(sourceCell.getValue()==null||sourceCell.getValue().equals("")?"0":sourceCell.getValue());
			// �����ǰ����Դ���ʱ�����ó�ʼֵΪ0
			if (cellinstance.getExpandCellCoordinate() != null) {
				result = 0;
			}
			if (expandCells != null)
				for (ReportCellInstanceInfo rcii : expandCells) {
					String tempValue = rcii.getValue();
					if (tempValue == null || tempValue.equals("")) {
					} else {
						// ��ǰ�������չ��
						if (cellinstance.getExpandCellCoordinate() != null
								&& rcii.getExpandCellCoordinate() != null) {

							if (sourceCell.isHaveTopMainCell()
									&& sourceCell.getExpandDirection().equals(
											"2")) {
								result += Double.valueOf(tempValue);
							} else {
								// �͵�ǰ����ͬһ�е���չ��
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
						//���Դ����������չ�ģ�������ֵͳ��
						if (sourceCell.getExpandDirection().equals("2")) {
							result += Double.valueOf(tempValue);
						}
					}
				}
			oldString = String.valueOf(result);
			return result;
		}
		// ���ݼ�ͳ������
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
					// ��������չ���ʱ���õ�ǰ���ֵ�滻
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
					} else {// ������չ���ʱ������չ���ֵ�����滻
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
		// ���ݼ�ͳ������
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
					// ��������չ���ʱ���õ�ǰ���ֵ�滻
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
					} else {// ������չ���ʱ������չ���ֵ�����滻
						boolean flag = true;// �Ƿ�Ϊ��չǰԭ��
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
									// �������չǰ��һ��Ҳ�����˲�ȥ��һ��
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
		// �滻������������Ԫ��ֵ
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
			// ��������չ���ʱ���õ�ǰ���ֵ�滻
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
			} else {// ������չ���ʱ������չ���ֵ�����滻
				boolean flag = true;// �Ƿ�Ϊ��չǰԭ��
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
							// �������չǰ��һ��Ҳ�����˲�ȥ��һ��
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
	 * ����ת�� ��������ڷ���(-1,-1)
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
	 * ͨ��Ԫ������õ�Ԫ��
	 * 
	 * @param rcc
	 * @return
	 */
	ReportCellInstanceInfo getSourceCellByCoordinate(ReportCellCoordinate rcc) {
		List<ReportCellInstanceInfo> rowcii = allCellsMap.get(rcc.getY());
		ReportCellInstanceInfo sourceCell = new ReportCellInstanceInfo();
		if(rowcii!=null&&rowcii.size()>0){
			sourceCell = rowcii.get(rcc.getX() - 1);// List������0��ʼ
		}
		return sourceCell;
	}

	/**
	 * ͨ��Ԫ�������ַ����õ�Ԫ��
	 * 
	 * @param strrcc
	 * @return
	 */
	ReportCellInstanceInfo getSourceCellByString(String strrcc) {
		ReportCellCoordinate rcc = str2rcc(strrcc);
		List<ReportCellInstanceInfo> rowcii = allCellsMap.get(rcc.getY());
		ReportCellInstanceInfo sourceCell = rowcii.get(rcc.getX() - 1);// List������0��ʼ
		return sourceCell;
	}

	/**
	 * ��չ��Ԫ����㵥Ԫ���ֵ
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
		// ���ݼ�����������ʾ
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

		// ���ݼ�ͳ������
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
					// ��������չ���ʱ���õ�ǰ���ֵ�滻
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
					} else {// ������չ���ʱ������չ���ֵ�����滻
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
		// ���ݼ�����ֱ����ʾ
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
			// �ж���滻ֵ���ڵ�ʱ����еݹ����
			oldString = countCellValue(cellinstance, valueMap,
					CoutType.CELLVALUE);
		}
		return oldString;
	}

	/**
	 * �õ���Ԫ��ʹ�õ�����Դ������
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
	 * �õ����ݼ�
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
	 * ��Ԫ�����ݸ�ʽ��
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
		return "û�з��������ļ�¼��";
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
