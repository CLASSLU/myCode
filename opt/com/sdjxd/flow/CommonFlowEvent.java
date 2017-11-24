package com.sdjxd.flow;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sdjxd.pms.development.pagenum.service.Pagenum;
import com.sdjxd.pms.platform.base.DataModify;
import com.sdjxd.pms.platform.form.service.FormInstance;
import com.sdjxd.pms.platform.organize.User;
import com.sdjxd.pms.platform.workflow.model.FlowOperatorBean;
import com.sdjxd.pms.platform.workflow.service.Flow;
import com.sdjxd.pms.platform.workflow.service.FlowActor;
import com.sdjxd.pms.platform.workflow.service.FlowFormInstance;
import com.sdjxd.pms.platform.workflow.service.FlowInstance;
import com.sdjxd.pms.platform.workflow.service.FlowNode;
import com.sdjxd.pms.platform.workflow.service.FlowNodeEnd;
import com.sdjxd.pms.platform.workflow.service.FlowNodeInstance;
import com.sdjxd.pms.platform.workflow.service.FlowNodeStart;
import com.sdjxd.pms.platform.workflow.service.FlowOperator;

public class CommonFlowEvent {
	private static Dao dao = new Dao();

	/**
	 * 保存当前创建流程启动信息到流程审核表中
	 * 
	 * @param flowNodeInstance
	 * @param arg
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean saveStartLog(FlowNodeInstance flowNodeInstance,
			Object arg) {
		try {
			// 流程实例不为空
			if (flowNodeInstance != null) {

				String flowInstanceId = flowNodeInstance.getFlowInstanceId();
				// 流程实例
				FlowInstance flowInstance = FlowInstance
						.loadFromCache(flowInstanceId);
				// 流程
				Flow flow = flowInstance.getTemplet();
				// 流程结点ID
				int flowNodeId = flowNodeInstance.getFlowNodeId();
				// 流程结点
				FlowNode flowNode = flow.getNodeById(flowNodeId);
				// 默认票面
				int formId = flowNode.getDefaultForm();
				// 流程表单实例
				FlowFormInstance flowFormInstance = flowInstance
						.getForm(formId);
				// 表单实例ID
				String formInstanceId = flowFormInstance.getInstanceId();
				boolean start = flowNode instanceof FlowNodeStart;
				int runStatus = flowNodeInstance.getRunStatus();
				boolean isComplete = FlowInstance.RUNSTATUS_COMPLET == runStatus;
				FlowNodeInstance lastNodeInstance = flowInstance
						.getLastNodeInstance();
				boolean isUntreadNode = dao.isUntreadNode(flowInstanceId,
						String.valueOf(flowNode.getId()));
				if (start && isComplete) {
					User curruser = User.getCurrentUser();
					Map receivermap = lastNodeInstance.getOperatorManager()
							.getReceivers();
					Iterator it = receivermap.keySet().iterator();
					while (it.hasNext()) {
						FlowOperator fob = (FlowOperator) receivermap.get(it
								.next());
						dao.saveStartLog(formInstanceId, curruser, fob,
								isUntreadNode);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 通用生成页号，要求页号字段为 PAGENUM
	 * 
	 * @param flowNodeInstance
	 *            流程结点实例
	 * @param arg
	 * @return
	 * @throws Exception
	 */
	public static boolean createPageNum(FlowNodeInstance flowNodeInstance,
			Object arg) throws Exception {
		// 流程实例不为空
		if (flowNodeInstance != null) {
			String flowInstanceId = flowNodeInstance.getFlowInstanceId();
			// 流程实例
			FlowInstance flowInstance = FlowInstance
					.loadFromCache(flowInstanceId);
			// 流程
			Flow flow = flowInstance.getTemplet();
			// 流程结点ID
			int flowNodeId = flowNodeInstance.getFlowNodeId();
			// 流程结点
			FlowNode flowNode = flow.getNodeById(flowNodeId);
			// 默认票面
			int formId = flowNode.getDefaultForm();
			// 流程表单实例
			FlowFormInstance flowFormInstance = flowInstance.getForm(formId);
			// 表单实例ID
			String formInstanceId = flowFormInstance.getInstanceId();
			// 表单模板ID
			String patternId = flow.getForm(formId).getFormPatternId();
			// FormInstance formInstance =
			// FormInstance.loadFromCache(formInstanceId);
			FormInstance formInstance = FormInstance.load(formInstanceId,
					patternId);
			if (formInstance.getIndex().get("PAGENUM") == null
					|| formInstance.getIndex().get("PAGENUM").equals("")) {
				String pagenum = Pagenum.create(formInstanceId);
				// 在票面元件上绑定生成的页号
				formInstance.addIndex("PAGENUM", pagenum);
			}
			return true;
		} else {
			return false;
		}
	}
}
